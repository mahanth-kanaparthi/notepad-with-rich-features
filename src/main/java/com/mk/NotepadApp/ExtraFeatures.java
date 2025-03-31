package com.mk.NotepadApp;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;

public class ExtraFeatures {
	MainWindow gui;
	
	public ExtraFeatures(MainWindow gui) {
		this.gui = gui;
	
	}

	public void exportToPDF(JTextArea textArea) {
	    JFileChooser fileChooser = new JFileChooser();
	    fileChooser.setDialogTitle("Save as PDF");
	    fileChooser.setFileFilter(new FileNameExtensionFilter("PDF Files", "pdf"));

	    int userSelection = fileChooser.showSaveDialog(gui.getMainFrameInstance());
	    if (userSelection == JFileChooser.APPROVE_OPTION) {
	        String filePath = fileChooser.getSelectedFile().getAbsolutePath();
	        if (!filePath.endsWith(".pdf")) {
	            filePath += ".pdf"; // Ensure the file has a .pdf extension
	        }

	        // Create PDF
	        try {
	            Document document = new Document();
	            PdfWriter.getInstance(document, new FileOutputStream(filePath));
	            document.open();

	            // Get the font style from the JTextArea
	            java.awt.Font textAreaFont = textArea.getFont();
	            String fontFamily = textAreaFont.getFamily();
	            int fontSize = textAreaFont.getSize();
	            int fontStyle = textAreaFont.getStyle();

	            // Map Java AWT font style to iText font style
	            int iTextFontStyle = Font.NORMAL;
	            if ((fontStyle & java.awt.Font.BOLD) != 0) {
	                iTextFontStyle |= Font.BOLD;
	            }
	            if ((fontStyle & java.awt.Font.ITALIC) != 0) {
	                iTextFontStyle |= Font.ITALIC;
	            }

	            // Map unsupported font families to a default font (e.g., HELVETICA)
	            Font.FontFamily pdfFontFamily;
	            try {
	                pdfFontFamily = Font.FontFamily.valueOf(fontFamily.toUpperCase());
	            } catch (IllegalArgumentException e) {
	                // If the font family is not supported, use HELVETICA as a fallback
	                pdfFontFamily = Font.FontFamily.HELVETICA;
	            }

	            // Create iText font
	            Font pdfFont = new Font(pdfFontFamily, fontSize, iTextFontStyle);

	            // Add text from JTextArea to the PDF with the same font style
	            String text = textArea.getText();
	            Paragraph paragraph = new Paragraph(text, pdfFont);
	            document.add(paragraph);

	            document.close();
	            JOptionPane.showMessageDialog(gui.getMainFrameInstance(), "PDF exported successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
	        } catch (DocumentException | IOException e) {
	            JOptionPane.showMessageDialog(gui.getMainFrameInstance(), "Error exporting PDF: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	            e.printStackTrace();
	        }
	    }
	}
	
	public void showAboutDialog() {
        JDialog aboutDialog = new JDialog(gui.getMainFrameInstance(), "About Notepad", true); // Modal dialog
        aboutDialog.setSize(500, 450);
        aboutDialog.setLocationRelativeTo(gui.getMainFrameInstance()); // Center relative to the main frame
        aboutDialog.setResizable(false); // Match image: not resizable

        // Use a layered pane to handle the image and text layout
        JLayeredPane layeredPane = new JLayeredPane();
        aboutDialog.add(layeredPane);


        // 1. Notepad Logo (using JLabel with ImageIcon)
        ImageIcon icon = new ImageIcon(getClass().getResource("notepad_logo.png")); // Replace with your image path
        JLabel imageLabel = new JLabel(icon);
        imageLabel.setBounds(20, 20,56,56); // Adjust position
        layeredPane.add(imageLabel, JLayeredPane.DEFAULT_LAYER);


        // 2. Text Content (using JTextArea for multi-line, non-editable text)
        JTextArea aboutText = new JTextArea(
                "Notepad\n\n" +
                "This Notepad is Developed by Mahanth\n\n"+
                		"Features : \n\n● Simple Notepad\n● Themes\n● Voice Typing"+
                "\n● Export as PDF\n● Export as Markdown"
        );
        

        aboutText.setEditable(false); // Prevent user editing
        aboutText.setBackground(aboutDialog.getBackground()); // Match dialog background
        aboutText.setBounds(100, 20, 350, 250);// Adjust position and size
        aboutText.setFont(new java.awt.Font("Arial",0,18));
        layeredPane.add(aboutText, JLayeredPane.DEFAULT_LAYER);

        // 3. email
        JLabel emailLabel = new JLabel("Contact Dev");
        emailLabel.setFont(new java.awt.Font("Arial",2,15));
        emailLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        
        emailLabel.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		try {
        			Desktop.getDesktop().mail(new URI("mailto:mahanth2817@gmail.com"));
        		}catch(Exception exception) {
        			exception.printStackTrace();
        		}
        	}
        });
        
        emailLabel.setBounds(100,310,100,20);
        layeredPane.add(emailLabel, JLayeredPane.DEFAULT_LAYER);

        // 4. OK Button
        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                aboutDialog.dispose(); // Close the dialog
            }
        });

        okButton.setBounds(290, 330, 80, 25); // Adjust position
        layeredPane.add(okButton, JLayeredPane.DEFAULT_LAYER);


        // Set the preferred size of the layered pane to encompass all components
        layeredPane.setPreferredSize(aboutDialog.getSize());

        aboutDialog.setVisible(true);
    }
}
