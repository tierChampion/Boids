package models.textures;

import java.nio.ByteBuffer;

/**
 * Raw image texture data
 */
public class TextureData {

    private int width;
    private int height;
    private ByteBuffer buffer;

    /**
     * Data of raw image texture
     * @param buffer raw data
     * @param width X dimension of image
     * @param height Y dimension of image
     */
    public TextureData(ByteBuffer buffer, int width, int height){
        this.buffer = buffer;
        this.width = width;
        this.height = height;
    }

    public int getWidth(){
        return width;
    }
    public int getHeight(){
        return height;
    }
    public ByteBuffer getBuffer(){
        return buffer;
    }

}
