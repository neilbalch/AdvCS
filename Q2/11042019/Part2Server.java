import java.io.*;
import java.net.*;
import java.util.HashMap;

//class Pair<T, U> {
//    private T t;
//    private U u;
//
//    public Pair(T t, U u) {
//        this.t = t;
//        this.u = u;
//    }
//
//    public T getT() {
//        return t;
//    }
//
//    public U getU() {
//        return u;
//    }
//}

public class Part2Server extends Thread {
    private ServerSocket serverSocket;

    public Part2Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(10000);
    }

    public void run() {
        while (true) {
            try {
                System.out.println("Waiting for client on port " +
                        serverSocket.getLocalPort() + "...");
                Socket server = serverSocket.accept();

                System.out.println("Just connected to " + server.getRemoteSocketAddress());
                DataInputStream in = new DataInputStream(server.getInputStream());
                DataOutputStream out = new DataOutputStream(server.getOutputStream());

//                ArrayList<Pair<String, String>> questions = new ArrayList<>();
//                questions.add(new Pair<String, String>("Capital of California?", "Sacramento"));
//                questions.add(new Pair<String, String>("What state is the biggest by land area?", "Alaska"));
//                questions.add(new Pair<String, String>("What state is most populous?", "California"));
//                questions.add(new Pair<String, String>("In what continent is Mother Russia?", "Asia"));
//                questions.add(new Pair<String, String>("Which country is north of the US?", "Canada"));

                HashMap<String, String> responses = new HashMap<>();
                responses.put("How do you make holy water?", "You boil the hell out of it.");
                responses.put("", "");
                responses.put("", "");
                responses.put("", "");
                responses.put("", "");

//                for(Pair<String, String> question : questions) {
//                    out.writeUTF(question.getT());
//                    String response = in.readUTF();
//
//                    if(response.equalsIgnoreCase(question.getU()))
//                        out.writeUTF("Correct!");
//                    else
//                        out.writeUTF("Incorrect! The correct answer was: " + question.getU());
//                }

//                System.out.println(in.readUTF());
//                out.writeUTF("Thank you for connecting to " + server.getLocalSocketAddress()
//                        + "\nGoodbye!");

                out.writeUTF("none more");
                server.close();

            } catch (SocketTimeoutException s) {
                System.out.println("Socket timed out!");
                break;
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public static void main(String[] args) {
//        int port = Integer.parseInt(args[0]);
        int port = 1024;

        try {
            Thread t = new Part1Server(port);
            t.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
