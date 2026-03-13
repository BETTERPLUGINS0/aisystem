/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.entity.HumanEntity
 */
package io.papermc.lib.environments;

import io.papermc.lib.environments.SpigotEnvironment;
import io.papermc.lib.features.asyncchunks.AsyncChunksPaper_13;
import io.papermc.lib.features.asyncchunks.AsyncChunksPaper_15;
import io.papermc.lib.features.asyncchunks.AsyncChunksPaper_9_12;
import io.papermc.lib.features.asyncteleport.AsyncTeleportPaper;
import io.papermc.lib.features.asyncteleport.AsyncTeleportPaper_13;
import io.papermc.lib.features.bedspawnlocation.BedSpawnLocationPaper;
import io.papermc.lib.features.blockstatesnapshot.BlockStateSnapshotOptionalSnapshots;
import io.papermc.lib.features.chunkisgenerated.ChunkIsGeneratedApiExists;
import io.papermc.lib.features.inventoryholdersnapshot.InventoryHolderSnapshotOptionalSnapshots;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.HumanEntity;

public class PaperEnvironment
extends SpigotEnvironment {
    public PaperEnvironment() {
        if (this.isVersion(13, 1)) {
            this.asyncChunksHandler = new AsyncChunksPaper_13();
            this.asyncTeleportHandler = new AsyncTeleportPaper_13();
        } else if (this.isVersion(9) && !this.isVersion(13)) {
            this.asyncChunksHandler = new AsyncChunksPaper_9_12();
            this.asyncTeleportHandler = new AsyncTeleportPaper();
        }
        if (this.isVersion(12)) {
            this.isGeneratedHandler = new ChunkIsGeneratedApiExists();
            this.blockStateSnapshotHandler = new BlockStateSnapshotOptionalSnapshots();
        }
        if (this.isVersion(15, 2)) {
            try {
                World.class.getDeclaredMethod("getChunkAtAsyncUrgently", Location.class);
                this.asyncChunksHandler = new AsyncChunksPaper_15();
                HumanEntity.class.getDeclaredMethod("getPotentialBedLocation", new Class[0]);
                this.bedSpawnLocationHandler = new BedSpawnLocationPaper();
            } catch (NoSuchMethodException noSuchMethodException) {
                // empty catch block
            }
        }
        if (this.isVersion(16)) {
            this.inventoryHolderSnapshotHandler = new InventoryHolderSnapshotOptionalSnapshots();
        }
    }

    @Override
    public String getName() {
        return "Paper";
    }

    @Override
    public boolean isPaper() {
        return true;
    }
}

