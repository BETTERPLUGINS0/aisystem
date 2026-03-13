/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 */
package com.andrei1058.bedwars.maprestore.internal.files;

import com.andrei1058.bedwars.api.util.ZipFileUtil;
import com.andrei1058.bedwars.maprestore.internal.InternalAdapter;
import java.io.File;
import java.io.IOException;
import org.bukkit.Bukkit;

public class WorldZipper {
    private final String worldName;
    private boolean replace;

    public WorldZipper(String worldName, boolean replace) {
        this.worldName = worldName;
        this.replace = replace;
        this.execute();
    }

    private void execute() {
        if (!this.exists() || this.replace) {
            try {
                this.zipWorldFolder();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void zipWorldFolder() throws IOException {
        File worldFolder = this.getWorldFolder();
        File backupFile = this.getBackupFile();
        ZipFileUtil.zipDirectory(worldFolder, backupFile);
    }

    private File getWorldFolder() {
        File worldContainer = Bukkit.getWorldContainer();
        return new File(worldContainer, this.worldName);
    }

    private File getBackupFile() {
        File backupFolder = InternalAdapter.backupFolder;
        return new File(backupFolder, this.worldName + ".zip");
    }

    private boolean exists() {
        File worldFolder = this.getWorldFolder();
        return worldFolder.isDirectory();
    }
}

