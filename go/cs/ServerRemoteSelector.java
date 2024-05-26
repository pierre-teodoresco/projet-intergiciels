package go.cs;

import go.Direction;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import java.util.HashMap;

public class ServerRemoteSelector extends UnicastRemoteObject implements RemoteSelector {

    private go.shm.Selector selector;

    public ServerRemoteSelector() throws RemoteException {}

    public ServerRemoteSelector(Map<String, Direction> channels) throws RemoteException {
        Map<go.Channel, Direction> goChannels = new HashMap<>();
        for (Map.Entry<String, Direction> entry : channels.entrySet()) {
            goChannels.put(ServerAPI.channels.get(entry.getKey()).getShmChannel(), entry.getValue());
        }
        selector = new go.shm.Selector(goChannels);
    }

    public RemoteChannel select() throws RemoteException {
        return ServerAPI.channels.get(selector.select().getName());
    }
}
