import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class chat_server_1 {
    private static final int PORT = 3000;
    private static final Set<ClientHandler> clients = ConcurrentHashMap.newKeySet();

    public static void main(String[] args) throws IOException {
        System.out.println("[chat_server_1] Listening on port " + PORT + "...");
        try (ServerSocket server = new ServerSocket(PORT)) {
            while (true) {
                Socket socket = server.accept();
                ClientHandler client = new ClientHandler(socket);
                clients.add(client);
                new Thread(client).start();
            }
        }
    }
    static void broadcast(String message, ClientHandler sender) {
        for (ClientHandler client : clients) {
            if (client != sender) {
                client.send(message);
            }
        }
    }
    static void remove(ClientHandler client) {
        clients.remove(client);
        System.out.println("[chat_server_1] Client disconnected: " + client.getName());
    }

    static class ClientHandler implements Runnable {
        private final Socket socket;
        private PrintWriter out;
        private String name = "Anonymous";

        ClientHandler(Socket socket) {
            this.socket = socket;
        }

        String getName() { return name; }

        @Override
        public void run() {
            System.out.println("[+] Connection from " + socket.getInetAddress());
            try (
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            ) {
                out = new PrintWriter(socket.getOutputStream(), true);
                out.println("Welcome to the Chat Server!");
                out.println("Enter your nickname:");

                name = in.readLine();
                broadcast("[System] " + name + " joined the chat.", this);

                String msg;
                while ((msg = in.readLine()) != null) {
                    if (msg.equalsIgnoreCase("/quit")) break;
                    System.out.println("[" + name + "]: " + msg);
                    broadcast("[" + name + "]: " + msg, this);
                }
            } catch (IOException e) {
                System.err.println("[!] " + e.getMessage());
            } finally {
                chat_server_1.remove(this);
                broadcast("[System] " + name + " left the chat.", this);
                try { socket.close(); } catch (IOException ignored) {}
            }
        }

        void send(String message) {
            if (out != null) out.println(message);
        }
    }
}
