/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraft.server.v1_8_R3.Block
 *  net.minecraft.server.v1_8_R3.DamageSource
 *  net.minecraft.server.v1_8_R3.DedicatedServer
 *  net.minecraft.server.v1_8_R3.Entity
 *  net.minecraft.server.v1_8_R3.EntityFireball
 *  net.minecraft.server.v1_8_R3.EntityHuman
 *  net.minecraft.server.v1_8_R3.EntityLiving
 *  net.minecraft.server.v1_8_R3.EntityPlayer
 *  net.minecraft.server.v1_8_R3.EntityTNTPrimed
 *  net.minecraft.server.v1_8_R3.EntityTypes
 *  net.minecraft.server.v1_8_R3.EntityVillager
 *  net.minecraft.server.v1_8_R3.EnumParticle
 *  net.minecraft.server.v1_8_R3.GenericAttributes
 *  net.minecraft.server.v1_8_R3.IChatBaseComponent
 *  net.minecraft.server.v1_8_R3.IChatBaseComponent$ChatSerializer
 *  net.minecraft.server.v1_8_R3.IProjectile
 *  net.minecraft.server.v1_8_R3.ItemArmor
 *  net.minecraft.server.v1_8_R3.ItemAxe
 *  net.minecraft.server.v1_8_R3.ItemBow
 *  net.minecraft.server.v1_8_R3.ItemStack
 *  net.minecraft.server.v1_8_R3.ItemSword
 *  net.minecraft.server.v1_8_R3.ItemTool
 *  net.minecraft.server.v1_8_R3.MinecraftServer
 *  net.minecraft.server.v1_8_R3.NBTTagCompound
 *  net.minecraft.server.v1_8_R3.Packet
 *  net.minecraft.server.v1_8_R3.PacketPlayOutChat
 *  net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy
 *  net.minecraft.server.v1_8_R3.PacketPlayOutEntityEquipment
 *  net.minecraft.server.v1_8_R3.PacketPlayOutEntityHeadRotation
 *  net.minecraft.server.v1_8_R3.PacketPlayOutEntityVelocity
 *  net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn
 *  net.minecraft.server.v1_8_R3.PacketPlayOutTitle
 *  net.minecraft.server.v1_8_R3.PacketPlayOutTitle$EnumTitleAction
 *  net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles
 *  net.minecraft.server.v1_8_R3.PathfinderGoalSelector
 *  net.minecraft.server.v1_8_R3.PlayerConnection
 *  net.minecraft.server.v1_8_R3.World
 *  org.bukkit.Color
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.block.BlockState
 *  org.bukkit.command.Command
 *  org.bukkit.craftbukkit.v1_8_R3.CraftServer
 *  org.bukkit.craftbukkit.v1_8_R3.CraftWorld
 *  org.bukkit.craftbukkit.v1_8_R3.entity.CraftFireball
 *  org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity
 *  org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
 *  org.bukkit.craftbukkit.v1_8_R3.entity.CraftTNTPrimed
 *  org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack
 *  org.bukkit.craftbukkit.v1_8_R3.util.UnsafeList
 *  org.bukkit.entity.ArmorStand
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.Fireball
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.TNTPrimed
 *  org.bukkit.event.entity.CreatureSpawnEvent$SpawnReason
 *  org.bukkit.event.inventory.InventoryEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.inventory.meta.PotionMeta
 *  org.bukkit.inventory.meta.SkullMeta
 *  org.bukkit.material.Sign
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.Potion
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.potion.PotionType
 *  org.bukkit.util.Vector
 */
package com.andrei1058.bedwars.support.version.v1_8_R3;

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
import com.andrei1058.bedwars.support.version.v1_8_R3.IGolem;
import com.andrei1058.bedwars.support.version.v1_8_R3.Silverfish;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import net.minecraft.server.v1_8_R3.Block;
import net.minecraft.server.v1_8_R3.DamageSource;
import net.minecraft.server.v1_8_R3.DedicatedServer;
import net.minecraft.server.v1_8_R3.EntityFireball;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.EntityTNTPrimed;
import net.minecraft.server.v1_8_R3.EntityTypes;
import net.minecraft.server.v1_8_R3.EntityVillager;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.GenericAttributes;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IProjectile;
import net.minecraft.server.v1_8_R3.ItemArmor;
import net.minecraft.server.v1_8_R3.ItemAxe;
import net.minecraft.server.v1_8_R3.ItemBow;
import net.minecraft.server.v1_8_R3.ItemSword;
import net.minecraft.server.v1_8_R3.ItemTool;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityEquipment;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityVelocity;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import net.minecraft.server.v1_8_R3.PathfinderGoalSelector;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.command.Command;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftFireball;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftTNTPrimed;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_8_R3.util.UnsafeList;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.Sign;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.util.Vector;

@Deprecated(forRemoval=true)
public class v1_8_R3
extends VersionSupport {
    public v1_8_R3(Plugin pl, String name) {
        super(pl, name);
        try {
            this.setEggBridgeEffect("MOBSPAWNER_FLAMES");
        } catch (InvalidEffectException e) {
            e.printStackTrace();
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
    public void playAction(Player p, String text) {
        CraftPlayer cPlayer = (CraftPlayer)p;
        IChatBaseComponent cbc = IChatBaseComponent.ChatSerializer.a((String)("{\"text\": \"" + text + "\"}"));
        PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, 2);
        cPlayer.getHandle().playerConnection.sendPacket((Packet)ppoc);
    }

    @Override
    public boolean isBukkitCommandRegistered(String name) {
        return ((CraftServer)this.getPlugin().getServer()).getCommandMap().getCommand(name) != null;
    }

    @Override
    public ItemStack getItemInHand(Player p) {
        return p.getItemInHand();
    }

    @Override
    public void hideEntity(Entity e, Player p) {
        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(new int[]{e.getEntityId()});
        ((CraftPlayer)p).getHandle().playerConnection.sendPacket((Packet)packet);
    }

    @Override
    public boolean isArmor(ItemStack itemStack) {
        if (CraftItemStack.asNMSCopy((ItemStack)itemStack) == null) {
            return false;
        }
        if (CraftItemStack.asNMSCopy((ItemStack)itemStack).getItem() == null) {
            return false;
        }
        return CraftItemStack.asNMSCopy((ItemStack)itemStack).getItem() instanceof ItemArmor;
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
        if (pm != null && pm.hasCustomEffects()) {
            return pm.hasCustomEffect(PotionEffectType.INVISIBILITY);
        }
        Potion potion = Potion.fromItemStack((ItemStack)itemStack);
        PotionType type = potion.getType();
        return type.getEffectType().equals((Object)PotionEffectType.INVISIBILITY);
    }

    @Override
    public boolean isGlass(Material type) {
        return type == Material.GLASS || type == Material.STAINED_GLASS;
    }

    @Override
    public void registerEntities() {
        this.registerEntity("Silverfish2", 60, Silverfish.class);
        this.registerEntity("IGolem", 99, IGolem.class);
        this.registerEntity("BwVilager", 120, VillagerShop.class);
    }

    @Override
    public void setCollide(Player p, IArena a, boolean value) {
        p.spigot().setCollidesWithEntities(value);
    }

    @Override
    public void minusAmount(Player p, ItemStack i, int amount) {
        if (i.getAmount() - amount <= 0) {
            p.getInventory().removeItem(new ItemStack[]{i});
            return;
        }
        i.setAmount(i.getAmount() - amount);
        p.updateInventory();
    }

    private void spawnVillager(Location loc) {
        VillagerShop nmsEntity = new VillagerShop(loc);
        NBTTagCompound tag = nmsEntity.getNBTTag();
        if (tag == null) {
            tag = new NBTTagCompound();
        }
        nmsEntity.c(tag);
        tag.setInt("NoAI", 1);
        nmsEntity.f(tag);
        ((CraftLivingEntity)nmsEntity.getBukkitEntity()).setRemoveWhenFarAway(false);
    }

    @Override
    public void spawnShop(Location loc, String name1, List<Player> players, IArena arena) {
        Location l = loc.clone();
        this.spawnVillager(l);
        for (Player p : players) {
            ArmorStand a;
            String[] nume = Language.getMsg(p, name1).split(",");
            if (nume.length == 1) {
                a = v1_8_R3.createArmorStand(nume[0], l.clone().add(0.0, 1.85, 0.0));
                new ShopHolo(Language.getPlayerLanguage(p).getIso(), a, null, l, arena);
                continue;
            }
            a = v1_8_R3.createArmorStand(nume[0], l.clone().add(0.0, 2.1, 0.0));
            ArmorStand b = v1_8_R3.createArmorStand(nume[1], l.clone().add(0.0, 1.85, 0.0));
            new ShopHolo(Language.getPlayerLanguage(p).getIso(), a, b, l, arena);
        }
        for (ShopHolo sh : ShopHolo.getShopHolo()) {
            if (sh.getA() != arena) continue;
            sh.update();
        }
    }

    @Override
    public double getDamage(ItemStack i) {
        net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy((ItemStack)i);
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
        try {
            ArrayList<Map> dataMap = new ArrayList<Map>();
            for (Field f : EntityTypes.class.getDeclaredFields()) {
                if (!f.getType().getSimpleName().equals(Map.class.getSimpleName())) continue;
                f.setAccessible(true);
                dataMap.add((Map)f.get(null));
            }
            if (((Map)dataMap.get(2)).containsKey(id)) {
                ((Map)dataMap.get(0)).remove(name);
                ((Map)dataMap.get(2)).remove(id);
            }
            Method method = EntityTypes.class.getDeclaredMethod("a", Class.class, String.class, Integer.TYPE);
            method.setAccessible(true);
            method.invoke(null, customClass, name, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    public void voidKill(Player p) {
        ((CraftPlayer)p).getHandle().damageEntity(DamageSource.OUT_OF_WORLD, 1000.0f);
    }

    @Override
    public void hideArmor(Player victim, Player receiver) {
        if (victim.equals((Object)receiver)) {
            return;
        }
        PacketPlayOutEntityEquipment helmet = new PacketPlayOutEntityEquipment(victim.getEntityId(), 1, CraftItemStack.asNMSCopy((ItemStack)new ItemStack(Material.AIR)));
        PacketPlayOutEntityEquipment chest = new PacketPlayOutEntityEquipment(victim.getEntityId(), 2, CraftItemStack.asNMSCopy((ItemStack)new ItemStack(Material.AIR)));
        PacketPlayOutEntityEquipment pants = new PacketPlayOutEntityEquipment(victim.getEntityId(), 3, CraftItemStack.asNMSCopy((ItemStack)new ItemStack(Material.AIR)));
        PacketPlayOutEntityEquipment boots = new PacketPlayOutEntityEquipment(victim.getEntityId(), 4, CraftItemStack.asNMSCopy((ItemStack)new ItemStack(Material.AIR)));
        PlayerConnection boundTo = ((CraftPlayer)receiver).getHandle().playerConnection;
        boundTo.sendPacket((Packet)helmet);
        boundTo.sendPacket((Packet)chest);
        boundTo.sendPacket((Packet)pants);
        boundTo.sendPacket((Packet)boots);
    }

    @Override
    public void showArmor(Player victim, Player receiver) {
        if (victim.equals((Object)receiver)) {
            return;
        }
        EntityPlayer entityPlayer = ((CraftPlayer)victim).getHandle();
        PacketPlayOutEntityEquipment helmet = new PacketPlayOutEntityEquipment(entityPlayer.getId(), 4, entityPlayer.inventory.getArmorContents()[3]);
        PacketPlayOutEntityEquipment chest = new PacketPlayOutEntityEquipment(entityPlayer.getId(), 3, entityPlayer.inventory.getArmorContents()[2]);
        PacketPlayOutEntityEquipment pants = new PacketPlayOutEntityEquipment(entityPlayer.getId(), 2, entityPlayer.inventory.getArmorContents()[1]);
        PacketPlayOutEntityEquipment boots = new PacketPlayOutEntityEquipment(entityPlayer.getId(), 1, entityPlayer.inventory.getArmorContents()[0]);
        EntityPlayer boundTo = ((CraftPlayer)receiver).getHandle();
        boundTo.playerConnection.sendPacket((Packet)helmet);
        boundTo.playerConnection.sendPacket((Packet)chest);
        boundTo.playerConnection.sendPacket((Packet)pants);
        boundTo.playerConnection.sendPacket((Packet)boots);
    }

    @Override
    public void spawnDragon(Location l, ITeam bwt) {
        l.getWorld().spawnEntity(l, EntityType.ENDER_DRAGON);
    }

    @Override
    public void colorBed(ITeam bwt) {
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
    public ItemStack addCustomData(ItemStack i, String data) {
        net.minecraft.server.v1_8_R3.ItemStack itemStack = CraftItemStack.asNMSCopy((ItemStack)i);
        NBTTagCompound tag = itemStack.getTag();
        if (tag == null) {
            tag = new NBTTagCompound();
        }
        tag.setString("BedWars1058", data);
        itemStack.setTag(tag);
        return CraftItemStack.asBukkitCopy((net.minecraft.server.v1_8_R3.ItemStack)itemStack);
    }

    @Override
    public ItemStack setTag(ItemStack itemStack, String key, String value) {
        net.minecraft.server.v1_8_R3.ItemStack is = CraftItemStack.asNMSCopy((ItemStack)itemStack);
        NBTTagCompound tag = is.getTag();
        if (tag == null) {
            tag = new NBTTagCompound();
        }
        tag.setString(key, value);
        is.setTag(tag);
        return CraftItemStack.asBukkitCopy((net.minecraft.server.v1_8_R3.ItemStack)is);
    }

    @Override
    public String getTag(ItemStack itemStack, String key) {
        net.minecraft.server.v1_8_R3.ItemStack i = CraftItemStack.asNMSCopy((ItemStack)itemStack);
        if (i == null) {
            return null;
        }
        NBTTagCompound tag = i.getTag();
        return tag == null ? null : (tag.hasKey(key) ? tag.getString(key) : null);
    }

    @Override
    public boolean isCustomBedWarsItem(ItemStack i) {
        net.minecraft.server.v1_8_R3.ItemStack itemStack = CraftItemStack.asNMSCopy((ItemStack)i);
        if (itemStack == null) {
            return false;
        }
        NBTTagCompound tag = itemStack.getTag();
        if (tag == null) {
            return false;
        }
        return tag.hasKey("BedWars1058");
    }

    @Override
    public String getCustomData(ItemStack i) {
        net.minecraft.server.v1_8_R3.ItemStack itemStack = CraftItemStack.asNMSCopy((ItemStack)i);
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
            this.getPlugin().getLogger().log(Level.WARNING, material + " is not a valid " + v1_8_R3.getName() + " material!");
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
        return null;
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
    public void setJoinSignBackground(BlockState b, Material material) {
        b.getLocation().getBlock().getRelative(((Sign)b.getData()).getAttachedFace()).setType(material);
    }

    @Override
    public Material woolMaterial() {
        return Material.WOOL;
    }

    @Override
    public String getShopUpgradeIdentifier(ItemStack itemStack) {
        net.minecraft.server.v1_8_R3.ItemStack i = CraftItemStack.asNMSCopy((ItemStack)itemStack);
        NBTTagCompound tag = i.getTag();
        return tag == null ? "" : (tag.hasKey("tierIdentifier") ? tag.getString("tierIdentifier") : "");
    }

    @Override
    public ItemStack setShopUpgradeIdentifier(ItemStack itemStack, String identifier) {
        net.minecraft.server.v1_8_R3.ItemStack i = CraftItemStack.asNMSCopy((ItemStack)itemStack);
        NBTTagCompound tag = i.getTag();
        if (tag == null) {
            tag = new NBTTagCompound();
        }
        tag.setString(VersionSupport.PLUGIN_TAG_TIER_KEY, identifier);
        i.setTag(tag);
        return CraftItemStack.asBukkitCopy((net.minecraft.server.v1_8_R3.ItemStack)i);
    }

    @Override
    public ItemStack getPlayerHead(Player player, ItemStack copyTagFrom) {
        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, 3);
        if (copyTagFrom != null) {
            net.minecraft.server.v1_8_R3.ItemStack i = CraftItemStack.asNMSCopy((ItemStack)head);
            i.setTag(CraftItemStack.asNMSCopy((ItemStack)copyTagFrom).getTag());
            head = CraftItemStack.asBukkitCopy((net.minecraft.server.v1_8_R3.ItemStack)i);
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
        if (arena.getRespawnSessions().containsKey(respawned)) {
            return;
        }
        EntityPlayer entityPlayer = ((CraftPlayer)respawned).getHandle();
        PacketPlayOutNamedEntitySpawn show = new PacketPlayOutNamedEntitySpawn((EntityHuman)entityPlayer);
        PacketPlayOutEntityVelocity playerVelocity = new PacketPlayOutEntityVelocity((net.minecraft.server.v1_8_R3.Entity)entityPlayer);
        PacketPlayOutEntityHeadRotation head = new PacketPlayOutEntityHeadRotation((net.minecraft.server.v1_8_R3.Entity)entityPlayer, this.getCompressedAngle(entityPlayer.yaw));
        PacketPlayOutEntityEquipment hand1 = new PacketPlayOutEntityEquipment(entityPlayer.getId(), 0, entityPlayer.inventory.getItemInHand());
        PacketPlayOutEntityEquipment helmet = new PacketPlayOutEntityEquipment(entityPlayer.getId(), 4, entityPlayer.inventory.getArmorContents()[3]);
        PacketPlayOutEntityEquipment chest = new PacketPlayOutEntityEquipment(entityPlayer.getId(), 3, entityPlayer.inventory.getArmorContents()[2]);
        PacketPlayOutEntityEquipment pants = new PacketPlayOutEntityEquipment(entityPlayer.getId(), 2, entityPlayer.inventory.getArmorContents()[1]);
        PacketPlayOutEntityEquipment boots = new PacketPlayOutEntityEquipment(entityPlayer.getId(), 1, entityPlayer.inventory.getArmorContents()[0]);
        for (Player p : arena.getPlayers()) {
            if (p == null || p.equals((Object)respawned) || arena.getRespawnSessions().containsKey(p)) continue;
            boundTo = ((CraftPlayer)p).getHandle();
            if (!p.getWorld().equals((Object)respawned.getWorld()) || !(respawned.getLocation().distance(p.getLocation()) <= (double)arena.getRenderDistance())) continue;
            boundTo.playerConnection.sendPacket((Packet)show);
            boundTo.playerConnection.sendPacket((Packet)playerVelocity);
            for (Packet packet : new Packet[]{hand1, helmet, chest, pants, boots, head}) {
                boundTo.playerConnection.sendPacket(packet);
            }
            if (p.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                this.hideArmor(p, respawned);
                continue;
            }
            PacketPlayOutNamedEntitySpawn show2 = new PacketPlayOutNamedEntitySpawn((EntityHuman)boundTo);
            PacketPlayOutEntityVelocity playerVelocity2 = new PacketPlayOutEntityVelocity((net.minecraft.server.v1_8_R3.Entity)boundTo);
            PacketPlayOutEntityHeadRotation head2 = new PacketPlayOutEntityHeadRotation((net.minecraft.server.v1_8_R3.Entity)boundTo, this.getCompressedAngle(boundTo.yaw));
            entityPlayer.playerConnection.sendPacket((Packet)show2);
            entityPlayer.playerConnection.sendPacket((Packet)playerVelocity2);
            entityPlayer.playerConnection.sendPacket((Packet)head2);
            this.showArmor(p, respawned);
        }
        for (Player spectator : arena.getSpectators()) {
            if (spectator == null || spectator.equals((Object)respawned)) continue;
            boundTo = ((CraftPlayer)spectator).getHandle();
            respawned.hidePlayer(spectator);
            if (!spectator.getWorld().equals((Object)respawned.getWorld()) || !(respawned.getLocation().distance(spectator.getLocation()) <= (double)arena.getRenderDistance())) continue;
            boundTo.playerConnection.sendPacket((Packet)show);
            boundTo.playerConnection.sendPacket((Packet)playerVelocity);
            boundTo.playerConnection.sendPacket((Packet)new PacketPlayOutEntityHeadRotation((net.minecraft.server.v1_8_R3.Entity)entityPlayer, this.getCompressedAngle(entityPlayer.yaw)));
            for (Packet packet : new Packet[]{hand1, helmet, chest, pants, boots}) {
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
        itemMeta.spigot().setUnbreakable(true);
    }

    @Override
    public int getVersion() {
        return 0;
    }

    @Override
    public void registerVersionListeners() {
        new VersionCommon(this);
    }

    @Override
    public String getMainLevel() {
        return ((DedicatedServer)MinecraftServer.getServer()).propertyManager.properties.getProperty("level-name");
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
        ((CraftLivingEntity)player).getHandle().getDataWatcher().watch(9, (Object)-1);
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
        PacketPlayOutWorldParticles pwp = new PacketPlayOutWorldParticles(EnumParticle.VILLAGER_HAPPY, true, (float)location.getX(), (float)location.getY(), (float)location.getZ(), 0.0f, 0.0f, 0.0f, 0.0f, 1, new int[0]);
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket((Packet)pwp);
    }

    public static class VillagerShop
    extends EntityVillager {
        VillagerShop(Location loc) {
            super((World)((CraftWorld)loc.getWorld()).getHandle());
            try {
                Field bField = PathfinderGoalSelector.class.getDeclaredField("b");
                bField.setAccessible(true);
                Field cField = PathfinderGoalSelector.class.getDeclaredField("c");
                cField.setAccessible(true);
                bField.set(this.goalSelector, new UnsafeList());
                bField.set(this.targetSelector, new UnsafeList());
                cField.set(this.goalSelector, new UnsafeList());
                cField.set(this.targetSelector, new UnsafeList());
            } catch (Exception exception) {
                // empty catch block
            }
            this.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
            this.setPositionRotation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
            ((CraftWorld)loc.getWorld()).getHandle().addEntity((net.minecraft.server.v1_8_R3.Entity)this, CreatureSpawnEvent.SpawnReason.CUSTOM);
            this.persistent = true;
        }

        public void move(double d0, double d1, double d2) {
        }

        public void collide(net.minecraft.server.v1_8_R3.Entity entity) {
        }

        public boolean damageEntity(DamageSource damagesource, float f) {
            return false;
        }

        public void g(double d0, double d1, double d2) {
        }

        public void makeSound(String s, float f, float f1) {
        }

        protected void initAttributes() {
            super.initAttributes();
            this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.0);
        }
    }
}

