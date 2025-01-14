import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class GameObject {
    private AffineTransform ObjectTransform; //the location/scale/rotation of our object
    private Shape ObjectShape; //the collider/rendered shape of this object
    private Material ObjectMaterial; //data about the fill color, border color, and border thickness
    private ArrayList<ScriptableBehavior> ObjectScripts = new ArrayList<>(); //all scripts attached to the object
    private boolean IsActive = true; //whether this gets Updated() and Draw()

    //TODO: Create the default GameObject using:
    //TODO: a default AffineTransform
    //TODO: a default Material
    //TODO: a 10x10 pixel Rectangle2D.Float Shape at coordinate (0,0)
    public GameObject(){
        this.ObjectTransform = new AffineTransform();
        this.ObjectMaterial = new Material();
        this.ObjectShape = new Rectangle2D.Float(0, 0, 10, 10);
    }

    //TODO: Create the default GameObject, but with its AffineTransform translated to the coordinate (x,y)
    public GameObject(int x, int y){
        this.ObjectTransform = new AffineTransform();
        this.ObjectTransform.translate(x,y);
        this.ObjectMaterial = new Material();
        this.ObjectShape = new Rectangle2D.Float(0, 0, 10, 10);
    }

    //Engine Methods

    //TODO: Start all scripts on the object
    public void Start(){
        for (ScriptableBehavior script : this.ObjectScripts){
            script.Start();
        }
    }

    //TODO: Update all scripts on the object
    public void Update() {
        if (this.IsActive){
            for (ScriptableBehavior script : this.ObjectScripts){
                script.Update();
            }
        }
    }

    //TODO: Adds a new scripted behavior at runtime.
    // Start the script and add it to the list.
    public void AddScript(ScriptableBehavior NewScript){
        ObjectScripts.add(NewScript);
        NewScript.Start();
    }


    //TODO: Draw the object by
    // 1) saving the renderer's old transform.
    // 2) transforming the renderer based on the GameObject's transform.
    // 3) based on the type of material, drawing either the styled shape or the image scaled to the bounds of the shape.
    // 4) returning the old transform to the renderer.
    public void Draw(Graphics2D renderer) {

        if (!IsActive) {
            return;
        }

        AffineTransform currentTransform = renderer.getTransform();
        renderer.transform(this.ObjectTransform);

        if (this.ObjectMaterial.IsShape) {
            if (this.ObjectShape != null) {
                renderer.setPaint(this.ObjectMaterial.GetFill());
                renderer.fill(this.ObjectShape);

                renderer.setPaint(this.ObjectMaterial.GetBorder());
                renderer.setStroke(new BasicStroke(this.ObjectMaterial.GetBorderWidth()));
                renderer.draw(this.ObjectShape);
            }

        }
        else {
            if (this.ObjectShape != null) {
                Rectangle2D bounds = this.ObjectShape.getBounds2D();
                BufferedImage matImage = this.ObjectMaterial.GetMaterialImage();

                renderer.scale(bounds.getWidth() / matImage.getWidth(), bounds.getHeight() / matImage.getHeight());
                renderer.drawImage(matImage, (int) bounds.getX(), (int) bounds.getY(), null);
            }
        }
        renderer.setTransform(currentTransform);

    }

    //Movement/Collision Methods
    //TODO: Move the GameObject's transform by the provided values.
    public void Translate(float dX, float dY){
        this.ObjectTransform.translate(dX, dY);
    }

    //TODO:
    public void MoveTo(float x, float y){
        double deltaX = x - this.ObjectTransform.getTranslateX();
        double deltaY = y - this.ObjectTransform.getTranslateY();

        this.ObjectTransform.translate(deltaX, deltaY);
    }

    //TODO: Scale the GameObject's transform around the CENTER of its transformed shape.
    public void Scale(float sX, float sY){

        Rectangle2D bounds = this.ObjectShape.getBounds2D();
        double centerX = bounds.getCenterX();
        double centerY = bounds.getCenterY();

        this.ObjectTransform.translate(centerX, centerY);
        this.ObjectTransform.scale(sX, sY);
        this.ObjectTransform.translate(-centerX, -centerY);
    }

    //TODO: Returns true if the two objects are touching
    // i.e., the intersection of their Areas is not empty)
    public boolean CollidesWith(GameObject other) {

        Area area1 = new Area(this.ObjectTransform.createTransformedShape(this.ObjectShape));
        Area area2 = new Area(other.ObjectTransform.createTransformedShape(other.ObjectShape));

        area1.intersect(area2);
        return !area1.isEmpty();
    }

    //TODO:Should return true if the transformed shape contains the point
    public boolean Contains(Point2D point){
        Shape transformedShape = this.ObjectTransform.createTransformedShape(this.ObjectShape);
        return transformedShape.contains(point);
    }



    //Getters and Setters
    //TODO: Done for you!
    public AffineTransform GetObjectTransform() {
        return ObjectTransform;
    }

    public void SetObjectTransform(AffineTransform objectTransform) {
        this.ObjectTransform = objectTransform;
    }

    public Shape GetShape() {
        return ObjectShape;
    }

    public void SetShape(Shape shape) {
        this.ObjectShape = shape;
    }

    public Material GetObjectMaterial() {
        return ObjectMaterial;
    }

    public void SetObjectMaterial(Material objectMaterial) {
        this.ObjectMaterial = objectMaterial;
    }

    public ArrayList<ScriptableBehavior> GetObjectScripts() {
        return ObjectScripts;
    }

    public void SetObjectScripts(ArrayList<ScriptableBehavior> objectScripts) {
        this.ObjectScripts = objectScripts;
    }

    public boolean IsActive() {
        return IsActive;
    }

    public void SetActive(boolean active) {
        this.IsActive = active;
    }

}
