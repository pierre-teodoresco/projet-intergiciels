package go.cs;

import go.Direction;
import log.Logger;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class ServerAPI extends UnicastRemoteObject implements API {

    public static HashMap<String, RemoteChannel> channels = new HashMap<>();

    public ServerAPI() throws RemoteException {}

    public RemoteChannel newChannel(String name) throws RemoteException {
        if (!channels.containsKey(name)) {
            channels.put(name, new ServerRemoteChannel(name));
        }
        return channels.get(name);
    }

    public RemoteSelector newSelector(Map<String, Direction> channels) throws RemoteException {
        return new ServerRemoteSelector(channels);
    }

    public void wakeMeUp(String channelName, Direction direction, Callback cb) throws RemoteException {
        RemoteChannel channel = channels.get(channelName);
        if (channel == null) {
            // Should not happen
            throw new RemoteException("Channel not found");
        }
        channel.observe(direction, () -> {
           try {
               Logger.info("Waking up client");
               cb.wakeUp();
           } catch (RemoteException e) {
               Logger.error("Error while waking up client", e);
           }
        });
    }
}
