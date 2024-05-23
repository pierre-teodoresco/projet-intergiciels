package go.cs;

import go.Observer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClientCallback extends UnicastRemoteObject implements Callback {

    private Observer observer;

    public ClientCallback() throws RemoteException {}

    public ClientCallback(Observer observer) throws RemoteException {
        this.observer = observer;
    }

    public void wakeUp() throws RemoteException {
        observer.update();
    }
}
