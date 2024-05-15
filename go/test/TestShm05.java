package go.test;

import go.Channel;
import go.Direction;
import go.Factory;
import go.Observer;

/** out | (in;in) | out */
public class TestShm05 {

    private static void quit(String msg) {
        System.out.println("TestShm05: " + msg);
        System.exit(msg.equals("ok") ? 0 : 1);
    }

    public static void main(String[] a) {
        Factory factory = new go.shm.Factory();
        Channel<Integer> c = factory.newChannel("c");

        new Thread(() -> {
            try { Thread.sleep(2000);  } catch (InterruptedException e) { }
            quit("KO (deadlock)");
        }).start();

        new Thread(() -> {
            c.out(4);
        }).start();

        new Thread(() -> {
            try { Thread.sleep(100);  } catch (InterruptedException e) { }
            c.observe(Direction.Out, () -> {
                int v = c.in();
                if (v != 4) {
                    quit("KO");
                }
            });
            quit("ok");
        }).start();
    }
}
