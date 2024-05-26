package go.test;

import go.Channel;
import go.Direction;
import go.Factory;
import go.Selector;
import log.Logger;

import java.util.Set;

/** Un unique in/out, ici in */
public class TestCS22b {

    private static void quit(String msg) {
        System.out.println("TestCS22b: " + msg);
        System.exit(msg.equals("ok") ? 0 : 1);
    }

    public static void main(String[] a) {
        Logger.setDebug(true);

        Factory factory = new go.cs.Factory();
        Channel<Integer> c2 = factory.newChannel("c2");

        new Thread(() -> {
                try { Thread.sleep(5000);  } catch (InterruptedException e) { }
                quit("KO (deadlock)");
        }).start();

        Logger.info("Client out...");
        c2.out(4);
        quit("ok");
    }
}
