import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Scanner;

public class ClientUDP {

    public static void send(String envoie, DatagramSocket client) throws IOException {
        byte[] buffer = envoie.getBytes();
        InetAddress adresse = InetAddress.getLocalHost();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, adresse, 2345);
        client.send(packet);
    }

    public static DatagramPacket receive(DatagramSocket client) throws IOException {
        byte[] buffer2 = new byte[8196];
        DatagramPacket packet2 = new DatagramPacket(buffer2, buffer2.length);
        client.receive(packet2);
        return packet2;
    }

    public static void main(String[] args) {
        try (DatagramSocket client = new DatagramSocket()) {
            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.print("Entrez votre commande : \n");
                String message = scanner.nextLine();
                ClientUDP.send(message, client);

                // Attendre la réponse du serveur
                DatagramPacket responsePacket = ClientUDP.receive(client);
                String response = new String(responsePacket.getData(), 0, responsePacket.getLength());
                System.out.println("Le serveur a répondu : " + response);
            }

         

           

          
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}