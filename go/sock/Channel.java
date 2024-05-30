package go.sock;

import go.Direction;
import go.Observer;

public class Channel<T> implements go.Channel<T> {

    private final go.Channel<T> channel;

    public Channel(go.Channel<T> channel) {
        this.channel = channel;
    }

    public void out(T v) {
        channel.out(v);
    }

    public T in() {
        return channel.in();
    }


    public String getName() {
        return channel.getName();
    }

    // Ignore
    public void observe(Direction direction, Observer observer) {
    }
}
