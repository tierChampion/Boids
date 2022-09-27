import window.Window;

public class MainApp {

    /**
     * TODO:
     *
     * make paper airplane model
     * clean up the code for better usability (inputs for both controller and keyboard/mouse)
     * especially in BoidScene.java and Window.java
     */

    public static void main(String[] args) {
        Window window = Window.create(1920, 1080, "Boid simulation");
        window.run(0);
    }
}
