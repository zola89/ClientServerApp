
package client;

import java.util.Random;

public class ClientUtil {
	public static String[] generateRandomWords(int numberOfWords)
	{
	    String[] randomStrings = new String[numberOfWords];
	    Random random = new Random();
	    for(int i = 0; i < numberOfWords; i++)
	    {
	        char[] word = new char[random.nextInt(19)+2]; 
	        for(int j = 0; j < word.length; j++)
	        {
	            word[j] = (char)('a' + random.nextInt(26));
	        }
	        randomStrings[i] = new String(word);
	    }
	    return randomStrings;
	}
	
	public static String[] generateWords(int numberOfWords)
	{
	    String[] randomStrings = new String[numberOfWords];
	    Random random = new Random();
	    for(int i = 0; i < numberOfWords; i++)
	    {
	        char[] word = new char[random.nextInt(2)+2]; 
	        for(int j = 0; j < word.length; j++)
	        {
	            word[j] = (char)('a' + random.nextInt(26));
	        }
	        randomStrings[i] = new String(word);
	        randomStrings[i] = randomStrings[i]+ i;
	    }
	    
	    return randomStrings;
	}
	
	public static void main(String[] args){
		String[] s = generateRandomWords(200);
		for (int i = 0; i < s.length; i++) {
			System.out.println(s[i]);
		}
	}
}