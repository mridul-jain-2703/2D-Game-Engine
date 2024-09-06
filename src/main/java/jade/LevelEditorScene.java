package jade;


public class LevelEditorScene extends Scene{


    public LevelEditorScene(){

    }
    @Override
    public void update(float dt){

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