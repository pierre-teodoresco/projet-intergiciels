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
            Logger.log("Début out sur " + name + "...");
            writing = true;
            message = v;
            Logger.log("Update observers...");
            updateObservers(Direction.Out);
        }
        semOut.release();
        try {
            Logger.log("Channel " + name + " waiting for in (sauf si déjà un in dans le channel)...");
            semIn.acquire();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        synchronized(this) {
            writing = false;
        }
        Logger.log("Fin out sur " + name + "...");
    }
    
    public T in() {
        synchronized(this) {
            Logger.log("Début in sur " + name + "...");
            reading = true;
            updateObservers(Direction.In);
        }
        try {
            Logger.log("Channel " + name + " waiting for out (sauf si déjà un out dans le channel)...");
            semOut.acquire();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        semIn.release();
        synchronized(this) {
            reading = false;
            Logger.log("Fin in sur " + name + "...");
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
                observers.get(dir).add(observer);
            }
        }
    }

    private void updateObservers(Direction dir) {
        Iterator<Observer> it = observers.get(dir).iterator();
        while (it.hasNext()) {
            it.next().update();
            it.remove();
        }
    }
}