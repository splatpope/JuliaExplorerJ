package core;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;
import org.lwjgl.opengl.GLUtil;
import org.lwjgl.system.Callback;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private final long id;

    private Callback debugProc;

    private boolean vsync;

    public Window(int width, int height, CharSequence title, boolean vsync) {
        this.vsync = vsync;

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        long temp = glfwCreateWindow(1, 1, "", NULL, NULL);
        glfwMakeContextCurrent(temp);
        GL.createCapabilities();
        GLCapabilities caps = GL.getCapabilities();
        glfwDestroyWindow(temp);

        glfwDefaultWindowHints();
        if (caps.OpenGL32) {
            glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
            glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
            glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
            glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
        } else if (caps.OpenGL21){
            glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 2);
            glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 1);
        } else {
            throw new RuntimeException("Neither OpenGL 3.2 nor OpenGL 2.1 is "
                    + "supported, you may want to update your graphics driver.");
        }
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
        glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, GLFW_TRUE);

        id = glfwCreateWindow(width, height, title, NULL, NULL);
        if (getId() == NULL) {
            glfwTerminate();
            throw new RuntimeException("Failed to create the GLFW window!");
        }

        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(getId(),
                (vidmode.width() - width) / 2,
                (vidmode.height() - height) / 2
        );

        glfwMakeContextCurrent(getId());
        GL.createCapabilities();
        debugProc = GLUtil.setupDebugMessageCallback(System.out);

        if (vsync) {
            glfwSwapInterval(1);
        }

    }

    public boolean isClosing() {
        return glfwWindowShouldClose(getId());
    }


    public void setTitle(CharSequence title) {
        glfwSetWindowTitle(getId(), title);
    }

    public void update() {
        glfwSwapBuffers(getId());
        glfwPollEvents();
    }

    public void destroy() {
        glfwDestroyWindow(getId());
        debugProc.free();
    }

    public void setVSync(boolean vsync) {
        this.vsync = vsync;
        if (vsync) {
            glfwSwapInterval(1);
        } else {
            glfwSwapInterval(0);
        }
    }

    public boolean isVSyncEnabled() {
        return this.vsync;
    }

    public long getId() {
        return id;
    }
}
