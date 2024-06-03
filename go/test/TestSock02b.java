package go.test;

import go.Channel;
import go.Factory;
import log.Logger;

/** Un unique in/out, ici out */
public class TestSock02b {

    private static void quit(String msg) {
        System.out.println("TestSock01b: " + msg);
        System.exit(msg.equals("ok") ? 0 : 1);
    }

    public static void main(String[] a) throws InterruptedException {
        Logger.setDebug(true);

        Factory factory = new go.sock.Factory();
        Channel<Channel<Integer>> c = factory.newChannel("c");

        new Thread(() -> {
                try { Thread.sleep(5000);  } catch (InterruptedException e) { }
                quit("KO (deadlock)");
        }).start();

        Channel<Integer> c1 = c.in();
        c1.out(42);
        // Sleep to avoid quiting to early and kill the socket
        Thread.sleep(10);
        quit("ok");
    }
}

