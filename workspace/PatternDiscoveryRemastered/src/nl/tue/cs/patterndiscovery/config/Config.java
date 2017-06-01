package nl.tue.cs.patterndiscovery.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import nl.tue.cs.patterndiscovery.filemanager.LinesGetter;

public class Config {
	
	//Root folder in which all data sets are found, for songs.
	protected static File dataSetsRoot;
	
	//Root folder in which all pattern discovery is found and stored.
	protected static File discoveryRoot;
	
	//List of valid file extensions
	protected static String[] validExtensions = new String[]{"", ".txt"};

	private static final String dataSetsRootTag = "Data Sets Root";
	private static final String discoveryRootTag = "Pattern Discovery Root";
	private static final String configFileName = "config.txt";
	
	protected static boolean initComplete = false;
	
	public static boolean isInitialized(){
		return initComplete;
	}
	
	public static File getDataSetsRoot(){
		return dataSetsRoot;
	}
	
	public static File getDiscoveryRoot(){
		return discoveryRoot;
	}

	public static void initConfig(){
		File con = new File(configFileName);
		if(con.exists()){
			try {
				readConfig(con);
				initComplete = true;
			} catch (IOException e) {
				new ConfigSetter();
				e.printStackTrace();
			}
		} else{
			new ConfigSetter();
		}
	}

	public static void readConfig(File f) throws IOException{
		String[] lines = LinesGetter.getLinesFromFile(f);
		clearFields();
		for(int i = 0; i < lines.length; i++){
			if(lines[i].matches(dataSetsRootTag + "=(.)+")) {
				dataSetsRoot = new File(lines[i].substring(dataSetsRootTag.length() + 1));
			}
			if(lines[i].matches(discoveryRootTag + "=(.)+")) {
				discoveryRoot = new File(lines[i].substring(discoveryRootTag.length() + 1));
			}
		}
		
		if(!fieldsFilled()){
			throw new IOException("Config file did not fill all fields, please fill the file correctly.");
		}
	}
	
	private static void clearFields(){
		dataSetsRoot = null;
		discoveryRoot = null;
	}
	
	public static boolean fieldsFilled(){
		return dataSetsRoot != null && discoveryRoot != null;
	}
	
	public static void saveConfig(){
		saveConfig(new File(configFileName), dataSetsRoot.getAbsolutePath(), discoveryRoot.getAbsolutePath());
	}
	
	public static void saveConfig(File con, String dsr, String dr){
		if(!con.exists()){
			try {
				con.createNewFile();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		PrintWriter writer;
		try {
			writer = new PrintWriter(con.getAbsolutePath(), "UTF-8");
			String newline = System.getProperty("line.separator");
			writer.write(dataSetsRootTag + "=" + dsr + newline + discoveryRootTag + "=" + dr);
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static boolean isValidExtension(String ext){
		for(int i = 0; i < validExtensions.length; i++){
			if(validExtensions[i].equals(ext)) return true;
		}
		return false;
	}
}
