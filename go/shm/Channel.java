package go.shm;

import go.Direction;
import go.Observer;
import log.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Iterator;
import java.util.concurrent.Semaphore;

public class Channel<T> implements go.Channel<T> {
    private final String name;
    private T message;
    private boolean reading = false, writing = false;
    private final Map<Direction, List<Observer>> observers = new HashMap<>();
    private Semaphore semOut = new Semaphore(0), semIn = new Semaphore(0);

    public Channel(String name) {
        this.name = name;
        observers.put(Direction.In, new ArrayList<>());
        observers.put(Direction.Out, new ArrayList<>());
    }

    public void out(T v) {
        synchronized(this) {
            Logger.info("Beginning out on " + this + "...");
            writing = true;
            message = v;
            updateObservers(Direction.Out);
        }
        semOut.release();
        try {
            Logger.info("Channel " + this + " waiting for in...");
            semIn.acquire();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        synchronized(this) {
            writing = false;
        }
        Logger.info("Ending out on " + this);
    }
    
    public T in() {
        synchronized(this) {
            Logger.info("Beginning in on " + this + "...");
            reading = true;
            updateObservers(Direction.In);
        }
        try {
            Logger.info("Channel " + this + " waiting for out...");
            semOut.acquire();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        semIn.release();
        synchronized(this) {
            reading = false;
            Logger.info("Ending in on " + this);
            return message;
        }
    }

    public String getName() {
        return name;
    }

    public void observe(Direction dir, Observer observer) {
        if (dir == Direction.In && reading) {
            observer.update();
        } else if (dir == Direction.Out && writing) {
            observer.update();
        } else {
            if (!observers.get(dir).contains(observer)) {
                Logger.info("Adding observer for " + dir + " on " + this);
                observers.get(dir).add(observer);
                Logger.info("observe: observers of " + this + " = " + observers);
            }
        }
    }

    private void updateObservers(Direction dir) {
        Logger.info("update: observers of " + this + " = " + observers);
        Iterator<Observer> it = observers.get(dir).iterator();
        while (it.hasNext()) {
            it.next().update();
            it.remove();
        }
    }
}