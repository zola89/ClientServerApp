package client;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class ServerWP {

    public static void main(String[] args) throws Exception {
        System.out.println("The word processing server is running.");
        ServerSocket listener = new ServerSocket(9898);
        try {
            while (true) {
                new WordProcessor(listener.accept()).start();
            }
        } finally {
            listener.close();
        }
    }

    private static class WordProcessor extends Thread {
        private Socket socket;

        public WordProcessor(Socket socket) {
            this.socket = socket;
            log("New connection with client "+ " at " + socket);
        }

        public void run() {
            try {

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                while (true) {
                    String input = in.readLine();
                    if (input == null || input.equals(".")) {
                        break;
                    }
                    out.println(input.toUpperCase());
                    try {
						Thread.sleep(input.length()*100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                }
            } catch (IOException e) {
                log("Error handling client " + ": " + e);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    log("Couldn't close a socket, what's going on?");
                }
                log("Connection with client is " + " closed");
            }
        }

        private void log(String message) {
            System.out.println(message);
        }
    }
}