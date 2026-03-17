/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.ChatColor
 *  net.md_5.bungee.api.ChatMessageType
 *  net.md_5.bungee.api.chat.TextComponent
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.entity.ArmorStand
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.util.EulerAngle
 *  org.bukkit.util.Vector
 */
package nl.sbdeveloper.vehiclesplus.handlers;

import java.util.ArrayList;
import java.util.Optional;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import nl.sbdeveloper.vehiclesplus.VehiclesPlus;
import nl.sbdeveloper.vehiclesplus.api.VehiclesPlusAPI;
import nl.sbdeveloper.vehiclesplus.api.events.impl.KeyPressEvent;
import nl.sbdeveloper.vehiclesplus.api.stands.ArmorStandName;
import nl.sbdeveloper.vehiclesplus.api.vehicles.collision.CollisionHandler;
import nl.sbdeveloper.vehiclesplus.api.vehicles.collision.HitboxSide;
import nl.sbdeveloper.vehiclesplus.api.vehicles.impl.DrivableVehicle;
import nl.sbdeveloper.vehiclesplus.api.vehicles.impl.SpawnedVehicle;
import nl.sbdeveloper.vehiclesplus.api.vehicles.movement.MovementInput;
import nl.sbdeveloper.vehiclesplus.api.vehicles.parts.Part;
import nl.sbdeveloper.vehiclesplus.api.vehicles.parts.impl.Wheel;
import nl.sbdeveloper.vehiclesplus.api.vehicles.parts.impl.seat.Controllable;
import nl.sbdeveloper.vehiclesplus.api.vehicles.parts.impl.seat.Seat;
import nl.sbdeveloper.vehiclesplus.api.vehicles.parts.impl.skin.Rotor;
import nl.sbdeveloper.vehiclesplus.api.vehicles.parts.impl.skin.Skin;
import nl.sbdeveloper.vehiclesplus.api.vehicles.settings.impl.Sounds;
import nl.sbdeveloper.vehiclesplus.api.vehicles.statics.strategies.AirMovementStrategy;
import nl.sbdeveloper.vehiclesplus.api.vehicles.types.FrictionType;
import nl.sbdeveloper.vehiclesplus.api.vehicles.types.MovementType;
import nl.sbdeveloper.vehiclesplus.api.vehicles.types.TiltType;
import nl.sbdeveloper.vehiclesplus.handlers.WGFlagHandler;
import nl.sbdeveloper.vehiclesplus.locale.Locale;
import nl.sbdeveloper.vehiclesplus.locale.PluginMessage;
import nl.sbdeveloper.vehiclesplus.utils.MainUtil;
import nl.sbdeveloper.vehiclesplus.utils.nms.MovementUtil;
import nl.sbdeveloper.vehiclesplus.utils.wg.WorldGuardHelper;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

public class MovementHandler
implements Listener {
    @EventHandler
    public void onSendKey(KeyPressEvent keyPressEvent) {
        int n;
        double d;
        Integer n2;
        Entity entity = keyPressEvent.getPlayer().getVehicle();
        if (!(entity instanceof ArmorStand)) {
            return;
        }
        ArmorStand armorStand = (ArmorStand)entity;
        if (armorStand.getCustomName() == null || !armorStand.getCustomName().contains(ArmorStandName.VP_PART.name())) {
            return;
        }
        Optional<SpawnedVehicle> optional = VehiclesPlusAPI.getVehicleFromPart(armorStand);
        if (optional.isEmpty()) {
            return;
        }
        DrivableVehicle drivableVehicle = optional.get().getAsDrivableVehicle();
        if (drivableVehicle == null) {
            return;
        }
        if (drivableVehicle.getStatics().isBroken()) {
            return;
        }
        Part part = drivableVehicle.getPart(armorStand);
        if (!(part instanceof Seat)) {
            return;
        }
        Seat seat = (Seat)part;
        MovementInput movementInput = keyPressEvent.getInput();
        if (seat instanceof Controllable) {
            ((Controllable)((Object)seat)).handleInput(movementInput);
        }
        if (!seat.isSteer()) {
            return;
        }
        Vector vector = new Vector(0, 0, 0);
        float f = drivableVehicle.getStatics().getAcceleration() / 50.0f;
        float f2 = drivableVehicle.getStatics().getBrakeForce();
        int n3 = drivableVehicle.getStatics().getMaxSpeed();
        float f3 = drivableVehicle.getStatics().getTurningRadius();
        float f4 = drivableVehicle.getStatics().getCurrentSpeed();
        float f5 = drivableVehicle.getStatics().getCurrentSteering();
        int n4 = drivableVehicle.getFromStrategy(MovementType.AIR, AirMovementStrategy::getLift, 0);
        Material material = drivableVehicle.getHolder().getLocation().clone().add(0.0, -1.0, 0.0).getBlock().getType();
        boolean bl = false;
        boolean bl2 = false;
        boolean bl3 = false;
        boolean bl4 = false;
        boolean bl5 = false;
        if (drivableVehicle.getVehicleModel().getType().hasMovementType(MovementType.LAND)) {
            bl5 = true;
        }
        if (drivableVehicle.getVehicleModel().getType().hasMovementType(MovementType.AIR)) {
            bl3 = true;
            bl = material == Material.AIR;
        } else if (drivableVehicle.getVehicleModel().getType().hasMovementType(MovementType.WATER)) {
            bl4 = true;
            bl2 = material.name().contains("WATER");
        }
        if (WorldGuardHelper.hasWorldGuard()) {
            n2 = WGFlagHandler.getRegionMaxSpeed(keyPressEvent.getPlayer(), drivableVehicle.getHolder().getLocation(), drivableVehicle.getVehicleModel().getType());
            drivableVehicle.getStatics().setTempMaxSpeedOverride(n2);
        }
        if (bl || bl2 || bl5) {
            HitboxSide hitboxSide;
            CollisionHandler collisionHandler;
            int n5 = 0;
            if (movementInput.isW() || movementInput.isS()) {
                n5 = drivableVehicle.getVehicleModel().getGearbox().handleGearbox(movementInput, drivableVehicle.getStatics().getCurrentSpeed());
            }
            if ((collisionHandler = drivableVehicle.getCollisionHandler()) != null && collisionHandler.isBlocked() && !drivableVehicle.getVehicleModel().getType().hasMovementType(MovementType.AIR)) {
                hitboxSide = collisionHandler.getBlockedSide();
                if (hitboxSide == HitboxSide.FRONT) {
                    if (movementInput.isS()) {
                        n5 = -1;
                        collisionHandler.clearBlock();
                    } else if (n5 > 0) {
                        n5 = 0;
                    }
                } else if (hitboxSide == HitboxSide.BACK) {
                    if (movementInput.isW()) {
                        n5 = 1;
                        collisionHandler.clearBlock();
                    } else if (n5 < 0) {
                        n5 = 0;
                    }
                }
            }
            if (n5 > 0 && drivableVehicle.getStatics().getCurrentFuel() > 0.0) {
                if (f4 >= 0.0f) {
                    if (f4 + f >= (float)n3) {
                        this.playSound(drivableVehicle, drivableVehicle.getVehicleModel().getSounds().getDriving());
                        f4 = n3;
                    } else {
                        this.playSound(drivableVehicle, drivableVehicle.getVehicleModel().getSounds().getAccelerate());
                        f4 += f;
                    }
                } else {
                    this.playSound(drivableVehicle, drivableVehicle.getVehicleModel().getSounds().getSlowingDown());
                    f4 = f4 + f2 >= (float)n3 / 4.0f ? -((float)n3 / 4.0f) : (f4 += f2);
                }
            } else if (n5 < 0 && drivableVehicle.getStatics().getCurrentFuel() > 0.0) {
                if (f4 <= 0.0f) {
                    if (f4 - f <= -((float)n3 / 4.0f)) {
                        this.playSound(drivableVehicle, drivableVehicle.getVehicleModel().getSounds().getDriving());
                        f4 = (float)(-n3) / 4.0f;
                    } else {
                        this.playSound(drivableVehicle, drivableVehicle.getVehicleModel().getSounds().getAccelerate());
                        f4 -= f;
                    }
                } else {
                    this.playSound(drivableVehicle, drivableVehicle.getVehicleModel().getSounds().getSlowingDown());
                    f4 = f4 - f2 <= -((float)n3 / 4.0f) ? -((float)n3 / 4.0f) : (f4 -= f2);
                }
            } else {
                this.playSound(drivableVehicle, drivableVehicle.getVehicleModel().getSounds().getIdle());
                if (f4 > 0.0f) {
                    f4 = f4 - f / 2.0f <= 0.0f ? 0.0f : (f4 -= f / 2.0f);
                } else if (f4 < 0.0f) {
                    f4 = f4 + f / 2.0f >= 0.0f ? 0.0f : (f4 += f / 2.0f);
                }
            }
            hitboxSide = drivableVehicle.getHolder().getLocation().clone().getDirection().normalize().multiply(f4 / 40.0f).setY(-1);
            vector.add((Vector)hitboxSide);
        }
        if (drivableVehicle.getVehicleModel().isRealisticSteering()) {
            if (movementInput.isD()) {
                f5 = f5 + 1.0f >= f3 ? f3 : (f5 += 1.0f);
            } else if (movementInput.isA()) {
                f5 = f5 - 1.0f <= -f3 ? -f3 : (f5 -= 1.0f);
            } else if (f5 > 0.0f) {
                f5 = f5 - 1.0f <= 0.0f ? 0.0f : (f5 -= 1.0f);
            } else if (f5 < 0.0f) {
                f5 = f5 + 1.0f >= 0.0f ? 0.0f : (f5 += 1.0f);
            }
        } else {
            f5 = movementInput.isD() ? f3 : (movementInput.isA() ? -f3 : 0.0f);
        }
        drivableVehicle.getStatics().setCurrentSteering(f5);
        drivableVehicle.getParts(Wheel.class, Wheel::isSteering).forEach(wheel -> wheel.setSteeringOffset(drivableVehicle.getStatics().getCurrentSteering() * 5.0f));
        if (bl4) {
            if (!bl2 && !drivableVehicle.getHolder().getLocation().getBlock().getType().name().contains("WATER")) {
                vector.setY(-0.5);
            } else {
                vector.setY(0);
            }
        }
        if (!drivableVehicle.getMomentum().isEmpty()) {
            n2 = drivableVehicle.getMomentum().get(0);
            drivableVehicle.getMomentum().remove(0);
        } else {
            n2 = new Vector(0, 0, 0);
        }
        if (WorldGuardHelper.hasWorldGuard() && !WGFlagHandler.allowsVehicleDriving(keyPressEvent.getPlayer(), drivableVehicle.getHolder().getLocation())) {
            vector = new Vector(0, 0, 0);
            f4 = 0.0f;
            f5 = 0.0f;
        }
        boolean bl6 = false;
        if (drivableVehicle.getVehicleModel().getHeightLimit() != null) {
            double d2 = drivableVehicle.getVehicleModel().getHeightLimit().getMinHeight();
            d = drivableVehicle.getVehicleModel().getHeightLimit().getMaxHeight();
            if (drivableVehicle.getHolder().getLocation().getY() < d2) {
                vector.setY(0.5);
                bl6 = true;
                keyPressEvent.getPlayer().sendMessage(String.valueOf(ChatColor.RED) + "You can't go lower than " + d2 + "!");
            } else if (drivableVehicle.getHolder().getLocation().getY() > d) {
                vector.setY(-0.5);
                bl6 = true;
                keyPressEvent.getPlayer().sendMessage(String.valueOf(ChatColor.RED) + "You can't go higher than " + d + "!");
            }
        }
        if (!bl6) {
            if (bl5 && bl3) {
                float f6 = drivableVehicle.getFromStrategy(MovementType.AIR, AirMovementStrategy::getLiftoffSpeed, Float.valueOf((float)n3 / 2.0f)).floatValue();
                if (f4 >= f6) {
                    if (movementInput.isW() && movementInput.isSpace() && drivableVehicle.getStatics().getCurrentFuel() > 0.0) {
                        if (n4 < 20) {
                            ++n4;
                        }
                    } else if (!movementInput.isW() && !movementInput.isSpace()) {
                        if (n4 > 0) {
                            --n4;
                        }
                    } else if (movementInput.isS()) {
                        if ((n4 -= 3) < 0) {
                            n4 = 0;
                        }
                    } else if (drivableVehicle.getStatics().getCurrentFuel() <= 0.0) {
                        n4 = Math.max(0, n4 - 2);
                        vector.setY(-0.5);
                    } else {
                        n4 = 10;
                    }
                } else if ((n4 -= 3) < 0) {
                    n4 = 0;
                }
                if (drivableVehicle.getStatics().getCurrentFuel() > 0.0) {
                    vector.setY(-1 + n4 / 10);
                }
            } else if (!bl5 && bl3) {
                if (movementInput.isW() && movementInput.isSpace() && drivableVehicle.getStatics().getCurrentFuel() > 0.0) {
                    f4 = 0.0f;
                    if (n4 < 20) {
                        ++n4;
                    }
                } else if (movementInput.isS() && movementInput.isSpace() && drivableVehicle.getStatics().getCurrentFuel() > 0.0) {
                    f4 = 0.0f;
                    if (n4 > 0) {
                        --n4;
                    }
                } else if (drivableVehicle.getStatics().getCurrentFuel() <= 0.0) {
                    n4 = Math.max(0, n4 - 2);
                    f4 = 0.0f;
                    vector.setY(-1.0);
                } else if (n4 > 10) {
                    --n4;
                } else if (n4 < 10) {
                    ++n4;
                }
                if (drivableVehicle.getStatics().getCurrentFuel() > 0.0) {
                    vector.setY(-0.5 + (double)n4 / 20.0);
                }
            }
            int n6 = n4;
            drivableVehicle.applyToStrategy(MovementType.AIR, movementStrategy -> ((AirMovementStrategy)movementStrategy).setLift(n6));
        }
        drivableVehicle.getStatics().setCurrentSpeed(f4);
        if (n2.getX() == 0.0 && n2.getY() == 0.0 && n2.getZ() == 0.0) {
            drivableVehicle.getHolder().setVelocity(new Vector(vector.getX() + n2.getX(), vector.getY() + n2.getY(), vector.getZ() + n2.getZ()));
        } else {
            drivableVehicle.getHolder().setVelocity(new Vector(vector.getX() / 2.0 + n2.getX() / 2.0, vector.getY() / 2.0 + n2.getY() / 2.0, vector.getZ() / 2.0 + n2.getZ() / 2.0));
        }
        Location location = drivableVehicle.getHolder().getLocation().clone();
        location.setYaw(location.getYaw() + (f4 > 0.0f ? f5 / 2.0f : -f5 / 2.0f));
        if (f4 != 0.0f) {
            MovementUtil.setPosition(drivableVehicle.getHolder(), location);
        }
        drivableVehicle.update();
        EulerAngle eulerAngle = MovementHandler.computeVehicleTilt(drivableVehicle, f4, n4, f5);
        d = eulerAngle.getX();
        double d3 = eulerAngle.getZ();
        double d4 = (double)Math.max(5, n4) * 0.015;
        float f7 = drivableVehicle.getHolder().getLocation().getYaw();
        drivableVehicle.getParts(Rotor.class).forEach(rotor -> {
            double d4 = 0.0;
            try {
                d4 = rotor.getRotationOffset();
            } catch (Throwable throwable) {
                // empty catch block
            }
            Location location = rotor.getHolder().getLocation().clone();
            location.setYaw(f7 + (float)d4);
            MovementUtil.setPosition(rotor.getHolder(), location);
            rotor.addSpin(d4);
            double d5 = rotor.getSpin();
            Quaternion quaternion = Quaternion.rx(d);
            Quaternion quaternion2 = Quaternion.rz(d3);
            Quaternion quaternion3 = Quaternion.mul(quaternion, quaternion2);
            Vec3 vec3 = Quaternion.rotate(quaternion3, new Vec3(0.0, 1.0, 0.0));
            Quaternion quaternion4 = Quaternion.axis(vec3.x, vec3.y, vec3.z, d5);
            Quaternion quaternion5 = Quaternion.mul(quaternion4, quaternion3);
            EulerAngle eulerAngle = quaternion5.toEulerXYZ();
            switch (rotor.getPosition()) {
                case HEAD: {
                    rotor.getHolder().setHeadPose(eulerAngle);
                    break;
                }
                case MAIN_HAND: {
                    rotor.getHolder().setRightArmPose(eulerAngle);
                    break;
                }
                case OFF_HAND: {
                    rotor.getHolder().setLeftArmPose(eulerAngle);
                }
            }
        });
        if (movementInput.isSpace()) {
            drivableVehicle.getVehicleModel().getHorn().horn(drivableVehicle.getHolder().getLocation());
        }
        if (movementInput.isW() || movementInput.isS()) {
            if (drivableVehicle.getVehicleModel().getType().getFrictionType() == FrictionType.MEDIUM_FRICTION) {
                for (n = 0; n < 4; ++n) {
                    drivableVehicle.getMomentum().add(vector.clone().multiply(1 / (n + 1)));
                }
            } else if (drivableVehicle.getVehicleModel().getType().getFrictionType() == FrictionType.LOW_FRICTION || drivableVehicle.getVehicleModel().isDrift() && movementInput.isSpace()) {
                for (n = 0; n < 8; ++n) {
                    drivableVehicle.getMomentum().add(vector.clone().multiply(1 / (n + 1)));
                }
            }
        }
        if (drivableVehicle.getVehicleModel().getType().getFrictionType() == FrictionType.LOW_FRICTION || drivableVehicle.getVehicleModel().isDrift() && movementInput.isSpace()) {
            if (drivableVehicle.getMomentum().size() > 40) {
                for (n = 20; n < drivableVehicle.getMomentum().size(); ++n) {
                    drivableVehicle.getMomentum().remove(n);
                }
            }
        } else if (drivableVehicle.getVehicleModel().getType().getFrictionType() == FrictionType.MEDIUM_FRICTION && drivableVehicle.getMomentum().size() > 20) {
            for (n = 10; n < drivableVehicle.getMomentum().size(); ++n) {
                drivableVehicle.getMomentum().remove(n);
            }
        }
        Vector vector2 = new Vector(0, 0, 0);
        if (drivableVehicle.getVehicleModel().getType().hasTiltType(TiltType.STEERING) && f4 != 0.0f) {
            vector2.setY(f5);
        }
        if (drivableVehicle.getVehicleModel().getType().hasTiltType(TiltType.ASCEND_DESCENT) && n4 > 10) {
            vector2.setZ(-(n4 - 10) * 6);
        }
        if (drivableVehicle.getVehicleModel().getType().hasTiltType(TiltType.FORWARD_BACKWARD)) {
            vector2.setZ(f4 / 2.0f);
        }
        EulerAngle eulerAngle2 = MovementHandler.computeVehicleTilt(drivableVehicle, f4, n4, f5);
        for (Skin part2 : drivableVehicle.getParts(Skin.class)) {
            switch (part2.getPosition()) {
                case HEAD: {
                    part2.getHolder().setHeadPose(eulerAngle2);
                    break;
                }
                case MAIN_HAND: {
                    part2.getHolder().setRightArmPose(eulerAngle2);
                    break;
                }
                case OFF_HAND: {
                    part2.getHolder().setLeftArmPose(eulerAngle2);
                }
            }
        }
        for (Seat seat2 : drivableVehicle.getParts(Seat.class)) {
            Optional<Player> optional2 = seat2.getPassenger();
            if (!optional2.isPresent()) continue;
            Player player = optional2.get();
            if (!VehiclesPlus.getSmoothCoasters().isEnabled(player)) continue;
            double d2 = Math.toRadians(location.getYaw()) + eulerAngle2.getY();
            double d5 = Math.toRadians(location.getPitch()) + eulerAngle2.getX();
            double d6 = -eulerAngle2.getZ();
            nl.sbdeveloper.vehiclesplus.utils.math.Quaternion quaternion = new nl.sbdeveloper.vehiclesplus.utils.math.Quaternion(d2, d5, d6);
            VehiclesPlus.getSmoothCoasters().setRotation(null, player, (float)quaternion.getX(), (float)quaternion.getY(), (float)quaternion.getZ(), (float)quaternion.getW(), (byte)3);
        }
        if (drivableVehicle.getStatics().getCurrentFuel() <= 0.0) {
            drivableVehicle.getStatics().setCurrentFuel(0.0);
            seat.getPassenger().get().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText((String)Locale.getMessage(PluginMessage.GENERAL_VEHICLES_ACTIONBAR_OUTOFFUEL)));
        } else {
            int n7 = Math.abs((int)(drivableVehicle.getVehicleModel().getFuel().getUsage() * ((double)drivableVehicle.getStatics().getCurrentSpeedKMPH() / (double)drivableVehicle.getVehicleModel().getMaxSpeed().getBase().intValue())));
            drivableVehicle.getStatics().setCurrentFuel(drivableVehicle.getStatics().getCurrentFuel() - (double)n7 / 1000.0);
            double d7 = drivableVehicle.getStatics().getCurrentFuel();
            int n5 = drivableVehicle.getStatics().getFuelTank();
            if (VehiclesPlus.getStorage().getConfig().isActionBar()) {
                String string = drivableVehicle.getStorageVehicle().getActionBarMessage().replace("%healthperc%", String.valueOf(drivableVehicle.getStatics().getCurrentHealth() / drivableVehicle.getVehicleModel().getHealth() * 100)).replace("%curhealth%", String.valueOf(drivableVehicle.getStatics().getCurrentHealth())).replace("%maxhealth%", String.valueOf(drivableVehicle.getVehicleModel().getHealth())).replace("%curspeed%", String.valueOf(drivableVehicle.getStatics().getCurrentSpeedKMPH())).replace("%maxspeed%", String.valueOf(drivableVehicle.getStatics().getMaxSpeed())).replace("%curfuel%", String.valueOf(Math.round(d7))).replace("%maxfuel%", String.valueOf(n5)).replace("%fuelperc%", String.valueOf((int)(d7 / (double)n5 * 100.0))).replace("%fuelusage%", String.valueOf(n7)).replace("%gear%", MainUtil.capitalize(drivableVehicle.getVehicleModel().getGearbox().getCurrentGear().name().toLowerCase())).replace("%gearshort%", drivableVehicle.getVehicleModel().getGearbox().getCurrentGear().name().substring(0, 1));
                seat.getPassenger().get().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText((String)ChatColor.translateAlternateColorCodes((char)'&', (String)string)));
            }
        }
        drivableVehicle.getVehicleModel().getExhaust().spawnParticle(drivableVehicle.getHolder().getLocation());
    }

    private void playSound(DrivableVehicle drivableVehicle, Sounds.Sound sound) {
        ArrayList arrayList = new ArrayList();
        drivableVehicle.getParts(Seat.class).forEach(seat -> {
            if (seat.isOccupied()) {
                arrayList.add(seat.getPassenger().get());
            }
        });
        sound.playSound(drivableVehicle, (Player)arrayList.get(0));
    }

    private static EulerAngle computeVehicleTilt(DrivableVehicle drivableVehicle, float f, int n, float f2) {
        double d;
        double d2 = 0.0;
        double d3 = 0.0;
        double d4 = Math.max(1.0, drivableVehicle.getVehicleModel().getMaxSpeed().getBase().doubleValue());
        double d5 = Math.min(1.0, (double)Math.abs(f) / d4);
        if (drivableVehicle.getVehicleModel().getType().hasTiltType(TiltType.FORWARD_BACKWARD)) {
            if ((double)f > 0.0) {
                d2 = 20.0 * d5;
            } else if ((double)f < 0.0) {
                d2 = -15.0 * d5;
            }
        }
        if (drivableVehicle.getVehicleModel().getType().hasTiltType(TiltType.ASCEND_DESCENT)) {
            d = n - 10;
            d2 += Math.max(-5.0, Math.min(5.0, d * 0.3));
        }
        if (drivableVehicle.getVehicleModel().getType().hasTiltType(TiltType.STEERING) && f != 0.0f) {
            d = Math.min(1.0, (double)(Math.abs(f2) / Math.max(1.0f, drivableVehicle.getStatics().getTurningRadius())));
            d3 = Math.copySign(15.0 * d * d5, (double)(-f2));
        }
        d2 = Math.max(-20.0, Math.min(15.0, d2));
        d3 = Math.max(-15.0, Math.min(15.0, d3));
        return new EulerAngle(Math.toRadians(d2), 0.0, Math.toRadians(d3));
    }

    static final class Quaternion {
        final double x;
        final double y;
        final double z;
        final double w;

        Quaternion(double d, double d2, double d3, double d4) {
            this.x = d;
            this.y = d2;
            this.z = d3;
            this.w = d4;
        }

        static Quaternion axis(double d, double d2, double d3, double d4) {
            double d5 = Math.sqrt(d * d + d2 * d2 + d3 * d3);
            if (d5 == 0.0) {
                return new Quaternion(0.0, 0.0, 0.0, 1.0);
            }
            double d6 = Math.sin(d4 / 2.0) / d5;
            return new Quaternion(d * d6, d2 * d6, d3 * d6, Math.cos(d4 / 2.0));
        }

        static Quaternion rx(double d) {
            return Quaternion.axis(1.0, 0.0, 0.0, d);
        }

        static Quaternion ry(double d) {
            return Quaternion.axis(0.0, 1.0, 0.0, d);
        }

        static Quaternion rz(double d) {
            return Quaternion.axis(0.0, 0.0, 1.0, d);
        }

        static Quaternion mul(Quaternion quaternion, Quaternion quaternion2) {
            return new Quaternion(quaternion.w * quaternion2.x + quaternion.x * quaternion2.w + quaternion.y * quaternion2.z - quaternion.z * quaternion2.y, quaternion.w * quaternion2.y - quaternion.x * quaternion2.z + quaternion.y * quaternion2.w + quaternion.z * quaternion2.x, quaternion.w * quaternion2.z + quaternion.x * quaternion2.y - quaternion.y * quaternion2.x + quaternion.z * quaternion2.w, quaternion.w * quaternion2.w - quaternion.x * quaternion2.x - quaternion.y * quaternion2.y - quaternion.z * quaternion2.z);
        }

        static Vec3 rotate(Quaternion quaternion, Vec3 vec3) {
            Quaternion quaternion2 = new Quaternion(vec3.x, vec3.y, vec3.z, 0.0);
            Quaternion quaternion3 = new Quaternion(-quaternion.x, -quaternion.y, -quaternion.z, quaternion.w);
            Quaternion quaternion4 = Quaternion.mul(Quaternion.mul(quaternion, quaternion2), quaternion3);
            return new Vec3(quaternion4.x, quaternion4.y, quaternion4.z);
        }

        EulerAngle toEulerXYZ() {
            double d = Math.sqrt(this.w * this.w + this.x * this.x + this.y * this.y + this.z * this.z);
            if (d < 1.0E-10) {
                return new EulerAngle(0.0, 0.0, 0.0);
            }
            double d2 = this.w / d;
            double d3 = this.x / d;
            double d4 = this.y / d;
            double d5 = this.z / d;
            double d6 = 2.0 * (d2 * d3 + d4 * d5);
            double d7 = 1.0 - 2.0 * (d3 * d3 + d4 * d4);
            double d8 = Math.atan2(d6, d7);
            double d9 = 2.0 * (d2 * d4 - d5 * d3);
            double d10 = Math.abs(d9) >= 1.0 ? Math.copySign(1.5707963267948966, d9) : Math.asin(d9);
            double d11 = 2.0 * (d2 * d5 + d3 * d4);
            double d12 = 1.0 - 2.0 * (d4 * d4 + d5 * d5);
            double d13 = Math.atan2(d11, d12);
            return new EulerAngle(d8, d10, d13);
        }
    }

    static final class Vec3 {
        final double x;
        final double y;
        final double z;

        Vec3(double d, double d2, double d3) {
            this.x = d;
            this.y = d2;
            this.z = d3;
        }
    }
}

