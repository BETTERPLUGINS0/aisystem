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
import java.util.UUID;
import lombok.Generated;
import org.jetbrains.annotations.NotNull;

@JsonInclude(content=JsonInclude.Include.NON_EMPTY)
public class GarageMember {
    @JsonProperty
    @NotNull
    private UUID member;
    @JsonProperty
    @NotNull
    private String garageRole;

    @JsonCreator
    public GarageMember(@JsonProperty(required=true, value="member") @NotNull UUID uUID, @JsonProperty(required=true, value="garageRole") @NotNull String string) {
        this.member = uUID;
        this.garageRole = string;
    }

    @NotNull
    @Generated
    public UUID getMember() {
        return this.member;
    }

    @NotNull
    @Generated
    public String getGarageRole() {
        return this.garageRole;
    }

    @JsonProperty
    @Generated
    public void setMember(@NotNull UUID uUID) {
        if (uUID == null) {
            throw new NullPointerException("member is marked non-null but is null");
        }
        this.member = uUID;
    }

    @JsonProperty
    @Generated
    public void setGarageRole(@NotNull String string) {
        if (string == null) {
            throw new NullPointerException("garageRole is marked non-null but is null");
        }
        this.garageRole = string;
    }
}

