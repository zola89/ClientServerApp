package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerWP {

	public static void main(String[] args) throws Exception {
		new ServerWP().doWork();
	}

	private void doWork() throws Exception {
		System.out.println("The word processing server is running.");
		ServerSocket listener = new ServerSocket(9898);
		WordProcessor wp = new WordProcessor();
		try {
			while (true) {
				Socket s = listener.accept();
				log("New connection with client " + " at " + s);
				new InputThread(s, wp).start();
				new OutputThread(s, wp).start();
			}
		} catch (IOException e) {
			log("Error handling client " + ": " + e);
		} finally {
			try {
				listener.close();
				wp.shutdown();
			} catch (IOException e) {
				log("Couldn't close a socket, what's going on?");
			}
			log("Connection with client is " + " closed");
		}
	}

	private class InputThread extends Thread {
		private Socket socket;
		private WordProcessor wp;

		public InputThread(Socket socket, WordProcessor wp) {
			this.socket = socket;
			this.wp = wp;
		}

		@Override
		public void run() {
			try {
				BufferedReader in = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));
				while (true) {
					String input = in.readLine();
					if (input == null) {
						socket.close();
						break; // Break out of the reading loop.
					}
					wp.process(input);
				}
			} catch (IOException e) {
				//log("Client has terminated the connection");
				 e.printStackTrace();
			}
		}
	}

	private class OutputThread extends Thread {
		private Socket socket;
		private WordProcessor wp;

		public OutputThread(Socket socket, WordProcessor wp) {
			this.socket = socket;
			this.wp = wp;
		}

		@Override
		public void run() {
			try {
				PrintWriter out = new PrintWriter(socket.getOutputStream(),
						true);
				while (true) {
					String output = wp.getResult();

					out.println(output);
				}
			} catch (Exception e) { // zasad
				log("BUM2");
				e.printStackTrace();
			}
		}
	}

	private void log(String message) {
		System.out.println(message);
	}

}