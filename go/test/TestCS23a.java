package go.test;

import go.Channel;
import go.Factory;
import log.Logger;

/** passage de canal en paramètre */
public class TestCS23a {

    private static void quit(String msg) {
        System.out.println("TestCS20a: " + msg);
        System.exit(msg.equals("ok") ? 0 : 1);
    }

    public static void main(String[] a) {
        Logger.setDebug(true);

        Factory factory = new go.cs.Factory();
        Channel<Integer> c1 = factory.newChannel("c1");
        Channel<Channel<Integer>> c = factory.newChannel("c");

        new Thread(() -> {
                try { Thread.sleep(5000);  } catch (InterruptedException e) { }
                quit("KO (deadlock)");
        }).start();
        
        c.out(c1);
        int v = c1.in();
        quit(v == 4 ? "ok" : "KO");
    }
}
