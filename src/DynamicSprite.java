import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class DynamicSprite extends SolidSprite{
    private Direction direction = Direction.EAST;
    private double speed = 5;
    private double timeBetweenFrame = 200;
    private boolean isWalking =true;
    private final int spriteSheetNumberOfColumn = 10;
    private int life = 100;  // Vie du héros
    private boolean isInTrap = false; // Indicateur de si le héros est dans un piège
    private boolean takingDamage;



    public DynamicSprite(double x, double y, Image image, double width, double height) {
        super(x, y, image, width, height);
        this.life = 100;  // Initialiser la vie à 100
        this.takingDamage = false;  // Le héros ne prend pas de dégâts au départ
    }

    private boolean isMovingPossible(ArrayList<Sprite> environment){
        Rectangle2D.Double moved = new Rectangle2D.Double();
        switch(direction){
            case EAST: moved.setRect(super.getHitBox().getX()+speed,super.getHitBox().getY(),
                                    super.getHitBox().getWidth(), super.getHitBox().getHeight());
                break;
            case WEST:  moved.setRect(super.getHitBox().getX()-speed,super.getHitBox().getY(),
                    super.getHitBox().getWidth(), super.getHitBox().getHeight());
                break;
            case NORTH:  moved.setRect(super.getHitBox().getX(),super.getHitBox().getY()-speed,
                    super.getHitBox().getWidth(), super.getHitBox().getHeight());
                break;
            case SOUTH:  moved.setRect(super.getHitBox().getX(),super.getHitBox().getY()+speed,
                    super.getHitBox().getWidth(), super.getHitBox().getHeight());
                break;
        }

        for (Sprite s : environment){
            if ((s instanceof SolidSprite) && (s!=this)){
                if (((SolidSprite) s).intersect(moved)){
                    return false;
                }
            }
        }
        return true;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    private void move(){
        switch (direction){
            case NORTH -> {
                this.y-=speed;
            }
            case SOUTH -> {
                this.y+=speed;
            }
            case EAST -> {
                this.x+=speed;
            }
            case WEST -> {
                this.x-=speed;
            }
        }
    }

    public void moveIfPossible(ArrayList<Sprite> environment){
        if (isMovingPossible(environment)){
            move();
        }
    }

    // Méthode pour gérer la collision avec les pièges et réduire la vie
    public void checkCollisionWithTraps(ArrayList<Trap> traps) {
        Rectangle2D.Double heroHitBox = (Rectangle2D.Double) this.getHitBox();

        for (Trap trap : traps) {
            if (trap.getHitBox().intersects(heroHitBox)) {
                // Si le héros entre dans le piège, commencer à prendre des dégâts
                if (!isInTrap) {
                    isInTrap = true;  // Le héros est dans un piège
                    startTakingDamage();  // Commencer à prendre des dégâts
                }
                // Appliquer les dégâts progressifs
                applyProgressiveDamage();
            } else {
                // Si le héros sort du piège, arrêter de prendre des dégâts
                if (isInTrap) {
                    isInTrap = false;
                    stopTakingDamage();  // Arrêter de prendre des dégâts
                }
            }
        }
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;  // Mettre à jour la vie avec la nouvelle valeur
        if (this.life < 0) {
            this.life = 0;  // La vie ne peut pas être négative
        }
        if (this.life > 100) {
            this.life = 100;  // La vie ne peut pas dépasser 100
        }
    }

    public void takeDamage(int amount) {
        int currentLife = getLife();  // Obtenir la vie actuelle
        currentLife -= amount;  // Réduire la vie en fonction des dégâts
        if (currentLife < 0) {
            currentLife = 0;  // La vie ne peut pas être négative
        }
        setLife(currentLife);  // Mettre à jour la vie avec la nouvelle valeur
        System.out.println("Héros a pris des dégâts! Vie restante : " + getLife());
    }

    // Gérer les dégâts progressifs
    public void applyProgressiveDamage() {
        if (takingDamage && life > 0) {
            life -= 1;  // On perd 1 point de vie à chaque appel
            System.out.println("Héros perd 1 point de vie, vie restante : " + life);
        }
    }


    // Commencer à prendre des dégâts (lorsqu'il touche un piège)
    public void startTakingDamage() {
        this.takingDamage = true;
    }

    // Arrêter de prendre des dégâts (lorsqu'il quitte le piège)
    public void stopTakingDamage() {
        this.takingDamage = false;
    }


    @Override
    public void draw(Graphics g) {
        int index= (int) (System.currentTimeMillis()/timeBetweenFrame%spriteSheetNumberOfColumn);

        g.drawImage(image,(int) x, (int) y, (int) (x+width),(int) (y+height),
                (int) (index*this.width), (int) (direction.getFrameLineNumber()*height),
                (int) ((index+1)*this.width), (int)((direction.getFrameLineNumber()+1)*this.height),null);
    }
}
