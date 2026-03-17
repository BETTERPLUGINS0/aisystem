/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.ArmorStand
 *  org.bukkit.entity.Entity
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.world.ChunkUnloadEvent
 */
package nl.sbdeveloper.vehiclesplus.listeners;

import java.util.Optional;
import nl.sbdeveloper.vehiclesplus.api.VehiclesPlusAPI;
import nl.sbdeveloper.vehiclesplus.api.events.impl.VehicleDespawnEvent;
import nl.sbdeveloper.vehiclesplus.api.vehicles.impl.SpawnedVehicle;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;

public class ChunkListener
implements Listener {
    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent chunkUnloadEvent) {
        for (Entity entity : chunkUnloadEvent.getChunk().getEntities()) {
            Optional<SpawnedVehicle> optional;
            ArmorStand armorStand;
            if (!(entity instanceof ArmorStand) || (armorStand = (ArmorStand)entity).getCustomName() == null || (optional = VehiclesPlusAPI.getVehicleFromPart(armorStand)).isEmpty()) continue;
            optional.get().despawn(VehicleDespawnEvent.DespawnReason.CHUNK_UNLOAD, true);
        }
    }
}

