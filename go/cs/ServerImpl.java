package go.cs;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Implantation d'un serveur hébergeant des canaux.
 *
 */
public class ServerImpl {

    public static void main(String args[]) {
        // TODO voir cours
        try {
            CanauxServeur<String> serveur = new CanauxServeurImpl<String>();
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("CanauxServeur", serveur);
            System.out.println("Serveur démarré avec succès...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
