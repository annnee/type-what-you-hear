import gameFiles.UserResponse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;

import com.google.gson.Gson;

//a test class to test some methods
public class TestClass {

	public static void main(String[] args) {
		String test = "hey";
		
		
		/*try {
			File f = new File(System.getProperty("user.dir")+"/WebContent/scoringFiles/beep-1.0");
			if( f.isFile() && f.canRead() && (f.length() > 0) ){
                RandomAccessFile raf = new RandomAccessFile(f, "r");
                
                if(result!=null){
                    // Match found.
                	System.out.println(result);
                }
                else {
                	System.out.println("no match found :(");
                }
                // No Match
                raf.close();
            }
			else {
				System.out.println("cant read soz");
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		try {		
			File homophonesTextFile = new File(System.getProperty("user.dir")+"/WebContent/scoringFiles/english3369_homophones.txt");
			File phonemeTextFile = new File(System.getProperty("user.dir")+"/WebContent/scoringFiles/beep-1.0");
			RandomAccessFile homophonesRAF = new RandomAccessFile(homophonesTextFile, "r");
			RandomAccessFile phonemeRAF = new RandomAccessFile(phonemeTextFile, "r");
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(levenshteinDistance("w ao r", "w er s t"));
		
		/*
		Gson gson = new Gson();
		UserResponse s = new UserResponse("nee", 1, "vastly");
		System.out.println(gson.toJson(s));
		String jsonString = "{\"username\":\"register\", \"pokemon\":\"pikachu\"}";
		UserResponse a = gson.fromJson(jsonString, UserResponse.class);
		System.out.println(a.getUsername());
		System.out.println(a.getUserResponse());
		System.out.println(a.getClipID());
		*/
		
		
	}
	/**
	 * 
	 * @param userResponse
	 * @param correctAnswer
	 * @return the edit distance between the two phonemic transcriptions
	 */
	public static int levenshteinDistance(String userResponse, String correctAnswer) {
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
}
