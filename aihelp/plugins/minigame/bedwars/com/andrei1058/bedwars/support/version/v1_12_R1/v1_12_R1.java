/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraft.server.v1_12_R1.Block
 *  net.minecraft.server.v1_12_R1.ChatMessageType
 *  net.minecraft.server.v1_12_R1.DamageSource
 *  net.minecraft.server.v1_12_R1.DataWatcherObject
 *  net.minecraft.server.v1_12_R1.DataWatcherRegistry
 *  net.minecraft.server.v1_12_R1.DedicatedServer
 *  net.minecraft.server.v1_12_R1.Entity
 *  net.minecraft.server.v1_12_R1.EntityFireball
 *  net.minecraft.server.v1_12_R1.EntityHuman
 *  net.minecraft.server.v1_12_R1.EntityLiving
 *  net.minecraft.server.v1_12_R1.EntityPlayer
 *  net.minecraft.server.v1_12_R1.EntityTNTPrimed
 *  net.minecraft.server.v1_12_R1.EntityTypes
 *  net.minecraft.server.v1_12_R1.EnumItemSlot
 *  net.minecraft.server.v1_12_R1.EnumParticle
 *  net.minecraft.server.v1_12_R1.IChatBaseComponent
 *  net.minecraft.server.v1_12_R1.IChatBaseComponent$ChatSerializer
 *  net.minecraft.server.v1_12_R1.IProjectile
 *  net.minecraft.server.v1_12_R1.Item
 *  net.minecraft.server.v1_12_R1.ItemArmor
 *  net.minecraft.server.v1_12_R1.ItemAxe
 *  net.minecraft.server.v1_12_R1.ItemBow
 *  net.minecraft.server.v1_12_R1.ItemElytra
 *  net.minecraft.server.v1_12_R1.ItemStack
 *  net.minecraft.server.v1_12_R1.ItemSword
 *  net.minecraft.server.v1_12_R1.ItemTool
 *  net.minecraft.server.v1_12_R1.MinecraftKey
 *  net.minecraft.server.v1_12_R1.MinecraftServer
 *  net.minecraft.server.v1_12_R1.NBTTagCompound
 *  net.minecraft.server.v1_12_R1.Packet
 *  net.minecraft.server.v1_12_R1.PacketPlayOutChat
 *  net.minecraft.server.v1_12_R1.PacketPlayOutEntityDestroy
 *  net.minecraft.server.v1_12_R1.PacketPlayOutEntityEquipment
 *  net.minecraft.server.v1_12_R1.PacketPlayOutEntityHeadRotation
 *  net.minecraft.server.v1_12_R1.PacketPlayOutEntityVelocity
 *  net.minecraft.server.v1_12_R1.PacketPlayOutNamedEntitySpawn
 *  net.minecraft.server.v1_12_R1.PacketPlayOutTitle
 *  net.minecraft.server.v1_12_R1.PacketPlayOutTitle$EnumTitleAction
 *  net.minecraft.server.v1_12_R1.PacketPlayOutWorldParticles
 *  org.bukkit.Color
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Particle
 *  org.bukkit.block.Bed
 *  org.bukkit.block.Block
 *  org.bukkit.block.BlockState
 *  org.bukkit.command.Command
 *  org.bukkit.craftbukkit.v1_12_R1.CraftServer
 *  org.bukkit.craftbukkit.v1_12_R1.entity.CraftFireball
 *  org.bukkit.craftbukkit.v1_12_R1.entity.CraftLivingEntity
 *  org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer
 *  org.bukkit.craftbukkit.v1_12_R1.entity.CraftTNTPrimed
 *  org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack
 *  org.bukkit.entity.ArmorStand
 *  org.bukkit.entity.EnderDragon
 *  org.bukkit.entity.EnderDragon$Phase
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.Fireball
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.TNTPrimed
 *  org.bukkit.entity.Villager
 *  org.bukkit.event.inventory.InventoryEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.inventory.meta.PotionMeta
 *  org.bukkit.inventory.meta.SkullMeta
 *  org.bukkit.material.Sign
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.util.Vector
 */
package com.andrei1058.bedwars.support.version.v1_12_R1;

import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.arena.shop.ShopHolo;
import com.andrei1058.bedwars.api.arena.team.ITeam;
import com.andrei1058.bedwars.api.arena.team.TeamColor;
import com.andrei1058.bedwars.api.entity.Despawnable;
import com.andrei1058.bedwars.api.events.player.PlayerKillEvent;
import com.andrei1058.bedwars.api.exceptions.InvalidEffectException;
import com.andrei1058.bedwars.api.language.Language;
import com.andrei1058.bedwars.api.language.Messages;
import com.andrei1058.bedwars.api.server.VersionSupport;
import com.andrei1058.bedwars.support.version.common.VersionCommon;
import com.andrei1058.bedwars.support.version.v1_12_R1.IGolem;
import com.andrei1058.bedwars.support.version.v1_12_R1.Silverfish;
import java.lang.reflect.Field;
import java.util.List;
import java.util.logging.Level;
import net.minecraft.server.v1_12_R1.Block;
import net.minecraft.server.v1_12_R1.ChatMessageType;
import net.minecraft.server.v1_12_R1.DamageSource;
import net.minecraft.server.v1_12_R1.DataWatcherObject;
import net.minecraft.server.v1_12_R1.DataWatcherRegistry;
import net.minecraft.server.v1_12_R1.DedicatedServer;
import net.minecraft.server.v1_12_R1.EntityFireball;
import net.minecraft.server.v1_12_R1.EntityHuman;
import net.minecraft.server.v1_12_R1.EntityLiving;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.EntityTNTPrimed;
import net.minecraft.server.v1_12_R1.EntityTypes;
import net.minecraft.server.v1_12_R1.EnumItemSlot;
import net.minecraft.server.v1_12_R1.EnumParticle;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.IProjectile;
import net.minecraft.server.v1_12_R1.Item;
import net.minecraft.server.v1_12_R1.ItemArmor;
import net.minecraft.server.v1_12_R1.ItemAxe;
import net.minecraft.server.v1_12_R1.ItemBow;
import net.minecraft.server.v1_12_R1.ItemElytra;
import net.minecraft.server.v1_12_R1.ItemSword;
import net.minecraft.server.v1_12_R1.ItemTool;
import net.minecraft.server.v1_12_R1.MinecraftKey;
import net.minecraft.server.v1_12_R1.MinecraftServer;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.Packet;
import net.minecraft.server.v1_12_R1.PacketPlayOutChat;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityEquipment;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityVelocity;
import net.minecraft.server.v1_12_R1.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_12_R1.PacketPlayOutTitle;
import net.minecraft.server.v1_12_R1.PacketPlayOutWorldParticles;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Bed;
import org.bukkit.block.BlockState;
import org.bukkit.command.Command;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftFireball;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftTNTPrimed;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.Villager;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.Sign;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class v1_12_R1
extends VersionSupport {
    public v1_12_R1(Plugin plugin, String name) {
        super(plugin, name);
        try {
            this.setEggBridgeEffect("MOBSPAWNER_FLAMES");
        } catch (InvalidEffectException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getTag(ItemStack itemStack, String key) {
        net.minecraft.server.v1_12_R1.ItemStack i = CraftItemStack.asNMSCopy((ItemStack)itemStack);
        if (i == null) {
            return null;
        }
        NBTTagCompound tag = i.getTag();
        return tag == null ? null : (tag.hasKey(key) ? tag.getString(key) : null);
    }

    @Override
    public void registerVersionListeners() {
        new VersionCommon(this);
    }

    @Override
    public void registerCommand(String name, Command clasa) {
        ((CraftServer)this.getPlugin().getServer()).getCommandMap().register(name, clasa);
    }

    @Override
    public void sendTitle(Player p, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        PacketPlayOutTitle length;
        PacketPlayOutTitle tit;
        IChatBaseComponent bc;
        if (title != null && !title.isEmpty()) {
            bc = IChatBaseComponent.ChatSerializer.a((String)("{\"text\": \"" + title + "\"}"));
            tit = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, bc);
            length = new PacketPlayOutTitle(fadeIn, stay, fadeOut);
            ((CraftPlayer)p).getHandle().playerConnection.sendPacket((Packet)tit);
            ((CraftPlayer)p).getHandle().playerConnection.sendPacket((Packet)length);
        }
        if (subtitle != null) {
            bc = IChatBaseComponent.ChatSerializer.a((String)("{\"text\": \"" + subtitle + "\"}"));
            tit = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, bc);
            length = new PacketPlayOutTitle(fadeIn, stay, fadeOut);
            ((CraftPlayer)p).getHandle().playerConnection.sendPacket((Packet)tit);
            ((CraftPlayer)p).getHandle().playerConnection.sendPacket((Packet)length);
        }
    }

    @Override
    public void spawnSilverfish(Location loc, ITeam bedWarsTeam, double speed, double health, int despawn, double damage) {
        new Despawnable(Silverfish.spawn(loc, bedWarsTeam, speed, health, despawn, damage), bedWarsTeam, despawn, Messages.SHOP_UTILITY_NPC_SILVERFISH_NAME, PlayerKillEvent.PlayerKillCause.SILVERFISH_FINAL_KILL, PlayerKillEvent.PlayerKillCause.SILVERFISH);
    }

    @Override
    public void spawnIronGolem(Location loc, ITeam bedWarsTeam, double speed, double health, int despawn) {
        new Despawnable(IGolem.spawn(loc, bedWarsTeam, speed, health, despawn), bedWarsTeam, despawn, Messages.SHOP_UTILITY_NPC_IRON_GOLEM_NAME, PlayerKillEvent.PlayerKillCause.IRON_GOLEM_FINAL_KILL, PlayerKillEvent.PlayerKillCause.IRON_GOLEM);
    }

    @Override
    public void playAction(Player p, String text) {
        CraftPlayer cPlayer = (CraftPlayer)p;
        IChatBaseComponent cbc = IChatBaseComponent.ChatSerializer.a((String)("{\"text\": \"" + text + "\"}"));
        PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, ChatMessageType.GAME_INFO);
        cPlayer.getHandle().playerConnection.sendPacket((Packet)ppoc);
    }

    @Override
    public boolean isBukkitCommandRegistered(String name) {
        return ((CraftServer)this.getPlugin().getServer()).getCommandMap().getCommand(name) != null;
    }

    @Override
    public ItemStack getItemInHand(Player p) {
        return p.getInventory().getItemInMainHand();
    }

    @Override
    public void hideEntity(Entity e, Player p) {
        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(new int[]{e.getEntityId()});
        ((CraftPlayer)p).getHandle().playerConnection.sendPacket((Packet)packet);
    }

    @Override
    public void minusAmount(Player p, ItemStack i, int amount) {
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
            Field sourceField = EntityTNTPrimed.class.getDeclaredField("source");
            sourceField.setAccessible(true);
            sourceField.set(nmsTNT, nmsEntityLiving);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public boolean isArmor(ItemStack itemStack) {
        if (CraftItemStack.asNMSCopy((ItemStack)itemStack) == null) {
            return false;
        }
        if (CraftItemStack.asNMSCopy((ItemStack)itemStack).getItem() == null) {
            return false;
        }
        return CraftItemStack.asNMSCopy((ItemStack)itemStack).getItem() instanceof ItemArmor || CraftItemStack.asNMSCopy((ItemStack)itemStack).getItem() instanceof ItemElytra;
    }

    @Override
    public boolean isTool(ItemStack itemStack) {
        if (CraftItemStack.asNMSCopy((ItemStack)itemStack) == null) {
            return false;
        }
        if (CraftItemStack.asNMSCopy((ItemStack)itemStack).getItem() == null) {
            return false;
        }
        return CraftItemStack.asNMSCopy((ItemStack)itemStack).getItem() instanceof ItemTool;
    }

    @Override
    public boolean isSword(ItemStack itemStack) {
        if (CraftItemStack.asNMSCopy((ItemStack)itemStack) == null) {
            return false;
        }
        if (CraftItemStack.asNMSCopy((ItemStack)itemStack).getItem() == null) {
            return false;
        }
        return CraftItemStack.asNMSCopy((ItemStack)itemStack).getItem() instanceof ItemSword;
    }

    @Override
    public boolean isAxe(ItemStack itemStack) {
        if (CraftItemStack.asNMSCopy((ItemStack)itemStack).getItem() == null) {
            return false;
        }
        return CraftItemStack.asNMSCopy((ItemStack)itemStack).getItem() instanceof ItemAxe;
    }

    @Override
    public boolean isBow(ItemStack itemStack) {
        if (CraftItemStack.asNMSCopy((ItemStack)itemStack) == null) {
            return false;
        }
        if (CraftItemStack.asNMSCopy((ItemStack)itemStack).getItem() == null) {
            return false;
        }
        return CraftItemStack.asNMSCopy((ItemStack)itemStack).getItem() instanceof ItemBow;
    }

    @Override
    public boolean isProjectile(ItemStack itemStack) {
        if (CraftItemStack.asNMSCopy((ItemStack)itemStack) == null) {
            return false;
        }
        if (CraftItemStack.asNMSCopy((ItemStack)itemStack).getItem() == null) {
            return false;
        }
        return CraftItemStack.asNMSCopy((ItemStack)itemStack).getItem() instanceof IProjectile;
    }

    @Override
    public boolean isInvisibilityPotion(ItemStack itemStack) {
        if (!itemStack.getType().equals((Object)Material.POTION)) {
            return false;
        }
        PotionMeta pm = (PotionMeta)itemStack.getItemMeta();
        return pm.hasCustomEffects() && pm.hasCustomEffect(PotionEffectType.INVISIBILITY);
    }

    @Override
    public void registerEntities() {
        this.registerEntity("Silverfish2", 60, Silverfish.class);
        this.registerEntity("IGolem", 99, IGolem.class);
    }

    @Override
    public void spawnShop(Location loc, String name1, List<Player> players, IArena arena) {
        Location l = loc.clone();
        Villager vlg = (Villager)loc.getWorld().spawnEntity(loc, EntityType.VILLAGER);
        vlg.setAI(false);
        vlg.setRemoveWhenFarAway(false);
        vlg.setCollidable(false);
        vlg.setInvulnerable(true);
        vlg.setSilent(true);
        for (Player p : players) {
            ArmorStand a;
            String[] nume = Language.getMsg(p, name1).split(",");
            if (nume.length == 1) {
                a = v1_12_R1.createArmorStand(nume[0], l.clone().add(0.0, 1.85, 0.0));
                new ShopHolo(Language.getPlayerLanguage(p).getIso(), a, null, l, arena);
                continue;
            }
            a = v1_12_R1.createArmorStand(nume[0], l.clone().add(0.0, 2.1, 0.0));
            ArmorStand b = v1_12_R1.createArmorStand(nume[1], l.clone().add(0.0, 1.85, 0.0));
            new ShopHolo(Language.getPlayerLanguage(p).getIso(), a, b, l, arena);
        }
        for (ShopHolo sh : ShopHolo.getShopHolo()) {
            if (sh.getA() != arena) continue;
            sh.update();
        }
    }

    @Override
    public double getDamage(ItemStack i) {
        net.minecraft.server.v1_12_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy((ItemStack)i);
        NBTTagCompound compound = nmsStack.hasTag() ? nmsStack.getTag() : new NBTTagCompound();
        return compound.getDouble("generic.attackDamage");
    }

    private static ArmorStand createArmorStand(String name, Location loc) {
        ArmorStand a = (ArmorStand)loc.getWorld().spawn(loc, ArmorStand.class);
        a.setGravity(false);
        a.setVisible(false);
        a.setCustomNameVisible(true);
        a.setCustomName(name);
        return a;
    }

    private void registerEntity(String name, int id, Class customClass) {
        EntityTypes.b.a(id, (Object)new MinecraftKey(name), (Object)customClass);
    }

    private void spawnVillager(Location loc) {
        Villager vlg = (Villager)loc.getWorld().spawnEntity(loc, EntityType.VILLAGER);
        vlg.setAI(false);
        vlg.setRemoveWhenFarAway(false);
        vlg.setCollidable(false);
        vlg.setInvulnerable(true);
        vlg.setSilent(true);
    }

    @Override
    public void voidKill(Player p) {
        ((CraftPlayer)p).getHandle().damageEntity(DamageSource.OUT_OF_WORLD, 1000.0f);
    }

    @Override
    public void hideArmor(Player victim, Player receiver) {
        PacketPlayOutEntityEquipment helmet = new PacketPlayOutEntityEquipment(victim.getEntityId(), EnumItemSlot.HEAD, new net.minecraft.server.v1_12_R1.ItemStack(Item.getById((int)0)));
        PacketPlayOutEntityEquipment chest = new PacketPlayOutEntityEquipment(victim.getEntityId(), EnumItemSlot.CHEST, new net.minecraft.server.v1_12_R1.ItemStack(Item.getById((int)0)));
        PacketPlayOutEntityEquipment pants = new PacketPlayOutEntityEquipment(victim.getEntityId(), EnumItemSlot.LEGS, new net.minecraft.server.v1_12_R1.ItemStack(Item.getById((int)0)));
        PacketPlayOutEntityEquipment boots = new PacketPlayOutEntityEquipment(victim.getEntityId(), EnumItemSlot.FEET, new net.minecraft.server.v1_12_R1.ItemStack(Item.getById((int)0)));
        EntityPlayer pc = ((CraftPlayer)receiver).getHandle();
        pc.playerConnection.sendPacket((Packet)helmet);
        pc.playerConnection.sendPacket((Packet)chest);
        pc.playerConnection.sendPacket((Packet)pants);
        pc.playerConnection.sendPacket((Packet)boots);
    }

    @Override
    public void showArmor(Player victim, Player receiver) {
        PacketPlayOutEntityEquipment helmet = new PacketPlayOutEntityEquipment(victim.getEntityId(), EnumItemSlot.HEAD, CraftItemStack.asNMSCopy((ItemStack)victim.getInventory().getHelmet()));
        PacketPlayOutEntityEquipment chest = new PacketPlayOutEntityEquipment(victim.getEntityId(), EnumItemSlot.CHEST, CraftItemStack.asNMSCopy((ItemStack)victim.getInventory().getChestplate()));
        PacketPlayOutEntityEquipment pants = new PacketPlayOutEntityEquipment(victim.getEntityId(), EnumItemSlot.LEGS, CraftItemStack.asNMSCopy((ItemStack)victim.getInventory().getLeggings()));
        PacketPlayOutEntityEquipment boots = new PacketPlayOutEntityEquipment(victim.getEntityId(), EnumItemSlot.FEET, CraftItemStack.asNMSCopy((ItemStack)victim.getInventory().getBoots()));
        EntityPlayer pc = ((CraftPlayer)receiver).getHandle();
        pc.playerConnection.sendPacket((Packet)helmet);
        pc.playerConnection.sendPacket((Packet)chest);
        pc.playerConnection.sendPacket((Packet)pants);
        pc.playerConnection.sendPacket((Packet)boots);
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
                ((Bed)bed).setColor(bwt.getColor().dye());
                bed.update();
            }
        }
    }

    @Override
    public void registerTntWhitelist(float endStoneBlast, float glassBlast) {
        try {
            Field field = Block.class.getDeclaredField("durability");
            field.setAccessible(true);
            field.set(Block.getByName((String)"glass"), Float.valueOf(glassBlast));
            field.set(Block.getByName((String)"stained_glass"), Float.valueOf(glassBlast));
            field.set(Block.getByName((String)"end_stone"), Float.valueOf(endStoneBlast));
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setBlockTeamColor(org.bukkit.block.Block block, TeamColor teamColor) {
        block.setData(teamColor.itemByte());
    }

    @Override
    public void setCollide(Player p, IArena a, boolean value) {
        p.setCollidable(value);
        if (a == null) {
            return;
        }
        a.updateSpectatorCollideRule(p, value);
    }

    @Override
    public ItemStack addCustomData(ItemStack i, String data) {
        net.minecraft.server.v1_12_R1.ItemStack itemStack = CraftItemStack.asNMSCopy((ItemStack)i);
        NBTTagCompound tag = itemStack.getTag();
        if (tag == null) {
            tag = new NBTTagCompound();
        }
        tag.setString("BedWars1058", data);
        itemStack.setTag(tag);
        return CraftItemStack.asBukkitCopy((net.minecraft.server.v1_12_R1.ItemStack)itemStack);
    }

    @Override
    public ItemStack setTag(ItemStack i, String key, String value) {
        net.minecraft.server.v1_12_R1.ItemStack itemStack = CraftItemStack.asNMSCopy((ItemStack)i);
        NBTTagCompound tag = itemStack.getTag();
        if (tag == null) {
            tag = new NBTTagCompound();
        }
        tag.setString(key, value);
        itemStack.setTag(tag);
        return CraftItemStack.asBukkitCopy((net.minecraft.server.v1_12_R1.ItemStack)itemStack);
    }

    @Override
    public boolean isCustomBedWarsItem(ItemStack i) {
        net.minecraft.server.v1_12_R1.ItemStack itemStack = CraftItemStack.asNMSCopy((ItemStack)i);
        NBTTagCompound tag = itemStack.getTag();
        if (tag == null) {
            return false;
        }
        return tag.hasKey("BedWars1058");
    }

    @Override
    public String getCustomData(ItemStack i) {
        net.minecraft.server.v1_12_R1.ItemStack itemStack = CraftItemStack.asNMSCopy((ItemStack)i);
        NBTTagCompound tag = itemStack.getTag();
        if (tag == null) {
            return "";
        }
        return tag.getString("BedWars1058");
    }

    @Override
    public ItemStack colourItem(ItemStack itemStack, ITeam bedWarsTeam) {
        if (itemStack == null) {
            return null;
        }
        switch (itemStack.getType().toString()) {
            default: {
                return itemStack;
            }
            case "WOOL": 
            case "STAINED_CLAY": 
            case "STAINED_GLASS": {
                return new ItemStack(itemStack.getType(), itemStack.getAmount(), (short)bedWarsTeam.getColor().itemByte());
            }
            case "GLASS": 
        }
        return new ItemStack(Material.STAINED_GLASS, itemStack.getAmount(), (short)bedWarsTeam.getColor().itemByte());
    }

    @Override
    public ItemStack createItemStack(String material, int amount, short data) {
        ItemStack i;
        try {
            i = new ItemStack(Material.valueOf((String)material), amount, data);
        } catch (Exception ex) {
            this.getPlugin().getLogger().severe(material + " is not a valid " + this.getVersion() + " material!");
            i = new ItemStack(Material.BEDROCK);
        }
        return i;
    }

    @Override
    public boolean isPlayerHead(String material, int data) {
        return material.equals("SKULL_ITEM") && data == 3;
    }

    @Override
    public Material materialFireball() {
        return Material.FIREBALL;
    }

    @Override
    public Material materialPlayerHead() {
        return Material.SKULL_ITEM;
    }

    @Override
    public Material materialSnowball() {
        return Material.SNOW_BALL;
    }

    @Override
    public Material materialGoldenHelmet() {
        return Material.GOLD_HELMET;
    }

    @Override
    public Material materialGoldenChestPlate() {
        return Material.GOLD_CHESTPLATE;
    }

    @Override
    public Material materialGoldenLeggings() {
        return Material.GOLD_LEGGINGS;
    }

    @Override
    public Material materialNetheriteHelmet() {
        return Material.DIAMOND_HELMET;
    }

    @Override
    public Material materialNetheriteChestPlate() {
        return Material.DIAMOND_CHESTPLATE;
    }

    @Override
    public Material materialNetheriteLeggings() {
        return Material.DIAMOND_LEGGINGS;
    }

    @Override
    public Material materialElytra() {
        return Material.ELYTRA;
    }

    @Override
    public Material materialCake() {
        return Material.CAKE_BLOCK;
    }

    @Override
    public Material materialCraftingTable() {
        return Material.WORKBENCH;
    }

    @Override
    public Material materialEnchantingTable() {
        return Material.ENCHANTMENT_TABLE;
    }

    @Override
    public boolean isBed(Material material) {
        return material == Material.BED_BLOCK || material == Material.BED;
    }

    @Override
    public boolean itemStackDataCompare(ItemStack i, short data) {
        return i.getData().getData() == data;
    }

    @Override
    public void setJoinSignBackgroundBlockData(BlockState block, byte data) {
        block.getBlock().getRelative(((Sign)block.getData()).getAttachedFace()).setData(data, true);
    }

    @Override
    public Material woolMaterial() {
        return Material.WOOL;
    }

    @Override
    public String getShopUpgradeIdentifier(ItemStack itemStack) {
        net.minecraft.server.v1_12_R1.ItemStack i = CraftItemStack.asNMSCopy((ItemStack)itemStack);
        NBTTagCompound tag = i.getTag();
        return tag == null ? "" : (tag.hasKey("tierIdentifier") ? tag.getString("tierIdentifier") : "");
    }

    @Override
    public ItemStack setShopUpgradeIdentifier(ItemStack itemStack, String identifier) {
        net.minecraft.server.v1_12_R1.ItemStack i = CraftItemStack.asNMSCopy((ItemStack)itemStack);
        NBTTagCompound tag = i.getTag();
        if (tag == null) {
            tag = new NBTTagCompound();
        }
        tag.setString(VersionSupport.PLUGIN_TAG_TIER_KEY, identifier);
        i.setTag(tag);
        return CraftItemStack.asBukkitCopy((net.minecraft.server.v1_12_R1.ItemStack)i);
    }

    @Override
    public ItemStack getPlayerHead(Player player, ItemStack copyTagFrom) {
        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, 3);
        if (copyTagFrom != null) {
            net.minecraft.server.v1_12_R1.ItemStack i = CraftItemStack.asNMSCopy((ItemStack)head);
            i.setTag(CraftItemStack.asNMSCopy((ItemStack)copyTagFrom).getTag());
            head = CraftItemStack.asBukkitCopy((net.minecraft.server.v1_12_R1.ItemStack)i);
        }
        SkullMeta headMeta = (SkullMeta)head.getItemMeta();
        try {
            Field profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, ((CraftPlayer)player).getProfile());
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException e1) {
            e1.printStackTrace();
        }
        head.setItemMeta((ItemMeta)headMeta);
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
        if (arena.isReSpawning(respawned)) {
            return;
        }
        EntityPlayer entityPlayer = ((CraftPlayer)respawned).getHandle();
        PacketPlayOutNamedEntitySpawn show = new PacketPlayOutNamedEntitySpawn((EntityHuman)entityPlayer);
        PacketPlayOutEntityVelocity playerVelocity = new PacketPlayOutEntityVelocity((net.minecraft.server.v1_12_R1.Entity)entityPlayer);
        PacketPlayOutEntityHeadRotation head = new PacketPlayOutEntityHeadRotation((net.minecraft.server.v1_12_R1.Entity)entityPlayer, this.getCompressedAngle(entityPlayer.yaw));
        PacketPlayOutEntityEquipment hand1 = new PacketPlayOutEntityEquipment(entityPlayer.getId(), EnumItemSlot.MAINHAND, entityPlayer.inventory.getItemInHand());
        PacketPlayOutEntityEquipment hand2 = new PacketPlayOutEntityEquipment(entityPlayer.getId(), EnumItemSlot.OFFHAND, entityPlayer.getItemInOffHand());
        PacketPlayOutEntityEquipment helmet = new PacketPlayOutEntityEquipment(entityPlayer.getId(), EnumItemSlot.HEAD, (net.minecraft.server.v1_12_R1.ItemStack)entityPlayer.inventory.getArmorContents().get(3));
        PacketPlayOutEntityEquipment chest = new PacketPlayOutEntityEquipment(entityPlayer.getId(), EnumItemSlot.CHEST, (net.minecraft.server.v1_12_R1.ItemStack)entityPlayer.inventory.getArmorContents().get(2));
        PacketPlayOutEntityEquipment pants = new PacketPlayOutEntityEquipment(entityPlayer.getId(), EnumItemSlot.LEGS, (net.minecraft.server.v1_12_R1.ItemStack)entityPlayer.inventory.getArmorContents().get(1));
        PacketPlayOutEntityEquipment boots = new PacketPlayOutEntityEquipment(entityPlayer.getId(), EnumItemSlot.FEET, (net.minecraft.server.v1_12_R1.ItemStack)entityPlayer.inventory.getArmorContents().get(0));
        for (Player p : arena.getPlayers()) {
            if (p == null || p.equals((Object)respawned) || arena.isReSpawning(p)) continue;
            boundTo = ((CraftPlayer)p).getHandle();
            if (!p.getWorld().equals((Object)respawned.getWorld()) || !(respawned.getLocation().distance(p.getLocation()) <= (double)arena.getRenderDistance())) continue;
            boundTo.playerConnection.sendPacket((Packet)show);
            boundTo.playerConnection.sendPacket((Packet)playerVelocity);
            for (Packet packet : new Packet[]{hand1, helmet, chest, pants, boots, hand2, head}) {
                boundTo.playerConnection.sendPacket(packet);
            }
            if (p.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                this.hideArmor(p, respawned);
                continue;
            }
            PacketPlayOutNamedEntitySpawn show2 = new PacketPlayOutNamedEntitySpawn((EntityHuman)boundTo);
            PacketPlayOutEntityVelocity playerVelocity2 = new PacketPlayOutEntityVelocity((net.minecraft.server.v1_12_R1.Entity)boundTo);
            PacketPlayOutEntityHeadRotation head2 = new PacketPlayOutEntityHeadRotation((net.minecraft.server.v1_12_R1.Entity)boundTo, this.getCompressedAngle(boundTo.yaw));
            entityPlayer.playerConnection.sendPacket((Packet)show2);
            entityPlayer.playerConnection.sendPacket((Packet)playerVelocity2);
            entityPlayer.playerConnection.sendPacket((Packet)head2);
            this.showArmor(p, respawned);
        }
        for (Player spectator : arena.getSpectators()) {
            if (spectator == null || spectator.equals((Object)respawned)) continue;
            boundTo = ((CraftPlayer)spectator).getHandle();
            respawned.hidePlayer(this.getPlugin(), spectator);
            if (!spectator.getWorld().equals((Object)respawned.getWorld()) || !(respawned.getLocation().distance(spectator.getLocation()) <= (double)arena.getRenderDistance())) continue;
            boundTo.playerConnection.sendPacket((Packet)show);
            boundTo.playerConnection.sendPacket((Packet)playerVelocity);
            boundTo.playerConnection.sendPacket((Packet)new PacketPlayOutEntityHeadRotation((net.minecraft.server.v1_12_R1.Entity)entityPlayer, this.getCompressedAngle(entityPlayer.yaw)));
            for (Packet packet : new Packet[]{hand1, helmet, chest, pants, boots, hand2}) {
                boundTo.playerConnection.sendPacket(packet);
            }
        }
    }

    @Override
    public String getInventoryName(InventoryEvent e) {
        return e.getInventory().getName();
    }

    @Override
    public void setUnbreakable(ItemMeta itemMeta) {
        itemMeta.setUnbreakable(true);
    }

    @Override
    public String getMainLevel() {
        return ((DedicatedServer)MinecraftServer.getServer()).propertyManager.properties.getProperty("level-name");
    }

    @Override
    public int getVersion() {
        return 5;
    }

    @Override
    public void setJoinSignBackground(BlockState b, Material material) {
        b.getLocation().getBlock().getRelative(((Sign)b.getData()).getAttachedFace()).setType(material);
    }

    @Override
    public void spigotShowPlayer(Player victim, Player receiver) {
        receiver.showPlayer(this.getPlugin(), victim);
    }

    @Override
    public void spigotHidePlayer(Player victim, Player receiver) {
        receiver.hidePlayer(this.getPlugin(), victim);
    }

    @Override
    public Fireball setFireballDirection(Fireball fireball, Vector vector) {
        EntityFireball fb = ((CraftFireball)fireball).getHandle();
        fb.dirX = vector.getX() * 0.1;
        fb.dirY = vector.getY() * 0.1;
        fb.dirZ = vector.getZ() * 0.1;
        return (Fireball)fb.getBukkitEntity();
    }

    @Override
    public void playRedStoneDot(Player player) {
        Color color = Color.RED;
        PacketPlayOutWorldParticles particlePacket = new PacketPlayOutWorldParticles(EnumParticle.REDSTONE, true, (float)player.getLocation().getX(), (float)(player.getLocation().getY() + 2.6), (float)player.getLocation().getZ(), (float)color.getRed(), (float)color.getRed(), (float)color.getRed(), 0.0f, 0, new int[0]);
        for (Player inWorld : player.getWorld().getPlayers()) {
            if (inWorld.equals((Object)player)) continue;
            ((CraftPlayer)inWorld).getHandle().playerConnection.sendPacket((Packet)particlePacket);
        }
    }

    @Override
    public void clearArrowsFromPlayerBody(Player player) {
        ((CraftLivingEntity)player).getHandle().getDataWatcher().set(new DataWatcherObject(10, DataWatcherRegistry.b), (Object)-1);
    }

    @Override
    public void placeTowerBlocks(org.bukkit.block.Block b, IArena a, TeamColor color, int x, int y, int z) {
        b.getRelative(x, y, z).setType(Material.WOOL);
        this.setBlockTeamColor(b.getRelative(x, y, z), color);
        a.addPlacedBlock(b.getRelative(x, y, z));
    }

    @Override
    public void placeLadder(org.bukkit.block.Block b, int x, int y, int z, IArena a, int ladderdata) {
        b.getRelative(x, y, z).setType(Material.LADDER);
        b.getRelative(x, y, z).setData((byte)ladderdata);
        a.addPlacedBlock(b.getRelative(x, y, z));
    }

    @Override
    public void playVillagerEffect(Player player, Location location) {
        player.spawnParticle(Particle.VILLAGER_HAPPY, location, 1);
    }
}

