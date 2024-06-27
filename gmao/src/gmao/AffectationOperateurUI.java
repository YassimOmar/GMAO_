package gmao;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

// Classe pour affecter un opérateur à une demande de maintenance
public class AffectationOperateurUI extends JFrame {

	private JTextField idAffectationOperateurField;
    private JTextField demandeIdField;
    private JTextField operateurIdField;
   

    // Constructeur de la classe AffectationOperateurUI
    public AffectationOperateurUI() {
        setTitle("Affecter Opérateur"); // Titre de la fenêtre
        setSize(400, 300); // Taille de la fenêtre
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Comportement de fermeture

        // Panel pour contenir les composants de la fenêtre
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2)); // Disposition des composants en grille

        // Champs de texte et étiquettes pour saisir les informations de l'affectation
        JLabel idAffectationOperateurLabel = new JLabel("numero d'affectation:");
        idAffectationOperateurField = new JTextField();
        JLabel demandeIdLabel = new JLabel("ID de la Demande:");
        demandeIdField = new JTextField();
        JLabel operateurIdLabel = new JLabel("ID de l'Opérateur:");
        operateurIdField = new JTextField();
        
        // Bouton pour affecter l'opérateur
        JButton affecterButton = new JButton("Affecter");
        affecterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String idAffectationOperateur = idAffectationOperateurField.getText();
                String demandeId = demandeIdField.getText();
                String operateurId = operateurIdField.getText();
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

                // Insertion de l'affectation dans la base de données
                try (Connection conn = DBUtil.getConnection()) {
                    String query = "INSERT INTO AffectationOperateur (id_AffectationOperateur,demande_id, operateur_id, date_affectation) VALUES (?,?, ?, ?)";
                    PreparedStatement statement = conn.prepareStatement(query);
                    statement.setInt(1, Integer.parseInt(demandeId));
                    statement.setInt(2, Integer.parseInt(demandeId));
                    statement.setInt(3, Integer.parseInt(operateurId));
                    statement.setString(4, sdf.format(date));

                    statement.executeUpdate();

                    // Message de confirmation
                    JOptionPane.showMessageDialog(AffectationOperateurUI.this,
                            "Opérateur affecté avec succès!",
                            "Succès",
                            JOptionPane.INFORMATION_MESSAGE);

                    // Réinitialisation des champs après affectation
                    demandeIdField.setText("");
                    operateurIdField.setText("");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(AffectationOperateurUI.this,
                            "Erreur lors de l'affectation de l'opérateur",
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Ajout des composants au panel
        panel.add(idAffectationOperateurLabel);
        panel.add(idAffectationOperateurField);
        panel.add(demandeIdLabel);
        panel.add(demandeIdField);
        panel.add(operateurIdLabel);
        panel.add(operateurIdField);
        panel.add(affecterButton);

        // Ajout du panel à la fenêtre
        add(panel);
        setVisible(true); // Rendre la fenêtre visible
    }
}

