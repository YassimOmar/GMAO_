package gmao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;

public class la_principale extends JFrame {

    private DefaultTableModel operateurTableModel;
    private DefaultTableModel demandesTableModel;
    private DefaultTableModel devisTableModel;
    private DefaultTableModel affectationTableModel;
    private JPanel mainPanel;
    private JButton btnModifier;
    private JButton btnSupprimer;

    public la_principale() {
        setTitle("GMAO Application");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JMenuBar menuBar = new JMenuBar();
        
        //menu pour demande de maintenance
        JMenu demandeMenu = new JMenu("Demande de Maintenance");
        JMenuItem nouvelleDemandeMenuItem = new JMenuItem("Nouvelle Demande");
        nouvelleDemandeMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new NouvelleDemandeMaintenanceUI();
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

        //menu pour devis
        JMenu devisMenu = new JMenu("Devis");
        JMenuItem nouveauDevisMenuItem = new JMenuItem("Nouveau Devis");
        nouveauDevisMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new NouveauDevisUI();
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

        //menu pour affectation
        JMenu affectationMenu = new JMenu("Affectation");
        JMenuItem affecterOperateurMenuItem = new JMenuItem("Affecter Opérateur");
        affecterOperateurMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AffectationOperateurUI();
            }
        });
        affectationMenu.add(affecterOperateurMenuItem);
        
        JMenuItem afficherAffectationMenuItem = new JMenuItem("Afficher la liste des affectations");
        afficherAffectationMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadAffectation();
            }
        });
        affectationMenu.add(afficherAffectationMenuItem);
        menuBar.add(affectationMenu);

        //menu operateur
        JMenu operateurMenu = new JMenu("Opérateur");
        JMenuItem ajouterOperateurMenuItem = new JMenuItem("Ajouter Opérateur");
        ajouterOperateurMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new NouvelOperateurUI();
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

        setJMenuBar(menuBar);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        add(mainPanel);

        setVisible(true);
    }

    private void loadOperateur() {
        mainPanel.removeAll();

        operateurTableModel = new DefaultTableModel();
        operateurTableModel.addColumn("ID operateur");
        operateurTableModel.addColumn("Nom");
        operateurTableModel.addColumn("Prénom");
        operateurTableModel.addColumn("Email");

        JTable operateurTable = new JTable(operateurTableModel);
        JScrollPane scrollPane = new JScrollPane(operateurTable);

        try (Connection conn = DBUtil.getConnection()) {
            String query = "SELECT * FROM Utilisateur WHERE role = 'Operateur_de_maintenance'";
            PreparedStatement statement = conn.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

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
                    "Erreur lors du chargement des opérateurs de maintenance",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.revalidate();

        JPanel panelButtons = new JPanel();
        btnModifier = new JButton("Modifier");
        btnSupprimer = new JButton("Supprimer");

        btnModifier.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = operateurTable.getSelectedRow();
                if (selectedRow != -1) {
                	String responsableId = (String) operateurTableModel.getValueAt(selectedRow, 0);
                    new UpdateResponsableMaintenanceUI(responsableId); // Ouvre la fenêtre pour modifier l operateur sélectionné
                } else {
                    JOptionPane.showMessageDialog(la_principale.this,
                            "Veuillez sélectionner un opérateur à modifier.",
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnSupprimer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = operateurTable.getSelectedRow();
                if (selectedRow != -1) {
                    String utilisateurId = (String) operateurTableModel.getValueAt(selectedRow, 0);
                    int confirmation = JOptionPane.showConfirmDialog(la_principale.this,
                            "Êtes-vous sûr de vouloir supprimer cet opérateur?",
                            "Confirmation",
                            JOptionPane.YES_NO_OPTION);
                    if (confirmation == JOptionPane.YES_OPTION) {
                        try (Connection conn = DBUtil.getConnection()) {
                            String query = "DELETE FROM Utilisateur WHERE id_Utilisateur = ?";
                            PreparedStatement statement = conn.prepareStatement(query);
                            statement.setString(1, utilisateurId);
                            statement.executeUpdate();

                            operateurTableModel.removeRow(selectedRow);
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(la_principale.this,
                                    "Erreur lors de la suppression de l'opérateur",
                                    "Erreur",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(la_principale.this,
                            "Veuillez sélectionner un opérateur à supprimer.",
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        panelButtons.add(btnModifier);
        panelButtons.add(btnSupprimer);
        mainPanel.add(panelButtons, BorderLayout.SOUTH);
    }


    private void loadDemandes() {
        mainPanel.removeAll();

        demandesTableModel = new DefaultTableModel();
        demandesTableModel.addColumn("ID");
        demandesTableModel.addColumn("Client ID");
        demandesTableModel.addColumn("Responsable ID");
        demandesTableModel.addColumn("Description");
        demandesTableModel.addColumn("Classification");
        demandesTableModel.addColumn("Statut");
        demandesTableModel.addColumn("Date de création");

        JTable demandesTable = new JTable(demandesTableModel);
        JScrollPane scrollPane = new JScrollPane(demandesTable);

        try (Connection conn = DBUtil.getConnection()) {
            String query = "SELECT * FROM DemandeMaintenance";
            PreparedStatement statement = conn.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

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

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.revalidate();

        JPanel panelButtons = new JPanel();
        btnModifier = new JButton("Modifier");
        btnSupprimer = new JButton("Supprimer");

        btnModifier.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = demandesTable.getSelectedRow();
                if (selectedRow != -1) {
                    String demandeId = (String) demandesTableModel.getValueAt(selectedRow, 0);
                    String clientId = (String) demandesTableModel.getValueAt(selectedRow, 1);
                    String responsableId = (String) demandesTableModel.getValueAt(selectedRow, 2);
                    String description = (String) demandesTableModel.getValueAt(selectedRow, 3);
                    String classification = (String) demandesTableModel.getValueAt(selectedRow, 4);
                    String statut = (String) demandesTableModel.getValueAt(selectedRow, 5);

                    // Ouvre la fenêtre de mise à jour avec les informations récupérées
                    new UpdateDemandeUI(demandeId, clientId, responsableId, description, classification, statut);
                } else {
                    JOptionPane.showMessageDialog(la_principale.this,
                            "Veuillez sélectionner une demande à modifier.",
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnSupprimer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = demandesTable.getSelectedRow();
                if (selectedRow != -1) {
                    String demandeId = (String) demandesTableModel.getValueAt(selectedRow, 0);
                    int confirmation = JOptionPane.showConfirmDialog(la_principale.this,
                            "Êtes-vous sûr de vouloir supprimer cette demande?",
                            "Confirmation",
                            JOptionPane.YES_NO_OPTION);
                    if (confirmation == JOptionPane.YES_OPTION) {
                        try (Connection conn = DBUtil.getConnection()) {
                            String query = "DELETE FROM DemandeMaintenance WHERE id_DemandeMaintenance = ?";
                            PreparedStatement statement = conn.prepareStatement(query);
                            statement.setString(1, demandeId);
                            statement.executeUpdate();

                            demandesTableModel.removeRow(selectedRow);
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(la_principale.this,
                                    "Erreur lors de la suppression de la demande de maintenance",
                                    "Erreur",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(la_principale.this,
                            "Veuillez sélectionner une demande à supprimer.",
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        panelButtons.add(btnModifier);
        panelButtons.add(btnSupprimer);
        mainPanel.add(panelButtons, BorderLayout.SOUTH);
    }

    private void loadDevis() {
        mainPanel.removeAll();

        devisTableModel = new DefaultTableModel();
        devisTableModel.addColumn("ID");
        devisTableModel.addColumn("demande_id");
        devisTableModel.addColumn("operateur_id");
        //devisTableModel.addColumn("Description");
        devisTableModel.addColumn("Montant");
        devisTableModel.addColumn("Date de création");

        JTable devisTable = new JTable(devisTableModel);
        JScrollPane scrollPane = new JScrollPane(devisTable);

        try (Connection conn = DBUtil.getConnection()) {
            String query = "SELECT * FROM Devis";
            PreparedStatement statement = conn.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Vector<String> row = new Vector<>();
                row.add(resultSet.getString("id_Devis"));
                row.add(resultSet.getString("demande_id"));
                row.add(resultSet.getString("operateur_id"));
                //row.add(resultSet.getString("description"));
                row.add(resultSet.getString("montant"));
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

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.revalidate();
        
        JButton btnModifierDevis = new JButton("Modifier Devis");
        AbstractButton btnSupprimerDevis = new JButton("Supprimer Devis");

        btnModifierDevis.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = devisTable.getSelectedRow();
                if (selectedRow != -1) {
                    String devisId = (String) devisTableModel.getValueAt(selectedRow, 0);
                    String demandeId = (String) devisTableModel.getValueAt(selectedRow, 1);
                    String operateurId = (String) devisTableModel.getValueAt(selectedRow, 2);
                    String montant = (String) devisTableModel.getValueAt(selectedRow, 3);
                    String dateCreation = (String) devisTableModel.getValueAt(selectedRow, 4);

                    
                    
                    // Ouvre la fenêtre de mise à jour avec les informations récupérées
                    new UpdateDevisUI(devisId, demandeId, operateurId, montant, dateCreation);
                } else {
                    JOptionPane.showMessageDialog(la_principale.this,
                            "Veuillez sélectionner un devis à modifier.",
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnSupprimerDevis.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = devisTable.getSelectedRow();
                if (selectedRow != -1) {
                    String devisId = (String) devisTableModel.getValueAt(selectedRow, 0);
                    int confirmation = JOptionPane.showConfirmDialog(la_principale.this,
                            "Êtes-vous sûr de vouloir supprimer ce devis?",
                            "Confirmation",
                            JOptionPane.YES_NO_OPTION);
                    if (confirmation == JOptionPane.YES_OPTION) {
                        try (Connection conn = DBUtil.getConnection()) {
                            String query = "DELETE FROM Devis WHERE id_Devis = ?";
                            PreparedStatement statement = conn.prepareStatement(query);
                            statement.setString(1, devisId);
                            statement.executeUpdate();

                            devisTableModel.removeRow(selectedRow);
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(la_principale.this,
                                    "Erreur lors de la suppression du devis",
                                    "Erreur",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(la_principale.this,
                            "Veuillez sélectionner un devis à supprimer.",
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        // Ajout des boutons à un panneau en bas de la fenêtre principale
        JPanel panelDevisButtons = new JPanel();
        panelDevisButtons.add(btnModifierDevis);
        panelDevisButtons.add(btnSupprimerDevis);
        mainPanel.add(panelDevisButtons, BorderLayout.SOUTH);

        setVisible(true);
    }
    
  /*  private void loadAffectation() {
        mainPanel.removeAll();

        affectationTableModel = new DefaultTableModel();
        affectationTableModel.addColumn("ID");
        affectationTableModel.addColumn("demande_id");
        devisTableModel.addColumn("operateur_id");
        affectationTableModel.addColumn("Date de création");

        JTable affectationTable = new JTable(affectationTableModel);
        JScrollPane scrollPane = new JScrollPane(affectationTable);

        try (Connection conn = DBUtil.getConnection()) {
            String query = "SELECT * FROM AffectationOperateur";
            PreparedStatement statement = conn.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Vector<String> row = new Vector<>();
                row.add(resultSet.getString("id_AffectationOperateur"));
                row.add(resultSet.getString("demande_id"));
                row.add(resultSet.getString("operateur_id"));
                row.add(resultSet.getString("date_creation"));
                affectationTableModel.addRow(row);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Erreur lors du chargement de la liste",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.revalidate();
        
        JButton btnModifierAffectation = new JButton("Modifier Affectation");
        AbstractButton btnSupprimerAffectation = new JButton("Supprimer Affectation");

        btnModifierAffectation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = affectationTable.getSelectedRow();
                if (selectedRow != -1) {
                    String id_AffectationOperateur = (String) affectationTableModel.getValueAt(selectedRow, 0);
                    String demandeId = (String) affectationTableModel.getValueAt(selectedRow, 1);
                    String operateurId = (String) affectationTableModel.getValueAt(selectedRow, 2);
                   // String montant = (String) affectationTableModel.getValueAt(selectedRow, 3);
                    String dateCreation = (String) affectationTableModel.getValueAt(selectedRow, 3);

                    
                    
                    // Ouvre la fenêtre de mise à jour avec les informations récupérées
                    new UpdateAffectationUI(id_AffectationOperateur, demandeId, operateurId, dateCreation);
                } else {
                    JOptionPane.showMessageDialog(la_principale.this,
                            "Veuillez sélectionner un devis à modifier.",
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnSupprimerAffectation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = affectationTable.getSelectedRow();
                if (selectedRow != -1) {
                    String id_AffectationOperateur = (String) affectationTableModel.getValueAt(selectedRow, 0);
                    int confirmation = JOptionPane.showConfirmDialog(la_principale.this,
                            "Êtes-vous sûr de vouloir supprimer ce devis?",
                            "Confirmation",
                            JOptionPane.YES_NO_OPTION);
                    if (confirmation == JOptionPane.YES_OPTION) {
                        try (Connection conn = DBUtil.getConnection()) {
                            String query = "DELETE FROM Devis WHERE id_AffectationOperateur = ?";
                            PreparedStatement statement = conn.prepareStatement(query);
                            statement.setString(1, id_AffectationOperateur);
                            statement.executeUpdate();

                            affectationTableModel.removeRow(selectedRow);
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(la_principale.this,
                                    "Erreur lors de la suppression du devis",
                                    "Erreur",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(la_principale.this,
                            "Veuillez sélectionner un devis à supprimer.",
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        // Ajout des boutons à un panneau en bas de la fenêtre principale
        JPanel panelAffectationButtons = new JPanel();
        panelAffectationButtons.add(btnModifierAffectation);
        panelAffectationButtons.add(btnSupprimerAffectation);
        mainPanel.add(panelAffectationButtons, BorderLayout.SOUTH);


        setVisible(true);
    }*/

    private void loadAffectation() {
        mainPanel.removeAll();

        affectationTableModel = new DefaultTableModel();
        affectationTableModel.addColumn("ID");
        affectationTableModel.addColumn("Demande ID");
        affectationTableModel.addColumn("Operateur ID");
        affectationTableModel.addColumn("Date de création");

        JTable affectationTable = new JTable(affectationTableModel);
        JScrollPane scrollPane = new JScrollPane(affectationTable);

        try (Connection conn = DBUtil.getConnection()) {
            String query = "SELECT * FROM AffectationOperateur";
            PreparedStatement statement = conn.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Vector<String> row = new Vector<>();
                row.add(resultSet.getString("id_AffectationOperateur"));
                row.add(resultSet.getString("demande_id"));
                row.add(resultSet.getString("operateur_id"));
                row.add(resultSet.getString("date_affectation"));
                affectationTableModel.addRow(row);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Erreur lors du chargement de la liste des affectations",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.revalidate();

        JButton btnModifierAffectation = new JButton("Modifier Affectation");
        JButton btnSupprimerAffectation = new JButton("Supprimer Affectation");

        btnModifierAffectation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = affectationTable.getSelectedRow();
                if (selectedRow != -1) {
                    String id_AffectationOperateur = (String) affectationTableModel.getValueAt(selectedRow, 0);
                    String demandeId = (String) affectationTableModel.getValueAt(selectedRow, 1);
                    String operateurId = (String) affectationTableModel.getValueAt(selectedRow, 2);
                    String dateCreation = (String) affectationTableModel.getValueAt(selectedRow, 3);

                    new UpdateAffectationUI(id_AffectationOperateur, demandeId, operateurId, dateCreation);
                } else {
                    JOptionPane.showMessageDialog(la_principale.this,
                            "Veuillez sélectionner une affectation à modifier.",
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnSupprimerAffectation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = affectationTable.getSelectedRow();
                if (selectedRow != -1) {
                    String id_AffectationOperateur = (String) affectationTableModel.getValueAt(selectedRow, 0);
                    int confirmation = JOptionPane.showConfirmDialog(la_principale.this,
                            "Êtes-vous sûr de vouloir supprimer cette affectation?",
                            "Confirmation",
                            JOptionPane.YES_NO_OPTION);
                    if (confirmation == JOptionPane.YES_OPTION) {
                        try (Connection conn = DBUtil.getConnection()) {
                            String query = "DELETE FROM AffectationOperateur WHERE id_AffectationOperateur = ?";
                            PreparedStatement statement = conn.prepareStatement(query);
                            statement.setString(1, id_AffectationOperateur);
                            statement.executeUpdate();

                            affectationTableModel.removeRow(selectedRow);
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(la_principale.this,
                                    "Erreur lors de la suppression de l'affectation",
                                    "Erreur",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(la_principale.this,
                            "Veuillez sélectionner une affectation à supprimer.",
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JPanel panelAffectationButtons = new JPanel();
        panelAffectationButtons.add(btnModifierAffectation);
        panelAffectationButtons.add(btnSupprimerAffectation);
        mainPanel.add(panelAffectationButtons, BorderLayout.SOUTH);

        setVisible(true);
    }

}
