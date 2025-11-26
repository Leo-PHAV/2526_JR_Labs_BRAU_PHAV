package TP4;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class MultithreadedTCPServerP5 {

    private int port;
    private static AtomicInteger clientCounter = new AtomicInteger(0);

    // Constructeur
    public MultithreadedTCPServerP5(int port) {
        this.port = port;
    }

    // méthode
    public void launch() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Multithreaded Server started on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                int clientId = clientCounter.incrementAndGet();

                ConnectionThreadP5 clientThread = new ConnectionThreadP5(clientSocket, clientId);
                clientThread.start();

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
