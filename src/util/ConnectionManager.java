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
	private long firstTime = 0, lastTime = 0;
	private int sendingIndex = 0;
	private int receivingIndex = 0;

	private Socket socket;

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
		socket = new Socket("localhost", 9898);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(socket.getOutputStream(), true);
	}

	public void disconnectFromServer() throws IOException {
		out.flush();
		// socket.close();
	}

	private class InputThread extends Thread {
		@Override
		public void run() {
			try {
				while (receivingIndex < words.length) {
					String input = in.readLine();
					long currTime = System.currentTimeMillis();
					times[receivingIndex] = currTime - times[receivingIndex];
					ui.update(input + " " + times[receivingIndex],
							receivingIndex);
					if (receivingIndex == times.length-1){
						lastTime = currTime-firstTime;
						//first 
						//last
						//sum
						//avg
						StringBuilder sb = new StringBuilder();
						sb.append("min exec time: " + times[0] + " " + words[0]+"\n");
						sb.append("max exec time: " + times[times.length-1] + " " + words[times.length-1]+"\n");
						sb.append("sum exec time: " + lastTime +"\n");
						sb.append("sum avg time: " + ClientServerUtil.getAvgClient(times) +"\n");
						ui.update(sb.toString());
						
						
						
					}

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
					if (sendingIndex == 0)
						firstTime = times[sendingIndex];
					sendingIndex++;
				}
			} catch (Exception e) { // zasad
				e.printStackTrace();
			}
		}
	}

}
