package lighting;

import org.joml.Vector3f;
import org.joml.Vector4f;

/**
 * Generic light source
 */
public abstract class Light {

    private Vector3f position;

    /////////////////////////////////////
    //          ATTENUATION            //
    // x component: base attenuation   //
    // y component: linear attenuation //
    // z component: square attenuation //
    /////////////////////////////////////
    private Vector3f attenuation;

    private Vector4f lightColor = new Vector4f(1, 1, 1, 1);

    protected Light(Vector3f position, Vector3f attenuation) {
        this.position = position;
        this.attenuation = attenuation;
    }

    /////////////
    // SETTERS //
    /////////////

    public void setLightColor(Vector4f newLightColor) {
        this.lightColor = newLightColor;
    }
    public void setAttenuation(Vector3f attenuation) {
        this.attenuation = attenuation;
    }
    public void move(float dx, float dy, float dz) {
        this.position.add(dx, dy, dz);
    }

    /////////////
    // GETTERS //
    /////////////

    public Vector3f getPosition() {
        return position;
    }
    public Vector3f getAttenuation() {
        return attenuation;
    }
    public Vector4f getLightColor() {
        return lightColor;
    }
}
