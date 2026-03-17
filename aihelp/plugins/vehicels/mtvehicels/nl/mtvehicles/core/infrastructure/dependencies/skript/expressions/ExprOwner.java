/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  ch.njol.skript.classes.Changer$ChangeMode
 *  ch.njol.skript.doc.Description
 *  ch.njol.skript.doc.Examples
 *  ch.njol.skript.doc.Name
 *  ch.njol.skript.doc.Since
 *  ch.njol.skript.expressions.base.SimplePropertyExpression
 *  org.bukkit.Bukkit
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.event.Event
 */
package nl.mtvehicles.core.infrastructure.dependencies.skript.expressions;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import java.util.UUID;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name(value="MTV Vehicle's owner")
@Description(value={"Get/Set the vehicle's owner (as OfflinePlayer)"})
@Examples(value={"set {_owner} to {_car}'s vehicle owner", "set {_owner} to vehicle owner of (mtv vehicle with license plate \"DF-4J-2R\")"})
@Since(value={"2.5.5"})
public class ExprOwner
extends SimplePropertyExpression<Vehicle, OfflinePlayer> {
    protected String getPropertyName() {
        return "[mtv] vehicle owner";
    }

    public Class<? extends OfflinePlayer> getReturnType() {
        return OfflinePlayer.class;
    }

    @Nullable
    public OfflinePlayer convert(Vehicle vehicle) {
        if (vehicle == null) {
            return null;
        }
        return Bukkit.getOfflinePlayer((UUID)vehicle.getOwnerUUID());
    }

    public @Nullable Class<?> @NotNull [] acceptChange(Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET) {
            return new Class[]{OfflinePlayer.class};
        }
        return null;
    }

    public void change(@NotNull Event event, @Nullable Object @NotNull [] delta, // Could not load outer class - annotation placement on inner may be incorrect
     @NotNull Changer.ChangeMode changeMode) {
        if (changeMode != Changer.ChangeMode.SET) {
            return;
        }
        Vehicle vehicle = (Vehicle)this.getExpr().getSingle(event);
        if (!Main.isNotNull(delta, delta[0], vehicle)) {
            return;
        }
        if (!(delta[0] instanceof OfflinePlayer)) {
            return;
        }
        OfflinePlayer newOwner = (OfflinePlayer)delta[0];
        vehicle.setOwner(newOwner.getUniqueId());
        vehicle.save();
    }

    static {
        ExprOwner.register(ExprOwner.class, OfflinePlayer.class, (String)"[mtv] vehicle owner", (String)"mtvehicles");
    }
}

