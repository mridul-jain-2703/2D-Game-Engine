package jade;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera {
    //Projection Matrix transforms 3d cords. into 2d viewable space. E.g. we will be looking in direction of Z-axis
    //but will be able to move along X & Y axis.
    //View Matrix defines the camera position, or what will be visible.
    private Matrix4f projectionMatrix, viewMatrix;
    public Vector2f position;

    public Camera(Vector2f position){
        this.position = position;
        this.projectionMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();
        adjustProjection();
    }

    //This is to set the type of projection i.e. Perspective or Orthographic
    //E.g. we would need to switch between them when resizing window
    public void adjustProjection(){
        //This sets the matrix to an identity matrix i.e. 1's
        projectionMatrix.identity();
        //From JOML(Java OpenGL Math Library)
        //This sets the extent of viewable area i.e. On x-axis -> 0.0f to 1280.0f, same from top to bottom
        //Near and Far are clipping planes i.e. at the distance object gets cut(not visible)
        projectionMatrix.ortho(0.0f,32.0f *40.0f,0.0f,32.0f * 21.0f,0.0f,100.0f);
    }

    //This defines where the camera is
    public Matrix4f getViewMatrix(){
        //This is where the front of camera is looking
        Vector3f cameraFront = new Vector3f(0.0f,0.0f,-1.0f);
        //This will be used to tell, which direction is up for the camera i.e. Y
        Vector3f cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);
        this.viewMatrix.identity();
        //eye -> This is where the camera is positioned
        //centre -> The point in space the camera is looking at, cameraFront.add moves the centre slightly in front of camera
        //[C  *] -> C is centre, * is camera position
        //up -> The direction of UP
        viewMatrix.lookAt(new Vector3f(position.x, position.y,20.0f),
                            cameraFront.add(position.x,position.y,0.0f),
                            cameraUp);
        return this.viewMatrix;
    }

    public Matrix4f getProjectionMatrix(){
        return this.projectionMatrix;
    }
}
