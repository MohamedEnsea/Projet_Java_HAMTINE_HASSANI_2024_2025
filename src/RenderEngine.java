import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class RenderEngine extends JPanel implements Engine{
    private ArrayList<Displayable> renderList;
    private long lastTime;
    private int frames;
    private int fps;
    private DynamicSprite hero;  // Référence au héros
    private Image gameOverImage;  // Référence à l'image Game Over

    public RenderEngine(JFrame jFrame) {
        renderList = new ArrayList<>();
        lastTime = System.currentTimeMillis();

        try {
            // Charger l'image Game Over
            gameOverImage = ImageIO.read(new File("./img/Game_Over.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour dessiner l'image Game Over sur tout l'écran
    private void drawGameOver(Graphics g) {
        if (gameOverImage != null) {
            // Redimensionner l'image pour qu'elle prenne toute la taille de la fenêtre
            g.drawImage(gameOverImage, 0, 0, getWidth(), getHeight(), null);  // Étirement de l'image pour remplir tout l'écran
        }
    }

    public void addToRenderList(Displayable displayable){
        if (!renderList.contains(displayable)){
            renderList.add(displayable);
        }
    }

    public void addToRenderList(ArrayList<Displayable> displayable) {
        for (Displayable d : displayable) {
            if (!renderList.contains(d)) {
                renderList.add(d);
            }
        }
    }

    // Méthode pour définir le héros dans le moteur de rendu
    public void setHero(DynamicSprite hero) {
        this.hero = hero;
    }

    public void drawHealthBar(Graphics g) {
        int health = hero.getLife();  // Récupérer la vie du héros
        int maxHealth = 100;  // Vie maximale du héros
        int barWidth = 200;  // Largeur de la barre de vie
        int barHeight = 20;  // Hauteur de la barre de vie

        // Calculer la position pour afficher la barre en haut à droite
        int x = getWidth() - barWidth - 20;  // Position horizontale à droite
        int y = 20;  // Position verticale à 20 pixels du haut

        // Dessiner l'arrière-plan de la barre de vie (barre grise)
        g.setColor(Color.GRAY);
        g.fillRect(x, y, barWidth, barHeight);

        // Dessiner la barre de vie
        int currentHealthWidth = (int) ((health / (double) maxHealth) * barWidth);
        g.setColor(Color.RED);
        g.fillRect(x, y, currentHealthWidth, barHeight);

        // Dessiner le texte de la vie au centre de la barre rouge
        g.setColor(Color.WHITE);  // Couleur du texte
        String lifeText = "Life: " + health;  // Texte à afficher
        FontMetrics metrics = g.getFontMetrics();  // Obtenir les dimensions du texte
        int textWidth = metrics.stringWidth(lifeText);  // Largeur du texte
        int textX = x + (currentHealthWidth - textWidth) / 2;  // Centrer le texte horizontalement
        int textY = y + metrics.getAscent();  // Centrer le texte verticalement

        // Afficher le texte
        g.drawString(lifeText, textX, textY);
    }


    @Override
    public void paint(Graphics g) {
        super.paint(g);

        for (Displayable renderObject : renderList) {
            renderObject.draw(g);
        }

        // Afficher la barre de vie du héros
        drawHealthBar(g);

        if (hero != null && hero.getLife() <= 0) {
            drawGameOver(g);  // Afficher l'image Game Over
        }

        // Afficher les FPS
        g.setFont(new Font("ROG Fonts", Font.BOLD, 12)); // Police en gras et plus grande
        g.setColor(Color.RED);
        g.drawString("FPS : " + fps, 10, 20);
    }

    @Override
    public void update() {
        long currentTime = System.nanoTime();  // Utilisation de nanoTime pour plus de précision
        frames++;

        if (currentTime - lastTime >= 1000000000L) { // 1 seconde = 1 milliard de nanosecondes
            fps = frames;
            frames = 0;
            lastTime = currentTime;
        }

        this.repaint();
    }
}
