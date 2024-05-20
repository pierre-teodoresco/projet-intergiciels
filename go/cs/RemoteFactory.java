package go.cs;

import go.Channel;
import go.Direction;
import go.Selector;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.Set;

public interface RemoteFactory extends Remote {
    /** Création ou accès à un canal existant. */
    public <T> go.Channel<T> newChannel(String name) throws RemoteException;

    /** Spécifie quels sont les canaux écoutés et la direction pour chacun. */
    public go.Selector newSelector(Map<go.Channel, Direction> channels) throws RemoteException;

    /** Spécifie quels sont les canaux écoutés et la même direction pour tous. */
    public Selector newSelector(Set<Channel> channels, Direction direction) throws RemoteException;

}
