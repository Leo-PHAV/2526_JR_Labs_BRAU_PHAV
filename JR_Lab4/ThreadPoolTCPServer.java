package TP4;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPoolTCPServer {

    private int port;
    private ExecutorService threadPool;
    private static AtomicInteger clientCounter = new AtomicInteger(0);
    private volatile boolean running = true;

    public ThreadPoolTCPServer(int port) {
        this.port = port;
        this.threadPool = Executors.newFixedThreadPool(10); 
    }

    public void launch() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Thread Pool Server started on port " + port);

            while (running) {
                Socket clientSocket = serverSocket.accept();
                int clientId = clientCounter.incrementAndGet();

                threadPool.execute(() -> {
                    ConnectionThreadP5 handler = new ConnectionThreadP5(clientSocket, clientId);
                    handler.run();
                });
            }

        } catch (IOException e) {
            if (running) {
                System.err.println("Server error: " + e.getMessage());
            }
        } finally {
            shutdown();
        }
    }

    public void shutdown() {
        running = false;
        threadPool.shutdown();
        System.out.println("Server shutdown initiated");
    }

    public static void main(String[] args) {
        int port = 8080;
        if (args.length > 0) port = Integer.parseInt(args[0]);

        ThreadPoolTCPServer server = new ThreadPoolTCPServer(port);

        Runtime.getRuntime().addShutdownHook(new Thread(server::shutdown));

        server.launch();
    }
}
