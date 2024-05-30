package go.sock;

import log.Logger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Naming {

    public static final String SERVER_ADDRESS = "localhost";
    public static final int SERVER_PORT = 8080;

    private static HashMap<String, MasterChannel> masters = new HashMap<>();

    public static void main(String[] args) {
        Logger.setDebug(true);
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            Logger.info("Le serveur est en attente de connexion sur le port " + SERVER_PORT + "...");

            while (true) {
                try (Socket socket = serverSocket.accept()) {
                    // Lire les données envoyées par le client
                    BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String name = input.readLine();

                    // Créer le channel
                    go.Channel channel = getChannel(name, socket.getInetAddress().getHostAddress(), socket.getPort());
                    ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                    output.writeObject(channel);

                } catch (IOException e) {
                    Logger.error("Erreur dans la communication avec le client", e);
                }
            }
        } catch (IOException e) {
            Logger.error("Erreur de démarrage du serveur", e);
        }
    }

    private static <T> go.Channel<T> getChannel(String name, String masterAddr, int masterPort) {
        go.Channel<T> result;
        if (masters.containsKey(name)) {
            MasterChannel master = masters.get(name);
            result = new SlaveChannel<>(name, master.getAddr(), master.getPort());
        } else {
            Logger.info("MasterChannel: \tport=" + masterPort + "\taddr=" + masterAddr);
            result = new MasterChannel<>(name, masterAddr, masterPort);
            masters.put(name, (MasterChannel) result);
        }
        return result;
    }

}
