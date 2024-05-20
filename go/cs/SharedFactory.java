package go.cs;

import go.Direction;
import log.Logger;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SharedFactory extends UnicastRemoteObject implements RemoteFactory {

    private Map<String, Channel> channels = new HashMap<>();

    protected SharedFactory() throws RemoteException {
    }

    /** Création ou accès à un canal existant.
     * Côté serveur, le canal est créé au premier appel avec un nom donné ;
     * les appels suivants avec le même nom donneront accès au même canal.
     */
    public <T> go.Channel<T> newChannel(String name) throws RemoteException {
        go.Channel<T> c = channels.computeIfAbsent(name, k -> new Channel<>(name));
        Logger.log("SharedFactory: newChannel " + c);
        return c;
    }

    /** Spécifie quels sont les canaux écoutés et la direction pour chacun. */
    public go.Selector newSelector(Map<go.Channel, Direction> channels) throws RemoteException {
        // TODO
        return null;
    }

    /** Spécifie quels sont les canaux écoutés et la même direction pour tous. */
    public go.Selector newSelector(Set<go.Channel> channels, Direction direction) throws RemoteException {
        return newSelector(channels
                .stream()
                .collect(Collectors.toMap(Function.identity(), e -> direction)));
    }
}
