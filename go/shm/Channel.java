package go.shm;

import go.Direction;
import go.Observer;

import java.util.Map;
import java.util.HashMap;

public class Channel<T> implements go.Channel<T> {
    private String name;
    private T message;
    private boolean isMessageAvailable; // true if message is available to read false otherwise
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
        notifyAll();
    }
    
    public synchronized T in() {
        while (!isMessageAvailable) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null; // or handle differently
            }
        }
        T message = this.message;
        isMessageAvailable = false;
        notifyAll();
        return message;
    }

    public String getName() {
        return name;
    }

    public void observe(Direction dir, Observer observer) {
        observers.put(dir, observer);
    }
        
}
