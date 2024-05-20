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
            RemoteFactory factory = new SharedFactory();
            Registry dns = LocateRegistry.createRegistry(1099);
            dns.bind("Factory", factory);
            System.out.println("Serveur démarré avec succès...");

            while (true) {
                Thread.sleep(1000);
            }


            // Close the registry
            // UnicastRemoteObject.unexportObject(dns, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
