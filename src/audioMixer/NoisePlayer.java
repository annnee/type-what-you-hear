package audioMixer;

import javax.sound.sampled.*;

import java.io.*;
import java.util.*;

public class NoisePlayer {
	static Random randomGen = new Random();
	
	public static void main(String[] args) throws Exception {
		processFile();	
		//System.out.println("Servlet Directory = " + System.getProperty("user.dir"));
	}
	
	public static String[] getWordFileNames() {
		// Directory path here		
		String wordsFilePath = "C:\\Users\\Ann Nee\\Documents\\Uni\\Java\\Dissertation\\soundFiles\\Words";
		
		File wordsFolder = new File(wordsFilePath);
		File[] wordsFiles = wordsFolder.listFiles();
		String[] wordsFileNames = new String[wordsFiles.length];
		
		for (int i = 0; i < wordsFiles.length; i++) {
			wordsFileNames[i] = wordsFiles[i].getName();
		}
		return wordsFileNames;
	}
	
	public static String[] getNoiseFileNames() {
		String noiseFilePath = "C:\\Users\\Ann Nee\\Documents\\Uni\\Java\\Dissertation\\soundFiles\\Noise";
		
		File noiseFolder = new File(noiseFilePath);
		File[] noiseFiles = noiseFolder.listFiles();
		String[] noiseFileNames = new String[noiseFiles.length];
		
		for (int i = 0; i < noiseFiles.length; i++) {
			noiseFileNames[i] = noiseFiles[i].getName();
		}
		return noiseFileNames;
	}

	public static String generateFile(String[] fileNamesArray) {
		
		int ranNum = randomGen.nextInt(fileNamesArray.length);
		return fileNamesArray[ranNum];
	}
	
	public static String processFile() {
		String randomWordFile = generateFile(getWordFileNames());
		String randomNoiseFile = generateFile(getNoiseFileNames());
		String userDir = System.getProperty("user.dir");
		System.out.println("Word File: " + randomWordFile + "\t Noise File: " + randomNoiseFile);
		File wordFile = new File(userDir + "\\soundFiles\\Words\\" + randomWordFile);
		File noiseFile = new File(userDir + "\\soundFiles\\Noise\\" + randomNoiseFile);
		File mixedFile = new File(userDir + "/WebContent/sound/" + randomWordFile + randomNoiseFile);
				
		try {
			mixedFile.createNewFile();
			//open the wav files
			WavFile wavWordFile = WavFile.openWavFile(wordFile);
			WavFile wavNoiseFile = WavFile.openWavFile(noiseFile);
			
			// Display information about the wav file
			wavWordFile.display();

			// Get the number of audio channels in the wav file
			int numChannels = wavWordFile.getNumChannels();
			int numWordFrames = (int)wavWordFile.getNumFrames();
			int numNoiseFrames = (int)wavNoiseFile.getNumFrames();

			// Create buffers
			double[] soundBuffer = new double[numWordFrames];
			double[] noiseBuffer = new double[numNoiseFrames];

			int framesRead;
			double min = Double.MAX_VALUE;
			double max = Double.MIN_VALUE;

			do
			{
				// Read frames into buffer
				framesRead = wavWordFile.readFrames(soundBuffer, numWordFrames);
				wavNoiseFile.readFrames(noiseBuffer, numNoiseFrames);

				// Loop through frames and look for minimum and maximum value
				for (int s=0 ; s<framesRead * numChannels ; s++)
				{
					if (soundBuffer[s] > max) max = soundBuffer[s];
					if (soundBuffer[s] < min) min = soundBuffer[s];
					if (noiseBuffer[s] > max) max = noiseBuffer[s];
					if (noiseBuffer[s] < min) min = noiseBuffer[s];
				}
			}
			while (framesRead != 0);
			
			//mix the two files
			int sampleRate = 16000;		// Samples per second

			// Create a wav file with the name specified as the first argument
			WavFile mixedWavFile = WavFile.newWavFile(mixedFile, 1, numWordFrames, 16, sampleRate);
			
			//buffer for the mixed wav file
			double[] buffer = new double[numWordFrames];

			// Initialise a local frame counter
			long frameCounter = 0;
			
			//offset for selecting which segment of the noise file to use
			int noiseOffset = randomGen.nextInt(numNoiseFrames-numWordFrames);

			// Loop until all frames written
			while (frameCounter < numWordFrames)
			{
				// Determine how many frames to write, up to a maximum of the buffer size
				long remaining = mixedWavFile.getFramesRemaining();
				int toWrite = (remaining > numWordFrames) ? numWordFrames : (int) remaining;

				// Fill the buffer
				for (int s=0 ; s<toWrite ; s++, frameCounter++)
				{
					buffer[s] = soundBuffer[s] + (0.5*noiseBuffer[noiseOffset + s]);
					if (buffer[s] > max)
						buffer[s] = max;
					if (buffer[s] < min)
						buffer[s] = min;
				}

				// Write the buffer
				mixedWavFile.writeFrames(buffer, toWrite);
			}
			
			// Close the wav files
			wavWordFile.close();
			wavNoiseFile.close();
			mixedWavFile.close();

			System.out.println("Mixing complete");			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return System.getProperty("user.dir") + "\\soundFiles\\MixedFiles\\" + randomWordFile + randomNoiseFile;

	}
}