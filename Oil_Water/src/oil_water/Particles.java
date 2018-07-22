package oil_water;

import org.lwjgl.util.vector.Vector3f;

public abstract class Particles {

    protected Vector3f Position;
    protected Vector3f Velocitie;
    protected String Type;
    protected float rho_0;
    protected float Density;
    protected float Mass;
    protected float Buoyancy_diffusion;
    protected float Viscosity_Coefficient;
    protected float Surface_Tension;
    protected float Threshold;
    protected float k;
    protected float Restitution_CR;
    protected float Kernel_particles_x;
    protected float h;
    public Particles() {
    }

    public void set_Position(Vector3f Position) {
        this.Position.x = Position.x;
        this.Position.y = Position.y;
        this.Position.z = Position.z;
    }

    public void set_Velocitie(Vector3f Velocitie) {
        this.Velocitie.x = Velocitie.x;
        this.Velocitie.y = Velocitie.y;
        this.Velocitie.z = Velocitie.z;
    }

    public void set_Type(String Type) {
        this.Type = Type;
    }

    public void set_rho_0(float rho) {
        this.rho_0 = rho;
    }

    public void set_densities(float rho) {
        this.Density = rho;
    }

    public void set_Mass(float Mass) {
        this.Mass = Mass;
    }

    public void set_Buoyancy_diffusion(float Buoyancy_diffusion) {
        this.Buoyancy_diffusion = Buoyancy_diffusion;
    }

    public void set_Viscosity_Coefficient(float Viscosity_Coefficient) {
        this.Viscosity_Coefficient = Viscosity_Coefficient;
    }

    public void set_Surface_Tension(float Surface_Tension) {
        this.Surface_Tension = Surface_Tension;
    }

    public void set_Threshold(float Threshold) {
        this.Threshold = Threshold;
    }

    public void set_k(float k) {
        this.k = k;
    }

    public void set_Restitution_CR(float Restitution_CR) {
        this.Restitution_CR = Restitution_CR;
    }

    public void set_Kernel_particles_x(float x) {
        this.Kernel_particles_x = x;
    }

    public void set_h(float h) {
        this.h = h;
    }

    public Vector3f get_Position() {
        Vector3f vec = new Vector3f(this.Position.x, this.Position.y, this.Position.z);
        return vec;
    }

    public float get_X() {
        return this.Position.x;
    }

    public float get_Y() {
        return this.Position.y;
    }

    public float get_Z() {
        return this.Position.z;
    }

    public Vector3f get_Velocitie() {
        Vector3f vec = new Vector3f(this.Velocitie.x, this.Velocitie.y, this.Velocitie.z);
        return vec;
    }

    public String get_Type() {
        return this.Type;
    }

    public float get_rho_0() {
        return this.rho_0;
    }

    public float get_densities() {
        return this.Density;
    }

    public float get_Mass() {
        return this.Mass;
    }

    public float get_Buoyancy_diffusion() {
        return this.Buoyancy_diffusion;
    }

    public float get_Viscosity_Coefficient() {
        return this.Viscosity_Coefficient;
    }

    public float get_Surface_Tension() {
        return this.Surface_Tension;
    }

    public float get_Threshold() {
        return this.Threshold;
    }

    public float get_k() {
        return this.k;
    }

    public float get_Restitution_CR() {
        return this.Restitution_CR;
    }

    public float get_Kernel_particles_x() {
        return this.Kernel_particles_x;
    }

    public float get_h() {
        return this.h;
    }

    public float dest(Vector3f pos) {
        float r[] = new float[3];
        r[0] = Position.x - pos.x;
        r[1] = Position.y - pos.y;
        r[2] = Position.z - pos.z;
        float r2 = r[0] * r[0] + r[1] * r[1] + r[2] * r[2];
        return (float) Math.sqrt(r2);
    }

}
