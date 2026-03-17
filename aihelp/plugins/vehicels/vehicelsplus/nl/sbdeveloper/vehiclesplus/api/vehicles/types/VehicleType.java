/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package nl.sbdeveloper.vehiclesplus.api.vehicles.types;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import lombok.Generated;
import nl.sbdeveloper.vehiclesplus.api.vehicles.VehicleModel;
import nl.sbdeveloper.vehiclesplus.api.vehicles.defaults.DefaultVehicleModel;
import nl.sbdeveloper.vehiclesplus.api.vehicles.types.FrictionType;
import nl.sbdeveloper.vehiclesplus.api.vehicles.types.MovementType;
import nl.sbdeveloper.vehiclesplus.api.vehicles.types.TiltType;
import nl.sbdeveloper.vehiclesplus.handlers.StorageHandler;
import nl.sbdeveloper.vehiclesplus.storage.db.Savable;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonDeserialize(builder=VehicleTypeBuilder.class)
public class VehicleType
implements Savable {
    private final String name;
    private final List<MovementType> movementTypes;
    private final List<TiltType> tiltTypes;
    private FrictionType frictionType;
    private Class<? extends DefaultVehicleModel> defaultModel;

    public void addMovementType(MovementType movementType) {
        this.movementTypes.add(movementType);
    }

    @JsonIgnore
    public boolean hasMovementType(MovementType movementType) {
        return this.movementTypes.contains((Object)movementType);
    }

    @JsonIgnore
    public boolean isMovementType(MovementType movementType) {
        return this.movementTypes.size() == 1 && this.hasMovementType(movementType);
    }

    @JsonIgnore
    public boolean canTilt() {
        return !this.tiltTypes.isEmpty();
    }

    public void addTiltType(TiltType tiltType) {
        this.tiltTypes.add(tiltType);
    }

    @JsonIgnore
    public boolean hasTiltType(TiltType tiltType) {
        return this.tiltTypes.contains((Object)tiltType);
    }

    @JsonIgnore
    public boolean isTiltType(TiltType tiltType) {
        return this.tiltTypes.size() == 1 && this.hasTiltType(tiltType);
    }

    public VehicleModel constructDefaultModel() {
        return DefaultVehicleModel.constructBuilder(this.defaultModel).build();
    }

    @Override
    public void save() {
        StorageHandler.save(this, "vehicletypes", this.name);
    }

    @Generated
    VehicleType(String string, List<MovementType> list, List<TiltType> list2, FrictionType frictionType, Class<? extends DefaultVehicleModel> clazz) {
        this.name = string;
        this.movementTypes = list;
        this.tiltTypes = list2;
        this.frictionType = frictionType;
        this.defaultModel = clazz;
    }

    @Generated
    public static VehicleTypeBuilder builder() {
        return new VehicleTypeBuilder();
    }

    @Generated
    public String getName() {
        return this.name;
    }

    @Generated
    public List<MovementType> getMovementTypes() {
        return this.movementTypes;
    }

    @Generated
    public List<TiltType> getTiltTypes() {
        return this.tiltTypes;
    }

    @Generated
    public FrictionType getFrictionType() {
        return this.frictionType;
    }

    @Generated
    public void setFrictionType(FrictionType frictionType) {
        this.frictionType = frictionType;
    }

    @Generated
    public Class<? extends DefaultVehicleModel> getDefaultModel() {
        return this.defaultModel;
    }

    @Generated
    public void setDefaultModel(Class<? extends DefaultVehicleModel> clazz) {
        this.defaultModel = clazz;
    }

    @JsonIgnoreProperties(ignoreUnknown=true)
    @JsonPOJOBuilder(withPrefix="", buildMethodName="build")
    @Generated
    public static class VehicleTypeBuilder {
        @Generated
        private String name;
        @Generated
        private ArrayList<MovementType> movementTypes;
        @Generated
        private ArrayList<TiltType> tiltTypes;
        @Generated
        private FrictionType frictionType;
        @Generated
        private Class<? extends DefaultVehicleModel> defaultModel;

        @Generated
        VehicleTypeBuilder() {
        }

        @Generated
        public VehicleTypeBuilder name(String string) {
            this.name = string;
            return this;
        }

        @Generated
        public VehicleTypeBuilder movementType(MovementType movementType) {
            if (this.movementTypes == null) {
                this.movementTypes = new ArrayList();
            }
            this.movementTypes.add(movementType);
            return this;
        }

        @Generated
        public VehicleTypeBuilder movementTypes(Collection<? extends MovementType> collection) {
            if (collection != null) {
                if (this.movementTypes == null) {
                    this.movementTypes = new ArrayList();
                }
                this.movementTypes.addAll(collection);
            }
            return this;
        }

        @Generated
        public VehicleTypeBuilder clearMovementTypes() {
            if (this.movementTypes != null) {
                this.movementTypes.clear();
            }
            return this;
        }

        @Generated
        public VehicleTypeBuilder tiltType(TiltType tiltType) {
            if (this.tiltTypes == null) {
                this.tiltTypes = new ArrayList();
            }
            this.tiltTypes.add(tiltType);
            return this;
        }

        @Generated
        public VehicleTypeBuilder tiltTypes(Collection<? extends TiltType> collection) {
            if (collection != null) {
                if (this.tiltTypes == null) {
                    this.tiltTypes = new ArrayList();
                }
                this.tiltTypes.addAll(collection);
            }
            return this;
        }

        @Generated
        public VehicleTypeBuilder clearTiltTypes() {
            if (this.tiltTypes != null) {
                this.tiltTypes.clear();
            }
            return this;
        }

        @Generated
        public VehicleTypeBuilder frictionType(FrictionType frictionType) {
            this.frictionType = frictionType;
            return this;
        }

        @Generated
        public VehicleTypeBuilder defaultModel(Class<? extends DefaultVehicleModel> clazz) {
            this.defaultModel = clazz;
            return this;
        }

        @Generated
        public VehicleType build() {
            return new VehicleType(this.name, switch (this.movementTypes == null ? 0 : this.movementTypes.size()) {
                case 0 -> Collections.emptyList();
                case 1 -> Collections.singletonList(this.movementTypes.get(0));
                default -> Collections.unmodifiableList(new ArrayList<MovementType>(this.movementTypes));
            }, switch (this.tiltTypes == null ? 0 : this.tiltTypes.size()) {
                case 0 -> Collections.emptyList();
                case 1 -> Collections.singletonList(this.tiltTypes.get(0));
                default -> Collections.unmodifiableList(new ArrayList<TiltType>(this.tiltTypes));
            }, this.frictionType, this.defaultModel);
        }

        @Generated
        public String toString() {
            return "VehicleType.VehicleTypeBuilder(name=" + this.name + ", movementTypes=" + String.valueOf(this.movementTypes) + ", tiltTypes=" + String.valueOf(this.tiltTypes) + ", frictionType=" + String.valueOf((Object)this.frictionType) + ", defaultModel=" + String.valueOf(this.defaultModel) + ")";
        }
    }
}

