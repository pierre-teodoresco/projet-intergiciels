package go.cs;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Scanner;

public class Client<T> {
    /*
    public static void main(String args[]) {
        try {
            // Se connecter au registre RMI sur le port 1099
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);

            // Obtenir une référence à l'interface du serveur
            CanauxServeurImpl<String> proxy = (CanauxServeurImpl<String>) registry.lookup("CanauxServeur");

            // Menu textuel
            Scanner scanner = new Scanner(System.in);
            int choix;
            do {
                System.out.println("Menu :");
                System.out.println("1. Créer et ajouter un canal");
                System.out.println("2. Lire les messages d'un canal");
                System.out.println("3. Envoyer un message à un canal");
                System.out.println("4. Afficher les canaux disponibles");
                System.out.println("0. Quitter");
                System.out.print("Votre choix : ");
                choix = scanner.nextInt();
                switch (choix) {
                    case 1:
                        // Créer et ajouter un canal
                        System.out.print("Nom du canal : ");
                        String nomCanal = scanner.next();
                        Channel<String> canal = new Channel<>(nomCanal);
                        proxy.ajouterChannel(canal);
                        System.out.println("Canal créé et ajouté avec succès.");
                        break;
                    case 2:
                        // Lire les messages d'un canal
                        System.out.print("Nom du canal : ");
                        String nomCanalLecture = scanner.next();
                        // On chercher le canal dans la liste des canaux à partir de son nom
                        Channel<String> canalLecture = null;
                        for (go.Channel c : proxy.getCanaux()) {
                            if (c.getName().equals(nomCanalLecture)) {
                                canalLecture = (Channel<String>) c;
                                break;
                            }
                        }
                        String message = (String) proxy.lire(canalLecture);
                        System.out.println("Messages reçus : \n" + message);
                        break;
                    case 3:
                        // Envoyer un message à un canal
                        System.out.print("Nom du canal : ");
                        String nomCanalEcriture = scanner.next();
                        System.out.print("Message à envoyer : ");
                        String m = scanner.next();
                        // On chercher le canal dans la liste des canaux à partir de son nom
                        Channel<String> canalEcriture = null;
                        for (go.Channel c : proxy.getCanaux()) {
                            if (c.getName().equals(nomCanalEcriture)) {
                                canalEcriture = (Channel<String>) c;
                                break;
                            }
                        }
                        //proxy.ecrire(canalEcriture, message);
                        System.out.println("Message envoyé avec succès.");
                        break;
                    case 4:
                        // Afficher les canaux disponibles
                        List<go.Channel> canaux = proxy.getCanaux();
                        System.out.println("Canaux disponibles :");
                        for (go.Channel c : canaux) {
                            System.out.println(c.getName());
                        }
                    case 0:
                        System.out.println("Au revoir !");
                        break;
                    default:
                        System.out.println("Choix invalide, veuillez réessayer.");
                        break;
                }
            } while (choix != 0);

        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }*/
}
