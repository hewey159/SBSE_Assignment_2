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

import junit.framework.Test;

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

    private static String TARGET_APP = "calculator.jar";
    //private static final String TARGET_APP = "simpleApp.jar";
    private static final String TARGET_APP_COLOR = "color.csv";
    private static final int TARGET_APP_RUNNINGTIME = 600;
    private static final String JAVA_COMMAND = "java -jar ";
    private static String parentDir = "";
    private static int screenshotsNum = 1000;
    private static int screenshot = 1;
    private static ArrayList<Long> results = new ArrayList<Long>();
    private static String line = "";
    private static long solution = 999999999;
    private static int RS = 1;
    
    private static int temperature = 100; 
    private static int noResult = 0;
    
    private static ArrayList<String> guiComponents = new ArrayList<>();
    private static ArrayList<ArrayList<Integer>> RGB = new ArrayList<>();
    
    private static ArrayList<ArrayList<Integer>> RGBNew = new ArrayList<>();
    private static Random randomInt = new Random();
    
    private static String filename = "";

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
            runApp(TARGET_APP, TARGET_APP_RUNNINGTIME);
            if(RS == 1) {
            	randomSearch(screenshotsNum,i);
            }
        }
        writeResultsToExcel(parentDir.concat("finalResults.csv"),results);

    }

    public static void runApp(String path, int targetAppRunningtime) {
        try {
            //java -jar C:\Users\Mahmoud-Uni\Documents\NetBeansProjects\calculator\dist\calculator.jar

            //path = "\""+path+"\"";
            System.out.println("Target App" + path);

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
            System.out.println("Target App");
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
    	
    	System.out.printf("pixelCount = %d Total Consumtion = %d Number = %d %n", pixelCount, consumption, screenshot);
    	
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
}
