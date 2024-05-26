package go.test;

import go.Channel;
import go.Direction;
import go.Factory;
import log.Logger;

/** Un unique in/out, ici in */
public class TestCS21a {

    private static void quit(String msg) {
        System.out.println("TestCS21a: " + msg);
        System.exit(msg.equals("ok") ? 0 : 1);
    }

    public static void main(String[] a) {
        Logger.setDebug(true);

        Factory factory = new go.cs.Factory();
        Channel<Integer> c = factory.newChannel("c");

        new Thread(() -> {
                try { Thread.sleep(5000);  } catch (InterruptedException e) { }
                quit("KO (deadlock)");
        }).start();

        Logger.info("Client observing...");
        c.observe(Direction.Out, () -> Logger.info("Client notified (out)"));
        Logger.info("Client in...");
        int v = c.in();
        quit(v == 4 ? "ok" : "KO");
    }
}
