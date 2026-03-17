/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package nl.sbdeveloper.vehiclesplus.inventories.garages;

import java.util.function.BiConsumer;
import java.util.function.Function;
import lombok.Generated;
import nl.sbdeveloper.vehiclesplus.api.garages.permissions.GaragePermissions;
import nl.sbdeveloper.vehiclesplus.libs.xseries.XMaterial;
import nl.sbdeveloper.vehiclesplus.locale.PluginMessage;

enum Permissions {
    RIDE(XMaterial.SADDLE, PluginMessage.INVENTORIES_VEHICLES_GARAGE_SETTINGS_SETTING_RIDE, GaragePermissions::isRide, GaragePermissions::setRide, false),
    OPENTRUNK(XMaterial.CHEST, PluginMessage.INVENTORIES_VEHICLES_GARAGE_SETTINGS_SETTING_OPENTRUNK, GaragePermissions::isOpenTrunk, GaragePermissions::setOpenTrunk, false),
    LOCK(XMaterial.TRIPWIRE_HOOK, PluginMessage.INVENTORIES_VEHICLES_GARAGE_SETTINGS_SETTING_LOCK, GaragePermissions::isLock, GaragePermissions::setLock, true),
    SPAWN(XMaterial.MINECART, PluginMessage.INVENTORIES_VEHICLES_GARAGE_SETTINGS_SETTING_SPAWN, GaragePermissions::isSpawn, GaragePermissions::setSpawn, true),
    REPAIR(XMaterial.ANVIL, PluginMessage.INVENTORIES_VEHICLES_GARAGE_SETTINGS_SETTING_REPAIR, GaragePermissions::isRepair, GaragePermissions::setRepair, true),
    REMOVE(XMaterial.BARRIER, PluginMessage.INVENTORIES_VEHICLES_GARAGE_SETTINGS_SETTING_REMOVE, GaragePermissions::isRemove, GaragePermissions::setRemove, true),
    BUY(XMaterial.EMERALD, PluginMessage.INVENTORIES_VEHICLES_GARAGE_SETTINGS_SETTING_BUY, GaragePermissions::isBuy, GaragePermissions::setBuy, true),
    UPGRADE(XMaterial.IRON_INGOT, PluginMessage.INVENTORIES_VEHICLES_GARAGE_SETTINGS_SETTING_UPGRADE, GaragePermissions::isUpgrade, GaragePermissions::setUpgrade, true),
    TUNE(XMaterial.NOTE_BLOCK, PluginMessage.INVENTORIES_VEHICLES_GARAGE_SETTINGS_SETTING_TUNE, GaragePermissions::isTune, GaragePermissions::setTune, true),
    RENAME_VEHICLE(XMaterial.NAME_TAG, PluginMessage.INVENTORIES_VEHICLES_GARAGE_SETTINGS_SETTING_RENAMEVEHICLE, GaragePermissions::isVehicleRename, GaragePermissions::setVehicleRename, true),
    DELETE(XMaterial.RED_STAINED_GLASS_PANE, PluginMessage.INVENTORIES_VEHICLES_GARAGE_SETTINGS_SETTING_DELETE, GaragePermissions::isDelete, GaragePermissions::setDelete, true),
    TRANSFER_VEHICLE(XMaterial.PLAYER_HEAD, PluginMessage.INVENTORIES_VEHICLES_GARAGE_SETTINGS_SETTING_TRANSFERVEHICLE, GaragePermissions::isTransferVehicle, GaragePermissions::setTransferVehicle, true),
    MANAGE_MEMBERS(XMaterial.SKELETON_SKULL, PluginMessage.INVENTORIES_VEHICLES_GARAGE_SETTINGS_SETTING_MANAGEMEMBERS_NAME, GaragePermissions::isManageMembers, GaragePermissions::setManageMembers, true),
    MANAGE_ROLES(XMaterial.MINECART, PluginMessage.INVENTORIES_VEHICLES_GARAGE_SETTINGS_SETTING_MANAGEROLES, GaragePermissions::isManageRoles, GaragePermissions::setManageRoles, true),
    RENAME(XMaterial.OAK_SIGN, PluginMessage.INVENTORIES_VEHICLES_GARAGE_SETTINGS_SETTING_RENAME, GaragePermissions::isRename, GaragePermissions::setRename, true),
    DETETE_GARAGE(XMaterial.TNT, PluginMessage.INVENTORIES_VEHICLES_GARAGE_SETTINGS_SETTING_DELETEGARAGE, GaragePermissions::isDeleteGarage, GaragePermissions::setDeleteGarage, true);

    private final XMaterial material;
    private final PluginMessage message;
    private final Function<GaragePermissions, Boolean> getter;
    private final BiConsumer<GaragePermissions, Boolean> setter;
    private final boolean isMembersOnly;

    private Permissions(XMaterial xMaterial, PluginMessage pluginMessage, Function<GaragePermissions, Boolean> function, BiConsumer<GaragePermissions, Boolean> biConsumer, boolean bl) {
        this.material = xMaterial;
        this.message = pluginMessage;
        this.getter = function;
        this.setter = biConsumer;
        this.isMembersOnly = bl;
    }

    @Generated
    public XMaterial getMaterial() {
        return this.material;
    }

    @Generated
    public PluginMessage getMessage() {
        return this.message;
    }

    @Generated
    public Function<GaragePermissions, Boolean> getGetter() {
        return this.getter;
    }

    @Generated
    public BiConsumer<GaragePermissions, Boolean> getSetter() {
        return this.setter;
    }

    @Generated
    public boolean isMembersOnly() {
        return this.isMembersOnly;
    }
}

