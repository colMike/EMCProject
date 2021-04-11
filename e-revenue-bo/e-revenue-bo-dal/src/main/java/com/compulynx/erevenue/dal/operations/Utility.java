package com.compulynx.erevenue.dal.operations;

import java.io.FileInputStream;
import java.util.Properties;

import com.compulynx.erevenue.models.CompasProperties;

/**
 * The Utility calss
 * @author Anita
 * @date May 11,2016
 *
 */
public class Utility {
	private static final String[] lowNames = { "zero", "one", "two", "three",
			"four", "five", "six", "seven", "eight", "nine", "ten", "eleven",
			"twelve", "thirteen", "fourteen", "fifteen", "sixteen",
			"seventeen", "eighteen", "nineteen" };

	private static final String[] tensNames = { "twenty", "thirty", "forty",
			"fifty", "sixty", "seventy", "eighty", "ninety" };

	private static final String[] bigNames = { "thousand", "million", "billion" };
	
	/**
	 * converts the amount to words
	 * function is used in permit report to show amount in words
	 * @param n- amount
	 * @return
	 */
	public static String convertNumberToWords(int n) {
		if (n < 0) {
			return "minus " + convertNumberToWords(-n);
		}
		if (n <= 999) {
			return convert999(n);
		}
		String s = null;
		int t = 0;
		while (n > 0) {
			if (n % 1000 != 0) {
				String s2 = convert999(n % 1000);
				if (t > 0) {
					s2 = s2 + " " + bigNames[t - 1];
				}
				if (s == null) {
					s = s2;
				} else {
					s = s2 + "  " + s;
				}
			}
			n /= 1000;
			t++;
		}
		return s;
	}

	// Range 0 to 999.
	private static String convert999(int n) {
		String s1 = lowNames[n / 100] + " hundred";
		String s2 = convert99(n % 100);
		if (n <= 99) {
			return s2;
		} else if (n % 100 == 0) {
			return s1;
		} else {
			return s1 + " " + s2;
		}
	}

	// Range 0 to 99.
	private static String convert99(int n) {
		if (n < 20) {
			return lowNames[n];
		}
		String s = tensNames[n / 10 - 2];
		if (n % 10 == 0) {
			return s;
		}
		return s + "-" + lowNames[n % 10];
	}

	/**
	 * Calculates the compound interest rate per month 
	 * @param p- initial value of amount
	 * @param r - compound rate 
	 * @param n- no of months
	 */
	public static double calculate(double p, double r, int n) {

		double amount = p * Math.pow(1 + (r), n);

		double interest = amount - p;

		System.out.println("Compond Interest is " + interest);

		return interest;

	}
	
	 public CompasProperties getCompasProperties(String param) {
	        String path = System.getProperty("catalina.base") +"/conf/application.properties";
	        Properties properties = new Properties();
	        try{
	            FileInputStream file;
	            //load the file handle for main.properties
	            file = new FileInputStream(path.trim());
	            //load all the properties from this file
	            properties.load(file);
	            //we have loaded the properties, so close the file handle
	            file.close();
	            return new CompasProperties(properties.getProperty(param).trim());
	        } catch (Exception ex){
	            ex.printStackTrace();
	        }
	        return null;
	    }
}
