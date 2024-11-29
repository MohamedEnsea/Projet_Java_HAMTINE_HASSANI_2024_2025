import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class Main {

    JFrame displayZoneFrame;
    RenderEngine renderEngine;
    GameEngine gameEngine;
    PhysicEngine physicEngine;

    public Main() throws Exception {
        displayZoneFrame = new JFrame("Java Labs");
        displayZoneFrame.setSize(400, 600);
        displayZoneFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Afficher l'écran titre
        TitleScreen titleScreen = new TitleScreen(this);
        displayZoneFrame.getContentPane().add(titleScreen);
        displayZoneFrame.setVisible(true);
    }

    // Définir la méthode startGame() en dehors du constructeur
    public void startGame() {
        try {
            // Création du héros
            DynamicSprite hero = new DynamicSprite(200, 300,
                    ImageIO.read(new File("./img/heroTileSheetLowRes.png")), 48, 50);

            // Création de RenderEngine et assignation du héros
            renderEngine = new RenderEngine(displayZoneFrame);  // Créer le RenderEngine
            renderEngine.setHero(hero);  // Assigner le héros à RenderEngine

            physicEngine = new PhysicEngine();
            gameEngine = new GameEngine(hero);

            // Création des timers pour la mise à jour
            Timer renderTimer = new Timer(50, (time) -> renderEngine.update());
            Timer gameTimer = new Timer(50, (time) -> gameEngine.update());
            Timer physicTimer = new Timer(50, (time) -> physicEngine.update());

            // Démarrer les timers
            renderTimer.start();
            gameTimer.start();
            physicTimer.start();

            // Ajouter RenderEngine au JFrame
            displayZoneFrame.getContentPane().removeAll();
            displayZoneFrame.getContentPane().add(renderEngine);
            displayZoneFrame.setVisible(true);

            // Charger le niveau du jeu
            Playground level = new Playground("./data/level1.txt");
            renderEngine.addToRenderList(level.getSpriteList());
            renderEngine.addToRenderList(hero);
            physicEngine.addToMovingSpriteList(hero);
            physicEngine.setEnvironment(level.getSolidSpriteList());

            // Ajouter le KeyListener au JFrame
            displayZoneFrame.addKeyListener(gameEngine);
            displayZoneFrame.requestFocusInWindow();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        Main main = new Main();  // Lancer
    }
}
