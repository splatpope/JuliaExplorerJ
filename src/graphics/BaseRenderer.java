package graphics;

import math.Vector2f;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL30.*;

public abstract class BaseRenderer {

    protected int vao;
    protected int vbo;

    protected int numVertices;

    protected boolean drawing;

    protected ShaderProgram program;

    protected FloatBuffer vertices;
    public void init() {
        vao = glGenVertexArrays();
        vbo = glGenBuffers();

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        glBindVertexArray(vao);

        glBindBuffer(GL_ARRAY_BUFFER, vbo);

        vertices = MemoryUtil.memAllocFloat(4096);
        //long size = vertices.capacity() * Float.BYTES;
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_DYNAMIC_DRAW);

        numVertices = 0;
        drawing = false;

        createShader();
        specifyVertexAttributes();
    }

    private static Vector2f getFBOSize() {
        long window = GLFW.glfwGetCurrentContext();
        int width, height;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer widthBuffer = stack.mallocInt(1);
            IntBuffer heightBuffer = stack.mallocInt(1);
            GLFW.glfwGetFramebufferSize(window, widthBuffer, heightBuffer);
            width = widthBuffer.get();
            height = heightBuffer.get();
        }
        return new Vector2f((float)width, (float)height);
    }

    public abstract void createShader();

    public void createShader(String vertexPath, String fragmentPath) {
        Shader vertexShader, fragmentShader;
        vertexShader = Shader.loadShader(GL_VERTEX_SHADER, vertexPath);
        fragmentShader = Shader.loadShader(GL_FRAGMENT_SHADER, fragmentPath);
        program = new ShaderProgram();
        program.attachShader(vertexShader);
        program.attachShader(fragmentShader);
        program.link();
        program.use();
        vertexShader.delete(); fragmentShader.delete();
    }
    public abstract void specifyVertexAttributes(); // depends on the shader and vbo format

    public void begin() {
        if (drawing) {
            throw new IllegalStateException("graphics.Renderer is already drawing!");
        }
        drawing = true;
        numVertices = 0;
    }

    public void end() {
        if (!drawing) {
            throw new IllegalStateException("graphics.Renderer isn't drawing!");
        }
        drawing = false;
        flush();
    }

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void flush() {
        if (numVertices > 0) {
            vertices.flip();

            glBindVertexArray(vao);
            program.use();

            /* Upload the new vertex data */
            glBindBuffer(GL_ARRAY_BUFFER, vbo);
            glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);

            /* Draw batch */
            glDrawArrays(GL_TRIANGLES, 0, numVertices);

            /* Clear vertex data for next batch */
            vertices.clear();
            numVertices = 0;
        }
    }

    public void dispose() {
        MemoryUtil.memFree(vertices);

        glDeleteVertexArrays(vao);
        glDeleteBuffers(vbo);
        program.delete();

    }

}
