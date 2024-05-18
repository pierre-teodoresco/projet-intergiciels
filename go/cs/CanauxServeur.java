import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import go.Channel;

public interface CanauxServeur<T> extends Remote {
    public void ecrire(go.Channel c, T message) throws RemoteException;
    public T lire(go.Channel c) throws RemoteException;
    public void ajouterChannel(go.Channel c) throws RemoteException;
    public List<go.Channel> getCanaux();
}