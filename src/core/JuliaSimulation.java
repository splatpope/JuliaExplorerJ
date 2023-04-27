package core;

import graphics.JuliaVisuals;

import java.util.HashMap;
import java.util.Map;

public class JuliaSimulation extends BaseSimulation {
    private static final float DEFAULT_PAN_STEP = 0.01f;
    private static final float DEFAULT_OFFSET_STEP = 0.005f;
    private static final float DEFAULT_ZOOM_STEP = 0.05f;

    public enum JuliaAction implements Action {
        PAN_UP, PAN_DOWN, PAN_LEFT, PAN_RIGHT,
        OFFSET_UP, OFFSET_DOWN, OFFSET_LEFT, OFFSET_RIGHT,
        ZOOM_FWD, ZOOM_BWD,
        ITER_10, ITER_100, ITER_1000,
        NONE,
    }

    public HashMap<JuliaAction, Boolean> actionStates = new HashMap<>();
    private final JuliaVisuals visuals;

    public JuliaSimulation(JuliaVisuals visuals) {
        this.visuals = visuals;
    }

    public void processActiveActions() {
        for(Map.Entry<JuliaAction, Boolean> actionEntry: actionStates.entrySet()) {
            JuliaAction action = actionEntry.getKey();
            boolean active = actionEntry.getValue();

            if (active) {
                processAction(action);
            }
        }
    }
    public void processAction(Action action) {
        JuliaAction juliaAction = (JuliaAction)action;
        float panStep = calculatePanStep();
        float offsetStep = calculateOffsetStep();
        float zoomStep = calculateZoomStep();

        switch (juliaAction) {
            case PAN_UP -> visuals.pan.y -= panStep;
            case PAN_DOWN -> visuals.pan.y += panStep;
            case PAN_LEFT -> visuals.pan.x -= panStep;
            case PAN_RIGHT -> visuals.pan.x += panStep;

            case OFFSET_UP -> visuals.offset.y -= offsetStep;
            case OFFSET_DOWN -> visuals.offset.y += offsetStep;
            case OFFSET_LEFT -> visuals.offset.x -= offsetStep;
            case OFFSET_RIGHT -> visuals.offset.x += offsetStep;

            case ZOOM_FWD -> visuals.zoom += zoomStep;
            case ZOOM_BWD -> visuals.zoom -= zoomStep;

            case ITER_10 -> visuals.iterations = 10;
            case ITER_100 -> visuals.iterations = 100;
            case ITER_1000 -> visuals.iterations = 1000;
        }
        visuals.dirty = true;
    }

    private float calculatePanStep() {
        return (float) (DEFAULT_PAN_STEP * Math.log(visuals.zoom * 10.0f) / visuals.zoom);
    }

    private float calculateOffsetStep() {
        return (float) (DEFAULT_OFFSET_STEP * Math.log(visuals.zoom * 10.0f) / visuals.zoom);
    }

    private float calculateZoomStep() {
        return (float) (DEFAULT_ZOOM_STEP * visuals.zoom);
    }
}
