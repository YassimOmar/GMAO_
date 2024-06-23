package gmao;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UpdateClientUI extends JFrame {
    
    private JTextField id_ClientField;
    private JTextField nomField;
    private JTextField ifuField;
    private JTextField rccmField;
    private JTextField adresseField;
    private JTextField codeApeField;
    private JTextField contactField;
    private String clientId;

    public UpdateClientUI(String clientId) {
        this.clientId = clientId;
        setTitle("Modifier Client"); // Titre de la fenêtre
        setSize(400, 300); // Taille de la fenêtre
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Comportement de fermeture

        // Panel pour contenir les composants de la fenêtre
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(8, 2)); // Disposition des composants en grille

        // Champs de texte et étiquettes pour saisir les informations du client
        JLabel id_ClientLabel = new JLabel("Identifiant:");
        id_ClientField = new JTextField();
        id_ClientField.setEditable(false); // L'identifiant ne peut pas être modifié
        JLabel nomLabel = new JLabel("Nom:");
        nomField = new JTextField();
        JLabel ifuLabel = new JLabel("Numéro IFU:");
        ifuField = new JTextField();
        JLabel rccmLabel = new JLabel("Numéro RCCM:");
        rccmField = new JTextField();
        JLabel adresseLabel = new JLabel("Adresse:");
        adresseField = new JTextField();
        JLabel codeApeLabel = new JLabel("Code APE:");
        codeApeField = new JTextField();
        JLabel contactLabel = new JLabel("Contact:");
        contactField = new JTextField();

        // Charger les informations actuelles du client
        loadClientInfo();

        // Bouton pour mettre à jour le client
        JButton updateButton = new JButton("Mettre à jour");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nom = nomField.getText();
                String ifu = ifuField.getText();
                String rccm = rccmField.getText();
                String adresse = adresseField.getText();
                String codeApe = codeApeField.getText();
                String contact = contactField.getText();

                // Mise à jour du client dans la base de données
                try (Connection conn = DBUtil.getConnection()) {
                    String query = "UPDATE Client SET nom = ?, numero_IFU = ?, numero_RCCM = ?, adresse = ?, code_APE = ?, contact = ? WHERE id_Client = ?";
                    PreparedStatement statement = conn.prepareStatement(query);
                    statement.setString(1, nom);
                    statement.setString(2, ifu);
                    statement.setString(3, rccm);
                    statement.setString(4, adresse);
                    statement.setString(5, codeApe);
                    statement.setString(6, contact);
                    statement.setString(7, clientId);

                    statement.executeUpdate();

                    // Message de confirmation
                    JOptionPane.showMessageDialog(UpdateClientUI.this,
                            "Client mis à jour avec succès!",
                            "Succès",
                            JOptionPane.INFORMATION_MESSAGE);

                    // Fermer la fenêtre après mise à jour
                    dispose();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(UpdateClientUI.this,
                            "Erreur lors de la mise à jour du client",
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Ajout des composants au panel
        panel.add(id_ClientLabel);
        panel.add(id_ClientField);
        panel.add(nomLabel);
        panel.add(nomField);
        panel.add(ifuLabel);
        panel.add(ifuField);
        panel.add(rccmLabel);
        panel.add(rccmField);
        panel.add(adresseLabel);
        panel.add(adresseField);
        panel.add(codeApeLabel);
        panel.add(codeApeField);
        panel.add(contactLabel);
        panel.add(contactField);
        panel.add(updateButton);

        // Ajout du panel à la fenêtre
        add(panel);
        setVisible(true); // Rendre la fenêtre visible
    }

    // Méthode pour charger les informations actuelles du client
    private void loadClientInfo() {
        try (Connection conn = DBUtil.getConnection()) {
            String query = "SELECT * FROM Client WHERE id_Client = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, clientId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                id_ClientField.setText(resultSet.getString("id_Client"));
                nomField.setText(resultSet.getString("nom"));
                ifuField.setText(resultSet.getString("numero_IFU"));
                rccmField.setText(resultSet.getString("numero_RCCM"));
                adresseField.setText(resultSet.getString("adresse"));
                codeApeField.setText(resultSet.getString("code_APE"));
                contactField.setText(resultSet.getString("contact"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Erreur lors du chargement des informations du client",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
