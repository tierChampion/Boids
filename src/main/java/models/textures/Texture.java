package models.textures;

import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

/**
 * Opengl texture
 */
public class Texture {

    protected final int textureId;
    protected final int width;
    protected final int height;
    protected final int type;

    /**
     * Opengl texture
     * @param textureId Pointer to opengl texture
     * @param width x dimension of the texture
     * @param height y dimension of the texture
     * @param type Internal opengl type of the texture (usually GL_TEXTURE_2D)
     */
    public Texture(int textureId, int width, int height, int type) {
        this.textureId = textureId;
        this.width = width;
        this.height = height;
        this.type = type;
    }

    /**
     * Bind texture to texture unit
     * @param unit Location to bind the texture to
     */
    public void bindToUnit(int unit) {
        activateUnit(unit);
        bind();
    }

    /**
     * Free memory for the texture
     */
    public void delete() {
        glDeleteTextures(this.textureId);
    }

    /**
     * Activate specific texture unit
     * @param unit Texture unit to activate
     */
    protected void activateUnit(int unit) {
        glActiveTexture(GL_TEXTURE0 + unit);
    }

    /**
     * Bind the texture
     */
    protected void bind() {
        glBindTexture(type, textureId);
    }

    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
}
