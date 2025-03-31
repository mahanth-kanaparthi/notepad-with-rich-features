package com.mk.NotepadApp;

import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;



public class SaveChanges {

	MainWindow gui;
	File file;
	FileReader reader;
	FileWriter writer;
	
	public SaveChanges(MainWindow gui) {
		this.gui = gui;
		this.file = new File("res/settings.ser");
		srcFile();
		
	
		@SuppressWarnings("unused")
		SettingValues s;
		try {
			s = readFromFile();
			//System.out.println("from constructor : " + s);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	private boolean writeToFile(SettingValues settings) {
		try(ObjectOutputStream out = new ObjectOutputStream(
				new FileOutputStream("res/settings.ser"))){
			out.writeObject(settings);
			return true;
		}catch(IOException e) {System.err.println("Write Error => "+e);}
		return false;
	}
	private SettingValues readFromFile() throws ClassNotFoundException {
		SettingValues settings;
		try(ObjectInputStream in = new ObjectInputStream(
				new FileInputStream("res/settings.ser"))){
			settings = (SettingValues) in.readObject();
			//System.out.println(settings);
			return settings;
		}catch(IOException e) {System.err.println("Read Error => "+e);
		}finally {
			settings = new SettingValues("Consolas",0,18,"light",false,false);
		}
		return settings;
	}
	@SuppressWarnings("unused")
	public void srcFile() {
		if(!file.exists()) {
			try {
				file.createNewFile();
				SettingValues settings = new SettingValues(
								"Consolas",0,18,"light",false,false
								);
				//System.out.println("write op : " + writeToFile(settings));
			} catch (IOException e) {
				System.err.println("File Not Exists => "+e);
			}
		}
	}
	
	public void saveFontSettings(String fontName,int fontStyle,int size) {
		
		SettingValues settings;
		try {
			settings = readFromFile();
			if (settings != null) {
				settings.setFontName(fontName);
				settings.setFontStyle(fontStyle);
				settings.setSize(size);
				writeToFile(settings);
			}else {
				//do nothing
				System.err.println("Error readingFromFile: settings not found");// =>for debugging purpose
			}
		} catch (ClassNotFoundException e) {
			System.err.println("save font op => "+e);
		}
	}
	public void readSettings() throws ClassNotFoundException {

		if(!file.equals(null)) {
			SettingValues settings = readFromFile();
			if (settings != null) {
				gui.fontName = settings.getFontName();
				gui.fontStyle = settings.getFontStyle();
				gui.size = settings.getSize();
				gui.enabledTheme = settings.getCurrentTheme();
				gui.isWordWrapON = settings.isWordWrapON();
				gui.isStatusBarON = settings.isStatusBarON();
			}
		}else {
			
			//Default Font
			gui.textArea.setFont(new Font("Consolas",Font.PLAIN,16));
			//dark mode 
			gui.enabledTheme = "light";
			gui.isWordWrapON = false;
		}
	}
	public void saveCurrentTheme(String theme) {
		
		SettingValues settings;
		try {
			settings = readFromFile();
			if (settings != null) {
				settings.setCurrentTheme(theme);
				writeToFile(settings);
			}else {
				//do nothing
				System.err.println("Error readingFromFile: settings not found");// =>for debugging purpose
			}
		} catch (ClassNotFoundException e) {
			System.err.println("save darkmode op => "+e);
		}
	}
	public void saveWrapOption(boolean flag) {
	
		SettingValues settings;
		try {
			settings = readFromFile();
			if (settings != null) {
				settings.setWordWrapON(flag);
				writeToFile(settings);
			}else {
				//do nothing
				System.err.println("Error readingFromFile: settings not found");// =>for debugging purpose
			}
		} catch (ClassNotFoundException e) {
			System.err.println("save wordwrap op => "+e);
		}
	}
	public void saveStatusBarOption(boolean flag) {
		SettingValues settings;
		try {
			settings = readFromFile();
			if(settings != null) {
				settings.setStatusBarON(flag);
				writeToFile(settings);
			}else {
				// do nothing
				System.err.println("Error readingFromFile: settings not found");
			}
		}catch(ClassNotFoundException e) {
			System.err.println("save statusbar op => "+e);
		}
	}
	
	
}

class SettingValues implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String fontName, currentTheme = "light";
	private int fontStyle;
	private int size;
	private boolean isWordWrapON, isStatusBarON;
	
	public String getFontName() {
		return fontName;
	}
	public void setFontName(String fontName) {
		this.fontName = fontName;
	}
	public String getCurrentTheme() {
		return currentTheme;
	}
	public void setCurrentTheme(String currentTheme) {
		this.currentTheme = currentTheme;
	}
	public int getFontStyle() {
		return fontStyle;
	}
	public void setFontStyle(int fontStyle) {
		this.fontStyle = fontStyle;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public boolean isWordWrapON() {
		return isWordWrapON;
	}
	public void setWordWrapON(boolean isWordWrapON) {
		this.isWordWrapON = isWordWrapON;
	}
	public boolean isStatusBarON() {
		return isStatusBarON;
	}
	public void setStatusBarON(boolean isStatusBarON) {
		this.isStatusBarON = isStatusBarON;
	}
	
	SettingValues(){}
	SettingValues(String fontName,int fontStyle, int size, String currentTheme,
			boolean isWordWrapON,boolean isStatusBarON){
		this.fontName = fontName;
		this.fontStyle = fontStyle;
		this.size = size;
		this.currentTheme = currentTheme;
		this.isWordWrapON = isWordWrapON;
		this.isStatusBarON = isStatusBarON;
	}
	@Override
	public String toString() {
		return "Settings { fontName: " + fontName + ", fontStyle: " + fontStyle + ", size: " + size
				+ ", currentTheme: " + currentTheme + ", isWordWrapON: " +
				isWordWrapON + ", isStatusBarON: "+ isStatusBarON+" }";
	}
	
}
