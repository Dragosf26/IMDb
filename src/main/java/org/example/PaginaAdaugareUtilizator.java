package org.example;

import org.example.Enum.AccountType;
import org.example.Exceptions.InformationIncompleteException;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class PaginaAdaugareUtilizator extends JFrame {

    private User user;

    IMDB imdb = IMDB.getInstance();

    public PaginaAdaugareUtilizator(User users) {
        this.user = users;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Adaugare Utilizator");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new GridLayout(3, 1));

        String[] options = {"Adaugati", "Stergeti"};
        JComboBox<String> optionsComboBox = new JComboBox<>(options);
        DefaultListCellRenderer renderer = new DefaultListCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        optionsComboBox.setRenderer(renderer);

        JLabel label = new JLabel("Adaugati sau stergeti un utilizator?");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(label);
        mainPanel.add(optionsComboBox);

        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> handleOkAction(optionsComboBox.getSelectedIndex()));
        mainPanel.add(okButton);

        add(mainPanel);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void handleOkAction(int selectedIndex) {
        if (selectedIndex == 0) {
            handleAdaugareUtilizator();
        } else if (selectedIndex == 1) {
            handleStergereUtilizator();
        }

        JOptionPane.showMessageDialog(this, "Actiune efectuata cu succes!");
    }

    private void handleAdaugareUtilizator() {
        try {
            String accountTypeString = JOptionPane.showInputDialog("Introduceti tipul de utilizator");
            AccountType accountType = AccountType.valueOf(accountTypeString);

            String username = JOptionPane.showInputDialog("Introduceti username");

            int experienta = 0;

            List<String> notificari = new ArrayList<>();

            SortedSet<Object> productieActoriPreferati = new TreeSet<>();

            String email = JOptionPane.showInputDialog("Introduceti email");
            if (email == null || email.isEmpty()) {
                throw new InformationIncompleteException("Email invalid!");
            }

            String password = JOptionPane.showInputDialog("Introduceti parola");
            if (password == null || password.isEmpty()) {
                throw new InformationIncompleteException("Parola invalida!");
            }

            Credentials credentials = new Credentials(email, password);

            String name = JOptionPane.showInputDialog("Introduceti numele");
            if (name == null || name.isEmpty()) {
                throw new InformationIncompleteException("Nume invalid!");
            }

            String country = JOptionPane.showInputDialog("Introduceti tara");
            int age = Integer.parseInt(JOptionPane.showInputDialog("Introduceti varsta"));

            char gender = JOptionPane.showInputDialog("Introduceti noul genul(M/F)").charAt(0);

            String birthDateInput = JOptionPane.showInputDialog("Introduceti data de nastere(\"yyyy-MM-dd\")");
            final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate birthDate = LocalDate.parse(birthDateInput, formatter);

            User.Information information = new User.Information.Builder()
                    .setCredentials(credentials)
                    .setName(name)
                    .setCountry(country)
                    .setAge(age)
                    .setGender(gender)
                    .setBirthDate(birthDate)
                    .build();

            if (accountType.equals(AccountType.Regular)) {
                Regular regular = new Regular(information, accountType, username, experienta, notificari, productieActoriPreferati);
                imdb.users.add(regular);
            } else if (accountType.equals(AccountType.Contributor)) {
                List<Request> requestList = new ArrayList<>();
                SortedSet<String> productieActoriAdaugati = new TreeSet<>();

                Contributor contributor = new Contributor(information, accountType, username, experienta, notificari, productieActoriPreferati, requestList, productieActoriAdaugati);
                imdb.users.add(contributor);
            } else {
                List<Request> requestList = new ArrayList<>();
                SortedSet<String> productieActoriAdaugati = new TreeSet<>();

                Admin admin = new Admin(information, accountType, username, experienta, notificari, productieActoriPreferati, requestList, productieActoriAdaugati);
                imdb.users.add(admin);
            }
        } catch (InputMismatchException e) {
            JOptionPane.showMessageDialog(this, "Optiune invalida!");
        } catch (InformationIncompleteException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void handleStergereUtilizator() {
        try {
            String deleteUser = JOptionPane.showInputDialog("Introduceti username");
            for (User user1 : IMDB.getInstance().users) {
                if (user1.getUsername().equals(deleteUser)) {
                    imdb.users.remove(user1);
                    break;
                }
            }
        } catch (InputMismatchException e) {
            JOptionPane.showMessageDialog(this, "Optiune invalida!");
        }
    }
}
