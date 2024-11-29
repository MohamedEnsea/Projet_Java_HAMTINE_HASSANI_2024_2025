import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;

public class TitleScreen extends JPanel implements KeyListener {
    private Main main;
    private Image titleImage; // Champ pour stocker l'image
    private Timer timer;
    private boolean isVisible;

    public TitleScreen(Main main) {
        this.main = main;

        // Charger l'image Ecran_Titre
        try {
            titleImage = ImageIO.read(new File("./img/Ecran_Titre.png")); // Chemin relatif à partir de src
        } catch (IOException e) {}

        // Ajouter le KeyListener
        addKeyListener(this);
        setFocusable(true);
        requestFocusInWindow(); // Demander le focus pour capter les événements de clavier

        // Timer pour faire clignoter le message
        timer = new Timer(500, e -> {
            isVisible = !isVisible;
            repaint();
        });
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (titleImage != null) {
            g.drawImage(titleImage, 0, 0, getWidth(), getHeight(), this);
        }

        if (isVisible) {
            g.setColor(Color.BLACK);
            g.setFont(new Font("ROG Fonts", Font.BOLD, 15));

            String message = "Press any key to start";

            int x = 65;
            int y = 530;

            g.drawString(message, x, y);
        }
    }

    // Méthode pour démarrer le jeu
    private void startGame() {
        timer.stop(); // Arrêter le timer de clignotement
        main.startGame(); // Appelle la méthode pour démarrer le jeu
    }

    @Override
    public void keyPressed(KeyEvent e) {
        startGame(); // Démarrer le jeu quand une touche est pressée
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Pas besoin de gérer cela pour l'écran titre
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Pas besoin de gérer cela pour l'écran titre
    }
}
