package oil_water;

import org.lwjgl.util.vector.Vector3f;

public class Solid extends Particles {
    public int num;
    public Solid() {
        Position = new Vector3f(0, 0, 0);
        Velocitie = new Vector3f(0, 0, 0);
        Type = "Solid";
        rho_0 = 998.29f;
        Density = 998.29f;
        Mass = 0.025f;
        Buoyancy_diffusion = 0;
        Viscosity_Coefficient = 0f;
        Surface_Tension = 0f;
        Threshold = 0f;
        k = 0f;
        Restitution_CR = 0;  // 0<= CR<=1
        Kernel_particles_x = 20;
        h = 0.0457f;
    }
}
