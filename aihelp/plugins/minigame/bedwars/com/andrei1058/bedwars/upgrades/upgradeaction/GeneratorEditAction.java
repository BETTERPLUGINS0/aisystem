/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package com.andrei1058.bedwars.upgrades.upgradeaction;

import com.andrei1058.bedwars.api.arena.generator.GeneratorType;
import com.andrei1058.bedwars.api.arena.generator.IGenerator;
import com.andrei1058.bedwars.api.arena.team.ITeam;
import com.andrei1058.bedwars.api.upgrades.UpgradeAction;
import com.andrei1058.bedwars.arena.OreGenerator;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GeneratorEditAction
implements UpgradeAction {
    private final int amount;
    private final int delay;
    private final int limit;
    private final ApplyType type;

    public GeneratorEditAction(ApplyType type, int amount, int delay, int limit) {
        this.type = type;
        this.amount = amount;
        this.delay = delay;
        this.limit = limit;
    }

    @Override
    public void onBuy(Player player, ITeam bwt) {
        List<Object> generator = new ArrayList();
        if (this.type == ApplyType.IRON) {
            generator = bwt.getGenerators().stream().filter(g -> g.getType() == GeneratorType.IRON).collect(Collectors.toList());
        } else if (this.type == ApplyType.GOLD) {
            generator = bwt.getGenerators().stream().filter(g -> g.getType() == GeneratorType.GOLD).collect(Collectors.toList());
        } else if (this.type == ApplyType.EMERALD) {
            if (!bwt.getArena().getConfig().getArenaLocations("Team." + bwt.getName() + ".Emerald").isEmpty()) {
                for (Location location : bwt.getArena().getConfig().getArenaLocations("Team." + bwt.getName() + ".Emerald")) {
                    OreGenerator gen = new OreGenerator(location, bwt.getArena(), GeneratorType.CUSTOM, bwt);
                    gen.setOre(new ItemStack(Material.EMERALD));
                    gen.setType(GeneratorType.EMERALD);
                    bwt.getGenerators().add(gen);
                    generator.add(gen);
                }
            } else {
                OreGenerator gen = new OreGenerator(bwt.getGenerators().get(0).getLocation().clone(), bwt.getArena(), GeneratorType.CUSTOM, bwt);
                gen.setOre(new ItemStack(Material.EMERALD));
                gen.setType(GeneratorType.EMERALD);
                bwt.getGenerators().add(gen);
                generator.add(gen);
            }
        }
        for (IGenerator iGenerator : generator) {
            iGenerator.setAmount(this.amount);
            iGenerator.setDelay(this.delay);
            iGenerator.setSpawnLimit(this.limit);
        }
    }

    public static enum ApplyType {
        IRON,
        GOLD,
        EMERALD;

    }
}

