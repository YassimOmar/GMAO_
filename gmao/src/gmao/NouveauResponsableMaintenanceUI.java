package gmao;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class NouveauResponsableMaintenanceUI extends JFrame {

    private JTextField utilisateurIdField;
    private JTextField nomField;
    private JTextField prenomField;
    private JTextField emailField;
    private JTextField motDePasseField;
    private JTextField roleField;

    public NouveauResponsableMaintenanceUI() {
        setTitle("Créer un Responsable de Maintenance"); // Titre de la fenêtre
        setSize(400, 300); // Taille de la fenêtre
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Comportement de fermeture

        // Panel pour contenir les composants de la fenêtre
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7, 2)); // Disposition des composants en grille

        // Champs de texte et étiquettes pour saisir les informations du responsable de maintenance
        JLabel utilisateurIdLabel = new JLabel("Identifiant du Responsable:");
        utilisateurIdField = new JTextField();
        JLabel nomLabel = new JLabel("Nom:");
        nomField = new JTextField();
        JLabel prenomLabel = new JLabel("Prénom:");
        prenomField = new JTextField();
        JLabel emailLabel = new JLabel("Email:");
        emailField = new JTextField();
        JLabel motDePasseLabel = new JLabel("Mot de passe:");
        motDePasseField = new JTextField();
        JLabel roleLabel = new JLabel("Role:");
        roleField = new JTextField("responsable_de_maintenance"); // Valeur par défaut

        // Bouton pour créer le responsable de maintenance
        JButton creerButton = new JButton("Créer");
        creerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String utilisateurId = utilisateurIdField.getText();
                String nom = nomField.getText();
                String prenom = prenomField.getText();
                String email = emailField.getText();
                String motDePasse = motDePasseField.getText();
                String role = roleField.getText();

                // Insertion du responsable de maintenance dans la base de données
                try (Connection conn = DBUtil.getConnection()) {
                    String query = "INSERT INTO Utilisateur (id_Utilisateur, nom, prenom, email, mot_de_passe, role) VALUES (?, ?, ?, ?, ?, ?)";
                    PreparedStatement statement = conn.prepareStatement(query);
                    statement.setInt(1, Integer.parseInt(utilisateurId));
                    statement.setString(2, nom);
                    statement.setString(3, prenom);
                    statement.setString(4, email);
                    statement.setString(5, motDePasse);
                    statement.setString(6, role);

                    int rowsInserted = statement.executeUpdate();

                    if (rowsInserted > 0) {
                        JOptionPane.showMessageDialog(NouveauResponsableMaintenanceUI.this,
                                "Responsable de maintenance créé avec succès!",
                                "Succès",
                                JOptionPane.INFORMATION_MESSAGE);

                        // Réinitialisation des champs après création
                        utilisateurIdField.setText("");
                        nomField.setText("");
                        prenomField.setText("");
                        emailField.setText("");
                        motDePasseField.setText("");
                        roleField.setText("responsable_de_maintenance");
                    } else {
                        JOptionPane.showMessageDialog(NouveauResponsableMaintenanceUI.this,
                                "Erreur lors de la création du responsable de maintenance",
                                "Erreur",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(NouveauResponsableMaintenanceUI.this,
                            "Erreur lors de la création du responsable de maintenance",
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
        panel.add(motDePasseLabel);
        panel.add(motDePasseField);
        panel.add(roleLabel);
        panel.add(roleField);
        panel.add(new JLabel()); // Placeholder pour l'espacement
        panel.add(creerButton);

        // Ajout du panel à la fenêtre
        add(panel);
        setLocationRelativeTo(null); // Centrer la fenêtre sur l'écran
        setVisible(true); // Rendre la fenêtre visible
    }
}
