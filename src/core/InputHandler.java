package core;

import org.lwjgl.glfw.GLFWKeyCallback;

import static org.lwjgl.glfw.GLFW.*;

public class InputHandler {

    private final GLFWKeyCallback keyCallback = new GLFWKeyCallback() {
        @Override
        public void invoke(long window, int key, int scancode, int action, int mods) {
            System.out.println(key); //TODO : remove this :)
            if ((action == GLFW_PRESS || action == GLFW_REPEAT) && mods == 0) {
                simpleKeyPress(window, key);
            }

            if (action == GLFW_RELEASE) {
                simpleKeyRelease(window, key);
            }
        }
    };

    protected void simpleKeyPress(long window, int key) {
        switch (key) {
            case GLFW_KEY_ESCAPE -> glfwSetWindowShouldClose(window, true);
            //
        }
    }

    protected void simpleKeyRelease(long window, int key) {
        //
    }

    public void assignKeyCallback(long window) {
        glfwSetKeyCallback(window, keyCallback);
    }
    public void dispose() {
        keyCallback.free();
    }
}
