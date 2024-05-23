package go.cs;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Callback extends Remote {
    void wakeUp() throws RemoteException;
}
