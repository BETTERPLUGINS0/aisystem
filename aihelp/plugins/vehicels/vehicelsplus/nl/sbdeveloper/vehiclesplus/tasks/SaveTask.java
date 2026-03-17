/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.scheduler.BukkitRunnable
 */
package nl.sbdeveloper.vehiclesplus.tasks;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import nl.sbdeveloper.vehiclesplus.VehiclesPlus;
import nl.sbdeveloper.vehiclesplus.storage.db.QueuedSavable;
import nl.sbdeveloper.vehiclesplus.storage.db.exceptions.DataStorageException;
import org.bukkit.scheduler.BukkitRunnable;

public class SaveTask
extends BukkitRunnable {
    private static final Set<QueuedSavable> queue = new HashSet<QueuedSavable>();

    public static void addToQueue(QueuedSavable ... queuedSavableArray) {
        Arrays.stream(queuedSavableArray).filter(queuedSavable -> queue.stream().noneMatch(queuedSavable2 -> queuedSavable.getSaveIdentifier().equals(queuedSavable2.getSaveIdentifier()))).forEach(queue::add);
    }

    public void run() {
        queue.forEach(queuedSavable -> {
            try {
                queuedSavable.forceSave();
            } catch (DataStorageException dataStorageException) {
                VehiclesPlus.getInstance().getLogger().log(Level.SEVERE, queuedSavable.getSaveError(), dataStorageException);
            }
        });
        queue.clear();
    }
}

