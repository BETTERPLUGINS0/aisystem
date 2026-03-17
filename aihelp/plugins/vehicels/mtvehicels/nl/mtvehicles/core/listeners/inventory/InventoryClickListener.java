/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemStack
 */
package nl.mtvehicles.core.listeners.inventory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.commands.vehiclesubs.VehicleEdit;
import nl.mtvehicles.core.commands.vehiclesubs.VehicleMenu;
import nl.mtvehicles.core.infrastructure.dataconfig.MessagesConfig;
import nl.mtvehicles.core.infrastructure.dataconfig.VehicleDataConfig;
import nl.mtvehicles.core.infrastructure.enums.InventoryTitle;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.enums.VehicleType;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.NBTItem;
import nl.mtvehicles.core.infrastructure.models.MTVListener;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.infrastructure.utils.ItemUtils;
import nl.mtvehicles.core.infrastructure.utils.LanguageUtils;
import nl.mtvehicles.core.infrastructure.utils.MenuUtils;
import nl.mtvehicles.core.infrastructure.utils.TextUtils;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;
import nl.mtvehicles.core.listeners.VehicleVoucherListener;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryClickListener
extends MTVListener {
    public HashMap<UUID, ItemStack> vehicleMenu = new HashMap();
    public static HashMap<UUID, Inventory> skinMenu = new HashMap();
    public static HashMap<UUID, Integer> intSave = new HashMap();
    public static HashMap<UUID, Integer> id = new HashMap();
    public static HashMap<UUID, Integer> raw = new HashMap();
    private ItemStack clickedItem;
    private int clickedSlot;
    private InventoryTitle title;

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        this.event = event;
        if (event.getCurrentItem() == null) {
            return;
        }
        if (!event.getCurrentItem().hasItemMeta()) {
            return;
        }
        String stringTitle = event.getView().getTitle();
        this.clickedItem = event.getCurrentItem();
        this.clickedSlot = event.getRawSlot();
        this.player = (Player)event.getWhoClicked();
        if (InventoryTitle.getByStringTitle(stringTitle) == null) {
            return;
        }
        this.title = InventoryTitle.getByStringTitle(stringTitle);
        this.setAPI(new nl.mtvehicles.core.events.inventory.InventoryClickEvent(this.title));
        nl.mtvehicles.core.events.inventory.InventoryClickEvent api = (nl.mtvehicles.core.events.inventory.InventoryClickEvent)this.getAPI();
        api.setClickedSlot(this.clickedSlot);
        this.callAPI();
        if (this.isCancelled()) {
            return;
        }
        this.clickedSlot = api.getClickedSlot();
        this.title = api.getTitle();
        event.setCancelled(true);
        if (this.title.equals((Object)InventoryTitle.VEHICLE_MENU)) {
            this.vehicleMenu();
        } else if (this.title.equals((Object)InventoryTitle.CHOOSE_VEHICLE_MENU)) {
            this.chooseVehicleMenu();
        } else if (this.title.equals((Object)InventoryTitle.CHOOSE_LANGUAGE_MENU)) {
            this.chooseLanguageMenu();
        } else if (this.title.equals((Object)InventoryTitle.CONFIRM_VEHICLE_MENU)) {
            this.confirmVehicleMenu();
        } else if (this.title.equals((Object)InventoryTitle.VEHICLE_RESTORE_MENU)) {
            this.vehicleRestoreMenu();
        } else if (this.title.equals((Object)InventoryTitle.VEHICLE_EDIT_MENU)) {
            this.vehicleEditMenu();
        } else if (this.title.equals((Object)InventoryTitle.VEHICLE_SETTINGS_MENU)) {
            this.vehicleSettingsMenu();
        } else if (this.title.equals((Object)InventoryTitle.VEHICLE_FUEL_MENU)) {
            this.vehicleFuelMenu();
        } else if (this.title.equals((Object)InventoryTitle.VEHICLE_TRUNK_MENU)) {
            this.vehicleTrunkMenu();
        } else if (this.title.equals((Object)InventoryTitle.VEHICLE_MEMBERS_MENU)) {
            this.vehicleMembersMenu();
        } else if (this.title.equals((Object)InventoryTitle.VEHICLE_SPEED_MENU)) {
            this.vehicleSpeedMenu();
        } else if (this.title.equals((Object)InventoryTitle.JERRYCAN_MENU)) {
            this.jerryCanMenu();
        } else if (this.title.equals((Object)InventoryTitle.VOUCHER_REDEEM_MENU)) {
            this.voucherRedeemMenu();
        } else {
            event.setCancelled(false);
        }
    }

    private void vehicleMenu() {
        id.put(this.player.getUniqueId(), 1);
        raw.put(this.player.getUniqueId(), this.clickedSlot);
        MenuUtils.getvehicleCMD(this.player, id.get(this.player.getUniqueId()), raw.get(this.player.getUniqueId()));
    }

    private void chooseVehicleMenu() {
        if (this.clickedItem.equals((Object)MenuUtils.getCloseItem())) {
            this.player.closeInventory();
            return;
        }
        if (this.clickedItem.equals((Object)MenuUtils.getBackItem())) {
            this.player.openInventory(VehicleMenu.beginMenu.get(this.player.getUniqueId()));
            return;
        }
        if (this.clickedItem.equals((Object)ItemUtils.getMenuItem(ItemUtils.getStainedGlassPane(), 1, "&c", "&c"))) {
            return;
        }
        if (this.clickedSlot == 53) {
            MenuUtils.getvehicleCMD(this.player, id.get(this.player.getUniqueId()) + 1, raw.get(this.player.getUniqueId()));
            id.put(this.player.getUniqueId(), id.get(this.player.getUniqueId()) + 1);
            return;
        }
        if (this.clickedSlot == 45) {
            if (id.get(this.player.getUniqueId()) > 1) {
                MenuUtils.getvehicleCMD(this.player, id.get(this.player.getUniqueId()) - 1, raw.get(this.player.getUniqueId()));
                id.put(this.player.getUniqueId(), id.get(this.player.getUniqueId()) - 1);
            }
            return;
        }
        this.vehicleMenu.put(this.player.getUniqueId(), this.clickedItem);
        Inventory inv = Bukkit.createInventory(null, (int)27, (String)InventoryTitle.CONFIRM_VEHICLE_MENU.getStringTitle());
        MessagesConfig msg = ConfigModule.messagesConfig;
        inv.setItem(11, ItemUtils.getMenuItem("RED_WOOL", "WOOL", (short)14, 1, "&c" + msg.getMessage(Message.CANCEL), "&7" + msg.getMessage(Message.CANCEL_ACTION)));
        inv.setItem(15, ItemUtils.getMenuItem("LIME_WOOL", "WOOL", (short)5, 1, "&a" + msg.getMessage(Message.CONFIRM), "&7" + msg.getMessage(Message.CONFIRM_ACTION), "&7" + msg.getMessage(Message.CONFIRM_VEHICLE_GIVE)));
        this.player.openInventory(inv);
    }

    private void chooseLanguageMenu() {
        LanguageUtils.changeLanguageMenu(this.player, this.clickedSlot);
        this.player.closeInventory();
    }

    private void confirmVehicleMenu() {
        if (this.clickedSlot == 11) {
            this.player.openInventory(skinMenu.get(this.player.getUniqueId()));
        } else if (this.clickedSlot == 15) {
            if (!this.canGetVehicleFromMenu()) {
                this.player.closeInventory();
                return;
            }
            List<Map<?, ?>> vehicles = ConfigModule.vehiclesConfig.getVehicles();
            ConfigModule.messagesConfig.sendMessage((CommandSender)this.player, Message.COMPLETED_VEHICLE_GIVE);
            this.player.getInventory().addItem(new ItemStack[]{this.vehicleMenu.get(this.player.getUniqueId())});
            NBTItem nbt = new NBTItem(this.vehicleMenu.get(this.player.getUniqueId()));
            String licensePlate = nbt.getString("mtvehicles.kenteken");
            List skins = (List)vehicles.get(intSave.get(this.player.getUniqueId())).get("cars");
            double price = 0.0;
            for (Map skin : skins) {
                if (!skin.get("itemDamage").equals(vehicles.get(intSave.get(this.player.getUniqueId())).get("skinDamage")) || !skin.get("SkinItem").equals(vehicles.get(intSave.get(this.player.getUniqueId())).get("skinItem"))) continue;
                price = (Double)skin.get("price");
            }
            Vehicle vehicle = new Vehicle(null, licensePlate, nbt.getString("mtvehicles.naam"), VehicleType.valueOf((String)vehicles.get(intSave.get(this.player.getUniqueId())).get("vehicleType")), false, this.vehicleMenu.get(this.player.getUniqueId()).getDurability(), this.vehicleMenu.get(this.player.getUniqueId()).getType().toString(), false, (Boolean)vehicles.get(intSave.get(this.player.getUniqueId())).get("hornEnabled"), (Double)vehicles.get(intSave.get(this.player.getUniqueId())).get("maxHealth"), (Boolean)vehicles.get(intSave.get(this.player.getUniqueId())).get("benzineEnabled"), 100.0, 0.01, (Boolean)vehicles.get(intSave.get(this.player.getUniqueId())).get("kofferbakEnabled"), 1, ConfigModule.vehicleDataConfig.getTrunkData(licensePlate), (Double)vehicles.get(intSave.get(this.player.getUniqueId())).get("acceleratieSpeed"), (Double)vehicles.get(intSave.get(this.player.getUniqueId())).get("maxSpeed"), (Double)vehicles.get(intSave.get(this.player.getUniqueId())).get("maxSpeedBackwards"), (Double)vehicles.get(intSave.get(this.player.getUniqueId())).get("brakingSpeed"), (Double)vehicles.get(intSave.get(this.player.getUniqueId())).get("aftrekkenSpeed"), (Integer)vehicles.get(intSave.get(this.player.getUniqueId())).get("rotateSpeed"), this.player.getUniqueId(), ConfigModule.vehicleDataConfig.getRiders(licensePlate), ConfigModule.vehicleDataConfig.getMembers(licensePlate), price, nbt.getString("mtcustom"));
            vehicle.save();
            this.player.closeInventory();
        }
    }

    private void vehicleRestoreMenu() {
        if (this.clickedItem.equals((Object)ItemUtils.getMenuItem(ItemUtils.getStainedGlassPane(), 1, "&c", "&c"))) {
            return;
        }
        if (this.clickedSlot == 53) {
            MenuUtils.restoreCMD(this.player, MenuUtils.restorePage.get(this.player) + 1, MenuUtils.restoreUUID.get(this.player));
            return;
        }
        if (this.clickedSlot == 45) {
            if (MenuUtils.restorePage.get(this.player) - 1 >= 1) {
                MenuUtils.restoreCMD(this.player, MenuUtils.restorePage.get(this.player) - 1, MenuUtils.restoreUUID.get(this.player));
            }
            return;
        }
        this.player.getInventory().addItem(new ItemStack[]{this.clickedItem});
    }

    private void vehicleEditMenu() {
        switch (this.clickedSlot) {
            case 10: {
                MenuUtils.menuEdit(this.player);
                break;
            }
            case 11: {
                MenuUtils.benzineEdit(this.player);
                break;
            }
            case 12: {
                MenuUtils.trunkEdit(this.player);
                break;
            }
            case 13: {
                MenuUtils.membersEdit(this.player);
                break;
            }
            case 14: {
                MenuUtils.speedEdit(this.player);
                break;
            }
            case 16: {
                this.deleteVehicle();
            }
        }
    }

    private void deleteVehicle() {
        try {
            NBTItem nbt = new NBTItem(this.player.getInventory().getItemInMainHand());
            String licensePlate = nbt.getString("mtvehicles.kenteken");
            VehicleUtils.getVehicle(licensePlate).delete();
            this.player.sendMessage(TextUtils.colorize(ConfigModule.messagesConfig.getMessage(Message.VEHICLE_DELETED)));
        } catch (Exception e) {
            this.player.sendMessage(TextUtils.colorize(ConfigModule.messagesConfig.getMessage(Message.VEHICLE_ALREADY_DELETED)));
        }
        this.player.getInventory().getItemInMainHand().setAmount(0);
        this.player.closeInventory();
    }

    private void vehicleSettingsMenu() {
        if (this.clickedItem.equals((Object)MenuUtils.getCloseItem())) {
            this.player.closeInventory();
            return;
        }
        if (this.clickedItem.equals((Object)MenuUtils.getBackItem())) {
            VehicleEdit.editMenu(this.player, this.player.getInventory().getItemInMainHand());
            return;
        }
        NBTItem nbt = new NBTItem(this.player.getInventory().getItemInMainHand());
        String licensePlate = nbt.getString("mtvehicles.kenteken");
        boolean isGlowing = (Boolean)ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.IS_GLOWING);
        if (this.clickedSlot == 16) {
            VehicleEdit.editGlowing(this.player, licensePlate, isGlowing ? "false" : "true");
            ConfigModule.vehicleDataConfig.save();
            MenuUtils.menuEdit(this.player);
        }
        if (this.clickedSlot == 13) {
            this.player.closeInventory();
            ConfigModule.messagesConfig.sendMessage((CommandSender)this.player, Message.TYPE_LICENSE_IN_CHAT);
            ItemUtils.edit.put(this.player.getUniqueId() + ".kenteken", true);
        }
        if (this.clickedSlot == 10) {
            this.player.closeInventory();
            ConfigModule.messagesConfig.sendMessage((CommandSender)this.player, Message.TYPE_NAME_IN_CHAT);
            ItemUtils.edit.put(this.player.getUniqueId() + ".naam", true);
        }
    }

    private void vehicleFuelMenu() {
        if (this.clickedItem.equals((Object)MenuUtils.getCloseItem())) {
            this.player.closeInventory();
            return;
        }
        if (this.clickedItem.equals((Object)MenuUtils.getBackItem())) {
            VehicleEdit.editMenu(this.player, this.player.getInventory().getItemInMainHand());
            return;
        }
        NBTItem nbt = new NBTItem(this.player.getInventory().getItemInMainHand());
        String licensePlate = nbt.getString("mtvehicles.kenteken");
        String menuItem = new NBTItem(this.clickedItem).getString("mtvehicles.item");
        if (menuItem.contains("1")) {
            boolean fuelEnabled = (Boolean)ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.FUEL_ENABLED);
            VehicleEdit.editFuelEnabled(this.player, licensePlate, fuelEnabled ? "false" : "true");
            MenuUtils.benzineEdit(this.player);
        }
        if (menuItem.contains("2")) {
            this.player.closeInventory();
            ConfigModule.messagesConfig.sendMessage((CommandSender)this.player, Message.TYPE_NEW_BENZINE_IN_CHAT);
            ItemUtils.edit.put(this.player.getUniqueId() + ".benzine", true);
        }
        if (menuItem.contains("3")) {
            this.player.closeInventory();
            ConfigModule.messagesConfig.sendMessage((CommandSender)this.player, Message.TYPE_NEW_BENZINE_IN_CHAT);
            ItemUtils.edit.put(this.player.getUniqueId() + ".benzineverbruik", true);
        }
    }

    private void vehicleTrunkMenu() {
        if (this.clickedItem.equals((Object)MenuUtils.getCloseItem())) {
            this.player.closeInventory();
            return;
        }
        if (this.clickedItem.equals((Object)MenuUtils.getBackItem())) {
            VehicleEdit.editMenu(this.player, this.player.getInventory().getItemInMainHand());
            return;
        }
        NBTItem nbt = new NBTItem(this.player.getInventory().getItemInMainHand());
        String licensePlate = nbt.getString("mtvehicles.kenteken");
        String menuItem = new NBTItem(this.clickedItem).getString("mtvehicles.item");
        if (menuItem.contains("1")) {
            boolean trunkEnabled = (Boolean)ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.TRUNK_ENABLED);
            VehicleEdit.editTrunkEnabled(this.player, licensePlate, trunkEnabled ? "false" : "true");
            MenuUtils.trunkEdit(this.player);
        }
        if (menuItem.contains("2")) {
            this.player.closeInventory();
            ConfigModule.messagesConfig.sendMessage((CommandSender)this.player, Message.TYPE_NEW_ROWS_IN_CHAT);
            ItemUtils.edit.put(this.player.getUniqueId() + ".kofferbakRows", true);
        }
        if (menuItem.contains("3")) {
            this.player.closeInventory();
            VehicleUtils.openTrunk(this.player, licensePlate);
        }
    }

    private void vehicleMembersMenu() {
        if (this.clickedItem.equals((Object)MenuUtils.getCloseItem())) {
            this.player.closeInventory();
        } else if (this.clickedItem.equals((Object)MenuUtils.getBackItem())) {
            VehicleEdit.editMenu(this.player, this.player.getInventory().getItemInMainHand());
        }
    }

    private void vehicleSpeedMenu() {
        if (this.clickedItem.equals((Object)MenuUtils.getCloseItem())) {
            this.player.closeInventory();
            return;
        }
        if (this.clickedItem.equals((Object)MenuUtils.getBackItem())) {
            VehicleEdit.editMenu(this.player, this.player.getInventory().getItemInMainHand());
            return;
        }
        String menuItem = new NBTItem(this.clickedItem).getString("mtvehicles.item");
        if (menuItem.contains("1")) {
            this.player.closeInventory();
            ConfigModule.messagesConfig.sendMessage((CommandSender)this.player, Message.TYPE_SPEED_IN_CHAT);
            ItemUtils.edit.put(this.player.getUniqueId() + ".acceleratieSpeed", true);
        }
        if (menuItem.contains("2")) {
            this.player.closeInventory();
            ConfigModule.messagesConfig.sendMessage((CommandSender)this.player, Message.TYPE_SPEED_IN_CHAT);
            ItemUtils.edit.put(this.player.getUniqueId() + ".maxSpeed", true);
        }
        if (menuItem.contains("3")) {
            this.player.closeInventory();
            ConfigModule.messagesConfig.sendMessage((CommandSender)this.player, Message.TYPE_SPEED_IN_CHAT);
            ItemUtils.edit.put(this.player.getUniqueId() + ".brakingSpeed", true);
        }
        if (menuItem.contains("4")) {
            this.player.closeInventory();
            ConfigModule.messagesConfig.sendMessage((CommandSender)this.player, Message.TYPE_SPEED_IN_CHAT);
            ItemUtils.edit.put(this.player.getUniqueId() + ".aftrekkenSpeed", true);
        }
        if (menuItem.contains("5")) {
            this.player.closeInventory();
            ConfigModule.messagesConfig.sendMessage((CommandSender)this.player, Message.TYPE_SPEED_IN_CHAT);
            ItemUtils.edit.put(this.player.getUniqueId() + ".rotateSpeed", true);
        }
        if (menuItem.contains("6")) {
            this.player.closeInventory();
            ConfigModule.messagesConfig.sendMessage((CommandSender)this.player, Message.TYPE_SPEED_IN_CHAT);
            ItemUtils.edit.put(this.player.getUniqueId() + ".maxSpeedBackwards", true);
        }
    }

    private void jerryCanMenu() {
        this.player.getInventory().addItem(new ItemStack[]{this.clickedItem});
    }

    private void voucherRedeemMenu() {
        if (this.clickedSlot == 15) {
            String carUUID = VehicleVoucherListener.voucher.get(this.player);
            if (VehicleUtils.createAndGetItemByUUID((OfflinePlayer)this.player, carUUID) == null) {
                this.player.sendMessage(ConfigModule.messagesConfig.getMessage(Message.GIVE_CAR_NOT_FOUND));
                this.player.closeInventory();
                return;
            }
            this.player.sendMessage(ConfigModule.messagesConfig.getMessage(Message.VOUCHER_REDEEM));
            this.player.getInventory().getItemInMainHand().setAmount(this.player.getInventory().getItemInMainHand().getAmount() - 1);
            this.player.getInventory().addItem(new ItemStack[]{VehicleUtils.createAndGetItemByUUID((OfflinePlayer)this.player, carUUID)});
        }
        VehicleVoucherListener.voucher.remove(this.player);
        this.player.closeInventory();
    }

    private boolean canGetVehicleFromMenu() {
        boolean canGetMore;
        int owned = ConfigModule.vehicleDataConfig.getNumberOfOwnedVehicles(this.player);
        if (this.player.hasPermission("mtvehicles.nolimit")) {
            return true;
        }
        int limit = this.player.getEffectivePermissions().stream().filter(permission -> permission.getPermission().startsWith("mtvehicles.limit.") && permission.getValue()).map(permission -> {
            try {
                return Integer.parseInt(permission.getPermission().replace("mtvehicles.limit.", ""));
            } catch (NumberFormatException e) {
                Main.logSevere("An error occurred whilst trying to retrieve player's 'mtvehicles.limit.X' permission.");
                return 0;
            }
        }).findFirst().orElse(-1);
        boolean bl = canGetMore = limit == -1 || limit > owned;
        if (!canGetMore) {
            ConfigModule.messagesConfig.sendMessage((CommandSender)this.player, Message.TOO_MANY_VEHICLES);
        }
        return canGetMore;
    }
}

