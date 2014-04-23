package gameFiles;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class ScoringModule {
	
	private static RandomAccessFile phonemeRAF;
	private static RandomAccessFile homophonesRAF;
	
	public static void main(String[]args) {
		initFiles();
	}

	public static void initFiles() {
		try {		
			File homophonesTextFile = new File("C:\\Users\\Ann Nee\\Documents\\Uni\\Java\\Dissertation\\WebContent\\scoringFiles\\english3369_homophones.txt");
			File phonemeTextFile = new File("C:\\Users\\Ann Nee\\Documents\\Uni\\Java\\Dissertation\\WebContent\\scoringFiles\\beep-1.0");
			homophonesRAF = new RandomAccessFile(homophonesTextFile, "r");
			phonemeRAF = new RandomAccessFile(phonemeTextFile, "r");
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param userResponse
	 * @param correctWord
	 * @return
	 */
	public static int computePartialScore(String userResponse, String correctWord) {
		try {
			//check if the correct word is in the homophones text file
			String homophone = binarySearch(homophonesRAF, correctWord);
			
			//if it is, return a score of 200
			if (homophone != null && userResponse.equalsIgnoreCase(homophone)) {
				return 200;
			}
			
			//if not, look for phonemic transcription of the player's response
			//and the correct answer in the phoneme library (beep-1.0)
			else {				
				String userAnswerPhoneme = binarySearch(phonemeRAF, userResponse);
				String correctWordPhoneme = binarySearch(phonemeRAF, correctWord.toUpperCase());
				
				if (userAnswerPhoneme != null && correctWordPhoneme != null) {
					int distance = levenshteinDistance(userAnswerPhoneme, correctWordPhoneme);
					
					//if minimal pair (differ by one phoneme)
					//e.g. hair and fair are minimal pairs
					if (distance==1) {
						return 80;
					}
					
					//answers differ by two phonemes
					//e.g. 
					else if (distance==2) {
						return 40;
					}
				}
				
				else {
					return 0;
				}						
			}
		}
		catch (IOException e) {			
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * 
	 * @param userResponse
	 * @param correctAnswer
	 * @return the edit distance between the two phonemic transcriptions
	 */
	private static int levenshteinDistance(String userResponse, String correctAnswer) {
		int distance = 0;
		
		//phonemes found in the correct word
		ArrayList<String> correctWordPho = new ArrayList<String>(Arrays.asList(correctAnswer.split(" ")));
		String[] responsePho = userResponse.split(" ");
		
		if (responsePho.length < correctWordPho.size()) {
			distance += (correctWordPho.size() - responsePho.length);
		}
		
		for (int i = 0; i<responsePho.length; i++) {
			if (correctWordPho.contains(responsePho[i]))
				correctWordPho.remove(responsePho[i]);
			
			else
				distance += 1;
		}
	 
		return distance;
	}
	
	/**
	 * Performs binary search on a sorted text file
	 * http://ernelljava.blogspot.co.uk/2012/04/java-binary-search-in-sorted-textfile.html
	 * @author Robert Andersson
	 * @param raf
	 * @param target
	 * @return null if the word is not found in the text file
	 * 			phonemic transcription of the word if it is found
	 * @throws IOException
	 */
	private static String binarySearch(RandomAccessFile raf, String target)
            throws IOException {

        raf.seek(0);
        String line = raf.readLine();

        if (line.compareTo(target) == 0) {
        	String[] result = line.split("\t"); 
        	
        	//phonemic transcription of the word
    		return result[result.length-1];
        }

        long low = 0;
        long high = raf.length();
        long p = -1;

        while (low < high) {

            long mid = (low + high) >>> 1;
            p = mid;
            while (p >= 0) {
                raf.seek(p);
                char c = (char) raf.readByte();
                if(c == '\n')
                    break;
                p--;
            }
            if(p < 0)
                raf.seek(0);
            
            line = raf.readLine();
            
            if( (line == null) || line.compareTo(target) < 0)
                low = mid + 1;
            else
                high = mid;
        }

        p = low;
        while (p >= 0){
            raf.seek(p);
            if(((char) raf.readByte()) == '\n')
                break;
            p--;
        }
        
        if(p < 0)
            raf.seek(0);
        
        
        while(true){
            
        	line = raf.readLine();
            
        	if (line != null && line.contains(target)) {
        		String[] result = line.split("\t");
        		
        		return result[result.length-1];
        	}
  		
        	else
                break;
        }

        // Nothing found
        return null;

    }
}
