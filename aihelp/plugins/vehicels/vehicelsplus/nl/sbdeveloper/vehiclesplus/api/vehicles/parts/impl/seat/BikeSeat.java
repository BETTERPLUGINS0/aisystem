/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.md_5.bungee.api.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.util.Vector
 */
package nl.sbdeveloper.vehiclesplus.api.vehicles.parts.impl.seat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Optional;
import lombok.Generated;
import net.md_5.bungee.api.ChatColor;
import nl.sbdeveloper.vehiclesplus.api.vehicles.impl.DrivableVehicle;
import nl.sbdeveloper.vehiclesplus.api.vehicles.impl.SpawnedVehicle;
import nl.sbdeveloper.vehiclesplus.api.vehicles.movement.MovementInput;
import nl.sbdeveloper.vehiclesplus.api.vehicles.parts.Part;
import nl.sbdeveloper.vehiclesplus.api.vehicles.parts.PartTypeName;
import nl.sbdeveloper.vehiclesplus.api.vehicles.parts.impl.seat.Controllable;
import nl.sbdeveloper.vehiclesplus.api.vehicles.parts.impl.seat.Seat;
import nl.sbdeveloper.vehiclesplus.api.vehicles.parts.impl.skin.BikeSkin;
import nl.sbdeveloper.vehiclesplus.libs.xseries.XMaterial;
import nl.sbdeveloper.vehiclesplus.utils.ItemBuilder;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

@PartTypeName(value="bikeseat")
public class BikeSeat
extends Seat
implements Controllable {
    @JsonIgnore
    private boolean wheelie = false;
    @JsonIgnore
    private float extraYOffset = 0.0f;

    public BikeSeat() {
    }

    public BikeSeat(double d, double d2, double d3, boolean bl) {
        this(d, d2, d3, 0, bl);
    }

    public BikeSeat(double d, double d2, double d3, int n, boolean bl) {
        super(d, d2, d3, n, bl);
    }

    @Override
    public void handleInput(MovementInput movementInput) {
        if (!this.isSteer()) {
            return;
        }
        this.wheelie = movementInput.isSpace();
        Optional<SpawnedVehicle> optional = this.getOwningVehicle();
        if (optional.isEmpty()) {
            return;
        }
        DrivableVehicle drivableVehicle = optional.get().getAsDrivableVehicle();
        if (drivableVehicle == null) {
            return;
        }
        int n = 0;
        for (Part part : drivableVehicle.getParts()) {
            Part part2;
            if (part instanceof BikeSkin) {
                part2 = (BikeSkin)part;
                if (this.wheelie) {
                    if ((float)((BikeSkin)part2).getWheelieOffset() < drivableVehicle.getStatics().getCurrentSpeed() / 2.0f && ((BikeSkin)part2).getWheelieOffset() < 60) {
                        ((BikeSkin)part2).setWheelieOffset((int)((float)((BikeSkin)part2).getWheelieOffset() + drivableVehicle.getStatics().getCurrentSpeed() / 50.0f));
                    } else if (((BikeSkin)part2).getWheelieOffset() < 60) {
                        ((BikeSkin)part2).setWheelieOffset((int)(drivableVehicle.getStatics().getCurrentSpeed() / 2.0f));
                    } else {
                        ((BikeSkin)part2).setWheelieOffset(60);
                    }
                } else if (((BikeSkin)part2).getWheelieOffset() > 0) {
                    ((BikeSkin)part2).setWheelieOffset(((BikeSkin)part2).getWheelieOffset() - 2);
                } else if (((BikeSkin)part2).getWheelieOffset() < 0) {
                    ((BikeSkin)part2).setWheelieOffset(0);
                }
                ((BikeSkin)part2).applyWheelieOffset();
                n = ((BikeSkin)part2).getWheelieOffset();
                continue;
            }
            if (!(part instanceof BikeSeat)) continue;
            part2 = (BikeSeat)part;
            ((BikeSeat)part2).setExtraYOffset((float)((double)((float)n / 90.0f) * part2.getXOffset()));
        }
    }

    @Override
    public Location applyExtraOffset(Location location) {
        location.setY(location.getY() + (double)this.extraYOffset);
        Vector vector = location.clone().getDirection().multiply(-((double)this.extraYOffset / 2.0));
        location.add(vector.getX(), 0.0, vector.getZ());
        return location;
    }

    @Override
    public ItemStack getPartGUIItem() {
        return new ItemBuilder(XMaterial.DIAMOND_HOE).displayname(String.valueOf(ChatColor.GOLD) + "Bike Seat").lore(String.valueOf(ChatColor.GRAY) + "For players to sit on.", String.valueOf(ChatColor.GRAY) + "This part supports a wheelie.").unbreakable().durability(2).hideAllFlags().getItemStack();
    }

    @Override
    public void despawnStand() {
        super.despawnStand();
        this.wheelie = false;
        this.extraYOffset = 0.0f;
    }

    @Generated
    public boolean isWheelie() {
        return this.wheelie;
    }

    @Generated
    public float getExtraYOffset() {
        return this.extraYOffset;
    }

    @JsonIgnore
    @Generated
    public void setExtraYOffset(float f) {
        this.extraYOffset = f;
    }
}

