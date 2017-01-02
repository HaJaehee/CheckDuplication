package logic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

import configuration.Configuration;

public class CheckDuplication {
	
	final File m_folder;
	private ArrayList<String> m_numArr;
	private ArrayList<String> m_fnameArr;
	
	public CheckDuplication () {
		m_folder = new File (System.getProperty("user.dir"));
		m_numArr = new ArrayList<String>();
		m_fnameArr = new ArrayList<String>();
	}
	
	
	
	private ArrayList<String> getDuplNumArray (ArrayList<String> a_numArr) {
		
		if ( Configuration.debugging ) {
			System.out.println("getDuplNumArray");
		}
		
		ArrayList<String> duplNumArr = new ArrayList<String>();
		ArrayList<String> argNumArr = a_numArr;
		ArrayList<String> numArr = new ArrayList<String>();
		if (m_numArr.size() == 0) {
			numArr = getNumArray();
		} else {
			numArr = m_numArr;
		}
		
		for ( int i = 0 ; i < argNumArr.size() ; i++ ) {
			for ( int j = 0 ; j < numArr.size() ; j++ ) {
				
				if ( Configuration.debugging ) {
					System.out.println("check:"+numArr.get(j)+":"+argNumArr.get(i));
				}
				if ( numArr.get(j).equals(argNumArr.get(i)) ) {
					for ( int k = 0 ; k < m_fnameArr.size() ; k++ ) {
						if ( m_fnameArr.get(k).contains(argNumArr.get(i)) ) {
							System.out.println(argNumArr.get(i)+" : "+m_fnameArr.get(k));
							if ( Configuration.debugging ) {
								System.out.println("Add duplNumArr:"+argNumArr.get(i));
							}
						}
						
					}
					duplNumArr.add(argNumArr.get(i));
					numArr.remove(j--);
					break;
				}
			}
		}

		return duplNumArr;
	}
	
	private ArrayList<String> getNumArray () {
		ArrayList<String> numArr = new ArrayList<String>();
		getFilesListForFolder(m_folder);
		for ( String fileName : m_fnameArr ) {
			ArrayList<String> tempNumArr = extractNumberFromFile(fileName);
			for ( int i = 0 ; i < tempNumArr.size() ; i++ ) {
				boolean isDupl = false;
				for ( int j = 0 ; j < numArr.size() ; j++ ) {
					if ( numArr.get(j).equals(tempNumArr.get(i)) ) {
						isDupl = true;
						break;
					}
				}
				if ( !isDupl && tempNumArr.get(i).length() >= 3 && !tempNumArr.get(i).equals("1080") ) {
					numArr.add(tempNumArr.get(i));
				}
			}
		}
		m_numArr = numArr;
		return numArr;
	}
	
	private void getFilesListForFolder (final File folder) {
		if ( folder.exists() ) {
			for (final File fileEntry : folder.listFiles()) {
				if (fileEntry.isDirectory()) {
					getFilesListForFolder(fileEntry);
				} else {
					if ( !fileEntry.getName().contains(".dat") ) {
						m_fnameArr.add(fileEntry.getName());
					}
				}
			}
		}
	}
	
	private ArrayList<String> extractNumberFromFile (String fileName) {
		ArrayList<String> list = new ArrayList<String>();
		String builder = "";
		for ( int i = 0 ; i < fileName.length() ; i++ ) {
			if (Character.isDigit(fileName.charAt(i))) {
				builder += fileName.charAt(i);
			} else {
				if (!builder.equals("")) {
					list.add(builder);
					builder = "";
				}
			}
		}
		if (!builder.equals("")) {
			list.add(builder);
		}
		return list;
	}
	
	public void saveNumArray (String a_fileNameOfNumArr) {
		ArrayList<String> numArr = new ArrayList<String>();
		if (m_numArr.size() == 0) {
			numArr = getNumArray();
		} else {
			numArr = m_numArr;
		}
		
		String fileNameOfNumArr = System.getProperty("user.dir")+"/"+a_fileNameOfNumArr;
		File file = new File(fileNameOfNumArr);
		if ( file.exists() ) {
			file.delete();
		}
		
		String contents = "";
		for ( int i = 0 ; i < numArr.size() ; i++) {
			contents += numArr.get(i) + "\n";
		}
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			bw.write(contents);
			bw.flush();
			bw.close();
			
		} catch (IOException e){
			e.printStackTrace();
		} 
	}
	
	public void checkDuplNumArray (String a_fileNameOfNumArr) {
		ArrayList<String> duplNumArr = getDuplNumArray(loadNumArray(a_fileNameOfNumArr));
		
		String fileNameOfNumArr = System.getProperty("user.dir")+"/dupl_num_arr.txt";
		File file = new File(fileNameOfNumArr);
		if ( file.exists() ) {
			file.delete();
		}
		
		String contents = "";
		for ( int i = 0 ; i < duplNumArr.size() ; i++) {
			contents += duplNumArr.get(i) + "\n";
		}
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			bw.write(contents);
			bw.flush();
			bw.close();
		} catch (IOException e){
			e.printStackTrace();
		} 
	}
	
	private ArrayList<String> loadNumArray (String a_fileNameOfNumArr) {
		
		if ( Configuration.debugging ) {
			System.out.println("loadNumArray");
		}
		
		String fileNameOfNumArr = a_fileNameOfNumArr;
		File file = new File(fileNameOfNumArr);
		if ( file.exists() ) {
			ArrayList<String> numArr = new ArrayList<String>();
	
			try {
				BufferedReader br = new BufferedReader(new FileReader(file));
				String line = "";
				while ( (line = br.readLine()) != null ) {
					numArr.add(line);
					if ( Configuration.debugging ) {
						System.out.println(line);
					}
				}
				br.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return numArr;
		} else {
			return new ArrayList<String>();
		}
	}
}
