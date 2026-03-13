/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Effect
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.block.BlockState
 *  org.bukkit.command.Command
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Fireball
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.TNTPrimed
 *  org.bukkit.event.inventory.InventoryEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.util.Vector
 *  org.jetbrains.annotations.Nullable
 */
package com.andrei1058.bedwars.api.server;

import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.arena.team.ITeam;
import com.andrei1058.bedwars.api.arena.team.TeamColor;
import com.andrei1058.bedwars.api.entity.Despawnable;
import com.andrei1058.bedwars.api.exceptions.InvalidEffectException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.command.Command;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

public abstract class VersionSupport {
    private static String name2;
    public static String PLUGIN_TAG_GENERIC_KEY;
    public static String PLUGIN_TAG_TIER_KEY;
    private Effect eggBridge;
    private static final ConcurrentHashMap<UUID, Despawnable> despawnables;
    private final Plugin plugin;

    public VersionSupport(Plugin plugin, String versionName) {
        name2 = versionName;
        this.plugin = plugin;
    }

    protected void loadDefaultEffects() {
        try {
            this.setEggBridgeEffect("MOBSPAWNER_FLAMES");
        } catch (InvalidEffectException e) {
            e.printStackTrace();
        }
    }

    public abstract void registerCommand(String var1, Command var2);

    public abstract void sendTitle(Player var1, String var2, String var3, int var4, int var5, int var6);

    public abstract void playAction(Player var1, String var2);

    public abstract boolean isBukkitCommandRegistered(String var1);

    public abstract ItemStack getItemInHand(Player var1);

    public abstract void hideEntity(Entity var1, Player var2);

    public abstract boolean isArmor(ItemStack var1);

    public abstract boolean isTool(ItemStack var1);

    public abstract boolean isSword(ItemStack var1);

    public abstract boolean isAxe(ItemStack var1);

    public abstract boolean isBow(ItemStack var1);

    public abstract boolean isProjectile(ItemStack var1);

    public abstract boolean isInvisibilityPotion(ItemStack var1);

    public boolean isGlass(Material type) {
        return type != Material.AIR && (type == Material.GLASS || type.toString().contains("_GLASS"));
    }

    public abstract void registerEntities();

    public abstract void spawnShop(Location var1, String var2, List<Player> var3, IArena var4);

    public abstract double getDamage(ItemStack var1);

    public abstract void spawnSilverfish(Location var1, ITeam var2, double var3, double var5, int var7, double var8);

    public abstract void spawnIronGolem(Location var1, ITeam var2, double var3, double var5, int var7);

    public boolean isDespawnable(Entity e) {
        return despawnables.get(e.getUniqueId()) != null;
    }

    public abstract void minusAmount(Player var1, ItemStack var2, int var3);

    public abstract void setSource(TNTPrimed var1, Player var2);

    public abstract void voidKill(Player var1);

    public abstract void hideArmor(Player var1, Player var2);

    public abstract void showArmor(Player var1, Player var2);

    public abstract void spawnDragon(Location var1, ITeam var2);

    public abstract void colorBed(ITeam var1);

    public abstract void registerTntWhitelist(float var1, float var2);

    public Effect eggBridge() {
        return this.eggBridge;
    }

    public void setEggBridgeEffect(String eggBridge) throws InvalidEffectException {
        try {
            this.eggBridge = Effect.valueOf((String)eggBridge);
        } catch (Exception e) {
            throw new InvalidEffectException(eggBridge);
        }
    }

    public abstract void setBlockTeamColor(Block var1, TeamColor var2);

    public abstract void setCollide(Player var1, IArena var2, boolean var3);

    public abstract ItemStack addCustomData(ItemStack var1, String var2);

    public abstract ItemStack setTag(ItemStack var1, String var2, String var3);

    public abstract String getTag(ItemStack var1, String var2);

    public abstract boolean isCustomBedWarsItem(ItemStack var1);

    public abstract String getCustomData(ItemStack var1);

    public abstract ItemStack colourItem(ItemStack var1, ITeam var2);

    public abstract ItemStack createItemStack(String var1, int var2, short var3);

    public boolean isPlayerHead(String material, int data) {
        return material.equalsIgnoreCase("PLAYER_HEAD");
    }

    public abstract Material materialFireball();

    public abstract Material materialPlayerHead();

    public abstract Material materialSnowball();

    public abstract Material materialGoldenHelmet();

    public abstract Material materialGoldenChestPlate();

    public abstract Material materialGoldenLeggings();

    public abstract Material materialNetheriteHelmet();

    public abstract Material materialNetheriteChestPlate();

    public abstract Material materialNetheriteLeggings();

    public abstract Material materialElytra();

    public abstract Material materialCake();

    public abstract Material materialCraftingTable();

    public abstract Material materialEnchantingTable();

    public boolean isBed(Material material) {
        return material.toString().contains("_BED");
    }

    public boolean itemStackDataCompare(ItemStack i, short data) {
        return true;
    }

    public void setJoinSignBackgroundBlockData(BlockState b, byte data) {
    }

    public abstract void setJoinSignBackground(BlockState var1, Material var2);

    public abstract Material woolMaterial();

    public abstract String getShopUpgradeIdentifier(ItemStack var1);

    public abstract ItemStack setShopUpgradeIdentifier(ItemStack var1, String var2);

    public abstract ItemStack getPlayerHead(Player var1, @Nullable ItemStack var2);

    public abstract void sendPlayerSpawnPackets(Player var1, IArena var2);

    public abstract String getInventoryName(InventoryEvent var1);

    public abstract void setUnbreakable(ItemMeta var1);

    public ConcurrentHashMap<UUID, Despawnable> getDespawnablesList() {
        return despawnables;
    }

    public static String getName() {
        return name2;
    }

    public abstract int getVersion();

    public Plugin getPlugin() {
        return this.plugin;
    }

    public abstract void registerVersionListeners();

    public abstract String getMainLevel();

    public byte getCompressedAngle(float value) {
        return (byte)(value * 256.0f / 360.0f);
    }

    public void spigotShowPlayer(Player victim, Player receiver) {
        receiver.showPlayer(victim);
    }

    public void spigotHidePlayer(Player victim, Player receiver) {
        receiver.hidePlayer(victim);
    }

    public abstract Fireball setFireballDirection(Fireball var1, Vector var2);

    public abstract void playRedStoneDot(Player var1);

    public abstract void clearArrowsFromPlayerBody(Player var1);

    public abstract void placeTowerBlocks(Block var1, IArena var2, TeamColor var3, int var4, int var5, int var6);

    public abstract void placeLadder(Block var1, int var2, int var3, int var4, IArena var5, int var6);

    public abstract void playVillagerEffect(Player var1, Location var2);

    static {
        PLUGIN_TAG_GENERIC_KEY = "BedWars1058";
        PLUGIN_TAG_TIER_KEY = "tierIdentifier";
        despawnables = new ConcurrentHashMap();
    }
}

