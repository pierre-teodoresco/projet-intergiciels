package go.shm;

import go.Direction;
import go.Channel;
import java.util.Map;

public class Selector implements go.Selector, go.Observer {
    private boolean isReady;
    private final Map<Channel, Direction> channels;

    public Selector(Map<Channel, Direction> channels) {
        this.channels = channels;
        isReady = false;
    }

    public Channel select() {
        synchronized(this) {
            while(true) {
                // Choisir le premier canal prêt ou observer tous les canaux
                for (Map.Entry<Channel, Direction> entry : channels.entrySet()) {
                    Channel channel = entry.getKey();
                    Direction direction = entry.getValue();
                    if (channel.isReady(direction)) {
                        return channel;
                    } else {
                        channel.observe(Direction.inverse(direction), this);
                    }
                }
                // Attendre qu'un canal soit prêt
                isReady = false;
                while (!isReady) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return null;
                    }
                }
            }
        }
    }

    public void update() {
        synchronized(this) {
            isReady = true;
            notifyAll();
        }
    }
}

