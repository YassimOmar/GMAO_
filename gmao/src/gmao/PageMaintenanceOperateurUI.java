package gmao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PageMaintenanceOperateurUI extends JFrame {

    private JTable table;
    private JScrollPane scrollPane;
    private JButton saveButton; // Bouton Enregistrer
    private String username; // Nom d'utilisateur
    private String operateurNom; // Nom de l'opérateur
    
    
    public PageMaintenanceOperateurUI() {
        setTitle("Liste des Maintenances");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //creation de barre de menu 
        JMenuBar menuBar = new JMenuBar();
        
     // Menu pour la déconnexion
        JMenuItem deconnexionMenuItem = new JMenuItem("Déconnexion");
        deconnexionMenuItem.setHorizontalAlignment(SwingConstants.CENTER);
        deconnexionMenuItem.setFont(new Font("Segoe UI", Font.BOLD | Font.ITALIC, 12));
        deconnexionMenuItem.setBackground(Color.RED);
        deconnexionMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(PageMaintenanceOperateurUI.this,
                        "Êtes-vous sûr de vouloir vous déconnecter?",
                        "Confirmation de déconnexion",
                        JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    dispose(); // Fermer la fenêtre principale
                    new AuthentificationUI(); // Retourner à la fenêtre d'authentification
                }
            }
        });
        menuBar.add(deconnexionMenuItem);
        // Ajout de la barre de menu à la fenêtre
        setJMenuBar(menuBar);
        
        
        // Création d'un tableau vide avec un modèle par défaut
        table = new JTable();
        scrollPane = new JScrollPane(table);

        // Ajout du JScrollPane à la fenêtre
        getContentPane().add(scrollPane);

        // Charger les données depuis la base de données
        loadMaintenancesAndDevis();

        // Ajout du bouton Enregistrer
        saveButton = new JButton("Enregistrer");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveModifications();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH); // Ajout du panel avec le bouton en bas de la fenêtre

        setLocationRelativeTo(null); // Centrer la fenêtre sur l'écran
        setVisible(true); // Rendre la fenêtre visible
    }

    private void loadMaintenancesAndDevis() {
        try (Connection conn = DBUtil.getConnection()) {
            String query = "SELECT dm.id_DemandeMaintenance, dm.description, dm.classification, dm.statut, d.id_Devis, d.montant, d.statut, u.nom as operateur_nom " +
                           "FROM DemandeMaintenance dm " +
                           "LEFT JOIN Devis d ON dm.id_DemandeMaintenance = d.demande_id " +
                           "LEFT JOIN Utilisateur u ON d.operateur_id = u.id_Utilisateur";
            PreparedStatement statement = conn.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            DefaultTableModel model = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return column != 0; // permettre toute modification sauf l'ID
                }
            };

            model.addColumn("ID Demande");
            model.addColumn("Description");
            model.addColumn("Classification");
            model.addColumn("Statut Demande");
            model.addColumn("ID Devis");
            model.addColumn("Montant");
            model.addColumn("Statut Devis");
            model.addColumn("Opérateur");

            while (resultSet.next()) {
                Object[] row = new Object[8];
                row[0] = resultSet.getInt("id_DemandeMaintenance");
                row[1] = resultSet.getString("description");
                row[2] = resultSet.getString("classification");
                row[3] = resultSet.getString("statut");
                row[4] = resultSet.getInt("id_Devis");
                row[5] = resultSet.getBigDecimal("montant");
                row[6] = resultSet.getString("statut");
                row[7] = resultSet.getString("operateur_nom");

                model.addRow(row);
            }

            table.setModel(model);

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Erreur lors du chargement des données de maintenance et de devis",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveModifications() {
        try (Connection conn = DBUtil.getConnection()) {
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            int rowCount = model.getRowCount();

            // Parcourir chaque ligne modifiée pour sauvegarder dans la base de données
            for (int i = 0; i < rowCount; i++) {
                int demandeId = (int) model.getValueAt(i, 0);  // ID Demande
                String description = (String) model.getValueAt(i, 1);
                String classification = (String) model.getValueAt(i, 2);
                String statutDemande = (String) model.getValueAt(i, 3);
                int devisId = (int) model.getValueAt(i, 4);
                BigDecimal montant = (BigDecimal) model.getValueAt(i, 5);  // Utilisation de BigDecimal
                String statutDevis = (String) model.getValueAt(i, 6);

                // Exemple de requête de mise à jour pour la demande de maintenance
                String updateDemandeQuery = "UPDATE DemandeMaintenance SET description = ?, classification = ?, statut = ? WHERE id_DemandeMaintenance = ?";
                PreparedStatement updateDemandeStatement = conn.prepareStatement(updateDemandeQuery);
                updateDemandeStatement.setString(1, description);
                updateDemandeStatement.setString(2, classification);
                updateDemandeStatement.setString(3, statutDemande);
                updateDemandeStatement.setInt(4, demandeId);
                updateDemandeStatement.executeUpdate();

                // Exemple de requête de mise à jour pour le devis
                String updateDevisQuery = "UPDATE Devis SET montant = ?, statut = ? WHERE id_Devis = ?";
                PreparedStatement updateDevisStatement = conn.prepareStatement(updateDevisQuery);
                updateDevisStatement.setBigDecimal(1, montant);
                updateDevisStatement.setString(2, statutDevis);
                updateDevisStatement.setInt(3, devisId);
                updateDevisStatement.executeUpdate();
            }

            JOptionPane.showMessageDialog(this,
                    "Modifications enregistrées avec succès!",
                    "Enregistrement",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Erreur lors de l'enregistrement des modifications",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PageMaintenanceOperateurUI());
    }
}
