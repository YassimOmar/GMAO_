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

public class UpdateAffectationUI extends JFrame {

    private JTextField id_AffectationOperateurField;
    private JTextField demandeField;
    private JTextField operateurField;
    //private JTextField montantField;
    //private JComboBox<String> statutComboBox;
    //private String id_Devis;
	private String id_AffectationOperateur;

    public UpdateAffectationUI(String id_AffectationOperateur, String demande_id, String operateur_id, String statut) {
        this.id_AffectationOperateur = id_AffectationOperateur;

        setTitle("Modifier Affectation");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2));

        JLabel id_AffectationOperateurLabel = new JLabel("Numero de laffectation:");
        id_AffectationOperateurField = new JTextField(id_AffectationOperateur);
        id_AffectationOperateurField.setEditable(false);

        JLabel demandeLabel = new JLabel("Numero demande:");
        demandeField = new JTextField(demande_id);

        JLabel operateurLabel = new JLabel("operateur ID:");
        operateurField = new JTextField(operateur_id);

    
        JButton updateButton = new JButton("Mettre à jour");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateAffectation();
            }
        });

        panel.add(id_AffectationOperateurLabel);
        panel.add(id_AffectationOperateurField);
        panel.add(demandeLabel);
        panel.add(demandeField);
        panel.add(operateurLabel);
        panel.add(operateurField);
 
        panel.add(updateButton);

        // Ajout du panel à la fenêtre
        add(panel);
        setVisible(true);
    }


    private void updateAffectation() {
        String demande_id = demandeField.getText();
        String operateur_id = operateurField.getText();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String date = sdf.format(new Date());

        try (Connection conn = DBUtil.getConnection()) {
            String query = "UPDATE AffectationOperateur SET demande_id=?, operateur_id=?, date_affectation=? WHERE id_AffectationOperateur=?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, Integer.parseInt(demande_id));
            statement.setInt(2, Integer.parseInt(operateur_id));
            statement.setString(3, date);
            statement.setInt(4, Integer.parseInt(id_AffectationOperateur));

            statement.executeUpdate();

            JOptionPane.showMessageDialog(this, "Modifié avec succès!", "Succès", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de la mise à jour", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

}
