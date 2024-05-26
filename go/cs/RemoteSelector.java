package go.cs;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteSelector extends Remote {
    RemoteChannel select() throws RemoteException;
}
