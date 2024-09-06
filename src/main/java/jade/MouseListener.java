package jade;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseListener {
    //This is going to be static class. instance will be our singleton
    private static MouseListener instance;

    private double scrollX, scrollY;
    //This will be dx, dy i.e. number of units the mouse moved in between 2 frames
    private double xPos, yPos, lastX, lastY;
    //Will store which button was last pressed
    private boolean mouseButtonPressed[] = new boolean[3];
    private boolean isDragging;

    //Private constructor as this will be a singleton class
    private MouseListener(){
        this.scrollX = 0.0;
        this.scrollY = 0.0;
        this.xPos = 0.0;
        this.yPos = 0.0;
        this.lastX = 0.0;
        this.lastY = 0.0;
    }
    //Singleton Class
    public static MouseListener get(){
        if(MouseListener.instance == null){
            MouseListener.instance = new MouseListener();
        }
        return MouseListener.instance;
    }

    public static void mousePosCallback(long window, double xpos, double ypos){
        get().lastX = get().xPos;
        get().lastY = get().yPos;
        get().xPos = xpos;
        get().yPos = ypos;
        //We will get true whenever a mouse button is pressed(mouseButtonCallback).
        //So if mouse position changes while any button is true, it means it is dragging.
        get().isDragging = get().mouseButtonPressed[0] || get().mouseButtonPressed[1] || get().mouseButtonPressed[2];
    }
    //Mods is used if and when you press ctrl key while clicking simultaneously etc.
    public static void mouseButtonCallback(long window, int button, int action, int mods){
        if(action == GLFW_PRESS){
            //to check if other than 3 buttons are getting pressed in the mouse(eg. in a gaming mouse). This avoids error
            if(button < get().mouseButtonPressed.length) {
                get().mouseButtonPressed[button] = true;
            }
        }else if(action == GLFW_RELEASE) {
            if (button < get().mouseButtonPressed.length) {
                get().mouseButtonPressed[button] = false;
                get().isDragging = false;
            }
        }
    }

    public static void mouseScrollCallback(long window, double xOffset, double yOffset){
        get().scrollX = xOffset;
        get().scrollY = yOffset;
    }

    public static void endFrame(){
        get().scrollY = 0;
        get().scrollX = 0;
        //This will set the delta back to zero for the next frame
        get().lastY = get().yPos;
        get().lastX = get().xPos;
    }

    public static float getX(){
        return (float)get().xPos;
    }
    public static float getY(){
        return (float)get().yPos;
    }
    public static float getdx(){
        return (float)(get().lastX - get().xPos);
    }
    public static float getdy(){
        return (float)(get().lastY - get().yPos);
    }
    public static float getScrollX(){
        return (float)get().scrollX;
    }
    public static float getScrollY(){
        return (float)get().scrollY;
    }
    public static boolean isDragging(){
        return get().isDragging;
    }
    public static boolean mouseButtonDown(int button){
        if(button < get().mouseButtonPressed.length){
            return get().mouseButtonPressed[button];
        }else{
            return false;
        }
    }
}

