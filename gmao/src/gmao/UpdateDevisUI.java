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

public class UpdateDevisUI extends JFrame {

    private JTextField devisField;
    private JTextField demandeField;
    private JTextField operateurField;
    private JTextField montantField;
    private JComboBox<String> statutComboBox;
    private String id_Devis;

    public UpdateDevisUI(String id_Devis, String demande_id, String operateur_id, String  montant, String statut) {
        this.id_Devis = id_Devis;

        setTitle("Modifier Devis");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2));

        JLabel DevisLabel = new JLabel("Numero Devis:");
        devisField = new JTextField(id_Devis);
        devisField.setEditable(false);

        JLabel demandeLabel = new JLabel("Numero demande:");
        demandeField = new JTextField(demande_id);

        JLabel operateurLabel = new JLabel("operateur ID:");
        operateurField = new JTextField(operateur_id);

        JLabel montantLabel = new JLabel("montant:");
        montantField = new JTextField(montant);

        //JLabel classificationLabel = new JLabel("Classification:");
        //classificationField = new JTextField(classification);

        JLabel statutLabel = new JLabel("Statut:");
        String[] statutOptions = {"créé", "modifié", "validé"};
        statutComboBox = new JComboBox<>(statutOptions);
        statutComboBox.setSelectedItem(statut);

        JButton updateButton = new JButton("Mettre à jour");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateDevis();
            }
        });

        panel.add(DevisLabel);
        panel.add(devisField);
        panel.add(demandeLabel);
        panel.add(demandeField);
        panel.add(operateurLabel);
        panel.add(operateurField);
        panel.add(montantLabel);
        panel.add(montantField);
       // panel.add(classificationLabel);
       // panel.add(classificationField);
        panel.add(statutLabel);
        panel.add(statutComboBox);
        panel.add(updateButton);

        // Ajout du panel à la fenêtre
        add(panel);
        setVisible(true);
    }

    private void updateDevis() {
        String demande_id = demandeField.getText();
        String operateur_id = operateurField.getText();
        String montant = montantField.getText();
       // String classification = classificationField.getText();
        String statut = (String) statutComboBox.getSelectedItem();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String date = sdf.format(new Date());

        try (Connection conn = DBUtil.getConnection()) {
            String query = "UPDATE Devis SET demande_id=?, operateur_id=?, montant=?,  statut=?, date_modification=? WHERE id_Devis=?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, Integer.parseInt(demande_id));
            statement.setInt(2, Integer.parseInt(operateur_id));
            statement.setInt(3, Integer.parseInt(montant));
            //statement.setString(4, classification);
            statement.setString(4, statut);
            statement.setString(5, date);
            statement.setString(6, id_Devis);
            statement.executeUpdate();

            JOptionPane.showMessageDialog(this, "Devis mis à jour avec succès!", "Succès", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de la mise à jour du devis", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}
