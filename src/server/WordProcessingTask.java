package server;

import java.util.concurrent.Callable;

class WordProcessingTask implements Callable<String> {

	private String str;
	private static long id =0;
	private long currentId =0;
	
	public WordProcessingTask(String str) {
		this.str = str;
		currentId = ++id;
		
		
	}

	@Override
	public String call() throws Exception {
		Thread.sleep(str.length()*100); // obrada
		return str;
	}
	
	public long getCurrentId(){
		return currentId;
	}
	
}
