package go.test;

import go.Channel;
import go.Factory;
import log.Logger;

public class TestShm1in2out {
    private static void quit(String msg) {
        System.out.println("TestShm1in2out: " + msg);
        System.exit(msg.equals("ok") ? 0 : 1);
    }

    public static void main(String[] a) {
        Factory factory = new go.shm.Factory();
        Channel<Integer> c = factory.newChannel("c");

        new Thread(() -> {
            try { Thread.sleep(2000);  } catch (InterruptedException e) { }
            quit("ok (deadlock)");
        }).start();

        new Thread(() -> {
            c.out(4);
        }).start();

        new Thread(() -> {
            try { Thread.sleep(100);  } catch (InterruptedException e) { }
            int v = c.in();
            quit(v == 4 || v == 5 ? "ok" : "KO");
        }).start();

        new Thread(() -> {
            c.out(5);
        }).start();
    }
}