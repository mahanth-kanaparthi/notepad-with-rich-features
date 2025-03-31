package com.mk.NotepadApp;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

public class EditMenuFunctions {
	MainWindow gui;

	public EditMenuFunctions(MainWindow gui){
		this.gui = gui;
	}

	public void undo() {
		try {
			gui.manager.undo();
		}catch(CannotUndoException e) {}

	}

	public void redo() {
		try {
			gui.manager.redo();
		}catch(CannotRedoException e) {}

	}

	public void copy() {
		gui.textArea.copy();

	}

	public void paste() {
		gui.textArea.paste();

	}

	public void selectAll() {
		gui.textArea.selectAll();

	}


}
