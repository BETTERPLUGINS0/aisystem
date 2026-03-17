/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.World
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.ArmorStand
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 */
package nl.mtvehicles.core.infrastructure.vehicle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.annotations.ToDo;
import nl.mtvehicles.core.infrastructure.dataconfig.DefaultConfig;
import nl.mtvehicles.core.infrastructure.dataconfig.VehicleDataConfig;
import nl.mtvehicles.core.infrastructure.enums.InventoryTitle;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.enums.RegionAction;
import nl.mtvehicles.core.infrastructure.enums.VehicleType;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.NBTItem;
import nl.mtvehicles.core.infrastructure.models.MTVConfig;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.infrastructure.utils.BossBarUtils;
import nl.mtvehicles.core.infrastructure.utils.ItemFactory;
import nl.mtvehicles.core.infrastructure.utils.ItemUtils;
import nl.mtvehicles.core.infrastructure.utils.PaperUtils;
import nl.mtvehicles.core.infrastructure.utils.TextUtils;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class VehicleUtils {
    public static HashMap<Player, String> openedTrunk = new HashMap();

    private VehicleUtils() {
    }

    public static void spawnVehicle(String licensePlate, Location location) throws IllegalArgumentException {
        if (!VehicleUtils.existsByLicensePlate(licensePlate)) {
            throw new IllegalArgumentException("Vehicle does not exists.");
        }
        ArmorStand standSkin = (ArmorStand)location.getWorld().spawn(location, ArmorStand.class);
        VehicleUtils.allowTicking(standSkin);
        standSkin.setVisible(false);
        standSkin.setCustomName("MTVEHICLES_SKIN_" + licensePlate);
        standSkin.getEquipment().setHelmet(ItemUtils.getVehicleItem(ItemUtils.getMaterial(ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.SKIN_ITEM).toString()), (int)((Integer)ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.SKIN_DAMAGE)), false, ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.NAME).toString(), licensePlate));
        ArmorStand standMain = (ArmorStand)location.getWorld().spawn(location, ArmorStand.class);
        standMain.setVisible(false);
        standMain.setCustomName("MTVEHICLES_MAIN_" + licensePlate);
        Vehicle vehicle = VehicleUtils.getVehicle(licensePlate);
        List seats = (List)vehicle.getVehicleData().get("seats");
        Map mainSeat = (Map)seats.get(0);
        Location locationMainSeat = new Location(location.getWorld(), location.getX() + (Double)mainSeat.get("x"), location.getY() + (Double)mainSeat.get("y"), location.getZ() + (Double)mainSeat.get("z"));
        ArmorStand standMainSeat = (ArmorStand)locationMainSeat.getWorld().spawn(locationMainSeat, ArmorStand.class);
        standMainSeat.setCustomName("MTVEHICLES_MAINSEAT_" + licensePlate);
        standMainSeat.setGravity(false);
        standMainSeat.setVisible(false);
        if (ConfigModule.vehicleDataConfig.getType(licensePlate).isBoat()) {
            standMain.setGravity(false);
            standSkin.setGravity(false);
        }
        if (ConfigModule.vehicleDataConfig.getType(licensePlate).isHelicopter()) {
            List helicopterBlades = (List)vehicle.getVehicleData().get("wiekens");
            Map blade = (Map)helicopterBlades.get(0);
            Location locationBlade = new Location(location.getWorld(), location.getX() + (Double)blade.get("z"), location.getY() + (Double)blade.get("y"), location.getZ() + (Double)blade.get("x"));
            ArmorStand standRotors = (ArmorStand)locationBlade.getWorld().spawn(locationBlade, ArmorStand.class);
            standRotors.setCustomName("MTVEHICLES_WIEKENS_" + licensePlate);
            standRotors.setGravity(false);
            standRotors.setVisible(false);
            if (((Boolean)ConfigModule.defaultConfig.get(DefaultConfig.Option.HELICOPTER_BLADES_ALWAYS_ON)).booleanValue()) {
                ItemStack rotor = new ItemFactory(Material.getMaterial((String)"DIAMOND_HOE")).setDurability(1058).setName(TextUtils.colorize("&6Wieken")).setNBT("mtvehicles.kenteken", licensePlate).toItemStack();
                ItemMeta itemMeta = rotor.getItemMeta();
                ArrayList<String> lore = new ArrayList<String>();
                lore.add(TextUtils.colorize("&a"));
                lore.add(TextUtils.colorize("&a" + licensePlate));
                lore.add(TextUtils.colorize("&a"));
                itemMeta.setLore(lore);
                itemMeta.setUnbreakable(true);
                rotor.setItemMeta(itemMeta);
                VehicleUtils.allowTicking(standRotors);
                standRotors.setHelmet((ItemStack)blade.get("item"));
            }
        }
    }

    public static String getLicensePlate(ItemStack item) {
        NBTItem nbt = new NBTItem(item);
        return nbt.getString("mtvehicles.kenteken");
    }

    public static Vehicle getVehicle(ItemStack item) {
        return VehicleUtils.getVehicle(VehicleUtils.getLicensePlate(item));
    }

    @Nullable
    public static String getDrivenVehiclePlate(Player p) {
        if (p.getVehicle() == null) {
            return null;
        }
        if (!p.getVehicle().getCustomName().contains("MTVEHICLES_")) {
            return null;
        }
        String[] name = p.getVehicle().getCustomName().split("_");
        return name[2];
    }

    public static Vehicle getDrivenVehicle(Player p) {
        if (VehicleUtils.getDrivenVehiclePlate(p) == null) {
            return null;
        }
        return VehicleUtils.getVehicle(VehicleUtils.getDrivenVehiclePlate(p));
    }

    @Deprecated
    public static ItemStack getItemByUUID(Player p, String uuid) {
        return VehicleUtils.createAndGetItemByUUID((OfflinePlayer)p, uuid);
    }

    public static boolean vehicleUUIDExists(String uuid) {
        boolean exists = false;
        List<Map<?, ?>> vehicles = ConfigModule.vehiclesConfig.getVehicles();
        block0: for (Map<?, ?> configVehicle : vehicles) {
            List skins = (List)configVehicle.get("cars");
            for (Map skin : skins) {
                if (skin.get("uuid") == null || !skin.get("uuid").equals(uuid)) continue;
                exists = true;
                break block0;
            }
        }
        return exists;
    }

    public static ItemStack createAndGetItemByUUID(OfflinePlayer owner, String uuid) {
        List<Map<?, ?>> vehicles = ConfigModule.vehiclesConfig.getVehicles();
        for (Map<?, ?> configVehicle : vehicles) {
            List skins = (List)configVehicle.get("cars");
            for (Map skin : skins) {
                if (skin.get("uuid") == null || !skin.get("uuid").equals(uuid)) continue;
                String nbtVal = skin.get("nbtValue") == null ? "null" : skin.get("nbtValue").toString();
                ItemStack item = ItemUtils.getVehicleItem(ItemUtils.getMaterial(skin.get("SkinItem").toString()), (int)((Integer)skin.get("itemDamage")), (String)skin.get("name"), "mtcustom", (Object)nbtVal);
                NBTItem nbt = new NBTItem(item);
                String licensePlate = nbt.getString("mtvehicles.kenteken");
                Vehicle vehicle = new Vehicle(null, licensePlate, (String)skin.get("name"), VehicleType.valueOf((String)configVehicle.get("vehicleType")), false, (Integer)skin.get("itemDamage"), (String)skin.get("SkinItem"), false, (Boolean)configVehicle.get("hornEnabled"), (Double)configVehicle.get("maxHealth"), (Boolean)configVehicle.get("benzineEnabled"), 100.0, 0.01, (Boolean)configVehicle.get("kofferbakEnabled"), 1, null, (Double)configVehicle.get("acceleratieSpeed"), (Double)configVehicle.get("maxSpeed"), (Double)configVehicle.get("maxSpeedBackwards"), (Double)configVehicle.get("brakingSpeed"), (Double)configVehicle.get("aftrekkenSpeed"), (Integer)configVehicle.get("rotateSpeed"), owner.getUniqueId(), null, null, (Double)skin.get("price"), (String)skin.get("nbtValue"));
                vehicle.save();
                return item;
            }
        }
        return null;
    }

    public static boolean getHornByDamage(int damage) {
        List<Map<?, ?>> vehicles = ConfigModule.vehiclesConfig.getVehicles();
        for (Map<?, ?> configVehicle : vehicles) {
            List skins = (List)configVehicle.get("cars");
            for (Map skin : skins) {
                if (skin.get("itemDamage") == null || !skin.get("itemDamage").equals(damage)) continue;
                return (Boolean)configVehicle.get("hornEnabled");
            }
        }
        return false;
    }

    public static double getMaxHealthByDamage(int damage) {
        List<Map<?, ?>> vehicles = ConfigModule.vehiclesConfig.getVehicles();
        for (Map<?, ?> configVehicle : vehicles) {
            List skins = (List)configVehicle.get("cars");
            for (Map skin : skins) {
                if (skin.get("itemDamage") == null || !skin.get("itemDamage").equals(damage)) continue;
                return (Double)configVehicle.get("maxHealth");
            }
        }
        return 0.0;
    }

    public static ItemStack getItemByLicensePlate(String licensePlate) {
        return VehicleUtils.getItem(VehicleUtils.getUUID(licensePlate));
    }

    public static ItemStack getItem(String carUUID) {
        List<Map<?, ?>> vehicles = ConfigModule.vehiclesConfig.getVehicles();
        ArrayList matchedVehicles = new ArrayList();
        for (Map<?, ?> configVehicle : vehicles) {
            List skins = (List)configVehicle.get("cars");
            for (Map skin : skins) {
                if (skin.get("uuid") == null || !skin.get("uuid").equals(carUUID) || skin.get("uuid") == null) continue;
                ItemStack is = ItemUtils.getVehicleItem(ItemUtils.getMaterial(skin.get("SkinItem").toString()), (Integer)skin.get("itemDamage"), (String)skin.get("name"));
                matchedVehicles.add(configVehicle);
                return is;
            }
        }
        return null;
    }

    public static boolean isVehicle(Entity entity) {
        return entity.getCustomName() != null && entity instanceof ArmorStand && entity.getCustomName().contains("MTVEHICLES");
    }

    @Nullable
    public static Player getCurrentDriver(String licensePlate) {
        Player driver = null;
        for (World world : Bukkit.getServer().getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if (entity.getCustomName() == null || !entity.getCustomName().contains("MAINSEAT_" + licensePlate)) continue;
                driver = (Player)entity.getPassenger();
            }
        }
        return driver;
    }

    public static String getLicensePlate(@Nullable Entity entity) {
        if (entity == null) {
            return null;
        }
        String name = entity.getCustomName();
        if (name.split("_").length > 1) {
            return name.split("_")[2];
        }
        return null;
    }

    public static String getUUID(String licensePlate) {
        if (!VehicleUtils.existsByLicensePlate(licensePlate)) {
            return null;
        }
        Object skinItem = ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.SKIN_ITEM);
        Object skinDamage = ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.SKIN_DAMAGE);
        Object nbtValue = ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.NBT_VALUE);
        List<Map<?, ?>> vehicles = ConfigModule.vehiclesConfig.getVehicles();
        for (Map<?, ?> configVehicle : vehicles) {
            List skins = (List)configVehicle.get("cars");
            for (Map skin : skins) {
                if (!skin.get("itemDamage").equals(skinDamage) || !skin.get("SkinItem").equals(skinItem)) continue;
                if (skin.get("nbtValue") != null) {
                    if (!skin.get("nbtValue").equals(nbtValue)) continue;
                    return skin.get("uuid").toString();
                }
                return skin.get("uuid").toString();
            }
        }
        return null;
    }

    @ToDo(value="Beautify the code inside this method.")
    public static Vehicle getVehicle(String licensePlate) {
        if (!VehicleUtils.existsByLicensePlate(licensePlate)) {
            return null;
        }
        HashMap<String, Object> vehicleData = new HashMap<String, Object>();
        for (VehicleDataConfig.Option option : VehicleDataConfig.Option.values()) {
            Object value = ConfigModule.vehicleDataConfig.get(licensePlate, option);
            if (value == null) continue;
            vehicleData.put(option.getPath(), value);
        }
        List<Map<?, ?>> vehicles = ConfigModule.vehiclesConfig.getVehicles();
        ArrayList matchedVehicles = new ArrayList();
        double price = 0.0;
        for (Map<?, ?> configVehicle : vehicles) {
            List skins = (List)configVehicle.get("cars");
            for (Map skin : skins) {
                if (!skin.get("itemDamage").equals(vehicleData.get(VehicleDataConfig.Option.SKIN_DAMAGE.getPath())) || !skin.get("SkinItem").equals(vehicleData.get(VehicleDataConfig.Option.SKIN_ITEM.getPath()))) continue;
                if (skin.get("nbtValue") != null) {
                    if (!skin.get("nbtValue").equals(vehicleData.get(VehicleDataConfig.Option.NBT_VALUE.getPath()))) continue;
                    matchedVehicles.add(configVehicle);
                    price = (Double)skin.get("price");
                    continue;
                }
                matchedVehicles.add(configVehicle);
                price = (Double)skin.get("price");
            }
        }
        if (matchedVehicles.isEmpty()) {
            return null;
        }
        if (matchedVehicles.size() > 1) {
            return null;
        }
        return new Vehicle((Map)matchedVehicles.get(0), licensePlate, (String)vehicleData.get(VehicleDataConfig.Option.NAME.getPath()), VehicleType.valueOf((String)vehicleData.get(VehicleDataConfig.Option.VEHICLE_TYPE.getPath())), (Boolean)vehicleData.get(VehicleDataConfig.Option.IS_OPEN.getPath()), (Integer)vehicleData.get(VehicleDataConfig.Option.SKIN_DAMAGE.getPath()), (String)vehicleData.get(VehicleDataConfig.Option.SKIN_ITEM.getPath()), (Boolean)vehicleData.get(VehicleDataConfig.Option.IS_GLOWING.getPath()), ConfigModule.vehicleDataConfig.isHornSet(licensePlate) ? ((Boolean)vehicleData.get(VehicleDataConfig.Option.HORN_ENABLED.getPath())).booleanValue() : ConfigModule.vehicleDataConfig.isHornEnabled(licensePlate), ConfigModule.vehicleDataConfig.isHealthSet(licensePlate) ? ((Double)vehicleData.get(VehicleDataConfig.Option.HEALTH.getPath())).doubleValue() : ConfigModule.vehicleDataConfig.getHealth(licensePlate), (Boolean)vehicleData.get(VehicleDataConfig.Option.FUEL_ENABLED.getPath()), (Double)vehicleData.get(VehicleDataConfig.Option.FUEL.getPath()), (Double)vehicleData.get(VehicleDataConfig.Option.FUEL_USAGE.getPath()), (Boolean)vehicleData.get(VehicleDataConfig.Option.TRUNK_ENABLED.getPath()), (Integer)vehicleData.get(VehicleDataConfig.Option.TRUNK_ROWS.getPath()), ConfigModule.vehicleDataConfig.getTrunkData(licensePlate), (Double)vehicleData.get(VehicleDataConfig.Option.ACCELERATION_SPEED.getPath()), (Double)vehicleData.get(VehicleDataConfig.Option.MAX_SPEED.getPath()), (Double)vehicleData.get(VehicleDataConfig.Option.MAX_SPEED_BACKWARDS.getPath()), (Double)vehicleData.get(VehicleDataConfig.Option.BRAKING_SPEED.getPath()), (Double)vehicleData.get(VehicleDataConfig.Option.FRICTION_SPEED.getPath()), (Integer)vehicleData.get(VehicleDataConfig.Option.ROTATION_SPEED.getPath()), UUID.fromString((String)vehicleData.get(VehicleDataConfig.Option.OWNER.getPath())), ConfigModule.vehicleDataConfig.getRiders(licensePlate), ConfigModule.vehicleDataConfig.getMembers(licensePlate), price, (String)vehicleData.get(VehicleDataConfig.Option.NBT_VALUE.getPath()));
    }

    public static boolean existsByLicensePlate(String licensePlate) {
        return ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.SKIN_ITEM) != null;
    }

    public static boolean canRide(Player player, String licensePlate) {
        return ConfigModule.vehicleDataConfig.getRiders(licensePlate).contains(player.getUniqueId().toString());
    }

    public static boolean canSit(Player player, String licensePlate) {
        return ConfigModule.vehicleDataConfig.getMembers(licensePlate).contains(player.getUniqueId().toString());
    }

    public static UUID getOwnerUUID(String licensePlate) {
        Object owner = ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.OWNER);
        if (owner == null) {
            return null;
        }
        return UUID.fromString(owner.toString());
    }

    public static void openTrunk(Player p, String license) {
        if (((Boolean)ConfigModule.defaultConfig.get(DefaultConfig.Option.TRUNK_ENABLED)).booleanValue()) {
            if (VehicleUtils.getVehicle(license) == null) {
                ConfigModule.messagesConfig.sendMessage((CommandSender)p, Message.VEHICLE_NOT_FOUND);
                return;
            }
            if (VehicleUtils.getVehicle(license).isOwner((OfflinePlayer)p) || p.hasPermission("mtvehicles.kofferbak")) {
                ConfigModule.configList.forEach(MTVConfig::reload);
                Inventory inv = Bukkit.createInventory(null, (int)((Integer)ConfigModule.vehicleDataConfig.get(license, VehicleDataConfig.Option.TRUNK_ROWS) * 9), (String)InventoryTitle.VEHICLE_TRUNK.getStringTitle());
                if (ConfigModule.vehicleDataConfig.get(license, VehicleDataConfig.Option.TRUNK_DATA) != null) {
                    List chestContentsFromConfig = (List)ConfigModule.vehicleDataConfig.get(license, VehicleDataConfig.Option.TRUNK_DATA);
                    for (ItemStack item : chestContentsFromConfig) {
                        if (item == null) continue;
                        inv.addItem(new ItemStack[]{item});
                    }
                }
                openedTrunk.put(p, license);
                VehicleData.trunkViewerAdd(license, p);
                p.openInventory(inv);
            } else {
                p.sendMessage(TextUtils.colorize(ConfigModule.messagesConfig.getMessage(Message.VEHICLE_NO_RIDER_TRUNK).replace("%p%", VehicleUtils.getVehicle(license).getOwnerName())));
            }
        }
    }

    public static boolean isTrunkInventoryOpen(Player p, String license) {
        return openedTrunk.containsKey(p) && openedTrunk.get(p).equals(license);
    }

    public static boolean isInsideVehicle(Player p) {
        if (p == null) {
            return false;
        }
        if (!p.isInsideVehicle()) {
            return false;
        }
        return VehicleUtils.isVehicle(p.getVehicle());
    }

    public static boolean isOccupied(String licensePlate) {
        return VehicleUtils.getCurrentDriver(licensePlate) != null;
    }

    @Deprecated
    public static String getRidersAsString(String licensePlate) {
        StringBuilder sb = new StringBuilder();
        for (String s : ConfigModule.vehicleDataConfig.getRiders(licensePlate)) {
            if (UUID.fromString(s).equals(VehicleUtils.getOwnerUUID(licensePlate))) continue;
            sb.append(Bukkit.getOfflinePlayer((UUID)UUID.fromString(s)).getName()).append(", ");
        }
        if (sb.toString().isEmpty()) {
            sb.append("Niemand");
        }
        return sb.toString();
    }

    public static void pickupVehicle(String license, Player player) {
        Vehicle vehicle = VehicleUtils.getVehicle(license);
        if (vehicle == null) {
            for (World world : Bukkit.getServer().getWorlds()) {
                for (Entity entity : world.getEntities()) {
                    if (entity.getCustomName() == null || !entity.getCustomName().contains(license)) continue;
                    entity.remove();
                }
            }
            ConfigModule.messagesConfig.sendMessage((CommandSender)player, Message.VEHICLE_NOT_FOUND);
            return;
        }
        if (vehicle.getOwnerName() == null) {
            ConfigModule.messagesConfig.sendMessage((CommandSender)player, Message.VEHICLE_NOT_FOUND);
            Main.logSevere("Could not find the owner of the vehicle " + license + "! The vehicleData.yml must be malformed!");
            return;
        }
        if (vehicle.isOwner((OfflinePlayer)player) && !((Boolean)ConfigModule.defaultConfig.get(DefaultConfig.Option.CAR_PICKUP)).booleanValue() || player.hasPermission("mtvehicles.oppakken")) {
            for (World world : Bukkit.getServer().getWorlds()) {
                for (Entity entity : world.getEntities()) {
                    if (entity.getCustomName() == null || !entity.getCustomName().contains(license)) continue;
                    ArmorStand test = (ArmorStand)entity;
                    if (test.getCustomName().contains("MTVEHICLES_SKIN_" + license)) {
                        for (Player trunkViewer : VehicleData.getTrunkViewers(license)) {
                            trunkViewer.closeInventory();
                        }
                        if (!TextUtils.checkInvFull(player)) {
                            player.getInventory().addItem(new ItemStack[]{test.getHelmet()});
                            player.sendMessage(TextUtils.colorize(ConfigModule.messagesConfig.getMessage(Message.VEHICLE_PICKUP).replace("%p%", vehicle.getOwnerName())));
                        } else {
                            ConfigModule.messagesConfig.sendMessage((CommandSender)player, Message.INVENTORY_FULL);
                            return;
                        }
                    }
                    test.remove();
                }
            }
        } else {
            if (((Boolean)ConfigModule.defaultConfig.get(DefaultConfig.Option.CAR_PICKUP)).booleanValue()) {
                player.sendMessage(TextUtils.colorize(ConfigModule.messagesConfig.getMessage(Message.CANNOT_DO_THAT_HERE)));
                return;
            }
            player.sendMessage(TextUtils.colorize(ConfigModule.messagesConfig.getMessage(Message.VEHICLE_NO_OWNER_PICKUP).replace("%p%", vehicle.getOwnerName())));
            return;
        }
    }

    public static void deleteVehicle(String ... licensePlates) throws IllegalArgumentException, IllegalStateException {
        for (String licensePlate : licensePlates) {
            if (!VehicleUtils.existsByLicensePlate(licensePlate)) {
                throw new IllegalArgumentException("Vehicle " + licensePlate + " does not exist.");
            }
            VehicleUtils.despawnVehicle(licensePlate);
            ConfigModule.vehicleDataConfig.delete(licensePlate);
        }
    }

    public static void teleportVehicle(String licensePlate, Location location) throws IllegalArgumentException {
        if (!VehicleUtils.existsByLicensePlate(licensePlate)) {
            throw new IllegalArgumentException("Vehicle does not exists.");
        }
        for (World world : Bukkit.getServer().getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if (entity.getCustomName() == null || !entity.getCustomName().contains(licensePlate)) continue;
                entity.teleport(location);
            }
        }
    }

    public static int despawnVehicle(String ... licensePlates) throws IllegalArgumentException {
        int despawned = 0;
        for (String licensePlate : licensePlates) {
            if (!VehicleUtils.existsByLicensePlate(licensePlate)) {
                throw new IllegalArgumentException("Vehicle " + licensePlate + " does not exist.");
            }
            for (Player trunkViewer : VehicleData.getTrunkViewers(licensePlate)) {
                trunkViewer.closeInventory();
            }
            for (World world : Bukkit.getServer().getWorlds()) {
                for (Entity entity : world.getEntities()) {
                    if (entity.getCustomName() == null || !entity.getCustomName().contains(licensePlate) || !entity.getCustomName().contains("MTVEHICLES")) continue;
                    entity.remove();
                    ++despawned;
                }
            }
        }
        return despawned;
    }

    public static int despawnVehicle(World world, String ... licensePlates) throws IllegalArgumentException {
        int despawned = 0;
        for (String licensePlate : licensePlates) {
            if (!VehicleUtils.existsByLicensePlate(licensePlate)) {
                throw new IllegalArgumentException("Vehicle " + licensePlate + " does not exist.");
            }
            for (Player trunkViewer : VehicleData.getTrunkViewers(licensePlate)) {
                trunkViewer.closeInventory();
            }
            for (Entity entity : world.getEntities()) {
                if (entity.getCustomName() == null || !entity.getCustomName().contains(licensePlate)) continue;
                entity.remove();
                ++despawned;
            }
        }
        return despawned;
    }

    public static List<String> getAllSpawnedVehiclePlates() {
        ArrayList<String> list = new ArrayList<String>();
        for (World world : Bukkit.getServer().getWorlds()) {
            for (Entity entity : world.getEntities()) {
                String name;
                if (entity.getCustomName() == null || !(name = entity.getCustomName()).contains("MTVEHICLES_MAIN_")) continue;
                list.add(name.split("_")[2]);
            }
        }
        return list;
    }

    public static List<String> getAllSpawnedVehiclePlates(World world) {
        ArrayList<String> list = new ArrayList<String>();
        for (Entity entity : world.getEntities()) {
            String name;
            if (entity.getCustomName() == null || !(name = entity.getCustomName()).contains("MTVEHICLES_MAIN_")) continue;
            list.add(name.split("_")[2]);
        }
        return list;
    }

    public static Set<String> getUniqueSpawnedVehiclePlates() {
        return new HashSet<String>(VehicleUtils.getAllSpawnedVehiclePlates());
    }

    public static Set<String> getUniqueSpawnedVehiclePlates(World world) {
        return new HashSet<String>(VehicleUtils.getAllSpawnedVehiclePlates(world));
    }

    public static boolean setFuel(String licensePlate, Double fuel) {
        if (!VehicleUtils.existsByLicensePlate(licensePlate)) {
            return false;
        }
        if (!(fuel <= 100.0) || !(fuel >= 0.0)) {
            return false;
        }
        VehicleData.fuel.put(licensePlate, fuel);
        ConfigModule.vehicleDataConfig.set(licensePlate, VehicleDataConfig.Option.FUEL, fuel);
        return true;
    }

    @ToDo(value="Beautify the code inside this method.")
    public static void enterVehicle(String licensePlate, Player p) {
        if (VehicleData.autostand2.get(licensePlate) != null && !VehicleData.autostand2.get(licensePlate).isEmpty()) {
            return;
        }
        Vehicle vehicle = VehicleUtils.getVehicle(licensePlate);
        if (vehicle == null) {
            ConfigModule.messagesConfig.sendMessage((CommandSender)p, Message.VEHICLE_NOT_FOUND);
            return;
        }
        if (vehicle.getOwnerName() == null) {
            ConfigModule.messagesConfig.sendMessage((CommandSender)p, Message.VEHICLE_NOT_FOUND);
            Main.logSevere("Could not find the owner of vehicle " + licensePlate + "! The vehicleData.yml must be malformed!");
            return;
        }
        if (!(vehicle.isPublic() || vehicle.isOwner((OfflinePlayer)p) || vehicle.canRide(p) || p.hasPermission("mtvehicles.ride"))) {
            p.sendMessage(ConfigModule.messagesConfig.getMessage(Message.VEHICLE_NO_RIDER_ENTER).replace("%p%", vehicle.getOwnerName()));
            return;
        }
        for (Entity entity : p.getWorld().getEntities()) {
            if (entity.getCustomName() == null || !entity.getCustomName().contains(licensePlate)) continue;
            ArmorStand vehicleAs = (ArmorStand)entity;
            if (!entity.isEmpty()) {
                return;
            }
            VehicleData.fuel.put(licensePlate, vehicle.getFuel());
            VehicleData.fuelUsage.put(licensePlate, (double)((Double)ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.FUEL_USAGE)));
            VehicleData.type.put(licensePlate, VehicleType.valueOf(ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.VEHICLE_TYPE).toString().toUpperCase(Locale.ROOT)));
            VehicleData.setRotationSpeed(licensePlate, (int)((Integer)ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.ROTATION_SPEED)));
            VehicleData.setSpeed(VehicleData.DataSpeed.MAXSPEED, licensePlate, (double)((Double)ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.MAX_SPEED)));
            VehicleData.setSpeed(VehicleData.DataSpeed.ACCELERATION, licensePlate, (double)((Double)ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.ACCELERATION_SPEED)));
            VehicleData.setSpeed(VehicleData.DataSpeed.BRAKING, licensePlate, (double)((Double)ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.BRAKING_SPEED)));
            VehicleData.setSpeed(VehicleData.DataSpeed.MAXSPEEDBACKWARDS, licensePlate, (double)((Double)ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.MAX_SPEED_BACKWARDS)));
            VehicleData.setSpeed(VehicleData.DataSpeed.FRICTION, licensePlate, (double)((Double)ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.FRICTION_SPEED)));
            Location location = new Location(entity.getWorld(), entity.getLocation().getX(), entity.getLocation().getY(), entity.getLocation().getZ(), entity.getLocation().getYaw(), entity.getLocation().getPitch());
            if (!ConfigModule.defaultConfig.canProceedWithAction(RegionAction.ENTER, vehicle.getVehicleType(), location, p)) {
                ConfigModule.messagesConfig.sendMessage((CommandSender)p, Message.CANNOT_DO_THAT_HERE);
                return;
            }
            VehicleType vehicleType = ConfigModule.vehicleDataConfig.getType(licensePlate);
            if (vehicleAs.getCustomName().contains("MTVEHICLES_SKIN_" + licensePlate)) {
                VehicleUtils.basicStandCreator(licensePlate, "SKIN", location, vehicleAs.getHelmet(), false);
                VehicleUtils.basicStandCreator(licensePlate, "MAIN", location, null, true);
                vehicle.saveSeats();
                List<Map<String, Double>> seats = vehicle.getSeats();
                VehicleData.seatsize.put(licensePlate, seats.size());
                for (int i = 1; i <= seats.size(); ++i) {
                    Map<String, Double> seat = seats.get(i - 1);
                    if (i == 1) {
                        VehicleUtils.mainSeatStandCreator(licensePlate, location, p, seat.get("x"), seat.get("y"), seat.get("z"));
                        BossBarUtils.addBossBar(p, licensePlate);
                        p.sendMessage(TextUtils.colorize(ConfigModule.messagesConfig.getMessage(Message.VEHICLE_ENTER_RIDER).replace("%p%", VehicleUtils.getVehicle(licensePlate).getOwnerName())));
                    }
                    if (i <= 1) continue;
                    VehicleData.seatx.put("MTVEHICLES_SEAT" + i + "_" + licensePlate, seat.get("x"));
                    VehicleData.seaty.put("MTVEHICLES_SEAT" + i + "_" + licensePlate, seat.get("y"));
                    VehicleData.seatz.put("MTVEHICLES_SEAT" + i + "_" + licensePlate, seat.get("z"));
                    Location location2 = new Location(location.getWorld(), location.getX() + Double.valueOf(seat.get("x")), location.getY() + Double.valueOf(seat.get("y")), location.getZ() + Double.valueOf(seat.get("z")));
                    ArmorStand as = (ArmorStand)location2.getWorld().spawn(location2, ArmorStand.class);
                    VehicleUtils.allowTicking(as);
                    as.setVisible(false);
                    as.setCustomName("MTVEHICLES_SEAT" + i + "_" + licensePlate);
                    as.setGravity(false);
                    VehicleData.autostand.put("MTVEHICLES_SEAT" + i + "_" + licensePlate, as);
                }
                List wiekens = (List)vehicle.getVehicleData().get("wiekens");
                if (vehicleType.isHelicopter()) {
                    VehicleData.maxheight.put(licensePlate, (int)((Integer)ConfigModule.defaultConfig.get(DefaultConfig.Option.MAX_FLYING_HEIGHT)));
                    for (int i = 1; i <= wiekens.size(); ++i) {
                        Map seat = (Map)wiekens.get(i - 1);
                        if (i != 1) continue;
                        Location location2 = new Location(location.getWorld(), location.getX() + (Double)seat.get("z"), Double.valueOf(location.getY()) + (Double)seat.get("y"), location.getZ() + (Double)seat.get("x"));
                        VehicleData.wiekenx.put("MTVEHICLES_WIEKENS_" + licensePlate, (Double)seat.get("x"));
                        VehicleData.wiekeny.put("MTVEHICLES_WIEKENS_" + licensePlate, (Double)seat.get("y"));
                        VehicleData.wiekenz.put("MTVEHICLES_WIEKENS_" + licensePlate, (Double)seat.get("z"));
                        ArmorStand as = (ArmorStand)location2.getWorld().spawn(location2, ArmorStand.class);
                        VehicleUtils.allowTicking(as);
                        as.setVisible(false);
                        as.setCustomName("MTVEHICLES_WIEKENS_" + licensePlate);
                        as.setGravity(false);
                        as.setHelmet((ItemStack)seat.get("item"));
                        VehicleData.autostand.put("MTVEHICLES_WIEKENS_" + licensePlate, as);
                    }
                }
            }
            vehicleAs.remove();
        }
    }

    private static void basicStandCreator(String license, String type, Location location, ItemStack item, Boolean gravity) {
        ArmorStand as = (ArmorStand)location.getWorld().spawn(location, ArmorStand.class);
        VehicleUtils.allowTicking(as);
        as.setVisible(false);
        as.setCustomName("MTVEHICLES_" + type + "_" + license);
        as.setHelmet(item);
        as.setGravity(gravity.booleanValue());
        VehicleData.autostand.put("MTVEHICLES_" + type + "_" + license, as);
    }

    private static void allowTicking(ArmorStand armorStand) {
        if (PaperUtils.isRunningPaper) {
            armorStand.setCanTick(true);
        }
    }

    private static void mainSeatStandCreator(String license, Location location, Player p, double x, double y, double z) {
        Location location2 = new Location(location.getWorld(), location.getX() + Double.valueOf(z), location.getY() + Double.valueOf(y), location.getZ() + Double.valueOf(z));
        ArmorStand as = (ArmorStand)location2.getWorld().spawn(location2, ArmorStand.class);
        VehicleUtils.allowTicking(as);
        as.setVisible(false);
        as.setCustomName("MTVEHICLES_MAINSEAT_" + license);
        as.setGravity(false);
        VehicleData.autostand.put("MTVEHICLES_MAINSEAT_" + license, as);
        VehicleData.speed.put(license, 0.0);
        VehicleData.speedhigh.put(license, 0.0);
        VehicleData.mainx.put("MTVEHICLES_MAINSEAT_" + license, x);
        VehicleData.mainy.put("MTVEHICLES_MAINSEAT_" + license, y);
        VehicleData.mainz.put("MTVEHICLES_MAINSEAT_" + license, z);
        as.setPassenger((Entity)p);
        VehicleData.autostand2.put(license, as);
    }

    public static Vehicle.Seat getSeat(Player player) {
        return Vehicle.Seat.getSeat(player);
    }

    public static boolean kickOut(Player player) throws IllegalStateException {
        if (VehicleUtils.getSeat(player) == null) {
            throw new IllegalStateException("Player is not seated in a vehicle!");
        }
        Entity seat = player.getVehicle();
        if (!VehicleUtils.getSeat(player).isDriver()) {
            return seat.removePassenger((Entity)player);
        }
        String license = VehicleUtils.getLicensePlate(seat);
        if (seat.removePassenger((Entity)player)) {
            BossBarUtils.removeBossBar(player, license);
            return VehicleUtils.turnOff(license);
        }
        return false;
    }

    public static Location getLocation(Vehicle vehicle) {
        return VehicleUtils.getLocation(vehicle.getLicensePlate());
    }

    public static Location getLocation(String licensePlate) {
        if (VehicleData.autostand.get("MTVEHICLES_MAIN_" + licensePlate) == null) {
            return null;
        }
        return VehicleData.autostand.get("MTVEHICLES_MAIN_" + licensePlate).getLocation();
    }

    public static boolean turnOff(@NotNull Vehicle vehicle) {
        String licensePlate = vehicle.getLicensePlate();
        if (VehicleData.autostand.get("MTVEHICLES_MAIN_" + licensePlate) == null) {
            return false;
        }
        ArmorStand standMain = VehicleData.autostand.get("MTVEHICLES_MAIN_" + licensePlate);
        ArmorStand standSkin = VehicleData.autostand.get("MTVEHICLES_SKIN_" + licensePlate);
        ArmorStand standMainSeat = VehicleData.autostand.get("MTVEHICLES_MAINSEAT_" + licensePlate);
        VehicleType vehicleType = VehicleData.type.get(licensePlate);
        VehicleData.lastRegions.remove(licensePlate);
        if (vehicleType == null) {
            return true;
        }
        if (vehicleType.isHelicopter()) {
            ArmorStand blades = VehicleData.autostand.get("MTVEHICLES_WIEKENS_" + licensePlate);
            Location locBelow = new Location(blades.getLocation().getWorld(), blades.getLocation().getX(), blades.getLocation().getY() - 0.2, blades.getLocation().getZ(), blades.getLocation().getYaw(), blades.getLocation().getPitch());
            blades.setGravity(locBelow.getBlock().getType().equals((Object)Material.AIR));
        }
        if (vehicleType.isHelicopter() && ((Boolean)ConfigModule.defaultConfig.get(DefaultConfig.Option.EXTREME_HELICOPTER_FALL)).booleanValue() && !standMainSeat.isOnGround()) {
            VehicleData.fallDamage.put(licensePlate, true);
        }
        if (!vehicleType.isBoat()) {
            standMain.setGravity(true);
            standSkin.setGravity(true);
        }
        List<Map<String, Double>> seats = vehicle.getSeats();
        for (int i = 2; i <= seats.size(); ++i) {
            if (VehicleData.autostand.get("MTVEHICLES_SEAT" + i + "_" + licensePlate) == null) continue;
            VehicleData.autostand.get("MTVEHICLES_SEAT" + i + "_" + licensePlate).remove();
        }
        VehicleData.type.remove(licensePlate);
        if (((Boolean)ConfigModule.defaultConfig.get(DefaultConfig.Option.FUEL_ENABLED)).booleanValue() && ((Boolean)ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.FUEL_ENABLED)).booleanValue()) {
            double fuel = VehicleData.fuel.get(licensePlate);
            ConfigModule.vehicleDataConfig.set(licensePlate, VehicleDataConfig.Option.FUEL, fuel);
            ConfigModule.vehicleDataConfig.saveToDisk();
        }
        return true;
    }

    public static boolean turnOff(@NotNull String licensePlate) {
        if (VehicleUtils.getVehicle(licensePlate) == null) {
            return false;
        }
        return VehicleUtils.turnOff(VehicleUtils.getVehicle(licensePlate));
    }

    public static Double getPrice(String carUUID) {
        List<Map<?, ?>> vehicles = ConfigModule.vehiclesConfig.getVehicles();
        for (Map<?, ?> configVehicle : vehicles) {
            List skins = (List)configVehicle.get("cars");
            for (Map skin : skins) {
                if (skin.get("uuid") == null || !skin.get("uuid").equals(carUUID) || skin.get("uuid") == null) continue;
                return (double)((Double)skin.get("price"));
            }
        }
        return null;
    }
}

