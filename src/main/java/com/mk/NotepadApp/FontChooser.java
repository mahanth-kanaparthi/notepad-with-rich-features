package com.mk.NotepadApp;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

@SuppressWarnings("serial")
public class FontChooser extends JDialog {
    MainWindow gui;

    private JButton jButton1;
    private JButton jButton2;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel sampleText;
    private JList<String> fontList;
    private JList<String> styleList;
    private JList<Integer> sizeList;
    private JScrollPane jScrollPane1;
    private JScrollPane jScrollPane2;
    private JScrollPane jScrollPane3;
    private JTextField fontSearch;
    private JTextField styleSearch;
    private JTextField sizeSearch;
    private String selectedFont;
    private int selectedStyle;
    private int selectedSize;

    public FontChooser(MainWindow gui) {
        super((Frame) gui.mainFrame.getParent(), "Font Chooser", true);
        this.gui = gui;
        selectedFont = gui.fontName;
        selectedStyle = gui.fontStyle;
        selectedSize = gui.size;

        initComponents((Frame) gui.mainFrame.getParent());
        setLocationRelativeTo(null);
        setIconImage(gui.icon.getImage());
        setVisible(true);
    }

    private void initComponents(Frame parent) {
        jLabel1 = new JLabel();
        jLabel2 = new JLabel();
        jLabel3 = new JLabel();
        fontSearch = new JTextField();
        styleSearch = new JTextField();
        sizeSearch = new JTextField();
        jScrollPane1 = new JScrollPane();
        fontList = new JList<>();
        jScrollPane2 = new JScrollPane();
        styleList = new JList<>();
        jScrollPane3 = new JScrollPane();
        sizeList = new JList<>();
        sampleText = new JLabel();
        jButton1 = new JButton();
        jButton2 = new JButton();

        jLabel1.setText("Font :");
        jLabel2.setText("Font Style :");
        jLabel3.setText("Size :");

        fontSearch.setText(selectedFont);
        styleSearch.setText(String.valueOf(selectedStyle));
        sizeSearch.setText(String.valueOf(selectedSize));

        // Font list
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fontNames = ge.getAvailableFontFamilyNames();
        fontList = new JList<>(fontNames);
        fontList.setSelectedValue(selectedFont, true);
        jScrollPane1.setViewportView(fontList);

        // Style list
        String[] styles = {"Regular", "Bold", "Italic", "Bold Italic"};
        styleList = new JList<>(styles);
        styleList.setSelectedIndex(selectedStyle);
        jScrollPane2.setViewportView(styleList);

        // Size list
        Integer[] sizes = {11, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 42, 44, 46, 48, 50};
        sizeList = new JList<>(sizes);
        sizeList.setSelectedValue(selectedSize, true);
        jScrollPane3.setViewportView(sizeList);

        // Font Search field
        fontSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String search = fontSearch.getText().toLowerCase();
                ListModel<String> model = fontList.getModel();
                for (int i = 0; i < model.getSize(); i++) {
                    if (model.getElementAt(i).toLowerCase().startsWith(search)) {
                        fontList.setSelectedIndex(i);
                        fontList.ensureIndexIsVisible(i);
                        break;
                    }
                }
            }
        });

        // Style Search field
        styleSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String search = styleSearch.getText().toLowerCase();
                ListModel<String> model = styleList.getModel();
                for (int i = 0; i < model.getSize(); i++) {
                    if (model.getElementAt(i).toLowerCase().startsWith(search)) {
                        styleList.setSelectedIndex(i);
                        styleList.ensureIndexIsVisible(i);
                        break;
                    }
                }
            }
        });

        // Size Search field
        sizeSearch.setPreferredSize(new Dimension(72, fontSearch.getPreferredSize().height));
        sizeSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String search = sizeSearch.getText().toLowerCase();
                ListModel<Integer> model = sizeList.getModel();
                for (int i = 0; i < model.getSize(); i++) {
                    if (model.getElementAt(i).toString().startsWith(search)) {
                        sizeList.setSelectedIndex(i);
                        sizeList.ensureIndexIsVisible(i);
                        break;
                    }
                }
            }
        });

        sampleText.setText("The quick brown fox jumps over the lazy dog");
        sampleText.setFont(new Font(selectedFont, selectedStyle, selectedSize));
        sampleText.setHorizontalAlignment(SwingConstants.LEFT);
        sampleText.setBorder(BorderFactory.createTitledBorder(null, "Preview"));

        // List selection listeners
        fontList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                selectedFont = fontList.getSelectedValue();
                updateSampleText();
            }
        });

        styleList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                selectedStyle = styleList.getSelectedIndex();
                updateSampleText();
            }
        });

        sizeList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                selectedSize = sizeList.getSelectedValue();
                updateSampleText();
            }
        });

        // OK button
        jButton1.setText("OK");
        jButton1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gui.textArea.setFont(selectFont());
                setVisible(false);
            }
        });

        // Cancel button
        jButton2.setText("Cancel");
        jButton2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selectedFont = null;
                setVisible(false);
            }
        });

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout
                .createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout
                        .createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup().addGap(207, 207, 207).addComponent(jButton1)
                                        .addGap(18, 18, 18).addComponent(jButton2))
                                .addGroup(layout.createSequentialGroup().addGap(16, 16, 16)
                                        .addGroup(layout
                                                .createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                .addComponent(jScrollPane1)
                                                .addComponent(fontSearch, GroupLayout.DEFAULT_SIZE, 144,
                                                        Short.MAX_VALUE)
                                                .addComponent(
                                                        jLabel1, GroupLayout.DEFAULT_SIZE,
                                                        GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGap(18, 18, 18)
                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                .addComponent(styleSearch, GroupLayout.PREFERRED_SIZE, 97,
                                                        GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabel2, GroupLayout.PREFERRED_SIZE, 97,
                                                        GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jScrollPane2, GroupLayout.PREFERRED_SIZE, 97,
                                                        GroupLayout.PREFERRED_SIZE))
                                        .addGap(18, 18, 18)
                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                .addComponent(sizeSearch, GroupLayout.PREFERRED_SIZE,
                                                        GroupLayout.DEFAULT_SIZE,
                                                        GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jScrollPane3, GroupLayout.PREFERRED_SIZE, 70,
                                                        GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabel3, GroupLayout.PREFERRED_SIZE, 70,
                                                        GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 0, Short.MAX_VALUE))
                        .addGroup(GroupLayout.Alignment.TRAILING,
                                layout.createSequentialGroup().addContainerGap().addComponent(sampleText,
                                        GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
                                        Short.MAX_VALUE)))
                .addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup().addGap(34, 34, 34)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel1, GroupLayout.PREFERRED_SIZE, 24,
                                        GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel2, GroupLayout.PREFERRED_SIZE, 24,
                                        GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel3, GroupLayout.PREFERRED_SIZE, 24,
                                        GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(fontSearch, GroupLayout.PREFERRED_SIZE,
                                        GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(styleSearch, GroupLayout.PREFERRED_SIZE,
                                        GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(sizeSearch, GroupLayout.PREFERRED_SIZE,
                                        GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
                                .addComponent(jScrollPane2, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                .addComponent(jScrollPane3, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addGap(17, 17, 17)
                        .addComponent(sampleText, GroupLayout.PREFERRED_SIZE, 45,
                                GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(jButton1).addComponent(jButton2))
                        .addContainerGap(25, Short.MAX_VALUE)));

        pack();
    }

    private void updateSampleText() {
        sampleText.setFont(new Font(selectedFont, selectedStyle, selectedSize));
        gui.settings.saveFontSettings(selectedFont, selectedStyle, selectedSize);
    }

    public String getSelectedFont() {
        return selectedFont;
    }

    public int getSelectedStyle() {
        return selectedStyle;
    }

    public int getSelectedSize() {
        return selectedSize;
    }

    public Font selectFont() {
        return new Font(getSelectedFont(), getSelectedStyle(), getSelectedSize());
    }
}