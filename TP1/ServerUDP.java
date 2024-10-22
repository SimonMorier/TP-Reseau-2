import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ServerUDP {

    public static DatagramPacket receive(DatagramSocket server) throws IOException {
        byte[] buffer = new byte[8192];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        server.receive(packet);
        return packet;
    }

    public static void send(DatagramSocket server, String str, DatagramPacket dp) throws IOException {
        byte[] buffer2 = (str).getBytes();
        DatagramPacket packet2 = new DatagramPacket(buffer2, buffer2.length, dp.getAddress(), dp.getPort());
        server.send(packet2);
    }

    public static void main(String[] args) {
        boolean serverReady = false;
        try (DatagramSocket server = new DatagramSocket(2345, InetAddress.getLocalHost())) {
            while (true) {
                DatagramPacket dp = ServerUDP.receive(server);
                String str = new String(dp.getData(), 0, dp.getLength());

                if (str.equals("Hello serveur RX302")) {
                    serverReady = true;
                    System.out.println("Nouveau Client");
                    System.out.print("Reçu de la part de " + dp.getAddress() + " sur le port " + dp.getPort() + " : ");
                    System.out.println(str);
                    ServerUDP.send(server, "Serveur RX302 ready", dp);
                } else if (serverReady) {
                    ServerUDP.send(server, "pong", dp);
                    System.out.println(
                            "Reçu de la part de " + dp.getAddress() + " sur le port " + dp.getPort() + " : " + str);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}