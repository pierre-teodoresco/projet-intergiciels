package go.test;

import go.Channel;
import go.Direction;
import go.Factory;
import go.Selector;
import log.Logger;

import java.util.Set;

/** Un unique in/out, ici in */
public class TestCS22a {

    private static void quit(String msg) {
        System.out.println("TestCS22a: " + msg);
        System.exit(msg.equals("ok") ? 0 : 1);
    }

    public static void main(String[] a) {
        Logger.setDebug(true);

        Factory factory = new go.cs.Factory();
        Channel<Integer> c1 = factory.newChannel("c1");
        Channel<Integer> c2 = factory.newChannel("c2");
        Selector selector = factory.newSelector(Set.of(c1, c2), Direction.In);

        new Thread(() -> {
                try { Thread.sleep(5000);  } catch (InterruptedException e) { }
                quit("KO (deadlock)");
        }).start();

        Logger.info("Client Selecting...");
        Channel<Integer> c = selector.select();
        Logger.info("Client in...");
        int v = c.in();
        quit(v == 4 ? "ok" : "KO");
    }
}
