/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.DataFixUtils
 *  com.mojang.datafixers.types.Type
 *  com.mojang.datafixers.util.Pair
 *  com.mojang.math.Vector3fa
 *  net.minecraft.SharedConstants
 *  net.minecraft.core.particles.ParticleParam
 *  net.minecraft.core.particles.ParticleParamRedstone
 *  net.minecraft.nbt.NBTTagCompound
 *  net.minecraft.network.chat.ChatMessageType
 *  net.minecraft.network.chat.IChatBaseComponent
 *  net.minecraft.network.chat.IChatBaseComponent$ChatSerializer
 *  net.minecraft.network.chat.IChatMutableComponent
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.game.PacketPlayOutChat
 *  net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy
 *  net.minecraft.network.protocol.game.PacketPlayOutEntityEquipment
 *  net.minecraft.network.protocol.game.PacketPlayOutEntityHeadRotation
 *  net.minecraft.network.protocol.game.PacketPlayOutEntityVelocity
 *  net.minecraft.network.protocol.game.PacketPlayOutNamedEntitySpawn
 *  net.minecraft.network.protocol.game.PacketPlayOutWorldParticles
 *  net.minecraft.network.syncher.DataWatcherObject
 *  net.minecraft.network.syncher.DataWatcherRegistry
 *  net.minecraft.server.MinecraftServer
 *  net.minecraft.server.dedicated.DedicatedServer
 *  net.minecraft.server.level.EntityPlayer
 *  net.minecraft.util.datafix.DataConverterRegistry
 *  net.minecraft.util.datafix.fixes.DataConverterTypes
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityLiving
 *  net.minecraft.world.entity.EntityTypes$Builder
 *  net.minecraft.world.entity.EnumCreatureType
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
 *  org.bukkit.Color
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.Particle
 *  org.bukkit.block.Block
 *  org.bukkit.block.BlockFace
 *  org.bukkit.block.BlockState
 *  org.bukkit.block.data.BlockData
 *  org.bukkit.block.data.type.Bed
 *  org.bukkit.block.data.type.Ladder
 *  org.bukkit.block.data.type.WallSign
 *  org.bukkit.command.Command
 *  org.bukkit.craftbukkit.v1_17_R1.CraftServer
 *  org.bukkit.craftbukkit.v1_17_R1.entity.CraftFireball
 *  org.bukkit.craftbukkit.v1_17_R1.entity.CraftLivingEntity
 *  org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer
 *  org.bukkit.craftbukkit.v1_17_R1.entity.CraftTNTPrimed
 *  org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack
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
 */
package com.andrei1058.bedwars.support.version.v1_17_R1;

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
import com.andrei1058.bedwars.support.version.v1_17_R1.IGolem;
import com.andrei1058.bedwars.support.version.v1_17_R1.Silverfish;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Vector3fa;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import net.minecraft.SharedConstants;
import net.minecraft.core.particles.ParticleParam;
import net.minecraft.core.particles.ParticleParamRedstone;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.chat.ChatMessageType;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.chat.IChatMutableComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayOutChat;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityEquipment;
import net.minecraft.network.protocol.game.PacketPlayOutEntityHeadRotation;
import net.minecraft.network.protocol.game.PacketPlayOutEntityVelocity;
import net.minecraft.network.protocol.game.PacketPlayOutNamedEntitySpawn;
import net.minecraft.network.protocol.game.PacketPlayOutWorldParticles;
import net.minecraft.network.syncher.DataWatcherObject;
import net.minecraft.network.syncher.DataWatcherRegistry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.util.datafix.DataConverterRegistry;
import net.minecraft.util.datafix.fixes.DataConverterTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EnumCreatureType;
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
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Particle;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Bed;
import org.bukkit.block.data.type.Ladder;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.command.Command;
import org.bukkit.craftbukkit.v1_17_R1.CraftServer;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftFireball;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftTNTPrimed;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
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
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

@Deprecated
public class v1_17_R1
extends VersionSupport {
    private static final UUID chatUUID = new UUID(0L, 0L);

    public v1_17_R1(Plugin plugin, String name) {
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
        net.minecraft.world.item.ItemStack i = CraftItemStack.asNMSCopy((ItemStack)itemStack);
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
        PacketPlayOutChat ppoc = new PacketPlayOutChat((IChatBaseComponent)cbc, ChatMessageType.c, chatUUID);
        cPlayer.getHandle().b.sendPacket((Packet)ppoc);
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
        ((CraftPlayer)p).getHandle().b.sendPacket((Packet)packet);
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
            Field sourceField = EntityTNTPrimed.class.getDeclaredField("d");
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
        if (CraftItemStack.asNMSCopy((ItemStack)itemStack).E() == null) {
            return false;
        }
        return CraftItemStack.asNMSCopy((ItemStack)itemStack).E() instanceof IProjectile;
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
        Map types = DataConverterRegistry.a().getSchema(DataFixUtils.makeKey((int)SharedConstants.getGameVersion().getWorldVersion())).findChoiceType(DataConverterTypes.q).types();
        types.put("minecraft:bwsilverfish", (Type)types.get("minecraft:silverfish"));
        EntityTypes.Builder.a(Silverfish::new, (EnumCreatureType)EnumCreatureType.a).a("bwsilverfish");
        types.put("minecraft:bwgolem", (Type)types.get("minecraft:iron_golem"));
        EntityTypes.Builder.a(IGolem::new, (EnumCreatureType)EnumCreatureType.a).a("bwgolem");
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
                a = v1_17_R1.createArmorStand(nume[0], l.clone().add(0.0, 1.85, 0.0));
                new ShopHolo(Language.getPlayerLanguage(p).getIso(), a, null, l, arena);
                continue;
            }
            a = v1_17_R1.createArmorStand(nume[0], l.clone().add(0.0, 2.1, 0.0));
            ArmorStand b2 = v1_17_R1.createArmorStand(nume[1], l.clone().add(0.0, 1.85, 0.0));
            new ShopHolo(Language.getPlayerLanguage(p).getIso(), a, b2, l, arena);
        }
        for (ShopHolo sh : ShopHolo.getShopHolo()) {
            if (sh.getA() != arena) continue;
            sh.update();
        }
    }

    @Override
    public double getDamage(ItemStack i) {
        net.minecraft.world.item.ItemStack nmsStack = CraftItemStack.asNMSCopy((ItemStack)i);
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
        ((CraftPlayer)p).getHandle().damageEntity(DamageSource.m, 1000.0f);
    }

    @Override
    public void hideArmor(Player victim, Player receiver) {
        ArrayList<Pair> items = new ArrayList<Pair>();
        items.add(new Pair((Object)EnumItemSlot.f, (Object)new net.minecraft.world.item.ItemStack((IMaterial)Item.getById((int)0))));
        items.add(new Pair((Object)EnumItemSlot.e, (Object)new net.minecraft.world.item.ItemStack((IMaterial)Item.getById((int)0))));
        items.add(new Pair((Object)EnumItemSlot.d, (Object)new net.minecraft.world.item.ItemStack((IMaterial)Item.getById((int)0))));
        items.add(new Pair((Object)EnumItemSlot.c, (Object)new net.minecraft.world.item.ItemStack((IMaterial)Item.getById((int)0))));
        PacketPlayOutEntityEquipment packet1 = new PacketPlayOutEntityEquipment(victim.getEntityId(), items);
        EntityPlayer pc = ((CraftPlayer)receiver).getHandle();
        pc.b.sendPacket((Packet)packet1);
    }

    @Override
    public void showArmor(Player victim, Player receiver) {
        ArrayList<Pair> items = new ArrayList<Pair>();
        items.add(new Pair((Object)EnumItemSlot.f, (Object)CraftItemStack.asNMSCopy((ItemStack)victim.getInventory().getHelmet())));
        items.add(new Pair((Object)EnumItemSlot.e, (Object)CraftItemStack.asNMSCopy((ItemStack)victim.getInventory().getChestplate())));
        items.add(new Pair((Object)EnumItemSlot.d, (Object)CraftItemStack.asNMSCopy((ItemStack)victim.getInventory().getLeggings())));
        items.add(new Pair((Object)EnumItemSlot.c, (Object)CraftItemStack.asNMSCopy((ItemStack)victim.getInventory().getBoots())));
        PacketPlayOutEntityEquipment packet1 = new PacketPlayOutEntityEquipment(victim.getEntityId(), items);
        EntityPlayer pc = ((CraftPlayer)receiver).getHandle();
        pc.b.sendPacket((Packet)packet1);
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
            Field field = BlockBase.class.getDeclaredField("aI");
            field.setAccessible(true);
            for (Block glass : new Block[]{Blocks.au, Blocks.dg, Blocks.dh, Blocks.di, Blocks.dj, Blocks.dk, Blocks.dl, Blocks.dm, Blocks.dn, Blocks.do_, Blocks.dp, Blocks.dq, Blocks.dr, Blocks.ds, Blocks.dt, Blocks.du, Blocks.dv}) {
                field.set(glass, Float.valueOf(glassBlast));
            }
            field.set(Blocks.eq, Float.valueOf(endStoneBlast));
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
        net.minecraft.world.item.ItemStack itemStack = CraftItemStack.asNMSCopy((ItemStack)i);
        NBTTagCompound tag = itemStack.getTag();
        if (tag == null) {
            tag = new NBTTagCompound();
        }
        tag.setString("BedWars1058", data);
        itemStack.setTag(tag);
        return CraftItemStack.asBukkitCopy((net.minecraft.world.item.ItemStack)itemStack);
    }

    @Override
    public ItemStack setTag(ItemStack itemStack, String key, String value) {
        net.minecraft.world.item.ItemStack is = CraftItemStack.asNMSCopy((ItemStack)itemStack);
        NBTTagCompound tag = is.getTag();
        if (tag == null) {
            tag = new NBTTagCompound();
        }
        tag.setString(key, value);
        is.setTag(tag);
        return CraftItemStack.asBukkitCopy((net.minecraft.world.item.ItemStack)is);
    }

    @Override
    public boolean isCustomBedWarsItem(ItemStack i) {
        net.minecraft.world.item.ItemStack itemStack = CraftItemStack.asNMSCopy((ItemStack)i);
        NBTTagCompound tag = itemStack.getTag();
        if (tag == null) {
            return false;
        }
        return tag.hasKey("BedWars1058");
    }

    @Override
    public String getCustomData(ItemStack i) {
        net.minecraft.world.item.ItemStack itemStack = CraftItemStack.asNMSCopy((ItemStack)i);
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
            this.getPlugin().getLogger().log(Level.WARNING, material + " is not a valid " + v1_17_R1.getName() + " material!");
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
        net.minecraft.world.item.ItemStack i = CraftItemStack.asNMSCopy((ItemStack)itemStack);
        NBTTagCompound tag = i.getTag();
        return tag == null ? "null" : (tag.hasKey("tierIdentifier") ? tag.getString("tierIdentifier") : "null");
    }

    @Override
    public ItemStack setShopUpgradeIdentifier(ItemStack itemStack, String identifier) {
        net.minecraft.world.item.ItemStack i = CraftItemStack.asNMSCopy((ItemStack)itemStack);
        NBTTagCompound tag = i.getTag();
        if (tag == null) {
            tag = new NBTTagCompound();
        }
        tag.setString(VersionSupport.PLUGIN_TAG_TIER_KEY, identifier);
        i.setTag(tag);
        return CraftItemStack.asBukkitCopy((net.minecraft.world.item.ItemStack)i);
    }

    @Override
    public ItemStack getPlayerHead(Player player, ItemStack copyTagFrom) {
        ItemStack head = new ItemStack(this.materialPlayerHead());
        if (copyTagFrom != null) {
            net.minecraft.world.item.ItemStack i = CraftItemStack.asNMSCopy((ItemStack)head);
            i.setTag(CraftItemStack.asNMSCopy((ItemStack)copyTagFrom).getTag());
            head = CraftItemStack.asBukkitCopy((net.minecraft.world.item.ItemStack)i);
        }
        SkullMeta headMeta = (SkullMeta)head.getItemMeta();
        assert (headMeta != null);
        headMeta.setOwningPlayer((OfflinePlayer)player);
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
        PacketPlayOutEntityVelocity playerVelocity = new PacketPlayOutEntityVelocity((net.minecraft.world.entity.Entity)entityPlayer);
        PacketPlayOutEntityHeadRotation head = new PacketPlayOutEntityHeadRotation((net.minecraft.world.entity.Entity)entityPlayer, this.getCompressedAngle(entityPlayer.getBukkitYaw()));
        ArrayList<Pair> list = new ArrayList<Pair>();
        list.add(new Pair((Object)EnumItemSlot.a, (Object)entityPlayer.getItemInMainHand()));
        list.add(new Pair((Object)EnumItemSlot.b, (Object)entityPlayer.getItemInOffHand()));
        list.add(new Pair((Object)EnumItemSlot.f, (Object)((net.minecraft.world.item.ItemStack)entityPlayer.getInventory().getArmorContents().get(3))));
        list.add(new Pair((Object)EnumItemSlot.e, (Object)((net.minecraft.world.item.ItemStack)entityPlayer.getInventory().getArmorContents().get(2))));
        list.add(new Pair((Object)EnumItemSlot.d, (Object)((net.minecraft.world.item.ItemStack)entityPlayer.getInventory().getArmorContents().get(1))));
        list.add(new Pair((Object)EnumItemSlot.c, (Object)((net.minecraft.world.item.ItemStack)entityPlayer.getInventory().getArmorContents().get(0))));
        for (Player p : arena.getPlayers()) {
            if (p == null || p.equals((Object)respawned) || arena.getRespawnSessions().containsKey(p)) continue;
            boundTo = ((CraftPlayer)p).getHandle();
            if (!p.getWorld().equals((Object)respawned.getWorld()) || !(respawned.getLocation().distance(p.getLocation()) <= (double)arena.getRenderDistance())) continue;
            boundTo.b.sendPacket((Packet)show);
            boundTo.b.sendPacket((Packet)head);
            boundTo.b.sendPacket((Packet)playerVelocity);
            boundTo.b.sendPacket((Packet)new PacketPlayOutEntityEquipment(entityPlayer.getId(), list));
            if (p.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                this.hideArmor(p, respawned);
                continue;
            }
            PacketPlayOutNamedEntitySpawn show2 = new PacketPlayOutNamedEntitySpawn((EntityHuman)boundTo);
            PacketPlayOutEntityVelocity playerVelocity2 = new PacketPlayOutEntityVelocity((net.minecraft.world.entity.Entity)boundTo);
            PacketPlayOutEntityHeadRotation head2 = new PacketPlayOutEntityHeadRotation((net.minecraft.world.entity.Entity)boundTo, this.getCompressedAngle(boundTo.getBukkitYaw()));
            entityPlayer.b.sendPacket((Packet)show2);
            entityPlayer.b.sendPacket((Packet)playerVelocity2);
            entityPlayer.b.sendPacket((Packet)head2);
            this.showArmor(p, respawned);
        }
        for (Player spectator : arena.getSpectators()) {
            if (spectator == null || spectator.equals((Object)respawned)) continue;
            boundTo = ((CraftPlayer)spectator).getHandle();
            respawned.hidePlayer(this.getPlugin(), spectator);
            if (!spectator.getWorld().equals((Object)respawned.getWorld()) || !(respawned.getLocation().distance(spectator.getLocation()) <= (double)arena.getRenderDistance())) continue;
            boundTo.b.sendPacket((Packet)show);
            boundTo.b.sendPacket((Packet)playerVelocity);
            boundTo.b.sendPacket((Packet)new PacketPlayOutEntityEquipment(entityPlayer.getId(), list));
            boundTo.b.sendPacket((Packet)new PacketPlayOutEntityHeadRotation((net.minecraft.world.entity.Entity)entityPlayer, this.getCompressedAngle(entityPlayer.getBukkitYaw())));
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
        return ((DedicatedServer)MinecraftServer.getServer()).getWorld();
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
        fb.b = vector.getX() * 0.1;
        fb.c = vector.getY() * 0.1;
        fb.d = vector.getZ() * 0.1;
        return (Fireball)fb.getBukkitEntity();
    }

    @Override
    public void playRedStoneDot(Player player) {
        Color color = Color.RED;
        PacketPlayOutWorldParticles particlePacket = new PacketPlayOutWorldParticles((ParticleParam)new ParticleParamRedstone(new Vector3fa((float)color.getRed(), (float)color.getGreen(), (float)color.getBlue()), 1.0f), true, player.getLocation().getX(), player.getLocation().getY() + 2.6, player.getLocation().getZ(), 0.0f, 0.0f, 0.0f, 0.0f, 0);
        for (Player inWorld : player.getWorld().getPlayers()) {
            if (inWorld.equals((Object)player)) continue;
            ((CraftPlayer)inWorld).getHandle().b.sendPacket((Packet)particlePacket);
        }
    }

    @Override
    public void clearArrowsFromPlayerBody(Player player) {
        ((CraftLivingEntity)player).getHandle().getDataWatcher().set(new DataWatcherObject(12, DataWatcherRegistry.b), (Object)-1);
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
    public void playVillagerEffect(Player player, Location location) {
        player.spawnParticle(Particle.VILLAGER_HAPPY, location, 1);
    }
}

