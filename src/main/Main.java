package main;

import logic.CheckDuplication;

public class Main {
	
	public static void main(String[] args) {
		boolean error = false;
		if (args.length == 2 && args[0].startsWith("--")) {
			String operation = args[0];
			String fileName = args[1];
			if ( operation.equals("--save") ) {
				CheckDuplication cd = new CheckDuplication();
				cd.saveNumArray(fileName);
			} else if ( operation.equals("--check-dupl") ) {
				CheckDuplication cd = new CheckDuplication();
				cd.checkDuplNumArray(fileName);
			} else {
				error = true;
			}
		} else {
			error = true;
		}
		
		if ( error ) {
			System.out.println("Usage: \n" +
					"If you want to extract numbers from names of all files in folders recursively and save it at \"filename\", use this\n" +
					"CheckDuplication.jar --save \"filename\"\n" +
					"If you want to check duplicated numbers between numbers from the file \"filename\" and numbers from names of all files in folders recursively, use this\n" +
					"CheckDuplication.jar --check-dupl \"filename\"\n");
		}
	}
}
