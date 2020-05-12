import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

class ServerThread implements Runnable {
    private final Socket sock;

    public ServerThread(Socket sock) {
        this.sock = sock;
    }

    public void run() {
        System.out.println(Thread.currentThread().getName() + ": connection opened with " + sock.getInetAddress().getHostAddress() + ":" + sock.getPort());

        try {
            DataInputStream in = new DataInputStream(sock.getInputStream());
            DataOutputStream out = new DataOutputStream(sock.getOutputStream());

            while (!sock.isClosed()) {
                Thread.sleep(100);
            }

            // Clears and close the output stream.
            out.flush();
            out.close();
            sock.close();
            System.out.println(Thread.currentThread().getName() + ": connection closed.");
        } catch (IOException err) {
            System.out.println("Thread caught IOException: " + err.getMessage());
        } catch (InterruptedException err) {
            System.out.println("Thread caught InterruptedException: " + err.getMessage());
        }
    }
}

public class Server {
    private static final int portNumber = 1024;

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(portNumber);
            System.out.println("Server ready for connections @ " + serverSocket.getInetAddress().getHostAddress() + ":" + portNumber);

            // This loop will run and wait for one connection at a time.
            while (true) {
                Thread thread = new Thread(new ServerThread(serverSocket.accept()));
                thread.start();
            }
        } catch (IOException err) {
            System.out.println("Client caught IOException: " + err.getMessage());
        }
    }
}
