/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.entity.BlockDisplay
 *  org.bukkit.entity.Display$Brightness
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.util.Transformation
 *  org.bukkit.util.Vector
 */
package com.magmaguy.magmacore;

import com.magmaguy.magmacore.MagmaCore;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Display;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class DrawLine {
    private DrawLine() {
    }

    public static LineData drawLine(Location start, Location end, float width, Material material, int tickDuration) {
        float height = (float)start.distance(end);
        if (height <= 0.0f) {
            return null;
        }
        float sx = width;
        float sy = height;
        float sz = width;
        Vector dir = end.toVector().subtract(start.toVector()).normalize();
        Vector3f target = new Vector3f((float)dir.getX(), (float)dir.getY(), (float)dir.getZ());
        Quaternionf rotation = new Quaternionf().rotateTo(new Vector3f(0.0f, 1.0f, 0.0f), target);
        Vector mid = start.toVector().add(end.toVector()).multiply(0.5);
        Location spawnLoc = new Location(end.getWorld(), mid.getX(), mid.getY(), mid.getZ());
        Vector3f localCenterOffset = new Vector3f(-sx / 2.0f, -sy / 2.0f, -sz / 2.0f);
        Vector3f worldCenterOffset = rotation.transform(new Vector3f(localCenterOffset));
        spawnLoc.add((double)worldCenterOffset.x, (double)worldCenterOffset.y, (double)worldCenterOffset.z);
        BlockDisplay display = (BlockDisplay)spawnLoc.getWorld().spawn(spawnLoc, BlockDisplay.class, entity -> {
            entity.setBlock(Bukkit.createBlockData((Material)material));
            entity.setInterpolationDuration(0);
            entity.setViewRange(128.0f);
            entity.setBrightness(new Display.Brightness(15, 15));
            entity.setPersistent(false);
            entity.setTransformation(new Transformation(new Vector3f(0.0f, 0.0f, 0.0f), rotation, new Vector3f(sx, sy, sz), new Quaternionf()));
        });
        if (tickDuration > 0) {
            Bukkit.getScheduler().runTaskLater((Plugin)MagmaCore.getInstance().getRequestingPlugin(), () -> ((BlockDisplay)display).remove(), (long)tickDuration);
        }
        return new LineData(display, start, end, width);
    }

    public static void updateLine(LineData lineData, Location start, Location end) {
        float width;
        if (lineData == null || lineData.getDisplay() == null || !lineData.getDisplay().isValid()) {
            return;
        }
        float height = (float)start.distance(end);
        if (height <= 0.0f) {
            return;
        }
        BlockDisplay display = lineData.getDisplay();
        float sx = width = lineData.getWidth();
        float sy = height;
        float sz = width;
        Vector dir = end.toVector().subtract(start.toVector()).normalize();
        Vector3f target = new Vector3f((float)dir.getX(), (float)dir.getY(), (float)dir.getZ());
        Quaternionf rotation = new Quaternionf().rotateTo(new Vector3f(0.0f, 1.0f, 0.0f), target);
        Vector mid = start.toVector().add(end.toVector()).multiply(0.5);
        Location newLoc = new Location(end.getWorld(), mid.getX(), mid.getY(), mid.getZ());
        Vector3f localCenterOffset = new Vector3f(-sx / 2.0f, -sy / 2.0f, -sz / 2.0f);
        Vector3f worldCenterOffset = rotation.transform(new Vector3f(localCenterOffset));
        newLoc.add((double)worldCenterOffset.x, (double)worldCenterOffset.y, (double)worldCenterOffset.z);
        display.teleport(newLoc);
        display.setTransformation(new Transformation(new Vector3f(0.0f, 0.0f, 0.0f), rotation, new Vector3f(sx, sy, sz), new Quaternionf()));
        lineData.updatePositions(start, end);
    }

    public static class LineData {
        private final BlockDisplay display;
        private final float width;
        private Location start;
        private Location end;

        public LineData(BlockDisplay display, Location start, Location end, float width) {
            this.display = display;
            this.start = start.clone();
            this.end = end.clone();
            this.width = width;
        }

        public BlockDisplay getDisplay() {
            return this.display;
        }

        public Location getStart() {
            return this.start.clone();
        }

        public Location getEnd() {
            return this.end.clone();
        }

        public float getWidth() {
            return this.width;
        }

        public void updatePositions(Location newStart, Location newEnd) {
            this.start = newStart.clone();
            this.end = newEnd.clone();
        }

        public void remove() {
            if (this.display != null && this.display.isValid()) {
                this.display.remove();
            }
        }
    }
}

