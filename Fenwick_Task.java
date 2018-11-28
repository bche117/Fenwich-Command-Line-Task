/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fenwick_task;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author baihanchen
 */
public class Fenwick_Task {
    
    private static final String FIRST_COMMAND = "Stats.exe"; 
    private static final String HELP_COMMAND = "help";
    private static final String RECORD_COMMAND = "record";
    private static final String SUMMARY_COMMAND = "summary";
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        startService();
    }
    
    public static void startService() {
        while (true) { //ask to input a command line repeatly
            
            System.out.print("Please enter your command:\n");
            System.out.print(">");
         
            Scanner reader = new Scanner(System.in);
            String inputCommand = reader.nextLine(); //read the command line
            
            getFunction(inputCommand); // help. record and summary actions
        }
    
    }

    private static void getFunction(String inputCommand) {
         if (inputCommand != null) {
             String[] commands = inputCommand.split(" ");
             if (commands[0].equals(FIRST_COMMAND) && commands.length >= 2){ //validate the first element(Stats.exe)
                 if (commands.length == 2 && commands[1].equals(HELP_COMMAND)) { // if it is the help command line
                     getHelpMsg();
                 }else if (commands[1].equals(RECORD_COMMAND)&& commands.length > 3){ // if it is the record command line
                     getRecordFunc(commands);
                 }else if (commands[1].equals(SUMMARY_COMMAND)&& commands.length == 3){ // if it is the summary command line
                     getSummaryFunc(commands);
                 }else{
                     getHelpMsg(); //error handling
                 }
             }else {
                 getHelpMsg(); //error handling
             }
         
         }else {
         //TODO
         }
    }
    // function for the help action
    private static void getHelpMsg() {
         System.out.printf("Command 1: %s                             --- %s", "Stats.exe help", "Display this usage guide.\n");
         System.out.printf("Command 2: %s                 --- %s", "Stats.exe summary filepath", "Summarise the data.\n");
         System.out.printf("Command 3: %s  --- %s", "Stats.exe record filepath [value1 value2]", "Save one or more values into a certain file.\n");
    }

    
    public static boolean isNumeric(String str) {  
        try {  
            double d = Double.parseDouble(str);  
        } catch(NumberFormatException nfe)  
        {  
        return false;  
        }  
        return true;  
    }
    
    // function for writing records into the file
    private static void getRecordFunc(String[] commands) {
        Boolean b = true;
        String fileName = commands[2];
        String[] variables = Arrays.copyOfRange(commands, 3, commands.length);
        for(int i = 0; i< variables.length; i++){
            if(!isNumeric(variables[i])){
                b = false; 
                System.out.printf("Invalid input number!\n");
                getHelpMsg(); //error handling
            }
        }
        if (fileName.contains(".txt") && b==true) { //validate the file name
            if(isExistingFile(fileName)){ //check if the file is exist
                writeIntoFile(fileName, variables);
            }else {
                if (createFile(fileName)) //create a new file
                    writeIntoFile(fileName, variables); //write data into the file
                       
                  }
            }
        } 
    // function for summarising data
    private static void getSummaryFunc(String[] commands) {
        String fileName = commands[2];
        Double sum = 0.0;
        int count = 0;
        Double avg = 0.0;
        if (fileName.contains(".txt")) {
            if(isExistingFile(fileName)){ //validate the file name
                File file = new File(fileName);
                if (file.length() == 0){ //check whether the file is empty
                    String leftAlignFormat = "| %-15s | %s |%n";
                    System.out.format("+-----------------+------+%n");
                    System.out.format(leftAlignFormat, "# of Entities", "0");
                    System.out.format(leftAlignFormat, "Min. value", "Null");
                    System.out.format(leftAlignFormat, "Max. value", "Null");
                    System.out.format(leftAlignFormat, "Avg. value", "Null");
                    System.out.format("+-----------------+------+%n"); // output a table when the file is empty
                }else{
                    ArrayList<Double> list = new ArrayList<Double>();
                    list = scanFile(fileName); //read the data from the file into an ArrayList
                    Double min = list.get(0);
                    Double max = list.get(0); //initiate min and max value

                    for (int i = 0; i < list.size(); i++) { // iterate through the Arraylist
                        sum += list.get(i); //calcuate the sum
                        count++; //count the numbers in the file
                        if (list.get(i) < min){
                            min = list.get(i);
                        }
                        if (list.get(i) > max){
                            max = list.get(i); //get the min and max value
                        }
                    }
                    avg = sum/count;
                    String leftAlignFormat1 = "| %-15s | %-4d |%n"; //format for the count row
                    String leftAlignFormat2 = "| %-15s | %-4f |%n"; //format for the max, min and avg row

                    System.out.format("+-----------------+------+%n");
                    System.out.format(leftAlignFormat1, "# of Entities", count);
                    System.out.format(leftAlignFormat2, "Min. value", min);
                    System.out.format(leftAlignFormat2, "Max. value", max);
                    System.out.format(leftAlignFormat2, "Avg. value", avg);
                    System.out.format("+-----------------+------+%n"); // output a table when the file has data
                }
            }
        }else{
            getHelpMsg(); //error handling
        }
    }

    //check if the file has existed
    private static boolean isExistingFile(String fileName) {
        File file = new File(fileName);
        return file.exists();
    }
    
    //create a new file
    private static boolean createFile(String fileName) {
        File newFile = new File(fileName);
        try {
            newFile.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(Fenwick_Task.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    //write data into file
    private static void writeIntoFile(String fileName, String[] variables) {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(fileName, true);
            for (int i = 0; i < variables.length; i++) {
                fileWriter.append(variables[i] + ",");
            }
        } catch (IOException ex) {
            Logger.getLogger(Fenwick_Task.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (fileWriter != null){
                try {
                    fileWriter.close();
                } catch (IOException ex) {
                    Logger.getLogger(Fenwick_Task.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        }
    }

    //get the data from the file into ArrayList
    private static ArrayList<Double> scanFile(String fileName) {
        // reading the file
        BufferedReader br;
        ArrayList<Double>list = new ArrayList<Double>();

        try {
            br = new BufferedReader(new FileReader(new File(fileName)));
            String line;
            while((line = br.readLine())!= null){
                // split the line you read by "," and save into an array     
                String [] r = line.split(",");
                // iterate through these values
                for(int i = 0; i < r.length; i++){
                    Double val = Double.parseDouble(r[i]);
                    list.add(val); // add the double type value into ArrayList
                }
            }br.close();
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Fenwick_Task.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Fenwick_Task.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        return list;
    }

 
    
}
