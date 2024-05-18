package go.cs;

import go.Direction;
import go.Observer;

// go.Channel<T> is already serialized

public class Channel<T> implements go.Channel<T>{

    private final String name;

    public Channel(String name) {
        // TODO
        this.name = name;
    }

    public void out(T v) {
        // TODO
    }
    
    public T in() {
        // TODO
        return null;
    }

    public String getName() {
        return name;
    }

    public void observe(Direction direction, Observer observer) {
        // TODO
    }

    public boolean isReady(Direction direction) {
        // TODO
        return false;
    }
}
