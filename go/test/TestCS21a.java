package go.test;

import go.Channel;
import go.Direction;
import go.Factory;
import go.Observer;
import log.Logger;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/** Un unique in/out, ici in */
public class TestCS21a {

    private static void quit(String msg) {
        System.out.println("TestCS21a: " + msg);
        System.exit(msg.equals("ok") ? 0 : 1);
    }

    public static void main(String[] a) {
        Logger.setDebug(true);

        Factory factory = new go.cs.Factory();
        Channel<Integer> c = factory.newChannel("c");

        new Thread(() -> {
                try { Thread.sleep(100000);  } catch (InterruptedException e) { }
                quit("KO (deadlock)");
        }).start();

        Logger.log("Client observing...");
        c.observe(Direction.Out, () -> {
            Logger.log("Client callback updated");
            Logger.log("Client in...");
            int v = c.in();
            quit(v == 4 ? "ok" : "KO");
        });
    }
}
