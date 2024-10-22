import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class HorlogeClient {

    private Socket socket;
    private BufferedReader input;
    private OutputStream output;

    public HorlogeClient(String host, int port) {
        try {
            socket = new Socket(host, port);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = socket.getOutputStream();
        } catch (Exception e) {
            System.out.println("Erreur lors de la connexion au serveur : " + e.getMessage());
        }
    }

    // Méthode pour envoyer un message au serveur
    public void send(String message) {
        try {
            // Envoyer le message au serveur
            output.write((message + "\n").getBytes());

            // Lire la réponse (heure actuelle) du serveur
            String serverResponse = input.readLine();
            System.out.println("Réponse du serveur : " + serverResponse);
        } catch (Exception e) {
            System.out.println("Erreur lors de l'envoi du message : " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        HorlogeClient client = new HorlogeClient("localhost", 12345);
        String message = "";

        // Envoyer un message au serveur
        while (!message.equals("CLOSE")) {
            System.out.print("Entrez votre commande : \n");
            Scanner scanner = new Scanner(System.in);
            message = scanner.nextLine();
            client.send(message);

        }
    }
}
