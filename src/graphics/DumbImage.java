package graphics;

import static org.lwjgl.opengl.GL11.glClearColor;

public class DumbImage extends BaseVisuals<TexturedRenderer> {
    private final Texture2D texture;
    public DumbImage(String path) {
        texture = Texture2D.loadTexture(path);
        renderer = new TexturedRenderer();
        renderer.init();
        glClearColor(0.5f, 0.5f, 0.5f, 1f);
    }
    public void update() {}

    public void render() {
        renderer.clear();
        texture.bind();

        renderer.begin();
        renderer.drawTexture(texture, -0.5f, -0.5f, 1f, 1f);
        renderer.end();
    }

    public void dispose() {
        super.dispose();
        texture.delete();
    }
}
