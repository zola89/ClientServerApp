package server;

import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.Collections;

import database.DatabaseManager;

import util.ClientServerUtil;

/**
 * 
 * for word processing and stats calculation
 * 
 */
public class WordProcessor {

	// blocking queue that keeps submited tasks in order
	private BlockingQueue<Future<WordProcessingTask>> queue = new LinkedBlockingQueue<>();

	// <task_id, word> map for stats calculations
	private Map<Long, String> map = new TreeMap<Long, String>();

	// two thread pools, each with fixed cappacity of 2 threads
	private ExecutorService poolOdd = Executors.newFixedThreadPool(2);
	private ExecutorService poolEven = Executors.newFixedThreadPool(2);

	// database manager instance
	private static DatabaseManager dbManager = new DatabaseManager();

	// for comparison of word processing times
	Comparator<Entry<Long, String>> cmp = new Comparator<Entry<Long, String>>() {
		public int compare(Entry<Long, String> entry1,
				Entry<Long, String> entry2) {
			return Integer.compare(entry1.getValue().length(), entry2
					.getValue().length());
		}
	};

	/**
	 * task creation and submision to different pools regarding its parity
	 * 
	 * @param input
	 *            word
	 */
	public void process(String input) {
		WordProcessingTask task = new WordProcessingTask(input);
		if (parity(input)) {
			queue.add(poolEven.submit(task));
		} else {
			queue.add(poolOdd.submit(task));
		}

	}

	/**
	 * 
	 * taking tasks from blocking queue and calculating stats
	 * 
	 * @return processed word
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public String getResult() throws InterruptedException, ExecutionException {
		WordProcessingTask task = queue.take().get();

		map.put(task.getCurrentId(), task.getWord());

		if (task.getCurrentId() % ClientServerUtil.BATCH_SIZE == 0) {

			Entry<Long, String> max = Collections.max(map.entrySet(), cmp);
			Entry<Long, String> min = Collections.min(map.entrySet(), cmp);

			int batchNo = (int) task.getCurrentId()
					/ ClientServerUtil.BATCH_SIZE;

			dbManager.insertBatch(batchNo, max.getValue().length() * 100, min
					.getValue().length() * 100, ClientServerUtil
					.getAvarages(map));
			for (Entry<Long, String> entry : map.entrySet()) {
				dbManager.insertWord(entry.getValue(), entry.getValue()
						.length() * 100, parity(entry.getValue()), batchNo);
			}
			map.clear();

		}

		return task.getWord();

	}

	public boolean parity(String str) {
		return str.length() % 2 == 0;
	}
	
	/**
	 * shutdown pools
	 */
	public void shutdown() {
		poolOdd.shutdown();
		poolEven.shutdown();
	}
}