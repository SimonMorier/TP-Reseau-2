import java.net.DatagramSocket;
import java.net.SocketException;

public class ScannerUDP {

    public static void main(String[] args) {
        System.out.println(scan(1, 1000));
    }

    public static int scan(int port_deb, int port_fin) {

        for (int i = port_deb; i < port_fin + 1; i++) {
            try {
                DatagramSocket cli = new DatagramSocket(i);
                cli.close();

                return i;

            } catch (SocketException ex) {
                // System.err.println("fermÃ© : " + i);
            }
        }
        return -1;

    }

}