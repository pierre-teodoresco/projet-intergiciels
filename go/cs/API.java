package go.cs;

import go.Direction;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface API extends Remote {
    /**
     * Création ou accès à un canal existant.
     * @param name Nom du canal.
     */
    RemoteChannel newChannel(String name) throws RemoteException;

    /**
     * Réveille le client lorsqu'un message est reçu sur le canal.
     * @param channelName Nom du canal.
     * @param direction Direction du canal.
     * @param cb Callback à appeler lorsqu'un message est reçu.
     */
    void wakeMeUp(String channelName, Direction direction, Callback cb) throws RemoteException;
}
