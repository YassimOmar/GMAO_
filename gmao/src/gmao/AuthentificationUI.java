package gmao;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthentificationUI extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    public AuthentificationUI() {
        setTitle("Authentification"); // Titre de la fenêtre d'authentification
        setSize(300, 150); // Taille de la fenêtre d'authentification
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Comportement de fermeture

        // Panel pour contenir les composants de la fenêtre d'authentification
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2)); // Disposition des composants en grille

        // Étiquettes et champs de texte pour saisir le nom d'utilisateur et le mot de passe
        JLabel usernameLabel = new JLabel("Nom d'utilisateur:");
        usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Mot de passe:");
        passwordField = new JPasswordField();

        // Bouton de connexion
        JButton loginButton = new JButton("Connexion");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                // Validation de l'authentification en vérifiant dans la base de données
                if (validateUser(username, password)) {
                    String role = getUserRole(username); // Récupérer le rôle de l'utilisateur
                    dispose(); // Fermer la fenêtre d'authentification

                    // Ouvrir la fenêtre appropriée en fonction du rôle
                    if (role.equalsIgnoreCase("admin")) {
                        launchAdminApplication();
                    } else if (role.equalsIgnoreCase("responsable_de_maintenance")) {
                        launchMainApplication();
                    } else {
                        JOptionPane.showMessageDialog(null,
                                "Rôle non pris en charge!",
                                "Erreur d'authentification",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(AuthentificationUI.this,
                            "Nom d'utilisateur ou mot de passe incorrect!",
                            "Erreur d'authentification",
                            JOptionPane.ERROR_MESSAGE);
                    // Réinitialiser les champs après une tentative incorrecte
                    usernameField.setText("");
                    passwordField.setText("");
                }
            }
        });

        // Ajout des composants au panel
        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(new JLabel()); // Placeholder pour l'espacement
        panel.add(loginButton);

        // Ajout du panel à la fenêtre d'authentification
        add(panel);
        setLocationRelativeTo(null); // Centrer la fenêtre d'authentification sur l'écran
        setVisible(true); // Rendre la fenêtre d'authentification visible
    }

    // Méthode pour valider l'authentification en vérifiant dans la base de données
    private boolean validateUser(String username, String password) {
        try (Connection conn = DBUtil.getConnection()) {
            String query = "SELECT * FROM Utilisateur WHERE nom = ? AND mot_de_passe = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();

            // Vérifier si l'utilisateur existe dans la base de données
            return resultSet.next();

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Erreur lors de l'authentification",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    // Méthode pour récupérer le rôle de l'utilisateur à partir de la base de données
    private String getUserRole(String username) {
        try (Connection conn = DBUtil.getConnection()) {
            String query = "SELECT role FROM Utilisateur WHERE nom = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            // Récupérer le rôle de l'utilisateur
            if (resultSet.next()) {
                return resultSet.getString("role");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Erreur lors de la récupération du rôle de l'utilisateur",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    // Méthode pour lancer l'application principale pour un admin
    private void launchAdminApplication() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new la_principale_admin(); // Lancer l'application principale pour un admin
            }
        });
    }

    // Méthode pour lancer l'application principale pour un responsable de maintenance
    private void launchMainApplication() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new la_principale(); // Lancer l'application principale pour un responsable de maintenance
            }
        });
    }

    // Méthode principale pour tester l'authentification
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new AuthentificationUI(); // Lancer la fenêtre d'authentification
            }
        });
    }
}
