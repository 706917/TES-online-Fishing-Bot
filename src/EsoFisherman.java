/** Java class to provide automated performance in TES Online game.
 * Automated actions - fishing
 * 
 * Current version works only with following conditions:
 * 1 - The monitor has resolution 1920 x 1080
 * 1 - The game runs on full screen mode
 * 2 - VOTAN FISHERMAN ADDON installed and activated in the game
 * to provide specified color changers of monitored pixels.
 * 
 * Future releases will handle these constrains
 */

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;



public final class EsoFisherman{
	
//*********** DATA FIELDS *******************//	
	
	//----------- Finals ----------------//
	// Coordinates of Main Pixel to check color changes at the event to Reel In is needed
	final  static int  X = 1076;
	final  static int  Y = 635;
	// RGB value of Main Pixel. This value means that fish present at the fishing spot and you may throw your fishing rod
	final  static int POINT_RGB = -16777216;
	//----------------------------------//

	//----------- COUNTERS--------------//
	// Variable to hold the COUNTER - number of fish robot caught today. Robot will save this value to the specified file	
	int totalCaughtToday;
	// Variable to hold the COUNTER - number of fish robot caught in whole history. Robot will save this value to the specified file	
	int totalCaught;
	//-----------------------------------//
	
	//----------- STORAGES---------------//
	
	// Variable to hold reference to the object associated with the file-storage of all values of "time before reel in". 
	// We calculate the "time before reel in" at each fishing event and store the values in the file for the sake of statistic analysis.
	File timesFile;
	
	// Variable to hold values of "time before reel in" during current fishing event. We read the data of previous events from file to this ArrayList.
	// And the data from this ArrayList will be saved back to the file after updating with new data of current fishing event. 
	// At the end of current fishing event we will use all data in this ArrayList to find and display the minimum value in whole history.
	ArrayList<Integer> timerBeforeReelIn;
	
	// Variable to create a LocalDate object for further obtaining the current date 
	LocalDate today;
	
	// Variable to hold reference to the object associated with the file-storage of amount of fish caught today. 
	// This object obtains the path to the file with support of "LocalDate today" variable.
	File caughtTodayFile;
	
	// Variable to hold reference to the object associated with the file-storage of amount of fish caught in whole history
	File caughtTotalFile;
	//-----------------------------------//
	
	
//************************************//	
	// Default Constructor
	EsoFisherman() throws IOException{
	
	// Initialize storage variables at the creation of object
	timeSteps();
	catchCounters();
	
	}
	
	
			
	 
	 //Method to press ALT-TAB button automatically to switch between IDE  and the game
	 public  void alt_tab() throws AWTException {
		Robot switcher = new Robot();
		
		// the delay added to give a time to move hands from mouse and take comfortable sit or go completely away from keyboard
		switcher.delay((int)Math.random()*6000 + 3000); 
		
		// Use this block if you have one monitor
//		switcher.keyPress(KeyEvent.VK_ALT);
//		switcher.keyPress(KeyEvent.VK_CONTROL);
//		switcher.keyPress(KeyEvent.VK_TAB);
//		switcher.delay(100);
//		switcher.keyRelease(KeyEvent.VK_ALT);
//		switcher.keyRelease(KeyEvent.VK_CONTROL);
//		switcher.keyRelease(KeyEvent.VK_TAB);
//		
//		switcher.keyPress(KeyEvent.VK_ENTER);
//		switcher.delay(50);
//		switcher.keyRelease(KeyEvent.VK_ALT);
//		
		// Use this block if u have two monitors - one for the game, one for the IDE
		switcher.mouseMove((int)Math.random()*1000, (int)Math.random()*600);
		switcher.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		switcher.delay(100);
		switcher.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
		
		System.out.println("Windows switched.");
	}
	 
	// Method to initialize counters
		private void catchCounters() throws IOException{
		
		
		// Create a LocalDate object to obtain current date
		today =  LocalDate.now();
		// String to hold the path to the file with the data associated with current date
		String path = "src/data/fish/" + today.toString() + ".txt"; 
		
		// Create a File object and assign it to the variable
		caughtTodayFile = new File(path);
		// If file doesn't exist - create it
		if(caughtTodayFile.createNewFile()) {System.out.println("New file for the day data created.");}
		
		// Read the data from file and assign the value to the variable. If no data - set the variable to zero.	
		Scanner caughtTodayInput = new Scanner (caughtTodayFile);
		totalCaughtToday = (caughtTodayInput.hasNext() == true ) ?  caughtTodayInput.nextInt() : 0;	
		caughtTodayInput.close();
		System.out.println("\"totalCaughtToday\" variable initialized. The value is: " + totalCaughtToday);
		
		
		// Read the data from file and assign the value to the variable
		caughtTotalFile = new File("src/TotalCount.txt");
		if(caughtTotalFile.createNewFile()) {System.out.println("New file for the Total data created.");}
		
		// Read the value from file and assign it to the variable. If no data - set the variable to zero.
		Scanner caughtTotalInput = new Scanner(caughtTotalFile);	
		totalCaught = (caughtTotalInput.hasNext() == true)? caughtTotalInput.nextInt() : 0;
		caughtTotalInput.close();
		System.out.println("\"totalCaught\" variable initialized. The value is: " + totalCaught);
				
		}
		
	 
	// Method to initialize timerBeforeReelIn ArrayList
		private void timeSteps() throws IOException {
		// Read the file at specified location. If not exist - create it
		timesFile = new File ("src/AllTimeStepsToReelIn.txt");
		if(timesFile.createNewFile()) {System.out.println("New file for the time-steps storage created.");}
		
		// Copy the values inside created ArrayList. We use the ArrayList, but array since the amount of values in the file increase with time
		Scanner input = new Scanner(timesFile);
		ArrayList<Integer> timerBeforeReelIn = new ArrayList<Integer>();
		
		if (input.hasNext() == true) { // what is more efficient: (input.hasNext() == true) or (timesFile.length() > 0)  ?
			do {
			timerBeforeReelIn.add(input.nextInt());
			} while(input.hasNext());
		}
		else timerBeforeReelIn.add(100);
		input.close();
		
		System.out.println("\"timerBeforeReelIn\" ArrayList initialized. Current min /max is: " + Collections.min(timerBeforeReelIn) + "/" + Collections.max(timerBeforeReelIn) );
		}
		
	 // Method to fish
	 
	 public void fish() throws AWTException, FileNotFoundException {
		 
	 // Create new Robot object		 
	 Robot fisher = new Robot();
	// System signal to let user know that Robot is ON
	Toolkit.getDefaultToolkit().beep();
	System.out.println("\nRobot started.");
		 
	 // Temporary variable to count number of fish we caught at current spot
	 int fishCounter = 0;
	 System.out.println("\"fishCounter\" local variable initialized. The value is: " + fishCounter);
	 
	 
	 
	 
	 
	 // Switch to the game window
	 alt_tab();
		
	// Consistently throw the fishing rod into the spot and reel in at the proper moment	
		
		do {
			// Throw the rod
			fisher.keyPress(KeyEvent.VK_E);
			fisher.delay(50);
			fisher.keyRelease(KeyEvent.VK_E);
			System.out.println("\nRod throwed.");
			
			// Start count the time before reel in
			long start = System.currentTimeMillis();
			
			// Consistently check the color of Main Pixel.
			// Stop at the moment the color changed and continue the outer loop
			do {
				 fisher.getPixelColor(X, Y).getRGB();
			}
			while (fisher.getPixelColor(X, Y).getRGB() == POINT_RGB);
			
			// Stop counting the time before reel in
			long finish = System.currentTimeMillis();
			
			// Imitate human reaction delay 
			fisher.delay((int) (Math.random()*2000+300));
			
			// Reel in
			fisher.keyPress(KeyEvent.VK_E);
			fisher.delay((int) (Math.random()*10+50));
			fisher.keyRelease(KeyEvent.VK_E);
			
			// Imitate human reaction - fisherman looking at his catch and deciding what to do
			fisher.delay((int) (Math.random()*2200 + 500));
			
			// Put the catch in the bag
			fisher.keyPress(KeyEvent.VK_R);
			fisher.delay((int) (Math.random()*10+50));
			fisher.keyRelease(KeyEvent.VK_R);
				
			// Calculate the time before reel in
			timerBeforeReelIn.add((int)(finish - start) / 1000);
			// Increase local counter by 1 
			fishCounter++;
			// Display the message about event state
			System.out.println("Just caught one more fish. Total at this spot is:____ " + fishCounter);
			// Imitate human reaction delay before next throw the rod
			fisher.delay((int) (Math.random()*3000+650));
			
		// Fish while the color of Main Pixel equal to the proper value. Change of color means that fish is gone.	
		} while(fisher.getPixelColor(X, Y).getRGB() == POINT_RGB);
		// Display the END-Event message
		System.out.println("\nNothing to catch!\n");
		
		// System signal to let user know that Robot is OFF
		Toolkit.getDefaultToolkit().beep();
		
		
		//--------- Section that saves the data to the file-storages and display final statistic ---------//
		
		// Save the new data of Total caught
		totalCaught += fishCounter;
		PrintWriter output = new PrintWriter(caughtTotalFile);
		output.print(totalCaught);
		output.close();
		
		// Save the new data of Today caught
		totalCaughtToday += fishCounter;
		PrintWriter outputDay = new PrintWriter(caughtTodayFile);
		outputDay.print(totalCaughtToday);
		outputDay.close();
		
		// Display statistics at the current time
		System.out.println("At this spot i caught " + fishCounter + " fish. \nTotal today is " + totalCaughtToday + 
				"\nTotal caught is " + totalCaught);
		
		System.out.print("The minimum time before reel in is : " + Collections.min(timerBeforeReelIn) + " seconds\n");
		
//		int min = timerBeforeReelIn.get(0);
//		for (int i = 1; i< timerBeforeReelIn.size(); i++) {
//			if (timerBeforeReelIn.get(i) < min)
//				min = timerBeforeReelIn.get(i);
//		}
//			System.out.println(min+ " seconds\n");
			

		// Save the updated data of "time before reel in"	
		PrintWriter outTimes = new PrintWriter(timesFile);
		for (Integer time : timerBeforeReelIn)
				outTimes.println(time + " ");
		outTimes.close();
}
}

	


