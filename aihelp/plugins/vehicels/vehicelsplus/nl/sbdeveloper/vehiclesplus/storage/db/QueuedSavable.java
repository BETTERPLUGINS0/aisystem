/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.sbdeveloper.vehiclesplus.storage.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import nl.sbdeveloper.vehiclesplus.storage.db.Savable;
import nl.sbdeveloper.vehiclesplus.storage.db.exceptions.DataStorageException;
import nl.sbdeveloper.vehiclesplus.tasks.SaveTask;

public interface QueuedSavable
extends Savable {
    @Override
    default public void save() {
        SaveTask.addToQueue(this);
    }

    public void forceSave() throws DataStorageException;

    @JsonIgnore
    public String getSaveIdentifier();

    @JsonIgnore
    public String getSaveError();
}

