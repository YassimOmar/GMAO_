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

    private JTextField demandeMaintenaceField;
    private JTextField clientField;
    private JTextField responsableField;
    private JTextField descriptionField;
    private JTextField classificationField;

    // Constructeur de la classe NouvelleDemandeMaintenanceUI
    public NouvelleDemandeMaintenanceUI() {
        setTitle("Nouvelle Demande de Maintenance"); // Titre de la fenêtre
        setSize(400, 300); // Taille de la fenêtre
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Comportement de fermeture

        // Panel pour contenir les composants de la fenêtre
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7, 2)); // Disposition des composants en grille

        // Champs de texte et étiquettes pour saisir les informations de la demande de maintenance
        JLabel demandeMaintenaceLabel = new JLabel("Numero Demande:");
        demandeMaintenaceField = new JTextField();
        JLabel clientLabel = new JLabel("Client ID:");
        clientField = new JTextField();
        JLabel responsableLabel = new JLabel("identifiant du Responsable :");
        responsableField = new JTextField();
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
            	String demandeMaintenace = demandeMaintenaceField.getText();
                String client = clientField.getText();
                String responsable = responsableField.getText();
                String description = descriptionField.getText();
                String classification = classificationField.getText();
                String statut = (String) statutComboBox.getSelectedItem();
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

                // Insertion de la demande de maintenance dans la base de données
                try (Connection conn = DBUtil.getConnection()) {
                    String query = "INSERT INTO DemandeMaintenance (id_DemandeMaintenance,client_id,responsable_id, description, classification, statut, date_creation) VALUES (?, ?, ?, ?, ?,?,?)";
                    PreparedStatement statement = conn.prepareStatement(query);
                    statement.setInt(1, Integer.parseInt(demandeMaintenace));
                    statement.setInt(2, Integer.parseInt(client));
                    statement.setInt(3, Integer.parseInt(responsable));
                    statement.setString(4, description);
                    statement.setString(5, classification);
                    statement.setString(6, statut);
                    statement.setString(7, sdf.format(date));
                    
                    //execution
                    statement.executeUpdate();

                    // Message de confirmation
                    JOptionPane.showMessageDialog(NouvelleDemandeMaintenanceUI.this,
                            "Demande de maintenance créée avec succès!",
                            "Succès",
                            JOptionPane.INFORMATION_MESSAGE);

                    // Réinitialisation des champs après création
                    demandeMaintenaceField.setText("");
                    clientField.setText("");
                    responsableField.setText("");
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
        panel.add(demandeMaintenaceLabel);
        panel.add(demandeMaintenaceField);
        panel.add(clientLabel);
        panel.add(clientField);
        panel.add(responsableLabel);
        panel.add(responsableField);
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
