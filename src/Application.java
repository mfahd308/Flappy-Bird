import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;


public class Application {
    //All the objects needed for the game minus the dynamically created obstacles
    static GameObject Gator, Background1, Background2A, Background2B, ScoreTracker, Floor, GatorHead, StarScreen;
    static GameObject Title, StartButton;
    static GameObject EndPopup, EndPopupArea, ResetButton;
    static GameObject Controller;
    static ArrayList<GameObject> Obstacles = new ArrayList<>();

    static boolean EndPoppedUp, isOnFloor, starCollided = false;

    static int state = 0; //0=title, 1=game running, 2=game ended
    static float speed = 8;
    static int score = 0;

    public static void Start(){

        //GAME OBJECTS
        Background1 = new GameObject(0,0);
        Background1.SetShape(new Rectangle2D.Float(0, 0, 1200, 800));//give a shape
        Background1.SetObjectMaterial((new Material("resources/Title.png")));//give a material
        //TODO: setup

        StartButton = new GameObject(715, 410); // position of button
        StartButton.SetShape(new Rectangle2D.Float(0, 0, 285, 120)); // button's dimensions
        StartButton.SetObjectMaterial((new Material(new Color(0, 0, 0, 0),new Color(0, 0, 0, 0),0)));
        StartButton.AddScript(new Button(StartButton));

        Background2A = new GameObject(0,0);
        Background2A.SetShape(new Rectangle2D.Float(0, 0, 1200, 800));//give a shape
        Background2A.SetObjectMaterial((new Material("resources/Background.png")));//give a material
        Background2A.AddScript(new HorizontalScroll(Background2A));

        StarScreen = new GameObject(0,0);
        StarScreen.SetShape(new Rectangle2D.Float(0, 0, 1200, 800));//give a shape
        StarScreen.SetObjectMaterial((new Material("resources/Background.png")));//give a material
        StarScreen.AddScript(new StarScreenScript(StarScreen));
        StarScreen.SetObjectMaterial(new Material(new Color(0, 0, 0, 0), new Color(0, 0, 0, 0), 0));


        Background2B = new GameObject(0,0);
        Background2B.Translate((float) Background2B.GetObjectTransform().getTranslateX() + 1200,0);
        Background2B.SetShape(new Rectangle2D.Float(0, 0, 1200, 800));//give a shape
        Background2B.SetObjectMaterial((new Material("resources/Background.png")));//give a material
        Background2B.AddScript(new HorizontalScroll(Background2B));


        GameObject bgManager = new GameObject(0, 0);
        bgManager.AddScript(new BackgroundResetter(bgManager));
        GatorEngine.Create(bgManager);
//        //TODO: setup

        Floor = new GameObject(0,GatorEngine.HEIGHT-80);
        Floor.SetShape(new Rectangle2D.Float(0, 0, 1200, 800));
        Floor.SetObjectMaterial((new Material(new Color(0, 0, 0, 0),new Color(0, 0, 0, 0),0)));

        EndPopup = new GameObject(0,0);
        EndPopup.SetShape(new Rectangle2D.Float(0, 0, 1200, 800));//give a shape
        EndPopup.SetObjectMaterial((new Material("resources/End.png")));//give a material

        EndPopupArea = new GameObject(715, 525); // position of button
        EndPopupArea.SetShape(new Rectangle2D.Float(0, 0, 285, 120)); // button's dimensions
        EndPopupArea.SetObjectMaterial((new Material(new Color(0, 0, 0, 0),new Color(0, 0, 0, 0),0)));
        EndPopupArea.AddScript(new EndButtonScript(EndPopupArea));

        GameObject ObstacleGenerator = new GameObject(0, 0);
        ObstacleGenerator.AddScript(new SwimmyGatorController(ObstacleGenerator));
        GatorEngine.Create(ObstacleGenerator);


        TitleScreen();


    }

    public static void Reset() {
        state = 0;
        score = 0;
        isOnFloor = false;
        starCollided = false;

        GatorEngine.Delete(Gator);
        GatorEngine.Delete(Floor);
        GatorEngine.Delete(EndPopup);
        GatorEngine.Delete(EndPopupArea);

        speed = 8;

        for (int i = 0; i < Obstacles.size(); i++) {
            GameObject obstacle = Obstacles.get(i);
            GatorEngine.Delete(obstacle);
        }
        Obstacles.clear();

        TitleScreen();
    }




    private static void CreateGator(){
        GatorEngine.Delete(Gator);
        GatorEngine.Delete(GatorHead);

        Gator = new GameObject(230, 400); // New Gator instance
        Gator.SetShape(new Ellipse2D.Float(0, 0, 330, 120));
        Gator.SetObjectMaterial(new Material("resources/Gator.png"));
        Gator.AddScript(new GatorController(Gator)); // Add control logic
        GatorEngine.Create(Gator);

        GatorHead = new GameObject(460, 430); // New Gator instance
        GatorHead.SetShape(new Rectangle2D.Float(0, 0, 100, 50));//give a shape
        GatorHead.SetObjectMaterial((new Material(new Color(0, 0, 0, 0),new Color(0, 0, 0, 0),0)));
        GatorHead.AddScript(new HeadController(GatorHead)); // Add control logic
        GatorEngine.Create(GatorHead);

    }

    private static GameObject CreateGarbage() {
        GameObject Garbage = new GameObject(GatorEngine.WIDTH,GatorEngine.HEIGHT/2-176/4 );
        //TODO: setup
        Garbage.SetShape(new Rectangle2D.Float(0, 0, 50, 75));//give a shape
        Garbage.SetObjectMaterial((new Material("resources/Garbage.png")));//give a material
        Garbage.AddScript(new ObstacleScroll(Garbage));
        GatorEngine.Create(Garbage);
        return Garbage;
    }

    private static GameObject CreateStar() {
        GameObject Star = new GameObject(GatorEngine.WIDTH,GatorEngine.HEIGHT/2-176/4 );
        //TODO: setup
        Star.SetShape(new Rectangle2D.Float(0, 0, 75, 75));//give a shape
        Star.SetObjectMaterial((new Material("resources/Star.png")));//give a material
        Star.AddScript(new StarScroll(Star));
        GatorEngine.Create(Star);
        return Star;
    }

    private static GameObject CreateRock() {
        Shape s = new Polygon(new int[]{77,155,0},new int[]{0,255,255},3);

        GameObject Rock = new GameObject(GatorEngine.WIDTH,GatorEngine.HEIGHT-275);
        Rock.SetShape(s);//give a shape
        Rock.SetObjectMaterial((new Material("resources/Rock.png")));//give a material
        Rock.AddScript(new ObstacleScroll(Rock));
        //TODO: setup
        GatorEngine.Create(Rock);
        return Rock;
    }
    private static GameObject CreateSwimmer() {
        Shape s = new Polygon(new int[]{0,363,200,50},new int[]{0,0,260,230},4);

        GameObject Swimmer = new GameObject(GatorEngine.WIDTH,0);
        Swimmer.SetShape(s);//give a shape
        Swimmer.SetObjectMaterial((new Material("resources/Swimmer.png")));//give a material
        Swimmer.AddScript(new ObstacleScroll(Swimmer));
        //TODO: setup
        GatorEngine.Create(Swimmer);
        return Swimmer;
    }

    //BELOW: scripts I used for my solution+brief(!) description
//TODO: implement these and use to setup objects

    //horizontally translates the gameObject based on speed
    public static class HorizontalScroll extends ScriptableBehavior{
        HorizontalScroll(GameObject g) {
            super(g);
        }

        @Override
        public void Start(){
        }

        @Override
        public void Update(){
            if (state == 1) {
                // Move the background left by speed
                gameObject.Translate(-speed, 0);
            }


        }
    }

    public static class StarScreenScript extends ScriptableBehavior{
        static float FRAMECOUNT = 0;
        static boolean isFlashing = false;
        static float flashDuration = 100; // The duration of the flash
        static float flashTimer = 0; // Timer to track the flash duration
        Color c1 = new Color(100, 120, 255, 128); // Blue
        Color c2 = new Color(255, 100, 100, 128); // Red
        Color currentColor = c1;

        StarScreenScript(GameObject g) {
            super(g);
        }

        @Override
        public void Start(){
        }

        @Override
        public void Update(){
            FRAMECOUNT++;

            // If the star collided with the gator, begin flashing
            if (starCollided) {
                if (!isFlashing) {
                    isFlashing = true;
                    flashTimer = flashDuration;
                }
            }

            if (isFlashing) {
                if (flashTimer > 0) {
                    if (FRAMECOUNT % 10 == 0) {
                        if (currentColor.equals(c1)) {
                            currentColor = c2;
                        } else {
                            currentColor = c1;
                        }
                    }
                    flashTimer--;
                } else {
                    isFlashing = false;
                    starCollided = false;
                    gameObject.SetObjectMaterial(new Material(new Color(0, 0, 0, 0), new Color(0, 0, 0, 0), 0)); // Reset to transparent
                }
            } else {
                if (FRAMECOUNT % 150 == 0) {
                    gameObject.SetObjectMaterial(new Material(new Color(0, 0, 0, 0), new Color(0, 0, 0, 0), 0)); // Reset screen
                }
            }

            if (isFlashing) {
                gameObject.SetObjectMaterial(new Material(currentColor, currentColor, 0));
            }
        }
    }


    public static class ObstacleScroll extends ScriptableBehavior{
        boolean bumped = false;
        ObstacleScroll(GameObject g) {
            super(g);
        }

        @Override
        public void Start(){
        }

        @Override
        public void Update(){
            if (starCollided) {
                if (gameObject.CollidesWith(GatorHead)) {
                    bumped = true;
                }
            }
            else {
                if (gameObject.CollidesWith(GatorHead)) {
                    if (state != 2) {
                        state = 2;
//                GatorEngine.Delete(gameObject);
                        End();
                    }
                }
            }
            if (state == 1) {
                if (gameObject.GetObjectTransform().getTranslateX() < -350) {
                    GatorEngine.Delete(gameObject);
                    Obstacles.remove(gameObject);
                }
                else {
                    if (bumped) {
                        gameObject.Translate(30,0);
                    }
                    else {
                        gameObject.Translate(-speed,0);
                    }
                }

            }
        }
    }

    public static class StarScroll extends ScriptableBehavior{
        StarScroll(GameObject g) {
            super(g);
        }

        @Override
        public void Start(){
        }

        @Override
        public void Update(){
            if (gameObject.CollidesWith(GatorHead)) {
                if (state != 2) {
                    starCollided = true;
                    GatorEngine.Delete(gameObject);
                    Obstacles.remove(gameObject);
                }
            }
            if (state == 1) {
                if (gameObject.GetObjectTransform().getTranslateX() < -350) {
                    GatorEngine.Delete(gameObject);
                    Obstacles.remove(gameObject);
                }
                else {
                    gameObject.Translate(-speed, 0);
                }

            }
        }
    }

//    public static class HorizontalDestroy extends ScriptableBehavior{
//        HorizontalDestroy(GameObject g) {
//            super(g);
//        }
//
//        @Override
//        public void Start(){
//
//        }
//
//        @Override
//        public void Update(){
//        }
//    }

    //moves the gator, and looks for input to make it "jump"
    public static class GatorController extends ScriptableBehavior{

        float vY = 0;
        float gravity = 2;
        float jumpGravity = -15;

        GatorController(GameObject g) {
            super(g);
        }

        @Override
        public void Start() {

        }

        @Override
        public void Update() {
            if (state == 1) {
                if (!isOnFloor) {
                    vY += gravity;
                }

                gameObject.Translate(0, vY);

                if (Input.GetKeyPressed(' ') && !isOnFloor) {
                    vY = jumpGravity;
                }
            }
            if (state == 2) {
                if (!isOnFloor) {
                    vY += gravity;
                    gameObject.Translate(0, vY);
                }
            }

        }
    }

    public static class HeadController extends ScriptableBehavior{

        float vY = 0;
        float gravity = 2;
        float jumpGravity = -15;

        float floorY = 770;

        HeadController(GameObject g) {
            super(g);
        }

        @Override
        public void Start() {

        }

        @Override
        public void Update() {
            if (state == 1) {
                if (!isOnFloor) {
                    vY += gravity;
                }

                gameObject.Translate(0, vY);

                if (Input.GetKeyPressed(' ') && !isOnFloor) {
                    vY = jumpGravity;
                }

                // Check for collision with the floor (if the gator's head hits the floor)
                if (gameObject.GetObjectTransform().getTranslateY() >= floorY) {
                    gameObject.GetObjectTransform().translate(gameObject.GetObjectTransform().getTranslateX(), floorY);
                    vY = 0;
                    isOnFloor = true;
                    state = 2;
                    GatorEngine.Delete(Gator);
                    End();
                }
            }

        }
    }

    //ends the game if the object collides with the gator head
//    public static class GatorCollideable extends ScriptableBehavior{
//        GatorCollideable(GameObject g) {
//            super(g);
//        }
//
//        @Override
//        public void Start() {
//
//        }
//
//        @Override
//        public void Update() {
//
//        }
//
//    }

    //checks when the backgrounds move off screen, and moves them to align with the other one
    public static class BackgroundResetter extends ScriptableBehavior{

        BackgroundResetter(GameObject g) {
            super(g);
        }

        @Override
        public void Start() {
//            GatorEngine.Create(Background2A);
//            GatorEngine.Create(Background2B);
        }

        @Override
        public void Update() {
            // Check if either background has moved off-screen
            if (Background2A.GetObjectTransform().getTranslateX() <= -1200) {
                Background2A.MoveTo((float) Background2B.GetObjectTransform().getTranslateX() + 1200,0);
            }
            if (Background2B.GetObjectTransform().getTranslateX() <= -1200) {
                Background2B.MoveTo((float) Background2A.GetObjectTransform().getTranslateX() + 1200,0);
            }
        }
    }

    //if the game has started, counts frames to spawn objects periodically
    public static class SwimmyGatorController extends ScriptableBehavior{

        static float FRAMECOUNT = 0;
        static float spawnRate = 50;

        SwimmyGatorController(GameObject g) {
            super(g);
        }

        @Override
        public void Start() {

        }

        @Override
        public void Update() {
            if (state == 1) {
                FRAMECOUNT++;

                if (FRAMECOUNT % spawnRate == 0) {

                    int rand = (int) (Math.random() * 4);
                    if (rand == 0) {
                        Obstacles.add(CreateGarbage());
                    } else if (rand == 1) {
                        Obstacles.add(CreateRock());
                    } else if (rand == 2) {
                        Obstacles.add(CreateSwimmer());
                    } else {
                        if (starCollided == false) {
                            Obstacles.add(CreateStar());
                        }
                    }
                }

                if (FRAMECOUNT % 10 == 0) {
                    speed += 0.25f;
                }
            }
        }


    }

    //runs the runnable when the button is clicked
//changes color when the button is hovered over by the mouse
    public static class Button extends ScriptableBehavior{

        Color c1, c2;
        Runnable r;

        Button(GameObject g) {
            super(g);
            c1= new Color(0, 0, 0, 0);
            c2= new Color(0, 100, 0, 128);
        }

        @Override
        public void Start() {
            state = 0;
            gameObject.SetObjectMaterial((new Material(c1, c1,0)));
            Input.SetMouseClicked(false);
        }

        @Override
        public void Update() {
            Point2D mousePosition = new Point2D.Float(Input.GetMouseX(), Input.GetMouseY());

            if (gameObject.Contains(mousePosition)) {
                gameObject.SetObjectMaterial(new Material(c2, c1, 0));

            } else {
                gameObject.SetObjectMaterial(new Material(c1, c1, 0));
            }
            if (Input.IsMouseDown()) {
                if (gameObject.Contains(mousePosition)) {
                    state = 1;
                    isOnFloor = false;
                    GatorEngine.Delete(Background1);
                    GatorEngine.Delete(StartButton);
                    GameScreen();

                }
            }

        }
    }

    public static class EndButtonScript extends ScriptableBehavior{

        Color c1, c2;
        Runnable r;

        EndButtonScript(GameObject g) {
            super(g);
            c1= new Color(0, 0, 0, 0);
            c2= new Color(0, 100, 0, 128);
        }

        @Override
        public void Start() {
            state = 2;
            gameObject.SetObjectMaterial((new Material(c1, c1,0)));
        }

        @Override
        public void Update() {
            Point2D mousePosition = new Point2D.Float(Input.GetMouseX(), Input.GetMouseY());

            if (gameObject.Contains(mousePosition)) {
                gameObject.SetObjectMaterial(new Material(c2, c1, 0));

            } else {
                gameObject.SetObjectMaterial(new Material(c1, c1, 0));
            }
            if (Input.IsMouseDown()) {
                if (gameObject.Contains(mousePosition)) {
                    Reset();

                }
            }

        }
    }


    //THESE ARE USED TO ACTIVATE/DEACTIVATE THE APPROPRIATE OBJECTS FOR EACH VIEW
//TODO: implement

    //turns off all objects not on the title screen, and turns those ones on
//deletes things made during the game
//changes state
    public static void TitleScreen(){
        GatorEngine.Delete(EndPopup);
        GatorEngine.Delete(EndPopupArea);
        GatorEngine.Create(Background1);
        GatorEngine.Create(StartButton);
    }

    //turns off all objects not on the game screen, and turns those ones on
//changes state
    public static void GameScreen(){
        // Clear backgrounds to prevent unintentional speeding up
        GatorEngine.Delete(Background2A);
        GatorEngine.Delete(Background2B);
        GatorEngine.Delete(StartButton);
        GatorEngine.Delete(Background1);

        GatorEngine.Create(Background2A);
        GatorEngine.Create(Background2B);
        GatorEngine.Create(StarScreen);
        GatorEngine.Create(Floor);
        CreateGator();


    }

    //turns on the popup, changes state
    public static void End(){
        GatorEngine.Delete(Floor);
        GatorEngine.Delete(StarScreen);
        GatorEngine.Create(EndPopup);
        GatorEngine.Create(EndPopupArea);

    }

}





