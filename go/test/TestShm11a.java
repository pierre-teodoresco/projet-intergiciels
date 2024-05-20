package go.test;

import go.Direction;
import go.*;
import log.Logger;

/* select in */
public class TestShm11a {

    private static void quit(String msg) {
        System.out.println("TestShm11a: " + msg);
        System.exit(msg.equals("ok") ? 0 : 1);
    }

    public static void main(String[] a) {
        Logger.setDebug(false);

        Factory factory = new go.shm.Factory();
        Channel<Integer> c1 = factory.newChannel("c1");

        Selector s = factory.newSelector(java.util.Set.of(c1), Direction.In); 
        new Thread(() -> {
            try { Thread.sleep(2000);  } catch (InterruptedException e) { }
            quit("KO (deadlock)");
        }).start();
        
        new Thread(() -> {
            try { Thread.sleep(200);  } catch (InterruptedException e) { }
            c1.out(4);
        }).start();

        new Thread(() -> {
            @SuppressWarnings("unchecked")
            Channel<Integer> c = s.select();
            int v = c.in();
            if (v != 4) quit("KO");

            quit("ok");
        }).start();
    }
}
