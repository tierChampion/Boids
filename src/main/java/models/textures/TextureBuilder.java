package models.textures;

import utils.File;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;

/**
 * Generator of a texture coming from a file
 */
public class TextureBuilder {

    private boolean clampEdges = false;
    private boolean mipmap = false;
    private boolean anisotropic = false;
    private boolean nearest = false;

    private final File file;

    /**
     * Texture generator
     * @param file Image file containing the texture
     */
    public TextureBuilder(File file) {
        this.file = file;
    }

    /**
     * Generate the texture
     * @return texture inside the file
     */
    public Texture create(){
        TextureData textureData = TextureUtils.decodeTextureFile(file);
        int textureId = TextureUtils.loadTextureToOpenGL(textureData, this);
        return new Texture(textureId, textureData.getWidth(), textureData.getHeight(), GL_TEXTURE_2D);
    }

    /// ----------------
    /// PROPERTY SETTERS
    /// ----------------

    public TextureBuilder clampEdges(){
        this.clampEdges = true;
        return this;
    }
    public TextureBuilder normalMipMap(){
        this.mipmap = true;
        this.anisotropic = false;
        return this;
    }
    public TextureBuilder nearestFiltering(){
        this.mipmap = false;
        this.anisotropic = false;
        this.nearest = true;
        return this;
    }
    public TextureBuilder anisotropic(){
        this.mipmap = true;
        this.anisotropic = true;
        return this;
    }

    /// ---------------
    /// PROPERTY CHECKS
    /// ---------------

    protected boolean isClampEdges() {
        return clampEdges;
    }
    protected boolean isMipmap() {
        return mipmap;
    }
    protected boolean isAnisotropic() {
        return anisotropic;
    }
    protected boolean isNearest() {
        return nearest;
    }
}
