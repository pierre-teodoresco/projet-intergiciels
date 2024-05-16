package go.test;

import go.Channel;
import go.Direction;
import go.Factory;
import go.Selector;
import log.Logger;

/* select avant in */
public class TestShm12 {

    private static void quit(String msg) {
        System.out.println("TestShm12: " + msg);
        System.exit(msg.equals("ok") ? 0 : 1);
    }

    public static void main(String[] a) {
        Logger.setDebug(false);

        Factory factory = new go.shm.Factory();
        Channel<Integer> c1 = factory.newChannel("c1");

        Selector s = factory.newSelector(java.util.Map.of(c1, Direction.Out));

        new Thread(() -> {
            try { Thread.sleep(2000);  } catch (InterruptedException e) { }
            quit("KO (deadlock)");
        }).start();
        
        new Thread(() -> {
            try { Thread.sleep(100);  } catch (InterruptedException e) { }
            int v = c1.in();
            if (v != 6) quit("KO");
        }).start();

        new Thread(() -> {
            @SuppressWarnings("unchecked")
            Channel<Integer> c = s.select();
            c.out(6);
            quit("ok");
        }).start();
    }
}
