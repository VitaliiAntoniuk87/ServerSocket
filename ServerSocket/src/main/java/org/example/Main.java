package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) {


        try (ServerSocket serverSocket = new ServerSocket(8081)) {
            System.out.println("Server is listening on port");

            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected");

            InputStream inputStream = clientSocket.getInputStream();
            OutputStream outputStream = clientSocket.getOutputStream();

            PrintWriter printWriter = new PrintWriter(outputStream);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String serverGreeting = "привіт";
            printWriter.write(serverGreeting);
            printWriter.flush();

            String clientAnswer = reader.readLine();
            System.out.println(clientAnswer);

            if (isRussianLettersExist(clientAnswer)) {
                sendControlQuestion(printWriter);
                String clientResponse = reader.readLine();

                if (clientResponse.equalsIgnoreCase("Хліб")) {
                    String currentTime = LocalDateTime.now().toString();
                    printWriter.write(currentTime);
                    printWriter.flush();
                } else {
                    printWriter.write("Wrong answer");
                    printWriter.flush();
                    clientSocket.close();
                    System.out.println("Client disconnected");
                }


            } else {
                printWriter.write("Дякую, що завітав");
                printWriter.flush();
                serverSocket.close();
            }


        } catch (IOException e) {
            System.err.println("Error while listening connection or reading input stream");
            e.printStackTrace();
        }

    }

    private static boolean isRussianLettersExist(String message) {
        char[] letters = message.toCharArray();
        List<Character> russianLetters = List.of('ы', 'э', 'ъ');
        for (char letter : letters) {
            if (russianLetters.contains(letter)) {
                return true;
            }
        }
        return false;
    }

    private static void sendControlQuestion(PrintWriter printWriter) {
        printWriter.write("Що таке паляниця?");
        printWriter.flush();
    }

}