package main.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Console{
	static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
	public static void print(String msg, String type) {
		Date date = new Date();
		switch(type.toUpperCase()) {
			case "CONFIG": 
				System.out.println(sdf.format(date) + "[Config]" + msg);
				break;
			case "GUI": 
				System.out.println(sdf.format(date) + "[Gui]" + msg);
				break;
			case "DB": 
				System.out.println(sdf.format(date) + "[Database]" + msg);
				break;
			case "FILEIO": 
				System.out.println(sdf.format(date) + "[FileI/O]" + msg);
				break;
			case "APP": 
				System.out.println(sdf.format(date) + "[Application]" + msg);
				break;
			case "DEBUG": 
				System.out.println(sdf.format(date) + "[Debug]" + msg);
				break;
			case "EXCEPTION": 
				System.out.println(sdf.format(date) + "[Exception]" + msg);
				break;
			case "ERROR": 
				System.out.println(sdf.format(date) + "[ERROR]" + msg);
				break;
			default:
				System.out.println(sdf.format(date) + "[Default]" + msg);
				break;
		}
	}
}
