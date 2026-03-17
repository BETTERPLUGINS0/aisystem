/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  org.bukkit.Color
 *  org.bukkit.entity.ArmorStand
 *  org.bukkit.entity.Player
 */
package nl.sbdeveloper.vehiclesplus.api.vehicles;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Generated;
import nl.sbdeveloper.vehiclesplus.VehiclesPlusPluginManager;
import nl.sbdeveloper.vehiclesplus.api.VehiclesPlusAPI;
import nl.sbdeveloper.vehiclesplus.api.vehicles.IVehicle;
import nl.sbdeveloper.vehiclesplus.api.vehicles.parts.Part;
import nl.sbdeveloper.vehiclesplus.api.vehicles.settings.UpgradableSetting;
import nl.sbdeveloper.vehiclesplus.api.vehicles.settings.impl.Exhaust;
import nl.sbdeveloper.vehiclesplus.api.vehicles.settings.impl.Fuel;
import nl.sbdeveloper.vehiclesplus.api.vehicles.settings.impl.Gearbox;
import nl.sbdeveloper.vehiclesplus.api.vehicles.settings.impl.HeightLimit;
import nl.sbdeveloper.vehiclesplus.api.vehicles.settings.impl.Hitbox;
import nl.sbdeveloper.vehiclesplus.api.vehicles.settings.impl.Horn;
import nl.sbdeveloper.vehiclesplus.api.vehicles.settings.impl.Permissions;
import nl.sbdeveloper.vehiclesplus.api.vehicles.settings.impl.Sounds;
import nl.sbdeveloper.vehiclesplus.api.vehicles.statics.MovementStrategy;
import nl.sbdeveloper.vehiclesplus.api.vehicles.statics.StrategyFactory;
import nl.sbdeveloper.vehiclesplus.api.vehicles.types.MovementType;
import nl.sbdeveloper.vehiclesplus.api.vehicles.types.VehicleType;
import nl.sbdeveloper.vehiclesplus.utils.ColorUtil;
import nl.sbdeveloper.vehiclesplus.utils.jackson.ColorList;
import org.bukkit.Color;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.ANY)
@JsonDeserialize(builder=Builder.class)
@JsonIgnoreProperties(ignoreUnknown=true)
public class VehicleModel
extends nl.sbdeveloper.vehiclesplus.utils.Builder
implements IVehicle {
    @NotNull
    private String id;
    @NotNull
    private String displayName;
    @NotNull
    private String typeId;
    @NotNull
    private List<MovementStrategy> typeStrategies = new ArrayList<MovementStrategy>();
    private double price;
    @NotNull
    private Permissions permissions;
    @NotNull
    private ColorList availableColors = new ColorList((List<Color>)new ArrayList<Color>());
    @NotNull
    private final List<Part> parts = new ArrayList<Part>();
    @NotNull
    private UpgradableSetting maxSpeed;
    @NotNull
    private UpgradableSetting fuelTank;
    @NotNull
    private UpgradableSetting turningRadius;
    @NotNull
    private UpgradableSetting acceleration;
    @NotNull
    private Hitbox hitbox;
    @NotNull
    private Fuel fuel;
    @NotNull
    private Exhaust exhaust;
    @NotNull
    private Horn horn;
    @NotNull
    private Sounds sounds;
    @JsonInclude(value=JsonInclude.Include.NON_NULL)
    @Nullable
    private HeightLimit heightLimit;
    private boolean realisticSteering;
    private int trunkSize;
    private boolean drift;
    private boolean exitWhileMoving;
    private int health;
    @NotNull
    private Gearbox gearbox;

    private VehicleModel() {
    }

    public static Builder builder() {
        return new Builder();
    }

    @JsonIgnore
    @NotNull
    public VehicleType getType() {
        return VehiclesPlusAPI.getVehicleType(this.typeId).orElseThrow(() -> new IllegalArgumentException("VehicleType '" + this.typeId + "' not found!"));
    }

    @JsonIgnore
    @NotNull
    public String getDisplayNameColored() {
        return ColorUtil.__(this.displayName);
    }

    @JsonIgnore
    @Nullable
    public <V> V getPart(@NotNull Class<V> clazz) {
        return this.parts.stream().filter(clazz::isInstance).map(clazz::cast).findFirst().orElse(null);
    }

    @JsonIgnore
    @NotNull
    public <V> List<V> getParts(@NotNull Class<V> clazz) {
        return this.parts.stream().filter(clazz::isInstance).map(clazz::cast).collect(Collectors.toList());
    }

    @JsonIgnore
    @Nullable
    public Part getPart(ArmorStand armorStand) {
        return this.parts.stream().filter(part -> part.getHolder().getUniqueId().equals(armorStand.getUniqueId())).findFirst().orElse(null);
    }

    @JsonIgnore
    @NotNull
    public List<String> getInfoList() {
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add(ColorUtil.__("&cSpeed: &a" + String.valueOf(this.maxSpeed.getBase())));
        arrayList.add(ColorUtil.__("&cAcceleration: &a" + String.valueOf(this.acceleration.getBase())));
        arrayList.add(ColorUtil.__("&cFuel Tank: &a" + String.valueOf(this.fuelTank.getBase())));
        arrayList.add(ColorUtil.__("&cTurning Radius: &a" + String.valueOf(this.turningRadius.getBase())));
        return arrayList;
    }

    @Override
    public <T extends MovementStrategy, R> R getFromStrategy(MovementType movementType, Function<T, R> function, @NotNull R r) {
        Class clazz = StrategyFactory.getStrategyClass(movementType);
        for (MovementStrategy movementStrategy : this.typeStrategies) {
            if (!clazz.isInstance(movementStrategy)) continue;
            return function.apply((MovementStrategy)clazz.cast(movementStrategy));
        }
        return r;
    }

    @JsonIgnore
    public boolean isAllowedToBuy(@NotNull Player player) {
        return player.hasPermission(VehiclesPlusPluginManager.getConfig().getPermissions().getBuy()) || player.hasPermission(this.permissions.getBuy());
    }

    @JsonIgnore
    public boolean isAllowedToAdjust(@NotNull Player player) {
        return player.hasPermission(VehiclesPlusPluginManager.getConfig().getPermissions().getAdjust()) || player.hasPermission(this.permissions.getAdjust());
    }

    @JsonIgnore
    public boolean isAllowedToSpawn(@NotNull Player player) {
        return player.hasPermission(VehiclesPlusPluginManager.getConfig().getPermissions().getSpawn()) || player.hasPermission(this.permissions.getSpawn());
    }

    @JsonIgnore
    public boolean isAllowedToRide(@NotNull Player player) {
        return this.isAllowedToDrive(player) || this.permissions.isSitWithoutRidePermission();
    }

    @JsonIgnore
    public boolean isAllowedToDrive(@NotNull Player player) {
        return player.hasPermission(VehiclesPlusPluginManager.getConfig().getPermissions().getRide()) || player.hasPermission(this.permissions.getRide());
    }

    public void addAvailableColor(@NotNull Color color) {
        this.availableColors.add(color);
    }

    @Generated
    public String toString() {
        return "VehicleModel(id=" + this.getId() + ", displayName=" + this.getDisplayName() + ", typeId=" + this.getTypeId() + ", typeStrategies=" + String.valueOf(this.getTypeStrategies()) + ", price=" + this.getPrice() + ", permissions=" + String.valueOf(this.getPermissions()) + ", availableColors=" + String.valueOf(this.getAvailableColors()) + ", parts=" + String.valueOf(this.getParts()) + ", maxSpeed=" + String.valueOf(this.getMaxSpeed()) + ", fuelTank=" + String.valueOf(this.getFuelTank()) + ", turningRadius=" + String.valueOf(this.getTurningRadius()) + ", acceleration=" + String.valueOf(this.getAcceleration()) + ", hitbox=" + String.valueOf(this.getHitbox()) + ", fuel=" + String.valueOf(this.getFuel()) + ", exhaust=" + String.valueOf(this.getExhaust()) + ", horn=" + String.valueOf(this.getHorn()) + ", sounds=" + String.valueOf(this.getSounds()) + ", heightLimit=" + String.valueOf(this.getHeightLimit()) + ", realisticSteering=" + this.isRealisticSteering() + ", trunkSize=" + this.getTrunkSize() + ", drift=" + this.isDrift() + ", exitWhileMoving=" + this.isExitWhileMoving() + ", health=" + this.getHealth() + ", gearbox=" + String.valueOf(this.getGearbox()) + ")";
    }

    @NotNull
    @Generated
    public String getId() {
        return this.id;
    }

    @Generated
    public void setId(@NotNull String string) {
        if (string == null) {
            throw new NullPointerException("id is marked non-null but is null");
        }
        this.id = string;
    }

    @NotNull
    @Generated
    public String getDisplayName() {
        return this.displayName;
    }

    @Generated
    public void setDisplayName(@NotNull String string) {
        if (string == null) {
            throw new NullPointerException("displayName is marked non-null but is null");
        }
        this.displayName = string;
    }

    @NotNull
    @Generated
    public String getTypeId() {
        return this.typeId;
    }

    @Generated
    public void setTypeId(@NotNull String string) {
        if (string == null) {
            throw new NullPointerException("typeId is marked non-null but is null");
        }
        this.typeId = string;
    }

    @NotNull
    @Generated
    public List<MovementStrategy> getTypeStrategies() {
        return this.typeStrategies;
    }

    @Generated
    public double getPrice() {
        return this.price;
    }

    @Generated
    public void setPrice(double d) {
        this.price = d;
    }

    @NotNull
    @Generated
    public Permissions getPermissions() {
        return this.permissions;
    }

    @Generated
    public void setPermissions(@NotNull Permissions permissions) {
        if (permissions == null) {
            throw new NullPointerException("permissions is marked non-null but is null");
        }
        this.permissions = permissions;
    }

    @NotNull
    @Generated
    public ColorList getAvailableColors() {
        return this.availableColors;
    }

    @NotNull
    @Generated
    public List<Part> getParts() {
        return this.parts;
    }

    @NotNull
    @Generated
    public UpgradableSetting getMaxSpeed() {
        return this.maxSpeed;
    }

    @NotNull
    @Generated
    public UpgradableSetting getFuelTank() {
        return this.fuelTank;
    }

    @NotNull
    @Generated
    public UpgradableSetting getTurningRadius() {
        return this.turningRadius;
    }

    @NotNull
    @Generated
    public UpgradableSetting getAcceleration() {
        return this.acceleration;
    }

    @NotNull
    @Generated
    public Hitbox getHitbox() {
        return this.hitbox;
    }

    @NotNull
    @Generated
    public Fuel getFuel() {
        return this.fuel;
    }

    @NotNull
    @Generated
    public Exhaust getExhaust() {
        return this.exhaust;
    }

    @NotNull
    @Generated
    public Horn getHorn() {
        return this.horn;
    }

    @NotNull
    @Generated
    public Sounds getSounds() {
        return this.sounds;
    }

    @Nullable
    @Generated
    public HeightLimit getHeightLimit() {
        return this.heightLimit;
    }

    @Generated
    public boolean isRealisticSteering() {
        return this.realisticSteering;
    }

    @Generated
    public void setRealisticSteering(boolean bl) {
        this.realisticSteering = bl;
    }

    @Generated
    public int getTrunkSize() {
        return this.trunkSize;
    }

    @Generated
    public void setTrunkSize(int n) {
        this.trunkSize = n;
    }

    @Generated
    public boolean isDrift() {
        return this.drift;
    }

    @Generated
    public void setDrift(boolean bl) {
        this.drift = bl;
    }

    @Generated
    public boolean isExitWhileMoving() {
        return this.exitWhileMoving;
    }

    @Generated
    public void setExitWhileMoving(boolean bl) {
        this.exitWhileMoving = bl;
    }

    @Generated
    public int getHealth() {
        return this.health;
    }

    @Generated
    public void setHealth(int n) {
        this.health = n;
    }

    @NotNull
    @Generated
    public Gearbox getGearbox() {
        return this.gearbox;
    }

    @Generated
    public void setGearbox(@NotNull Gearbox gearbox) {
        if (gearbox == null) {
            throw new NullPointerException("gearbox is marked non-null but is null");
        }
        this.gearbox = gearbox;
    }

    @JsonPOJOBuilder(withPrefix="")
    public static class Builder {
        private final VehicleModel vehicleModel = new VehicleModel();

        private Builder() {
        }

        public Builder id(String string) {
            this.vehicleModel.id = string;
            return this;
        }

        public Builder displayName(String string) {
            this.vehicleModel.displayName = string;
            return this;
        }

        public Builder typeId(String string) {
            this.vehicleModel.typeId = string;
            return this;
        }

        public Builder price(double d) {
            this.vehicleModel.price = d;
            return this;
        }

        public Builder permissions(Permissions permissions) {
            this.vehicleModel.permissions = permissions;
            return this;
        }

        public Builder availableColors(ColorList colorList) {
            this.vehicleModel.availableColors = colorList;
            return this;
        }

        public Builder parts(List<Part> list) {
            this.vehicleModel.parts.addAll(list);
            return this;
        }

        public Builder part(Part part) {
            this.vehicleModel.parts.add(part);
            return this;
        }

        public Builder maxSpeed(UpgradableSetting upgradableSetting) {
            this.vehicleModel.maxSpeed = upgradableSetting;
            return this;
        }

        public Builder fuelTank(UpgradableSetting upgradableSetting) {
            this.vehicleModel.fuelTank = upgradableSetting;
            return this;
        }

        public Builder turningRadius(UpgradableSetting upgradableSetting) {
            this.vehicleModel.turningRadius = upgradableSetting;
            return this;
        }

        public Builder acceleration(UpgradableSetting upgradableSetting) {
            this.vehicleModel.acceleration = upgradableSetting;
            return this;
        }

        public Builder hitbox(Hitbox hitbox) {
            this.vehicleModel.hitbox = hitbox;
            return this;
        }

        public Builder fuel(Fuel fuel) {
            this.vehicleModel.fuel = fuel;
            return this;
        }

        public Builder exhaust(Exhaust exhaust) {
            this.vehicleModel.exhaust = exhaust;
            return this;
        }

        public Builder horn(Horn horn) {
            this.vehicleModel.horn = horn;
            return this;
        }

        public Builder sounds(Sounds sounds) {
            this.vehicleModel.sounds = sounds;
            return this;
        }

        public Builder heightLimit(HeightLimit heightLimit) {
            this.vehicleModel.heightLimit = heightLimit;
            return this;
        }

        public Builder realisticSteering(boolean bl) {
            this.vehicleModel.realisticSteering = bl;
            return this;
        }

        public Builder trunkSize(int n) {
            this.vehicleModel.trunkSize = n;
            return this;
        }

        public Builder drift(boolean bl) {
            this.vehicleModel.drift = bl;
            return this;
        }

        public Builder exitWhileMoving(boolean bl) {
            this.vehicleModel.exitWhileMoving = bl;
            return this;
        }

        public Builder health(int n) {
            this.vehicleModel.health = n;
            return this;
        }

        public Builder gearbox(Gearbox gearbox) {
            this.vehicleModel.gearbox = gearbox;
            return this;
        }

        public Builder typeStrategies(MovementStrategy ... movementStrategyArray) {
            this.vehicleModel.typeStrategies.addAll(List.of((Object[])movementStrategyArray));
            return this;
        }

        public VehicleModel build() {
            this.vehicleModel.validateNonNullFields();
            return this.vehicleModel;
        }
    }
}

