package go.sock;

import go.Channel;
import go.Direction;
import go.Observer;
import log.Logger;

import java.io.*;
import java.net.Socket;

public class SlaveChannel<T> implements go.Channel<T> {

    private String name;

    // Socket
    private String masterAddr;
    private int masterPort;

    public SlaveChannel(String name, String masterAddr, int masterPort) {
        this.name = name;
        this.masterAddr = masterAddr;
        this.masterPort = masterPort;
    }

    public String getName() {
        return name;
    }

    public void out(T v) {
        try (Socket socket = new Socket(masterAddr, masterPort)) {
            PrintWriter zoiper = new PrintWriter(socket.getOutputStream(), true);
            zoiper.println("out");

            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String message = input.readLine();

            if (!message.equals("ok")) {
                Logger.error("Slave Out", new Exception("Erreur lors de l'envoi de la valeur"));
            }

            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            output.writeObject(v);

            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            message = input.readLine();

            if (!message.equals("ok")) {
                Logger.error("Slave Out", new Exception("Erreur lors de l'envoi de la valeur"));
            }

        } catch (IOException e) {
            Logger.error("Slave Out", e);
        }
    }

    public T in() {
        try (Socket socket = new Socket(masterAddr, masterPort)) {
            PrintWriter zoiper = new PrintWriter(socket.getOutputStream(), true);
            zoiper.println("in");

            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            return (T) input.readObject();

        } catch (IOException | ClassNotFoundException e) {
            Logger.error("Slave Out", e);
        }
        return null;
    }

    // Ignore
    public void observe(Direction direction, Observer observer) {

    }
}
