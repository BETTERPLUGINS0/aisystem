/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package nl.sbdeveloper.vehiclesplus.api.garages.permissions;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Generated;

@JsonDeserialize(builder=GaragePermissionsBuilder.class)
public class GaragePermissions {
    private boolean ride;
    private boolean openTrunk;
    private boolean lock;
    private boolean spawn;
    private boolean repair;
    private boolean remove;
    private boolean buy;
    private boolean upgrade;
    private boolean tune;
    private boolean vehicleRename;
    private boolean delete;
    private boolean transferVehicle;
    private boolean manageMembers;
    private boolean manageRoles;
    private boolean rename;
    private boolean deleteGarage;

    public GaragePermissions() {
        this.ride = true;
        this.openTrunk = true;
        this.lock = false;
        this.spawn = false;
        this.repair = false;
        this.remove = false;
        this.buy = false;
        this.upgrade = false;
        this.tune = false;
        this.vehicleRename = false;
        this.delete = false;
        this.transferVehicle = false;
        this.manageMembers = false;
        this.manageRoles = false;
        this.rename = false;
        this.deleteGarage = false;
    }

    @Generated
    public static GaragePermissionsBuilder builder() {
        return new GaragePermissionsBuilder();
    }

    @Generated
    public boolean isRide() {
        return this.ride;
    }

    @Generated
    public boolean isOpenTrunk() {
        return this.openTrunk;
    }

    @Generated
    public boolean isLock() {
        return this.lock;
    }

    @Generated
    public boolean isSpawn() {
        return this.spawn;
    }

    @Generated
    public boolean isRepair() {
        return this.repair;
    }

    @Generated
    public boolean isRemove() {
        return this.remove;
    }

    @Generated
    public boolean isBuy() {
        return this.buy;
    }

    @Generated
    public boolean isUpgrade() {
        return this.upgrade;
    }

    @Generated
    public boolean isTune() {
        return this.tune;
    }

    @Generated
    public boolean isVehicleRename() {
        return this.vehicleRename;
    }

    @Generated
    public boolean isDelete() {
        return this.delete;
    }

    @Generated
    public boolean isTransferVehicle() {
        return this.transferVehicle;
    }

    @Generated
    public boolean isManageMembers() {
        return this.manageMembers;
    }

    @Generated
    public boolean isManageRoles() {
        return this.manageRoles;
    }

    @Generated
    public boolean isRename() {
        return this.rename;
    }

    @Generated
    public boolean isDeleteGarage() {
        return this.deleteGarage;
    }

    @Generated
    public void setRide(boolean bl) {
        this.ride = bl;
    }

    @Generated
    public void setOpenTrunk(boolean bl) {
        this.openTrunk = bl;
    }

    @Generated
    public void setLock(boolean bl) {
        this.lock = bl;
    }

    @Generated
    public void setSpawn(boolean bl) {
        this.spawn = bl;
    }

    @Generated
    public void setRepair(boolean bl) {
        this.repair = bl;
    }

    @Generated
    public void setRemove(boolean bl) {
        this.remove = bl;
    }

    @Generated
    public void setBuy(boolean bl) {
        this.buy = bl;
    }

    @Generated
    public void setUpgrade(boolean bl) {
        this.upgrade = bl;
    }

    @Generated
    public void setTune(boolean bl) {
        this.tune = bl;
    }

    @Generated
    public void setVehicleRename(boolean bl) {
        this.vehicleRename = bl;
    }

    @Generated
    public void setDelete(boolean bl) {
        this.delete = bl;
    }

    @Generated
    public void setTransferVehicle(boolean bl) {
        this.transferVehicle = bl;
    }

    @Generated
    public void setManageMembers(boolean bl) {
        this.manageMembers = bl;
    }

    @Generated
    public void setManageRoles(boolean bl) {
        this.manageRoles = bl;
    }

    @Generated
    public void setRename(boolean bl) {
        this.rename = bl;
    }

    @Generated
    public void setDeleteGarage(boolean bl) {
        this.deleteGarage = bl;
    }

    @Generated
    public GaragePermissions(boolean bl, boolean bl2, boolean bl3, boolean bl4, boolean bl5, boolean bl6, boolean bl7, boolean bl8, boolean bl9, boolean bl10, boolean bl11, boolean bl12, boolean bl13, boolean bl14, boolean bl15, boolean bl16) {
        this.ride = bl;
        this.openTrunk = bl2;
        this.lock = bl3;
        this.spawn = bl4;
        this.repair = bl5;
        this.remove = bl6;
        this.buy = bl7;
        this.upgrade = bl8;
        this.tune = bl9;
        this.vehicleRename = bl10;
        this.delete = bl11;
        this.transferVehicle = bl12;
        this.manageMembers = bl13;
        this.manageRoles = bl14;
        this.rename = bl15;
        this.deleteGarage = bl16;
    }

    @JsonPOJOBuilder(withPrefix="", buildMethodName="build")
    @Generated
    public static class GaragePermissionsBuilder {
        @Generated
        private boolean ride;
        @Generated
        private boolean openTrunk;
        @Generated
        private boolean lock;
        @Generated
        private boolean spawn;
        @Generated
        private boolean repair;
        @Generated
        private boolean remove;
        @Generated
        private boolean buy;
        @Generated
        private boolean upgrade;
        @Generated
        private boolean tune;
        @Generated
        private boolean vehicleRename;
        @Generated
        private boolean delete;
        @Generated
        private boolean transferVehicle;
        @Generated
        private boolean manageMembers;
        @Generated
        private boolean manageRoles;
        @Generated
        private boolean rename;
        @Generated
        private boolean deleteGarage;

        @Generated
        GaragePermissionsBuilder() {
        }

        @Generated
        public GaragePermissionsBuilder ride(boolean bl) {
            this.ride = bl;
            return this;
        }

        @Generated
        public GaragePermissionsBuilder openTrunk(boolean bl) {
            this.openTrunk = bl;
            return this;
        }

        @Generated
        public GaragePermissionsBuilder lock(boolean bl) {
            this.lock = bl;
            return this;
        }

        @Generated
        public GaragePermissionsBuilder spawn(boolean bl) {
            this.spawn = bl;
            return this;
        }

        @Generated
        public GaragePermissionsBuilder repair(boolean bl) {
            this.repair = bl;
            return this;
        }

        @Generated
        public GaragePermissionsBuilder remove(boolean bl) {
            this.remove = bl;
            return this;
        }

        @Generated
        public GaragePermissionsBuilder buy(boolean bl) {
            this.buy = bl;
            return this;
        }

        @Generated
        public GaragePermissionsBuilder upgrade(boolean bl) {
            this.upgrade = bl;
            return this;
        }

        @Generated
        public GaragePermissionsBuilder tune(boolean bl) {
            this.tune = bl;
            return this;
        }

        @Generated
        public GaragePermissionsBuilder vehicleRename(boolean bl) {
            this.vehicleRename = bl;
            return this;
        }

        @Generated
        public GaragePermissionsBuilder delete(boolean bl) {
            this.delete = bl;
            return this;
        }

        @Generated
        public GaragePermissionsBuilder transferVehicle(boolean bl) {
            this.transferVehicle = bl;
            return this;
        }

        @Generated
        public GaragePermissionsBuilder manageMembers(boolean bl) {
            this.manageMembers = bl;
            return this;
        }

        @Generated
        public GaragePermissionsBuilder manageRoles(boolean bl) {
            this.manageRoles = bl;
            return this;
        }

        @Generated
        public GaragePermissionsBuilder rename(boolean bl) {
            this.rename = bl;
            return this;
        }

        @Generated
        public GaragePermissionsBuilder deleteGarage(boolean bl) {
            this.deleteGarage = bl;
            return this;
        }

        @Generated
        public GaragePermissions build() {
            return new GaragePermissions(this.ride, this.openTrunk, this.lock, this.spawn, this.repair, this.remove, this.buy, this.upgrade, this.tune, this.vehicleRename, this.delete, this.transferVehicle, this.manageMembers, this.manageRoles, this.rename, this.deleteGarage);
        }

        @Generated
        public String toString() {
            return "GaragePermissions.GaragePermissionsBuilder(ride=" + this.ride + ", openTrunk=" + this.openTrunk + ", lock=" + this.lock + ", spawn=" + this.spawn + ", repair=" + this.repair + ", remove=" + this.remove + ", buy=" + this.buy + ", upgrade=" + this.upgrade + ", tune=" + this.tune + ", vehicleRename=" + this.vehicleRename + ", delete=" + this.delete + ", transferVehicle=" + this.transferVehicle + ", manageMembers=" + this.manageMembers + ", manageRoles=" + this.manageRoles + ", rename=" + this.rename + ", deleteGarage=" + this.deleteGarage + ")";
        }
    }
}

