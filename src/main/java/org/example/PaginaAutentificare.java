package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

public class PaginaAutentificare extends JFrame {
    IMDB imdb = IMDB.getInstance();

    private JTextField textFieldUtilizator;
    private JPasswordField passwordFieldParola;

    public PaginaAutentificare() {
        setTitle("IMDB");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        try {
            URL imageUrl = new URL("https://m.media-amazon.com/images/G/01/imdb/images-ANDW73HA/favicon_desktop_32x32._CB1582158068_.png");

            // incarca imaginea de la URL
            ImageIcon icon = new ImageIcon(imageUrl);
            setIconImage(icon.getImage());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        panel.add(new JLabel("Utilizator:"));
        textFieldUtilizator = new JTextField();
        panel.add(textFieldUtilizator);

        panel.add(new JLabel("Parola:"));
        passwordFieldParola = new JPasswordField();
        panel.add(passwordFieldParola);

        JButton btnAutentificare = new JButton("Autentificare");
        btnAutentificare.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                autentificare();
            }
        });
        panel.add(btnAutentificare);

        add(panel);

        setVisible(true);
    }

    private void autentificare() {
        String utilizator = textFieldUtilizator.getText();
        char[] parola = passwordFieldParola.getPassword();

        System.out.println(utilizator);
        System.out.println(parola);

        boolean autentificat = false;

        for (User user : imdb.users) {
            if (user.getInformation().getCredentials().getUsername().equals(utilizator) && comparaStringCuCharArray(user.getInformation().getCredentials().getPassword(), parola)) {
                JOptionPane.showMessageDialog(this, "Autentificare reusita pentru utilizator: " + utilizator);
                autentificat = true;

                PaginaPrincipala paginaPrincipala = new PaginaPrincipala(user);
                paginaPrincipala.setVisible(true);

                System.out.println("Experienta: " + user.getExperienta());

                textFieldUtilizator.setText("");
                passwordFieldParola.setText("");

                dispose();
                break;
            }
        }
        if (!autentificat) {
            JOptionPane.showMessageDialog(this, "Autentificare nereusita. Verificati va rugam datele de conectare si incercati din nou!");
        }
    }

    public static boolean comparaStringCuCharArray(String str, char[] charArray) {
        if (str.length() != charArray.length) {
            return false;
        }

        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) != charArray[i]) {
                return false;
            }
        }

        return true;
    }

}
