package gmao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;

// Classe principale de l'application avec un menu pour accéder aux différentes fonctionnalités
public class la_principale extends JFrame {

	private DefaultTableModel operateurTableModel;
    private DefaultTableModel demandesTableModel;
    private DefaultTableModel devisTableModel;
    private JPanel mainPanel;
    private JButton btnModifier;
    private JButton btnSupprimer;


    // Constructeur de la classe la_principale
    public la_principale() {
        setTitle("GMAO Application"); // Titre de la fenêtre principale
        setSize(800, 600); // Taille de la fenêtre principale
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Comportement de fermeture
        setLocationRelativeTo(null); // Centrer la fenêtre sur l'écran

        // Barre de menu
        JMenuBar menuBar = new JMenuBar();

        // Menu pour les demandes de maintenance
        JMenu demandeMenu = new JMenu("Demande de Maintenance");
        JMenuItem nouvelleDemandeMenuItem = new JMenuItem("Nouvelle Demande");
        nouvelleDemandeMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new NouvelleDemandeMaintenanceUI(); // Ouvre la fenêtre pour créer une nouvelle demande de maintenance
            }
        });
        demandeMenu.add(nouvelleDemandeMenuItem);

        JMenuItem afficherDemandesMenuItem = new JMenuItem("Afficher Demandes");
        afficherDemandesMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadDemandes();
            }
        });
        demandeMenu.add(afficherDemandesMenuItem);
        menuBar.add(demandeMenu);

        // Menu pour les devis
        JMenu devisMenu = new JMenu("Devis");
        JMenuItem nouveauDevisMenuItem = new JMenuItem("Nouveau Devis");
        nouveauDevisMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new NouveauDevisUI(); // Ouvre la fenêtre pour créer un nouveau devis
            }
        });
        devisMenu.add(nouveauDevisMenuItem);

        JMenuItem afficherDevisMenuItem = new JMenuItem("Afficher Devis");
        afficherDevisMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadDevis();
            }
        });
        devisMenu.add(afficherDevisMenuItem);
        menuBar.add(devisMenu);

        // Menu pour l'affectation des opérateurs
        JMenu affectationMenu = new JMenu("Affectation");
        JMenuItem affecterOperateurMenuItem = new JMenuItem("Affecter Opérateur");
        affecterOperateurMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AffectationOperateurUI(); // Ouvre la fenêtre pour affecter un opérateur à une maintenance
            }
        });
        affectationMenu.add(affecterOperateurMenuItem);
        menuBar.add(affectationMenu);

        // Menu pour ajouter un nouvel opérateur
        JMenu operateurMenu = new JMenu("Opérateur");
        JMenuItem ajouterOperateurMenuItem = new JMenuItem("Ajouter Opérateur");
        ajouterOperateurMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new NouvelOperateurUI(); // Ouvre la fenêtre pour ajouter un nouvel opérateur
            }
        });
        operateurMenu.add(ajouterOperateurMenuItem);
        
        JMenuItem afficherOperateurMenuItem = new JMenuItem("Afficher les Opérateurs");
        afficherOperateurMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadOperateur();
            }
        });
        operateurMenu.add(afficherOperateurMenuItem);
        menuBar.add(operateurMenu);

        // Ajout de la barre de menu à la fenêtre
        setJMenuBar(menuBar);

        // Panel principal pour contenir les différentes vues
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        add(mainPanel);

        // Rendre la fenêtre principale visible
        setVisible(true);
    }

    private void loadOperateur() {
        // Nettoyer le contenu du panel principal avant d'ajouter la table
        mainPanel.removeAll();

        // Créer le modèle de tableau pour les responsables de maintenance
        operateurTableModel = new DefaultTableModel();
        operateurTableModel.addColumn("ID operateur");
        operateurTableModel.addColumn("Nom");
        operateurTableModel.addColumn("Prénom");
        operateurTableModel.addColumn("Email");

        // Table pour afficher les Operateurs
        JTable operateurTable = new JTable(operateurTableModel);
        JScrollPane scrollPane = new JScrollPane(operateurTable);
        

        // Charger les données depuis la base de données
        try (Connection conn = DBUtil.getConnection()) {
            String query = "SELECT * FROM Utilisateur WHERE role = 'Operateur'";
            PreparedStatement statement = conn.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            // Ajouter les données au modèle de tableau
            while (resultSet.next()) {
                Vector<String> row = new Vector<>();
                row.add(resultSet.getString("id_Utilisateur"));
                row.add(resultSet.getString("nom"));
                row.add(resultSet.getString("prenom"));
                row.add(resultSet.getString("email"));
                operateurTableModel.addRow(row);
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
    
    // Méthode pour charger les demandes de maintenance depuis la base de données
    private void loadDemandes() {
        // Nettoyer le contenu du panel principal avant d'ajouter la table
        mainPanel.removeAll();

        // Créer le modèle de tableau pour les demandes de maintenance
        demandesTableModel = new DefaultTableModel();
        demandesTableModel.addColumn("ID");
        demandesTableModel.addColumn("Client ID");
        demandesTableModel.addColumn("Responsable ID");
        demandesTableModel.addColumn("Description");
        demandesTableModel.addColumn("Classification");
        demandesTableModel.addColumn("Statut");
        demandesTableModel.addColumn("Date de création");

        // Table pour afficher les demandes de maintenance
        JTable demandesTable = new JTable(demandesTableModel);
        JScrollPane scrollPane = new JScrollPane(demandesTable);

        // Charger les données depuis la base de données
        try (Connection conn = DBUtil.getConnection()) {
            String query = "SELECT * FROM DemandeMaintenance";
            PreparedStatement statement = conn.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            // Ajouter les données au modèle de tableau
            while (resultSet.next()) {
                Vector<String> row = new Vector<>();
                row.add(resultSet.getString("id_DemandeMaintenance"));
                row.add(resultSet.getString("client_id"));
                row.add(resultSet.getString("responsable_id"));
                row.add(resultSet.getString("description"));
                row.add(resultSet.getString("classification"));
                row.add(resultSet.getString("statut"));
                row.add(resultSet.getString("date_creation"));
                demandesTableModel.addRow(row);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Erreur lors du chargement des demandes de maintenance",
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

    // Méthode pour charger les devis depuis la base de données
    private void loadDevis() {
        // Nettoyer le contenu du panel principal avant d'ajouter la table
        mainPanel.removeAll();

        // Créer le modèle de tableau pour les devis
        devisTableModel = new DefaultTableModel();
        devisTableModel.addColumn("ID Devis");
        devisTableModel.addColumn("ID Demande");
        devisTableModel.addColumn("ID Opérateur");
        devisTableModel.addColumn("Montant");
        devisTableModel.addColumn("Statut");
        devisTableModel.addColumn("Date de création");

        // Table pour afficher les devis
        JTable devisTable = new JTable(devisTableModel);
        JScrollPane scrollPane = new JScrollPane(devisTable);

        // Charger les données depuis la base de données
        try (Connection conn = DBUtil.getConnection()) {
            String query = "SELECT * FROM Devis";
            PreparedStatement statement = conn.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            // Ajouter les données au modèle de tableau
            while (resultSet.next()) {
                Vector<String> row = new Vector<>();
                row.add(resultSet.getString("devis_id"));
                row.add(resultSet.getString("demande_id"));
                row.add(resultSet.getString("operateur_id"));
                row.add(resultSet.getString("montant"));
                row.add(resultSet.getString("statut"));
                row.add(resultSet.getString("date_creation"));
                devisTableModel.addRow(row);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Erreur lors du chargement des devis",
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
                new la_principale();
            }
        });
    }
}
