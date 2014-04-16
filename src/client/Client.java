package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import util.ClientServerUtil;

/**
 * client connection manager, send/receive words, client side stats calculation
 * 
 */
public class Client {
	private BufferedReader in;
	private PrintWriter out;

	// ui reference needed for progress bar update
	private ClientUI ui;

	// words batch
	private String[] words = null;
	// words processing times client side
	private long[] times;
	// first sent, last recived word time stamp
	private long firstTime = 0, lastTime = 0;

	private int sendingIndex = 0;
	private int receivingIndex = 0;

	private Socket socket;

	public Client(ClientUI ui) {
		this.ui = ui;
	}

	/**
	 * intiates whole process / starts input and output threads
	 * 
	 * @throws InterruptedException
	 */
	public void sendWords() throws InterruptedException {
		words = ClientServerUtil
				.generateRandomWords(ClientServerUtil.BATCH_SIZE);
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
		socket.close();
	}

	/**
	 * receive processed words from server
	 * 
	 */
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
					if (receivingIndex == times.length - 1) {
						lastTime = currTime - firstTime;
						ui.update(calcStats());
					}
					receivingIndex++;
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {

				ui.finished();
			}
		}

		// Stats calculations
		private String calcStats() {
			StringBuilder sb = new StringBuilder();

			// minimum execution time
			sb.append("min exec time: " + times[0] + " WORD: " + words[0]
					+ "\n");
			// maximum execution time
			sb.append("max exec time: " + times[times.length - 1] + " WORD: "
					+ words[times.length - 1] + "\n");
			// sum time (first sent, last received)
			sb.append("sum exec time: " + lastTime + "\n");
			// avg time:
			sb.append("avg exec time: " + ClientServerUtil.getAvgClient(times)
					+ "\n");
			return sb.toString();
		}
	}

	/**
	 * sending batch of randomly generated words
	 */
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
