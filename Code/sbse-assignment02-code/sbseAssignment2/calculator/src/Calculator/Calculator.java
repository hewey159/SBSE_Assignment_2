/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Calculator;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import java.awt.Color;

public class Calculator {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        //might need to be change for each user
        String imageLocation = System.getProperty("user.dir") + "\\sbse-assignment02-code\\sbseAssignment2\\calculator\\src\\Calculator\\TestImages\\TestImage1.png";
        
        //inital power consumtion, -1 is error
        float powerConsumption = -1;
        int totalPixels = -1;
        //try to read the image
        try{
            powerConsumption = getPowerConsumptionOfImage(imageLocation);
            totalPixels = getNumberOfPixels(imageLocation);
        } 
        catch(IOException ie) {
            ie.printStackTrace();
        }

        //print out the total power consuption
        System.out.println("Toatal Power Used: " + powerConsumption + " mA");
        System.out.println("Total Pixels in the Image: " + totalPixels);
    }

    //calculates the total power usage of one pixel
    public static double calculateChargeConsumptionPerPixel(int red, int green, int blue){
        //calculated using the Nexus 6 excel sheets
        double redPowerUsage = 3.255e-5;
        double greenPowerUsage = 3.797e-5;
        double bluePowerUsage = 6.51e-5;

        //calculates the percentage of brightness out of 255 (255 is the max value)
        float redPercentageValue = (float) red / 255;
        float greenPercentageValue = (float) green / 255;
        float bluePercentageValue = (float) blue / 255;
        
        //total usage for each pixel
        double totalRedPowerUsage = redPowerUsage * redPercentageValue;
        double totalGreenPowerUsage = greenPowerUsage * greenPercentageValue;
        double totalBluePowerUsage = bluePowerUsage * bluePercentageValue;
        
        //total usage for all red, green, blue pixels
        double totalPowerUsage = totalRedPowerUsage + totalGreenPowerUsage + totalBluePowerUsage;
        
        return totalPowerUsage;
    }

    public static float getPowerConsumptionOfImage(String location) throws IOException {
        File file = new File(location);
        BufferedImage image = ImageIO.read(file);
        float totalPowerConsumption = 0;

        //loop over each pixel
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {

                //converts the pixels into red green and blue
                Color c = new Color(image.getRGB(x, y));
                int red = c.getRed();
                int green = c.getGreen();
                int blue = c.getBlue();

                //adds the calculated power consumtion to the total
                totalPowerConsumption += calculateChargeConsumptionPerPixel(red, green, blue);
            }
        }
        return totalPowerConsumption;
    }

    public static int getNumberOfPixels(String location) throws IOException{
        //imports the image
        File file = new File(location);
        BufferedImage image = ImageIO.read(file);
        
        //total number of pixels
        int totalPixelCount = image.getWidth() * image.getHeight();
        return totalPixelCount;
    }
    
    
    //reutrns the difference between colors where int[] = [red, green, blue]
    public double EuclideanDistanceBetweenTheColours(int[] colour1, int[] colour2){
        
        double red = Math.pow(colour1[0] - colour2[0], 2);
        double green = Math.pow(colour1[1] - colour2[1], 2);
        double blue = Math.pow(colour1[2] - colour2[2], 2);

        double distance = Math.sqrt(red + green + blue);
        
        return distance;
    }

}
