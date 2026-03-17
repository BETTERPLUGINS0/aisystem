/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.FireworkEffect
 *  org.bukkit.Material
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.FireworkEffectMeta
 *  org.bukkit.inventory.meta.FireworkMeta
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.jetbrains.annotations.Contract
 *  org.jetbrains.annotations.NotNull
 */
package me.zombie_striker.qav.gui.builder.item;

import java.util.Arrays;
import java.util.List;
import me.zombie_striker.qav.gui.builder.item.BaseItemBuilder;
import me.zombie_striker.qav.gui.components.exception.GuiException;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class FireworkBuilder
extends BaseItemBuilder<FireworkBuilder> {
    private static final Material STAR = Material.FIREWORK_STAR;
    private static final Material ROCKET = Material.FIREWORK_ROCKET;

    FireworkBuilder(@NotNull ItemStack itemStack) {
        super(itemStack);
        if (itemStack.getType() != STAR && itemStack.getType() != ROCKET) {
            throw new GuiException("FireworkBuilder requires the material to be a FIREWORK_STAR/FIREWORK_ROCKET!");
        }
    }

    @NotNull
    @Contract(value="_ -> this")
    public FireworkBuilder effect(@NotNull FireworkEffect ... fireworkEffectArray) {
        return this.effect(Arrays.asList(fireworkEffectArray));
    }

    @NotNull
    @Contract(value="_ -> this")
    public FireworkBuilder effect(@NotNull List<FireworkEffect> list) {
        if (list.isEmpty()) {
            return this;
        }
        if (this.getItemStack().getType() == STAR) {
            FireworkEffectMeta fireworkEffectMeta = (FireworkEffectMeta)this.getMeta();
            fireworkEffectMeta.setEffect(list.get(0));
            this.setMeta((ItemMeta)fireworkEffectMeta);
            return this;
        }
        FireworkMeta fireworkMeta = (FireworkMeta)this.getMeta();
        fireworkMeta.addEffects(list);
        this.setMeta((ItemMeta)fireworkMeta);
        return this;
    }

    @NotNull
    @Contract(value="_ -> this")
    public FireworkBuilder power(int n) {
        if (this.getItemStack().getType() == ROCKET) {
            FireworkMeta fireworkMeta = (FireworkMeta)this.getMeta();
            fireworkMeta.setPower(n);
            this.setMeta((ItemMeta)fireworkMeta);
        }
        return this;
    }
}

