package gmao;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Classe principale de l'application avec un menu pour accéder aux différentes fonctionnalités
public class la_principale extends JFrame {

    // Constructeur de la classe MainApp
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

        // Ajout des menus à la barre de menu
        menuBar.add(demandeMenu);
        menuBar.add(devisMenu);
        menuBar.add(affectationMenu);

        // Ajout de la barre de menu à la fenêtre
        setJMenuBar(menuBar);
        setVisible(true); // Rendre la fenêtre visible
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
