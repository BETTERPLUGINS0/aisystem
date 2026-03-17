/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.entity.ArmorStand
 *  org.bukkit.util.Vector
 */
package me.zombie_striker.qav.attachments;

import java.util.List;
import me.zombie_striker.qav.VehicleEntity;
import me.zombie_striker.qav.attachments.Attachment;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.util.Vector;

public class Wheel
extends Attachment {
    public Wheel(String string, int n, Material material, Vector vector) {
        super(string, n, material, vector);
    }

    public Wheel(String string, List<String> list, int n, Material material, Vector vector) {
        super(string, list, n, material, vector);
    }

    @Override
    public void animate(VehicleEntity vehicleEntity, ArmorStand armorStand) {
        armorStand.setHeadPose(armorStand.getHeadPose().add(0.0, 0.0, 0.5));
    }
}

