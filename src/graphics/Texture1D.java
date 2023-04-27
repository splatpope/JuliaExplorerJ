package graphics;

import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_MAX_LEVEL;
//import static org.lwjgl.opengl.GL13.GL_CLAMP_TO_BORDER;
import static org.lwjgl.stb.STBImage.*;

public class Texture1D {
    private final int id;
    private int width;
    public Texture1D() {
        id = glGenTextures();
    }
    public void bind() {
        glBindTexture(GL_TEXTURE_1D, id);
    }
    public void setParameter(int name, int value) {
        glTexParameteri(GL_TEXTURE_1D, name, value);
    }
    public void uploadData(int width, ByteBuffer data) {
        uploadData(GL_RGB, width, GL_RGB, data);
    }
    public void uploadData(int internalFormat, int width, int format, ByteBuffer data) {
        glTexImage1D(GL_TEXTURE_1D, 0, internalFormat, width, 0, format, GL_UNSIGNED_BYTE, data);
    }

    public void delete() {
        glDeleteTextures(id);
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        if (width > 0) {
            this.width = width;
        }
    }

    public static Texture1D createTexture(int width, ByteBuffer data) {
        Texture1D texture = new Texture1D();
        texture.setWidth(width);

        texture.bind();

        texture.setParameter(GL_TEXTURE_MAX_LEVEL, 0);
        texture.setParameter(GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        texture.setParameter(GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        texture.setParameter(GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        texture.uploadData(GL_RGB, width, GL_RGB, data);

        return texture;
    }
    public static Texture1D loadTexture(String path) {
        ByteBuffer image;
        int width;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            /* Prepare image buffers */
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer comp = stack.mallocInt(1);

            /* Load image */
            //stbi_set_flip_vertically_on_load(true);
            image = stbi_load(path, w, h, comp, STBI_rgb);
            if (image == null) {
                throw new RuntimeException("Failed to load a texture file!"
                                           + System.lineSeparator() + stbi_failure_reason());
            }
            if (h.get() > 1) {
                throw new RuntimeException("Trying to load a 2D image as a 1D texture !" + System.lineSeparator() + path);
            }

            /* Get width and height of image */
            width = w.get();
        }

        return createTexture(width, image);
    }

}
