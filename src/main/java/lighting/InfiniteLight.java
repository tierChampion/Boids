package lighting;

import org.joml.Vector3f;

public class InfiniteLight extends Light {

    private static final int INFINITE_DISTANCE = 1000;


    public InfiniteLight(Vector3f lightDirection) {
        super(lightDirection.normalize().mul(INFINITE_DISTANCE), new Vector3f(1, 0, 0));
    }

    @Override
    public void setAttenuation(Vector3f attenuation) {
        System.err.println("ERROR: An Infinite light can not have its attenuation changed! ");
    }
}
