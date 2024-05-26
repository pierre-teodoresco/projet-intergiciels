package go.cs;

import go.Direction;
import go.Observer;
import log.Logger;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ServerRemoteChannel<T> extends UnicastRemoteObject implements RemoteChannel<T> {

    // Utilise le channel a mémoire partagée
    private go.shm.Channel<T> channel;

    public ServerRemoteChannel() throws RemoteException {}

    public ServerRemoteChannel(String name) throws RemoteException {
        channel = new go.shm.Channel<>(name);
        Logger.info("Channel " + channel + " created");
    }

    public go.Channel<T> getShmChannel() {
        return channel;
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
