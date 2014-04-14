package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import client.ClientUI;

public class ConnectionManager {
	private BufferedReader in;
	private PrintWriter out;

	private ClientUI ui;

	private String[] words = null;
	private long[] times;

	private int sendingIndex = 0;
	private int receivingIndex = 0;

	public ConnectionManager(ClientUI ui) {
		this.ui = ui;
	}

	public void sendWords() throws InterruptedException {
		words = ClientServerUtil.generateWords(200);
		times = new long[words.length];
		sendingIndex = 0;
		receivingIndex = 0;

		Thread inputThread = new InputThread();
		Thread outputThread = new OutputThread();
		inputThread.start();
		outputThread.start();
	}

	public void connectToServer() throws IOException {
		Socket socket = new Socket("localhost", 9898);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(socket.getOutputStream(), true);
	}

	private class InputThread extends Thread {
		@Override
		public void run() {
			try {
				while (receivingIndex < words.length) {
					String input = in.readLine();
					times[receivingIndex] = System.currentTimeMillis() - times[receivingIndex];
					ui.update(input + " " + times[receivingIndex], receivingIndex);

					receivingIndex++;
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				
				ui.finished();
			}
		}
	}

	private class OutputThread extends Thread {
		@Override
		public void run() {
			try {
				while (sendingIndex < words.length) {
					out.println(words[sendingIndex]);
					times[sendingIndex] = System.currentTimeMillis();

					sendingIndex++;
				}
			} catch (Exception e) { // zasad
				e.printStackTrace();
			}
		}
	}

}
