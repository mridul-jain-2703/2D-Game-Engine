package renderer;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;

public class Shader {
    private int shaderProgramID;
    private String vertexSource;
    private String fragmentSource;
    private String filepath;

    public Shader(String filepath){
        this.filepath = filepath;
        try{
            String source = new String(Files.readAllBytes(Paths.get(filepath)));
            //This is our regex(used to match complex patterns.
            //This will give us shader section i.e. #type vertex || #type fragment
            String[] splitString = source.split("(#type)( )+([a-zA-z]+)");

            //Find the first pattern after #type 'pattern'
            int index = source.indexOf("#type") + 6;
            //This will look for end of line index number starting from the beginning i.e. index
            int eol = source.indexOf("\r\n",index);
            //trim() removes whitespaces
            //This will give us firstPattern i.e. vertex or fragment
            String firstPattern = source.substring(index,eol).trim();

            //Find the second pattern after #type 'pattern'
            index = source.indexOf("#type", eol) + 6;
            eol = source.indexOf("\r\n", index);
            String secondPattern = source.substring(index,eol).trim();

            //the first index will contain all the code in type pattern(whatever comes first)
            //starting from v of version core 460 till next # symbol is encountered.
            //p.s. 0th index is '#' therefore ignored.
            if(firstPattern.equals("vertex")){
                vertexSource = splitString[1];
            }else if(firstPattern.equals("fragment")){
                fragmentSource = splitString[1];
            }else{
                throw new IOException("Unexpected token '"+firstPattern+"'");
            }

            if(secondPattern.equals("vertex")){
                vertexSource = splitString[2];
            }else if(secondPattern.equals("fragment")){
                fragmentSource = splitString[2];
            }else{
                throw new IOException("Unexpected token '"+secondPattern+"'");
            }
        }catch (IOException e){
            //It helps in debugging by showing the sequence of method calls that led to the exception,
            // making it easier to identify the root cause of the error.
            e.printStackTrace();
            assert false: "Error: could not open file for shader '"+filepath+"'";
        }

    }

    public void compile(){
        int vertexId,fragmentId;

        //   =========================================================================
        //   Compile and Link Shaders
        //   =========================================================================

        //First load and compile the vertex shader
        vertexId = glCreateShader(GL_VERTEX_SHADER);
        //Pass the shader source to the GPU
        glShaderSource(vertexId,vertexSource);
        glCompileShader(vertexId);

        //Check for errors in compilation
        //i is for info, GL_COMPILE_STATUS returns 0 for false and any non-zero for true.
        int success = glGetShaderi(vertexId,GL_COMPILE_STATUS);
        if(success == GL_FALSE){
            int len = glGetShaderi(vertexId, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: '"+filepath+"' \n\tvertex shader compilation failed");
            System.out.println(glGetShaderInfoLog(vertexId, len));
            assert false: "";
        }

        //Load and compile the fragment shader
        fragmentId = glCreateShader(GL_FRAGMENT_SHADER);
        //Pass the shader source to the GPU
        glShaderSource(fragmentId,fragmentSource);
        glCompileShader(fragmentId);


        //Check for errors in compilation
        //i is for info, GL_COMPILE_STATUS returns 0 for false and any non-zero for true.
        success = glGetShaderi(fragmentId,GL_COMPILE_STATUS);
        if(success == GL_FALSE){
            int len = glGetShaderi(fragmentId, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: '"+filepath+"' \n\tfragment shader compilation failed");
            System.out.println(glGetShaderInfoLog(fragmentId, len));
            assert false: "";
        }


        //Link shaders and check for errors
        //Once again, a new identifier for
        shaderProgramID = glCreateProgram();
        glAttachShader(shaderProgramID, vertexId);
        glAttachShader(shaderProgramID, fragmentId);
        glLinkProgram(shaderProgramID);

        //Check for linking errors
        success = glGetProgrami(shaderProgramID,GL_LINK_STATUS);
        if(success == GL_FALSE){
            int len = glGetProgrami(shaderProgramID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: '"+filepath+"' \n\tlinking of shaders failed");
            System.out.println(glGetProgramInfoLog(shaderProgramID, len));
            assert false: "";
        }
    }

    public void use(){
        //Bind shader program
        glUseProgram(shaderProgramID);
    }

    public void detach(){
        glUseProgram(0);
    }

    public void uploadMat4f(String varName, Matrix4f mat4){
        int varLocation = glGetUniformLocation(shaderProgramID,varName);
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        mat4.get(matBuffer);
        glUniformMatrix4fv(varLocation,false,matBuffer);
    }
}
