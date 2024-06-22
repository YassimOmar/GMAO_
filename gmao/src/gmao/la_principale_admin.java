package gmao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;

public class la_principale_admin extends JFrame {

    private DefaultTableModel clientsTableModel;
    private DefaultTableModel responsablesTableModel;
    private JPanel mainPanel;
    private JButton btnModifier;
    private JButton btnSupprimer;

    public la_principale_admin() {
        setTitle("GMAO Application (Admin)"); // Titre de la fenêtre principale
        setSize(800, 600); // Taille de la fenêtre principale
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Comportement de fermeture
        setLocationRelativeTo(null); // Centrer la fenêtre sur l'écran

        // Barre de menu
        JMenuBar menuBar = new JMenuBar();

        // Menu pour les clients
        JMenu clientMenu = new JMenu("Clients");
        JMenuItem creerClientMenuItem = new JMenuItem("Créer Client");
        creerClientMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ClientUI(); // Ouvre la fenêtre pour créer un nouveau client
            }
        });
        clientMenu.add(creerClientMenuItem);

        JMenuItem afficherClientsMenuItem = new JMenuItem("Afficher Clients");
        afficherClientsMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadClients(); // Charge les clients depuis la base de données
            }
        });
        clientMenu.add(afficherClientsMenuItem);
        menuBar.add(clientMenu);

        // Menu pour les responsables de maintenance
        JMenu responsableMenu = new JMenu("Responsables de Maintenance");
        JMenuItem creerResponsableMenuItem = new JMenuItem("Créer Responsable");
        creerResponsableMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new NouveauResponsableMaintenanceUI(); // Ouvre la fenêtre pour créer un responsable de maintenance
            }
        });
        responsableMenu.add(creerResponsableMenuItem);

        JMenuItem afficherResponsablesMenuItem = new JMenuItem("Afficher Responsables");
        afficherResponsablesMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadResponsables(); // Charge les responsables de maintenance depuis la base de données
            }
        });
        responsableMenu.add(afficherResponsablesMenuItem);
        menuBar.add(responsableMenu);

        // Ajout de la barre de menu à la fenêtre
        setJMenuBar(menuBar);

        // Panel principal pour contenir les différentes vues
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        add(mainPanel);

        // Rendre la fenêtre principale visible
        setVisible(true);
    }

    // Méthode pour charger les clients depuis la base de données
    private void loadClients() {
        // Nettoyer le contenu du panel principal avant d'ajouter la table
        mainPanel.removeAll();

        // Créer le modèle de tableau pour les clients
        clientsTableModel = new DefaultTableModel();
        clientsTableModel.addColumn("ID Client");
        clientsTableModel.addColumn("Nom");
        clientsTableModel.addColumn("Numéro IFU");
        clientsTableModel.addColumn("Numéro RCCM");
        clientsTableModel.addColumn("Adresse");
        clientsTableModel.addColumn("Code APE");
        clientsTableModel.addColumn("Contact");

        // Table pour afficher les clients
        JTable clientsTable = new JTable(clientsTableModel);
        JScrollPane scrollPane = new JScrollPane(clientsTable);

        // Charger les données depuis la base de données
        try (Connection conn = DBUtil.getConnection()) {
            String query = "SELECT * FROM Client";
            PreparedStatement statement = conn.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            // Ajouter les données au modèle de tableau
            while (resultSet.next()) {
                Vector<String> row = new Vector<>();
                row.add(resultSet.getString("id_Client"));
                row.add(resultSet.getString("nom"));
                row.add(resultSet.getString("numero_IFU"));
                row.add(resultSet.getString("numero_RCCM"));
                row.add(resultSet.getString("adresse"));
                row.add(resultSet.getString("code_APE"));
                row.add(resultSet.getString("contact"));
                clientsTableModel.addRow(row);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Erreur lors du chargement des clients",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }

        // Ajouter la table au panel principal
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.revalidate(); // Rafraîchir l'affichage
        
        //ajouter ls boutons supprimer et modifier
        JPanel panelButtons = new JPanel();
        btnModifier = new JButton("Modifier");
        btnSupprimer = new JButton("Supprimer");
        panelButtons.add(btnModifier);
        panelButtons.add(btnSupprimer);
        mainPanel.add(panelButtons, BorderLayout.SOUTH);
    }
    
    //Methode pour supprimer un client
    //****************************

    // Méthode pour charger les responsables de maintenance depuis la base de données
    private void loadResponsables() {
        // Nettoyer le contenu du panel principal avant d'ajouter la table
        mainPanel.removeAll();

        // Créer le modèle de tableau pour les responsables de maintenance
        responsablesTableModel = new DefaultTableModel();
        responsablesTableModel.addColumn("ID Responsable");
        responsablesTableModel.addColumn("Nom");
        responsablesTableModel.addColumn("Prénom");
        responsablesTableModel.addColumn("Email");

        // Table pour afficher les responsables de maintenance
        JTable responsablesTable = new JTable(responsablesTableModel);
        JScrollPane scrollPane = new JScrollPane(responsablesTable);

        // Charger les données depuis la base de données
        try (Connection conn = DBUtil.getConnection()) {
            String query = "SELECT * FROM Utilisateur WHERE role = 'responsable_de_maintenance'";
            PreparedStatement statement = conn.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            // Ajouter les données au modèle de tableau
            while (resultSet.next()) {
                Vector<String> row = new Vector<>();
                row.add(resultSet.getString("id_Utilisateur"));
                row.add(resultSet.getString("nom"));
                row.add(resultSet.getString("prenom"));
                row.add(resultSet.getString("email"));
                responsablesTableModel.addRow(row);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Erreur lors du chargement des responsables de maintenance",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }

        // Ajouter la table au panel principal
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.revalidate(); // Rafraîchir l'affichage
        
      //ajouter ls boutons supprimer et modifier
        JPanel panelButtons = new JPanel();
        btnModifier = new JButton("Modifier");
        btnSupprimer = new JButton("Supprimer");
        panelButtons.add(btnModifier);
        panelButtons.add(btnSupprimer);
        mainPanel.add(panelButtons, BorderLayout.SOUTH);
    }

    // Méthode principale pour lancer l'application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new la_principale_admin();
            }
        });
    }
}
