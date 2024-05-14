package go.shm;

import go.Direction;
import go.Observer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Iterator;

public class Channel<T> implements go.Channel<T> {
    private final String name;
    private T message;
    private boolean isEmpty, isInReady, isOutReady;
    private final Map<Direction, List<Observer>> observers;

    public Channel(String name) {
        this.name = name;
        isEmpty = true;
        isInReady = false;
        isOutReady = false;
        observers = new HashMap<>();
        observers.put(Direction.In, new ArrayList<>());
        observers.put(Direction.Out, new ArrayList<>());
    }

    public synchronized void out(T v) {
        // Remplir le canal
        message = v;
        isEmpty = false;

        // Notifier que le canal est plein
        notifyAll();

        // Attendre que quelqu'un vide le canal et prévenir les observateurs
        isInReady = true;
        updateObservers(Direction.Out);
        while (!isEmpty) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        isInReady = false;
    }

    public synchronized T in() {
        // Attendre que quelqu'un vide le canal et prévenir les observateurs
        isOutReady = true;
        updateObservers(Direction.In);
        while (isEmpty) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        isOutReady = false;

        // Vider le canal
        T v = message;
        isEmpty = true;

        // Notifier que le canal est vide
        notifyAll();

        return v;
    }

    public String getName() {
        return name;
    }

    public void observe(Direction dir, Observer observer) {
        observers.get(dir).add(observer);
    }

    private void updateObservers(Direction dir) {
        Iterator<Observer> it = observers.get(dir).iterator();
        while (it.hasNext()) {
            it.next().update();
            it.remove();
        }
    }

    public boolean isReady(Direction direction) {
        switch (direction) {
            case In : return isInReady;
            case Out: return isOutReady;
            default: return false; // pfeuh, compilateur incompétent
        }
    }
}