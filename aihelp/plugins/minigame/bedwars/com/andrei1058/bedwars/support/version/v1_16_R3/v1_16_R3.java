/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.DataFixUtils
 *  com.mojang.datafixers.types.Type
 *  com.mojang.datafixers.util.Pair
 *  net.minecraft.server.v1_16_R3.Block
 *  net.minecraft.server.v1_16_R3.BlockBase
 *  net.minecraft.server.v1_16_R3.Blocks
 *  net.minecraft.server.v1_16_R3.ChatMessageType
 *  net.minecraft.server.v1_16_R3.DamageSource
 *  net.minecraft.server.v1_16_R3.DataConverterRegistry
 *  net.minecraft.server.v1_16_R3.DataConverterTypes
 *  net.minecraft.server.v1_16_R3.DataWatcherObject
 *  net.minecraft.server.v1_16_R3.DataWatcherRegistry
 *  net.minecraft.server.v1_16_R3.DedicatedServer
 *  net.minecraft.server.v1_16_R3.Entity
 *  net.minecraft.server.v1_16_R3.EntityFireball
 *  net.minecraft.server.v1_16_R3.EntityHuman
 *  net.minecraft.server.v1_16_R3.EntityLiving
 *  net.minecraft.server.v1_16_R3.EntityPlayer
 *  net.minecraft.server.v1_16_R3.EntityTNTPrimed
 *  net.minecraft.server.v1_16_R3.EntityTypes$Builder
 *  net.minecraft.server.v1_16_R3.EnumCreatureType
 *  net.minecraft.server.v1_16_R3.EnumItemSlot
 *  net.minecraft.server.v1_16_R3.IChatBaseComponent
 *  net.minecraft.server.v1_16_R3.IChatBaseComponent$ChatSerializer
 *  net.minecraft.server.v1_16_R3.IChatMutableComponent
 *  net.minecraft.server.v1_16_R3.IMaterial
 *  net.minecraft.server.v1_16_R3.IProjectile
 *  net.minecraft.server.v1_16_R3.Item
 *  net.minecraft.server.v1_16_R3.ItemArmor
 *  net.minecraft.server.v1_16_R3.ItemAxe
 *  net.minecraft.server.v1_16_R3.ItemBow
 *  net.minecraft.server.v1_16_R3.ItemElytra
 *  net.minecraft.server.v1_16_R3.ItemStack
 *  net.minecraft.server.v1_16_R3.ItemSword
 *  net.minecraft.server.v1_16_R3.ItemTool
 *  net.minecraft.server.v1_16_R3.MinecraftServer
 *  net.minecraft.server.v1_16_R3.NBTTagCompound
 *  net.minecraft.server.v1_16_R3.Packet
 *  net.minecraft.server.v1_16_R3.PacketPlayOutChat
 *  net.minecraft.server.v1_16_R3.PacketPlayOutEntityDestroy
 *  net.minecraft.server.v1_16_R3.PacketPlayOutEntityEquipment
 *  net.minecraft.server.v1_16_R3.PacketPlayOutEntityHeadRotation
 *  net.minecraft.server.v1_16_R3.PacketPlayOutEntityVelocity
 *  net.minecraft.server.v1_16_R3.PacketPlayOutNamedEntitySpawn
 *  net.minecraft.server.v1_16_R3.PacketPlayOutWorldParticles
 *  net.minecraft.server.v1_16_R3.ParticleParam
 *  net.minecraft.server.v1_16_R3.ParticleParamRedstone
 *  net.minecraft.server.v1_16_R3.SharedConstants
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
 *  org.bukkit.craftbukkit.v1_16_R3.CraftServer
 *  org.bukkit.craftbukkit.v1_16_R3.entity.CraftFireball
 *  org.bukkit.craftbukkit.v1_16_R3.entity.CraftLivingEntity
 *  org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer
 *  org.bukkit.craftbukkit.v1_16_R3.entity.CraftTNTPrimed
 *  org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack
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
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.util.Vector
 *  org.jetbrains.annotations.NotNull
 */
package com.andrei1058.bedwars.support.version.v1_16_R3;

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
import com.andrei1058.bedwars.support.version.v1_16_R3.IGolem;
import com.andrei1058.bedwars.support.version.v1_16_R3.Silverfish;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.util.Pair;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import net.minecraft.server.v1_16_R3.Block;
import net.minecraft.server.v1_16_R3.BlockBase;
import net.minecraft.server.v1_16_R3.Blocks;
import net.minecraft.server.v1_16_R3.ChatMessageType;
import net.minecraft.server.v1_16_R3.DamageSource;
import net.minecraft.server.v1_16_R3.DataConverterRegistry;
import net.minecraft.server.v1_16_R3.DataConverterTypes;
import net.minecraft.server.v1_16_R3.DataWatcherObject;
import net.minecraft.server.v1_16_R3.DataWatcherRegistry;
import net.minecraft.server.v1_16_R3.DedicatedServer;
import net.minecraft.server.v1_16_R3.Entity;
import net.minecraft.server.v1_16_R3.EntityFireball;
import net.minecraft.server.v1_16_R3.EntityHuman;
import net.minecraft.server.v1_16_R3.EntityLiving;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.EntityTNTPrimed;
import net.minecraft.server.v1_16_R3.EntityTypes;
import net.minecraft.server.v1_16_R3.EnumCreatureType;
import net.minecraft.server.v1_16_R3.EnumItemSlot;
import net.minecraft.server.v1_16_R3.IChatBaseComponent;
import net.minecraft.server.v1_16_R3.IChatMutableComponent;
import net.minecraft.server.v1_16_R3.IMaterial;
import net.minecraft.server.v1_16_R3.IProjectile;
import net.minecraft.server.v1_16_R3.Item;
import net.minecraft.server.v1_16_R3.ItemArmor;
import net.minecraft.server.v1_16_R3.ItemAxe;
import net.minecraft.server.v1_16_R3.ItemBow;
import net.minecraft.server.v1_16_R3.ItemElytra;
import net.minecraft.server.v1_16_R3.ItemSword;
import net.minecraft.server.v1_16_R3.ItemTool;
import net.minecraft.server.v1_16_R3.MinecraftServer;
import net.minecraft.server.v1_16_R3.NBTTagCompound;
import net.minecraft.server.v1_16_R3.Packet;
import net.minecraft.server.v1_16_R3.PacketPlayOutChat;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityEquipment;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityVelocity;
import net.minecraft.server.v1_16_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_16_R3.PacketPlayOutWorldParticles;
import net.minecraft.server.v1_16_R3.ParticleParam;
import net.minecraft.server.v1_16_R3.ParticleParamRedstone;
import net.minecraft.server.v1_16_R3.SharedConstants;
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
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftFireball;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftTNTPrimed;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EnderDragon;
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
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class v1_16_R3
extends VersionSupport {
    private static final UUID chatUUID = new UUID(0L, 0L);

    public v1_16_R3(Plugin plugin, String name) {
        super(plugin, name);
        this.loadDefaultEffects();
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
    public String getTag(ItemStack itemStack, String key) {
        net.minecraft.server.v1_16_R3.ItemStack i = CraftItemStack.asNMSCopy((ItemStack)itemStack);
        if (i == null) {
            return null;
        }
        NBTTagCompound tag = i.getTag();
        return tag == null ? null : (tag.hasKey(key) ? tag.getString(key) : null);
    }

    @Override
    public void sendTitle(Player p, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        p.sendTitle(title == null ? " " : title, subtitle == null ? " " : subtitle, fadeIn, stay, fadeOut);
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
        IChatMutableComponent cbc = IChatBaseComponent.ChatSerializer.a((String)("{\"text\": \"" + text + "\"}"));
        PacketPlayOutChat ppoc = new PacketPlayOutChat((IChatBaseComponent)cbc, ChatMessageType.GAME_INFO, chatUUID);
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
    public void hideEntity(org.bukkit.entity.Entity e, Player p) {
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
        if (CraftItemStack.asNMSCopy((ItemStack)itemStack).A() == null) {
            return false;
        }
        return CraftItemStack.asNMSCopy((ItemStack)itemStack).A() instanceof IProjectile;
    }

    @Override
    public boolean isInvisibilityPotion(ItemStack itemStack) {
        if (!itemStack.getType().equals((Object)Material.POTION)) {
            return false;
        }
        PotionMeta pm = (PotionMeta)itemStack.getItemMeta();
        return pm != null && pm.hasCustomEffects() && pm.hasCustomEffect(PotionEffectType.INVISIBILITY);
    }

    @Override
    public void registerEntities() {
        Map types = DataConverterRegistry.a().getSchema(DataFixUtils.makeKey((int)SharedConstants.getGameVersion().getWorldVersion())).findChoiceType(DataConverterTypes.ENTITY).types();
        types.put("minecraft:bwsilverfish", (Type)types.get("minecraft:silverfish"));
        EntityTypes.Builder.a(Silverfish::new, (EnumCreatureType)EnumCreatureType.MONSTER).a("bwsilverfish");
        types.put("minecraft:bwgolem", (Type)types.get("minecraft:iron_golem"));
        EntityTypes.Builder.a(IGolem::new, (EnumCreatureType)EnumCreatureType.AMBIENT).a("bwgolem");
    }

    @Override
    public void spawnShop(Location loc, String name1, List<Player> players, IArena arena) {
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
            String[] nume = Language.getMsg(p, name1).split(",");
            if (nume.length == 1) {
                a = v1_16_R3.createArmorStand(nume[0], l.clone().add(0.0, 1.85, 0.0));
                new ShopHolo(Language.getPlayerLanguage(p).getIso(), a, null, l, arena);
                continue;
            }
            a = v1_16_R3.createArmorStand(nume[0], l.clone().add(0.0, 2.1, 0.0));
            ArmorStand b2 = v1_16_R3.createArmorStand(nume[1], l.clone().add(0.0, 1.85, 0.0));
            new ShopHolo(Language.getPlayerLanguage(p).getIso(), a, b2, l, arena);
        }
        for (ShopHolo sh : ShopHolo.getShopHolo()) {
            if (sh.getA() != arena) continue;
            sh.update();
        }
    }

    @Override
    public double getDamage(ItemStack i) {
        net.minecraft.server.v1_16_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy((ItemStack)i);
        NBTTagCompound compound = nmsStack.hasTag() ? nmsStack.getTag() : new NBTTagCompound();
        return compound.getDouble("generic.attackDamage");
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
        ((CraftPlayer)p).getHandle().damageEntity(DamageSource.OUT_OF_WORLD, 1000.0f);
    }

    @Override
    public void hideArmor(Player victim, Player receiver) {
        ArrayList<Pair> items = new ArrayList<Pair>();
        items.add(new Pair((Object)EnumItemSlot.HEAD, (Object)new net.minecraft.server.v1_16_R3.ItemStack((IMaterial)Item.getById((int)0))));
        items.add(new Pair((Object)EnumItemSlot.CHEST, (Object)new net.minecraft.server.v1_16_R3.ItemStack((IMaterial)Item.getById((int)0))));
        items.add(new Pair((Object)EnumItemSlot.LEGS, (Object)new net.minecraft.server.v1_16_R3.ItemStack((IMaterial)Item.getById((int)0))));
        items.add(new Pair((Object)EnumItemSlot.FEET, (Object)new net.minecraft.server.v1_16_R3.ItemStack((IMaterial)Item.getById((int)0))));
        PacketPlayOutEntityEquipment packet1 = new PacketPlayOutEntityEquipment(victim.getEntityId(), items);
        EntityPlayer pc = ((CraftPlayer)receiver).getHandle();
        pc.playerConnection.sendPacket((Packet)packet1);
    }

    @Override
    public void showArmor(Player victim, Player receiver) {
        ArrayList<Pair> items = new ArrayList<Pair>();
        items.add(new Pair((Object)EnumItemSlot.HEAD, (Object)CraftItemStack.asNMSCopy((ItemStack)victim.getInventory().getHelmet())));
        items.add(new Pair((Object)EnumItemSlot.CHEST, (Object)CraftItemStack.asNMSCopy((ItemStack)victim.getInventory().getChestplate())));
        items.add(new Pair((Object)EnumItemSlot.LEGS, (Object)CraftItemStack.asNMSCopy((ItemStack)victim.getInventory().getLeggings())));
        items.add(new Pair((Object)EnumItemSlot.FEET, (Object)CraftItemStack.asNMSCopy((ItemStack)victim.getInventory().getBoots())));
        PacketPlayOutEntityEquipment packet1 = new PacketPlayOutEntityEquipment(victim.getEntityId(), items);
        EntityPlayer pc = ((CraftPlayer)receiver).getHandle();
        pc.playerConnection.sendPacket((Packet)packet1);
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
            Field field = BlockBase.class.getDeclaredField("durability");
            field.setAccessible(true);
            for (Block glass : new Block[]{Blocks.WHITE_STAINED_GLASS, Blocks.ORANGE_STAINED_GLASS, Blocks.MAGENTA_STAINED_GLASS, Blocks.LIGHT_BLUE_STAINED_GLASS, Blocks.YELLOW_STAINED_GLASS, Blocks.LIME_STAINED_GLASS, Blocks.PINK_STAINED_GLASS, Blocks.GRAY_STAINED_GLASS, Blocks.LIGHT_GRAY_STAINED_GLASS, Blocks.CYAN_STAINED_GLASS, Blocks.PURPLE_STAINED_GLASS, Blocks.BLUE_STAINED_GLASS, Blocks.BROWN_STAINED_GLASS, Blocks.GREEN_STAINED_GLASS, Blocks.RED_STAINED_GLASS, Blocks.BLACK_STAINED_GLASS, Blocks.GLASS}) {
                field.set(glass, Float.valueOf(glassBlast));
            }
            field.set(Blocks.END_STONE, Float.valueOf(endStoneBlast));
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setBlockTeamColor(org.bukkit.block.Block block, TeamColor teamColor) {
        if (block.getType().toString().contains("STAINED_GLASS") || block.getType().toString().equals("GLASS")) {
            block.setType(teamColor.glassMaterial());
        } else if (block.getType().toString().contains("_TERRACOTTA")) {
            block.setType(teamColor.glazedTerracottaMaterial());
        } else if (block.getType().toString().contains("_WOOL")) {
            block.setType(teamColor.woolMaterial());
        }
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
        net.minecraft.server.v1_16_R3.ItemStack itemStack = CraftItemStack.asNMSCopy((ItemStack)i);
        NBTTagCompound tag = itemStack.getTag();
        if (tag == null) {
            tag = new NBTTagCompound();
        }
        tag.setString("BedWars1058", data);
        itemStack.setTag(tag);
        return CraftItemStack.asBukkitCopy((net.minecraft.server.v1_16_R3.ItemStack)itemStack);
    }

    @Override
    public ItemStack setTag(ItemStack itemStack, String key, String value) {
        net.minecraft.server.v1_16_R3.ItemStack is = CraftItemStack.asNMSCopy((ItemStack)itemStack);
        NBTTagCompound tag = is.getTag();
        if (tag == null) {
            tag = new NBTTagCompound();
        }
        tag.setString(key, value);
        is.setTag(tag);
        return CraftItemStack.asBukkitCopy((net.minecraft.server.v1_16_R3.ItemStack)is);
    }

    @Override
    public boolean isCustomBedWarsItem(ItemStack i) {
        net.minecraft.server.v1_16_R3.ItemStack itemStack = CraftItemStack.asNMSCopy((ItemStack)i);
        NBTTagCompound tag = itemStack.getTag();
        if (tag == null) {
            return false;
        }
        return tag.hasKey("BedWars1058");
    }

    @Override
    public String getCustomData(ItemStack i) {
        net.minecraft.server.v1_16_R3.ItemStack itemStack = CraftItemStack.asNMSCopy((ItemStack)i);
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
        String type = itemStack.getType().toString();
        if (type.contains("_BED")) {
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
            this.getPlugin().getLogger().log(Level.WARNING, material + " is not a valid " + v1_16_R3.getName() + " material!");
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
        net.minecraft.server.v1_16_R3.ItemStack i = CraftItemStack.asNMSCopy((ItemStack)itemStack);
        NBTTagCompound tag = i.getTag();
        return tag == null ? "null" : (tag.hasKey("tierIdentifier") ? tag.getString("tierIdentifier") : "null");
    }

    @Override
    public ItemStack setShopUpgradeIdentifier(ItemStack itemStack, String identifier) {
        net.minecraft.server.v1_16_R3.ItemStack i = CraftItemStack.asNMSCopy((ItemStack)itemStack);
        NBTTagCompound tag = i.getTag();
        if (tag == null) {
            tag = new NBTTagCompound();
        }
        tag.setString(VersionSupport.PLUGIN_TAG_TIER_KEY, identifier);
        i.setTag(tag);
        return CraftItemStack.asBukkitCopy((net.minecraft.server.v1_16_R3.ItemStack)i);
    }

    @Override
    public ItemStack getPlayerHead(Player player, ItemStack copyTagFrom) {
        ItemStack head = new ItemStack(this.materialPlayerHead());
        if (copyTagFrom != null) {
            net.minecraft.server.v1_16_R3.ItemStack i = CraftItemStack.asNMSCopy((ItemStack)head);
            i.setTag(CraftItemStack.asNMSCopy((ItemStack)copyTagFrom).getTag());
            head = CraftItemStack.asBukkitCopy((net.minecraft.server.v1_16_R3.ItemStack)i);
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
        PacketPlayOutEntityVelocity playerVelocity = new PacketPlayOutEntityVelocity((Entity)entityPlayer);
        PacketPlayOutEntityHeadRotation head = new PacketPlayOutEntityHeadRotation((Entity)entityPlayer, this.getCompressedAngle(entityPlayer.yaw));
        ArrayList<Pair> list = new ArrayList<Pair>();
        list.add(new Pair((Object)EnumItemSlot.MAINHAND, (Object)entityPlayer.getItemInMainHand()));
        list.add(new Pair((Object)EnumItemSlot.OFFHAND, (Object)entityPlayer.getItemInOffHand()));
        list.add(new Pair((Object)EnumItemSlot.HEAD, (Object)((net.minecraft.server.v1_16_R3.ItemStack)entityPlayer.inventory.getArmorContents().get(3))));
        list.add(new Pair((Object)EnumItemSlot.CHEST, (Object)((net.minecraft.server.v1_16_R3.ItemStack)entityPlayer.inventory.getArmorContents().get(2))));
        list.add(new Pair((Object)EnumItemSlot.LEGS, (Object)((net.minecraft.server.v1_16_R3.ItemStack)entityPlayer.inventory.getArmorContents().get(1))));
        list.add(new Pair((Object)EnumItemSlot.FEET, (Object)((net.minecraft.server.v1_16_R3.ItemStack)entityPlayer.inventory.getArmorContents().get(0))));
        for (Player p : arena.getPlayers()) {
            if (p == null || p.equals((Object)respawned) || arena.getRespawnSessions().containsKey(p)) continue;
            boundTo = ((CraftPlayer)p).getHandle();
            if (!p.getWorld().equals((Object)respawned.getWorld()) || !(respawned.getLocation().distance(p.getLocation()) <= (double)arena.getRenderDistance())) continue;
            boundTo.playerConnection.sendPacket((Packet)show);
            boundTo.playerConnection.sendPacket((Packet)head);
            boundTo.playerConnection.sendPacket((Packet)playerVelocity);
            boundTo.playerConnection.sendPacket((Packet)new PacketPlayOutEntityEquipment(entityPlayer.getId(), list));
            if (p.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                this.hideArmor(p, respawned);
                continue;
            }
            PacketPlayOutNamedEntitySpawn show2 = new PacketPlayOutNamedEntitySpawn((EntityHuman)boundTo);
            PacketPlayOutEntityVelocity playerVelocity2 = new PacketPlayOutEntityVelocity((Entity)boundTo);
            PacketPlayOutEntityHeadRotation head2 = new PacketPlayOutEntityHeadRotation((Entity)boundTo, this.getCompressedAngle(boundTo.yaw));
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
            boundTo.playerConnection.sendPacket((Packet)new PacketPlayOutEntityEquipment(entityPlayer.getId(), list));
            boundTo.playerConnection.sendPacket((Packet)new PacketPlayOutEntityHeadRotation((Entity)entityPlayer, this.getCompressedAngle(entityPlayer.yaw)));
        }
    }

    @Override
    public String getInventoryName(InventoryEvent e) {
        return e.getView().getTitle();
    }

    @Override
    public void setUnbreakable(ItemMeta itemMeta) {
        itemMeta.setUnbreakable(true);
    }

    @Override
    public String getMainLevel() {
        return ((DedicatedServer)MinecraftServer.getServer()).getDedicatedServerProperties().levelName;
    }

    @Override
    public int getVersion() {
        return 8;
    }

    @Override
    public void setJoinSignBackground(BlockState b2, Material material) {
        if (b2.getBlockData() instanceof WallSign) {
            b2.getBlock().getRelative(((WallSign)b2.getBlockData()).getFacing().getOppositeFace()).setType(material);
        }
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
        PacketPlayOutWorldParticles particlePacket = new PacketPlayOutWorldParticles((ParticleParam)new ParticleParamRedstone((float)color.getRed(), (float)color.getBlue(), (float)color.getGreen(), 1.0f), true, (double)((float)player.getLocation().getX()), (double)((float)(player.getLocation().getY() + 2.6)), (double)((float)player.getLocation().getZ()), 0.0f, 0.0f, 0.0f, 0.0f, 0);
        for (Player inWorld : player.getWorld().getPlayers()) {
            if (inWorld.equals((Object)player)) continue;
            ((CraftPlayer)inWorld).getHandle().playerConnection.sendPacket((Packet)particlePacket);
        }
    }

    @Override
    public void clearArrowsFromPlayerBody(Player player) {
        ((CraftLivingEntity)player).getHandle().getDataWatcher().set(new DataWatcherObject(11, DataWatcherRegistry.b), (Object)-1);
    }

    @Override
    public void placeTowerBlocks(org.bukkit.block.Block b2, IArena a, TeamColor color, int x, int y, int z) {
        b2.getRelative(x, y, z).setType(color.woolMaterial());
        a.addPlacedBlock(b2.getRelative(x, y, z));
    }

    @Override
    public void placeLadder(org.bukkit.block.Block b2, int x, int y, int z, IArena a, int ladderdata) {
        org.bukkit.block.Block block = b2.getRelative(x, y, z);
        block.setType(Material.LADDER);
        Ladder ladder = (Ladder)block.getBlockData();
        a.addPlacedBlock(block);
        switch (ladderdata) {
            case 2: {
                ladder.setFacing(BlockFace.NORTH);
                block.setBlockData((BlockData)ladder);
                return;
            }
            case 3: {
                ladder.setFacing(BlockFace.SOUTH);
                block.setBlockData((BlockData)ladder);
                return;
            }
            case 4: {
                ladder.setFacing(BlockFace.WEST);
                block.setBlockData((BlockData)ladder);
                return;
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

