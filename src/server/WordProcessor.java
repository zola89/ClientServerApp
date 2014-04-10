package server;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

public class WordProcessor extends Thread {

	private BlockingQueue<Future<String>> queue = new LinkedBlockingQueue<>();

	private ExecutorService poolOdd = Executors.newFixedThreadPool(2);
	private ExecutorService poolEven = Executors.newFixedThreadPool(2);

	public void process(String input) {
		WordProcessingTask task = new WordProcessingTask(input);

		System.out.println(input + "ide u queue");
		if (parity(input)) {
			queue.add(poolEven.submit(task));
		} else {
			queue.add(poolOdd.submit(task));
		}

	}

	public String getResult() throws InterruptedException, ExecutionException {
		return queue.take().get();
	}

	public boolean parity(String str) {
		return str.length() % 2 == 0;
	}

	public void shutdown() {
		System.out.println("shutdown poolova");
		poolOdd.shutdown();
		poolEven.shutdown();
	}
}