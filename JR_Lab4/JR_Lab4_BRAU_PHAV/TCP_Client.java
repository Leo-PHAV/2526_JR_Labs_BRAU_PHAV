package TP4;

// https://www.youtube.com/watch?v=gchR3DpY-8Q Video i see to make the client and server TCP 

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;



public class TCP_Client{


    public static void main(String[] args){

        Socket socket = null;
        InputStreamReader inputStreamReader = null;
        OutputStreamWriter outputStreamWriter = null;
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;

        String host = args[0];
        int port = Integer.parseInt(args[1]);
        try{
            socket = new Socket(host, port);
            inputStreamReader = new InputStreamReader(socket.getInputStream());// recupere le flux venant du serveur et convertit les bytes en texte
            outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());//flux pour envoyer au serveur et convertit le texte en bytes
            bufferedReader = new BufferedReader(inputStreamReader);// 
            bufferedWriter = new BufferedWriter(outputStreamWriter);// 
            Scanner scanner = new Scanner(System.in);// permet d elire ce que tu lis dans la console
            
            String welcome = bufferedReader.readLine();
            System.out.println("Server : " + welcome);

            while (true) {
                System.out.print("You: ");
                String msgToSend = scanner.nextLine();

                bufferedWriter.write(msgToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();

                String reply = bufferedReader.readLine();
                if (reply == null) break;
                System.out.println("Server : " + reply);

                if (msgToSend.equalsIgnoreCase("quit")) break;
            }

        }catch (IOException e) {
                e.printStackTrace();
        }finally{
            try{
                if (socket != null){socket.close();}
                if (bufferedReader != null) bufferedReader.close();
                if (bufferedWriter != null) bufferedWriter.close();
                if (inputStreamReader != null) inputStreamReader.close();
                if (outputStreamWriter != null) outputStreamWriter.close();
            } catch (IOException e ){
                e.printStackTrace();
            }
        
        }
    }
}