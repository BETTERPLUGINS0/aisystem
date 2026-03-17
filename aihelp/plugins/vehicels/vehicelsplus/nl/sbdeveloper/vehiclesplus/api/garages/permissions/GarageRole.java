/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package nl.sbdeveloper.vehiclesplus.api.garages.permissions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Generated;
import nl.sbdeveloper.vehiclesplus.api.garages.permissions.GaragePermissions;
import org.jetbrains.annotations.NotNull;

@JsonInclude(content=JsonInclude.Include.NON_EMPTY)
public class GarageRole {
    @JsonProperty
    @NotNull
    private String roleName;
    @JsonProperty
    @NotNull
    private GaragePermissions permissions;

    public GarageRole(@NotNull String string) {
        this.roleName = string;
        this.permissions = new GaragePermissions();
    }

    @JsonCreator
    public GarageRole(@JsonProperty(required=true, value="roleName") @NotNull String string, @JsonProperty(required=true, value="permissions") @NotNull GaragePermissions garagePermissions) {
        this.roleName = string;
        this.permissions = garagePermissions;
    }

    @NotNull
    @Generated
    public String getRoleName() {
        return this.roleName;
    }

    @NotNull
    @Generated
    public GaragePermissions getPermissions() {
        return this.permissions;
    }

    @JsonProperty
    @Generated
    public void setRoleName(@NotNull String string) {
        if (string == null) {
            throw new NullPointerException("roleName is marked non-null but is null");
        }
        this.roleName = string;
    }

    @JsonProperty
    @Generated
    public void setPermissions(@NotNull GaragePermissions garagePermissions) {
        if (garagePermissions == null) {
            throw new NullPointerException("permissions is marked non-null but is null");
        }
        this.permissions = garagePermissions;
    }
}

