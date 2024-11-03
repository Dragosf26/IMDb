package org.example;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.List;

public class ListaActorilorFrame extends JFrame {

    public ListaActorilorFrame(List<Actor> actors) {
        super("Lista Actorilor");

        setLayout(new BorderLayout());

        JTextArea textArea = new JTextArea(20, 40);
        textArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);

        Collections.sort(actors, (a1, a2) -> a1.getName().compareToIgnoreCase(a2.getName()));

        StringBuilder actorsList = new StringBuilder("Lista Actorilor:\n");
        for (Actor actor : actors) {
            actorsList.append(actor.getName()).append("\n");
        }
        textArea.setText(actorsList.toString());

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
