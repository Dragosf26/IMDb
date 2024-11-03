package org.example;

import javax.swing.*;

public class MainFrame extends JFrame {
    private JTextArea textArea;

    public MainFrame() {
        setTitle("Detalii Productii");
        setSize(600, 400);

        textArea = new JTextArea();
        textArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public void adaugaDetalii(String detalii) {

        textArea.append(detalii);
    }
}