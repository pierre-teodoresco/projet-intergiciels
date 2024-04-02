package go.shm;

import go.Direction;
import go.Observer;

import java.util.Map;
import java.util.HashMap;

public class Channel<T> implements go.Channel<T> {
    private String name;
    private T message;
    private boolean isMessageAvailable; // true if message is available to read on the channel false otherwise
    private boolean isOutPending; // true if a message is pending to be written on the channel false otherwise
    private boolean isInPending; // true if a message is pending to be read from the channel false otherwise
    private Map<Direction, Observer> observers;

    public Channel(String name) {
        this.name = name;
        isMessageAvailable = false;
        observers = new HashMap<>();
    }

    public synchronized void out(T v) {
        while (isMessageAvailable) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        message = v;
        isMessageAvailable = true;
        notifyObservers(Direction.Out);
        notifyAll();
    }
    
    public synchronized T in() {
        while (!isMessageAvailable) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }
        }
        T message = this.message;
        isMessageAvailable = false;
        notifyObservers(Direction.In);
        notifyAll();
        return message;
    }

    public String getName() {
        return name;
    }

    public void observe(Direction dir, Observer observer) {
        observers.put(dir, observer);
    }

    public boolean isPending(Direction direction) {
        switch (direction) {
            case In:
                return !isMessageAvailable;
            case Out:
                return isMessageAvailable;
            default:
                return false;
        }
    }

    private void notifyObservers(Direction dir) {
        if (!observers.isEmpty() && observers.containsKey(dir)) {
            observers.get(dir).update();
        }
    }
}
