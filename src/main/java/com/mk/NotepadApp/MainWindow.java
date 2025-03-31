package com.mk.NotepadApp;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.undo.UndoManager;

public class MainWindow implements ActionListener{

	//Text Area
	JFrame mainFrame;
	JTextArea textArea;
	UndoManager manager;

	JScrollPane scrollPane;
	JLabel statusBar;
	//Top Menu
	JMenuBar menuBar;
	JMenu fileMenu, editMenu, formatMenu, viewMenu, extraMenu;
	boolean isModified = false; // Tracks unsaved changes
	//File
	JMenuItem newItem,openItem,saveItem,saveasItem,printItem,exitItem;
	//Format
	JMenuItem wordWrapItem,fontItem;
	//Edit 
	JMenuItem undo,redo,copy,cut,paste,selectAll;
	//View
	JMenu theme;
	JRadioButtonMenuItem light,dark, monokai, obsidian, solarizedLight, solarizedDark;
	ButtonGroup themeGroup;
	JCheckBoxMenuItem statusBarCheckBox;
	
	//Extra
	JMenuItem voiceTyping, exportToPdf, about;
	
	ImageIcon icon = new ImageIcon(getClass().getResource("notepad.png"));

	FileMenuFunctions file = new FileMenuFunctions(this);
	FormatMenuFunctions format = new FormatMenuFunctions(this);
	EditMenuFunctions edit = new EditMenuFunctions(this);
	ViewMenuFunctions view = new ViewMenuFunctions(this);
	SpeechRecognition speech = new SpeechRecognition(this);
	ExtraFeatures extras = new ExtraFeatures(this);
	// last saved changes 
	SaveChanges settings = new SaveChanges(this);

	// Default settings : Font font = new Font("Leelawadee",Font.PLAIN,16);
	String fontName;
	int fontStyle;
	int size;
	boolean isWordWrapON,isStatusBarON;
	String enabledTheme;
	


	public MainWindow() {
		//isWordWrapOn = false;
		setDefaults();
		//System.err.println("1. "+enabledTheme);
		try {
			settings.readSettings();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		//System.err.println("2. "+enabledTheme);
		
		createMainFrame();
		createTextArea();
		createMenuBar();
		// initial read from serialized file : theme and word wrap
		
		if(isWordWrapON) {
			textArea.setLineWrap(true);
			textArea.setWrapStyleWord(true);
			wordWrapItem.setText("Word Wrap \u2714");
		}
		//System.err.println("3. "+enabledTheme);
		setEnabledTheme(enabledTheme);
		file.newFile();
		mainFrame.setVisible(true);
	}



	private void createMainFrame() {
		mainFrame = new JFrame("Notepad");
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Double width =  screenSize.getWidth() * 0.80;
		Double height = screenSize.getHeight() * 0.80;
		mainFrame.setSize(width.intValue(),height.intValue());
		mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // Prevent immediate exit
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitApplication(); // Call custom exit method
            }
        });
		mainFrame.setIconImage(icon.getImage());
		mainFrame.setLocationRelativeTo(null);
		
		statusBarCheckBox = new JCheckBoxMenuItem("Status Bar");
		statusBarCheckBox.setSelected(true);
		statusBarCheckBox.addActionListener(this);
		
		statusBar = new JLabel("Ln 1 Col 1"); //initial status
		if(statusBarCheckBox.getState())
			mainFrame.add(statusBar, BorderLayout.SOUTH);
	}
	private void createTextArea() {
		textArea = new JTextArea();
		//for changing the line when reached edge of end
		textArea.setFont(new Font(fontName,fontStyle,size));

		scrollPane = new JScrollPane(textArea,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());


		manager = new UndoManager();
		textArea.getDocument().addUndoableEditListener(manager);
		
		
		
		textArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                SwingUtilities.invokeLater(() -> updateStatusBar()); // Use invokeLater
                markAsModified();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                SwingUtilities.invokeLater(() -> updateStatusBar()); // Use invokeLater
                markAsModified();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // Not needed for plain text, but included for completeness
                SwingUtilities.invokeLater(() -> updateStatusBar()); // Use invokeLater
                markAsModified();
            }
        });

		mainFrame.add(scrollPane,BorderLayout.CENTER);
		
	}
	private void updateStatusBar() {
        try {
            int caretPosition = textArea.getCaretPosition();
            int lineNumber = textArea.getLineOfOffset(caretPosition); // Directly get the line number
            int columnNumber = caretPosition - textArea.getLineStartOffset(lineNumber);

            statusBar.setText("Ln " + (lineNumber + 1) + ", Col " + (columnNumber + 1)); // 1-based
        } catch (Exception ex) {
            statusBar.setText("Error updating status");
        }
    }

	private void setDefaults() {
		fontName = "Arial";
		fontStyle = Font.PLAIN;
		size = 18;
		enabledTheme = "";
	}
    private void createMenuBar() {
		menuBar = new JMenuBar();
		mainFrame.setJMenuBar(menuBar);

		fileMenu = new JMenu("File");
		menuBar.add(fileMenu);

		editMenu = new JMenu("Edit");
		menuBar.add(editMenu);

		formatMenu = new JMenu("Format");
		menuBar.add(formatMenu);

		viewMenu = new JMenu("View");
		theme = new JMenu("Theme");
		viewMenu.add(theme);
		viewMenu.add(statusBarCheckBox);
		menuBar.add(viewMenu);
		
		extraMenu = new JMenu("Extra");
		menuBar.add(extraMenu);

		createFileMenu();
		createFormatMenu();
		createEditMenu();
		createViewMenu();
		createExtraMenu();
	}
	private void createFileMenu() {
		newItem = new JMenuItem("New");
		newItem.addActionListener(this);
		newItem.setActionCommand("New");
		newItem.setAccelerator(KeyStroke.
				getKeyStroke(KeyEvent.VK_N,KeyEvent.CTRL_DOWN_MASK));
		fileMenu.add(newItem);

		openItem = new JMenuItem("Open");
		openItem.addActionListener(this);
		openItem.setActionCommand("Open");
		openItem.setAccelerator(KeyStroke.
				getKeyStroke(KeyEvent.VK_O,KeyEvent.CTRL_DOWN_MASK));
		fileMenu.add(openItem);

		saveItem = new JMenuItem("Save");
		saveItem.addActionListener(this);
		saveItem.setActionCommand("Save");
		saveItem.setAccelerator(KeyStroke.
				getKeyStroke(KeyEvent.VK_S,KeyEvent.CTRL_DOWN_MASK));
		fileMenu.add(saveItem);

		saveasItem = new JMenuItem("Save As");
		saveasItem.addActionListener(this);
		saveasItem.setActionCommand("SaveAs");
		saveasItem.setAccelerator(KeyStroke.
				getKeyStroke(KeyEvent.VK_S,InputEvent.CTRL_MASK+InputEvent.SHIFT_MASK));
		fileMenu.add(saveasItem);

		printItem = new JMenuItem("Print");
		printItem.addActionListener(this);
		printItem.setActionCommand("Print");
		printItem.setAccelerator(KeyStroke.
				getKeyStroke(KeyEvent.VK_P,KeyEvent.CTRL_DOWN_MASK));
		fileMenu.add(printItem);

		exitItem = new JMenuItem("Exit");
		exitItem.addActionListener(this);
		exitItem.setActionCommand("Exit");
		exitItem.setAccelerator(KeyStroke.
				getKeyStroke(KeyEvent.VK_W,KeyEvent.CTRL_DOWN_MASK));
		fileMenu.add(exitItem);
	}
	public void createFormatMenu() {
		wordWrapItem = new JMenuItem("Word Wrap \u274c"); //on : \u2713 off: \u274c
		wordWrapItem.addActionListener(this);
		wordWrapItem.setActionCommand("Wrap");
		formatMenu.add(wordWrapItem);

		fontItem = new JMenuItem("Font..");
		fontItem.addActionListener(this);
		fontItem.setActionCommand("Font");
		formatMenu.add(fontItem);
	}
	public void createEditMenu() {
		undo = new JMenuItem("Undo");
		undo.addActionListener(this);
		undo.setActionCommand("undo");
		undo.setAccelerator(KeyStroke.
				getKeyStroke(KeyEvent.VK_Z,KeyEvent.CTRL_DOWN_MASK));
		editMenu.add(undo);

		redo = new JMenuItem("Redo");
		redo.addActionListener(this);
		redo.setActionCommand("redo");
		redo.setAccelerator(KeyStroke.
				getKeyStroke(KeyEvent.VK_Y,KeyEvent.CTRL_DOWN_MASK));
		editMenu.add(redo);

		copy = new JMenuItem("Copy");
		copy.addActionListener(this);
		copy.setActionCommand("copy");
		copy.setAccelerator(KeyStroke.
				getKeyStroke(KeyEvent.VK_C,KeyEvent.CTRL_DOWN_MASK));
		editMenu.add(copy);

		cut = new JMenuItem("Cut");
		cut.addActionListener(this);
		cut.setActionCommand("cut");
		cut.setAccelerator(KeyStroke.
				getKeyStroke(KeyEvent.VK_X,KeyEvent.CTRL_DOWN_MASK));
		editMenu.add(cut);

		paste = new JMenuItem("Paste");
		paste.addActionListener(this);
		paste.setActionCommand("paste");
		paste.setAccelerator(KeyStroke.
				getKeyStroke(KeyEvent.VK_V,KeyEvent.CTRL_DOWN_MASK));
		editMenu.add(paste);

		selectAll = new JMenuItem("Select all");
		selectAll.addActionListener(this);
		selectAll.setActionCommand("selectAll");
		selectAll.setAccelerator(KeyStroke.
				getKeyStroke(KeyEvent.VK_A,KeyEvent.CTRL_DOWN_MASK));
		editMenu.add(selectAll);

	}
	public void createViewMenu() {
		light = new JRadioButtonMenuItem("Light");
		light.addActionListener(this);
		light.setActionCommand("light");

		dark = new JRadioButtonMenuItem("Dark");
		dark.addActionListener(this);
		dark.setActionCommand("dark");
		
		monokai = new JRadioButtonMenuItem("Monokai");
		monokai.addActionListener(this);
		monokai.setActionCommand("monokai");
		
		obsidian = new JRadioButtonMenuItem("Obsidian");
		obsidian.addActionListener(this);
		obsidian.setActionCommand("obsidian");
		
		solarizedLight = new JRadioButtonMenuItem("Solarized light");
		solarizedLight.addActionListener(this);
		solarizedLight.setActionCommand("solarizedLight");
		
		solarizedDark = new JRadioButtonMenuItem("Solarized dark");
		solarizedDark.addActionListener(this);
		solarizedDark.setActionCommand("solarizedDark");
		
		themeGroup = new ButtonGroup();		
		themeGroup.add(light);
		themeGroup.add(dark);
		themeGroup.add(monokai);
		themeGroup.add(obsidian);
		themeGroup.add(solarizedLight);
		themeGroup.add(solarizedDark);
		theme.add(light);
		theme.add(dark);
		theme.add(monokai);
		theme.add(obsidian);
		theme.add(solarizedLight);
		theme.add(solarizedDark);
		
	}
	private void createExtraMenu() {
		voiceTyping = new JMenuItem("Voice Typing");
		voiceTyping.addActionListener(this);
		voiceTyping.setActionCommand("voiceTyping");
		extraMenu.add(voiceTyping);
		
		exportToPdf = new JMenuItem("Export to PDF");
		exportToPdf.addActionListener(this);
		exportToPdf.setActionCommand("exportToPdf");
		extraMenu.add(exportToPdf);
		
		about = new JMenuItem("About");
		about.addActionListener(this);
		about.setActionCommand("about");
		extraMenu.add(about);
	}
	
	

	@Override
	public void actionPerformed(ActionEvent e) {

		String actionCommand = e.getActionCommand();

		switch(actionCommand) {
		case "New"     : file.newFile(); break;
		case "Open"    : file.openFile(); break;
		case "Save"    : file.saveFile(); break;
		case "SaveAs" : file.saveAsFile("Save As"); break;
		case "Print"   : file.printFile(); break;
		case "Exit"    : file.exitFile(); break;

		case "Wrap"    : format.wordWrap(); break;
		case "Font"    : format.fontStyle(); break;

		case "undo"    : edit.undo(); break;
		case "redo"    : edit.redo(); break;
		case "copy"    : edit.copy(); break;
		case "paste"   : edit.paste(); break;
		case "selectAll" : edit.selectAll(); break;

		case "light"   : view.changeTheme(actionCommand); break;
		case "dark"	   : view.changeTheme(actionCommand); break;
		case "monokai" : view.changeTheme(actionCommand); break;
		case "obsidian": view.changeTheme(actionCommand); break;
		case "solarizedLight" : view.changeTheme(actionCommand); break;
		case "solarizedDark"  : view.changeTheme(actionCommand); break;
		
		case "voiceTyping" : SwingUtilities.invokeLater(() -> speech.startRecognition()); break;
		case "exportToPdf" : extras.exportToPDF(textArea); break;
		case "about" : SwingUtilities.invokeLater(() -> extras.showAboutDialog()); break;
		}
		
		boolean isChecked = statusBarCheckBox.isSelected();
		if(isChecked) {
			statusBar.setVisible(true);
			mainFrame.add(statusBar,BorderLayout.SOUTH);
			settings.saveStatusBarOption(isChecked);
		}
		else {
			statusBar.setVisible(false);
			mainFrame.remove(statusBar);
			settings.saveStatusBarOption(isChecked);
		}
		mainFrame.revalidate(); // Revalidates to update layout
		mainFrame.repaint(); // repaint to reflect changes

	}
	public JFrame getMainFrameInstance() {
		return mainFrame;
	}
	public JTextArea getMainTextArea() {
		return textArea;
	}
	public String getSelectedFontName() {
		return fontName;
	}
	public int getSelectedFontStyle() {
		return fontStyle;
	}
	public int getSelectedFontSize() {
		return size;
	}
	private void setEnabledTheme(String theme) {
		
		HashMap<String,JRadioButtonMenuItem> radioButtonMap = new HashMap<>();
		radioButtonMap.put("light", light);
		radioButtonMap.put("dark", dark);
		radioButtonMap.put("monokai", monokai);
		radioButtonMap.put("obsidian", obsidian);
		radioButtonMap.put("solarizedLight", solarizedLight);
		radioButtonMap.put("solarizedDark", solarizedDark);
		
		if (radioButtonMap.containsKey(theme)) {
            JRadioButtonMenuItem rb = radioButtonMap.get(theme);
            themeGroup.clearSelection();
            rb.setSelected(true);
            rb.doClick();
            
            mainFrame.revalidate();
            mainFrame.repaint();
        }
	}
	
	private void markAsModified() {
        if (!isModified) {
            isModified = true;
            updateTitle();
        }
    }
	public void updateTitle() {
        String title = isModified ? file.fileName + " *" : file.fileName;
        mainFrame.setTitle(title);
    }
	
	public boolean confirmSaveBeforeExit() {
        if (isModified) {
            int choice = JOptionPane.showOptionDialog(
                    mainFrame,
                    "You have unsaved changes. Do you want to save before exiting?",
                    "Save Changes",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.WARNING_MESSAGE,
                    null,
                    new String[]{"Save", "Don't Save", "Cancel"},
                    "Save"
            );

            if (choice == JOptionPane.YES_OPTION) {
                file.saveFile();
            }
            return choice != JOptionPane.CANCEL_OPTION;
        }
        return true; // No changes, safe to exit
    }
	public void exitApplication() {
	    if (confirmSaveBeforeExit()) {
	        System.exit(0);
	    }
	}

}
