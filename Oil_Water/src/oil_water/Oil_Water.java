package oil_water;

import static oil_water.Data.*;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.util.vector.Vector3f;

public class Oil_Water {

    public SPH sph = null;
    static public Draw DrawAll = null;

    public void start() throws InterruptedException {
        sph = new SPH();
        DrawAll = new Draw();
        init();

        while (!Display.isCloseRequested()) {
            if (BoolForTybeCircle) {
                PositionTube = new Vector3f(0, R_circle, 0);
            } else {
                PositionTube = new Vector3f(0, length / 2, 0);
            }
            System.out.println("*****     "+ numParticles);
            glClear(GL_COLOR_BUFFER_BIT);
            fluidVolume = 0;
            for (int i = 0; i < numParticles; i++) {
                fluidVolume += Particle[i].Mass / Particle[i].Density;
            }
            glClear(GL_DEPTH_BUFFER_BIT);
            glEnable(GL_DEPTH_TEST);
            glDisable(GL_DEPTH_TEST);
            sph.algo();
            if (BoolForTybeCircle) {
                DrawAll.circle();//// رسم الخزان 
            } else {
                DrawAll.cube();
            }
            DrawAll.drawTube();
            DrawAll.draw();/// رسم الجزيئات #
            DrawAll.Keyboard();
            DrawAll.getSolid();

            CurrentTime += timestep;
            cam();/// الكاميرا  @@
            Display.update();
            Display.sync(60);
        }

        Display.destroy();
    }

    public void cam() {
        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            Display.destroy();
            System.exit(0);
        }

    }

    public void init() {
        try {
            Display.setParent(Start.canvas);
            Display.setTitle("first LWJGL");
            Display.create();
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(-2, 2, -2, 2, 2, -2);
        glMatrixMode(GL_MODELVIEW);
        glEnable(GL_TEXTURE_2D);
    }

}
