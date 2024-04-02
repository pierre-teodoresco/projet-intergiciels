package go.test;

import go.Channel;
import go.Factory;

/** in | out */
public class TestShm02 {

    private static void quit(String msg) {
        System.out.println("TestShm04: " + msg);
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
                try { Thread.sleep(100);  } catch (InterruptedException e) { }
                c.out(4);
        }).start();

        new Thread(() -> {
                int v = c.in();
                quit(v == 4 ? "ok" : "KO");
        }).start();


    }
}
