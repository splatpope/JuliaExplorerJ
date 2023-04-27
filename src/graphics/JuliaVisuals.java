package graphics;
import math.Vector2f;

public class JuliaVisuals extends BaseVisuals<JuliaRenderer> {
    private Texture1D palette;
    public int iterations = 10000;
    public Vector2f offset = new Vector2f(0.0f, 0.0f);
    public Vector2f pan = new Vector2f(0.0f, 0.0f);
    public float zoom = 1.0f;
    public boolean dirty = true;
    public JuliaVisuals(String palettePath) {
        setNewPalette(palettePath);
        renderer = new JuliaRenderer();
        renderer.init();


    }

    public void setNewPalette(String palettePath) {
        if (palette != null) {
            palette.delete();
        }
        palette = Texture1D.loadTexture(palettePath);
    }

    public void update() {
        if (dirty) {
            renderer.setIterations(iterations);
            renderer.setOffset(offset);
            renderer.setPan(pan);
            renderer.setZoom(zoom);
            dirty = false;
        }
    }

    public void render() {
        renderer.clear();
        palette.bind();

        renderer.begin();
        renderer.makeScreenQuad();
        renderer.end();
    }

    @Override
    public void dispose() {
        super.dispose();
        palette.delete();
    }
}
