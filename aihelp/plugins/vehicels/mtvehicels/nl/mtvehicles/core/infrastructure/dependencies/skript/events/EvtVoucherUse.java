/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  ch.njol.skript.Skript
 *  ch.njol.skript.doc.Description
 *  ch.njol.skript.doc.Examples
 *  ch.njol.skript.doc.Name
 *  ch.njol.skript.lang.Literal
 *  ch.njol.skript.lang.SkriptEvent
 *  ch.njol.skript.lang.SkriptParser$ParseResult
 *  ch.njol.skript.registrations.EventValues
 *  ch.njol.skript.util.Getter
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 */
package nl.mtvehicles.core.infrastructure.dependencies.skript.events;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import nl.mtvehicles.core.events.VehicleVoucherEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name(value="Vehicle Voucher Use Event")
@Description(value={"Called when a voucher is right clicked"})
@Examples(value={"on vehicle voucher use:", "set {_player} to event-player", "set {_uuid} to event-text"})
public class EvtVoucherUse
extends SkriptEvent {
    public boolean init(Literal<?>[] args, int matchedPattern, SkriptParser.ParseResult parseResult) {
        return true;
    }

    public boolean check(Event e) {
        return true;
    }

    public String toString(@Nullable Event e, boolean debug) {
        return "Vehicle voucher event";
    }

    static {
        Skript.registerEvent((String)"VehicleVoucherEvent", EvtVoucherUse.class, VehicleVoucherEvent.class, (String[])new String[]{"[mtv] vehicle voucher use"});
        EventValues.registerEventValue(VehicleVoucherEvent.class, Player.class, (Getter)new Getter<Player, VehicleVoucherEvent>(){

            public Player get(VehicleVoucherEvent event) {
                return event.getPlayer();
            }
        }, (int)0);
        EventValues.registerEventValue(VehicleVoucherEvent.class, String.class, (Getter)new Getter<String, VehicleVoucherEvent>(){

            public String get(VehicleVoucherEvent event) {
                return event.getVoucherUUID();
            }
        }, (int)0);
    }
}

