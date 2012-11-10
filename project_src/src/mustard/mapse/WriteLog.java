package com.mustard.mapse;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class WriteLog {
	File logFile = new File("sdcard/mapse/log.file");
	
	public WriteLog () {
	       if (!logFile.exists()) {
	          try {
	             logFile.createNewFile();
	          } catch (IOException e) {
	             e.printStackTrace();
	          }//end try/catch
	       }//end if
	}//end WriteLog constructor
	
	
	
    /***********************************************
     * Following method is for writing and appending to a log file
     */
    public void appendLog(String text) {
       try {
          //BufferedWriter for performance, true to set append to file flag
          BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true)); 
          buf.append(text);
          buf.newLine();
          buf.flush();
          buf.close();
       } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
       }//end try/catch
    }//end appendLog
}//end WriteLog
