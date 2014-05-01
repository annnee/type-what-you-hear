package audioMixer;

import java.io.*;
import java.util.*;

import database.DBInterface;

public class NoisyTokensGenerator {
	private static Random randomGen = new Random();

	private static String userDir = "";
	
	private static ArrayList<String> wordFiles;
	private static ArrayList<String> noiseFiles;
	
	//{easy SNR, medium SNR, hard SNR}
	private static final double[] SNR = {15.0, 5.0, 0.0}; 
	
	public static void main(String[] args) throws Exception {		
		/*long startTime = System.nanoTime();
		initFiles();
		long endTime = System.nanoTime();
		long duration = endTime - startTime;
		System.out.println(duration/1000000 + " milliseconds");
		
		generateNoisyTokens(15, 3);
		generateNoisyTokens(4, 3);
		generateNoisyTokens(-1, 3);*/
	}
	
	public static void initFiles() {
		if (System.getProperty("user.dir").contains("Ann Nee")) 
			userDir = "C:/Users/Ann Nee/Documents/Uni/Java/Dissertation/WebContent/";
		
		else {
			userDir = "mytomcat/webapps/dissertation/";
		}
		
		wordFiles = getWordFileNames();
		noiseFiles = getNoiseFileNames();
	}	
	
	public static double getEasySNR() {
		return SNR[0];
	}
	
	public static double getMediumSNR() {
		return SNR[1];
	}
	
	public static double getHardSNR() {
		return SNR[2];
	}
	
	/**
	 * Generates a user-specified number of noisy tokens 
	 * @param SNR
	 * @param counter
	 * @return
	 */
	public static ArrayList<String> generateNoisyTokens(double SNR, int counter) {
		ArrayList<String> tokens = new ArrayList<String>();
		if (counter < 0) {
			return tokens;
		}
		Collections.shuffle(wordFiles);
		Collections.shuffle(noiseFiles);
		
		for (int i=0; i<counter; i++) {
			tokens.add(generateToken(SNR, wordFiles.get(i), noiseFiles.get(i)));
		}
		// add to list of noisy tokens	
		return tokens;
	}
	
	/**
	 * Retrieves all of the file names in the words folder
	 * @return an array containing all the file names
	 */
	private static ArrayList<String> getWordFileNames() {
		// Directory path here	
		String wordsFilePath = userDir + "/soundFiles/Words";
		
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
		String noiseFilePath = userDir + "/soundFiles/Noise";
		
		File noiseFolder = new File(noiseFilePath);
		File[] noiseFiles = noiseFolder.listFiles();
		String[] noiseFileNames = new String[noiseFiles.length];
		
		for (int i = 0; i < noiseFiles.length; i++) {
			noiseFileNames[i] = noiseFiles[i].getName();
		}
		
		return new ArrayList<String>(Arrays.asList(noiseFileNames));
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
	private static String generateToken(double desiredSNR, String wordFilename, String noiseFilename) {
		String mixedFilename = "";
		try {
			//get auto_increment value from DB - this will be the filename of the noisy token
			mixedFilename = DBInterface.getTokenAutoIncVal()+".wav";
			//mixedFilename = wordFilename + " " + noiseFilename + " " + desiredSNR + ".wav";
			File wordFile = new File(userDir + "/soundFiles/Words/" + wordFilename);
			File noiseFile = new File(userDir + "/soundFiles/Noise/" + noiseFilename);
			File mixedFile = new File(userDir + "/sound/" + mixedFilename);
			
			String noise = noiseFilename.split("_")[1];
			noise = noise.substring(0, noise.length()-4); //remove the ".wav" part
			String word = wordFilename.substring(0, wordFilename.length()-4);
			
			
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
			
			// Adjust the noisiness of the noise signal
			double c = (speechRMS/noiseRMS)*Math.pow(10, (-desiredSNR/20));
			for (int i = 0; i<noiseBufferSegment.length; i++) {
				noiseBufferSegment[i] = noiseBufferSegment[i]*c;
			}

			// Create a wav file with the name specified as the first argument
			WavFile mixedWavFile = WavFile.newWavFile(mixedFile, 1, numSpeechFrames, 16, 16000);
			
			// Buffer for the mixed wav file
			double[] buffer = new double[numSpeechFrames];
			double max = 0.0;
			
			// Fill the buffer
			for (int s = 0 ; s < buffer.length; s++)
			{
				buffer[s] = wordBuffer[s] + noiseBufferSegment[s];
				if (buffer[s]>1.0) {
					max = buffer[s];
				}	
			}
			
			//scale the signal exceeds the max value
			if (max!=0.0) {
				for (int s = 0 ; s < buffer.length; s++)
				{
					buffer[s] = buffer[s]*0.99/max;
				}
			}
			
			// Write the buffer
			mixedWavFile.writeFrames(buffer, numSpeechFrames);
						
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
		return mixedFilename;
	}	
}