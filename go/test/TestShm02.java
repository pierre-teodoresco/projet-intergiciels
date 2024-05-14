package go.test;

import go.Channel;
import go.Factory;

public class TestShm02 {

    private static void quit(String msg) {
        System.out.println("TestShm02: " + msg);
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
            c.out(42);
        }).start();

        new Thread(() -> {
            try { Thread.sleep(100);  } catch (InterruptedException e) { }
            int v = c.in();
            if (v != 42) quit("KO");
            quit("ok");
        }).start();
    }
}
