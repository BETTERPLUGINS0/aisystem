/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.Player
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.util.Vector
 */
package com.andrei1058.bedwars.api.arena.team;

import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.arena.generator.IGenerator;
import com.andrei1058.bedwars.api.arena.team.TeamColor;
import com.andrei1058.bedwars.api.arena.team.TeamEnchant;
import com.andrei1058.bedwars.api.language.Language;
import com.andrei1058.bedwars.api.upgrades.EnemyBaseEnterTrap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Location;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public interface ITeam {
    public UUID getIdentity();

    public TeamColor getColor();

    public String getName();

    public String getDisplayName(Language var1);

    public boolean isMember(Player var1);

    public IArena getArena();

    public List<Player> getMembers();

    public void defaultSword(Player var1, boolean var2);

    public Location getBed();

    public ConcurrentHashMap<String, Integer> getTeamUpgradeTiers();

    public List<TeamEnchant> getBowsEnchantments();

    public List<TeamEnchant> getSwordsEnchantments();

    public List<TeamEnchant> getArmorsEnchantments();

    public int getSize();

    public void addPlayers(Player ... var1);

    public void firstSpawn(Player var1);

    public void spawnNPCs();

    public void reJoin(Player var1);

    public void reJoin(Player var1, int var2);

    public void sendDefaultInventory(Player var1, boolean var2);

    public void respawnMember(Player var1);

    public void sendArmor(Player var1);

    public void addTeamEffect(PotionEffectType var1, int var2, int var3);

    public void addBaseEffect(PotionEffectType var1, int var2, int var3);

    public List<PotionEffect> getBaseEffects();

    public void addBowEnchantment(Enchantment var1, int var2);

    public void addSwordEnchantment(Enchantment var1, int var2);

    public void addArmorEnchantment(Enchantment var1, int var2);

    public boolean wasMember(UUID var1);

    public boolean isBedDestroyed();

    public Location getSpawn();

    public Location getShop();

    public Location getTeamUpgrades();

    public void setBedDestroyed(boolean var1);

    @Deprecated
    public IGenerator getIronGenerator();

    @Deprecated
    public IGenerator getGoldGenerator();

    @Deprecated
    public IGenerator getEmeraldGenerator();

    @Deprecated
    public void setEmeraldGenerator(IGenerator var1);

    public List<IGenerator> getGenerators();

    public int getDragons();

    public void setDragons(int var1);

    @Deprecated
    public List<Player> getMembersCache();

    public void destroyData();

    @Deprecated
    public void destroyBedHolo(Player var1);

    public LinkedList<EnemyBaseEnterTrap> getActiveTraps();

    public Vector getKillDropsLocation();

    public void setKillDropsLocation(Vector var1);

    public boolean isBed(Location var1);

    default public void onBedDestroy(Location location) {
        throw new RuntimeException("Not implemented yet");
    }
}

