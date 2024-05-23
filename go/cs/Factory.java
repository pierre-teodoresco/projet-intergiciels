package go.cs;

import go.Direction;
import log.Logger;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Set;
import java.util.Map;

import static java.lang.System.exit;

public class Factory implements go.Factory {

    private final API proxy;

    public Factory() {
       try {
           // Se connecter au registre RMI sur le port 1099
           Registry dns = LocateRegistry.getRegistry("localhost", 1099);

           // Obtenir une référence à l'interface du serveur
           proxy = (API) dns.lookup("API");
       } catch (Exception e) {
           throw new RuntimeException(e);
       }
    }

    /** Création ou accès à un canal existant.
     * Côté serveur, le canal est créé au premier appel avec un nom donné ;
     * les appels suivants avec le même nom donneront accès au même canal.
     */
    public <T> go.Channel<T> newChannel(String name) {
        try {
            RemoteChannel<T> channel = proxy.newChannel(name);
            return new Channel<>(channel);
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /** Spécifie quels sont les canaux écoutés et la direction pour chacun. */
    public go.Selector newSelector(Map<go.Channel, Direction> channels) {
        // TODO
        return null;
    }

    /** Spécifie quels sont les canaux écoutés et la même direction pour tous. */
    public go.Selector newSelector(Set<go.Channel> channels, Direction direction) {
        // TODO
        return null;
    }
}

