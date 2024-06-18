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

// Classe pour créer une nouvelle demande de maintenance
public class NouvelleDemandeMaintenanceUI extends JFrame {

    private JTextField clientField;
    private JTextField descriptionField;
    private JTextField classificationField;

    // Constructeur de la classe NouvelleDemandeMaintenanceUI
    public NouvelleDemandeMaintenanceUI() {
        setTitle("Nouvelle Demande de Maintenance"); // Titre de la fenêtre
        setSize(400, 300); // Taille de la fenêtre
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Comportement de fermeture

        // Panel pour contenir les composants de la fenêtre
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2)); // Disposition des composants en grille

        // Champs de texte et étiquettes pour saisir les informations de la demande de maintenance
        JLabel clientLabel = new JLabel("Client ID:");
        clientField = new JTextField();
        JLabel descriptionLabel = new JLabel("Description:");
        descriptionField = new JTextField();
        JLabel classificationLabel = new JLabel("Classification:");
        classificationField = new JTextField();
        JLabel statutLabel = new JLabel("Statut:");
        String[] statutOptions = {"créée", "validée", "en cours", "terminée"};
        JComboBox<String> statutComboBox = new JComboBox<>(statutOptions);

        // Bouton pour créer la demande de maintenance
        JButton creerButton = new JButton("Créer");
        creerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String client = clientField.getText();
                String description = descriptionField.getText();
                String classification = classificationField.getText();
                String statut = (String) statutComboBox.getSelectedItem();
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                // Insertion de la demande de maintenance dans la base de données
                try (Connection conn = DBUtil.getConnection()) {
                    String query = "INSERT INTO DemandeMaintenance (client_id, description, classification, statut, date_creation) VALUES (?, ?, ?, ?, ?)";
                    PreparedStatement statement = conn.prepareStatement(query);
                    statement.setInt(1, Integer.parseInt(client));
                    statement.setString(2, description);
                    statement.setString(3, classification);
                    statement.setString(4, statut);
                    statement.setString(5, sdf.format(date));

                    statement.executeUpdate();

                    // Message de confirmation
                    JOptionPane.showMessageDialog(NouvelleDemandeMaintenanceUI.this,
                            "Demande de maintenance créée avec succès!",
                            "Succès",
                            JOptionPane.INFORMATION_MESSAGE);

                    // Réinitialisation des champs après création
                    clientField.setText("");
                    descriptionField.setText("");
                    classificationField.setText("");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(NouvelleDemandeMaintenanceUI.this,
                            "Erreur lors de la création de la demande de maintenance",
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Ajout des composants au panel
        panel.add(clientLabel);
        panel.add(clientField);
        panel.add(descriptionLabel);
        panel.add(descriptionField);
        panel.add(classificationLabel);
        panel.add(classificationField);
        panel.add(statutLabel);
        panel.add(statutComboBox);
        panel.add(creerButton);

        // Ajout du panel à la fenêtre
        add(panel);
        setVisible(true); // Rendre la fenêtre visible
    }
}
