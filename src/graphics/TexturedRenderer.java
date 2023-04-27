package graphics;

/* For use with textured.fs/vs */
public class TexturedRenderer extends BaseRenderer {
    public void init() {
        super.init();
        int uniTex = program.getUniformLocation("tex");
        program.setUniform(uniTex, 0);
    }
    public void createShader() {
        createShader("assets/textured.vs", "assets/textured.fs");
    }
    public void specifyVertexAttributes() {
        int posAttrib = program.getAttributeLocation("aPos");
        program.enableVertexAttribute(posAttrib);
        program.pointVertexAttribute(posAttrib, 2, 4 * Float.BYTES, 0);

        int texAttrib = program.getAttributeLocation("aTexCoord");
        program.enableVertexAttribute(texAttrib);
        program.pointVertexAttribute(texAttrib, 2, 4 * Float.BYTES, 6 * Float.BYTES);
    }


    public void drawTexture(Texture2D texture, float x, float y) {
        /* Vertex positions */
        float x1 = x;
        float y1 = y;
        float x2 = x1 + texture.getWidth();
        float y2 = y1 + texture.getHeight();

        /* graphics.Texture coordinates */
        float s1 = 0f;
        float t1 = 0f;
        float s2 = 1f;
        float t2 = 1f;

        drawTextureRegion(x1, y1, x2, y2, s1, t1, s2, t2);
    }

    public void drawTexture(Texture2D texture, float x, float y, float w, float h) {
        /* Vertex positions */
        float x1 = x;
        float y1 = y;
        float x2 = x1 + w;
        float y2 = y1 + h;

        /* graphics.Texture coordinates */
        float s1 = 0f;
        float t1 = 0f;
        float s2 = 1f;
        float t2 = 1f;

        drawTextureRegion(x1, y1, x2, y2, s1, t1, s2, t2);
    }


    public void drawTextureRegion(float x1, float y1, float x2, float y2, float s1, float t1, float s2, float t2) {
        if (vertices.remaining() < 4 * 6) {
            /* We need more space in the buffer, so flush it */
            flush();
        }

        float[] data = {
                x1, y1, s1, t1,
                x1, y2, s1, t2,
                x2, y2, s2, t2,
                x1, y1, s1, t1,
                x2, y2, s2, t2,
                x2, y1, s2, t1,
        };

        vertices.put(data);
        numVertices += 6;
    }

}
