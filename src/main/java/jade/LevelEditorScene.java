package jade;


import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import renderer.Shader;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class LevelEditorScene extends Scene{

    private String vertexShaderSrc = "#version 460 core\n" +
            "\n" +
            "layout (location = 0) in vec3 aPos;\n" +
            "layout (location = 1) in vec4 aColor;\n" +
            "\n" +
            "out vec4 fColor;\n" +
            "\n" +
            "void main(){\n" +
            "    fColor = aColor;\n" +
            "    gl_Position = vec4(aPos, 1.0);\n" +
            "}";

    private String fragmentShaderSrc = "#version 460 core\n" +
            "\n" +
            "in vec4 fColor;\n" +
            "out vec4 color;\n" +
            "\n" +
            "void main(){\n" +
            "    color = fColor;\n" +
            "\n" +
            "}";

    //Since we are passing data from our CPU to the GPU, we need some form of identifiers.
    //shaderProgram is just the combination of vertexId and fragmentId.

    private int vertexId, fragmentId, shaderProgram;

    private float[] vertexArray = {
        //position                        color
         100.5f, -0.5f, 0.0f,         1.0f, 0.0f, 0.0f, 1.0f, //Bottom right 0
        -0.5f,  100.5f, 0.0f,         0.0f, 1.0f, 0.0f, 1.0f, //Top left     1
         100.5f,  100.5f, 0.0f,         1.0f, 0.0f, 1.0f, 1.0f, //Top Left     2
        -0.5f, -0.5f, 0.0f,         1.0f, 1.0f, 0.0f, 1.0f, //Bottom Left  3
    };

    //IMPORTANT: Must be in counter-clockwise order
    private int[] elementArray = {
            /*
                *        *


                *        *
             */
        2, 1, 0, //Top-right triangle
        0, 1, 3  //bottom-right triangle
    };

    private int vaoId, vboId, eboId;
    private Shader defaultShader;

    public LevelEditorScene(){

    }

    @Override
    public void init(){
        //Puts the camera at (0,0)
        this.camera = new Camera(new Vector2f());

        defaultShader = new Shader("assets/shaders/default.glsl");
        defaultShader.compile();

        //   ======================================================================
        //   Generate VAO, VBO, EBO buffer objects and send to GPU
        //   ======================================================================
        //This creates a vertex array for the GPU
        vaoId = glGenVertexArrays();
        //Make sure everything we are doing, is to be done for vaoId.
        glBindVertexArray(vaoId);

        //Create a float buffer of vertices
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        //This will orient it the correct way for openGL, otherwise ERROR.
        vertexBuffer.put(vertexArray).flip();

        //Create VBO upload the vertex buffer
        vboId = glGenBuffers();
        //Make sure everything we are doing, is to be done for this buffer.
        //In this syntax, vboID is what we want to bind.
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        //This means, we are working with an arrayBuffer, we want to send vertexBuffer to vboId,
        //and send it statically i.e. we are not going to be changing it.
        glBufferData(GL_ARRAY_BUFFER,vertexBuffer,GL_STATIC_DRAW);

        //Create the indices and upload
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();
        eboId = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer,GL_STATIC_DRAW);

        //Add the vertex attribute pointers
        int positionsSize = 3;
        int colorsSize = 4;
        int floatSizeBytes = 4;
        //This will give us how big the whole vertex is in floats.
        int vertexSizeBytes = (positionsSize + colorsSize) * floatSizeBytes;
        glVertexAttribPointer(0, positionsSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);
        //pointer -> offset
        glVertexAttribPointer(1,colorsSize, GL_FLOAT, false, vertexSizeBytes, positionsSize * floatSizeBytes);
        glEnableVertexAttribArray(1);
    }



    @Override
    public void update(float dt){
        camera.position.x -= dt*50.0f;

        defaultShader.use();
        defaultShader.uploadMat4f("uProjection",camera.getProjectionMatrix());
        defaultShader.uploadMat4f("uView",camera.getViewMatrix());

        //Bind the VAO's we are using
        glBindVertexArray(vaoId);

        //Enable the vertex Attribute pointers
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        //Unbind Everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);
        defaultShader.detach();
    }
}
















//************************************************Rough Work*******************************************************

//    private boolean changingScene = false;
//    private float timeToChangeScene = 2.0f;//seconds

//      public void update(float dt){
//          This was test code that is not needed for the final project
//        System.out.println("" +(1.0f/dt) + "FPS");
//        if(!changingScene && KeyListener.isKeyPressed(KeyEvent.VK_SPACE)){
//            changingScene = true;
//        }

//        if(changingScene && timeToChangeScene > 0){
//            //This takes away the time that has passed away in the last frame
//            timeToChangeScene -= dt;
//            //Now we just update the windows RGB values for fade
//            //If we want to change the scene, we want to slowly decrease the opacity
//            Window.get().r -= dt * 5.0f;
//            Window.get().g -= dt * 5.0f;
//            Window.get().b -= dt * 5.0f;
//        }
//        //This means no time left i.e. we want to now change the scene as the 2sec. have elapsed
//        else if(changingScene){
//            Window.changeScene(1);
//        }
//      }