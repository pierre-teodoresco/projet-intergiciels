package go.sock;

import go.Direction;
import go.Observer;
import log.Logger;

import java.io.*;
import java.net.Socket;

public class SlaveChannel<T> implements go.Channel<T> {

    private final String name;

    // Socket
    private final String masterAddr;
    private final int masterPort;

    public SlaveChannel(String name, String masterAddr, int masterPort) {
        this.name = name;
        this.masterAddr = masterAddr;
        this.masterPort = masterPort;
    }

    public String getName() {
        return name;
    }

    public void out(T v) {
        try (Socket socket = new Socket(masterAddr, masterPort);
             ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream())) {

            Logger.info("Sending out request with message: " + v);
            output.writeObject("out");
            output.writeObject(v);
            output.flush();
        } catch (IOException e) {
            Logger.error("Error while connecting to the master", e);
        }
    }

    public T in() {
        try (Socket socket = new Socket(masterAddr, masterPort);
             ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream input = new ObjectInputStream(socket.getInputStream())) {

            Logger.info("Sending in request");
            output.writeObject("in");
            output.flush();

            @SuppressWarnings("unchecked")
            T v = (T) input.readObject();
            Logger.info("Received message: " + v);

            return v;
        } catch (IOException | ClassNotFoundException e) {
            Logger.error("Error while connecting to the master", e);
            return null;
        }
    }

    public void observe(Direction direction, Observer observer) {
        // Ignore
    }
}
