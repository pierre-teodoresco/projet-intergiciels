package go.test;

import go.Channel;
import go.Factory;
import log.Logger;

/** Un unique in/out, ici out */
public class TestSock01b {

    private static void quit(String msg) {
        System.out.println("TestSock01b: " + msg);
        System.exit(msg.equals("ok") ? 0 : 1);
    }

    public static void main(String[] a) throws InterruptedException {
        Logger.setDebug(true);

        Factory factory = new go.sock.Factory();
        Channel<Integer> c = factory.newChannel("c");

        new Thread(() -> {
                try { Thread.sleep(10000);  } catch (InterruptedException e) { }
                quit("KO (deadlock)");
        }).start();

        int v = c.in();
        Thread.sleep(10);
        quit(v == 42 ? "ok" : "KO (bad value)");
    }
}

