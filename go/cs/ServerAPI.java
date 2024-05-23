package go.cs;

import go.Direction;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class ServerAPI extends UnicastRemoteObject implements API {

    private HashMap<String, RemoteChannel> channels = new HashMap<>();

    public ServerAPI() throws RemoteException {}

    public RemoteChannel newChannel(String name) throws RemoteException {
        if (!channels.containsKey(name)) {
            channels.put(name, new ServerRemoteChannel(name));
        }
        return channels.get(name);
    }

    public void wakeMeUp(String channelName, Direction direction, Callback cb) throws RemoteException {
        RemoteChannel channel = channels.get(channelName);
        if (channel == null) {
            // Should not happen
            throw new RemoteException("Channel not found");
        }
        channel.observe(direction, () -> {
            try {
                cb.wakeUp();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }
}
