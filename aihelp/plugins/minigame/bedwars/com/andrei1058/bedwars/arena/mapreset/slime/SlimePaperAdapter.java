/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.infernalsuite.aswm.api.SlimePlugin
 *  com.infernalsuite.aswm.api.exceptions.CorruptedWorldException
 *  com.infernalsuite.aswm.api.exceptions.InvalidWorldException
 *  com.infernalsuite.aswm.api.exceptions.NewerFormatException
 *  com.infernalsuite.aswm.api.exceptions.UnknownWorldException
 *  com.infernalsuite.aswm.api.exceptions.WorldAlreadyExistsException
 *  com.infernalsuite.aswm.api.exceptions.WorldLoadedException
 *  com.infernalsuite.aswm.api.exceptions.WorldLockedException
 *  com.infernalsuite.aswm.api.exceptions.WorldTooBigException
 *  com.infernalsuite.aswm.api.loaders.SlimeLoader
 *  com.infernalsuite.aswm.api.world.SlimeWorld
 *  com.infernalsuite.aswm.api.world.properties.SlimeProperties
 *  com.infernalsuite.aswm.api.world.properties.SlimePropertyMap
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.World
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.world.WorldInitEvent
 *  org.bukkit.event.world.WorldLoadEvent
 *  org.bukkit.plugin.Plugin
 *  org.jetbrains.annotations.NotNull
 */
package com.andrei1058.bedwars.arena.mapreset.slime;

import com.andrei1058.bedwars.api.BedWars;
import com.andrei1058.bedwars.api.arena.GameState;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.server.ISetupSession;
import com.andrei1058.bedwars.api.server.RestoreAdapter;
import com.andrei1058.bedwars.api.server.ServerType;
import com.andrei1058.bedwars.api.util.FileUtil;
import com.andrei1058.bedwars.api.util.ZipFileUtil;
import com.flowpowered.nbt.CompoundMap;
import com.flowpowered.nbt.CompoundTag;
import com.flowpowered.nbt.IntTag;
import com.flowpowered.nbt.stream.NBTInputStream;
import com.flowpowered.nbt.stream.NBTOutputStream;
import com.infernalsuite.aswm.api.SlimePlugin;
import com.infernalsuite.aswm.api.exceptions.CorruptedWorldException;
import com.infernalsuite.aswm.api.exceptions.InvalidWorldException;
import com.infernalsuite.aswm.api.exceptions.NewerFormatException;
import com.infernalsuite.aswm.api.exceptions.UnknownWorldException;
import com.infernalsuite.aswm.api.exceptions.WorldAlreadyExistsException;
import com.infernalsuite.aswm.api.exceptions.WorldLoadedException;
import com.infernalsuite.aswm.api.exceptions.WorldLockedException;
import com.infernalsuite.aswm.api.exceptions.WorldTooBigException;
import com.infernalsuite.aswm.api.loaders.SlimeLoader;
import com.infernalsuite.aswm.api.world.SlimeWorld;
import com.infernalsuite.aswm.api.world.properties.SlimeProperties;
import com.infernalsuite.aswm.api.world.properties.SlimePropertyMap;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class SlimePaperAdapter
extends RestoreAdapter {
    private final SlimePlugin slime = (SlimePlugin)Bukkit.getPluginManager().getPlugin("SlimeWorldManager");
    private final BedWars api = (BedWars)Objects.requireNonNull(Bukkit.getServer().getServicesManager().getRegistration(BedWars.class)).getProvider();

    public SlimePaperAdapter(Plugin plugin) {
        super(plugin);
    }

    @Override
    public void onEnable(@NotNull IArena a2) {
        if (this.api.getVersionSupport().getMainLevel().equalsIgnoreCase(a2.getWorldName()) && (this.api.getServerType() != ServerType.BUNGEE || this.api.getArenaUtil().getGamesBeforeRestart() != 1)) {
            FileUtil.setMainLevel("ignore_main_level", this.api.getVersionSupport());
            this.getOwner().getLogger().log(Level.SEVERE, "Cannot use level-name as arenas. Automatically creating a new void map for level-name.");
            this.getOwner().getLogger().log(Level.SEVERE, "The server is restarting...");
            Bukkit.getServer().spigot().restart();
            return;
        }
        Bukkit.getScheduler().runTaskAsynchronously(this.getOwner(), () -> {
            if (Bukkit.getWorld((String)a2.getWorldName()) != null) {
                Bukkit.getScheduler().runTask(this.getOwner(), () -> {
                    World w = Bukkit.getWorld((String)a2.getWorldName());
                    a2.init(w);
                });
                return;
            }
            SlimeLoader flat = this.slime.getLoader("file");
            String[] spawn = a2.getConfig().getString("waiting.Loc").split(",");
            SlimePropertyMap spm = this.getCreateProperties((int)Double.parseDouble(spawn[0]), (int)Double.parseDouble(spawn[1]), (int)Double.parseDouble(spawn[2]));
            try {
                SlimeWorld world = this.slime.loadWorld(flat, a2.getArenaName(), true, spm);
                if (this.api.getServerType() == ServerType.BUNGEE && this.api.isAutoScale()) {
                    world = world.clone(a2.getWorldName());
                }
                SlimeWorld finalWorld = world;
                Bukkit.getScheduler().runTask(this.getOwner(), () -> {
                    SlimeWorld loaded = this.slime.loadWorld(finalWorld);
                    if (null == loaded) {
                        this.api.getArenaUtil().removeFromEnableQueue(a2);
                        this.getOwner().getLogger().severe("Something wrong... removing arena " + a2.getArenaName() + " from queue.");
                        return;
                    }
                    World w = Bukkit.getWorld((String)loaded.getName());
                    if (w == null) {
                        this.api.getArenaUtil().removeFromEnableQueue(a2);
                        this.getOwner().getLogger().severe("Something wrong... removing arena " + a2.getArenaName() + " from queue.");
                        return;
                    }
                    Bukkit.getPluginManager().callEvent((Event)new WorldInitEvent(w));
                    Bukkit.getScheduler().runTask(this.getOwner(), () -> Bukkit.getPluginManager().callEvent((Event)new WorldLoadEvent(w)));
                });
            } catch (CorruptedWorldException | NewerFormatException | UnknownWorldException | WorldLockedException | IOException ex) {
                this.api.getArenaUtil().removeFromEnableQueue(a2);
                ex.printStackTrace();
            } catch (ConcurrentModificationException thisShouldNotHappenSWM) {
                thisShouldNotHappenSWM.printStackTrace();
                this.api.getArenaUtil().removeFromEnableQueue(a2);
                this.getOwner().getLogger().severe("This is a AdvancedSlimePaper issue!");
                this.getOwner().getLogger().severe("I've submitted a bug report: https://github.com/Grinderwolf/Slime-World-Manager/issues/174");
                this.getOwner().getLogger().severe("Trying again to load arena: " + a2.getArenaName());
                this.onEnable(a2);
            }
        });
    }

    @Override
    public void onRestart(IArena a2) {
        if (this.api.getServerType() == ServerType.BUNGEE) {
            if (this.api.getArenaUtil().getGamesBeforeRestart() == 0) {
                if (this.api.getArenaUtil().getArenas().size() == 1 && this.api.getArenaUtil().getArenas().get(0).getStatus() == GameState.restarting) {
                    this.getOwner().getLogger().info("Dispatching command: " + this.api.getConfigs().getMainConfig().getString("bungee-settings.restart-cmd"));
                    Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), (String)this.api.getConfigs().getMainConfig().getString("bungee-settings.restart-cmd"));
                }
            } else {
                if (this.api.getArenaUtil().getGamesBeforeRestart() != -1) {
                    this.api.getArenaUtil().setGamesBeforeRestart(this.api.getArenaUtil().getGamesBeforeRestart() - 1);
                }
                Bukkit.getScheduler().runTask(this.getOwner(), () -> {
                    Bukkit.unloadWorld((String)a2.getWorldName(), (boolean)false);
                    if (this.api.getArenaUtil().canAutoScale(a2.getArenaName())) {
                        Bukkit.getScheduler().runTaskLater(this.getOwner(), () -> this.api.getArenaUtil().loadArena(a2.getArenaName(), null), 80L);
                    }
                });
            }
        } else {
            Bukkit.getScheduler().runTask(this.getOwner(), () -> {
                Bukkit.unloadWorld((String)a2.getWorldName(), (boolean)false);
                Bukkit.getScheduler().runTaskLater(this.getOwner(), () -> this.api.getArenaUtil().loadArena(a2.getArenaName(), null), 80L);
            });
        }
    }

    @Override
    public void onDisable(IArena a2) {
        if (this.api.isShuttingDown()) {
            Bukkit.unloadWorld((String)a2.getWorldName(), (boolean)false);
            return;
        }
        Bukkit.getScheduler().runTask(this.getOwner(), () -> Bukkit.unloadWorld((String)a2.getWorldName(), (boolean)false));
    }

    @Override
    public void onSetupSessionStart(ISetupSession s) {
        Bukkit.getScheduler().runTaskAsynchronously(this.getOwner(), () -> {
            SlimeLoader sLoader = this.slime.getLoader("file");
            String[] spawn = new String[]{"0", "50", "0"};
            if (s.getConfig().getYml().getString("waiting.Loc") != null) {
                spawn = s.getConfig().getString("waiting.Loc").split(",");
            }
            SlimePropertyMap spm = this.getCreateProperties((int)Double.parseDouble(spawn[0]), (int)Double.parseDouble(spawn[1]), (int)Double.parseDouble(spawn[2]));
            try {
                SlimeWorld world;
                if (Bukkit.getWorld((String)s.getWorldName()) != null) {
                    Bukkit.getScheduler().runTask(this.getOwner(), () -> Bukkit.unloadWorld((String)s.getWorldName(), (boolean)false));
                }
                if (sLoader.worldExists(s.getWorldName())) {
                    world = this.slime.loadWorld(sLoader, s.getWorldName(), false, spm);
                    Bukkit.getScheduler().runTask(this.getOwner(), () -> s.getPlayer().sendMessage(String.valueOf(ChatColor.GREEN) + "Loading world from SlimeWorldManager container."));
                } else if (new File(Bukkit.getWorldContainer(), s.getWorldName() + "/level.dat").exists()) {
                    Bukkit.getScheduler().runTask(this.getOwner(), () -> s.getPlayer().sendMessage(String.valueOf(ChatColor.GREEN) + "Importing world to the SlimeWorldManager container."));
                    this.slime.importWorld(new File(Bukkit.getWorldContainer(), s.getWorldName()), s.getWorldName().toLowerCase(), sLoader);
                    world = this.slime.loadWorld(sLoader, s.getWorldName(), false, spm);
                } else {
                    Bukkit.getScheduler().runTask(this.getOwner(), () -> s.getPlayer().sendMessage(String.valueOf(ChatColor.GREEN) + "Creating anew void map."));
                    world = this.slime.createEmptyWorld(sLoader, s.getWorldName(), false, spm);
                }
                SlimeWorld sw = world;
                Bukkit.getScheduler().runTask(this.getOwner(), () -> {
                    if (null == sw) {
                        return;
                    }
                    World w = Bukkit.getWorld((String)sw.getName());
                    if (w == null) {
                        return;
                    }
                    Bukkit.getPluginManager().callEvent((Event)new WorldInitEvent(w));
                    Bukkit.getScheduler().runTask(this.getOwner(), () -> Bukkit.getPluginManager().callEvent((Event)new WorldLoadEvent(w)));
                    s.teleportPlayer();
                });
            } catch (CorruptedWorldException | InvalidWorldException | NewerFormatException | UnknownWorldException | WorldAlreadyExistsException | WorldLoadedException | WorldLockedException | WorldTooBigException | IOException ex) {
                s.getPlayer().sendMessage(String.valueOf(ChatColor.RED) + "An error occurred! Please check console.");
                ex.printStackTrace();
                s.close();
            }
        });
    }

    @Override
    public void onSetupSessionClose(ISetupSession s) {
        Objects.requireNonNull(Bukkit.getWorld((String)s.getWorldName())).save();
        Bukkit.getScheduler().runTask(this.getOwner(), () -> Bukkit.unloadWorld((String)s.getWorldName(), (boolean)true));
    }

    @Override
    public boolean isWorld(String name) {
        try {
            return this.slime.getLoader("file").worldExists(name);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void deleteWorld(String name) {
        Bukkit.getScheduler().runTaskAsynchronously(this.getOwner(), () -> {
            try {
                this.slime.getLoader("file").deleteWorld(name);
            } catch (UnknownWorldException | IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void cloneArena(String name1, String name2) {
        Bukkit.getScheduler().runTaskAsynchronously(this.getOwner(), () -> {
            try {
                SlimeWorld world = this.slime.loadWorld(this.slime.getLoader("file"), name1, true, this.getCreateProperties(0, 118, 0));
                world.clone(name2, this.slime.getLoader("file"));
            } catch (CorruptedWorldException | NewerFormatException | UnknownWorldException | WorldAlreadyExistsException | WorldLockedException | IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    @Override
    public List<String> getWorldsList() {
        try {
            return this.slime.getLoader("file").listWorlds();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<String>();
        }
    }

    @Override
    public void convertWorlds() {
        File[] fls;
        File dir = new File(this.getOwner().getDataFolder(), "/Arenas");
        SlimeLoader sl = this.slime.getLoader("file");
        if (dir.exists() && (fls = dir.listFiles()) != null) {
            for (File fl : fls) {
                if (!fl.isFile() || !fl.getName().endsWith(".yml")) continue;
                String name = fl.getName().replace(".yml", "").toLowerCase();
                File ff = new File(Bukkit.getWorldContainer(), fl.getName().replace(".yml", ""));
                try {
                    if (sl.worldExists(name)) continue;
                    if (!fl.getName().equals(name) && !fl.renameTo(new File(dir, name + ".yml"))) {
                        this.getOwner().getLogger().log(Level.WARNING, "Could not rename " + fl.getName() + ".yml to " + name + ".yml");
                    }
                    File bc = new File(String.valueOf(this.getOwner().getDataFolder()) + "/Cache", ff.getName() + ".zip");
                    if (ff.exists() && bc.exists()) {
                        FileUtil.delete(ff);
                        ZipFileUtil.unzipFileIntoDirectory(bc, new File(Bukkit.getWorldContainer(), name));
                    }
                    this.deleteWorldTrash(name);
                    this.handleLevelDat(name);
                    this.convertWorld(name, null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Bukkit.getScheduler().runTaskAsynchronously(this.getOwner(), () -> {
            File[] files = Bukkit.getWorldContainer().listFiles();
            if (files != null) {
                for (File f : files) {
                    if (f == null || !f.isDirectory() || !f.getName().contains("bw_temp_")) continue;
                    try {
                        FileUtils.deleteDirectory(f);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public String getDisplayName() {
        return "Advanced Slime Paper by InfernalSuite";
    }

    private void convertWorld(String name, Player player) {
        File worldFolder = new File(Bukkit.getWorldContainer(), name);
        if (!worldFolder.exists() || !worldFolder.isDirectory()) {
            this.getOwner().getLogger().severe("Tried converting arena " + name + " to Slime format, but couldn't find any bukkit world folder.");
            return;
        }
        SlimeLoader sl = this.slime.getLoader("file");
        try {
            this.getOwner().getLogger().log(Level.INFO, "Converting " + name + " to the Slime format.");
            this.slime.importWorld(new File(Bukkit.getWorldContainer(), name), name, sl);
        } catch (InvalidWorldException | WorldAlreadyExistsException | WorldLoadedException | WorldTooBigException | IOException e) {
            if (player != null) {
                player.sendMessage(String.valueOf(ChatColor.RED) + "Could not convert " + name + " to the Slime format.");
                player.sendMessage(String.valueOf(ChatColor.RED) + "Check the console for details.");
                ISetupSession s = this.api.getSetupSession(player.getUniqueId());
                if (s != null) {
                    s.close();
                }
            }
            this.getOwner().getLogger().log(Level.WARNING, "Could not convert " + name + " to the Slime format.");
            e.printStackTrace();
        }
    }

    private void deleteWorldTrash(String world) {
        for (File f : new File[]{new File(Bukkit.getWorldContainer(), world + "/level.dat_mcr"), new File(Bukkit.getWorldContainer(), world + "/level.dat_old"), new File(Bukkit.getWorldContainer(), world + "/session.lock"), new File(Bukkit.getWorldContainer(), world + "/uid.dat")}) {
            if (!f.exists() || f.delete()) continue;
            this.getOwner().getLogger().warning("Could not delete: " + f.getPath());
            this.getOwner().getLogger().warning("This may cause issues!");
        }
    }

    private void handleLevelDat(String world) throws IOException {
        File worldFolder = new File(Bukkit.getWorldContainer(), world);
        if (!worldFolder.exists() || !worldFolder.isDirectory()) {
            return;
        }
        File levelFile = new File(worldFolder, "level.dat");
        if (levelFile.exists()) {
            return;
        }
        File regionFolder = new File(worldFolder, "region");
        File[] regionFiles = regionFolder.listFiles();
        if (!regionFolder.exists() || null == regionFiles || !regionFolder.isDirectory()) {
            this.getOwner().getLogger().severe("Tried detecting world version, but it has no regions! (" + world + ")");
            return;
        }
        Optional<File> firstRegion = Arrays.stream(regionFiles).filter(regionFile -> regionFile.isFile() && regionFile.getName().endsWith(".mca")).findFirst();
        Optional<Object> dataVersion = Optional.empty();
        if (firstRegion.isPresent()) {
            try {
                Optional<CompoundTag> dataTag;
                Optional version;
                NBTInputStream inputStream = new NBTInputStream(new FileInputStream(firstRegion.get()));
                Optional<CompoundTag> tag = inputStream.readTag().getAsCompoundTag();
                inputStream.close();
                if (tag.isPresent() && (version = (dataTag = tag.get().getAsCompoundTag("Chunk")).flatMap(tagMap -> tagMap.getIntValue("DataVersion"))).isPresent()) {
                    dataVersion = version;
                    Bukkit.getLogger().info("Detected world version from region file for level.dat creation: v" + String.valueOf(version.get()) + " (" + world + ")");
                }
            } catch (Exception inputStream) {
                // empty catch block
            }
        }
        String errorMessage = "Cannot create level.dat in " + String.valueOf(worldFolder);
        if (!levelFile.createNewFile()) {
            this.getOwner().getLogger().severe(errorMessage);
            return;
        }
        try (NBTOutputStream outputStream = new NBTOutputStream(new FileOutputStream(levelFile));){
            CompoundMap cm = new CompoundMap();
            cm.put(new IntTag("SpawnX", 0));
            cm.put(new IntTag("SpawnY", 255));
            cm.put(new IntTag("SpawnZ", 0));
            dataVersion.ifPresent(integer -> cm.put(new IntTag("DataVersion", (int)integer)));
            CompoundTag dataTag = new CompoundTag("Data", cm);
            CompoundMap rootTag = new CompoundMap();
            rootTag.put(dataTag);
            outputStream.writeTag(new CompoundTag("", rootTag));
            outputStream.flush();
        } catch (Exception ignored) {
            try {
                levelFile.delete();
            } catch (Exception exception) {
                // empty catch block
            }
            this.getOwner().getLogger().severe(errorMessage);
        }
    }

    @NotNull
    private SlimePropertyMap getCreateProperties(int spawnX, int spawnY, int spawnZ) {
        SlimePropertyMap spm = new SlimePropertyMap();
        spm.setValue(SlimeProperties.WORLD_TYPE, (Object)"flat");
        spm.setValue(SlimeProperties.SPAWN_X, (Object)spawnX);
        spm.setValue(SlimeProperties.SPAWN_Y, (Object)spawnY);
        spm.setValue(SlimeProperties.SPAWN_Z, (Object)spawnZ);
        spm.setValue(SlimeProperties.ALLOW_ANIMALS, (Object)false);
        spm.setValue(SlimeProperties.ALLOW_MONSTERS, (Object)false);
        spm.setValue(SlimeProperties.DIFFICULTY, (Object)"easy");
        spm.setValue(SlimeProperties.PVP, (Object)true);
        return spm;
    }
}

