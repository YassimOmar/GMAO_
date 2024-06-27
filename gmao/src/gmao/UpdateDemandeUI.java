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

public class UpdateDemandeUI extends JFrame {

    private JTextField demandeMaintenaceField;
    private JTextField clientField;
    private JTextField responsableField;
    private JTextField descriptionField;
    private JTextField classificationField;
    private JComboBox<String> statutComboBox;
    private String demandeId;

    public UpdateDemandeUI(String demandeId, String client, String responsable, String description, String classification, String statut) {
        this.demandeId = demandeId;

        setTitle("Modifier Demande de Maintenance");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7, 2));

        JLabel demandeMaintenaceLabel = new JLabel("Numero Demande:");
        demandeMaintenaceField = new JTextField(demandeId);
        demandeMaintenaceField.setEditable(false);

        JLabel clientLabel = new JLabel("Client ID:");
        clientField = new JTextField(client);

        JLabel responsableLabel = new JLabel("Responsable ID:");
        responsableField = new JTextField(responsable);

        JLabel descriptionLabel = new JLabel("Description:");
        descriptionField = new JTextField(description);

        JLabel classificationLabel = new JLabel("Classification:");
        classificationField = new JTextField(classification);

        JLabel statutLabel = new JLabel("Statut:");
        String[] statutOptions = {"créée", "validée", "en cours", "terminée"};
        statutComboBox = new JComboBox<>(statutOptions);
        statutComboBox.setSelectedItem(statut);

        JButton updateButton = new JButton("Mettre à jour");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateDemande();
            }
        });

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
        panel.add(updateButton);

        // Ajout du panel à la fenêtre
        add(panel);
        setVisible(true);
    }

    private void updateDemande() {
        String client = clientField.getText();
        String responsable = responsableField.getText();
        String description = descriptionField.getText();
        String classification = classificationField.getText();
        String statut = (String) statutComboBox.getSelectedItem();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String date = sdf.format(new Date());

        try (Connection conn = DBUtil.getConnection()) {
            String query = "UPDATE DemandeMaintenance SET client_id=?, responsable_id=?, description=?, classification=?, statut=?, date_modification=? WHERE id_DemandeMaintenance=?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, Integer.parseInt(client));
            statement.setInt(2, Integer.parseInt(responsable));
            statement.setString(3, description);
            statement.setString(4, classification);
            statement.setString(5, statut);
            statement.setString(6, date);
            statement.setString(7, demandeId);
            statement.executeUpdate();

            JOptionPane.showMessageDialog(this, "Demande de maintenance mise à jour avec succès!", "Succès", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de la mise à jour de la demande de maintenance", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}
