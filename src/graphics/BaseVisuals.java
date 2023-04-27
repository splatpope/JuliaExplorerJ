package graphics;

public abstract class BaseVisuals<T extends BaseRenderer> {
    protected T renderer;

    public abstract void update();
    public abstract void render();
    public void dispose() {
        renderer.dispose();
    }
}
