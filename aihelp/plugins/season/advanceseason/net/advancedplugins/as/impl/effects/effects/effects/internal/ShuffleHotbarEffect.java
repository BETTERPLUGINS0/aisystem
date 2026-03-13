/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.as.impl.effects.effects.effects.internal;

import java.lang.invoke.CallSite;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.effects.AdvancedEffect;
import net.advancedplugins.as.impl.utils.ASManager;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class ShuffleHotbarEffect
extends AdvancedEffect {
    public ShuffleHotbarEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "SHUFFLE_HOTBAR", "Shuffle player's hotbar", "%e");
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        if (!(livingEntity instanceof Player)) {
            return false;
        }
        Player player = (Player)livingEntity;
        ArrayList<CallSite> arrayList = new ArrayList<CallSite>();
        ArrayList<ItemStack> arrayList2 = new ArrayList<ItemStack>();
        for (int i = 0; i < 9; ++i) {
            arrayList.add((CallSite)((Object)("" + i)));
            if (player.getInventory().getItem(i) == null) continue;
            arrayList2.add(player.getInventory().getItem(i).clone());
            player.getInventory().setItem(i, new ItemStack(Material.AIR));
        }
        for (ItemStack itemStack : arrayList2) {
            if (itemStack == null) continue;
            int n = ASManager.parseInt((String)arrayList.get(ThreadLocalRandom.current().nextInt(arrayList.size())));
            arrayList.remove("" + n);
            player.getInventory().setItem(n, itemStack);
        }
        return true;
    }
}

