package lighting;

import org.joml.Vector3f;

public class PointLight extends Light {

        public PointLight(Vector3f lightPosition, Vector3f attenuation) {
            super(lightPosition, attenuation);
        }


}
