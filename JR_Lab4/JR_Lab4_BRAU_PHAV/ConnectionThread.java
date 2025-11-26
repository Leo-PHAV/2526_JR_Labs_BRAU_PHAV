package TP4;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;


public class ConnectionThread extends Thread {

    private Socket clientSocket;
    private int clientId;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private static AtomicInteger clientCounter = new AtomicInteger(0);

    // 3.1 Step 1: Create ConnectionThread Class
    public ConnectionThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
        // 3.2.2 Solution Using AtomicInteger
        this.clientId = clientCounter.incrementAndGet();
        this.setName("ClientHandler-" + clientId);
    }

    @Override
    public void run() {
        System.out.println("[" + new Date() + "] Client " + clientId + " connected");
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

            bufferedWriter.write("Welcome! You are client #" + clientId);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            
            String message;
            while ((message = bufferedReader.readLine()) != null) {
                System.out.println("[" + new Date() + "] Client " + clientId + " sent: " + message);

                bufferedWriter.write("MSG Received: " + message);
                bufferedWriter.newLine();
                bufferedWriter.flush();

                // Quit command
                if (message.equalsIgnoreCase("quit")) {
                    System.out.println("[" + new Date() + "] Client " + clientId + " requested disconnect");
                    break;
                }
            }

        } catch (IOException e) {
            System.err.println("Client " + clientId + " error: " + e.getMessage());
        } finally {
            cleanup();
        }
    }

    private void cleanup() {
        try {
            if (bufferedReader != null) bufferedReader.close();
            if (bufferedWriter != null) bufferedWriter.close();
            if (clientSocket != null) clientSocket.close();

            log("Connection closed.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void log(String msg) {
        String time = new SimpleDateFormat("HH:mm:ss").format(new Date());
        System.out.println("[" + time + "] [Client " + clientId + "] " + msg);
    }
}
