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

// Classe pour créer un nouveau devis
public class NouveauDevisUI extends JFrame {

    private JTextField demandeIdField;
    private JTextField operateurIdField;
    private JTextField montantField;

    // Constructeur de la classe NouveauDevisUI
    public NouveauDevisUI() {
        setTitle("Nouveau Devis"); // Titre de la fenêtre
        setSize(400, 300); // Taille de la fenêtre
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Comportement de fermeture

        // Panel pour contenir les composants de la fenêtre
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2)); // Disposition des composants en grille

        // Champs de texte et étiquettes pour saisir les informations du devis
        JLabel demandeIdLabel = new JLabel("ID de la Demande:");
        demandeIdField = new JTextField();
        JLabel operateurIdLabel = new JLabel("ID de l'Opérateur:");
        operateurIdField = new JTextField();
        JLabel montantLabel = new JLabel("Montant:");
        montantField = new JTextField();
        JLabel statutLabel = new JLabel("Statut:");
        String[] statutOptions = {"créé", "modifié", "validé"};
        JComboBox<String> statutComboBox = new JComboBox<>(statutOptions);

        // Bouton pour créer le devis
        JButton creerButton = new JButton("Créer");
        creerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String demandeId = demandeIdField.getText();
                String operateurId = operateurIdField.getText();
                String montant = montantField.getText();
                String statut = (String) statutComboBox.getSelectedItem();
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                // Insertion du devis dans la base de données
                try (Connection conn = DBUtil.getConnection()) {
                    String query = "INSERT INTO Devis (demande_id, operateur_id, montant, statut, date_creation) VALUES (?, ?, ?, ?, ?)";
                    PreparedStatement statement = conn.prepareStatement(query);
                    statement.setInt(1, Integer.parseInt(demandeId));
                    statement.setInt(2, Integer.parseInt(operateurId));
                    //statement.setBigDecimal(3, new BigDecimal(montant));
                    statement.setString(4, statut);
                    statement.setString(5, sdf.format(date));

                    statement.executeUpdate();

                    // Message de confirmation
                    JOptionPane.showMessageDialog(NouveauDevisUI.this,
                            "Devis créé avec succès!",
                            "Succès",
                            JOptionPane.INFORMATION_MESSAGE);

                    // Réinitialisation des champs après création
                    demandeIdField.setText("");
                    operateurIdField.setText("");
                    montantField.setText("");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(NouveauDevisUI.this,
                            "Erreur lors de la création du devis",
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Ajout des composants au panel
        panel.add(demandeIdLabel);
        panel.add(demandeIdField);
        panel.add(operateurIdLabel);
        panel.add(operateurIdField);
        panel.add(montantLabel);
        panel.add(montantField);
        panel.add(statutLabel);
        panel.add(statutComboBox);
        panel.add(creerButton);

        // Ajout du panel à la fenêtre
        add(panel);
        setVisible(true); // Rendre la fenêtre visible
    }
}

