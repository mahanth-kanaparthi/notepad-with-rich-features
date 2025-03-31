package com.mk.NotepadApp;

import java.io.File;
import java.awt.print.PrinterException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFileChooser;


public class FileMenuFunctions {

	MainWindow gui;
	String fileName,fileAddress;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
    String timestamp = sdf.format(new Date());
    
	public FileMenuFunctions(MainWindow gui) {
		this.gui = gui;
	}

	public void newFile() {
		gui.textArea.setText("");
		fileName = "Untitled_"+timestamp; // Default name for a new file
		gui.mainFrame.setTitle("Untitled_"+timestamp);
		fileAddress = null;
		gui.isModified = false;
		gui.updateTitle();
	}

	public void openFile() {
	    JFileChooser fileChooser = new JFileChooser();
	    fileChooser.setDialogTitle("Open File");

	    int userSelection = fileChooser.showOpenDialog(gui.mainFrame);

	    if (userSelection == JFileChooser.APPROVE_OPTION) {
	        File selectedFile = fileChooser.getSelectedFile();
	        fileName = selectedFile.getName();
	        fileAddress = selectedFile.getParent();
	        gui.mainFrame.setTitle(fileName);

	        try (BufferedReader br = new BufferedReader(new FileReader(selectedFile))) {
	            gui.textArea.setText("");

	            String line;
	            while ((line = br.readLine()) != null) {
	                gui.textArea.append(line + "\n");
	            }
	        } catch (IOException e) {
	            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, e);
	        }
	    }
	}

	public void saveFile() {
        if (fileName.equals("Untitled_"+timestamp)) {
            saveAsFile("Save");
        } else {
            try (FileWriter fw = new FileWriter(new File(fileAddress, fileName))) {
                fw.write(gui.textArea.getText());
                gui.isModified = false;
                gui.updateTitle();
            } catch (IOException e) {
                Logger.getLogger(FileMenuFunctions.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }

    public void saveAsFile(String saveType) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(saveType);
        
     // Set the default filename in the file chooser dialog
        if (fileName == null) {
            fileName = "Untitled_"+timestamp;
        }
        fileChooser.setSelectedFile(new File(fileName));
        
        int userSelection = fileChooser.showSaveDialog(gui.mainFrame);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String filePath = fileToSave.getAbsolutePath();

            // Ensure .txt is added if no extension is present
            if (!filePath.contains(".")) {
                fileToSave = new File(filePath + ".txt");
            }
            
            fileName = fileToSave.getName();
            fileAddress = fileToSave.getParent();

            try (FileWriter fw = new FileWriter(fileToSave)) {
                fw.write(gui.textArea.getText());
                gui.isModified = false;
                gui.updateTitle();
            } catch (IOException e) {
                Logger.getLogger(FileMenuFunctions.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }

	public void printFile() {

		try {
			gui.textArea.print();			
		} catch (PrinterException e) {
			Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE,null,e);
			e.printStackTrace();
		}
	}

	public void exitFile() {
		System.exit(0);

	}
}
