package go.cs;

import go.Direction;
import go.Observer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class SharedChannel<T> extends UnicastRemoteObject implements RemoteChannel<T> {

    // Utilise le channel a mémoire partagée
    private go.shm.Channel<T> channel;

    public SharedChannel(String name) throws RemoteException {
        channel = new go.shm.Channel<>(name);
    }

    public void out(T v) throws RemoteException {
        channel.out(v);
    }

    public T in() throws RemoteException {
        return channel.in();
    }

    public String getName() throws RemoteException {
        return channel.getName();
    }

    public void observe(Direction direction, Observer observer) throws RemoteException {
        channel.observe(direction, observer);
    }
}
