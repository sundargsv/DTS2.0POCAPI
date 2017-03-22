package com.dts.DTSAPI;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class CommonFunctions {

	static Properties prop = new Properties();
	
	public Properties loadProperties() {

		  String propFileName = "ldap1.properties";

		  InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
		  try {
		   prop.load(inputStream);
		  } catch (IOException e) {
		   e.printStackTrace();
		   System.out.println("Property file '" + propFileName + "' not found in the classpath");
		  }
		  return prop;
		 }

}
