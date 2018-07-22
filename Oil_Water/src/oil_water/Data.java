package oil_water;

import org.lwjgl.util.vector.Vector3f;

public class Data {

    static float CurrentTime = 0;
    static float width = 0.4f;
    static float length = 0.4f;
    static float height = 0.3f;
    static float r = 0.01f;
    static int MaxWaterParticle = 300;
    static int MaxOilParticle = 50;
    static float n = 1;
    static float R = 8.314472f;
    static float num = 5;   ///    متحول لتغير المسافة بين الجزيئات

    static Vector3f Gravity = new Vector3f(0, -9.82f, 0); //
    static float timestep = 0.01f; //                  delta t
    static float Temperature = 293.15f; // (K)         T
    static float Pressure = 101325; //Pa =1(Atm)       P
    static float SpeenWind = 0;

//    static float rho0 = 998.29f; // kg/m^3   Density (rest)
//    static Vector3f Gravity = new Vector3f(0, -9.82f, 0); //
//    static float timestep = 0.01f; //                  delta t
//    static float Temperature = 293.15f; // (K)         T
//    static float Pressure = 101325; //Pa =1(Atm)       P
//    static float Buyancy_diffusion = 0; // n/a         b
//    static float Viscosity = 3.5f; //  Pa.s            u
//    static float Mass_particle = 0.02f;  //kg          m
//    static float Surface = 0.0728f; // N/m     oُ
//    static float Threshold = 7.065f; // n/a (l)        l
//    static float K = 3; // J /// ثابت الغاز متغير حسب الوسط
//    static float Restitution = 0; // n/a               cR
//    static float Kernel_particles = 20.0f; //n/a       x
//    static float h = 0.0457f; // m                     Support radius
//    static float h2 = 0.0f;
//    static float h6 = 0.0f;
//    static float h9 = 0.0f;
//    static float Mass_Oil = 0.04f;  //kg          m
//    static float Viscosity_Oil = 36f; //  Pa.s            u
//    static float Surface_Oil = 6; // N/m     oُ
//    static float rho0_Oil = 1000; // kg/m^3   Density (rest)
    ///static int Hash_number = 100;
    /// static List[] hash_table;
    static int numParticles = 0, numWaterParticles = 0; /// عدد الجزيئات
    static Particles Particle[] = new Particles[50000];
    static boolean bo = true; /// لتنزيل الماء بالماوس
    static boolean bo2 = false;
    static Vector3f[] force_total;
    static float fluidVolume = 0;
    static float R_circle = 0.3f;
    static boolean BoolForTybeCircle = true;
    static boolean getSolid = true;
    static float M = 0, I = 0;
    static Vector3f Xcm = new Vector3f(0, 0, 0);
    static Vector3f Vcm = new Vector3f(0, 0, 0);
    static Vector3f F = new Vector3f(0, 0, 0);
    static Vector3f Tao = new Vector3f(0, 0, 0);
    static Vector3f L = new Vector3f(0, 0, 0);
    static Vector3f W = new Vector3f(0, 0, 0);
    static Vector3f[] ri = new Vector3f[10000];
    static float w_boom = 0.01f, l_boom = 0.18f, width_Square = 0.1f;
    static int index_boom_left = -1, index_boom_right = -1;
    static float r_boom = 0.06f;
    static float force_moveBoom = 500000;

    static boolean booldegreeChange = false;
    static boolean booldegree = false;
    static float degree = 0;
    static int Type_Solid = 0; //  0--> boom      1--> Square      2--> Rectangle    else--> Circle
    static int NumberSolid = 0;

    static Vector3f PositionTube = new Vector3f(0, 0.3f, 0);
}
