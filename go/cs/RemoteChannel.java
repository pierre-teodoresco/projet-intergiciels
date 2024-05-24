package go.cs;

import go.Direction;
import go.Observer;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteChannel<T> extends Remote {
    void out(T v) throws RemoteException;
    T in() throws RemoteException;
    String getName() throws RemoteException;
    void observe(Direction direction, Observer observer) throws RemoteException;
    go.Channel<T> getShmChannel() throws RemoteException;
}
