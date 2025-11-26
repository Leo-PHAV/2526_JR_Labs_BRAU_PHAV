package TP4;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConnectionThreadP5 extends Thread {

    private Socket clientSocket;
    private int clientId;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    public ConnectionThreadP5(Socket clientSocket, int clientId) {
        this.clientSocket = clientSocket;
        this.clientId = clientId;
    }

    @Override
    public void run() {
        log("Client connected");
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

            // Message de bienvenue
            bufferedWriter.write("Welcome! You are client #" + clientId);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            String message;
            while ((message = bufferedReader.readLine()) != null) {
                log("Sent: " + message);

                bufferedWriter.write("MSG Received: " + message);
                bufferedWriter.newLine();
                bufferedWriter.flush();

                if ("quit".equalsIgnoreCase(message)) {
                    log("Client requested disconnect");
                    break;
                }
            }

        } catch (IOException e) {
            log("Error: " + e.getMessage());
        } finally {
            cleanup();
        }
    }

    private void cleanup() {
        try {
            if (bufferedReader != null) bufferedReader.close();
            if (bufferedWriter != null) bufferedWriter.close();
            if (clientSocket != null) clientSocket.close();
            log("Connection closed");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void log(String msg) {
        String time = new SimpleDateFormat("HH:mm:ss").format(new Date());
        System.out.println("[" + time + "] [Client " + clientId + "] " + msg);
    }
}
