package go.cs;

import go.Direction;
import go.Observer;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Channel<T> implements go.Channel<T> {

    private RemoteChannel<T> channel;

    public Channel(String name) {
        try {
            channel = new ServerRemoteChannel<>(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Channel(RemoteChannel<T> channel) {
        this.channel = channel;
    }

    public void out(T v) {
        try {
            channel.out(v);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public T in() {
        try {
            return channel.in();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getName() {
        try {
            return channel.getName();
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
            api.wakeMeUp(channel.getName(), direction, clientCallback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
