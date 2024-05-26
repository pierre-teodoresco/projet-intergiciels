package go.cs;

import go.Direction;
import go.Observer;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Channel<T> implements go.Channel<T> {

    private RemoteChannel<T> proxy;

    public Channel(String name) {
        try {
            proxy = new ServerRemoteChannel<>(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Channel(RemoteChannel<T> proxy) {
        this.proxy = proxy;
    }

    public void out(T v) {
        try {
            proxy.out(v);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public T in() {
        try {
            return proxy.in();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getName() {
        try {
            return proxy.getName();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void observe(Direction direction, Observer observer) {
        try {
            ClientCallback clientCallback = new ClientCallback(observer);
            Registry dns = LocateRegistry.getRegistry("localhost", 1099);
            API api = (API) dns.lookup("API");
            api.wakeMeUp(proxy.getName(), direction, clientCallback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
