package models;

import models.textures.Texture;
import models.textures.TextureBuilder;
import utils.File;

/**
 * Texture of an opengl model.
 */
public class ModelTexture {

    private Texture texture;
    private float reflectivity;
    private float shineDamper;

    public ModelTexture(File textureFile, float shineDamper, float reflectivity) {
        this.texture = new TextureBuilder(textureFile).anisotropic().create();
        this.reflectivity = reflectivity;
        this.shineDamper = shineDamper;
    }

    public void bindTexture(int unit) {
        this.texture.bindToUnit(unit);
    }

    public float getReflectivity() {
        return reflectivity;
    }
    public void setReflectivity(float reflectivity) {
        this.reflectivity = reflectivity;
    }

    public float getShineDamper() {
        return shineDamper;
    }
    public void setShineDamper(float shineDamper) {
        this.shineDamper = shineDamper;
    }
}
