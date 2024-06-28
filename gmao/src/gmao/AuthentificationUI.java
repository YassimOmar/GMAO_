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
        setTitle("Authentification");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        JLabel usernameLabel = new JLabel("Nom d'utilisateur:");
        usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Mot de passe:");
        passwordField = new JPasswordField();

        JButton loginButton = new JButton("Connexion");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                // Validation de l'authentification en vérifiant dans la base de données
                if (validateUser(username, password)) {
                    String role = getUserRole(username);
                    dispose(); // Fermer la fenêtre d'authentification après connexion

                    // Redirection vers l'application appropriée en fonction du rôle
                    switch (role) {
                        case "admin":
                            launchAdminApplication();
                            break;
                        case "responsable_de_maintenance":
                            launchMaintenanceApplication();
                            break;
                        case "Operateur_de_maintenance": // Assurez-vous que le rôle exact correspond à celui dans la base de données
                            launchOperatorApplication();
                            break;
                        default:
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

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(new JLabel());
        panel.add(loginButton);

        add(panel);
        setLocationRelativeTo(null);
        setVisible(true);
    }

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

    private void launchAdminApplication() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new la_principale_admin(); // Lancer l'application principale pour un administrateur
            }
        });
    }

    private void launchMaintenanceApplication() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new la_principale(); // Lancer l'application principale pour un responsable de maintenance
            }
        });
    }

    private void launchOperatorApplication() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new PageMaintenanceOperateurUI(); // Lancer l'application principale pour un opérateur
            }
        });
    }


    private String getOperateurName(String username) {
        try (Connection conn = DBUtil.getConnection()) {
            String query = "SELECT nom FROM Utilisateur WHERE nom = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("nom");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Erreur lors de la récupération du nom de l'opérateur",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new AuthentificationUI(); // Lancer la fenêtre d'authentification
            }
        });
    }
}
