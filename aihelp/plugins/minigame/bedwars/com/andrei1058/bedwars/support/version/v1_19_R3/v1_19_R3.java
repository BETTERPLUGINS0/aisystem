/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.util.Pair
 *  net.md_5.bungee.api.ChatMessageType
 *  net.md_5.bungee.api.chat.BaseComponent
 *  net.md_5.bungee.api.chat.TextComponent
 *  net.minecraft.core.particles.ParticleParam
 *  net.minecraft.core.particles.ParticleParamRedstone
 *  net.minecraft.nbt.NBTTagCompound
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy
 *  net.minecraft.network.protocol.game.PacketPlayOutEntityEquipment
 *  net.minecraft.network.protocol.game.PacketPlayOutEntityHeadRotation
 *  net.minecraft.network.protocol.game.PacketPlayOutEntityVelocity
 *  net.minecraft.network.protocol.game.PacketPlayOutNamedEntitySpawn
 *  net.minecraft.network.protocol.game.PacketPlayOutWorldParticles
 *  net.minecraft.server.MinecraftServer
 *  net.minecraft.server.dedicated.DedicatedServer
 *  net.minecraft.server.level.EntityPlayer
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityLiving
 *  net.minecraft.world.entity.EnumItemSlot
 *  net.minecraft.world.entity.item.EntityTNTPrimed
 *  net.minecraft.world.entity.player.EntityHuman
 *  net.minecraft.world.entity.projectile.EntityFireball
 *  net.minecraft.world.entity.projectile.IProjectile
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemArmor
 *  net.minecraft.world.item.ItemAxe
 *  net.minecraft.world.item.ItemBow
 *  net.minecraft.world.item.ItemElytra
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.ItemSword
 *  net.minecraft.world.item.ItemTool
 *  net.minecraft.world.level.IMaterial
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.Blocks
 *  net.minecraft.world.level.block.state.BlockBase
 *  org.bukkit.ChatColor
 *  org.bukkit.Color
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Particle
 *  org.bukkit.block.Block
 *  org.bukkit.block.BlockFace
 *  org.bukkit.block.BlockState
 *  org.bukkit.block.data.BlockData
 *  org.bukkit.block.data.type.Bed
 *  org.bukkit.block.data.type.Ladder
 *  org.bukkit.block.data.type.WallSign
 *  org.bukkit.command.Command
 *  org.bukkit.craftbukkit.v1_19_R3.CraftServer
 *  org.bukkit.craftbukkit.v1_19_R3.entity.CraftFireball
 *  org.bukkit.craftbukkit.v1_19_R3.entity.CraftLivingEntity
 *  org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer
 *  org.bukkit.craftbukkit.v1_19_R3.entity.CraftTNTPrimed
 *  org.bukkit.craftbukkit.v1_19_R3.inventory.CraftItemStack
 *  org.bukkit.entity.ArmorStand
 *  org.bukkit.entity.EnderDragon
 *  org.bukkit.entity.EnderDragon$Phase
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.Fireball
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.TNTPrimed
 *  org.bukkit.entity.Villager
 *  org.bukkit.event.inventory.InventoryEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.inventory.meta.PotionMeta
 *  org.bukkit.inventory.meta.SkullMeta
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.util.Vector
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 *  org.joml.Vector3f
 */
package com.andrei1058.bedwars.support.version.v1_19_R3;

import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.arena.shop.ShopHolo;
import com.andrei1058.bedwars.api.arena.team.ITeam;
import com.andrei1058.bedwars.api.arena.team.TeamColor;
import com.andrei1058.bedwars.api.entity.Despawnable;
import com.andrei1058.bedwars.api.events.player.PlayerKillEvent;
import com.andrei1058.bedwars.api.language.Language;
import com.andrei1058.bedwars.api.language.Messages;
import com.andrei1058.bedwars.api.server.VersionSupport;
import com.andrei1058.bedwars.support.version.common.VersionCommon;
import com.andrei1058.bedwars.support.version.v1_19_R3.despawnable.DespawnableAttributes;
import com.andrei1058.bedwars.support.version.v1_19_R3.despawnable.DespawnableFactory;
import com.andrei1058.bedwars.support.version.v1_19_R3.despawnable.DespawnableType;
import com.mojang.datafixers.util.Pair;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.core.particles.ParticleParam;
import net.minecraft.core.particles.ParticleParamRedstone;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityEquipment;
import net.minecraft.network.protocol.game.PacketPlayOutEntityHeadRotation;
import net.minecraft.network.protocol.game.PacketPlayOutEntityVelocity;
import net.minecraft.network.protocol.game.PacketPlayOutNamedEntitySpawn;
import net.minecraft.network.protocol.game.PacketPlayOutWorldParticles;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.EnumItemSlot;
import net.minecraft.world.entity.item.EntityTNTPrimed;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.entity.projectile.EntityFireball;
import net.minecraft.world.entity.projectile.IProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemArmor;
import net.minecraft.world.item.ItemAxe;
import net.minecraft.world.item.ItemBow;
import net.minecraft.world.item.ItemElytra;
import net.minecraft.world.item.ItemSword;
import net.minecraft.world.item.ItemTool;
import net.minecraft.world.level.IMaterial;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBase;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Bed;
import org.bukkit.block.data.type.Ladder;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.command.Command;
import org.bukkit.craftbukkit.v1_19_R3.CraftServer;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftFireball;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftTNTPrimed;
import org.bukkit.craftbukkit.v1_19_R3.inventory.CraftItemStack;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.Villager;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class v1_19_R3
extends VersionSupport {
    private final DespawnableFactory despawnableFactory;

    public v1_19_R3(Plugin plugin, String name) {
        super(plugin, name);
        this.loadDefaultEffects();
        this.despawnableFactory = new DespawnableFactory(this);
    }

    @Override
    public void registerVersionListeners() {
        new VersionCommon(this);
    }

    @Override
    public void registerCommand(String name, Command cmd) {
        ((CraftServer)this.getPlugin().getServer()).getCommandMap().register(name, cmd);
    }

    @Override
    public String getTag(ItemStack itemStack, String key) {
        NBTTagCompound tag = this.getTag(itemStack);
        return tag == null ? null : (tag.e(key) ? tag.l(key) : null);
    }

    @Override
    public void sendTitle(@NotNull Player p, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        p.sendTitle(title == null ? " " : title, subtitle == null ? " " : subtitle, fadeIn, stay, fadeOut);
    }

    @Override
    public void spawnSilverfish(Location loc, ITeam bedWarsTeam, double speed, double health, int despawn, double damage) {
        DespawnableAttributes attr = new DespawnableAttributes(DespawnableType.SILVERFISH, speed, health, damage, despawn);
        LivingEntity entity = this.despawnableFactory.spawn(attr, loc, bedWarsTeam);
        new Despawnable(entity, bedWarsTeam, despawn, Messages.SHOP_UTILITY_NPC_SILVERFISH_NAME, PlayerKillEvent.PlayerKillCause.SILVERFISH_FINAL_KILL, PlayerKillEvent.PlayerKillCause.SILVERFISH);
    }

    @Override
    public void spawnIronGolem(Location loc, ITeam bedWarsTeam, double speed, double health, int despawn) {
        DespawnableAttributes attr = new DespawnableAttributes(DespawnableType.IRON_GOLEM, speed, health, 4.0, despawn);
        LivingEntity entity = this.despawnableFactory.spawn(attr, loc, bedWarsTeam);
        new Despawnable(entity, bedWarsTeam, despawn, Messages.SHOP_UTILITY_NPC_IRON_GOLEM_NAME, PlayerKillEvent.PlayerKillCause.IRON_GOLEM_FINAL_KILL, PlayerKillEvent.PlayerKillCause.IRON_GOLEM);
    }

    @Override
    public void playAction(@NotNull Player p, String text) {
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, (BaseComponent)new TextComponent(ChatColor.translateAlternateColorCodes((char)'&', (String)text)));
    }

    @Override
    public boolean isBukkitCommandRegistered(String name) {
        return ((CraftServer)this.getPlugin().getServer()).getCommandMap().getCommand(name) != null;
    }

    @Override
    public ItemStack getItemInHand(@NotNull Player p) {
        return p.getInventory().getItemInMainHand();
    }

    @Override
    public void hideEntity(@NotNull Entity e, Player p) {
        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(new int[]{e.getEntityId()});
        ((CraftPlayer)p).getHandle().b.a((Packet)packet);
    }

    @Override
    public void minusAmount(Player p, @NotNull ItemStack i, int amount) {
        if (i.getAmount() - amount <= 0) {
            if (p.getInventory().getItemInOffHand().equals((Object)i)) {
                p.getInventory().setItemInOffHand(null);
            } else {
                p.getInventory().removeItem(new ItemStack[]{i});
            }
            return;
        }
        i.setAmount(i.getAmount() - amount);
        p.updateInventory();
    }

    @Override
    public void setSource(TNTPrimed tnt, Player owner) {
        EntityLiving nmsEntityLiving = ((CraftLivingEntity)owner).getHandle();
        EntityTNTPrimed nmsTNT = ((CraftTNTPrimed)tnt).getHandle();
        try {
            Field sourceField = EntityTNTPrimed.class.getDeclaredField("d");
            sourceField.setAccessible(true);
            sourceField.set(nmsTNT, nmsEntityLiving);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public boolean isArmor(ItemStack itemStack) {
        Item i = this.getItem(itemStack);
        if (null == i) {
            return false;
        }
        return i instanceof ItemArmor || i instanceof ItemElytra;
    }

    @Override
    public boolean isTool(ItemStack itemStack) {
        Item i = this.getItem(itemStack);
        if (null == i) {
            return false;
        }
        return i instanceof ItemTool;
    }

    @Override
    public boolean isSword(ItemStack itemStack) {
        Item i = this.getItem(itemStack);
        if (null == i) {
            return false;
        }
        return i instanceof ItemSword;
    }

    @Override
    public boolean isAxe(ItemStack itemStack) {
        Item i = this.getItem(itemStack);
        if (null == i) {
            return false;
        }
        return i instanceof ItemAxe;
    }

    @Override
    public boolean isBow(ItemStack itemStack) {
        Item i = this.getItem(itemStack);
        if (null == i) {
            return false;
        }
        return i instanceof ItemBow;
    }

    @Override
    public boolean isProjectile(ItemStack itemStack) {
        net.minecraft.world.entity.Entity entity = this.getEntity(itemStack);
        if (null == entity) {
            return false;
        }
        return entity instanceof IProjectile;
    }

    @Override
    public boolean isInvisibilityPotion(@NotNull ItemStack itemStack) {
        if (!itemStack.getType().equals((Object)Material.POTION)) {
            return false;
        }
        PotionMeta pm = (PotionMeta)itemStack.getItemMeta();
        return pm != null && pm.hasCustomEffects() && pm.hasCustomEffect(PotionEffectType.INVISIBILITY);
    }

    @Override
    public void registerEntities() {
    }

    @Override
    public void spawnShop(@NotNull Location loc, String name1, List<Player> players, IArena arena) {
        Location l = loc.clone();
        if (l.getWorld() == null) {
            return;
        }
        Villager vlg = (Villager)l.getWorld().spawnEntity(loc, EntityType.VILLAGER);
        vlg.setAI(false);
        vlg.setRemoveWhenFarAway(false);
        vlg.setCollidable(false);
        vlg.setInvulnerable(true);
        vlg.setSilent(true);
        for (Player p : players) {
            ArmorStand a;
            String[] name = Language.getMsg(p, name1).split(",");
            if (name.length == 1) {
                a = v1_19_R3.createArmorStand(name[0], l.clone().add(0.0, 1.85, 0.0));
                new ShopHolo(Language.getPlayerLanguage(p).getIso(), a, null, l, arena);
                continue;
            }
            a = v1_19_R3.createArmorStand(name[0], l.clone().add(0.0, 2.1, 0.0));
            ArmorStand b2 = v1_19_R3.createArmorStand(name[1], l.clone().add(0.0, 1.85, 0.0));
            new ShopHolo(Language.getPlayerLanguage(p).getIso(), a, b2, l, arena);
        }
        for (ShopHolo sh : ShopHolo.getShopHolo()) {
            if (sh.getA() != arena) continue;
            sh.update();
        }
    }

    @Override
    public double getDamage(ItemStack i) {
        NBTTagCompound tag = this.getTag(i);
        if (null == tag) {
            throw new RuntimeException("Provided item has no Tag");
        }
        return tag.k("generic.attackDamage");
    }

    private static ArmorStand createArmorStand(String name, Location loc) {
        if (loc == null) {
            return null;
        }
        if (loc.getWorld() == null) {
            return null;
        }
        ArmorStand a = (ArmorStand)loc.getWorld().spawn(loc, ArmorStand.class);
        a.setGravity(false);
        a.setVisible(false);
        a.setCustomNameVisible(true);
        a.setCustomName(name);
        return a;
    }

    @Override
    public void voidKill(Player p) {
        EntityPlayer player = this.getPlayer(p);
        player.a(player.dG().l(), 1000.0f);
    }

    @Override
    public void hideArmor(@NotNull Player victim, Player receiver) {
        ArrayList<Pair> items = new ArrayList<Pair>();
        items.add(new Pair((Object)EnumItemSlot.f, (Object)new net.minecraft.world.item.ItemStack((IMaterial)Item.b((int)0))));
        items.add(new Pair((Object)EnumItemSlot.e, (Object)new net.minecraft.world.item.ItemStack((IMaterial)Item.b((int)0))));
        items.add(new Pair((Object)EnumItemSlot.d, (Object)new net.minecraft.world.item.ItemStack((IMaterial)Item.b((int)0))));
        items.add(new Pair((Object)EnumItemSlot.c, (Object)new net.minecraft.world.item.ItemStack((IMaterial)Item.b((int)0))));
        PacketPlayOutEntityEquipment packet1 = new PacketPlayOutEntityEquipment(victim.getEntityId(), items);
        EntityPlayer pc = this.getPlayer(receiver);
        pc.b.a((Packet)packet1);
    }

    @Override
    public void showArmor(@NotNull Player victim, Player receiver) {
        ArrayList<Pair> items = new ArrayList<Pair>();
        items.add(new Pair((Object)EnumItemSlot.f, (Object)CraftItemStack.asNMSCopy((ItemStack)victim.getInventory().getHelmet())));
        items.add(new Pair((Object)EnumItemSlot.e, (Object)CraftItemStack.asNMSCopy((ItemStack)victim.getInventory().getChestplate())));
        items.add(new Pair((Object)EnumItemSlot.d, (Object)CraftItemStack.asNMSCopy((ItemStack)victim.getInventory().getLeggings())));
        items.add(new Pair((Object)EnumItemSlot.c, (Object)CraftItemStack.asNMSCopy((ItemStack)victim.getInventory().getBoots())));
        PacketPlayOutEntityEquipment packet1 = new PacketPlayOutEntityEquipment(victim.getEntityId(), items);
        EntityPlayer pc = this.getPlayer(receiver);
        pc.b.a((Packet)packet1);
    }

    @Override
    public void spawnDragon(Location l, ITeam bwt) {
        if (l == null || l.getWorld() == null) {
            this.getPlugin().getLogger().log(Level.WARNING, "Could not spawn Dragon. Location is null");
            return;
        }
        EnderDragon ed = (EnderDragon)l.getWorld().spawnEntity(l, EntityType.ENDER_DRAGON);
        ed.setPhase(EnderDragon.Phase.CIRCLING);
    }

    @Override
    public void colorBed(ITeam bwt) {
        for (int x = -1; x <= 1; ++x) {
            for (int z = -1; z <= 1; ++z) {
                BlockState bed = bwt.getBed().clone().add((double)x, 0.0, (double)z).getBlock().getState();
                if (!(bed instanceof Bed)) continue;
                bed.setType(bwt.getColor().bedMaterial());
                bed.update();
            }
        }
    }

    @Override
    public void registerTntWhitelist(float endStoneBlast, float glassBlast) {
        try {
            float protection = 300.0f;
            Field field = BlockBase.class.getDeclaredField("aH");
            field.setAccessible(true);
            field.set(Blocks.fj, Float.valueOf(endStoneBlast));
            field.set(Blocks.ce, Float.valueOf(glassBlast));
            field.set(Blocks.aH, Float.valueOf(glassBlast));
            Block[] coloredGlass = new Block[]{Blocks.dU, Blocks.dV, Blocks.dW, Blocks.dX, Blocks.dY, Blocks.dZ, Blocks.dZ, Blocks.ea, Blocks.eb, Blocks.ec, Blocks.ed, Blocks.ee, Blocks.ef, Blocks.eg, Blocks.eh, Blocks.ei, Blocks.ej};
            Arrays.stream(coloredGlass).forEach(glass -> {
                try {
                    field.set(glass, Float.valueOf(protection));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setBlockTeamColor(@NotNull org.bukkit.block.Block block, TeamColor teamColor) {
        if (block.getType().toString().contains("STAINED_GLASS") || block.getType().toString().equals("GLASS")) {
            block.setType(teamColor.glassMaterial());
        } else if (block.getType().toString().contains("_TERRACOTTA")) {
            block.setType(teamColor.glazedTerracottaMaterial());
        } else if (block.getType().toString().contains("_WOOL")) {
            block.setType(teamColor.woolMaterial());
        }
    }

    @Override
    public void setCollide(@NotNull Player p, IArena a, boolean value) {
        p.setCollidable(value);
        if (a == null) {
            return;
        }
        a.updateSpectatorCollideRule(p, value);
    }

    @Override
    public ItemStack addCustomData(ItemStack i, String data) {
        NBTTagCompound tag = this.getCreateTag(i);
        tag.a(VersionSupport.PLUGIN_TAG_GENERIC_KEY, data);
        return this.applyTag(i, tag);
    }

    @Override
    public ItemStack setTag(ItemStack itemStack, String key, String value) {
        NBTTagCompound tag = this.getCreateTag(itemStack);
        tag.a(key, value);
        return this.applyTag(itemStack, tag);
    }

    @Override
    public boolean isCustomBedWarsItem(ItemStack i) {
        return this.getCreateTag(i).e(VersionSupport.PLUGIN_TAG_GENERIC_KEY);
    }

    @Override
    public String getCustomData(ItemStack i) {
        return this.getCreateTag(i).l(VersionSupport.PLUGIN_TAG_GENERIC_KEY);
    }

    @Override
    public ItemStack colourItem(ItemStack itemStack, ITeam bedWarsTeam) {
        if (itemStack == null) {
            return null;
        }
        String type = itemStack.getType().toString();
        if (this.isBed(itemStack.getType())) {
            return new ItemStack(bedWarsTeam.getColor().bedMaterial(), itemStack.getAmount());
        }
        if (type.contains("_STAINED_GLASS_PANE")) {
            return new ItemStack(bedWarsTeam.getColor().glassPaneMaterial(), itemStack.getAmount());
        }
        if (type.contains("STAINED_GLASS") || type.equals("GLASS")) {
            return new ItemStack(bedWarsTeam.getColor().glassMaterial(), itemStack.getAmount());
        }
        if (type.contains("_TERRACOTTA")) {
            return new ItemStack(bedWarsTeam.getColor().glazedTerracottaMaterial(), itemStack.getAmount());
        }
        if (type.contains("_WOOL")) {
            return new ItemStack(bedWarsTeam.getColor().woolMaterial(), itemStack.getAmount());
        }
        return itemStack;
    }

    @Override
    public ItemStack createItemStack(String material, int amount, short data) {
        ItemStack i;
        try {
            i = new ItemStack(Material.valueOf((String)material), amount);
        } catch (Exception ex) {
            this.getPlugin().getLogger().log(Level.WARNING, material + " is not a valid " + v1_19_R3.getName() + " material!");
            i = new ItemStack(Material.BEDROCK);
        }
        return i;
    }

    @Override
    public Material materialFireball() {
        return Material.FIRE_CHARGE;
    }

    @Override
    public Material materialPlayerHead() {
        return Material.PLAYER_HEAD;
    }

    @Override
    public Material materialSnowball() {
        return Material.SNOWBALL;
    }

    @Override
    public Material materialGoldenHelmet() {
        return Material.GOLDEN_HELMET;
    }

    @Override
    public Material materialGoldenChestPlate() {
        return Material.GOLDEN_CHESTPLATE;
    }

    @Override
    public Material materialGoldenLeggings() {
        return Material.GOLDEN_LEGGINGS;
    }

    @Override
    public Material materialNetheriteHelmet() {
        return Material.NETHERITE_HELMET;
    }

    @Override
    public Material materialNetheriteChestPlate() {
        return Material.NETHERITE_CHESTPLATE;
    }

    @Override
    public Material materialNetheriteLeggings() {
        return Material.NETHERITE_LEGGINGS;
    }

    @Override
    public Material materialElytra() {
        return Material.ELYTRA;
    }

    @Override
    public Material materialCake() {
        return Material.CAKE;
    }

    @Override
    public Material materialCraftingTable() {
        return Material.CRAFTING_TABLE;
    }

    @Override
    public Material materialEnchantingTable() {
        return Material.ENCHANTING_TABLE;
    }

    @Override
    public Material woolMaterial() {
        return Material.WHITE_WOOL;
    }

    @Override
    public String getShopUpgradeIdentifier(ItemStack itemStack) {
        NBTTagCompound tag = this.getCreateTag(itemStack);
        return tag.e(VersionSupport.PLUGIN_TAG_TIER_KEY) ? tag.l(VersionSupport.PLUGIN_TAG_TIER_KEY) : "null";
    }

    @Override
    public ItemStack setShopUpgradeIdentifier(ItemStack itemStack, String identifier) {
        NBTTagCompound tag = this.getCreateTag(itemStack);
        tag.a(VersionSupport.PLUGIN_TAG_TIER_KEY, identifier);
        return this.applyTag(itemStack, tag);
    }

    @Override
    public ItemStack getPlayerHead(Player player, ItemStack copyTagFrom) {
        ItemMeta meta;
        ItemStack head = new ItemStack(this.materialPlayerHead());
        if (copyTagFrom != null) {
            NBTTagCompound tag = this.getTag(copyTagFrom);
            head = this.applyTag(head, tag);
        }
        if ((meta = head.getItemMeta()) instanceof SkullMeta) {
            ((SkullMeta)meta).setOwnerProfile(player.getPlayerProfile());
        }
        head.setItemMeta(meta);
        return head;
    }

    @Override
    public void sendPlayerSpawnPackets(Player respawned, IArena arena) {
        EntityPlayer boundTo;
        if (respawned == null) {
            return;
        }
        if (arena == null) {
            return;
        }
        if (!arena.isPlayer(respawned)) {
            return;
        }
        if (arena.getRespawnSessions().containsKey(respawned)) {
            return;
        }
        EntityPlayer entityPlayer = this.getPlayer(respawned);
        PacketPlayOutNamedEntitySpawn show = new PacketPlayOutNamedEntitySpawn((EntityHuman)entityPlayer);
        PacketPlayOutEntityVelocity playerVelocity = new PacketPlayOutEntityVelocity((net.minecraft.world.entity.Entity)entityPlayer);
        PacketPlayOutEntityHeadRotation head = new PacketPlayOutEntityHeadRotation((net.minecraft.world.entity.Entity)entityPlayer, this.getCompressedAngle(entityPlayer.getBukkitYaw()));
        List<Pair<EnumItemSlot, net.minecraft.world.item.ItemStack>> list = this.getPlayerEquipment(entityPlayer);
        for (Player p : arena.getPlayers()) {
            if (p == null || p.equals((Object)respawned) || arena.getRespawnSessions().containsKey(p)) continue;
            boundTo = this.getPlayer(p);
            if (!p.getWorld().equals((Object)respawned.getWorld()) || !(respawned.getLocation().distance(p.getLocation()) <= (double)arena.getRenderDistance())) continue;
            boundTo.b.a((Packet)show);
            boundTo.b.a((Packet)head);
            boundTo.b.a((Packet)playerVelocity);
            boundTo.b.a((Packet)new PacketPlayOutEntityEquipment(respawned.getEntityId(), list));
            if (p.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                this.hideArmor(p, respawned);
                continue;
            }
            PacketPlayOutNamedEntitySpawn show2 = new PacketPlayOutNamedEntitySpawn((EntityHuman)boundTo);
            PacketPlayOutEntityVelocity playerVelocity2 = new PacketPlayOutEntityVelocity((net.minecraft.world.entity.Entity)boundTo);
            PacketPlayOutEntityHeadRotation head2 = new PacketPlayOutEntityHeadRotation((net.minecraft.world.entity.Entity)boundTo, this.getCompressedAngle(boundTo.getBukkitYaw()));
            entityPlayer.b.a((Packet)show2);
            entityPlayer.b.a((Packet)playerVelocity2);
            entityPlayer.b.a((Packet)head2);
            this.showArmor(p, respawned);
        }
        for (Player spectator : arena.getSpectators()) {
            if (spectator == null || spectator.equals((Object)respawned)) continue;
            boundTo = ((CraftPlayer)spectator).getHandle();
            respawned.hidePlayer(this.getPlugin(), spectator);
            if (!spectator.getWorld().equals((Object)respawned.getWorld()) || !(respawned.getLocation().distance(spectator.getLocation()) <= (double)arena.getRenderDistance())) continue;
            boundTo.b.a((Packet)show);
            boundTo.b.a((Packet)playerVelocity);
            boundTo.b.a((Packet)new PacketPlayOutEntityEquipment(respawned.getEntityId(), list));
            boundTo.b.a((Packet)new PacketPlayOutEntityHeadRotation((net.minecraft.world.entity.Entity)entityPlayer, this.getCompressedAngle(entityPlayer.getBukkitYaw())));
        }
    }

    @Override
    public String getInventoryName(@NotNull InventoryEvent e) {
        return e.getView().getTitle();
    }

    @Override
    public void setUnbreakable(@NotNull ItemMeta itemMeta) {
        itemMeta.setUnbreakable(true);
    }

    @Override
    public String getMainLevel() {
        return ((DedicatedServer)MinecraftServer.getServer()).a().m;
    }

    @Override
    public int getVersion() {
        return 9;
    }

    @Override
    public void setJoinSignBackground(@NotNull BlockState b2, Material material) {
        if (b2.getBlockData() instanceof WallSign) {
            b2.getBlock().getRelative(((WallSign)b2.getBlockData()).getFacing().getOppositeFace()).setType(material);
        }
    }

    @Override
    public void spigotShowPlayer(Player victim, @NotNull Player receiver) {
        receiver.showPlayer(this.getPlugin(), victim);
    }

    @Override
    public void spigotHidePlayer(Player victim, @NotNull Player receiver) {
        receiver.hidePlayer(this.getPlugin(), victim);
    }

    @Override
    public Fireball setFireballDirection(Fireball fireball, @NotNull Vector vector) {
        EntityFireball fb = ((CraftFireball)fireball).getHandle();
        fb.b = vector.getX() * 0.1;
        fb.c = vector.getY() * 0.1;
        fb.d = vector.getZ() * 0.1;
        return (Fireball)fb.getBukkitEntity();
    }

    @Override
    public void playRedStoneDot(@NotNull Player player) {
        Color color = Color.RED;
        PacketPlayOutWorldParticles particlePacket = new PacketPlayOutWorldParticles((ParticleParam)new ParticleParamRedstone(new Vector3f((float)color.getRed(), (float)color.getGreen(), (float)color.getBlue()), 1.0f), true, player.getLocation().getX(), player.getLocation().getY() + 2.6, player.getLocation().getZ(), 0.0f, 0.0f, 0.0f, 0.0f, 0);
        for (Player inWorld : player.getWorld().getPlayers()) {
            if (inWorld.equals((Object)player)) continue;
            this.getPlayer((Player)inWorld).b.a((Packet)particlePacket);
        }
    }

    @Override
    public void clearArrowsFromPlayerBody(Player player) {
    }

    @Nullable
    private Item getItem(ItemStack itemStack) {
        net.minecraft.world.item.ItemStack i = CraftItemStack.asNMSCopy((ItemStack)itemStack);
        if (null == i) {
            return null;
        }
        return i.c();
    }

    @Nullable
    private net.minecraft.world.entity.Entity getEntity(ItemStack itemStack) {
        net.minecraft.world.item.ItemStack i = CraftItemStack.asNMSCopy((ItemStack)itemStack);
        if (null == i) {
            return null;
        }
        return i.G();
    }

    @Nullable
    private NBTTagCompound getTag(@NotNull ItemStack itemStack) {
        net.minecraft.world.item.ItemStack i = CraftItemStack.asNMSCopy((ItemStack)itemStack);
        if (null == i) {
            return null;
        }
        return i.u();
    }

    @Nullable
    private NBTTagCompound getTag(@NotNull net.minecraft.world.item.ItemStack itemStack) {
        return itemStack.u();
    }

    @NotNull
    private NBTTagCompound initializeTag(ItemStack itemStack) {
        net.minecraft.world.item.ItemStack i = CraftItemStack.asNMSCopy((ItemStack)itemStack);
        if (null == i) {
            throw new RuntimeException("Cannot convert given item to a NMS item");
        }
        return this.initializeTag(i);
    }

    @NotNull
    private NBTTagCompound initializeTag(net.minecraft.world.item.ItemStack itemStack) {
        NBTTagCompound tag = this.getTag(itemStack);
        if (null != tag) {
            throw new RuntimeException("Provided item already has a Tag");
        }
        tag = new NBTTagCompound();
        itemStack.c(tag);
        return tag;
    }

    public NBTTagCompound getCreateTag(net.minecraft.world.item.ItemStack itemStack) {
        NBTTagCompound tag = this.getTag(itemStack);
        return null == tag ? this.initializeTag(itemStack) : tag;
    }

    public NBTTagCompound getCreateTag(ItemStack itemStack) {
        net.minecraft.world.item.ItemStack i = CraftItemStack.asNMSCopy((ItemStack)itemStack);
        if (null == i) {
            throw new RuntimeException("Cannot convert given item to a NMS item");
        }
        return this.getCreateTag(i);
    }

    public ItemStack applyTag(ItemStack itemStack, NBTTagCompound tag) {
        return CraftItemStack.asBukkitCopy((net.minecraft.world.item.ItemStack)this.applyTag(this.getNmsItemCopy(itemStack), tag));
    }

    public net.minecraft.world.item.ItemStack applyTag(@NotNull net.minecraft.world.item.ItemStack itemStack, NBTTagCompound tag) {
        itemStack.c(tag);
        return itemStack;
    }

    public net.minecraft.world.item.ItemStack getNmsItemCopy(ItemStack itemStack) {
        net.minecraft.world.item.ItemStack i = CraftItemStack.asNMSCopy((ItemStack)itemStack);
        if (null == i) {
            throw new RuntimeException("Cannot convert given item to a NMS item");
        }
        return i;
    }

    public EntityPlayer getPlayer(Player player) {
        return ((CraftPlayer)player).getHandle();
    }

    public List<Pair<EnumItemSlot, net.minecraft.world.item.ItemStack>> getPlayerEquipment(@NotNull Player player) {
        return this.getPlayerEquipment(this.getPlayer(player));
    }

    public List<Pair<EnumItemSlot, net.minecraft.world.item.ItemStack>> getPlayerEquipment(@NotNull EntityPlayer entityPlayer) {
        ArrayList<Pair<EnumItemSlot, net.minecraft.world.item.ItemStack>> list = new ArrayList<Pair<EnumItemSlot, net.minecraft.world.item.ItemStack>>();
        list.add(new Pair((Object)EnumItemSlot.a, (Object)entityPlayer.c(EnumItemSlot.a)));
        list.add(new Pair((Object)EnumItemSlot.b, (Object)entityPlayer.c(EnumItemSlot.b)));
        list.add(new Pair((Object)EnumItemSlot.f, (Object)entityPlayer.c(EnumItemSlot.f)));
        list.add(new Pair((Object)EnumItemSlot.e, (Object)entityPlayer.c(EnumItemSlot.e)));
        list.add(new Pair((Object)EnumItemSlot.d, (Object)entityPlayer.c(EnumItemSlot.d)));
        list.add(new Pair((Object)EnumItemSlot.c, (Object)entityPlayer.c(EnumItemSlot.c)));
        return list;
    }

    @Override
    public void placeTowerBlocks(@NotNull org.bukkit.block.Block b2, @NotNull IArena a, @NotNull TeamColor color, int x, int y, int z) {
        b2.getRelative(x, y, z).setType(color.woolMaterial());
        a.addPlacedBlock(b2.getRelative(x, y, z));
    }

    @Override
    public void placeLadder(@NotNull org.bukkit.block.Block b2, int x, int y, int z, @NotNull IArena a, int ladderData) {
        org.bukkit.block.Block block = b2.getRelative(x, y, z);
        block.setType(Material.LADDER);
        Ladder ladder = (Ladder)block.getBlockData();
        a.addPlacedBlock(block);
        switch (ladderData) {
            case 2: {
                ladder.setFacing(BlockFace.NORTH);
                block.setBlockData((BlockData)ladder);
                break;
            }
            case 3: {
                ladder.setFacing(BlockFace.SOUTH);
                block.setBlockData((BlockData)ladder);
                break;
            }
            case 4: {
                ladder.setFacing(BlockFace.WEST);
                block.setBlockData((BlockData)ladder);
                break;
            }
            case 5: {
                ladder.setFacing(BlockFace.EAST);
                block.setBlockData((BlockData)ladder);
            }
        }
    }

    @Override
    public void playVillagerEffect(@NotNull Player player, Location location) {
        player.spawnParticle(Particle.VILLAGER_HAPPY, location, 1);
    }
}

