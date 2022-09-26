package lighting;

import org.joml.Vector3f;
import org.joml.Vector4f;

public abstract class Light {

    private Vector3f position;
    // How much the light dissipates with distance
    // x component: base attenuation
    // y component: linear attenuation
    // z component: square attenuation (just like nature)
    private Vector3f attenuation;
    // Color, initially white
    private Vector4f lightColor = new Vector4f(1, 1, 1, 1);

    // TODO
    // Add some kind of heading, to get like a headlight effect
    // Have a heading direction, with a value that the dot product of the heading and to light vector have to have

    protected Light(Vector3f position, Vector3f attenuation) {
        this.position = position;
        this.attenuation = attenuation;
    }

    public void setLightColor(Vector4f newLightColor) {
        this.lightColor = newLightColor;
    }

    public void setAttenuation(Vector3f attenuation) {
        this.attenuation = attenuation;
    }

    public void move(float dx, float dy, float dz) {
        this.position.add(dx, dy, dz);
    }

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
