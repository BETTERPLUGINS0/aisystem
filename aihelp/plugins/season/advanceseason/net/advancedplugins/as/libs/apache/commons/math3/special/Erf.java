/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.as.libs.apache.commons.math3.special;

import net.advancedplugins.as.libs.apache.commons.math3.special.Gamma;
import net.advancedplugins.as.libs.apache.commons.math3.util.FastMath;

public class Erf {
    private static final double X_CRIT = 0.4769362762044697;

    private Erf() {
    }

    public static double erf(double d) {
        if (FastMath.abs(d) > 40.0) {
            return d > 0.0 ? 1.0 : -1.0;
        }
        double d2 = Gamma.regularizedGammaP(0.5, d * d, 1.0E-15, 10000);
        return d < 0.0 ? -d2 : d2;
    }

    public static double erfc(double d) {
        if (FastMath.abs(d) > 40.0) {
            return d > 0.0 ? 0.0 : 2.0;
        }
        double d2 = Gamma.regularizedGammaQ(0.5, d * d, 1.0E-15, 10000);
        return d < 0.0 ? 2.0 - d2 : d2;
    }

    public static double erf(double d, double d2) {
        if (d > d2) {
            return -Erf.erf(d2, d);
        }
        return d < -0.4769362762044697 ? (d2 < 0.0 ? Erf.erfc(-d2) - Erf.erfc(-d) : Erf.erf(d2) - Erf.erf(d)) : (d2 > 0.4769362762044697 && d > 0.0 ? Erf.erfc(d) - Erf.erfc(d2) : Erf.erf(d2) - Erf.erf(d));
    }

    public static double erfInv(double d) {
        double d2;
        double d3 = -FastMath.log((1.0 - d) * (1.0 + d));
        if (d3 < 6.25) {
            d2 = -3.64441206401782E-21;
            d2 = -1.6850591381820166E-19 + d2 * (d3 -= 3.125);
            d2 = 1.28584807152564E-18 + d2 * d3;
            d2 = 1.1157877678025181E-17 + d2 * d3;
            d2 = -1.333171662854621E-16 + d2 * d3;
            d2 = 2.0972767875968562E-17 + d2 * d3;
            d2 = 6.637638134358324E-15 + d2 * d3;
            d2 = -4.054566272975207E-14 + d2 * d3;
            d2 = -8.151934197605472E-14 + d2 * d3;
            d2 = 2.6335093153082323E-12 + d2 * d3;
            d2 = -1.2975133253453532E-11 + d2 * d3;
            d2 = -5.415412054294628E-11 + d2 * d3;
            d2 = 1.0512122733215323E-9 + d2 * d3;
            d2 = -4.112633980346984E-9 + d2 * d3;
            d2 = -2.9070369957882005E-8 + d2 * d3;
            d2 = 4.2347877827932404E-7 + d2 * d3;
            d2 = -1.3654692000834679E-6 + d2 * d3;
            d2 = -1.3882523362786469E-5 + d2 * d3;
            d2 = 1.8673420803405714E-4 + d2 * d3;
            d2 = -7.40702534166267E-4 + d2 * d3;
            d2 = -0.006033670871430149 + d2 * d3;
            d2 = 0.24015818242558962 + d2 * d3;
            d2 = 1.6536545626831027 + d2 * d3;
        } else if (d3 < 16.0) {
            d3 = FastMath.sqrt(d3) - 3.25;
            d2 = 2.2137376921775787E-9;
            d2 = 9.075656193888539E-8 + d2 * d3;
            d2 = -2.7517406297064545E-7 + d2 * d3;
            d2 = 1.8239629214389228E-8 + d2 * d3;
            d2 = 1.5027403968909828E-6 + d2 * d3;
            d2 = -4.013867526981546E-6 + d2 * d3;
            d2 = 2.9234449089955446E-6 + d2 * d3;
            d2 = 1.2475304481671779E-5 + d2 * d3;
            d2 = -4.7318229009055734E-5 + d2 * d3;
            d2 = 6.828485145957318E-5 + d2 * d3;
            d2 = 2.4031110387097894E-5 + d2 * d3;
            d2 = -3.550375203628475E-4 + d2 * d3;
            d2 = 9.532893797373805E-4 + d2 * d3;
            d2 = -0.0016882755560235047 + d2 * d3;
            d2 = 0.002491442096107851 + d2 * d3;
            d2 = -0.003751208507569241 + d2 * d3;
            d2 = 0.005370914553590064 + d2 * d3;
            d2 = 1.0052589676941592 + d2 * d3;
            d2 = 3.0838856104922208 + d2 * d3;
        } else if (!Double.isInfinite(d3)) {
            d3 = FastMath.sqrt(d3) - 5.0;
            d2 = -2.7109920616438573E-11;
            d2 = -2.555641816996525E-10 + d2 * d3;
            d2 = 1.5076572693500548E-9 + d2 * d3;
            d2 = -3.789465440126737E-9 + d2 * d3;
            d2 = 7.61570120807834E-9 + d2 * d3;
            d2 = -1.496002662714924E-8 + d2 * d3;
            d2 = 2.914795345090108E-8 + d2 * d3;
            d2 = -6.771199775845234E-8 + d2 * d3;
            d2 = 2.2900482228026655E-7 + d2 * d3;
            d2 = -9.9298272942317E-7 + d2 * d3;
            d2 = 4.526062597223154E-6 + d2 * d3;
            d2 = -1.968177810553167E-5 + d2 * d3;
            d2 = 7.599527703001776E-5 + d2 * d3;
            d2 = -2.1503011930044477E-4 + d2 * d3;
            d2 = -1.3871931833623122E-4 + d2 * d3;
            d2 = 1.0103004648645344 + d2 * d3;
            d2 = 4.849906401408584 + d2 * d3;
        } else {
            d2 = Double.POSITIVE_INFINITY;
        }
        return d2 * d;
    }

    public static double erfcInv(double d) {
        return Erf.erfInv(1.0 - d);
    }
}

