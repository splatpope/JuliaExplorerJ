package core;

import graphics.JuliaVisuals;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;

public class Application {

    public static final int TARGET_FPS = 75;
    public static final int TARGET_UPS = 30;
    private final static GLFWErrorCallback errorCallback = GLFWErrorCallback.createPrint(System.err);

    protected Timer timer = new Timer();
    protected JuliaVisuals visuals;
    protected JuliaInputHandler inputHandler;
    protected Window window;

    public void init() {

        glfwSetErrorCallback(errorCallback);
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        this.window = new Window(800, 800, "JJuliaExplorer", true);

        timer.init();
        visuals = new JuliaVisuals("assets/softgold.png");
        inputHandler = new JuliaInputHandler(new JuliaSimulation(visuals));
        inputHandler.assignKeyCallback(window.getId());
    }

    public void dispose() {
        visuals.dispose();
        inputHandler.dispose();
        window.destroy();
        glfwTerminate();
        errorCallback.free();
    }

    public void loop() {
        float delta;
        while (!window.isClosing()) {
            delta = timer.getDelta();

            //update then
            visuals.update();
            timer.updateUps();

            visuals.render();
            timer.updateFps();

            timer.update();

            window.update();

            if (!window.isVSyncEnabled()) {
                sync(TARGET_FPS);
            }
            inputHandler.act();
        }
    }

    public void sync(int fps) {
        double lastLoopTime = timer.getLastLoopTime();
        double now = timer.getTime();
        float targetTime = 1f / fps;

        while (now - lastLoopTime < targetTime) {
            Thread.yield();

            try {
                Thread.sleep(1);
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            }

            now = timer.getTime();
        }
    }
    public void run() {
        this.init();
        this.loop();
        this.dispose();

    }

    public static boolean isDefaultContext() {
        return GL.getCapabilities().OpenGL32;
    }
}
