package go.test;

import go.Channel;
import go.Direction;
import go.Factory;
import go.Observer;

/** out | (in;in) | out */
public class TestShm04 {

    private static void quit(String msg) {
        System.out.println("TestShm04: " + msg);
        System.exit(msg.equals("ok") ? 0 : 1);
    }

    public static void main(String[] a) {
        Factory factory = new go.shm.Factory();
        Channel<Integer> c = factory.newChannel("c");
        c.observe(Direction.Out, new Observer() {
                public void update() {
                    System.out.println("c.out");
                }
            }
        );

        new Thread(() -> {
            try { Thread.sleep(2000);  } catch (InterruptedException e) { }
            quit("KO (deadlock)");
        }).start();
        
        new Thread(() -> {
            c.out(4);
        }).start();

        new Thread(() -> {
            try { Thread.sleep(200);  } catch (InterruptedException e) { }
            c.out(5);
        }).start();

        new Thread(() -> {
            try { Thread.sleep(100);  } catch (InterruptedException e) { }
            int v = c.in();
            quit(v == 4 ? "ok" : "KO");
            v = c.in();
            quit(v == 5 ? "ok" : "KO");
        }).start();
        
                   
    }
}
