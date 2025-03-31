package com.mk.NotepadApp;

import org.vosk.Model;
import org.vosk.Recognizer;
import javax.sound.sampled.*;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.json.JSONObject;

import java.io.IOException;

public class SpeechRecognition {

    MainWindow gui;
    private static boolean isPaused = false; // Flag to pause/resume recognition
    private static boolean continueRec = true;
    private Model model;
    private AudioFormat format;
    private DataLine.Info info;
    private TargetDataLine microphone;
    private Recognizer recognizer;
    private byte[] buffer;
    private StringBuilder fullText;

    public SpeechRecognition(MainWindow gui) {
        this.gui = gui;
    }

    private void initializeModelAndRecognizer() {
        try {
            // Load Vosk model
            model = new Model("res/vosk-model");
            // Configure microphone
            format = new AudioFormat(16000, 16, 1, true, false);
            info = new DataLine.Info(TargetDataLine.class, format);
            microphone = (TargetDataLine) AudioSystem.getLine(info);

            // Create recognizer
            recognizer = new Recognizer(model, 16000);
            buffer = new byte[4096];

            // StringBuilder to store recognized text
            fullText = new StringBuilder();

        } catch (IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void startRecognition() {
        // Initialize model and recognizer
        initializeModelAndRecognizer();

        // Start recognition in a separate thread
        new Thread(() -> {
            // Show start dialog
            showDialog("Speech recognition is started..."
                    + "\n\nSay 'Pause Recognition' to Pause,"
                    + "\n'Stop Recognition' to Stop."
                    , "Speech Recognition Started");

			/*
			 * System.out.println("Listening... " +
			 * "Say 'pause recognition', 'resume recognition'" +
			 * ", or 'stop recognition'.");
			 */
            
            try {
                microphone.open(format);
                microphone.start();
            } catch (LineUnavailableException e) {
                e.printStackTrace();
                return;
            }
            continueRec = true;

            // Continuous recognition loop
            while (continueRec) {
                int bytesRead = microphone.read(buffer, 0, buffer.length);
                if (bytesRead < 0) break;

                if (recognizer.acceptWaveForm(buffer, bytesRead)) {
                    String result = recognizer.getResult();
                    String recognizedText = parseJsonText(result);

                    if (!recognizedText.isEmpty()) {
                        if (checkCommands(recognizedText)) {
                            continue; // Skip processing if command was executed
                        }

                        if (!isPaused) {
                            fullText.append(recognizedText).append(" ");
                            SwingUtilities.invokeLater(() -> updateTextArea(recognizedText));
                            //System.out.println("Current Text: " + fullText.toString().trim());
                        }
                    }
                }

                // If paused, enter command-only listening mode
                while (isPaused) {
                    bytesRead = microphone.read(buffer, 0, buffer.length);
                    if (bytesRead < 0) break;

                    if (recognizer.acceptWaveForm(buffer, bytesRead)) {
                        String result = recognizer.getResult();
                        String commandText = parseJsonText(result);

                        if (!commandText.isEmpty() && checkCommands(commandText)) {
                            break; // Exit pause mode if "resume recognition" is detected
                        }
                    }
                }
            }

            //System.err.println("Program ended!");

            // Cleanup
            microphone.close();
            recognizer.close();
            model.close();
        }).start();
    }

    // Method to extract text from Vosk JSON response
    private static String parseJsonText(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            return jsonObject.optString("text", "").trim();
        } catch (Exception e) {
            return "";
        }
    }

    // Check for pause, resume, and stop commands
    private static boolean checkCommands(String command) {
        switch (command.toLowerCase()) {
            case "pause recognition":
                isPaused = true;
                showDialog("▶ Recognition Paused!\n\nTo resume, say 'Resume Recognition'.", "Recognition Paused!");
                //System.out.println("▶ Recognition Paused. Say 'resume recognition' to continue.");
                return true;

            case "resume recognition":
                isPaused = false;
                showDialog("⏯ Recognition Resumed. Keep speaking...","Recognition Resumed!");
                //System.out.println("⏯ Recognition Resumed. Keep speaking...");
                return true;

            case "stop recognition":
                showDialog("🛑 Recognition Stopped!", "Recognition Stopped");
                //System.out.println("🛑 Stopping recognition...");
                continueRec = false;
                return true;

            default:
                return false; // Not a command
        }
    }

    // Show a dialog box with a message
    private static void showDialog(String message, String title) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
        });
    }

    private void updateTextArea(String update) {
        SwingUtilities.invokeLater(() -> {
            gui.textArea.append(update + " ");
            gui.mainFrame.revalidate();
            gui.mainFrame.repaint();
        });
    }
}