/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.md_5.bungee.api.ChatColor
 *  org.bukkit.inventory.ItemStack
 */
package nl.sbdeveloper.vehiclesplus.api.vehicles.parts.impl.seat;

import java.util.Arrays;
import java.util.Optional;
import lombok.Generated;
import net.md_5.bungee.api.ChatColor;
import nl.sbdeveloper.vehiclesplus.api.vehicles.impl.DrivableVehicle;
import nl.sbdeveloper.vehiclesplus.api.vehicles.impl.SpawnedVehicle;
import nl.sbdeveloper.vehiclesplus.api.vehicles.movement.MovementInput;
import nl.sbdeveloper.vehiclesplus.api.vehicles.parts.PartTypeName;
import nl.sbdeveloper.vehiclesplus.api.vehicles.parts.impl.seat.Controllable;
import nl.sbdeveloper.vehiclesplus.api.vehicles.parts.impl.seat.Seat;
import nl.sbdeveloper.vehiclesplus.api.vehicles.parts.impl.skin.Turret;
import nl.sbdeveloper.vehiclesplus.libs.xseries.XMaterial;
import nl.sbdeveloper.vehiclesplus.utils.ItemBuilder;
import org.bukkit.inventory.ItemStack;

@PartTypeName(value="turretseat")
public class TurretSeat
extends Seat
implements Controllable {
    private final String controllingTurret;

    public TurretSeat() {
        this(0.0, 0.0, 0.0, "");
    }

    public TurretSeat(double d, double d2, double d3, String string) {
        this(d, d2, d3, 0, string);
    }

    public TurretSeat(double d, double d2, double d3, int n, String string) {
        super(d, d2, d3, n, false);
        this.controllingTurret = string;
    }

    @Override
    public void handleInput(MovementInput movementInput) {
        Optional<SpawnedVehicle> optional = this.getOwningVehicle();
        if (optional.isEmpty()) {
            return;
        }
        DrivableVehicle drivableVehicle = optional.get().getAsDrivableVehicle();
        if (drivableVehicle == null) {
            return;
        }
        Turret turret2 = drivableVehicle.getPart(Turret.class, turret -> turret.getIdentifier().equals(this.controllingTurret)).orElseThrow(IllegalArgumentException::new);
        if (movementInput.isD() || movementInput.isA()) {
            double d;
            double d2 = turret2.getXOffset();
            double d3 = turret2.getZOffset();
            double d4 = Math.sqrt(Math.pow(this.xOffset - d2, 2.0) + Math.pow(this.zOffset - d3, 2.0));
            double d5 = Math.atan2(this.zOffset - d3, this.xOffset - d2);
            double d6 = Math.toRadians(1.0);
            if (movementInput.isA()) {
                d = d5 - d6;
                turret2.setRotationOffset(turret2.getRotationOffset() + 1);
            } else {
                d = d5 + d6;
                turret2.setRotationOffset(turret2.getRotationOffset() - 1);
            }
            this.rotationOffset = turret2.getRotationOffset();
            this.xOffset = d2 + d4 * Math.cos(d);
            this.zOffset = d3 + d4 * Math.sin(d);
        }
        if (movementInput.isSpace()) {
            ItemStack itemStack = turret2.getAmmo().clone();
            itemStack.setAmount(1);
            if (Arrays.stream(drivableVehicle.getTrunk().getContents()).anyMatch(itemStack2 -> itemStack2 != null && itemStack2.isSimilar(itemStack)) && turret2.shoot()) {
                drivableVehicle.getTrunk().removeItem(new ItemStack[]{itemStack});
            }
        }
    }

    @Override
    public ItemStack getPartGUIItem() {
        return new ItemBuilder(XMaterial.DIAMOND_HOE).displayname(String.valueOf(ChatColor.GOLD) + "Turret Seat").lore(String.valueOf(ChatColor.GRAY) + "For players to sit on.", String.valueOf(ChatColor.GRAY) + "This part will move a turret.").unbreakable().durability(3).hideAllFlags().getItemStack();
    }

    @Override
    public String asString() {
        return super.asString() + String.valueOf(ChatColor.GOLD) + "Controlling Turret: " + String.valueOf(ChatColor.WHITE) + this.controllingTurret + "\n";
    }

    @Generated
    public String getControllingTurret() {
        return this.controllingTurret;
    }
}

