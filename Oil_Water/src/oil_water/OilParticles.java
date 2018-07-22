package oil_water;

import java.util.Random;
import static oil_water.Data.width;
import org.lwjgl.util.vector.Vector3f;

public class OilParticles extends Particles {
    public float C3;
    public float C4;
    public float C1;
    public OilParticles() {
        Position = new Vector3f(0, 0, 0);
        Velocitie = new Vector3f(0, 0, 0);
        Type = "Oil";
        /**/ rho_0 = 850f;
        /**/ Density = 850;
        /**/ Mass = 0.01703f;
        Buoyancy_diffusion = 0;
        /**/ Viscosity_Coefficient = 9f;
        Surface_Tension = 0.0728f;
        Threshold = 7.065f;
        k = 3f;
        Restitution_CR = 0;
        Kernel_particles_x = 20;
        h = 0.0726f -0.02f;
        C3 = 0.7f;
        C4 = 10f;
        C1 = 2.71f;
        
        
//        Position = new Vector3f(0, 0, 0);
//        Velocitie = new Vector3f(0, 0, 0);
//        Type = "Oil";
//        rho_0 = 998.29f;
//        Density = rho_0;
//        Mass = 0.02f - 0.001f;
//        Buoyancy_diffusion = 0;
//        Viscosity_Coefficient = 3.5f;
//        Surface_Tension = 0.0728f;
//        Threshold = 7.065f;
//        k = 3f;
//        Restitution_CR = 0;
//        Kernel_particles_x = 20;
//        h = 0.0457f;

    }

}
/*
 Type = "Mucus";
 rho_0 = 1000.0f;
 Density = rho_0;
 Mass = 0.04f;
 Buoyancy_diffusion = 0;
 Viscosity_Coefficient = 36f;
 Surface_Tension = 6f;
 Threshold = 5;
 k = 5f;
 Restitution_CR = 0.5f;
 Kernel_particles_x = 40;
 h = 0.0726f;
 */


 /*
 Type = "Steam";
 rho_0 = 0.59f;
 Density = rho_0;
 Mass = 0.00005f;
 Buoyancy_diffusion = 5;
 Viscosity_Coefficient = 0.01f;
 Surface_Tension = 0f;
 Threshold = 0;
 k = 4f;
 Restitution_CR = 0f;
 Kernel_particles_x = 12;
 h = 0.0624f;
 */
