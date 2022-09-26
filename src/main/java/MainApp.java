import window.Window;

public class MainApp {

    /**
     * TODO:
     * Refactor (remove constants if possible)
     * remove some functionnalities that aren't working (compute shaders / ray marching) - not gonna work on that ever
     *      again and way to hard to fix
     * add comments
     *
     * make paper airplane model
     *
     * MODELS DONE COMMENTING
     * BOIDS DONE
     * WINDOW DONE
     * UTILS DONE
     * CAMERA DONE
     *
     * @param args
     */

    public static void main(String[] args) {
        Window window = Window.create(1920, 1080, "Boid simulation");
        window.run(0);
    }
}
