package go.shm;

import go.Direction;
import go.Channel;
import go.Observer;
import log.Logger;

import java.util.*;
import java.util.concurrent.Semaphore;

public class Selector implements go.Selector {
    private final Map<Channel, Direction> channels;
    private Semaphore sem = new Semaphore(0);
    private List<Channel> ch = new ArrayList<>();
    private Map<Channel, Observer> obs = new HashMap<>();

    public Selector(Map<Channel, Direction> channels) {
        this.channels = channels;
        this.channels.forEach((channel, direction) -> {
            obs.put(channel, new SelectorObserver(channel));
        });
    }

    public Channel select() {
        channels.forEach((channel, direction) -> {
            channel.observe(Direction.inverse(direction), obs.get(channel));
        });

        try {
            // wait for a channel to be updated
            Logger.log("Waiting for a channel to be updated...");
            sem.acquire();
            Logger.log("Semaphore released, selecting channel...");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // return the first channel updated
        Channel c = ch.getFirst();
        Logger.log("Selected channel: " + c.getName());
        return c;
    }

    private class SelectorObserver implements go.Observer {
        private final Channel channel;

        public SelectorObserver(Channel channel) {
            this.channel = channel;
        }

        public void update() {
            Logger.log("Channel " + channel.getName() + " updated in selector");
            ch.add(channel);
            sem.release();
            Logger.log("sem has " + sem.availablePermits() + " token(s) available");
        }
    }
}


