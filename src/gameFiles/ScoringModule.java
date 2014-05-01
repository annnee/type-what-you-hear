package gameFiles;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class ScoringModule {
	
	private static RandomAccessFile phonemeRAF;
	private static String userDir = "";
	
	public static void main(String[]args) {
		initFiles();
	}

	public static void initFiles() {
		if (System.getProperty("user.dir").contains("Ann Nee")) 
			userDir = "C:/Users/Ann Nee/Documents/Uni/Java/Dissertation/WebContent/";
		
		else {
			userDir = "mytomcat/webapps/dissertation/";
		}
	
		try {		
			File phonemeTextFile = new File(userDir + "scoringFiles/beep-1.0.txt");	
			phonemeRAF = new RandomAccessFile(phonemeTextFile, "r");
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * 
	 * @param userResponse
	 * @param correctWord
	 * @return
	 */
	public static double computePartialScore(String userResponse, String correctWord) {
		try {
			//look for phonemic transcription of the player's response
			//and the correct answer in the phoneme library (beep-1.0)
			ArrayList<String> userAnswerPhonemes = binarySearch(phonemeRAF, userResponse);
			ArrayList<String> correctWordPhonemes = binarySearch(phonemeRAF, correctWord.toUpperCase());
			int distance = 100;	
			
			if (userAnswerPhonemes != null && correctWordPhonemes != null) {
				for (String userAns : userAnswerPhonemes) {
					String[] curAns =  userAns.split("\t");
					String userPhonemes = curAns[curAns.length-1];
					
					for (String correctAnswerPhonemes : correctWordPhonemes) {
						String[] curCorrectAns =  correctAnswerPhonemes.split("\t");
						String ansPhonemes = curCorrectAns[curCorrectAns.length-1];
						
						int tempDistance = levenshteinDistance(userPhonemes, ansPhonemes);
									
						if (tempDistance < distance) {
							distance = tempDistance;				
						}
					}
				}
				
				//if it is a homophone
				if (distance==0) {
					return 200.0;
				}

				//if minimal pair (differ by one phoneme)
				//e.g. hair and fair are minimal pairs
				if (distance==1) {
					return 100.0;
				}

				//answers differ by two phonemes
				else if (distance==2) {
					return 50.0;
				}

				//differs by three phonemes
				else if (distance==3) {
					return 25.0;
				}

				//differs by more than three phonemes
				else {
					return 0.0;
				}			
			}
			
			else {
				return 0.0;
			}
		}
		catch (IOException e) {			
			e.printStackTrace();
		}
		return 0.0;
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
		ArrayList<String> correctWordPhonemes = new ArrayList<String>(Arrays.asList(correctAnswer.split(" ")));
		String[] responsePhonemes = userResponse.split(" ");
		
		if (responsePhonemes.length < correctWordPhonemes.size()) {
			distance += (correctWordPhonemes.size() - responsePhonemes.length);
		}
		
		for (int i = 0; i<responsePhonemes.length; i++) {
			if (correctWordPhonemes.contains(responsePhonemes[i]))
				correctWordPhonemes.remove(responsePhonemes[i]);
			
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
	private static ArrayList<String> binarySearch(RandomAccessFile raf, String target)
            throws IOException {
		ArrayList<String> possibleAnswers =  new ArrayList<String>();
        raf.seek(0);
        String line = raf.readLine();

        if (line.compareTo(target) == 0) {
        	String [] result = line.split("\t");
    		
    		possibleAnswers.add(line);      		
    		
    		//read next line
    		line = raf.readLine();        		
    		while (line.split("\t")[0].equalsIgnoreCase(target)) {
    			possibleAnswers.add(line);
    			line = raf.readLine();
    		}
    		
    		for (String ans : possibleAnswers) {
    			System.out.println("possible answer: " + ans);
    		}
       
        	//phonemic transcription of the word
    		return possibleAnswers;
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

        		possibleAnswers.add(line);
        		
        		//read next line
        		line = raf.readLine();        		
        		while (line.split("\t")[0].equalsIgnoreCase(target)) {
        			possibleAnswers.add(line);
        			line = raf.readLine();
        		}
        		
        		return possibleAnswers;
        	}
  		
        	else
                break;
        }

        // Nothing found
        return null;

    }
}
