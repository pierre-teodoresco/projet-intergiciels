package go.cs;

import go.Direction;
import go.Observer;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteChannel<T> extends Remote {
    /**
     * Envoi synchrone d'un message.
     * Bloque tant que le message ne peut pas être envoyé.
     */
    public void out(T v) throws RemoteException;

    /**
     * Réception synchrone d'un message.
     * Bloque tant qu'il n'y a pas de message disponible.
     */
    public T in() throws RemoteException;

    /**
     * Nom du canal.
     * On suppose que les noms sont uniques (en particulier pour client-serveur ou point-à-point).
     */
    public String getName() throws RemoteException;

    /**
     * Adds an observer to the set of observers for this channel, for the
     * specified direction. The observer is notified when a in/out operation
     * is pending, and then it is removed: an observation fires at most once.
     * If an in/out operation is already pending when observe is called, it
     * immediately fires.
     */
    public void observe(Direction direction, Observer observer) throws RemoteException;
}
