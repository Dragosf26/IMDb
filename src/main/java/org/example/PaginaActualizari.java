package org.example;

import javax.swing.*;
import java.awt.*;

public class PaginaActualizari extends JFrame {

    private final User user;
    IMDB imdb = IMDB.getInstance();

    public PaginaActualizari(User user) {
        this.user = user;

        initializeUI();
    }

    private void initializeUI() {
        setTitle("Pagina Actualizari");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new GridLayout(0, 1));

        JLabel titleLabel = new JLabel("Ce actualizati?");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(titleLabel);

        String[] options = {"Productie", "Actor"};
        JComboBox<String> optionsComboBox = new JComboBox<>(options);
        DefaultListCellRenderer renderer = new DefaultListCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        optionsComboBox.setRenderer(renderer);
        mainPanel.add(optionsComboBox);

        JButton updateButton = new JButton("Actualizare");
        updateButton.addActionListener(e -> handleUpdateAction(optionsComboBox.getSelectedIndex() + 1));
        mainPanel.add(updateButton);

        add(mainPanel);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void handleUpdateAction(int selectedIndex) {
        try {
            String userInput;
            if (selectedIndex == 1) {
                userInput = JOptionPane.showInputDialog(this, "Introduceti numele productiei:");
            } else if (selectedIndex == 2) {
                userInput = JOptionPane.showInputDialog(this, "Introduceti numele actorului:");
            } else {
                JOptionPane.showMessageDialog(this, "Alegere incorecta!");
                return;
            }

            if (userInput != null && !userInput.isEmpty()) {
                if (selectedIndex == 1) {
                    for (Production production : imdb.productions) {
                        if (production.getTitle().equals(userInput)) {
                            ((Staff) user).updateProductionGUI(production, this);
                            JOptionPane.showMessageDialog(this, "Productie actualizata cu succes!");
                            return;
                        }
                    }
                } else {
                    for (Actor actor : imdb.actors) {
                        if (actor.getName().equals(userInput)) {
                            ((Staff) user).updateActorGUI(actor, this);
                            JOptionPane.showMessageDialog(this, "Actor actualizat cu succes!");
                            return;
                        }
                    }
                }
                JOptionPane.showMessageDialog(this, "Numele introdus nu a fost gasit!");
            } else {
                JOptionPane.showMessageDialog(this, "Numele introdus nu este valid!");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Optiune invalida!");
        }
    }

}
