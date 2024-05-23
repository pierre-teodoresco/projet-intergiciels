package go.cs;

import log.Logger;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
/**
 * Implantation d'un serveur hébergeant des canaux.
 *
 */
public class ServerImpl {

    public static void main(String[] args) {
        Logger.setDebug(true);
        try {
            API api = new ServerAPI();
            Registry dns = LocateRegistry.createRegistry(1099);
            dns.bind("API", api);
            Logger.log("RMI Server listening port 1099...");

            while (true) {
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
