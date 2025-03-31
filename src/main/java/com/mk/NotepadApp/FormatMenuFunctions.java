package com.mk.NotepadApp;

public class FormatMenuFunctions {
	MainWindow gui;

	public FormatMenuFunctions(MainWindow gui) {
		this.gui = gui;
	}
	public void wordWrap() {

		if (!gui.isWordWrapON) {
			gui.isWordWrapON = true;
			gui.textArea.setLineWrap(true);
			gui.textArea.setWrapStyleWord(true);
			gui.wordWrapItem.setText("Word Wrap \u2714");
			gui.settings.saveWrapOption(true);
			//			System.out.println(" word" + gui.isWordWrapON); =>for debugging purpose
		}else {
			gui.isWordWrapON = false;
			gui.textArea.setLineWrap(false);
			gui.textArea.setWrapStyleWord(false);
			gui.wordWrapItem.setText("Word Wrap \u274c");
			gui.settings.saveWrapOption(false);
			//			System.out.println(" word"+gui.isWordWrapON); =>for debugging purpose
		}

	}
	public void fontStyle() {

		new Thread(() -> {
			
			new FontChooser(gui);
			
		}).start();

	}
}
