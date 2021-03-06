/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guioptimiser;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Mahmoud-Uni
 */
public class GuiOptimiser {

    //private static String TARGET_APP = "calculator.jar";
     private static String TARGET_APP = "simpleApp.jar";
    private static final String TARGET_APP_COLOR = "color.csv";
    private static final int TARGET_APP_RUNNINGTIME = 2000;
    private static final String JAVA_COMMAND = "java -jar ";
    private static String parentDir = "";
    private static int screenshotsNum = 1000;
    private static int screenshot = 1;
    private static ArrayList<Long> results = new ArrayList<Long>();
    private static String line = "";
    private static long solution = 999999999;
    private static long bestsolution = 999999999;
    private static int RS = 3;
    
    //simulated annealing operators
    private static double temperature = 110.0;
    private static double coolingRate = 0.99;
    private final static double minTemperature = 0.5;
    private static ArrayList<ArrayList<Integer>> randomRGB = new ArrayList<>();
    private static Random randomGenerator = new Random();
    private static int saNeigbourhoodSize = 200;
    private static int sanumberOfNeighbours = 20 ;
    
 
    private static int noResult = 0;
    
    private static ArrayList<String> guiComponents = new ArrayList<>();
    private static ArrayList<ArrayList<Integer>> RGB = new ArrayList<>();
    private static ArrayList<ArrayList<Integer>> prevRGB = new ArrayList<>();
    
    private static ArrayList<ArrayList<Integer>> RGBNew = new ArrayList<>();
    private static Random randomInt = new Random();
    
    private static String filename = "";

    private static int numberOfScreenShots = 0;
    private static int hillClimbingNeigbourhoodSize = 50;
    private static int numberOfNeighbours = 20;

    /**
     * @param args the command line arguments
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException{
        // first run the target app
        switch (args[0]) {
            case "calculator.jar":
                TARGET_APP = args[0].trim();
                System.out.println(args[0]);
                break;
            case "simpleApp.jar":
                TARGET_APP = args[0].trim();
                System.out.println(args[0]);
                break;
            default:
                System.out.println(args[0]);
                return;
        }
        parentDir = getParentDir();
        solution = solution*solution;

        //System.out.println(parentDir.concat(TARGET_APP));
        for (int i = 0; i < screenshotsNum; i++) //RunTargetApp runTargetApp = new RunTargetApp(parentDir.concat(TARGET_APP), TARGET_APP_RUNNINGTIME);
        {
            //runApp(parentDir.concat(TARGET_APP), TARGET_APP_RUNNINGTIME);
            //runApp(TARGET_APP, TARGET_APP_RUNNINGTIME);
            if(RS == 1) {
            	randomSearch(screenshotsNum,i);
            } 
            if(RS == 3) {
            	simulatedAnnealingSearch(screenshotsNum,i,  saNeigbourhoodSize, sanumberOfNeighbours);
            }
        }
        if(RS == 2){
            hillClimbingSearch(screenshotsNum, hillClimbingNeigbourhoodSize, numberOfNeighbours);
        }
      
        writeResultsToExcel(parentDir.concat("finalResults.csv"),results);

    }

    public static void runApp(String path, int targetAppRunningtime) {
        try {
            //java -jar C:\Users\Mahmoud-Uni\Documents\NetBeansProjects\calculator\dist\calculator.jar

            //path = "\""+path+"\"";
            //System.out.println("Target App" + path);
            
            Runtime runTime = Runtime.getRuntime();
            Process process = runTime.exec(JAVA_COMMAND.concat(path));
            try {
                Thread.sleep(targetAppRunningtime);
                Capture capture = new Capture();
                filename = capture.takeScreenShoot();
//                BufferedReader stdError = new BufferedReader(new
//                InputStreamReader(process.getErrorStream()));
//                String line = "";
//                while((line=stdError.readLine())!=null)
//                {
//                    System.out.println("error!");
//                    System.out.println(line);
//                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //System.out.println("Target App");
            process.destroy();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static long calculateChargeConsumptionPerPixel(String path) throws IOException {
        BufferedImage image = ImageIO.read(new File(path));
        int width = image.getWidth();
        int height = image.getHeight();
        int pixelCount = 0;
        long consumption = 0;

    	for (int x = 0; x < width; x++) {
    		for (int y = 0; y < height ; y++) {
    			
                int pixel = image.getRGB(x, y);
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = (pixel) & 0xff;
                
    	        consumption += red*(120) + green*(140) + blue*(240);

    	        pixelCount++;
    	    }
    	}
    	
    	//System.out.printf("pixelCount = %d Total Consumtion = %d Number = %d %n", pixelCount, consumption, screenshot);
        System.out.println(screenshot);
        
    	screenshot++;
    	return consumption/3686400;
    }

    public static void deleteCapture()  {
    	File file = new File(parentDir.concat(filename));
    	file.delete();
    }
    
    public static void randomSearch(int num, int count) throws IOException {
    	changeColorAll();
    	long result = calculateChargeConsumptionPerPixel(parentDir.concat(filename));
    	results.add( result );
    	
    	if ( result < solution) {
    		solution = result;
    	}
    	else {
    		deleteCapture();
    	}
    	System.out.println(solution);
    }

    public static void writeResultsToExcel(String filePath, ArrayList<Long> results){
        
        try {
            BufferedWriter br = new BufferedWriter(new FileWriter(new File(filePath)));
            String line = "";
            for (int i = 0; i < results.size(); i++) {
                line += results.get(i)+ "\n";
                //System.out.println(line);
            }
            br.write(line);
            br.flush();
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void changeColorAll() {
        try {
            // guiComponents contains GUI components' name.
            ArrayList<String> guiComponents = new ArrayList<>();
            guiComponents.add("mainFrameColor"); // both apps
            guiComponents.add("jButton1");// both apps
            guiComponents.add("jButton2");
            guiComponents.add("jButton3");
            guiComponents.add("jButton4");
            guiComponents.add("jButton5");
            guiComponents.add("jButton6");
            guiComponents.add("jButton7");
            guiComponents.add("jButton8");
            guiComponents.add("jButton9");
            guiComponents.add("jButton10");
            guiComponents.add("jButton11");
            guiComponents.add("jButton12");
            guiComponents.add("jButton13");
            guiComponents.add("jButton14");
            guiComponents.add("jButton15");
            guiComponents.add("jButton16");
            guiComponents.add("jButton17");
            guiComponents.add("jButton18");
            
            guiComponents.add("jTextField1");// both apps
            guiComponents.add("jTextField1TextColor");// both apps

            guiComponents.add("jLabel1");// both apps

            guiComponents.add("jPanel1");// both apps
            guiComponents.add("jPanel2");
            guiComponents.add("jPanel3");
            guiComponents.add("jPanel4");
            guiComponents.add("jPanel5");

            ArrayList<ArrayList<Integer>> RGB = new ArrayList<>();
            Random randomInt = new Random();

            RGB.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{randomInt.nextInt(256), randomInt.nextInt(256), randomInt.nextInt(256)})));
            RGB.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{randomInt.nextInt(256), randomInt.nextInt(256), randomInt.nextInt(256)})));
            RGB.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{randomInt.nextInt(256), randomInt.nextInt(256), randomInt.nextInt(256)})));
            RGB.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{randomInt.nextInt(256), randomInt.nextInt(256), randomInt.nextInt(256)})));
            RGB.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{randomInt.nextInt(256), randomInt.nextInt(256), randomInt.nextInt(256)})));
            RGB.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{randomInt.nextInt(256), randomInt.nextInt(256), randomInt.nextInt(256)})));
            RGB.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{randomInt.nextInt(256), randomInt.nextInt(256), randomInt.nextInt(256)})));
            RGB.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{randomInt.nextInt(256), randomInt.nextInt(256), randomInt.nextInt(256)})));
            RGB.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{randomInt.nextInt(256), randomInt.nextInt(256), randomInt.nextInt(256)})));
            RGB.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{randomInt.nextInt(256), randomInt.nextInt(256), randomInt.nextInt(256)})));
            RGB.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{randomInt.nextInt(256), randomInt.nextInt(256), randomInt.nextInt(256)})));
            RGB.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{randomInt.nextInt(256), randomInt.nextInt(256), randomInt.nextInt(256)})));
            RGB.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{randomInt.nextInt(256), randomInt.nextInt(256), randomInt.nextInt(256)})));
            RGB.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{randomInt.nextInt(256), randomInt.nextInt(256), randomInt.nextInt(256)})));
            RGB.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{randomInt.nextInt(256), randomInt.nextInt(256), randomInt.nextInt(256)})));
            RGB.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{randomInt.nextInt(256), randomInt.nextInt(256), randomInt.nextInt(256)})));
            RGB.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{randomInt.nextInt(256), randomInt.nextInt(256), randomInt.nextInt(256)})));
            RGB.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{randomInt.nextInt(256), randomInt.nextInt(256), randomInt.nextInt(256)})));

            RGB.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{randomInt.nextInt(256), randomInt.nextInt(256), randomInt.nextInt(256)})));
            RGB.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{randomInt.nextInt(256), randomInt.nextInt(256), randomInt.nextInt(256)})));
            RGB.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{randomInt.nextInt(256), randomInt.nextInt(256), randomInt.nextInt(256)}))); //jTextField1
            
            RGB.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{randomInt.nextInt(256), randomInt.nextInt(256), randomInt.nextInt(256)}))); //jTextField1TextColor
            
            while(Math.sqrt((RGB.get(20).get(0)-RGB.get(21).get(0))^2 + (RGB.get(20).get(1)-RGB.get(21).get(1))^2 + (RGB.get(20).get(2)-RGB.get(21).get(2))^2) < 128) {
            	RGB.get(21).set( 0, randomInt.nextInt(256));
            	RGB.get(21).set( 1, randomInt.nextInt(256));
            	RGB.get(21).set( 2, randomInt.nextInt(256));
            }

            RGB.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{randomInt.nextInt(256), randomInt.nextInt(256), randomInt.nextInt(256)})));
            RGB.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{randomInt.nextInt(256), randomInt.nextInt(256), randomInt.nextInt(256)})));
            RGB.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{randomInt.nextInt(256), randomInt.nextInt(256), randomInt.nextInt(256)})));
            RGB.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{randomInt.nextInt(256), randomInt.nextInt(256), randomInt.nextInt(256)})));
            RGB.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{randomInt.nextInt(256), randomInt.nextInt(256), randomInt.nextInt(256)})));

            saveToCSV(parentDir.concat(TARGET_APP_COLOR), guiComponents, RGB);

            prevRGB = RGB;
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void saveToCSV(String filePath, ArrayList<String> guiComponents, ArrayList<ArrayList<Integer>> RGB) {
        try {
            BufferedWriter br = new BufferedWriter(new FileWriter(new File(filePath)));
            String line = "";
            for (int i = 0; i < guiComponents.size(); i++) {
                line += guiComponents.get(i).concat(",").concat(RGB.get(i).toString().replace("[", "").replace("]", "").replaceAll("\\s", "")) + "\n";
                //System.out.println(line);
            }
            br.write(line);
            br.flush();
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            BufferedWriter br = new BufferedWriter(new FileWriter(new File(parentDir.concat("results.csv"))));
            
            for (int i = 0; i < guiComponents.size(); i++) {
            	if(i == (guiComponents.size()-1)){
            		line += RGB.get(i).toString().replace("[", "").replace("]", "").replaceAll("\\s", "") +"\n";
            	}
            	else {
            		line += RGB.get(i).toString().replace("[", "").replace("]", "").replaceAll("\\s", "") + ",";
            	}
                //System.out.println(line);
            }
            br.write(line);
            br.flush();
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getParentDir() {
        String dir = "";
        try {
            File temp = new File("temp");
            dir = temp.getAbsolutePath().replace("temp", "");
            //System.out.println(dir);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dir;
    }


    //hill climbing search with give size of neigbourhood
    public static void hillClimbingSearch(int totalScreenShots, int sizeOfNeighbourHood, int numberOfNeighbours) throws IOException{
        //the first claculator has a random intal value
        
        //generate a random RGB array
        changeColorAll();
        boolean betterNeighbourFound = true;

        //run method until we used up all the screenshots        
        while((numberOfScreenShots < totalScreenShots)){
            
            //generate neibouring solutions
            ArrayList<ArrayList<ArrayList<Integer>>> RGBNeigbours = GenerateNeighbours(prevRGB, sizeOfNeighbourHood, numberOfNeighbours);
            
            betterNeighbourFound = false;
            for (ArrayList<ArrayList<Integer>> RGBValue : RGBNeigbours) {
                //run app and save to file so app can read it
                addGUIComponetsCalculator();

                runApp(TARGET_APP, TARGET_APP_RUNNINGTIME);
                saveToCSV(parentDir.concat(TARGET_APP_COLOR), guiComponents, RGBValue);

                
                // get the result of the charge cosumtion
                long result = calculateChargeConsumptionPerPixel(parentDir.concat(filename));

                //check if solution is good
                if ( result < solution) {
                    solution = result;
                    prevRGB = RGBValue;
                    betterNeighbourFound = true;
                    results.add( result );
                    
                    System.out.println(solution);
                    System.out.println(filename);
                }
                else {
                    //delete bad solution screenshots
                    deleteCapture();
                }
                numberOfScreenShots++;
            }
            
            //reduce the scope of the neighbourhood
            if(sizeOfNeighbourHood > 5){
                float newSize = sizeOfNeighbourHood / 1.1f;
                sizeOfNeighbourHood = (int) newSize;
            }
            //if no new neigbours are found this is the local optimum so try anoter random starting point
            if(!betterNeighbourFound){
                System.out.println("no better Neighbour found generating another random");
                changeColorAll();
            }
        }
    }

    //adds the componets needed for the calculator
    public static void addGUIComponetsCalculator(){

        //guiComponents contains GUI components' name.
        guiComponents = new ArrayList<>();
        guiComponents.add("mainFrameColor"); // both apps
        guiComponents.add("jButton1");// both apps
        guiComponents.add("jButton2");
        guiComponents.add("jButton3");
        guiComponents.add("jButton4");
        guiComponents.add("jButton5");
        guiComponents.add("jButton6");
        guiComponents.add("jButton7");
        guiComponents.add("jButton8");
        guiComponents.add("jButton9");
        guiComponents.add("jButton10");
        guiComponents.add("jButton11");
        guiComponents.add("jButton12");
        guiComponents.add("jButton13");
        guiComponents.add("jButton14");
        guiComponents.add("jButton15");
        guiComponents.add("jButton16");
        guiComponents.add("jButton17");
        guiComponents.add("jButton18");
        
        guiComponents.add("jTextField1");// both apps
        guiComponents.add("jTextField1TextColor");// both apps

        guiComponents.add("jLabel1");// both apps

        guiComponents.add("jPanel1");// both apps
        guiComponents.add("jPanel2");
        guiComponents.add("jPanel3");
        guiComponents.add("jPanel4");
        guiComponents.add("jPanel5");
    }

    //returns a neigbourhood from a give RGB array
    public static ArrayList<ArrayList<ArrayList<Integer>>> GenerateNeighbours(ArrayList<ArrayList<Integer>> currentRGB, int sizeOfNeighbourHood, int numberOfNeighbours){
        ArrayList<ArrayList<ArrayList<Integer>>> allRGBNeigbours = new ArrayList<>();
        
        //generate neibours
        //loop over each RGB component
        for(int i = 0; i < numberOfNeighbours; i++){
            ArrayList<ArrayList<Integer>> neighbourRGB = new ArrayList<ArrayList<Integer>>();

            for(int j = 0; j < currentRGB.size(); j++){
                
                //get a random RGB index colour
                int index = randomInt.nextInt(3);
                int currentRGBValue = currentRGB.get(j).get(index);
                

                int randomNumber = randomInt.nextInt(3);
                //randomlly pick either to add or subtract colours or do nothing
                if(randomNumber == 0){
                    if(currentRGBValue - sizeOfNeighbourHood >= 0) currentRGBValue -= randomInt.nextInt(sizeOfNeighbourHood);
                }
                else if(randomNumber == 1){
                    if(currentRGBValue + sizeOfNeighbourHood <= 255) currentRGBValue += randomInt.nextInt(sizeOfNeighbourHood);
                }
                else{
                    //do nothing
                }
                
                //create array with the new values in it            
                ArrayList<Integer> newRGB = new ArrayList<Integer>(currentRGB.get(j));
                newRGB.set(index, currentRGBValue);
                
                neighbourRGB.add(newRGB);
                
            }

            //if neigbour doesn't satsify, dont add it to the neighbourhood
            if(isNotValidEuclidenDistance(neighbourRGB)){
                // System.out.println("Limted by Euclidean Distance");
            }else{
                allRGBNeigbours.add(neighbourRGB);
            }
        }
        return allRGBNeigbours;
    }

    public static boolean isNotValidEuclidenDistance(ArrayList<ArrayList<Integer>> inputRGB){
        ArrayList<Integer> colour1 = new ArrayList<Integer>();
        ArrayList<Integer> colour2 = new ArrayList<Integer>();
        if(TARGET_APP.equals("calculator.jar")){
            colour1 = inputRGB.get(20);
            colour2 = inputRGB.get(21);
        }
        if(TARGET_APP.equals("simpleApp.jar")){
            colour1 = inputRGB.get(2);
            colour2 = inputRGB.get(3);
        }

        if(EuclideanDistanceBetweenTheColours(colour1, colour2) < 128){
            return false;
        }
        return true;
    }

    //reutrns the difference between colors where ArrayList<Integer> = [red, green, blue]
    public static double EuclideanDistanceBetweenTheColours(ArrayList<Integer> colour1, ArrayList<Integer> colour2){
        
        double red = Math.pow(colour1.get(0) - colour2.get(0), 2);
        double green = Math.pow(colour1.get(1) - colour2.get(1), 2);
        double blue = Math.pow(colour1.get(2) - colour2.get(2), 2);

        double distance = Math.sqrt(red + green + blue);
        
        return distance;
    }
    
    public static void simulatedAnnealingSearch(int totalScreenShots, int current, int sizeOfNeighbourHood, int numberOfNeighbours) throws IOException{
    	
        //generate one random solution
        if(current == 0){
            changeColorAll();
        }

        //run method until 1000 screenshots are reached       
        while((numberOfScreenShots < totalScreenShots)){
        	
            //generate neighbouring solutions
            ArrayList<ArrayList<ArrayList<Integer>>> RGBNeigbours = GenerateNeighbours(prevRGB, sizeOfNeighbourHood,numberOfNeighbours);
            
            System.out.println("new neigbourhoods");
            System.out.println(RGBNeigbours.size());
            
            //variable for deciding the new neighbour usage
            boolean usenew = false;
            
            //loop through the neighbours
            
            for (ArrayList<ArrayList<Integer>> RGBValue : RGBNeigbours) {
            	
            	//pick one neighbour at random 
            	int index = randomGenerator.nextInt(RGBNeigbours.size());
            	randomRGB = RGBNeigbours.get(index);
            	//System.out.println("current " + RGBValue);
            	//System.out.println("random " + randomRGB);
            	
            	
            	//evaluate the chargeConsumption
                //run app and save to file so app can read it
                addGUIComponetsCalculator();
                runApp(TARGET_APP, TARGET_APP_RUNNINGTIME);
                
                //neighbour result
                long neighbourresult1 = calculateChargeConsumptionPerPixel(parentDir.concat(filename));
                //difference between neighbour/working solution  and current solution
                long chargedifference = solution - neighbourresult1;
            	
               
                System.out.println("current consumption " + solution);
                System.out.println("working consumption " + neighbourresult1);
                
                
                //check if current neighbour solution is good
                if ( neighbourresult1 < solution) {
                		usenew = true; 
                		solution = neighbourresult1;
                		
                }
                
                //allow bad neighbours with certain probability   p = exp(loss/temp) < 1
                else if (Math.exp((chargedifference) / temperature) < Math.random()) {                	
                	 solution = neighbourresult1;
                	 prevRGB = randomRGB;
                	 results.add(neighbourresult1 );
                	 usenew = true;
                	 System.out.println("worse neighbour, still allowed with certain probability");
                	 System.out.println(solution);
                    // System.out.println(filename);
     
                }else {
                    //delete bad solution screenshots
                    deleteCapture();
                }
                
                //if we are using the new value, compare it with previous best solutions
                if(usenew) {
                	usenew = false;
                	prevRGB = randomRGB;
            		if(neighbourresult1 < bestsolution ) {
            			bestsolution = neighbourresult1;
            			saveToCSV(parentDir.concat(TARGET_APP_COLOR), guiComponents, prevRGB);
            			
            		}else
            		{
            			solution = neighbourresult1;
            			saveToCSV(parentDir.concat(TARGET_APP_COLOR), guiComponents, prevRGB);
            			//delete unwanted screenshots
            			deleteCapture();
            			
            		}
            		
                	
                }
                
               
                System.out.println("best solution "+ bestsolution);
                System.out.println("file name " + filename);
                
               
              // RGBNeigbours = GenerateNeighbours(prevRGB, sizeOfNeighbourHood,numberOfNeighbours);
                
                //reduce the scope of the neighbourhood
                if(sizeOfNeighbourHood > 5){
                    float newSize = sizeOfNeighbourHood / 1.1f;
                    sizeOfNeighbourHood = (int) newSize;
                }
               
                numberOfScreenShots++;
                
                
                System.out.println("temp " + temperature );
                
                //reduce the temperature 
                temperature*=coolingRate;
                
                //if the system cools down before the 1000 screenshots, restart the system
                if (temperature < minTemperature) {
                	
                	System.out.println("Temperature " + temperature);
                	System.out.println("Starting again");
                	changeColorAll();
                	temperature = 110.0;
                	
                }
                
                
             
                
              
            }
            
           
            
          
        }
    }
}
