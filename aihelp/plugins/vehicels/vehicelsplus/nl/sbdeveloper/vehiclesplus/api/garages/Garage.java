/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  org.bukkit.Bukkit
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.entity.Player
 */
package nl.sbdeveloper.vehiclesplus.api.garages;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.Generated;
import nl.sbdeveloper.vehiclesplus.VehiclesPlusPluginManager;
import nl.sbdeveloper.vehiclesplus.api.garages.permissions.GarageMember;
import nl.sbdeveloper.vehiclesplus.api.garages.permissions.GaragePermissions;
import nl.sbdeveloper.vehiclesplus.api.garages.permissions.GarageRole;
import nl.sbdeveloper.vehiclesplus.locale.Locale;
import nl.sbdeveloper.vehiclesplus.locale.PluginMessage;
import nl.sbdeveloper.vehiclesplus.storage.db.DataStorage;
import nl.sbdeveloper.vehiclesplus.storage.db.QueuedSavable;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@JsonInclude(content=JsonInclude.Include.NON_EMPTY)
public class Garage
implements QueuedSavable {
    @JsonProperty
    @NotNull
    private String name;
    private boolean personal;
    @JsonProperty
    @NotNull
    private UUID owner;
    @JsonProperty
    private final List<GarageMember> members;
    @JsonProperty
    private final List<GarageRole> roles;
    @JsonProperty
    private String displayName;
    @JsonProperty
    private final List<UUID> vehicles;

    public Garage(@NotNull String string, @NotNull UUID uUID) {
        this(string, uUID, Locale.getMessage(PluginMessage.INVENTORIES_VEHICLES_GARAGE_TITLE, (Map<String, String>)Map.of((Object)"%garage%", (Object)string)));
    }

    public Garage(@NotNull String string, @NotNull UUID uUID, String string2) {
        this(string, uUID, string2, false);
    }

    public Garage(@NotNull String string, @NotNull UUID uUID, String string2, boolean bl) {
        this(string, uUID, new ArrayList<GarageRole>(List.of((Object)new GarageRole("default"), (Object)new GarageRole("member", new GaragePermissions(true, true, true, true, true, true, false, false, false, false, false, false, false, false, false, false)), (Object)new GarageRole("owner", new GaragePermissions(true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true)))), new ArrayList<GarageMember>(List.of((Object)new GarageMember(uUID, "owner"))), new ArrayList<UUID>(), string2, bl);
    }

    private Garage(@NotNull String string, @NotNull UUID uUID, List<GarageRole> list, List<GarageMember> list2, List<UUID> list3, String string2, boolean bl) {
        this.name = string;
        this.owner = uUID;
        this.roles = list;
        this.members = list2;
        this.displayName = string2;
        this.vehicles = list3;
        this.personal = bl;
    }

    @JsonCreator
    private Garage(@JsonProperty(required=true, value="name") @NotNull String string, @JsonProperty(required=true, value="displayName") String string2, @JsonProperty(required=true, value="owner") @NotNull UUID uUID, @JsonProperty(required=true, value="roles") List<GarageRole> list, @JsonProperty(value="members") List<GarageMember> list2, @JsonProperty(value="vehicles") List<UUID> list3) {
        this.name = string;
        this.owner = uUID;
        this.roles = list;
        this.members = list2;
        this.displayName = string2;
        this.vehicles = list3;
    }

    @Override
    public void forceSave() {
        if (VehiclesPlusPluginManager.getConfig().getDataSettings().isVerbose()) {
            VehiclesPlusPluginManager.getVehiclesPlusPlugin().getLogger().info("Saving the garage " + this.name + " to data storage...");
        }
        DataStorage.getInstance().saveGarage(this);
    }

    @Override
    public String getSaveIdentifier() {
        return this.name;
    }

    @Override
    public String getSaveError() {
        return "An error occurred while saving the garage " + this.name + " to data storage.";
    }

    public GarageRole addRole(String string) {
        GarageRole garageRole = new GarageRole(string);
        this.roles.add(garageRole);
        return garageRole;
    }

    public boolean removeRole(String string) {
        return this.removeRole(string, false);
    }

    public boolean removeRole(String string, boolean bl) {
        Optional<GarageRole> optional = this.roles.stream().filter(garageRole -> garageRole.getRoleName().equals(string)).findFirst();
        if (optional.isEmpty()) {
            return false;
        }
        GarageRole garageRole2 = optional.get();
        if (!bl) {
            if (this.getMembers().stream().anyMatch(garageMember -> garageMember.getGarageRole().equals(string))) {
                return false;
            }
        } else {
            this.getMembers().stream().filter(garageMember -> garageMember.getGarageRole().equals(string)).forEach(garageMember -> garageMember.setGarageRole("default"));
        }
        return this.roles.remove(garageRole2);
    }

    public GarageRole getRole(Player player) {
        GarageMember garageMember = this.getMember(player);
        if (garageMember == null) {
            return this.getRole("default").get();
        }
        return this.getRole(this.getMember(player).getGarageRole()).get();
    }

    public Optional<GarageRole> getRole(String string) {
        return this.getRole(string, false);
    }

    public Optional<GarageRole> getRole(String string, boolean bl) {
        Optional<GarageRole> optional = this.roles.stream().filter(garageRole -> garageRole.getRoleName().equalsIgnoreCase(string)).findFirst();
        if (bl) {
            return Optional.of(optional.orElseGet(() -> this.addRole(string)));
        }
        return optional;
    }

    public void addMember(UUID uUID) {
        this.addMember(uUID, "member");
    }

    public void addMember(UUID uUID, String string) {
        this.members.add(new GarageMember(uUID, this.getRole(string, true).get().getRoleName()));
    }

    public boolean removeMember(UUID uUID) {
        return this.members.removeIf(garageMember -> garageMember.getMember().equals(uUID));
    }

    public GarageMember getMember(UUID uUID) {
        Optional<GarageMember> optional = this.members.stream().filter(garageMember -> garageMember.getMember().equals(uUID)).findFirst();
        return optional.orElse(null);
    }

    public GarageMember getMember(Player player) {
        Optional<GarageMember> optional = this.members.stream().filter(garageMember -> garageMember.getMember().equals(player.getUniqueId())).findFirst();
        return optional.orElse(null);
    }

    public void addVehicle(UUID uUID) {
        this.vehicles.add(uUID);
    }

    public boolean removeVehicle(UUID uUID) {
        return this.vehicles.removeIf(uUID2 -> uUID2.equals(uUID));
    }

    @JsonIgnore
    public OfflinePlayer getOwner() {
        return Optional.of(Bukkit.getOfflinePlayer((UUID)this.owner)).get();
    }

    public void setOwner(Player player) {
        if (this.getMember(player) != null) {
            this.getMember(this.owner).setGarageRole(this.getRole("member", true).get().getRoleName());
        } else {
            this.addMember(player.getUniqueId());
        }
        this.getMember(player).setGarageRole(this.getRole("owner", true).get().getRoleName());
        this.owner = player.getUniqueId();
    }

    @NotNull
    @Generated
    public String getName() {
        return this.name;
    }

    @Generated
    public boolean isPersonal() {
        return this.personal;
    }

    @Generated
    public List<GarageMember> getMembers() {
        return this.members;
    }

    @Generated
    public List<GarageRole> getRoles() {
        return this.roles;
    }

    @Generated
    public String getDisplayName() {
        return this.displayName;
    }

    @Generated
    public List<UUID> getVehicles() {
        return this.vehicles;
    }

    @Generated
    public void setPersonal(boolean bl) {
        this.personal = bl;
    }

    @JsonProperty
    @Generated
    public void setName(@NotNull String string) {
        if (string == null) {
            throw new NullPointerException("name is marked non-null but is null");
        }
        this.name = string;
    }

    @JsonProperty
    @Generated
    public void setDisplayName(String string) {
        this.displayName = string;
    }
}

