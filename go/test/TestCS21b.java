package go.test;

import go.Channel;
import go.Direction;
import go.Factory;
import log.Logger;

/** Un unique in/out, ici in */
public class TestCS21b {

    private static void quit(String msg) {
        System.out.println("TestCS21b: " + msg);
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
        c.observe(Direction.In, () -> Logger.info("Client notified (in)"));
        Logger.info("Client out...");
        c.out(4);
        quit("ok");
    }
}
