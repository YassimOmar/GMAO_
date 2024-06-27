package gmao;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UpdateResponsableMaintenanceUI extends JFrame {

    private JTextField utilisateurIdField;
    private JTextField nomField;
    private JTextField prenomField;
    private JTextField emailField;
    private String utilisateurId;

    public UpdateResponsableMaintenanceUI(String utilisateurId) {
        this.utilisateurId = utilisateurId;
        setTitle("Modifier Responsable de Maintenance"); // Titre de la fenêtre
        setSize(400, 300); // Taille de la fenêtre
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Comportement de fermeture

        // Panel pour contenir les composants de la fenêtre
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2)); // Disposition des composants en grille

        // Champs de texte et étiquettes pour modifier les informations du responsable de maintenance
        JLabel utilisateurIdLabel = new JLabel("Identifiant du Responsable:");
        utilisateurIdField = new JTextField();
        utilisateurIdField.setEditable(false); // L'identifiant ne peut pas être modifié
        JLabel nomLabel = new JLabel("Nom:");
        nomField = new JTextField();
        JLabel prenomLabel = new JLabel("Prénom:");
        prenomField = new JTextField();
        JLabel emailLabel = new JLabel("Email:");
        emailField = new JTextField();

        // Charger les informations actuelles du responsable de maintenance
        loadResponsableMaintenanceInfo();

        // Bouton pour mettre à jour le responsable de maintenance
        JButton updateButton = new JButton("Mettre à jour");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nom = nomField.getText();
                String prenom = prenomField.getText();
                String email = emailField.getText();

                // Mise à jour du responsable de maintenance dans la base de données
                try (Connection conn = DBUtil.getConnection()) {
                    String query = "UPDATE Utilisateur SET nom = ?, prenom = ?, email = ? WHERE id_Utilisateur = ?";
                    PreparedStatement statement = conn.prepareStatement(query);
                    statement.setString(1, nom);
                    statement.setString(2, prenom);
                    statement.setString(3, email);
                    statement.setString(4, utilisateurId);

                    int rowsUpdated = statement.executeUpdate();

                    if (rowsUpdated > 0) {
                        JOptionPane.showMessageDialog(UpdateResponsableMaintenanceUI.this,
                                "Responsable de maintenance mis à jour avec succès!",
                                "Succès",
                                JOptionPane.INFORMATION_MESSAGE);
                        dispose(); // Fermer la fenêtre après mise à jour
                    } else {
                        JOptionPane.showMessageDialog(UpdateResponsableMaintenanceUI.this,
                                "Aucune mise à jour effectuée",
                                "Avertissement",
                                JOptionPane.WARNING_MESSAGE);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(UpdateResponsableMaintenanceUI.this,
                            "Erreur lors de la mise à jour du responsable de maintenance",
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Ajout des composants au panel
        panel.add(utilisateurIdLabel);
        panel.add(utilisateurIdField);
        panel.add(nomLabel);
        panel.add(nomField);
        panel.add(prenomLabel);
        panel.add(prenomField);
        panel.add(emailLabel);
        panel.add(emailField);
        panel.add(new JLabel()); // Placeholder pour l'espacement
        panel.add(updateButton);

        // Ajout du panel à la fenêtre
        add(panel);
        setLocationRelativeTo(null); // Centrer la fenêtre sur l'écran
        setVisible(true); // Rendre la fenêtre visible
    }

    // Méthode pour charger les informations actuelles du responsable de maintenance
    private void loadResponsableMaintenanceInfo() {
        try (Connection conn = DBUtil.getConnection()) {
            String query = "SELECT * FROM Utilisateur WHERE id_Utilisateur = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, utilisateurId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                utilisateurIdField.setText(resultSet.getString("id_Utilisateur"));
                nomField.setText(resultSet.getString("nom"));
                prenomField.setText(resultSet.getString("prenom"));
                emailField.setText(resultSet.getString("email"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Erreur lors du chargement des informations du responsable de maintenance",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
