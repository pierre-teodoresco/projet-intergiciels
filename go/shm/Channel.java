package go.shm;

import go.Direction;
import go.Observer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Channel<T> implements go.Channel<T> {
    private final String name;
    private T message;
    private boolean isMessageAvailable;
    private final Map<Direction, List<Observer>> observers;

    public Channel(String name) {
        this.name = name;
        isMessageAvailable = false;
        observers = new HashMap<>();
        observers.put(Direction.In, new ArrayList<>());
        observers.put(Direction.Out, new ArrayList<>());
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
        observers.get(dir).add(observer);
    }

    private void notifyObservers(Direction dir) {
        for (Observer observer : observers.get(dir)) {
            observer.update();
        }
    }
}
