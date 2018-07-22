package oil_water;

import java.util.Random;
import static oil_water.Data.*;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex3f;
import org.lwjgl.util.vector.Vector3f;
import static org.lwjgl.opengl.GL11.*;

public class Draw {

    public void circle() {
        glDisable(GL_DEPTH_TEST);
        sphere s = new sphere();
        glPushMatrix();
        {
            glColor3f(0, 1, 1);
            glTranslated(0, 0, 0);
            s.draw(R_circle * (num + 0.3f), 30, 30);
        }
        glPopMatrix();
        glEnable(GL_DEPTH_TEST);
    }

    public void cube() {

        glColor3f(1, 1, 1);
        float w = (width * num) / 2 + r, l = (length * num) / 2 + r, h = (height * num) / 2 + r;
        glBegin(GL_LINE_LOOP);                // BACK
        {
            glVertex3f(-w, -l, -h);
            glVertex3f(-w, l, -h);
            glVertex3f(w, l, -h);
            glVertex3f(w, -l, -h);
        }
        glEnd();

        glBegin(GL_LINE_LOOP);                // FRONT
        {
            glVertex3f(-w, -l, h);
            glVertex3f(-w, l, h);
            glVertex3f(w, l, h);
            glVertex3f(w, -l, h);
        }
        glEnd();
        glBegin(GL_LINE_LOOP);               // RIGHT
        {
            glVertex3f(w, -l, h);
            glVertex3f(w, -l, -h);
            glVertex3f(w, l, -h);
            glVertex3f(w, l, h);
        }
        glEnd();
        glBegin(GL_LINE_LOOP);             // LEFT
        {
            glVertex3f(-w, -l, h);
            glVertex3f(-w, -l, -h);
            glVertex3f(-w, l, -h);
            glVertex3f(-w, l, h);
        }
        glEnd();
        glBegin(GL_LINE_LOOP);            // UP
        {
            glVertex3f(-w, l, h);
            glVertex3f(-w, l, -h);
            glVertex3f(w, l, -h);
            glVertex3f(w, l, h);
        }
        glEnd();
        glBegin(GL_LINE_LOOP);            // DOWN
        {
            glVertex3f(-w, -l, -h);
            glVertex3f(-w, -l, h);
            glVertex3f(w, -l, h);
            glVertex3f(w, -l, -h);
        }
        glEnd();

    }

    public void draw() {
        Vector3f col_1 = new Vector3f(0, 0, 0);
        for (int i = 0; i < numParticles; i++) {
            if (Particle[i].get_Type() == "Water") {
                glColor3f(67.0f / 255, 119.0f / 255, 216.0f / 255);
            } else if (Particle[i].get_Type() == "Oil") {
                glColor3f(80.0f / 255, 80.0f / 255, 80.0f / 255);
            } else {
                glColor3f(145.0f / 255, 69.0f / 255, 0.0f / 255);
            }
            sphere s = new sphere();
            glPushMatrix();
            {
                glTranslated(Particle[i].get_X() * num, Particle[i].get_Y() * num, Particle[i].get_Z() * num);
                float temp = Particle[i].get_Mass() / Particle[i].get_densities();
                temp *= (3.0 / 4);
                temp /= Math.PI;
                temp = (float) Math.pow(temp, 0.3333333333);
                s.draw((Particle[i].get_Type() == "Solid" ? r * 4 : temp + r), 10, 10);
            }
            glPopMatrix();

        }

    }

    public void Keyboard() {
        if (Keyboard.isKeyDown(Keyboard.KEY_F)) {
            for (int i = 0; i < numParticles; i++) {
                Particle[i].Velocitie.x += 0.5f;

            }
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_G)) {
            for (int i = 0; i < numParticles; i++) {
                Particle[i].Velocitie.y += 0.5f;

            }
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_H)) {
            for (int i = 0; i < numParticles; i++) {
                Particle[i].Velocitie.z += 0.5f;

            }
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_O) && numParticles - numWaterParticles < MaxOilParticle) {
            float te = (float) Math.pow(MaxOilParticle - (numParticles - numWaterParticles), 0.5);
            for (float i = 0; i < te; i++) {
                for (float j = 0; j < te; j++) {
                    Particle[numParticles++] = new OilParticles();
                    Particle[numParticles - 1].set_Position(new Vector3f(
                            (i * (width / 2)) / te - (width / 2),
                            (j * (length / 2)) / te,
                            0)
                    );
                    Particle[numParticles - 1].set_Velocitie(new Vector3f(
                            1,
                            0,
                            0)
                    );
                }
            }
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_W) && numWaterParticles < MaxWaterParticle) {
            float te = (float) Math.pow(MaxWaterParticle - numWaterParticles, 0.5);
            for (float i = 0; i < te; i++) {
                for (float j = 0; j < te; j++) {
                    numWaterParticles++;
                    Particle[numParticles++] = new WaterParticles();
                    Particle[numParticles - 1].set_Position(new Vector3f(
                            (i * (width / 2)) / te,
                            (j * (length)) / te - (length / 2),
                            0)
                    );
                    Particle[numParticles - 1].set_Velocitie(new Vector3f(
                            1,
                            0,
                            0)
                    );
                }
            }
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_P) && numParticles - numWaterParticles < MaxOilParticle) {  //                   تنزيل الماء بالصنبور
            Random x = new Random();
            Vector3f vec = new Vector3f(PositionTube.x, PositionTube.y, PositionTube.z);
            Particle[numParticles] = new OilParticles();
            if (bo2) {
                bo2 = false;
                vec.x = PositionTube.x + 0.001f;
                vec.z = 0;
            } else {
                bo2 = true;
                vec.x = PositionTube.x;
                vec.z = 0;
            }
            Particle[numParticles].set_Position(vec);
            Particle[numParticles].set_Velocitie(new Vector3f(0.0f, 0f, 0.0f));
            numParticles++;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_E) && numWaterParticles < MaxWaterParticle) {  //                   تنزيل الماء بالصنبور
            Random x = new Random();
            Vector3f vec = new Vector3f(PositionTube.x, PositionTube.y, PositionTube.z);
            Particle[numParticles] = new WaterParticles();
            if (bo2) {
                bo2 = false;
                vec.x = PositionTube.x + 0.001f;
                vec.z = 0;
            } else {
                bo2 = true;
                vec.x = PositionTube.x;
                vec.z = 0;
            }
            Particle[numParticles].set_Position(vec);
            Particle[numParticles].set_Velocitie(new Vector3f(0.0f, 0f, 0.0f));
            numParticles++;
            numWaterParticles++;
        }

    }

    void drawTube() {
        glColor3f(1, 1, 1);
        glPushMatrix();
        {
            glTranslatef(PositionTube.x * num, PositionTube.y * num, PositionTube.z * num);
            glRotatef(90, 0, -1, 0);
            glRotatef(30, -1, 0, 0);
            tube(r * (num), 0.01f * (num));
        }
        glPopMatrix();
    }

    void tube(float radius, float segment_length) {
///        glPolygonMode(GL_BACK, GL_NONE);
        glPolygonMode(GL_FRONT, GL_FILL);

        glPushMatrix();
        {
            float z1 = 0.0f;
            float z2 = segment_length;

            float y_offset = 0.0f;
            float y_change = 0.00f;

            int i = 0;
            int j = 0;
            for (j = 0; j < 20; j++) {
                glPushMatrix();
                {
                    glBegin(GL_TRIANGLE_STRIP);
                    {
                        for (i = 360; i >= 0; i--) {
                            float theta = (float) i * (float) Math.PI / 180;
                            float x = radius * (float) Math.cos(theta);
                            float y = radius * (float) Math.sin(theta) + y_offset;

                            glVertex3f(x, y, z1);
                            glVertex3f(x, y, z2);
                        }
                    }
                    glEnd();
                }
                glPopMatrix();

                // attach the front of the next segment to the back of the previous
                z1 = z2;
                z2 += segment_length;

                // make some other adjustments
                y_offset += y_change;
            }
        }
        glPopMatrix();
    }

    public void getSolid() {
        if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            if (getSolid) {
                getSolid = false;
                if (Type_Solid == 0) {
                    getSolidBoom();
                } else if (Type_Solid == 1) {
                    getSolidSquare();
                } else if (Type_Solid == 2) {
                    getSolidRect();
                } else {
                    getSolidCircle();
                }
                NumberSolid++;
            }
        } 
    }

    public void getSolidBoom() {
        float r = Data.r * (3.0f / 4);
        //      int last = -1;
        for (int j = 0; j < l_boom / (2 * r); j++) {
            for (int i = 0; i < w_boom / (2 * r); i++) {
                if (j != 0 && j <= l_boom / (2 * r) - 1) {
                    if (i > 0 && i < w_boom / (2 * r) - 1) {
                        continue;
                    }
                }
                Particle[numParticles++] = new Solid();
                ((Solid) Particle[numParticles - 1]).num = NumberSolid;
                float x = i * (2 * r) - (w_boom / 2) + r;
                float y = j * (2 * r) - (l_boom / 2) + r;
                Particle[numParticles - 1].set_Position(new Vector3f(x, y, 0));
                //               last = numParticles - 1;
            }
        }
//                Particle[last].set_Mass(0.08f);
//                Particle[last - 1].set_Mass(0.08f);
        int temp = (int) (r_boom / (2 * r));
        float l = 2 * (float) Math.PI * r_boom;
        l = l / (2 * r);
        l = 360 / l;
        for (float d = 0; d < 360; d += l) {
            Particle[numParticles++] = new Solid();
            ((Solid) Particle[numParticles - 1]).num = NumberSolid;
            float x = r_boom * (float) Math.cos(d * Math.PI / 180);
            float y = r_boom * (float) Math.sin(d * Math.PI / 180) + (l_boom - r * 2);
            Particle[numParticles - 1].set_Position(new Vector3f(x, y, 0));
        }
    }

    public void getSolidSquare() {
        for (int j = 0; j < width_Square / (2 * r); j++) {
            for (int i = 0; i < width_Square / (2 * r); i++) {
                if (j != 0 && j <= width_Square / (2 * r) - 2) {
                    if (i > 0 && i < width_Square / (2 * r) - 1) {
                        continue;
                    }
                }
                Particle[numParticles++] = new Solid();
                ((Solid) Particle[numParticles - 1]).num = NumberSolid;
                float x = i * (2 * r) - (width_Square / 2) + r;
                float y = j * (2 * r) - (width_Square / 2) + 1;
                Particle[numParticles - 1].set_Position(new Vector3f(x, y, 0));
            }
        }
    }

    public void getSolidRect() {
        for (int j = 0; j < l_boom / (2 * r); j++) {
            for (int i = 0; i < width_Square / (2 * r); i++) {
                if (j != 0 && j <= l_boom / (2 * r) - 2) {
                    if (i > 0 && i < width_Square / (2 * r) - 1) {
                        continue;
                    }
                }
                Particle[numParticles++] = new Solid();
                ((Solid) Particle[numParticles - 1]).num = NumberSolid;
                float x = i * (2 * r) - (width_Square / 2) + r;
                float y = j * (2 * r) - (l_boom / 2) + 1;
                Particle[numParticles - 1].set_Position(new Vector3f(x, y, 0));
            }
        }
    }

    public void getSolidCircle() {
        float r = Data.r * (3.0f / 4);
        int temp = (int) (r_boom / (2 * r));
        float l = 2 * (float) Math.PI * r_boom;
        l = l / (2 * r);
        l = 360 / l;
        for (float d = 0; d < 360; d += l) {
            Particle[numParticles++] = new Solid();
            ((Solid) Particle[numParticles - 1]).num = NumberSolid;
            float x = r_boom * (float) Math.cos(d * Math.PI / 180);
            float y = r_boom * (float) Math.sin(d * Math.PI / 180) + (l_boom - r * 2);
            Particle[numParticles - 1].set_Position(new Vector3f(x, y, 0));
        }
    }

}
