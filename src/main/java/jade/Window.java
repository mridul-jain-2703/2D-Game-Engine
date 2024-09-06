package jade;

import Util.Time;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

//This is a singleton class as we only ever want one window object
public class Window {
    private int width, height;
    private String title;
    private long glfwWindow;
    private boolean fadeToBlack = false;
    public float r,g,b,a;

    private static Window window = null;
    //Constructor is kept private as we do not ever want some outside thing creating a window

    private static Scene currentScene;
    private Window(){
        this.width = 1920;
        this.height = 1080;
        this.title = "Mario";
        r = 1;
        g = 1;
        b = 1;
        a = 1;
    }

    public static void changeScene(int newScene){
        switch(newScene){
            case 0:
                currentScene = new LevelEditorScene();
                currentScene.init();
                break;
            case 1:
                currentScene = new LevelScene();
                currentScene.init();
                break;
            default:
                assert false: "Unknown Scene '"+newScene+"'";
                break;
        }

    }

    public static Window get(){
        if(Window.window == null){
            Window.window = new Window();
        }
        return Window.window;
    }

    public void run(){
        System.out.println("Hello LWJGL" + Version.getVersion() + "!");

        init();
        loop();

        //These will be done automatically by OS, but proper practice is to include it in your code
        //Free the memory
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        //Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    //Initialization function to get the window up and running
    public void init(){
        //Setting up an error callback telling it where to print errors i.e. in build window where we see the output
        GLFWErrorCallback.createPrint(System.err).set();
        //Initialize GLFW
        if(!glfwInit()){
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        //Configure GLFW
        //We give configuration before creating the glfw window.
        //That is why GLFW_VISIBLE is kept as false
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        //Create the window
        //Null,Null sets it to primary monitor, and no sharing.
        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        //glfwCreateWindow gives a memory address where window is stored in form of LONG number.
        //that is why we make a private long glfwWindow instance variable in the beginning;
        if(glfwWindow == NULL){
            throw new IllegalStateException("failed to create GLFW window");
        }

        //These will be our mouse listener callbacks
        //Tells glfw to whom to forward its position callback.
        //This is(::) a shortcut for lambda function
        glfwSetCursorPosCallback(glfwWindow,MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);

        //Make the OpenGL context current
        glfwMakeContextCurrent(glfwWindow);
        //Enable V-sync i.e. swapping frames as fast as we can
        glfwSwapInterval(1);
        //Make the window visible
        glfwShowWindow(glfwWindow);
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        //In short, makes bindings available
        GL.createCapabilities();
        //So that we are in a scene when the game starts.
        Window.changeScene(0);
    }

    public void loop(){
        //We are using glfw.GetTime() as our getTime function is giving infinite fps numbers.
        float beginTime = (float) glfwGetTime();
        float endTime;
        float dt = -1.0f;

        while(!glfwWindowShouldClose(glfwWindow)){
            //Poll events i.e. getting key, mouse events etc.
            //This will poll the events and get them into the key listeners.
            glfwPollEvents();
            glClearColor(r,g,b,a);

            //Not needed for actual game
//            if(fadeToBlack){
//                r = Math.max(r-0.01f,0);
//                g = Math.max(g-0.01f,0);
//                b = Math.max(b-0.01f,0);
//            }
//            if(KeyListener.isKeyPressed(GLFW_KEY_SPACE)){
//                fadeToBlack = true;
//            }

            //This tells openGL how to clear the buffer, here it uses the color provided above, and flush that color to our entire screen
            glClear(GL_COLOR_BUFFER_BIT);
            //We are updating our scene not the first time we go through as dt is currently -1.
            //
            if (dt >= 0){
                currentScene.update(dt);
            }
            //Swaps buffers automatically
            glfwSwapBuffers(glfwWindow);

            endTime = (float) glfwGetTime();
            dt = endTime - beginTime;
            beginTime = endTime;

        }
    }
}


