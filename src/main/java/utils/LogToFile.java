/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.io.IOException;

public class LogToFile {
     
    public static void main(String[] args) throws IOException {
       

        
        LogToFile ltf= new LogToFile();
        ltf.logAdmin("The method", "Bob1212");
    }
    
    public void logAdmin(String method,String name) throws IOException{
         Logger logger = Logger.getLogger(LogToFile.class.getName());
        FileHandler fileHandler = new FileHandler("login.log", true);        
        logger.addHandler(fileHandler);
            
            logger.log(Level.WARNING, "Method and result: "+ method +" Username used: "+ name);
        
    }
}
