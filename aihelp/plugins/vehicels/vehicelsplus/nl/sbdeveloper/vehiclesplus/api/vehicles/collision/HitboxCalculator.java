/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.util.Vector
 */
package nl.sbdeveloper.vehiclesplus.api.vehicles.collision;

import java.util.ArrayList;
import java.util.List;
import nl.sbdeveloper.vehiclesplus.api.vehicles.collision.HitboxPoint;
import nl.sbdeveloper.vehiclesplus.api.vehicles.collision.HitboxResult;
import nl.sbdeveloper.vehiclesplus.api.vehicles.collision.HitboxSide;
import nl.sbdeveloper.vehiclesplus.api.vehicles.settings.impl.Hitbox;
import org.bukkit.util.Vector;

public class HitboxCalculator {
    private HitboxCalculator() {
    }

    public static HitboxResult calculate(Hitbox hitbox, double d) {
        double d2;
        double d3;
        double d4 = hitbox.getLength() * 2.0;
        double d5 = hitbox.getWidth() + 1.0;
        double d6 = hitbox.getHeight() * 2.0 - 0.5;
        double d7 = d4 / 2.0;
        double d8 = d5 / 2.0;
        double d9 = Math.toRadians(d);
        double d10 = Math.sin(d9);
        double d11 = Math.cos(d9);
        Vector vector = new Vector(d11, 0.0, d10);
        Vector vector2 = new Vector(0, 1, 0);
        Vector vector3 = new Vector(-d10, 0.0, d11);
        Vector vector4 = HitboxCalculator.combine(vector, vector2, vector3, -d8, 0.0, -d7);
        Vector vector5 = HitboxCalculator.combine(vector, vector2, vector3, d8, 0.0, -d7);
        Vector vector6 = HitboxCalculator.combine(vector, vector2, vector3, -d8, d6, -d7);
        Vector vector7 = HitboxCalculator.combine(vector, vector2, vector3, -d8, 0.0, d7);
        ArrayList<HitboxPoint> arrayList = new ArrayList<HitboxPoint>();
        for (d3 = 0.5; d3 < d6; d3 += 0.5) {
            for (d2 = 0.5; d2 < d5; d2 += 0.5) {
                arrayList.add(new HitboxPoint(HitboxCalculator.combine(vector4, vector2, vector, 1.0, d3, d2), HitboxSide.BACK));
                arrayList.add(new HitboxPoint(HitboxCalculator.combine(vector7, vector2, vector, 1.0, d3, d2), HitboxSide.FRONT));
            }
            for (d2 = 0.0; d2 <= d4; d2 += 0.5) {
                arrayList.add(new HitboxPoint(HitboxCalculator.combine(vector4, vector2, vector3, 1.0, d3, d2), HitboxSide.LEFT));
                arrayList.add(new HitboxPoint(HitboxCalculator.combine(vector5, vector2, vector3, 1.0, d3, d2), HitboxSide.RIGHT));
            }
        }
        for (d3 = 0.0; d3 <= d4; d3 += 0.5) {
            for (d2 = 0.0; d2 <= d5; d2 += 0.5) {
                arrayList.add(new HitboxPoint(HitboxCalculator.combine(vector4, vector, vector3, 1.0, d2, d3), HitboxSide.BOTTOM));
                arrayList.add(new HitboxPoint(HitboxCalculator.combine(vector6, vector, vector3, 1.0, d2, d3), HitboxSide.TOP));
            }
        }
        List list = List.of((Object)vector4, (Object)vector5, (Object)HitboxCalculator.combine(vector, vector2, vector3, d8, 0.0, d7), (Object)vector7, (Object)vector6, (Object)HitboxCalculator.combine(vector, vector2, vector3, d8, d6, -d7), (Object)HitboxCalculator.combine(vector, vector2, vector3, d8, d6, d7), (Object)HitboxCalculator.combine(vector, vector2, vector3, -d8, d6, d7));
        return new HitboxResult(arrayList, list);
    }

    public static Vector combine(Vector vector, Vector vector2, Vector vector3, double d, double d2, double d3) {
        return new Vector(vector.getX() * d + vector2.getX() * d2 + vector3.getX() * d3, vector.getY() * d + vector2.getY() * d2 + vector3.getY() * d3, vector.getZ() * d + vector2.getZ() * d2 + vector3.getZ() * d3);
    }

    public static boolean hasNoCollision(List<Vector> list, List<Vector> list2) {
        return HitboxCalculator.isSeparate(list, list2) || HitboxCalculator.isSeparate(list2, list);
    }

    private static boolean isSeparate(List<Vector> list, List<Vector> list2) {
        block0: for (int i = 0; i < list.size(); ++i) {
            int n = i + 1 < list.size() ? i + 1 : 0;
            Vector vector = list.get(i).getCrossProduct(list.get(n));
            for (Vector vector2 : list2) {
                if (!(vector.dot(vector2) >= 0.0)) continue;
                continue block0;
            }
            return true;
        }
        return false;
    }
}

