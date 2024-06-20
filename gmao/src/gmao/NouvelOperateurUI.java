package gmao;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

// Classe pour ajouter un nouvel opérateur
public class NouvelOperateurUI extends JFrame {
	
	private JTextField utilisateurIdField;
    private JTextField nomField;
    private JTextField prenomField;
    private JTextField emailField;
    private JTextField motDePasseField;
    private JTextField roleField;

    // Constructeur de la classe NouvelOperateurUI
    public NouvelOperateurUI() {
        setTitle("Ajouter un Nouvel Opérateur"); // Titre de la fenêtre
        setSize(400, 300); // Taille de la fenêtre
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Comportement de fermeture

        // Panel pour contenir les composants de la fenêtre
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7, 2)); // Disposition des composants en grille

        // Champs de texte et étiquettes pour saisir les informations de l'opérateur
        JLabel utilisateurIdLabel = new JLabel("identifiant de l'operateur:");
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
        roleField = new JTextField("Operateur_de_maintenance");

        // Bouton pour ajouter l'opérateur
        JButton ajouterButton = new JButton("Ajouter");
        ajouterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	String utilisateurId = utilisateurIdField.getText();
                String nom = nomField.getText();
                String prenom = prenomField.getText();
                String email = emailField.getText();
                String motDePasse = motDePasseField.getText();
                String role = roleField.getText();

                // Insertion de l'opérateur dans la base de données
                try (Connection conn = DBUtil.getConnection()) {
                    String query = "INSERT INTO Utilisateur (id_Utilisateur, nom, prenom, email, mot_de_passe, role) VALUES (?, ?, ?, ?, ?, ?)";
                    PreparedStatement statement = conn.prepareStatement(query);
                    statement.setInt(1, Integer.parseInt(utilisateurId));
                    statement.setString(2, nom);
                    statement.setString(3, prenom);
                    statement.setString(4, email);
                    statement.setString(5, motDePasse);
                    statement.setString(6, role);

                    statement.executeUpdate();

                    // Message de confirmation
                    JOptionPane.showMessageDialog(NouvelOperateurUI.this,
                            "Opérateur ajouté avec succès!",
                            "Succès",
                            JOptionPane.INFORMATION_MESSAGE);

                    // Réinitialisation des champs après ajout
                    utilisateurIdField.setText("");
                    nomField.setText("");
                    prenomField.setText("");
                    emailField.setText("");
                    motDePasseField.setText("");
                    roleField.setText("");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(NouvelOperateurUI.this,
                            "Erreur lors de l'ajout de l'opérateur",
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
        panel.add(ajouterButton);

        // Ajout du panel à la fenêtre
        add(panel);
        setVisible(true); // Rendre la fenêtre visible
    }
}
