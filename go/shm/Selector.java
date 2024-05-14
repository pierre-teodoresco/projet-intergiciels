package go.shm;

import go.Direction;
import go.Channel;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.Semaphore;

public class Selector implements go.Selector {
    private final Map<Channel, Direction> channels;
    private Semaphore sem = new Semaphore(0);

    public Selector(Map<Channel, Direction> channels) {
        this.channels = channels;
    }

    public Channel select() {
        ArrayList<Channel> ch = new ArrayList<>();

        channels.forEach((channel, direction) -> {
            channel.observe(Direction.inverse(direction), () -> {
                ch.add(channel);
                sem.release();
            });
        });

        try {
            // wait for a channel to be updated
            sem.acquire();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // return the first channel updated
        return ch.get(0);
    }
}


