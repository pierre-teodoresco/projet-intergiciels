package go.cs;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import go.Channel;

public class CanauxServeurImpl<T> extends UnicastRemoteObject implements CanauxServeur<T> {

    protected CanauxServeurImpl() throws RemoteException {
        // TODO
    }

    @Override
    public void ecrire(Channel c, T message) throws RemoteException {
        // TODO 
    }

    @Override
    public T lire(Channel c) throws RemoteException {
        // TODO
        return null; 
    }

    @Override
    public void ajouterChannel(Channel c) throws RemoteException {
        // TODO 
    }

    @Override
    public List<Channel> getCanaux() {
        // TODO
        return null;
    }
}
