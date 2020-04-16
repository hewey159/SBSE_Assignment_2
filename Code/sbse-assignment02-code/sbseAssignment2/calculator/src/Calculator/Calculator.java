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

        //try to read the image
        try{
            powerConsumption = getPowerConsumptionOfImage(imageLocation);
        } 
        catch(IOException ie) {
            ie.printStackTrace();
        }

        //print out the total power consuption
        System.out.println("Toatal Power Used: " + powerConsumption);
    }

    //calculates the total power usage of one pixel
    public static float calculateChargeConsumptionPerPixel(int red, int green, int blue){
        float powerUseage = 1;
        
        
        //calculate power usage of single pixel


        return  powerUseage;
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
        return  totalPowerConsumption;
    }
    
}