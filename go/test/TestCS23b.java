package go.test;

import go.Channel;
import go.Factory;
import log.Logger;

/** Un unique in/out, ici in */
public class TestCS23b {

    private static void quit(String msg) {
        System.out.println("TestCS20b: " + msg);
        System.exit(msg.equals("ok") ? 0 : 1);
    }

    public static void main(String[] a) {
        Logger.setDebug(true);

        Factory factory = new go.cs.Factory();
        Channel<Channel<Integer>> c = factory.newChannel("c");

        new Thread(() -> {
                try { Thread.sleep(5000);  } catch (InterruptedException e) { }
                quit("KO (deadlock)");
        }).start();
        
        Channel<Integer> c1 = c.in();
        c1.out(4);
        quit("ok");
    }
}
