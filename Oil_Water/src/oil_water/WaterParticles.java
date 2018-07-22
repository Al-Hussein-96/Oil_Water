package oil_water;

import java.util.Random;
import static oil_water.Data.*;
import org.lwjgl.util.vector.Vector3f;

public class WaterParticles extends Particles {

    public WaterParticles() {
        this.Position = new Vector3f(0, 0, 0);
        this.Velocitie = new Vector3f(0, 0, 0);
        Random x = new Random();
        Position.x = -width / 2 + Math.abs((float) (x.nextInt()) / Integer.MAX_VALUE) * (width / 8);
        Position.y = -length / 2 + Math.abs((float) (x.nextInt()) / Integer.MAX_VALUE) * (length / 8.0f);
        Position.z = (float) (x.nextInt()) / Integer.MAX_VALUE * height / 4.0f;
        Position.z = 0;

        Type = "Water";
        rho_0 = 998.29f;
        Density = 998.29f;
        Mass = 0.02f;
        Buoyancy_diffusion = 0;
        Viscosity_Coefficient = 3.5f;
        Surface_Tension = 0.0728f;
        Threshold = 7.065f;
        k = 3f;
        Restitution_CR = 0;  // 0<= CR<=1
        Kernel_particles_x = 20;
        h = 0.0457f;
    }

}
