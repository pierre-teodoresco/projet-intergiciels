package go.test;

import go.Channel;
import go.Factory;

/** Un unique in/out, commençant par out */
public class TestShm00 {

    private static void quit(String msg) {
        System.out.println("TestShm00: " + msg);
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
            int v = c.in();
            quit(v == 4 ? "ok" : "KO");
        }).start();
    }
}
