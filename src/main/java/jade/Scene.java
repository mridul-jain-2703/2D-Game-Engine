package jade;

public abstract class Scene {
    protected Camera camera;
    //This will be our main big game wrapper containing our game physics, objects etc.
    public Scene(){

    }
    public void init(){

    }

    //This is h where we will pass our update method using deltaTime,
    //so it can update whatever it needs to update
    public abstract void update(float dt);
}
