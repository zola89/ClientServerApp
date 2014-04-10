package server;

import java.util.concurrent.Callable;

class WordProcessingTask implements Callable<String> {

	private String str;
	
	public WordProcessingTask(String str) {
		this.str = str;
	}

	@Override
	public String call() throws Exception {
		Thread.sleep(str.length()*100); // obrada
		return str;
	}
	
}
