package com.mk.NotepadApp;

import java.awt.Color;

public class ViewMenuFunctions {
	MainWindow gui;
	String currentTheme;

	public ViewMenuFunctions(MainWindow gui) {
		this.gui = gui;
	}

	public void changeTheme(String theme) {

		switch(theme) {
		case "light" : 
			if(!gui.enabledTheme.equals(theme) || gui.enabledTheme.equals(theme)) {
				lightTheme();
				gui.enabledTheme = theme;
				gui.textArea.setCaretColor(Color.black);
				gui.settings.saveCurrentTheme(theme);
				//				System.out.println("dark"+gui.isDarkModeON); =>for debugging purpose
			}
			break;
		case "dark"  :
			if(!gui.enabledTheme.equals(theme) || gui.enabledTheme.equals(theme)) {
				darkTheme();
				gui.enabledTheme = theme;
				gui.textArea.setCaretColor(Color.WHITE);
				gui.settings.saveCurrentTheme(theme);
				//				System.out.println("dark"+gui.isDarkModeON); =>for debugging purpose		
			}
			break;
			
		case "monokai" :
			if(!gui.enabledTheme.equals(theme) || gui.enabledTheme.equals(theme)) {
				monokaiTheme();
				gui.enabledTheme = theme;
				gui.textArea.setCaretColor(Color.white);
				gui.settings.saveCurrentTheme(theme);
			}
			break;
		case "obsidian" :
			if(!gui.enabledTheme.equals(theme) || gui.enabledTheme.equals(theme)) {
				obsidianTheme();
				gui.enabledTheme = theme;
				gui.textArea.setCaretColor(Color.white);
				gui.settings.saveCurrentTheme(theme);
			}
			break;
		case "solarizedLight" : 
			if(!gui.enabledTheme.equals(theme) || gui.enabledTheme.equals(theme)) {
				solarizedLightTheme();
				gui.enabledTheme = theme;
				gui.textArea.setCaretColor(Color.black);
				gui.settings.saveCurrentTheme(theme);
			}
			break;
		case "solarizedDark" : 
			if(!gui.enabledTheme.equals(theme) || gui.enabledTheme.equals(theme)) {
				solarizedDarkTheme();
				gui.enabledTheme = theme;
				gui.textArea.setCaretColor(Color.white);
				gui.settings.saveCurrentTheme(theme);
			}
			break;
		default :
			lightTheme();
			gui.enabledTheme = theme;
			gui.textArea.setCaretColor(Color.black);
			gui.settings.saveCurrentTheme(theme);
			
		}
	}
	private void lightTheme() {
		gui.mainFrame.getContentPane().setBackground(Color.WHITE);
		gui.textArea.setBackground(Color.WHITE);
		gui.textArea.setForeground(Color.BLACK);
	}
	private void darkTheme() {
		gui.mainFrame.getContentPane().setBackground(new Color(20,20,20));
		gui.textArea.setBackground(new Color(24,24,24));
		gui.textArea.setForeground(Color.WHITE);
	}
	private void monokaiTheme() {
		gui.mainFrame.getContentPane().setBackground(new Color(20,20,20));
		gui.textArea.setBackground(new Color(46,46,46));
		gui.textArea.setForeground(Color.WHITE);
	}
	private void obsidianTheme() {
		gui.mainFrame.getContentPane().setBackground(new Color(20,20,20));
		gui.textArea.setBackground(new Color(15,15,15));
		gui.textArea.setForeground(Color.WHITE);
	}
	private void solarizedLightTheme() {
		gui.mainFrame.getContentPane().setBackground(new Color(20,20,20));
		gui.textArea.setBackground(new Color(253,246,227));
		gui.textArea.setForeground(Color.BLACK);
	}
	private void solarizedDarkTheme() {
		gui.mainFrame.getContentPane().setBackground(new Color(20,20,20));
		gui.textArea.setBackground(new Color(0,43,54));
		gui.textArea.setForeground(Color.WHITE);
	}
	
	
}
