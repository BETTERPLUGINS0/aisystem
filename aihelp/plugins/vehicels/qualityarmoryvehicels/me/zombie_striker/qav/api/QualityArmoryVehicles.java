/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.block.BlockFace
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.entity.ArmorStand
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.util.Vector
 *  org.jetbrains.annotations.Nullable
 */
package me.zombie_striker.qav.api;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import me.zombie_striker.qav.ItemFact;
import me.zombie_striker.qav.Main;
import me.zombie_striker.qav.MessagesConfig;
import me.zombie_striker.qav.UnlockedVehicle;
import me.zombie_striker.qav.VehicleEntity;
import me.zombie_striker.qav.api.events.PlayerEnterQAVehicleEvent;
import me.zombie_striker.qav.api.events.VehicleSpawnEvent;
import me.zombie_striker.qav.attachments.Attachment;
import me.zombie_striker.qav.customitemmanager.MaterialStorage;
import me.zombie_striker.qav.hooks.ProtectionHandler;
import me.zombie_striker.qav.hooks.model.Animation;
import me.zombie_striker.qav.perms.PermissionHandler;
import me.zombie_striker.qav.vehicles.AbstractVehicle;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

public class QualityArmoryVehicles {
    private static Main main;

    public static void setPlugin(Main main) {
        QualityArmoryVehicles.main = main;
    }

    public static Main getPlugin() {
        return main;
    }

    public static VehicleEntity getVehicleEntityByEntity(Entity entity) {
        if (entity == null) {
            return null;
        }
        for (VehicleEntity vehicleEntity : Main.vehicles) {
            if (vehicleEntity == null || vehicleEntity.getDriverSeat() != entity && !vehicleEntity.getModelEntities().contains(entity) && !vehicleEntity.getPassagerSeats().contains(entity)) continue;
            return vehicleEntity;
        }
        return null;
    }

    public static Vector rotateRelToCar(Entity entity, Vector vector, boolean bl) {
        VehicleEntity vehicleEntity = QualityArmoryVehicles.getVehicleEntityByEntity(entity);
        Objects.requireNonNull(vehicleEntity);
        return QualityArmoryVehicles.rotateRelToCar(vehicleEntity, (Entity)vehicleEntity.getModelEntity(), vector, bl);
    }

    public static Vector rotateRelToCar(ArmorStand armorStand, Vector vector, boolean bl) {
        VehicleEntity vehicleEntity = QualityArmoryVehicles.getVehicleEntityByEntity((Entity)armorStand);
        return QualityArmoryVehicles.rotateRelToCar(vehicleEntity, (Entity)armorStand, vector, bl);
    }

    public static Vector rotateRelToCar(VehicleEntity vehicleEntity, Entity entity, Vector vector, boolean bl) {
        Vector vector2;
        if (entity == null) {
            return new Vector(0, 0, 1);
        }
        double d = vehicleEntity.getDriverSeat() instanceof ArmorStand ? ((ArmorStand)vehicleEntity.getDriverSeat()).getHeadPose().getX() : (double)entity.getLocation().getPitch();
        double d2 = Math.cos(vehicleEntity.getAngleRotation() - 4.71238898038469);
        double d3 = Math.sin(vehicleEntity.getAngleRotation() - 4.71238898038469);
        if (d == 0.0) {
            vector2 = new Vector(vector.getX() * d2 - vector.getZ() * d3, vector.getY(), vector.getZ() * d2 + vector.getX() * d3);
        } else {
            double d4 = Math.cos(d);
            double d5 = Math.sin(d);
            double d6 = Math.sqrt(vector.getX() * vector.getX() + vector.getZ() * vector.getZ());
            double d7 = d6 * d5;
            vector2 = new Vector((vector.getX() * d2 - vector.getZ() * d3) * d4, vector.getY() + d7, (vector.getZ() * d2 + vector.getX() * d3) * d4);
        }
        if (bl) {
            vector2.multiply(-1);
        }
        return vector2;
    }

    public static boolean isVehicleByItem(ItemStack itemStack) {
        return QualityArmoryVehicles.getVehicleByItem(itemStack) != null;
    }

    public static AbstractVehicle getVehicleByItem(MaterialStorage materialStorage) {
        for (AbstractVehicle abstractVehicle : Main.vehicleTypes) {
            if (abstractVehicle.getMaterial() != materialStorage.getMat() || abstractVehicle.getItemData() != materialStorage.getData()) continue;
            return abstractVehicle;
        }
        return null;
    }

    @Nullable
    public static AbstractVehicle getVehicleByItem(ItemStack itemStack) {
        int n;
        if (itemStack == null) {
            return null;
        }
        try {
            n = itemStack.getItemMeta().getCustomModelData();
        } catch (Error | Exception throwable) {
            n = itemStack.getDurability();
        }
        for (AbstractVehicle abstractVehicle : Main.vehicleTypes) {
            if (abstractVehicle.getMaterial() != itemStack.getType() || abstractVehicle.getItemData() != n) continue;
            return abstractVehicle;
        }
        return null;
    }

    public static ItemStack getVehicleItemStack(AbstractVehicle abstractVehicle) {
        return ItemFact.getItem(abstractVehicle);
    }

    public static ItemStack getAttachmentItemStack(Attachment attachment) {
        return ItemFact.getItem(attachment);
    }

    public static VehicleEntity getVehiclePlayerLookingAt(Player player) {
        for (VehicleEntity vehicleEntity : Main.vehicles) {
            if (!vehicleEntity.getBoundingBox().intersects(player.getEyeLocation(), player.getLocation().getDirection(), 6)) continue;
            return vehicleEntity;
        }
        return null;
    }

    public static AbstractVehicle getVehicle(String string) {
        for (AbstractVehicle abstractVehicle : Main.vehicleTypes) {
            if (!abstractVehicle.getName().equalsIgnoreCase(string)) continue;
            return abstractVehicle;
        }
        return null;
    }

    public static boolean isQAVEntity(Entity entity) {
        return QualityArmoryVehicles.isVehicle(entity) || QualityArmoryVehicles.isPassager(entity);
    }

    public static boolean isVehicle(Entity entity) {
        if (entity == null || entity.getCustomName() == null) {
            return false;
        }
        return entity.getCustomName().startsWith(Main.VEHICLEPREFIX) || entity.getCustomName().startsWith(Main.MODEL_PREFIX) || entity.getCustomName().startsWith(Main.PASSAGER_PREFIX);
    }

    public static boolean isPassager(Entity entity) {
        if (entity == null) {
            return false;
        }
        if (entity.getCustomName() == null) {
            return false;
        }
        return entity.getCustomName().startsWith(Main.PASSAGER_PREFIX);
    }

    public static boolean isWithinVehicle(Location location, VehicleEntity vehicleEntity) {
        return vehicleEntity.getBoundingBox().intersects(location);
    }

    public static VehicleEntity spawnVehicle(UnlockedVehicle unlockedVehicle, @Nullable Player player) {
        return QualityArmoryVehicles.spawnVehicle(unlockedVehicle.getVehicleType(), player);
    }

    public static VehicleEntity spawnVehicle(AbstractVehicle abstractVehicle, @Nullable Player player) {
        return QualityArmoryVehicles.spawnVehicle(abstractVehicle, player.getLocation(), player);
    }

    public static VehicleEntity spawnVehicle(AbstractVehicle abstractVehicle, Location location, @Nullable Player player) {
        if (location.getWorld() != null && Main.blacklistedWorlds.contains(location.getWorld().getName())) {
            if (player != null) {
                player.sendMessage(Main.prefix + MessagesConfig.MESSAGE_BLACKLIST_WORLD);
            }
            return null;
        }
        if (!ProtectionHandler.canPlace(player, location)) {
            if (player != null) {
                player.sendMessage(Main.prefix + MessagesConfig.MESSAGE_BLACKLIST_PLACE);
            }
            return null;
        }
        VehicleEntity vehicleEntity = new VehicleEntity(abstractVehicle, location.getBlock().getRelative(BlockFace.UP).getLocation(), player != null ? player.getUniqueId() : null);
        VehicleSpawnEvent vehicleSpawnEvent = new VehicleSpawnEvent(player, vehicleEntity);
        Bukkit.getPluginManager().callEvent((Event)vehicleSpawnEvent);
        if (vehicleSpawnEvent.isCanceled()) {
            return null;
        }
        vehicleEntity.spawn();
        return vehicleEntity;
    }

    public static void setAddPassager(VehicleEntity vehicleEntity, Player player, int n) {
        QualityArmoryVehicles.spawnPassager(vehicleEntity, n).setPassenger((Entity)player);
    }

    public static void addPlayerToCar(VehicleEntity vehicleEntity, Player player, boolean bl) {
        if (vehicleEntity == null) {
            return;
        }
        if (vehicleEntity.getDriverSeat() == null) {
            return;
        }
        if (vehicleEntity.getDriverSeat().getPassenger() == null && bl && (!Main.requirePermissionToDrive || PermissionHandler.canDrive(player, vehicleEntity.getType()))) {
            PlayerEnterQAVehicleEvent playerEnterQAVehicleEvent = new PlayerEnterQAVehicleEvent(vehicleEntity, player);
            Bukkit.getPluginManager().callEvent((Event)playerEnterQAVehicleEvent);
            if (playerEnterQAVehicleEvent.isCanceled()) {
                return;
            }
            if (vehicleEntity.getOwner() == null && Main.setOwnerIfNoneExist) {
                vehicleEntity.setOwner(player.getUniqueId());
                if (MessagesConfig.MESSAGE_NOW_OWN_CAR.length() > 1) {
                    player.sendMessage(Main.prefix + MessagesConfig.MESSAGE_NOW_OWN_CAR.replace("%car%", ChatColor.stripColor((String)vehicleEntity.getType().getDisplayname())));
                }
            }
            vehicleEntity.getType().playAnimation(vehicleEntity, Animation.AnimationType.ENTER, "driver");
            vehicleEntity.getDriverSeat().setPassenger((Entity)player);
        } else if (vehicleEntity.getPassagers().size() < vehicleEntity.getType().getPassagerSpots().size()) {
            int n = vehicleEntity.getFirstSeat();
            if (n < 0) {
                return;
            }
            PlayerEnterQAVehicleEvent playerEnterQAVehicleEvent = new PlayerEnterQAVehicleEvent(vehicleEntity, player);
            Bukkit.getPluginManager().callEvent((Event)playerEnterQAVehicleEvent);
            if (playerEnterQAVehicleEvent.isCanceled()) {
                return;
            }
            vehicleEntity.getType().playAnimation(vehicleEntity, Animation.AnimationType.ENTER, n + "");
            QualityArmoryVehicles.setAddPassager(vehicleEntity, player, n);
        }
    }

    public static Location getPassagerOffsetLocation(ArmorStand armorStand, AbstractVehicle abstractVehicle, int n) {
        return QualityArmoryVehicles.getPassagerOffsetLocation((Entity)armorStand, abstractVehicle, n);
    }

    public static Location getPassagerOffsetLocation(Entity entity, AbstractVehicle abstractVehicle, int n) {
        Vector vector = abstractVehicle.getPassagerSpots().get(n);
        return entity.getLocation().clone().add(QualityArmoryVehicles.rotateRelToCar(entity, vector, false));
    }

    public static Entity spawnPassager(VehicleEntity vehicleEntity, int n) {
        Location location = QualityArmoryVehicles.getPassagerOffsetLocation(vehicleEntity.getModelEntity(), vehicleEntity.getType(), n);
        Entity entity = vehicleEntity.spawnSeat(location.clone().subtract(0.0, 1.0, 0.0), n);
        entity.setCustomName(Main.PASSAGER_PREFIX + n);
        vehicleEntity.addPassager(n, entity);
        return entity;
    }

    public static List<UnlockedVehicle> unlockedVehicles(OfflinePlayer offlinePlayer) {
        File file = new File(Main.playerUnlock, offlinePlayer.getUniqueId() + ".yml");
        return QualityArmoryVehicles.parseUnlockedVehicles(file);
    }

    public static List<UnlockedVehicle> parseUnlockedVehicles(File file) {
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration((File)file);
        return yamlConfiguration.getList("unlockedVehicles", new ArrayList());
    }

    public static void addUnlockedVehicle(OfflinePlayer offlinePlayer, UnlockedVehicle unlockedVehicle) {
        File file = new File(Main.playerUnlock, offlinePlayer.getUniqueId() + ".yml");
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration((File)file);
        List list = yamlConfiguration.getList("unlockedVehicles", new ArrayList());
        list.add(unlockedVehicle);
        yamlConfiguration.set("unlockedVehicles", (Object)list);
        try {
            yamlConfiguration.save(file);
        } catch (IOException iOException) {
            iOException.printStackTrace();
        }
    }

    public static File getUnlockedVehiclesFile(OfflinePlayer offlinePlayer) {
        return new File(Main.playerUnlock, offlinePlayer.getUniqueId() + ".yml");
    }

    public static List<File> getUnlockedVehiclesFiles() {
        ArrayList<File> arrayList = new ArrayList<File>();
        for (File file : Objects.requireNonNull(Main.playerUnlock.listFiles())) {
            if (!file.getName().endsWith(".yml")) continue;
            arrayList.add(file);
        }
        return arrayList;
    }

    public static UnlockedVehicle findUnlockedVehicle(OfflinePlayer offlinePlayer, AbstractVehicle abstractVehicle) {
        File file = QualityArmoryVehicles.getUnlockedVehiclesFile(offlinePlayer);
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration((File)file);
        List list = yamlConfiguration.getList("unlockedVehicles", new ArrayList());
        Optional<UnlockedVehicle> optional = list.stream().filter(unlockedVehicle -> unlockedVehicle.getVehicleType().getName().equals(abstractVehicle.getName())).findFirst();
        return optional.orElse(null);
    }

    public static void setUnlockedVehicles(File file, List<UnlockedVehicle> list) {
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration((File)file);
        yamlConfiguration.set("unlockedVehicles", list);
        try {
            yamlConfiguration.save(file);
        } catch (IOException iOException) {
            iOException.printStackTrace();
        }
    }

    public static void setUnlockedVehicles(OfflinePlayer offlinePlayer, List<UnlockedVehicle> list) {
        File file = QualityArmoryVehicles.getUnlockedVehiclesFile(offlinePlayer);
        QualityArmoryVehicles.setUnlockedVehicles(file, list);
    }

    public static void removeUnlockedVehicle(OfflinePlayer offlinePlayer, AbstractVehicle abstractVehicle) {
        File file = QualityArmoryVehicles.getUnlockedVehiclesFile(offlinePlayer);
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration((File)file);
        ArrayList arrayList = new ArrayList(yamlConfiguration.getList("unlockedVehicles", new ArrayList()));
        UnlockedVehicle unlockedVehicle = QualityArmoryVehicles.findUnlockedVehicle(offlinePlayer, abstractVehicle);
        arrayList.remove(unlockedVehicle);
        yamlConfiguration.set("unlockedVehicles", arrayList);
        try {
            yamlConfiguration.save(file);
        } catch (IOException iOException) {
            iOException.printStackTrace();
        }
    }

    public static void removeUnlockedVehicle(OfflinePlayer offlinePlayer, UnlockedVehicle unlockedVehicle) {
        File file = QualityArmoryVehicles.getUnlockedVehiclesFile(offlinePlayer);
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration((File)file);
        List list = yamlConfiguration.getList("unlockedVehicles", new ArrayList());
        list.remove(unlockedVehicle);
        yamlConfiguration.set("unlockedVehicles", (Object)list);
        try {
            yamlConfiguration.save(file);
        } catch (IOException iOException) {
            iOException.printStackTrace();
        }
    }

    public static Object getPlayerData(OfflinePlayer offlinePlayer, String string) {
        File file = new File(Main.playerUnlock, offlinePlayer.getUniqueId() + ".yml");
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration((File)file);
        return yamlConfiguration.get(string);
    }

    public static void setPlayerData(OfflinePlayer offlinePlayer, String string, Object object) {
        File file = new File(Main.playerUnlock, offlinePlayer.getUniqueId() + ".yml");
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration((File)file);
        yamlConfiguration.set(string, object);
        try {
            yamlConfiguration.save(file);
        } catch (IOException iOException) {
            iOException.printStackTrace();
        }
    }

    public static List<VehicleEntity> getOwnedVehicles(UUID uUID) {
        ArrayList<VehicleEntity> arrayList = new ArrayList<VehicleEntity>();
        for (VehicleEntity vehicleEntity : Main.vehicles) {
            if (vehicleEntity.getOwner() == null || !vehicleEntity.getOwner().equals(uUID)) continue;
            arrayList.add(vehicleEntity);
        }
        return arrayList;
    }

    public static void giveOrDrop(Inventory inventory, Location location, ItemStack itemStack) {
        if (inventory.firstEmpty() != -1) {
            inventory.addItem(new ItemStack[]{itemStack});
        } else {
            location.getWorld().dropItem(location, itemStack);
        }
    }
}

