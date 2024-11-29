import java.util.ArrayList;
import javax.swing.Timer;

public class PhysicEngine implements Engine{
    private ArrayList<DynamicSprite> movingSpriteList;
    private ArrayList <Sprite> environment;
    private ArrayList<Trap> traps;  // Liste des pièges
    private Timer damageTimer; // Timer pour appliquer les dégâts

    public PhysicEngine() {
        movingSpriteList = new ArrayList<>();
        environment = new ArrayList<>();
        traps = new ArrayList<>();
        // Créer un Timer qui applique les dégâts toutes les 50ms
        damageTimer = new Timer(50, e -> {
            // Appliquer les dégâts progressifs à chaque sprite dynamique
            for (DynamicSprite dynamicSprite : movingSpriteList) {
                dynamicSprite.applyProgressiveDamage();
            }
        });

        // Démarrer le timer lorsque l'instance de PhysicEngine est créée
        damageTimer.start();
    }

    public void addToEnvironmentList(Sprite sprite){
        if (!environment.contains(sprite)){
            environment.add(sprite);
        }
    }

    public void addToTrapList(Trap trap) {
        if (!traps.contains(trap)) {
            traps.add(trap);
        }
    }

    public void setEnvironment(ArrayList<Sprite> environment){
        this.environment=environment;
    }

    public void addToMovingSpriteList(DynamicSprite sprite){
        if (!movingSpriteList.contains(sprite)){
            movingSpriteList.add(sprite);
        }
    }

    private void triggerGameOver() {
        System.out.println("Game Over !");
    }

    @Override
    public void update() {
        for(DynamicSprite dynamicSprite : movingSpriteList) {
            dynamicSprite.moveIfPossible(environment);

            boolean isInTrap = false;
            for (Trap trap : traps) {
                if (dynamicSprite.getHitBox().intersects(trap.getHitBox())) {
                    isInTrap = true;
                    dynamicSprite.startTakingDamage();
                }
            }

            if (!isInTrap) {
                dynamicSprite.stopTakingDamage();  // Si le héros n'est plus dans un piège, arrête les dégâts
            }

            // Appliquer les dégâts progressifs si le héros est sur un piège
            dynamicSprite.applyProgressiveDamage();

            // Vérification si la vie du héros est à zéro
            if (dynamicSprite.getLife() <= 0) {
                triggerGameOver();
            }
        }
    }
}
