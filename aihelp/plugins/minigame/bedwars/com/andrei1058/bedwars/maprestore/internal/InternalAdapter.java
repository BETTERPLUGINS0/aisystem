/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.World
 *  org.bukkit.WorldCreator
 *  org.bukkit.command.CommandSender
 *  org.bukkit.generator.ChunkGenerator
 *  org.bukkit.plugin.Plugin
 */
package com.andrei1058.bedwars.maprestore.internal;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.server.ISetupSession;
import com.andrei1058.bedwars.api.server.RestoreAdapter;
import com.andrei1058.bedwars.api.server.ServerType;
import com.andrei1058.bedwars.api.util.FileUtil;
import com.andrei1058.bedwars.api.util.ZipFileUtil;
import com.andrei1058.bedwars.arena.Arena;
import com.andrei1058.bedwars.arena.VoidChunkGenerator;
import com.andrei1058.bedwars.maprestore.internal.files.WorldZipper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.CommandSender;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.Plugin;

public class InternalAdapter
extends RestoreAdapter {
    public static File backupFolder = new File(String.valueOf(BedWars.plugin.getDataFolder()) + "/Cache");

    public InternalAdapter(Plugin plugin) {
        super(plugin);
    }

    @Override
    public void onEnable(IArena a) {
        Bukkit.getScheduler().runTask(this.getOwner(), () -> {
            if (Bukkit.getWorld((String)a.getWorldName()) != null) {
                Bukkit.getScheduler().runTask(this.getOwner(), () -> {
                    World w = Bukkit.getWorld((String)a.getWorldName());
                    a.init(w);
                });
                return;
            }
            Bukkit.getScheduler().runTaskAsynchronously((Plugin)BedWars.plugin, () -> {
                File bf = new File(backupFolder, a.getArenaName() + ".zip");
                File af = new File(Bukkit.getWorldContainer(), a.getArenaName());
                if (bf.exists()) {
                    FileUtil.delete(af);
                }
                if (!bf.exists()) {
                    new WorldZipper(a.getArenaName(), true);
                } else {
                    try {
                        ZipFileUtil.unzipFileIntoDirectory(bf, new File(Bukkit.getWorldContainer(), a.getWorldName()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                this.deleteWorldTrash(a.getWorldName());
                Bukkit.getScheduler().runTask((Plugin)BedWars.plugin, () -> {
                    WorldCreator wc = new WorldCreator(a.getWorldName());
                    wc.generateStructures(false);
                    wc.generator((ChunkGenerator)new VoidChunkGenerator());
                    World w = Bukkit.createWorld((WorldCreator)wc);
                    if (w == null) {
                        throw new IllegalStateException("World should be null");
                    }
                    w.setKeepSpawnInMemory(true);
                    w.setAutoSave(false);
                });
            });
        });
    }

    @Override
    public void onRestart(IArena a) {
        Bukkit.getScheduler().runTask(this.getOwner(), () -> {
            if (BedWars.getServerType() == ServerType.BUNGEE) {
                if (Arena.getGamesBeforeRestart() == 0) {
                    if (Arena.getArenas().isEmpty()) {
                        BedWars.plugin.getLogger().info("Dispatching command: " + BedWars.config.getString("bungee-settings.restart-cmd"));
                        Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), (String)BedWars.config.getString("bungee-settings.restart-cmd"));
                    }
                } else {
                    if (Arena.getGamesBeforeRestart() != -1) {
                        Arena.setGamesBeforeRestart(Arena.getGamesBeforeRestart() - 1);
                    }
                    Bukkit.unloadWorld((String)a.getWorldName(), (boolean)false);
                    if (Arena.canAutoScale(a.getArenaName())) {
                        Bukkit.getScheduler().runTaskLater((Plugin)BedWars.plugin, () -> new Arena(a.getArenaName(), null), 80L);
                    }
                }
            } else {
                Bukkit.unloadWorld((String)a.getWorldName(), (boolean)false);
                Bukkit.getScheduler().runTaskLater((Plugin)BedWars.plugin, () -> new Arena(a.getArenaName(), null), 80L);
            }
            if (!a.getWorldName().equals(a.getArenaName())) {
                this.deleteWorld(a.getWorldName());
            }
        });
    }

    @Override
    public void onDisable(IArena a) {
        if (BedWars.isShuttingDown()) {
            Bukkit.unloadWorld((String)a.getWorldName(), (boolean)false);
            return;
        }
        Bukkit.getScheduler().runTask(this.getOwner(), () -> Bukkit.unloadWorld((String)a.getWorldName(), (boolean)false));
    }

    @Override
    public void onSetupSessionStart(ISetupSession s) {
        Bukkit.getScheduler().runTaskAsynchronously(this.getOwner(), () -> {
            File bf = new File(backupFolder, s.getWorldName() + ".zip");
            File af = new File(Bukkit.getWorldContainer(), s.getWorldName());
            if (bf.exists()) {
                FileUtil.delete(af);
                try {
                    ZipFileUtil.unzipFileIntoDirectory(bf, new File(Bukkit.getWorldContainer(), s.getWorldName()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            WorldCreator wc = new WorldCreator(s.getWorldName());
            wc.generator((ChunkGenerator)new VoidChunkGenerator());
            wc.generateStructures(false);
            Bukkit.getScheduler().runTask(this.getOwner(), () -> {
                try {
                    File level = new File(Bukkit.getWorldContainer(), s.getWorldName() + "/region");
                    if (!level.exists()) {
                        try {
                            s.getPlayer().sendMessage(String.valueOf(ChatColor.GREEN) + "Creating a new void map: " + s.getWorldName());
                            World w = Bukkit.createWorld((WorldCreator)wc);
                            w.setKeepSpawnInMemory(true);
                            Bukkit.getScheduler().runTaskLater((Plugin)BedWars.plugin, s::teleportPlayer, 20L);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            s.close();
                        }
                        return;
                    }
                    s.getPlayer().sendMessage(String.valueOf(ChatColor.GREEN) + "Loading " + s.getWorldName() + " from Bukkit worlds container.");
                    this.deleteWorldTrash(s.getWorldName());
                    World w = Bukkit.createWorld((WorldCreator)wc);
                    w.setKeepSpawnInMemory(true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    s.close();
                    return;
                }
                Bukkit.getScheduler().runTaskLater((Plugin)BedWars.plugin, s::teleportPlayer, 20L);
            });
        });
    }

    @Override
    public void onSetupSessionClose(ISetupSession s) {
        Bukkit.getScheduler().runTask(this.getOwner(), () -> {
            Bukkit.getWorld((String)s.getWorldName()).save();
            Bukkit.unloadWorld((String)s.getWorldName(), (boolean)true);
            Bukkit.getScheduler().runTaskAsynchronously((Plugin)BedWars.plugin, () -> new WorldZipper(s.getWorldName(), true));
        });
    }

    @Override
    public boolean isWorld(String name) {
        return new File(Bukkit.getWorldContainer(), name + "/region").exists();
    }

    @Override
    public void deleteWorld(String name) {
        Bukkit.getScheduler().runTaskAsynchronously(this.getOwner(), () -> {
            try {
                FileUtils.deleteDirectory(new File(Bukkit.getWorldContainer(), name));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void cloneArena(String name1, String name2) {
        Bukkit.getScheduler().runTaskAsynchronously((Plugin)BedWars.plugin, () -> {
            try {
                FileUtils.copyDirectory(new File(Bukkit.getWorldContainer(), name1), new File(Bukkit.getWorldContainer(), name2));
                this.deleteWorldTrash(name2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public List<String> getWorldsList() {
        ArrayList<String> worlds = new ArrayList<String>();
        File dir = Bukkit.getWorldContainer();
        if (dir.exists()) {
            File[] fls = dir.listFiles();
            for (File fl : Objects.requireNonNull(fls)) {
                File dat;
                if (!fl.isDirectory() || !(dat = new File(fl.getName() + "/region")).exists() || fl.getName().startsWith("bw_temp")) continue;
                worlds.add(fl.getName());
            }
        }
        return worlds;
    }

    @Override
    public void convertWorlds() {
        File dir = new File(BedWars.plugin.getDataFolder(), "/Arenas");
        if (dir.exists()) {
            ArrayList<File> files = new ArrayList<File>();
            File[] fls = dir.listFiles();
            for (File fl : Objects.requireNonNull(fls)) {
                if (!fl.isFile() || !fl.getName().contains(".yml")) continue;
                files.add(fl);
            }
            ArrayList<File> toRemove = new ArrayList<File>();
            ArrayList<File> toAdd = new ArrayList<File>();
            for (File file : files) {
                File folder;
                if (file.getName().equals(file.getName().toLowerCase())) continue;
                File newName = new File(dir.getPath() + "/" + file.getName().toLowerCase());
                if (!file.renameTo(newName)) {
                    toRemove.add(file);
                    BedWars.plugin.getLogger().severe("Could not rename " + file.getName() + " to " + file.getName().toLowerCase() + "! Please do it manually!");
                } else {
                    toAdd.add(newName);
                    toRemove.add(file);
                }
                if (!(folder = new File(BedWars.plugin.getServer().getWorldContainer(), file.getName().replace(".yml", ""))).exists() || folder.getName().equals(folder.getName().toLowerCase()) || folder.renameTo(new File(BedWars.plugin.getServer().getWorldContainer().getPath() + "/" + folder.getName().toLowerCase()))) continue;
                BedWars.plugin.getLogger().severe("Could not rename " + folder.getName() + " folder to " + folder.getName().toLowerCase() + "! Please do it manually!");
                toRemove.add(file);
                return;
            }
            for (File f : toRemove) {
                files.remove(f);
            }
            files.addAll(toAdd);
        }
        Bukkit.getScheduler().runTaskAsynchronously(this.getOwner(), () -> {
            File[] files = Bukkit.getWorldContainer().listFiles();
            if (files != null) {
                for (File f : files) {
                    if (f == null || !f.isDirectory() || !f.getName().contains("bw_temp_")) continue;
                    this.deleteWorld(f.getName());
                }
            }
        });
    }

    @Override
    public String getDisplayName() {
        return "Internal Restore Adapter";
    }

    private void deleteWorldTrash(String world) {
        for (File f : new File[]{new File(Bukkit.getWorldContainer(), world + "/level.dat"), new File(Bukkit.getWorldContainer(), world + "/level.dat_mcr"), new File(Bukkit.getWorldContainer(), world + "/level.dat_old"), new File(Bukkit.getWorldContainer(), world + "/session.lock"), new File(Bukkit.getWorldContainer(), world + "/uid.dat")}) {
            if (!f.exists() || f.delete()) continue;
            this.getOwner().getLogger().warning("Could not delete: " + f.getPath());
            this.getOwner().getLogger().warning("This may cause issues!");
        }
    }
}

