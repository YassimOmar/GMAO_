package gmao;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ClientUI extends JFrame {
	
	private JTextField id_ClientField;
    private JTextField nomField;
    private JTextField ifuField;
    private JTextField rccmField;
    private JTextField adresseField;
    private JTextField codeApeField;
    private JTextField contactField;

    public ClientUI() {
        setTitle("Créer Nouveau Client"); // Titre de la fenêtre
        setSize(400, 300); // Taille de la fenêtre
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Comportement de fermeture

        // Panel pour contenir les composants de la fenêtre
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(8, 2)); // Disposition des composants en grille

        // Champs de texte et étiquettes pour saisir les informations du client
        
        JLabel id_ClientLabel = new JLabel("Identifiant:");
        id_ClientField = new JTextField();
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

        // Bouton pour créer le client
        JButton creerButton = new JButton("Créer");
        creerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	String id_Client = id_ClientField.getText();
            	String nom = nomField.getText();
                String ifu = ifuField.getText();
                String rccm = rccmField.getText();
                String adresse = adresseField.getText();
                String codeApe = codeApeField.getText();
                String contact = contactField.getText();

                // Insertion du client dans la base de données
                try (Connection conn = DBUtil.getConnection()) {
                    String query = "INSERT INTO Client (id_Client,nom, numero_IFU, numero_RCCM, adresse, code_APE, contact) VALUES (?,?, ?, ?, ?, ?, ?)";
                    PreparedStatement statement = conn.prepareStatement(query);
                    statement.setInt(1, Integer.parseInt(id_Client));
                    statement.setString(2, nom);
                    statement.setString(3, ifu);
                    statement.setString(4, rccm);
                    statement.setString(5, adresse);
                    statement.setString(6, codeApe);
                    statement.setString(7, contact);

                    statement.executeUpdate();

                    // Message de confirmation
                    JOptionPane.showMessageDialog(ClientUI.this,
                            "Client créé avec succès!",
                            "Succès",
                            JOptionPane.INFORMATION_MESSAGE);

                    // Réinitialisation des champs après création
                    id_ClientField.setText("");
                    nomField.setText("");
                    ifuField.setText("");
                    rccmField.setText("");
                    adresseField.setText("");
                    codeApeField.setText("");
                    contactField.setText("");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(ClientUI.this,
                            "Erreur lors de la création du client",
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
        panel.add(creerButton);

        // Ajout du panel à la fenêtre
        add(panel);
        setVisible(true); // Rendre la fenêtre visible
    }
}
