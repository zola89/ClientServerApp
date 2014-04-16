package server;

import java.util.concurrent.Callable;

class WordProcessingTask implements Callable<WordProcessingTask> {

	private String str;
	private static long id =0;
	private long currentId =0;
	
	public WordProcessingTask(String str) {
		this.str = str;
		currentId = ++id;
		
		
	}

	@Override
	public WordProcessingTask call() throws Exception {
		Thread.sleep(str.length()*100); // obrada
		return this;
	}
	
	public long getCurrentId(){
		return currentId;
	}
	
	public String getWord(){
		return str;
	}
	
	
}
