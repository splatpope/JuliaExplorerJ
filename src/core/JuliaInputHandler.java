package core;

import static org.lwjgl.glfw.GLFW.*;
import static core.JuliaSimulation.JuliaAction.*;
import static core.JuliaSimulation.JuliaAction;
public class JuliaInputHandler extends InputHandler {
    private final JuliaSimulation sim;
    public JuliaInputHandler(JuliaSimulation sim) {
        this.sim = sim;
    }

    @Override
    protected void simpleKeyPress(long window, int key) {
        super.simpleKeyPress(window, key);
        JuliaAction action = keyToAction(key);
        sim.actionStates.put(action, true);
        //sim.processAction(action);
    }

    @Override
    protected void simpleKeyRelease(long window, int key) {
        super.simpleKeyRelease(window, key);
        JuliaAction action = keyToAction(key);
        sim.actionStates.put(action, false);
    }

    protected static JuliaAction keyToAction(int key) {
        return switch(key) {
            case GLFW_KEY_W -> PAN_UP;
            case GLFW_KEY_S -> PAN_DOWN;
            case GLFW_KEY_A -> PAN_LEFT;
            case GLFW_KEY_D -> PAN_RIGHT;

            case GLFW_KEY_UP -> OFFSET_UP;
            case GLFW_KEY_DOWN -> OFFSET_DOWN;
            case GLFW_KEY_LEFT -> OFFSET_LEFT;
            case GLFW_KEY_RIGHT -> OFFSET_RIGHT;

            case GLFW_KEY_KP_ADD -> ZOOM_FWD;
            case GLFW_KEY_KP_SUBTRACT -> ZOOM_BWD;

            case GLFW_KEY_1 -> ITER_10;
            case GLFW_KEY_2 -> ITER_100;
            case GLFW_KEY_3 -> ITER_1000;
            default -> NONE;
        };
    }

    protected void act() {
        this.sim.processActiveActions();
    }

}
