package go.cs;

import go.Direction;
import go.Observer;

// go.Channel<T> is already serialized

public class Channel<T> implements go.Channel<T> {

    private RemoteChannel<T> channel;

    public Channel(String name) {
        try {
            channel = new SharedChannel<>(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void out(T v) {
        try {
            channel.out(v);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public T in() {
        try {
            return channel.in();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getName() {
        try {
            return channel.getName();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void observe(Direction direction, Observer observer) {
        try {
            channel.observe(direction, observer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
