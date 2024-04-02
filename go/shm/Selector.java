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
        while (true) {
            for (Map.Entry<Channel, Direction> entry : channels.entrySet()) {
                Channel channel = entry.getKey();
                Direction direction = entry.getValue();

                if (channel.isPending(Direction.inverse(direction))) {
                    return channel; // return the first ready channel
                }
            }

            // little sleep to avoid busy waiting
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                // ignore
            }
        }
    }
}
