package util;

import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;



public class ClientServerUtil {
	public static final int BATCH_SIZE = 200;

	public static String[] generateRandomWords(int numberOfWords) {
		String[] randomStrings = new String[numberOfWords];
		Random random = new Random();
		for (int i = 0; i < numberOfWords; i++) {
			char[] word = new char[random.nextInt(19) + 2];
			for (int j = 0; j < word.length; j++) {
				word[j] = (char) ('a' + random.nextInt(26));
			}
			randomStrings[i] = new String(word);
		}
		return randomStrings;
	}

	public static String[] generateWords(int numberOfWords) {
		String[] randomStrings = new String[numberOfWords];
		Random random = new Random();
		for (int i = 0; i < numberOfWords; i++) {
			char[] word = new char[random.nextInt(2) + 2];
			for (int j = 0; j < word.length; j++) {
				word[j] = (char) ('a' + random.nextInt(26));
			}
			randomStrings[i] = new String(word);
			randomStrings[i] = randomStrings[i] + i;
		}

		return randomStrings;
	}

	public static long getAvarages(Map<Long, String> map) {
		long sum = 0;
		for (Entry<Long, String> entry : map.entrySet()) {
			sum += entry.getValue().length() * 100;
		}
		return sum / map.size();

	}

	public static double getSum(Map<Long, String> map) {
		double sum = 0;
		for (Entry<Long, String> entry : map.entrySet()) {
			sum += entry.getValue().length() * 100;
		}
		return sum;

	}
	
	public static long getSumClient(long [] array){
		long sum = 0;
		for (int i = 0; i < array.length; i++) {
			sum += array[i];
		}
		return sum;
	}
	
	public static long getAvgClient(long [] array){
		long sum = 0;
		for (int i = 0; i < array.length; i++) {
			sum += array[i];
		}
		return sum/array.length;
	}

	public static void main(String[] args) {
		String[] s = generateRandomWords(200);
		for (int i = 0; i < s.length; i++) {
			System.out.println(s[i]);
		}
	}
}