package go.shm;

import go.Direction;
import go.Channel;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

public class Selector implements go.Selector {

    private final Map<Channel, Direction> channels;

    public Selector(Map<Channel, Direction> channels) {
        this.channels = channels;
    }

    public Channel select() {
        
    }
}
