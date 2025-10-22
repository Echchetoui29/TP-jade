# Documentation Complète du Projet JADE - Système de Gestion Intelligente de Bâtiment

## Table des Matières
1. [Vue d'ensemble du projet](#vue-densemble-du-projet)
2. [Architecture générale](#architecture-générale)
3. [Concepts fondamentaux](#concepts-fondamentaux)
4. [Structure du projet](#structure-du-projet)
5. [Détail des agents](#détail-des-agents)
6. [Détail des comportements](#détail-des-comportements)
7. [Flux d'exécution](#flux-dexécution)
8. [Guide d'utilisation](#guide-dutilisation)
9. [Améliorations futures](#améliorations-futures)

---

## Vue d'ensemble du projet

### Objectif Principal
Ce projet est un **système multi-agents intelligent** basé sur le framework **JADE (Java Agent DEvelopment Framework)** conçu pour gérer et monitorer les ressources d'un bâtiment intelligent (smart building).

### Fonctionnalités Principales
- **Surveillance de la température** : Mesure continue de la température ambiante
- **Détection de mouvement** : Identification de la présence de personnes
- **Comptage de personnes** : Suivi du nombre de personnes présentes dans le bâtiment
- **Analyse énergétique** : Monitoring de la consommation d'énergie avec recommandations

### Technologies Utilisées
- **JADE Framework** : Plateforme multi-agents distribuée
- **Java** : Langage de programmation principal
- **Architecture orientée agents** : Chaque fonction est gérée par un agent autonome

---

## Architecture générale

### Modèle Multi-Agents
Le système fonctionne selon un modèle **multi-agents décentralisé** où chaque agent est responsable d'une tâche spécifique :

\`\`\`
┌─────────────────────────────────────────────────────────┐
│                    JADE Runtime                         │
│  (Conteneur principal - Main Container)                 │
├─────────────────────────────────────────────────────────┤
│                                                         │
│  ┌──────────────────┐  ┌──────────────────┐            │
│  │ CapteurTemp      │  │ CapteurMouv      │            │
│  │ (Température)    │  │ (Mouvement)      │            │
│  └──────────────────┘  └──────────────────┘            │
│                                                         │
│  ┌──────────────────┐  ┌──────────────────┐            │
│  │ CompteurPers     │  │ AnalyseurEner    │            │
│  │ (Personnes)      │  │ (Énergie)        │            │
│  └──────────────────┘  └──────────────────┘            │
│                                                         │
└─────────────────────────────────────────────────────────┘
\`\`\`

### Principes de Conception
1. **Autonomie** : Chaque agent fonctionne indépendamment
2. **Réactivité** : Les agents réagissent aux événements
3. **Proactivité** : Les agents exécutent des tâches planifiées
4. **Scalabilité** : Facile d'ajouter de nouveaux agents

---

## Concepts fondamentaux

### Qu'est-ce qu'un Agent JADE ?
Un agent JADE est une entité logicielle autonome qui :
- Possède un cycle de vie (création, exécution, destruction)
- Peut communiquer avec d'autres agents
- Exécute des comportements (behaviors)
- Fonctionne dans un conteneur JADE

### Comportements (Behaviors)
Les comportements définissent **ce que fait un agent**. Il existe plusieurs types :

#### 1. **SimpleBehaviour**
- Exécute une action une seule fois
- Utilisé pour les tâches ponctuelles
- Exemple : Analyse d'énergie

#### 2. **TickerBehaviour**
- Exécute une action **périodiquement** à intervalle régulier
- Idéal pour le monitoring continu
- Exemple : Mesure de température (toutes les 5 secondes)

#### 3. **CyclicBehaviour**
- Exécute une action en boucle infinie
- Utilisé pour l'écoute de messages

### Cycle de Vie d'un Agent
\`\`\`
1. setup()      → Initialisation de l'agent
2. action()     → Exécution du comportement
3. takeDown()   → Nettoyage et arrêt
   \`\`\`

---

## Structure du projet

### Arborescence des fichiers
\`\`\`
src/
├── Main.java                          # Point d'entrée du système
├── agents/
│   ├── CapteurTemperatureAgent.java   # Agent de mesure de température
│   ├── CapteurMouvementAgent.java     # Agent de détection de mouvement
│   ├── CompteurPersonnesAgent.java    # Agent de comptage de personnes
│   └── AnalyseurEnergieAgent.java     # Agent d'analyse énergétique
└── behaviours/
├── MesureTemperatureBehaviour.java
├── DetectionMouvementBehaviour.java
├── ComptagePersonnesBehaviour.java
└── AnalyseEnergieBehaviour.java
\`\`\`

---

## Détail des agents

### 1. CapteurTemperatureAgent

#### Objectif
Mesurer la température ambiante du bâtiment à intervalles réguliers.

#### Code Source Commenté
\`\`\`java
package agents;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import java.text.DecimalFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class CapteurTemperatureAgent extends Agent {
// Variables d'instance
private int nombreMesures = 0;              // Compteur de mesures effectuées
private final int MAX_MESURES = 20;         // Limite de mesures avant arrêt
private static final DecimalFormat df = new DecimalFormat("0.0");  // Format pour affichage
private static final DateTimeFormatter tf = DateTimeFormatter.ofPattern("HH:mm:ss");  // Format heure

    // Méthode d'initialisation appelée au démarrage de l'agent
    protected void setup() {
        System.out.println("Agent " + getLocalName() + " démarré.");
        
        // Ajoute un comportement qui s'exécute toutes les 5000ms (5 secondes)
        addBehaviour(new MesureTemperatureBehaviour(this, 5000));
    }

    // Méthode appelée à l'arrêt de l'agent
    protected void takeDown() {
        System.out.println("Agent " + getLocalName() + " terminé. Total mesures: " + nombreMesures);
    }

    // Classe interne définissant le comportement périodique
    private class MesureTemperatureBehaviour extends TickerBehaviour {
        // Constructeur : prend l'agent et la période en millisecondes
        public MesureTemperatureBehaviour(Agent a, long period) {
            super(a, period);
        }

        // Méthode appelée à chaque tick (toutes les 5 secondes)
        protected void onTick() {
            // Génère une température aléatoire entre 15°C et 30°C
            double temperature = 15.0 + Math.random() * 15.0;

            // Récupère l'heure actuelle au format HH:mm:ss
            String heure = LocalTime.now().format(tf);
            
            // Affiche la mesure
            System.out.println("[CapteurTemp] " + heure + " - Température: " + df.format(temperature) + "°C");

            // Alerte si température > 28°C
            if (temperature > 28.0) {
                System.out.println(">>> ALERTE CapteurTemp: Température > 28°C détectée.");
            }

            // Incrémente le compteur
            nombreMesures++;

            // Arrête l'agent après 20 mesures
            if (nombreMesures >= MAX_MESURES) {
                System.out.println("CapteurTemp: Nombre maximum de mesures (" + MAX_MESURES + ") atteint.");
                myAgent.doDelete();  // Supprime l'agent
            }
        }
    }
}
\`\`\`

#### Explication Détaillée
- **Période** : 5 secondes entre chaque mesure
- **Plage de température** : 15°C à 30°C (simulation)
- **Alerte** : Déclenche une alerte si T > 28°C
- **Arrêt** : L'agent s'arrête après 20 mesures

---

### 2. CapteurMouvementAgent

#### Objectif
Détecter la présence de mouvement dans le bâtiment.

#### Code Source Commenté
\`\`\`java
package agents;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class CapteurMouvementAgent extends Agent {
// Compteur de détections de mouvement
private int nombreDetections = 0;

    // Format pour afficher l'heure
    private static final DateTimeFormatter tf = DateTimeFormatter.ofPattern("HH:mm:ss");

    // Initialisation de l'agent
    protected void setup() {
        System.out.println("Agent " + getLocalName() + " démarré.");
        
        // Ajoute un comportement qui s'exécute toutes les 3000ms (3 secondes)
        addBehaviour(new DetectionMouvementBehaviour(this, 3000));
    }

    // Arrêt de l'agent
    protected void takeDown() {
        System.out.println("Agent " + getLocalName() + " terminé. Total détections: " + nombreDetections);
    }

    // Méthode de simulation de détection de mouvement
    private boolean detecterMouvement() {
        // Retourne true avec 40% de probabilité
        return Math.random() < 0.4;
    }

    // Classe interne pour le comportement de détection
    private class DetectionMouvementBehaviour extends TickerBehaviour {
        public DetectionMouvementBehaviour(Agent a, long period) {
            super(a, period);
        }

        // Exécuté toutes les 3 secondes
        protected void onTick() {
            String heure = LocalTime.now().format(tf);

            // Vérifie s'il y a mouvement
            if (detecterMouvement()) {
                nombreDetections++;
                System.out.println("[CapteurMouv] " + heure + " - MOUVEMENT DÉTECTÉ ! (Total: " + nombreDetections + ")");
            } else {
                System.out.println("[CapteurMouv] " + heure + " - Aucun mouvement");
            }
        }
    }
}
\`\`\`

#### Explication Détaillée
- **Période** : 3 secondes entre chaque vérification
- **Probabilité** : 40% de chance de détecter un mouvement
- **Comptage** : Enregistre le nombre total de détections
- **Affichage** : Indique clairement quand un mouvement est détecté

---

### 3. CompteurPersonnesAgent

#### Objectif
Compter le nombre de personnes présentes dans le bâtiment (entrées/sorties).

#### Code Source Commenté
\`\`\`java
package agents;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class CompteurPersonnesAgent extends Agent {
// Variables de suivi
private int personnesPresentes = 0;    // Nombre actuel de personnes
private int totalEntrees = 0;          // Total des entrées
private int totalSorties = 0;          // Total des sorties
private int operations = 0;            // Compteur d'opérations

    private static final DateTimeFormatter tf = DateTimeFormatter.ofPattern("HH:mm:ss");

    // Initialisation
    protected void setup() {
        System.out.println("Agent " + getLocalName() + " démarré.");
        
        // Ajoute un comportement qui s'exécute toutes les 4000ms (4 secondes)
        addBehaviour(new ComptagePersonnesBehaviour(this, 4000));
    }

    // Arrêt
    protected void takeDown() {
        System.out.println("Agent " + getLocalName() + " terminé.");
        afficherStatistiques();  // Affiche les stats finales
    }

    // Affiche les statistiques de comptage
    private void afficherStatistiques() {
        System.out.println("\n=== STATISTIQUES (" + getLocalName() + ") ===");
        System.out.println("Personnes actuelles: " + personnesPresentes);
        System.out.println("Total entrées: " + totalEntrees);
        System.out.println("Total sorties: " + totalSorties);
        System.out.println("===========================\n");
    }

    // Classe interne pour le comportement de comptage
    private class ComptagePersonnesBehaviour extends TickerBehaviour {
        public ComptagePersonnesBehaviour(Agent a, long period) {
            super(a, period);
        }

        // Exécuté toutes les 4 secondes
        protected void onTick() {
            String heure = LocalTime.now().format(tf);
            
            // Génère aléatoirement une entrée (+1) ou une sortie (-1) avec 50/50
            int changement = (Math.random() < 0.5) ? 1 : -1;

            if (changement == 1) {
                // Entrée
                personnesPresentes++;
                totalEntrees++;
                System.out.println("[CompteurPersonnes] " + heure + " - Personnes présentes: " + personnesPresentes + " (+1 entrée)");
            } else {
                // Sortie (avec vérification que le nombre ne devient pas négatif)
                if (personnesPresentes > 0) {
                    personnesPresentes--;
                    totalSorties++;
                    System.out.println("[CompteurPersonnes] " + heure + " - Personnes présentes: " + personnesPresentes + " (-1 sortie)");
                } else {
                    // Impossible de sortir si personne n'est présent
                    System.out.println("[CompteurPersonnes] " + heure + " - Personnes présentes: 0 (Tentative de sortie ignorée)");
                }
            }

            operations++;

            // Affiche les statistiques tous les 10 opérations
            if (operations % 10 == 0) {
                afficherStatistiques();
            }
        }
    }
}
\`\`\`

#### Explication Détaillée
- **Période** : 4 secondes entre chaque opération
- **Logique** : 50% entrée, 50% sortie
- **Sécurité** : Empêche le nombre de personnes de devenir négatif
- **Statistiques** : Affiche un résumé tous les 10 opérations

---

### 4. AnalyseurEnergieAgent

#### Objectif
Analyser la consommation d'énergie et fournir des recommandations.

#### Code Source Commenté
\`\`\`java
package agents;

import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import java.util.ArrayList;
import java.util.List;
import java.text.DecimalFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class AnalyseurEnergieAgent extends Agent {
// Historique des consommations (pour calculer la moyenne mobile)
private List<Double> historique = new ArrayList<>();

    // Taille de la fenêtre glissante pour la moyenne
    private final int TAILLE_HISTORIQUE = 5;
    
    // Compteur d'analyses
    private int nombreAnalyses = 0;
    
    // Nombre maximum d'analyses avant arrêt
    private final int MAX_ANALYSES = 15;
    
    // Formatage des nombres (1 décimale)
    private static final DecimalFormat df = new DecimalFormat("0.0");
    
    // Format de l'heure
    private static final DateTimeFormatter tf = DateTimeFormatter.ofPattern("HH:mm:ss");

    // Initialisation
    protected void setup() {
        System.out.println("Agent " + getLocalName() + " démarré.");
        
        // Ajoute un comportement simple (non périodique)
        addBehaviour(new AnalyseEnergieBehaviour(this));
    }

    // Arrêt
    protected void takeDown() {
        System.out.println("Agent " + getLocalName() + " terminé. Total analyses: " + nombreAnalyses);
    }

    // Calcule la moyenne des consommations dans l'historique
    private double calculerMoyenne() {
        if (historique.isEmpty()) {
            return 0.0;  // Retourne 0 si l'historique est vide
        }

        double sum = 0;
        for (double cons : historique) {
            sum += cons;  // Additionne toutes les consommations
        }
        return sum / historique.size();  // Divise par le nombre d'éléments
    }

    // Suggère des économies si la consommation est trop élevée
    private void suggererEconomies(double consommation) {
        if (consommation > 150) {
            System.out.println("[AnalyseurEnergie] RECOMMANDATION: Réduire la consommation (Actuel: " + df.format(consommation) + " kW)");
        }
    }

    // Classe interne pour le comportement d'analyse
    private class AnalyseEnergieBehaviour extends SimpleBehaviour {
        private boolean isFinished = false;

        public AnalyseEnergieBehaviour(Agent a) {
            super(a);
        }

        // Exécuté une fois à chaque cycle
        @Override
        public void action() {
            // Génère une consommation aléatoire entre 50 et 200 kW
            double consommation = 50.0 + Math.random() * 150.0;

            // Ajoute la consommation à l'historique
            historique.add(consommation);
            
            // Garde seulement les 5 dernières mesures (fenêtre glissante)
            if (historique.size() > TAILLE_HISTORIQUE) {
                historique.remove(0);  // Supprime la plus ancienne
            }

            // Calcule la moyenne mobile
            double moyenne = calculerMoyenne();

            // Affiche les résultats
            String heure = LocalTime.now().format(tf);
            System.out.println("[AnalyseurEnergie] " + heure +
                    " - Conso: " + df.format(consommation) + " kW. " +
                    "Moyenne mobile (" + TAILLE_HISTORIQUE + "): " + df.format(moyenne) + " kW.");

            // Suggère des économies si nécessaire
            suggererEconomies(consommation);

            // Incrémente le compteur
            nombreAnalyses++;
            
            // Arrête après 15 analyses
            if (nombreAnalyses >= MAX_ANALYSES) {
                isFinished = true;
                myAgent.doDelete();  // Supprime l'agent
            } else {
                block(7000);  // Pause 7 secondes avant la prochaine analyse
            }
        }

        @Override
        public boolean done() {
            return isFinished;
        }
    }
}
\`\`\`

#### Explication Détaillée
- **Historique** : Garde les 5 dernières mesures
- **Moyenne mobile** : Calcule la moyenne des 5 dernières consommations
- **Alerte** : Recommande une réduction si consommation > 150 kW
- **Période** : 7 secondes entre chaque analyse
- **Arrêt** : L'agent s'arrête après 15 analyses

---

## Détail des comportements

### Comportements Implémentés

#### 1. MesureTemperatureBehaviour (TickerBehaviour)
- **Type** : Périodique
- **Période** : 5 secondes
- **Action** : Mesure la température et génère une alerte si > 28°C
- **Arrêt** : Après 20 mesures

#### 2. DetectionMouvementBehaviour (TickerBehaviour)
- **Type** : Périodique
- **Période** : 3 secondes
- **Action** : Détecte le mouvement avec 40% de probabilité
- **Arrêt** : Continu (pas d'arrêt automatique)

#### 3. ComptagePersonnesBehaviour (TickerBehaviour)
- **Type** : Périodique
- **Période** : 4 secondes
- **Action** : Simule entrées/sorties de personnes
- **Arrêt** : Continu (pas d'arrêt automatique)

#### 4. AnalyseEnergieBehaviour (SimpleBehaviour)
- **Type** : Non périodique (mais avec pause)
- **Période** : 7 secondes (via block())
- **Action** : Analyse la consommation d'énergie
- **Arrêt** : Après 15 analyses

---

## Flux d'exécution

### Démarrage du Système

\`\`\`
1. Exécution de Main.java
   ↓
2. Création du Runtime JADE
   ↓
3. Configuration du profil (localhost, GUI activée)
   ↓
4. Création du conteneur principal
   ↓
5. Création et démarrage des 4 agents :
    - CapteurTemperatureAgent (CapteurTemp)
    - CapteurMouvementAgent (CapteurMouv)
    - CompteurPersonnesAgent (CompteurPers)
    - AnalyseurEnergieAgent (AnalyseurEner)
      ↓
6. Chaque agent exécute sa méthode setup()
   ↓
7. Chaque agent ajoute son comportement
   ↓
8. Les comportements s'exécutent en parallèle
   \`\`\`

### Exécution Parallèle
Tous les agents fonctionnent **simultanément** :
- CapteurTemp : Mesure toutes les 5 secondes
- CapteurMouv : Détecte toutes les 3 secondes
- CompteurPers : Compte toutes les 4 secondes
- AnalyseurEner : Analyse toutes les 7 secondes

### Arrêt du Système
Chaque agent s'arrête selon ses critères :
- CapteurTemp : Après 20 mesures
- CapteurMouv : Continu (manuel)
- CompteurPers : Continu (manuel)
- AnalyseurEner : Après 15 analyses

---

## Guide d'utilisation

### Prérequis
- Java JDK 8 ou supérieur
- JADE Framework (jade.jar)
- IDE Java (Eclipse, IntelliJ, etc.)

### Installation
1. Télécharger JADE depuis http://jade.tilab.com/
2. Ajouter jade.jar au classpath du projet
3. Compiler les fichiers Java
4. Exécuter Main.java

### Exécution
\`\`\`bash
java -cp .:jade.jar src.Main
\`\`\`

### Interface JADE
Une interface graphique (RMA - Remote Management Agent) s'ouvre automatiquement permettant de :
- Visualiser les agents actifs
- Monitorer les messages
- Arrêter les agents manuellement

### Interprétation des Logs
\`\`\`
[CapteurTemp] 14:30:45 - Température: 22.5°C
[CapteurMouv] 14:30:46 - MOUVEMENT DÉTECTÉ ! (Total: 3)
[CompteurPersonnes] 14:30:47 - Personnes présentes: 5 (+1 entrée)
[AnalyseurEnergie] 14:30:48 - Conso: 125.3 kW. Moyenne mobile (5): 118.7 kW.
\`\`\`

---

## Améliorations futures

### Court terme
1. **Communication inter-agents** : Implémenter l'échange de messages ACL
2. **Persistance des données** : Sauvegarder les mesures dans une base de données
3. **Interface web** : Créer un dashboard pour visualiser les données en temps réel

### Moyen terme
1. **Machine Learning** : Prédire la consommation d'énergie
2. **Alertes intelligentes** : Notifications basées sur des seuils adaptatifs
3. **Optimisation énergétique** : Recommandations automatiques d'économies

### Long terme
1. **Intégration IoT** : Connecter des capteurs réels
2. **Système de contrôle** : Automatiser l'ajustement du chauffage/climatisation
3. **Analyse prédictive** : Anticiper les problèmes avant qu'ils ne surviennent
4. **Multi-bâtiments** : Gérer plusieurs bâtiments avec un système centralisé

---

## Conclusion

Ce projet démontre les capacités du framework JADE pour créer un système multi-agents distribué et autonome. Chaque agent fonctionne indépendamment tout en contribuant à l'objectif global de gestion intelligente du bâtiment.

### Points clés
- **Modularité** : Facile d'ajouter ou de modifier des agents
- **Scalabilité** : Peut gérer de nombreux agents simultanément
- **Autonomie** : Les agents prennent des décisions indépendantes
- **Réactivité** : Réagit rapidement aux changements

### Ressources
- Documentation JADE : http://jade.tilab.com/doc/
- Tutoriels : http://jade.tilab.com/doc/tutorials/
- Forum : http://jade.tilab.com/community/

---

**Auteur** : Système de Gestion Intelligente de Bâtiment  
**Date** : 2025  
**Version** : 1.0
