/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.entity.ArmorStand
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package com.andrei1058.bedwars.api.arena.generator;

import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.arena.generator.GeneratorType;
import com.andrei1058.bedwars.api.arena.generator.IGenHolo;
import com.andrei1058.bedwars.api.arena.team.ITeam;
import java.util.HashMap;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface IGenerator {
    public HashMap<String, IGenHolo> getLanguageHolograms();

    public void disable();

    public void upgrade();

    public void spawn();

    public void dropItem(Location var1);

    public void setOre(ItemStack var1);

    public IArena getArena();

    public void rotate();

    public void setDelay(int var1);

    public void setAmount(int var1);

    public Location getLocation();

    public ItemStack getOre();

    public void updateHolograms(Player var1, String var2);

    public void enableRotation();

    public void setSpawnLimit(int var1);

    public ITeam getBwt();

    public ArmorStand getHologramHolder();

    public GeneratorType getType();

    public int getAmount();

    public int getDelay();

    public int getNextSpawn();

    public int getSpawnLimit();

    public void setNextSpawn(int var1);

    public void setStack(boolean var1);

    public boolean isStack();

    public void setType(GeneratorType var1);

    public void destroyData();
}

