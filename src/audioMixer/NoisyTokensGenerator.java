package audioMixer;

import java.io.*;
import java.util.*;

import database.DBInterface;

public class NoisyTokensGenerator {
	private static Random randomGen = new Random();
	private static String userDir = System.getProperty("user.dir");
	
	/*private static final double EASY_BAB = 0;
	private static final double MEDIUM_BAB = -7.5;
	private static final double HARD_BAB = -15.0;*/
	
	private static final double EASY = 0;
	private static final double MEDIUM = -6.0;
	private static final double HARD = -12.0;
	
	public static void main(String[] args) throws Exception {
		/*generateToken(EASY, selectRandomFile(getWordFileNames()), "EN_ssn.wav");
		generateToken(MEDIUM, selectRandomFile(getWordFileNames()), "EN_ssn.wav");
		generateToken(HARD, selectRandomFile(getWordFileNames()), "EN_ssn.wav");
				
		generateToken(EASY, selectRandomFile(getWordFileNames()), "EN_bmn8.wav");
		generateToken(MEDIUM, selectRandomFile(getWordFileNames()), "EN_bmn8.wav");
		generateToken(HARD, selectRandomFile(getWordFileNames()), "EN_bmn8.wav");
				
		generateToken(EASY, selectRandomFile(getWordFileNames()), "EN_bab8.wav");
		generateToken(MEDIUM, selectRandomFile(getWordFileNames()), "EN_bab8.wav");
		generateToken(HARD, selectRandomFile(getWordFileNames()), "EN_bab8.wav");
		
		generateToken(EASY, selectRandomFile(getWordFileNames()), "EN_revbab8.wav");
		generateToken(MEDIUM, selectRandomFile(getWordFileNames()), "EN_revbab8.wav");
		generateToken(HARD, selectRandomFile(getWordFileNames()), "EN_revbab8.wav");*/
	}
	
	/**
	 * Generates 4 'easy', 3 'medium', and 3 'difficult' noisy tokens 
	 * @param words
	 * @param noises
	 * @return a list of noisy tokens
	 */
	public static ArrayList<String> generateNoisyTokens(ArrayList<String> words, ArrayList<String> noises) {
		//shuffle both lists and get one item from each list
		
		
		//check if this particular combination exists
		
		
		//mix them with the current difficulty
		
		
		//add to list of noisy tokens
		
		
		//to be replaced with an array that contains the noisy tokens
		return null;
	}
	
	/**
	 * Retrieves all of the file names in the words folder
	 * @return an array containing all the file names
	 */
	private static ArrayList<String> getWordFileNames() {
		// Directory path here		
		String wordsFilePath = userDir + "\\soundFiles\\Words";
		
		File wordsFolder = new File(wordsFilePath);
		File[] wordsFiles = wordsFolder.listFiles();
		String[] wordsFileNames = new String[wordsFiles.length];
		
		for (int i = 0; i < wordsFiles.length; i++) {
			wordsFileNames[i] = wordsFiles[i].getName();
		}

		return new ArrayList<String>(Arrays.asList(wordsFileNames));
	}
	
	/**
	 * Retrieves all of the file names in the noise folder
	 * @return an array containing all the file names
	 */
	private static ArrayList<String> getNoiseFileNames() {
		String noiseFilePath = userDir + "\\soundFiles\\Noise";
		
		File noiseFolder = new File(noiseFilePath);
		File[] noiseFiles = noiseFolder.listFiles();
		String[] noiseFileNames = new String[noiseFiles.length];
		
		for (int i = 0; i < noiseFiles.length; i++) {
			noiseFileNames[i] = noiseFiles[i].getName();
		}
		
		return new ArrayList<String>(Arrays.asList(noiseFileNames));
	}
	
	/**
	 * Select a random file
	 * @param fileNamesArray
	 * @return randomly selected file
	 */
	public static String selectRandomFile(ArrayList<String> fileNamesArray) {
		
		int ranNum = randomGen.nextInt(fileNamesArray.size());
		return fileNamesArray.get(ranNum);
	}
	
	private static double computeRMS(double[] buffer) {
		double rms = 0;
		
		for (int i = 0; i<buffer.length; i++) {
			rms += Math.pow(buffer[i], 2);
		}
		rms = Math.sqrt(rms/buffer.length);
		
		return rms;
	}
	
	/**
	 * Generates a noisy token; it assumes this particular combination of word, noise 
	 * and SNR hasn't already been generated
	 * @param desiredSNR
	 * @param wordFilename
	 * @param noiseFilename
	 */
	public static void generateToken(double desiredSNR, String wordFilename, String noiseFilename) {
		try {
			//get auto_increment value from DB - this will be the filename of the noisy token
			String mixedFilename = DBInterface.getTokenAutoIncVal();
			
			File wordFile = new File(userDir + "\\soundFiles\\Words\\" + wordFilename);
			File noiseFile = new File(userDir + "\\soundFiles\\Noise\\" + noiseFilename);
			File mixedFile = new File(userDir + "/WebContent/sound/" +mixedFilename+ ".wav");
			
			String word = wordFilename.split("_")[0];
			String noise = noiseFilename.split("_")[1];
			
			//open the wav files
			WavFile wavWordFile = WavFile.openWavFile(wordFile);
			WavFile wavNoiseFile = WavFile.openWavFile(noiseFile);
			mixedFile.createNewFile();
			
			// Get the number of audio channels in the wav file
	        int numChannelsWord = wavWordFile.getNumChannels();
	        int numChannelsNoise = wavNoiseFile.getNumChannels();
			
			// Get number of samples
			int numSpeechFrames = (int)wavWordFile.getNumFrames();
			int numNoiseFrames = (int)wavNoiseFile.getNumFrames();

			// Create buffers
			double[] wordBuffer = new double[numSpeechFrames * numChannelsWord];
			double[] noiseBuffer = new double[numNoiseFrames * numChannelsNoise];
	
			int framesReadWord;
			int framesReadNoise;
			
			// Read frames into word buffer
			do
			{
				framesReadWord = wavWordFile.readFrames(wordBuffer, numSpeechFrames);
			}
			while (framesReadWord != 0);
			
			// Read frames into noise buffer
			do
			{
				framesReadNoise = wavNoiseFile.readFrames(noiseBuffer, numNoiseFrames);	
			}
			while (framesReadNoise != 0);
			
			//offset for selecting which segment of the noise file to use	
			int noiseOffset = randomGen.nextInt(numNoiseFrames-numSpeechFrames);
			
			//the part of the noise file that will be mixed with the word buffer
			double[] noiseBufferSegment = new double[numSpeechFrames * numChannelsWord];
			
			for (int i = 0; i<noiseBufferSegment.length; i++) {
				noiseBufferSegment[i] = noiseBuffer[i+noiseOffset];
			}
			
			// Compute RMS for speech and noise signals
			double speechRMS = computeRMS(wordBuffer);
			double noiseRMS = computeRMS(noiseBufferSegment);
			
			//normalize the noise signal
			for (int i = 0; i<noiseBufferSegment.length; i++) {
				noiseBufferSegment[i] = noiseBufferSegment[i]/noiseRMS * speechRMS;				
			}
			
			// Adjust the SNR of the noisy token
			double c = (speechRMS/noiseRMS)*Math.pow(10, (-desiredSNR/20));			
			for (int i = 0; i<noiseBufferSegment.length; i++) {
				noiseBufferSegment[i] = noiseBufferSegment[i]*c;
				
			}

			// Create a wav file with the name specified as the first argument
			WavFile mixedWavFile = WavFile.newWavFile(mixedFile, 1, numSpeechFrames, 16, 16000);
			
			// Buffer for the mixed wav file
			double[] buffer = new double[numSpeechFrames];

			// Initialise a local frame counter
			long frameCounter = 0;
			
			// Loop until all frames written
			while (frameCounter < numSpeechFrames)
			{
				// Determine how many frames to write, up to a maximum of the buffer size
				long remaining = mixedWavFile.getFramesRemaining();
				int toWrite = (remaining > numSpeechFrames) ? numSpeechFrames : (int) remaining;

				// Fill the buffer
				for (int s=0 ; s<toWrite ; s++, frameCounter++)
				{
					buffer[s] = wordBuffer[s] + noiseBufferSegment[s];				
				}				

				// Write the buffer
				mixedWavFile.writeFrames(buffer, toWrite);
			}
			
			// Close the wav files
			wavWordFile.close();
			wavNoiseFile.close();
			mixedWavFile.close();
			
			//add info to database
			DBInterface.storeTokenInfo(word, noise, desiredSNR, noiseOffset);
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}