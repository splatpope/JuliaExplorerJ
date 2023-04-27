package graphics;

import math.Vector2f;

public class JuliaRenderer extends BaseRenderer {

    public void init() {
        super.init();
        //set uniforms
        int uniTex = program.getUniformLocation("tex");
        program.setUniform(uniTex, 0);
    }

    public void setZoom(float zoom) {
        int uniZoom = program.getUniformLocation("zoom");
        program.setUniform(uniZoom, zoom);
    }

    public void setPan(Vector2f panVector) {
        int uniPan = program.getUniformLocation("pan");
        program.setUniform(uniPan, panVector);
    }

    public void setOffset(Vector2f offset) {
        int uniC = program.getUniformLocation("c");
        program.setUniform(uniC, offset);
    }

    public void setIterations(int iterations) {
        int uniIter = program.getUniformLocation("iter");
        program.setUniform(uniIter, iterations);
    }

    public void createShader() {
        createShader("assets/julia.vs", "assets/julia.fs");
    }
    public void specifyVertexAttributes() {
        int posAttrib = program.getAttributeLocation("aPos");
        program.enableVertexAttribute(posAttrib);
        program.pointVertexAttribute(posAttrib, 2, 4 * Float.BYTES, 0);

        int texAttrib = program.getAttributeLocation("aTexCoord");
        program.enableVertexAttribute(texAttrib);
        program.pointVertexAttribute(texAttrib, 2, 4 * Float.BYTES, 2 * Float.BYTES);
    }

    //TODO : adapt to screenspace coordinates OR make it happen with projection matrices
    public void makeScreenQuad() {
        if (vertices.remaining() < 4 * 6) {
            flush();
        }

        float x1 = -1.0f;
        float x2 = 1.0f;
        float y1 = 1.0f;
        float y2 = -1.0f;

        float s1 = -1.0f;
        float t1 = -1.0f;
        float s2 = 1.0f;
        float t2 = 1.0f;

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
