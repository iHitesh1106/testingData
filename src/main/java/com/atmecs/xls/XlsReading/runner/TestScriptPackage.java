package com.atmecs.xls.XlsReading.runner;

import java.io.File;
import java.util.regex.Pattern;

public class TestScriptPackage 
{

	  String pack = "";
	    public String getPack() 
	    {
	        return pack;
	    }

	    public void getPackage(File node) 
	    {
	        if (node.isDirectory()) 
	        {
	            String[] subNote = node.list();
	            for (String filename : subNote) 
	            {
	                getPackage(new File(node, filename));
	            }
	        } 
	        else if (node.isFile()) 
	        {
	        	String pattern = Pattern.quote(System.getProperty("file.separator"));
	            pack = pack + "," + node.getParent().split("java"+pattern)[1];
	        }
	    }

	    public void setPack(String pack) 
	    {
	        this.pack = pack;
	    }

}
