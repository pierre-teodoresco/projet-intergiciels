package go.cs;

public class Selector implements go.Selector {

    private RemoteSelector proxy;

    public Selector(RemoteSelector proxy) {
        this.proxy = proxy;
    }

    public go.Channel select() {
        try {
            return new Channel(proxy.select());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
