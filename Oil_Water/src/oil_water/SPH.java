package oil_water;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import static oil_water.Data.*;
import org.lwjgl.input.Keyboard;
import static org.lwjgl.opengl.GL11.glRotated;
import org.lwjgl.util.vector.Vector3f;

public class SPH {

    public void algo() {

        force_total = new Vector3f[numParticles];
        /* مجموع القوى المؤثرة*/
        for (int i = 0; i < numParticles; i++) {
            force_total[i] = new Vector3f(0, 0, 0);
        }
        ///Calc_F();
        Calc_Density(); // حساب الكثافة 
        Calc_Pressure();
        Calc_Visocity();
        Calc_Gravity();
        Calc_Surface_tension();
        Solid_Power();
        Update_Velocity_AdvectParticles();
        Calc_Boundary();
        //  Calc_h();
        CalcOilOnly();
    }

    public void Calc_F() {
        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
            F.y -= force_moveBoom;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
            F.y += force_moveBoom;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
            F.x -= force_moveBoom;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
            F.x += force_moveBoom;
        }
    }

    public void Calc_h() {
        for (int i = 0; i < numParticles; i++) {
            float temp = 3 * fluidVolume * Particle[i].Kernel_particles_x;
            temp /= (4 * Math.PI * numParticles);
            Particle[i].h = (float) Math.pow(temp, 0.33333333333f);
            System.out.println(Particle[i].h + "   *********");
        }
    }

    public float Length_Vector(Vector3f a) {
        return (float) Math.sqrt(a.x * a.x + a.y * a.y + a.z * a.z);
    }

    public void Normalize_Vector(Vector3f a, Vector3f b)/* شعاع الواحدة*/ {
        float len = Length_Vector(a); // يجيب  طول  a
        if (len > 0.0f) {
            b.x = a.x / len;
            b.y = a.y / len;
            b.z = a.z / len;
        } else {
            b.x = b.y = b.z = 0.0f;
        }
    }

    public float Dot_Vector(Vector3f a, Vector3f b) {
        return a.x * b.x + a.y * b.y + a.z * b.z;
    }

    public Vector3f cross_prod(Vector3f a, Vector3f b) {
        Vector3f res = new Vector3f(
                a.y * b.z - b.y * a.z,
                b.x * a.z - a.x * b.z,
                a.x * b.y - b.x * a.y);
        return res;
    }

    public float laplWviscosity(float r, float h) /* Viscosity Kernel نواة اللزوجة */ {
        float w = 0.0f;
        if (r <= h) {
            float h2 = h * h;
            float h6 = h2 * h2 * h2;
            float h9 = h * h2 * h6;

            w = 45.0f / ((float) Math.PI * h6) * (h - r);
        }
        return w;
    }

    public float Wspiky(float r, float h) /*  Spiky Kernel تحسب قوةالضغط */ {
        float w = 0.0f;
        if (r <= h) {
            float h2 = h * h;
            float h6 = h2 * h2 * h2;
            float h9 = h * h2 * h6;

            float hr = h - r;
            float hr3 = hr * hr * hr;
            w = -15.0f / (((float) Math.PI) * h6) * hr3;
        }
        return w;
    }

    public float gradWspiky(float r, float h) /*  Spiky Kernel تحسب قوةالضغط */ {
        float w = 0.0f;
        if (r <= h) {
            float h2 = h * h;
            float h6 = h2 * h2 * h2;
            float h9 = h * h2 * h6;

            float hr = h - r;
            float hr2 = hr * hr;
            w = -45.0f / (((float) Math.PI) * h6) * hr2;
        }
        return w;
    }

    public float W_default(float r, float h) /* Poly6 Kernel لحسساب الكثافة   */ {
        /// Poly6 Kernel لحسساب الكثافة
        float w = 0.0f;
        if (r <= h) {
            float h2 = h * h;
            float h6 = h2 * h2 * h2;
            float h9 = h * h2 * h6;

            float r2 = r * r;
            float hr3 = h2 - r2;
            hr3 = hr3 * hr3 * hr3;
            w = 315.0f / (64.0f * ((float) Math.PI) * h9) * hr3;
        }
        return w;
    }

    public float gradW_default(float r, float h) {
        float w = 0.0f;
        if (r <= h) {
            float h2 = h * h;
            float h6 = h2 * h2 * h2;
            float h9 = h * h2 * h6;

            float r2 = r * r;
            float hr2 = h2 - r2;
            hr2 = hr2 * hr2;
            w = -945.0f / (32.0f * ((float) Math.PI) * h9) * r * hr2;
        }
        return w;
    }

    public float laplW_default(float r, float h) {
        float w = 0.0f;
        if (r <= h) {
            float h2 = h * h;
            float h6 = h2 * h2 * h2;
            float h9 = h * h2 * h6;

            float r2 = r * r;
            float hr = h2 - r2;
            float hr2 = hr * hr;
            w = -945.0f / (32.0f * ((float) Math.PI) * h9) * hr2 * (3.0f * h2 - 7.0f * r2);
        }
        return w;
    }

    public void CalcOilOnly() {
        for (int i = 0; i < numParticles; i++) {
            if ("Oil".equals(Particle[i].Type)) {
                double Yt = Calc_Yt((OilParticles) Particle[i], 0);
                double Yt_1 = Calc_Yt((OilParticles) Particle[i], timestep);

                double Et = Calc_Et((OilParticles) Particle[i], 0);
                double Et_1 = Calc_Et((OilParticles) Particle[i], timestep);

                double temp1 = Particle[i].Density * (Yt_1 - Yt);
                double temp2 = ((OilParticles) Particle[i]).C3 * (Et * (1 - Yt) - Et_1 * (1 - Yt_1));
                double temp3 = 998.29f * (Yt - Yt_1);
                Particle[i].Density += temp1 + temp2 + temp3;

                //// Change Viscosity
                double c3 = ((OilParticles) Particle[i]).C3;
                double c4 = ((OilParticles) Particle[i]).C4;
                double viscosity = Particle[i].Viscosity_Coefficient;

                double temp4 = c4 * Et + (2.5f * Yt) / (1 - c3 * Yt);
                double temp5 = c4 * Et_1 + (2.5f * Yt_1) / (1 - c3 * Yt_1);

                Particle[i].Viscosity_Coefficient += viscosity * (Math.exp(temp4) - Math.exp(temp5));
            }

        }

    }

    public float Calc_Yt(OilParticles oil, double ok) {
        float res = 0.0f;
        float temp1 = (-0.02f / oil.C3);
        float temp2 = (1 + SpeenWind) * (1 + SpeenWind);
        res = oil.C3 * (1 - (float) Math.exp(temp1 * temp2 * (timestep + CurrentTime - ok)));
        return res;
    }

    public float Calc_Et(OilParticles oil, double ok) {
        float res = (oil.C1 + 0.045f * (Temperature - 273.15f)) * (float) Math.log((timestep + CurrentTime - ok) / 60);
        return res;
    }

    public void Calc_Density() {

        for (int i = 0; i < numParticles; i++) {
            float rho = 0.0f;
            for (int j = 0; j < numParticles; j++) {
                float r = Particle[i].dest(Particle[j].get_Position());
                float w = W_default(r, Particle[j].get_h());
                rho += Particle[j].get_Mass() * w;
            }
            Particle[i].set_densities(rho);
        }
    }

    public void Calc_Pressure() {

        for (int i = 0; i < numParticles; i++) {
            float roi, roj;
            roi = Particle[i].get_densities();
            Vector3f ri = new Vector3f(0, 0, 0);
            ri = Particle[i].get_Position();
            float pi = Particle[i].get_k() * (Particle[i].get_densities() - Particle[i].get_rho_0()); /// قانون الضغط  /// صفحة 15  ///
            Vector3f fpressure = new Vector3f(0, 0, 0);
            for (int j = 0; j < numParticles; j++) {
                if (i == j) {
                    continue;
                }
                roj = Particle[j].get_densities();
                Vector3f rj = new Vector3f();
                rj = Particle[j].get_Position();
                float pj = Particle[j].get_k() * (Particle[j].get_densities() - Particle[j].get_rho_0()); /// قانون الضغط  /// صفحة 15 
                Vector3f r = new Vector3f(0, 0, 0);
                r.x = ri.x - rj.x;
                r.y = ri.y - rj.y;
                r.z = ri.z - rj.z;
                float magr = (float) Math.sqrt(r.x * r.x + r.y * r.y + r.z * r.z);
                float mfp = -Particle[j].get_Mass() * ((pi / (roi * roi) + pj / (roj * roj))) * gradWspiky(magr, Particle[j].get_h()); /// 15 صفحة مراجعة 
                mfp *= Particle[j].get_densities();
                Normalize_Vector(r, r);
                fpressure.x += mfp * r.x;
                fpressure.y += mfp * r.y;
                fpressure.z += mfp * r.z;
            }

            force_total[i].x += fpressure.x;
            force_total[i].y += fpressure.y;
            force_total[i].z += fpressure.z;
        }
    }

    public void Calc_Visocity() {

        for (int i = 0; i < numParticles; i++) {
            Vector3f vi = new Vector3f(0, 0, 0);
            vi = Particle[i].get_Velocitie();
            Vector3f fviscosity = new Vector3f(0, 0, 0);
            for (int j = 0; j < numParticles; j++) {
                Vector3f vj = new Vector3f(0, 0, 0);
                vj = Particle[j].get_Velocitie();
                Vector3f vdiff = new Vector3f();
                vdiff.x = vj.x - vi.x;
                vdiff.y = vj.y - vi.y;
                vdiff.z = vj.z - vi.z;
                float r = Particle[i].dest(Particle[j].get_Position());
                float tmp = Particle[j].get_Viscosity_Coefficient() * Particle[j].get_Mass() * laplWviscosity(r, Particle[j].get_h()) / Particle[j].get_densities();
                fviscosity.x += vdiff.x * tmp;
                fviscosity.y += vdiff.y * tmp;
                fviscosity.z += vdiff.z * tmp;
            }
            force_total[i].x += fviscosity.x;
            force_total[i].y += fviscosity.y;
            force_total[i].z += fviscosity.z;
        }
    }

    public void Calc_Gravity() {
        for (int i = 0; i < numParticles; i++) {
            force_total[i].x += Gravity.x * Particle[i].get_densities();
            force_total[i].y += Gravity.y * Particle[i].get_densities();
            force_total[i].z += Gravity.z * Particle[i].get_densities();
        }
    }

    public void Calc_Surface_tension() {
        for (int i = 0; i < numParticles; i++) {
            Vector3f gradcolor = new Vector3f(0, 0, 0);
            float laplcolor = 0.0f;
            Vector3f ri = new Vector3f(0, 0, 0);
            ri = Particle[i].get_Position();
            for (int j = 0; j < numParticles; j++) {
                Vector3f rj = new Vector3f(0, 0, 0);
                rj = Particle[j].get_Position();
                Vector3f r = new Vector3f();
                r.x = ri.x - rj.x;
                r.y = ri.y - rj.y;
                r.z = ri.z - rj.z;
                float r2 = r.x * r.x + r.y * r.y + r.z * r.z;
                float magr = (float) Math.sqrt(r2);
                float maggradcolor = Particle[j].get_Mass() * gradW_default(magr, Particle[j].get_h()) / Particle[j].get_densities();
                Vector3f nr = new Vector3f();
                Normalize_Vector(r, nr);
                nr.x *= maggradcolor;
                nr.y *= maggradcolor;
                nr.z *= maggradcolor;
                gradcolor.x += nr.x;
                gradcolor.y += nr.y;
                gradcolor.z += nr.z;
                laplcolor += Particle[j].get_Mass() * laplW_default(magr, Particle[j].get_h()) / Particle[j].get_densities();
            }
            Normalize_Vector(gradcolor, gradcolor);
            float tmp = -Particle[i].get_Surface_Tension() * laplcolor;
            force_total[i].x += tmp * gradcolor.x;
            force_total[i].y += tmp * gradcolor.y;
            force_total[i].z += tmp * gradcolor.z;
        }
    }

    public void Update_Velocity_AdvectParticles() {
        for (int i = 0; i < numParticles; i++) {
            if (!Particle[i].get_Type().equals("Solid")) {
                Vector3f particle = new Vector3f(0, 0, 0);
                particle = Particle[i].get_Position();
                float sc = timestep / Particle[i].get_densities();
                Vector3f velocity = new Vector3f(0, 0, 0);
                velocity = Particle[i].get_Velocitie();
                velocity.x += force_total[i].x * sc;
                velocity.y += force_total[i].y * sc;
                velocity.z += force_total[i].z * sc;

                particle.x = particle.x + velocity.x * timestep;
                particle.y = particle.y + velocity.y * timestep;
                particle.z = particle.z + velocity.z * timestep;
                Particle[i].set_Position(particle);
                Particle[i].set_Velocitie(velocity);
            }
        }
        if (NumberSolid > 0) {
            for (int i = 0; i < NumberSolid; i++) {
                get_Solid(i);
                updata_Solid(i);
            }
        }
    }

    float DEG_to_RAD(float t) {
        float res = t * ((float) Math.PI);
        res /= 180;
        return res;
    }

    float RAD_to_DEG(float t) {
        float res = t * 180;
        res /= ((float) Math.PI);
        return res;
    }

    public void updata_Solid(int ind) {
        float teta = 0;
        teta = W.z * timestep;
        ///  teta = DEG_to_RAD(teta);
        if(!booldegreeChange)
        {
             if (!booldegree) {
                    teta = DEG_to_RAD(degree);
                } else {
                    teta = 0;
                }
             booldegree = true;
        }
        

        System.out.println(RAD_to_DEG(teta));
        for (int i = 0; i < numParticles; i++) {
            if (Particle[i].get_Type().equals("Solid") && ((Solid) Particle[i]).num == ind) {
                float x = ri[i].x * (float) Math.cos(teta) - ri[i].y * (float) Math.sin(teta);
                float y = ri[i].y * (float) Math.cos(teta) + ri[i].x * (float) Math.sin(teta);
                Vector3f vec = new Vector3f(x, y, 0);
                Particle[i].Position.x = Xcm.x + vec.x;
                Particle[i].Position.y = Xcm.y + vec.y;
                Particle[i].Position.z = Xcm.z + vec.z;
                vec = cross_prod(ri[i], W);
                Particle[i].Velocitie.x = Vcm.x + vec.x;
                Particle[i].Velocitie.y = Vcm.y + vec.y;
                Particle[i].Velocitie.z = Vcm.z + vec.z;
            }
        }
    }

    public void Solid_Power() {
        if (getSolid) {
            return;
        }
        /// الضغط
        int[] Volume = new int[100000];
        for (int i = 0; i < numParticles; i++) {
            if (Particle[i].get_Type().equals("Solid")) {
                Vector3f ri = new Vector3f(0, 0, 0);
                ri = Particle[i].get_Position();
                Volume[i] = 0;
                for (int j = 0; j < numParticles; j++) {
                    if (!Particle[j].get_Type().equals("Solid")) {
                        Vector3f rj = Particle[j].get_Position();
                        Vector3f r = new Vector3f(0, 0, 0);
                        r.x = ri.x - rj.x;
                        r.y = ri.y - rj.y;
                        r.z = ri.z - rj.z;
                        float magr = (float) Math.sqrt(r.x * r.x + r.y * r.y + r.z * r.z);
                        float W = Wspiky(magr, Particle[j].get_h());
                        Volume[i] += W;
                    }
                }
                if (Volume[i] != 0) {
                    Volume[i] = 1 / Volume[i];
                }
            }
        }
        for (int i = 0; i < numParticles; i++) {
            Vector3f ri = new Vector3f(0, 0, 0);
            ri = Particle[i].get_Position();
            if (Particle[i].get_Type().equals("Solid")) {
                for (int j = 0; j < numParticles; j++) {
                    if (!Particle[j].get_Type().equals("Solid")) {
                        Vector3f rj = Particle[j].get_Position();
                        Vector3f r = new Vector3f(0, 0, 0);
                        r.x = ri.x - rj.x;
                        r.y = ri.y - rj.y;
                        r.z = ri.z - rj.z;

                        float magr = (float) Math.sqrt(r.x * r.x + r.y * r.y + r.z * r.z);
                        float press = Particle[j].get_k() * (Particle[j].get_densities() - Particle[j].get_rho_0());
                        float dens = Particle[j].get_densities();
                        float W = gradWspiky(magr, Particle[j].get_h());
                        float vol = Volume[i];
                        float power = -Particle[j].get_Mass() * Particle[j].get_rho_0() * vol * (press / (dens * dens)) * W;
                        Normalize_Vector(r, r);
                        force_total[i].x += power * r.x;
                        force_total[i].y += power * r.y;
                        force_total[i].z += power * r.z;

                        force_total[j].x -= power * r.x;
                        force_total[j].y -= power * r.y;
                        force_total[j].z -= power * r.z;
                    }
                }
            }
        }
        /// اللزوجة
        for (int i = 0; i < numParticles; i++) {
            if (Particle[i].get_Type().equals("Solid")) {
                Vector3f vi = new Vector3f(0, 0, 0);
                vi = Particle[i].get_Velocitie();
                for (int j = 0; j < numParticles; j++) {
                    if (!Particle[i].get_Type().equals("Solid")) {
                        Vector3f vj = new Vector3f(0, 0, 0);
                        vj = Particle[j].get_Velocitie();
                        Vector3f vdiff = new Vector3f();
                        vdiff.x = vj.x - vi.x;
                        vdiff.y = vj.y - vi.y;
                        vdiff.z = vj.z - vi.z;
                        float r = Particle[i].dest(Particle[j].get_Position());
                        float tmp = Particle[j].get_Viscosity_Coefficient() * (Particle[j].get_Mass() / Particle[j].get_densities()) * laplWviscosity(r, Particle[j].get_h()) / Particle[j].get_densities();
                        force_total[i].x += vdiff.x * tmp;
                        force_total[i].y += vdiff.y * tmp;
                        force_total[i].z += vdiff.z * tmp;

                        force_total[j].x -= vdiff.x * tmp;
                        force_total[j].y -= vdiff.y * tmp;
                        force_total[j].z -= vdiff.z * tmp;
                    }
                }
            }
        }
    }

    public void get_Solid(int ind) {
        Tao = new Vector3f(0, 0, 0);
        M = 0;
        float densities = 0;
        I = 0;
        Xcm = new Vector3f(0, 0, 0);
        F = new Vector3f(0, 0, 0);
        Tao = new Vector3f(0, 0, 0);
        L = new Vector3f(0, 0, 0);
        W = new Vector3f(0, 0, 0);
        Calc_F();
        double maxy = -1e9;
        for (int i = 0; i < numParticles; i++) {
            if (Particle[i].get_Type().equals("Solid") && ((Solid) Particle[i]).num == ind) {
                M += Particle[i].get_densities();
                densities += Particle[i].get_densities();
                Xcm.x += Particle[i].get_densities() * Particle[i].get_X();
                Xcm.y += Particle[i].get_densities() * Particle[i].get_Y();
                Xcm.z += Particle[i].get_densities() * Particle[i].get_Z();
            } else {
                if (Particle[i].Position.y > maxy && !Particle[i].get_Type().equals("Solid")) {
                    maxy = Particle[i].Position.y;
                }
            }
        }
        double vol_sold_in_water = 0;
        double rho_mid = 0, cn = 0;
        for (int i = 0; i < numParticles; i++) {
            if (Particle[i].get_Type().equals("Solid") && ((Solid) Particle[i]).num == ind) {
                if (Particle[i].Position.y <= maxy) {
                    vol_sold_in_water++;
                }
            } else if (!Particle[i].get_Type().equals("Solid")) {
                cn++;
                rho_mid += Particle[i].Density;
            }
        }
        if (cn > 0) {
            rho_mid = rho_mid / cn;
            F.y -= (vol_sold_in_water * Gravity.y * rho_mid);
        }
        Xcm.x /= M;
        Xcm.y /= M;
        Xcm.z /= M;
        for (int i = 0; i < numParticles; i++) {
            if (Particle[i].get_Type().equals("Solid") && ((Solid) Particle[i]).num == ind) {
                ri[i] = new Vector3f(Particle[i].get_X() - Xcm.x, Particle[i].get_Y() - Xcm.y, Particle[i].get_Z() - Xcm.z);
                I += Particle[i].get_densities() * Length_Vector(ri[i]) * Length_Vector(ri[i]);
                F.x += force_total[i].x;
                F.y += force_total[i].y;
                F.z += force_total[i].z;
                Vector3f temp = cross_prod(ri[i], force_total[i]);
                Tao.x += temp.x;
                Tao.y += temp.y;
                Tao.z += temp.z;
            }
        }
        float sc = timestep / densities;
        Vcm.x = F.x * sc;
        Vcm.y = F.y * sc;
        Vcm.z = F.z * sc;
        Xcm.x = Xcm.x + Vcm.x * timestep;
        Xcm.y = Xcm.y + Vcm.y * timestep;
        Xcm.z = Xcm.z + Vcm.z * timestep;
        L.x += Tao.x * timestep;
        L.y += Tao.y * timestep;
        L.z += Tao.z * timestep;
        W.x = L.x / I;
        W.y = L.y / I;
        W.z = L.z / I;

    }

    public void Calc_Boundary() {

        for (int i = 0; i < NumberSolid; i++) {
            if (BoolForTybeCircle) {
                Calc_Boundary_Circle_Solid(i);
            } else {
                Calc_Boundary_Tube_Solid(i);
            }
        }

        for (int i = 0; i < numParticles; i++) {
            Vector3f particle = new Vector3f(0, 0, 0);
            particle = Particle[i].get_Position();
            Vector3f velocity = new Vector3f(0, 0, 0);
            velocity = Particle[i].get_Velocitie();
            if (BoolForTybeCircle) {
                if (!Particle[i].get_Type().equals("Solid")) {
                    Calc_Boundary_Circle_NotSolid(i, particle, velocity);
                }
            } else {
                if (!Particle[i].get_Type().equals("Solid")) {
                    Calc_Boundary_Tube_NotSolid(i, particle, velocity);
                }
            }
        }
    }

    public void Calc_Boundary_Circle_NotSolid(int i, Vector3f particle, Vector3f velocity) {
        float damping = 0.5f;
        float d = Length_Vector(particle);
        if (d > R_circle) {
            Vector3f vec = new Vector3f(0, 0, 0);
            Normalize_Vector(particle, vec);
            particle.x = vec.x * R_circle;
            particle.y = vec.y * R_circle;
            particle.z = vec.z * R_circle;

            vec.x *= -1;
            vec.y *= -1;
            vec.z *= -1;
            float temp = (d - R_circle) / (timestep * Length_Vector(velocity));
            temp *= Particle[i].get_Restitution_CR();
            float dot = Dot_Vector(velocity, vec);
            //            System.out.println("befor     " + velocity.x + "   " + velocity.y + "   " + velocity.z);

            velocity.x = velocity.x - (1 + temp) * dot * vec.x;
            velocity.y = velocity.y - (1 + temp) * dot * vec.y;
            velocity.z = velocity.z - (1 + temp) * dot * vec.z;
            Particle[i].set_Position(particle);
            Particle[i].set_Velocitie(velocity);
        }
    }

    public void Calc_Boundary_Tube_NotSolid(int i, Vector3f particle, Vector3f velocity) {
        float damping = 1f;
        float w = width / 2 - 0.01f, l = length / 2 - 0.01f, h = height / 2 - 0.01f;
        if (particle.x < -w) {
            particle.x = -w;
            velocity.x = -damping * velocity.x;
        }
        if (particle.x > w) {
            particle.x = w;
            velocity.x = -damping * velocity.x;
        }
        if (particle.y < -l) {
            particle.y = -l;
            velocity.y = -damping * velocity.y;
        }
        if (particle.y > l) {
            particle.y = l;
            velocity.y = -damping * velocity.y;
        }
        if (particle.z < -h) {
            particle.z = -h;
            velocity.z = -damping * velocity.z;
        }
        if (particle.z > h) {
            particle.z = h;
            velocity.z = -damping * velocity.z;

        }
        Particle[i].set_Position(particle);
        Particle[i].set_Velocitie(velocity);
    }

    public void Calc_Boundary_Circle_Solid(int j) {
        get_Solid(j);
        float d = Length_Vector(Xcm);
        R_circle -= 0.01f;
        if (d > R_circle) {
            Vector3f vec = new Vector3f(0, 0, 0);
            Normalize_Vector(Xcm, vec);
            Xcm.x = vec.x * R_circle;
            Xcm.y = vec.y * R_circle;
            Xcm.z = vec.z * R_circle;

            vec.x *= -1;
            vec.y *= -1;
            vec.z *= -1;
            float temp = (d - R_circle) / (timestep * Length_Vector(Vcm));
            temp *= 0;
            float dot = Dot_Vector(Vcm, vec);
            Vcm.x = Vcm.x - (1 + temp) * dot * vec.x;
            Vcm.y = Vcm.y - (1 + temp) * dot * vec.y;
            Vcm.z = Vcm.z - (1 + temp) * dot * vec.z;
            updata_Solid(j);
        }
        R_circle += 0.01f;
    }

    public void Calc_Boundary_Tube_Solid(int j) {
        get_Solid(j);
        float MaxRight_Solid = -1000, MaxUp_Solid = -1000, MaxLeft_Solid = 1000, MaxDown_Solid = 1000, MaxDist_0 = 0;
        int IndexMaxRight = -1, IndexMaxLeft = -1, IndexMaxUp = -1, IndexMaxDown = -1, IndexMaxDist = -1;
        for (int i = 0; i < numParticles; i++) {
            if (Particle[i].get_Type().equals("Solid") && ((Solid) Particle[i]).num == j) {
                if (Length_Vector(Particle[i].get_Position()) > MaxDist_0) {
                    MaxDist_0 = Length_Vector(Particle[i].get_Position());
                    IndexMaxDist = i;
                }
                if (MaxRight_Solid < Particle[i].get_X()) {
                    MaxRight_Solid = Math.max(MaxRight_Solid, Particle[i].get_X());
                    IndexMaxRight = i;
                }
                if (MaxUp_Solid < Particle[i].get_Y()) {
                    System.out.println("dfdfdfd");
                    MaxUp_Solid = Math.max(MaxUp_Solid, Particle[i].get_Y());
                    IndexMaxUp = i;
                }
                if (MaxLeft_Solid > Particle[i].get_X()) {
                    MaxLeft_Solid = Math.min(MaxLeft_Solid, Particle[i].get_X());
                    IndexMaxLeft = i;
                }
                if (MaxDown_Solid > Particle[i].get_Y()) {
                    MaxDown_Solid = Math.min(MaxDown_Solid, Particle[i].get_Y());
                    IndexMaxDown = i;
                }
            }
        }
        float damping = 0.5f;
        float w = width / 2 - r, l = length / 2 - r, h = height / 2 - r;
        if (MaxLeft_Solid < -w) {
            float t = (float) Math.abs(ri[IndexMaxLeft].x);
            Xcm.x = -w + t;
            Vcm.x = -damping * Vcm.x;
        }
        if (MaxRight_Solid > w) {
            float t = (float) Math.abs(ri[IndexMaxRight].x);
            Xcm.x = w - t;
            Vcm.x = -damping * Vcm.x;
        }
        if (MaxDown_Solid < -l) {
            float t = (float) Math.abs(ri[IndexMaxDown].y);
            Xcm.y = -l + t;
            Vcm.y = -damping * Vcm.y;
        }
        if (MaxUp_Solid > l) {
            System.out.println(ri[IndexMaxUp].y);
            float t = (float) Math.abs(ri[IndexMaxUp].y);
            Xcm.y = l - t;
            Vcm.y = -damping * Vcm.y;
        }
        if (Xcm.z < -h) {
            Xcm.z = -h;
            Vcm.z = -damping * Vcm.z;
        }
        if (Xcm.z > h) {
            Xcm.z = h;
            Vcm.z = -damping * Vcm.z;

        }
        updata_Solid(j);
    }
}
