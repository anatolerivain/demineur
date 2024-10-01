import java.net.*;
import java.io.*;

public class Client {
    private Socket socket;
    private DataInputStream entree;
    private DataOutputStream sortie;

    public Client(String adresse, int port) {
        try {
            // Se connecter au serveur
            socket = new Socket(adresse, port);
            entree = new DataInputStream(socket.getInputStream());
            sortie = new DataOutputStream(socket.getOutputStream());

            // Envoyer le nom du joueur
            sortie.writeUTF("Joueur1"); // Remplacez par un nom dynamique si besoin
            System.out.println("Connecté au serveur");

            // Lire le score initial envoyé par le serveur
            int score = entree.readInt();
            System.out.println("Score initial : " + score);
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String commande;
            while (true) {
                System.out.print("Entrez une commande : ");
                commande = reader.readLine(); // Lecture de la commande de l'utilisateur
                if (commande.equals("exit")) { // Ajout d'une condition pour sortir de la boucle
                    break;
                }
                sortie.writeUTF(commande); // Envoi de la commande au serveur
                boolean result = entree.readBoolean(); // Lire le résultat de la commande
                System.out.println("Résultat de la révélation : " + result);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Client("localhost", 10000); // Connecte le client à localhost sur le port 10000
    }
}
