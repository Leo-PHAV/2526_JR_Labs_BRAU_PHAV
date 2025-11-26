package TP4;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MultithreadedTCPServer {

    private int port;

    public MultithreadedTCPServer(int port) {
        this.port = port;
    }

    public void launch() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Multithreaded Server started on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();

                ConnectionThread clientThread = new ConnectionThread(clientSocket);
                clientThread.start();

                // Affiche le nombre de threads actifs 
                System.out.println("Active threads: " + (Thread.activeCount() - 1));
            }

        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        int port = 8080; // Valeur par défaut
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }
        MultithreadedTCPServer server = new MultithreadedTCPServer(port);
        server.launch();
    }
}
