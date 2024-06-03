package go.test;

import go.Channel;
import go.Factory;
import log.Logger;

/** passage de canal en paramètre */
public class TestSock02a {

    private static void quit(String msg) {
        System.out.println("TestSock01a: " + msg);
        System.exit(msg.equals("ok") ? 0 : 1);
    }

    public static void main(String[] a) throws InterruptedException {
        Logger.setDebug(true);

        Factory factory = new go.sock.Factory();
        Channel<Channel<Integer>> c = factory.newChannel("c");
        Channel<Integer> c1 = factory.newChannel("c1");

        new Thread(() -> {
                try { Thread.sleep(10000);  } catch (InterruptedException e) { }
                quit("KO (deadlock)");
        }).start();

        c.out(c1);
        int v = c1.in();
        // Sleep to avoid quiting to early and kill the socket
        Thread.sleep(10);
        quit(v == 42 ? "ok" : "KO (bad value)");
    }
}

