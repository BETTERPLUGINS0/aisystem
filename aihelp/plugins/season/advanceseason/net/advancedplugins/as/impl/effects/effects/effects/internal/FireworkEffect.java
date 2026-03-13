/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Color
 *  org.bukkit.FireworkEffect
 *  org.bukkit.FireworkEffect$Type
 *  org.bukkit.Location
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.inventory.meta.FireworkMeta
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.as.impl.effects.effects.effects.internal;

import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.effects.AdvancedEffect;
import net.advancedplugins.as.impl.utils.ASManager;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class FireworkEffect
extends AdvancedEffect {
    public FireworkEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "FIREWORK", "Spawns a firework", "%e:[COLOR]:[FADE_COLOR]:[TYPE]:[POWER]:[TRAIL]:[FLICKER]");
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        return this.executeEffect(executionTask, livingEntity.getLocation(), stringArray);
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, Location location, String[] stringArray) {
        String[] stringArray2;
        Color color = Color.RED;
        Color color2 = Color.ORANGE;
        FireworkEffect.Type type = FireworkEffect.Type.BALL;
        int n = 1;
        boolean bl = true;
        boolean bl2 = true;
        if (stringArray.length > 0) {
            try {
                if (stringArray[0].contains(",")) {
                    stringArray2 = stringArray[0].split(",");
                    color = Color.fromRGB((int)Integer.parseInt(stringArray2[0]), (int)Integer.parseInt(stringArray2[1]), (int)Integer.parseInt(stringArray2[2]));
                } else {
                    color = this.getColorByName(stringArray[0]);
                }
                if (stringArray.length > 1) {
                    if (stringArray[1].contains(",")) {
                        stringArray2 = stringArray[1].split(",");
                        color2 = Color.fromRGB((int)Integer.parseInt(stringArray2[0]), (int)Integer.parseInt(stringArray2[1]), (int)Integer.parseInt(stringArray2[2]));
                    } else {
                        color2 = this.getColorByName(stringArray[1]);
                    }
                }
                if (stringArray.length > 2) {
                    type = FireworkEffect.Type.valueOf((String)stringArray[2].toUpperCase());
                }
                if (stringArray.length > 3) {
                    n = Math.min(4, Math.max(0, ASManager.parseInt(stringArray[3])));
                }
                if (stringArray.length > 4) {
                    bl = ASManager.parseBoolean(stringArray[4], true);
                }
                if (stringArray.length > 5) {
                    bl2 = ASManager.parseBoolean(stringArray[5], true);
                }
            } catch (IllegalArgumentException illegalArgumentException) {
                executionTask.reportIssue(this.getName(), "Invalid arguments provided");
            }
        }
        stringArray2 = (String[])location.getWorld().spawnEntity(location, EntityType.FIREWORK);
        FireworkMeta fireworkMeta = stringArray2.getFireworkMeta();
        org.bukkit.FireworkEffect fireworkEffect = org.bukkit.FireworkEffect.builder().withColor(color).withFade(color2).flicker(bl2).trail(bl).with(type).build();
        fireworkMeta.addEffect(fireworkEffect);
        fireworkMeta.setPower(n);
        stringArray2.setFireworkMeta(fireworkMeta);
        return true;
    }

    private Color getColorByName(String string) {
        try {
            return switch (string.toUpperCase()) {
                case "RED" -> Color.RED;
                case "BLUE" -> Color.BLUE;
                case "GREEN" -> Color.GREEN;
                case "YELLOW" -> Color.YELLOW;
                case "PURPLE" -> Color.PURPLE;
                case "ORANGE" -> Color.ORANGE;
                case "BLACK" -> Color.BLACK;
                case "WHITE" -> Color.WHITE;
                case "GRAY" -> Color.GRAY;
                case "AQUA" -> Color.AQUA;
                case "LIME" -> Color.LIME;
                case "SILVER" -> Color.SILVER;
                default -> Color.RED;
            };
        } catch (IllegalArgumentException illegalArgumentException) {
            return Color.RED;
        }
    }
}

