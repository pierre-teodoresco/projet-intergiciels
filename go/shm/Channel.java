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
    private boolean isEmpty, writing, reading;
    private final Map<Direction, List<Observer>> observers;

    public Channel(String name) {
        this.name = name;
        isEmpty = true;
        writing = false;
        reading = false;
        observers = new HashMap<>();
        observers.put(Direction.In, new ArrayList<>());
        observers.put(Direction.Out, new ArrayList<>());
    }

    public synchronized void out(T v) {
        // Annoncer qu'on écrit
        writing = true;
        updateObservers(Direction.Out);

        // Remplir le canal
        message = v;
        isEmpty = false;

        // Notifier que le canal est plein
        notifyAll();

        // Attendre que quelqu'un vide le canal
        while (!isEmpty) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Annoncer qu'on a fini d'écrire
        writing = false;
    }

    public synchronized T in() {
        // Annoncer qu'on lit
        reading = true;
        updateObservers(Direction.In);

        // Attendre que quelqu'un vide le canal
        while (isEmpty) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Vider le canal
        T v = message;
        isEmpty = true;

        // Notifier que le canal est vide
        notifyAll();

        // Annoncer qu'on a fini de lire
        reading = false;

        return v;
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
            observers.get(dir).add(observer);
        }
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
            case In : return writing;
            case Out: return reading;
            default: return false; // pfeuh, compilateur incompétent
        }
    }
}