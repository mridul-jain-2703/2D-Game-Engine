package jade;

import javax.imageio.plugins.tiff.GeoTIFFTagSet;
import java.security.Key;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class KeyListener {
    private static KeyListener instance;
    //350 is the amount of keybindings glfw has.
    private boolean keyPressed[] = new boolean[350];

    //We need to initialize the array to all 0's.
    //Java does this automatically as non initialized boolean are false.
    private KeyListener(){

    }

    //Singleton class
    public static KeyListener get(){
        if(KeyListener.instance == null){
            KeyListener.instance = new KeyListener();
        }
        return KeyListener.instance;
    }

    //action is either press or release
    //mods is again if some other key is being pressed simultaneously
    //scancode tells the comp. which keys are being pressed.
    public static void keyCallback(long window, int key, int scancode, int action, int mods){
        if(action == GLFW_PRESS){
            get().keyPressed[key] = true;
        }else if(action == GLFW_RELEASE){
            get().keyPressed[key] = false;
        }
    }

    //This is out getter function.
    public static boolean isKeyPressed(int keyCode){
            return get().keyPressed[keyCode];
    }
}

