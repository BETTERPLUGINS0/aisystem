/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.internal.LinkedTreeMap
 *  org.apache.commons.io.FileUtils
 *  org.bukkit.Bukkit
 *  org.bukkit.event.Event
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 */
package com.magmaguy.magmacore.dlc;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.magmaguy.magmacore.MagmaCore;
import com.magmaguy.magmacore.dlc.ConfigurationImportProfiles;
import com.magmaguy.magmacore.events.ModelInstallationEvent;
import com.magmaguy.magmacore.util.Logger;
import com.magmaguy.magmacore.util.ZipFile;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigurationImporter {
    private final JavaPlugin ownerPlugin;
    private final Path eliteMobsPath;
    private final Path extractioncraftPath;
    private final Path betterStructuresPath;
    private final Path freeMinecraftModelsPath;
    private final Path modelEnginePath;
    private final Path eternalTDPath;
    private final Path megaBlockSurvivorsPath;
    private PluginPlatform pluginPlatform;
    private File importsFolder;
    private boolean modelsInstalled = false;

    public ConfigurationImporter(JavaPlugin ownerPlugin) {
        this.ownerPlugin = ownerPlugin == null ? MagmaCore.getInstance().getRequestingPlugin() : ownerPlugin;
        Path pluginsDirectory = Path.of((String)this.ownerPlugin.getDataFolder().getParentFile().getAbsolutePath(), (String[])new String[0]);
        this.eliteMobsPath = pluginsDirectory.resolve("EliteMobs");
        this.extractioncraftPath = pluginsDirectory.resolve("Extractioncraft");
        this.betterStructuresPath = pluginsDirectory.resolve("BetterStructures");
        this.freeMinecraftModelsPath = pluginsDirectory.resolve("FreeMinecraftModels");
        this.modelEnginePath = pluginsDirectory.resolve("ModelEngine");
        this.eternalTDPath = pluginsDirectory.resolve("EternalTD");
        this.megaBlockSurvivorsPath = pluginsDirectory.resolve("MegaBlockSurvivors");
        if (!this.createImportsDirectory()) {
            return;
        }
        this.importsFolder = this.getImportsDirectory();
        if (this.importsFolder == null || this.importsFolder.listFiles().length == 0) {
            return;
        }
        this.pluginPlatform = this.getPluginPlatform(this.ownerPlugin.getName());
        this.processImportsFolder();
        if (Bukkit.getPluginManager().isPluginEnabled("FreeMinecraftModels") && this.modelsInstalled) {
            if (Bukkit.isPrimaryThread()) {
                Bukkit.getPluginManager().callEvent((Event)new ModelInstallationEvent());
            } else {
                Bukkit.getScheduler().runTask((Plugin)this.ownerPlugin, () -> Bukkit.getPluginManager().callEvent((Event)new ModelInstallationEvent()));
            }
        }
    }

    private static void deleteDirectory(File file) {
        if (file == null) {
            return;
        }
        if (file.isDirectory()) {
            for (File iteratedFile : file.listFiles()) {
                if (iteratedFile == null) continue;
                ConfigurationImporter.deleteDirectory(iteratedFile);
            }
        }
        Logger.info("Cleaning up " + file.getPath());
        file.delete();
    }

    private void moveWorlds(File worldcontainerFile) {
        for (File file : worldcontainerFile.listFiles()) {
            try {
                File worldContainer = Bukkit.getWorldContainer().getCanonicalFile();
                Path worldContainerPath = worldContainer.toPath().normalize().toAbsolutePath();
                Path destinationPath = worldContainerPath.resolve(file.getName());
                File destinationFile = destinationPath.toFile();
                if (destinationFile.exists()) {
                    Logger.info("Overriding existing directory " + destinationFile.getPath());
                    if (Bukkit.getWorld((String)file.getName()) != null) {
                        if (Bukkit.isPrimaryThread()) {
                            Bukkit.unloadWorld((String)file.getName(), (boolean)false);
                        } else {
                            try {
                                Bukkit.getScheduler().callSyncMethod((Plugin)this.ownerPlugin, () -> Bukkit.unloadWorld((String)file.getName(), (boolean)false)).get();
                            } catch (InterruptedException | ExecutionException e) {
                                Logger.warn("Failed to unload world " + file.getName() + " on main thread!");
                                e.printStackTrace();
                            }
                        }
                        Logger.warn("Unloaded world " + file.getName() + " for safe replacement!");
                    }
                    ConfigurationImporter.deleteDirectory(destinationFile);
                }
                ConfigurationImporter.moveDirectory(file, destinationPath);
            } catch (Exception exception) {
                Logger.warn("Failed to move worlds for " + file.getName() + "! Tell the dev!");
                exception.printStackTrace();
            }
        }
    }

    private static void moveDirectory(File unzippedDirectory, Path targetPath) {
        for (File file : unzippedDirectory.listFiles()) {
            try {
                ConfigurationImporter.moveFile(file, targetPath);
            } catch (Exception exception) {
                Logger.warn("Failed to move directories for " + file.getName() + "! Tell the dev!");
                exception.printStackTrace();
            }
        }
    }

    private static void moveFile(File file, Path targetPath) {
        try {
            Path destinationPath = targetPath.resolve(file.getName());
            if (file.isDirectory()) {
                if (Files.exists(destinationPath, new LinkOption[0])) {
                    for (File iteratedFile : file.listFiles()) {
                        ConfigurationImporter.moveFile(iteratedFile, destinationPath);
                    }
                } else {
                    Files.createDirectories(targetPath, new FileAttribute[0]);
                    Files.move(file.toPath().normalize().toAbsolutePath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
                }
            } else {
                Files.createDirectories(targetPath, new FileAttribute[0]);
                Files.move(file.toPath().normalize().toAbsolutePath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception exception) {
            Logger.warn("Failed to move file/directories for " + file.getName() + "! Tell the dev!");
            exception.printStackTrace();
        }
    }

    private boolean createImportsDirectory() {
        Path configurationsPath = Paths.get(this.ownerPlugin.getDataFolder().getAbsolutePath(), new String[0]);
        Path importsPath = configurationsPath.normalize().resolve("imports");
        if (!Files.isDirectory(importsPath, new LinkOption[0])) {
            try {
                File importsFile = importsPath.toFile();
                if (!importsFile.getParentFile().exists()) {
                    importsPath.toFile().mkdirs();
                }
                Files.createDirectories(importsPath, new FileAttribute[0]);
                return true;
            } catch (Exception exception) {
                Logger.warn("Failed to create import directory! Tell the dev!");
                exception.printStackTrace();
                return false;
            }
        }
        return true;
    }

    private File getImportsDirectory() {
        try {
            File dir = Paths.get(this.ownerPlugin.getDataFolder().getCanonicalPath(), new String[0]).resolve("imports").toFile();
            return dir;
        } catch (Exception ex) {
            Logger.warn("Failed to get imports folder! Report this to the dev!");
            ex.printStackTrace();
            return null;
        }
    }

    private PluginPlatform getPluginPlatform(String name) {
        if (name == null) {
            return PluginPlatform.NONE;
        }
        switch (name.toLowerCase(Locale.ROOT)) {
            case "elitemobs": {
                return PluginPlatform.ELITEMOBS;
            }
            case "extractioncraft": {
                return PluginPlatform.EXTRACTIONCRAFT;
            }
            case "betterstructures": {
                return PluginPlatform.BETTERSTRUCTURES;
            }
            case "freeminecraftmodels": {
                return PluginPlatform.FREEMINECRAFTMODELS;
            }
            case "eternaltd": {
                return PluginPlatform.ETERNALTD;
            }
            case "megablocksurvivors": {
                return PluginPlatform.MEGABLOCKSURVIVORS;
            }
        }
        return PluginPlatform.NONE;
    }

    private void processImportsFolder() {
        for (File zippedFile : this.importsFolder.listFiles()) {
            if (zippedFile.getName().endsWith(".zip")) {
                this.unzipImportFile(zippedFile);
                continue;
            }
            if (this.pluginPlatform == PluginPlatform.FREEMINECRAFTMODELS && zippedFile.getName().endsWith(".bbmodel")) {
                this.processBbmodel(zippedFile);
                continue;
            }
            if (zippedFile.isDirectory()) {
                boolean incorrectlyUnzippedFolder = false;
                for (File iteratedFile : zippedFile.listFiles()) {
                    if (!iteratedFile.getName().equalsIgnoreCase("pack.meta")) continue;
                    incorrectlyUnzippedFolder = true;
                    break;
                }
                if (!incorrectlyUnzippedFolder) continue;
                this.processUnzippedFile(zippedFile);
                continue;
            }
            Logger.warn("File " + zippedFile.getPath() + " can't be imported! It will be skipped.");
        }
    }

    private void unzipImportFile(File zippedFile) {
        try {
            File unzippedFolder = ZipFile.unzip(zippedFile, new File(zippedFile.getAbsolutePath().replace(".zip", "")));
            this.processUnzippedFile(unzippedFolder);
            ConfigurationImporter.deleteDirectory(zippedFile);
        } catch (Exception ex) {
            Logger.warn("Failed to unzip " + zippedFile.getPath() + " ! This probably means the file is corrupted.");
            Logger.warn("To fix this, delete this file from the imports folder and download a clean copy!");
            ex.printStackTrace();
        }
    }

    private void processUnzippedFile(File unzippedFolder) {
        PluginPlatform platform = this.pluginPlatform;
        for (File unzippedFile : unzippedFolder.listFiles()) {
            if (!unzippedFile.getName().equalsIgnoreCase("pack.meta")) continue;
            platform = this.getPluginPlatform(this.readPackMeta(unzippedFile));
        }
        for (File unzippedFile : unzippedFolder.listFiles()) {
            this.moveUnzippedFiles(unzippedFile, platform);
        }
        ConfigurationImporter.deleteDirectory(unzippedFolder);
    }

    private void moveUnzippedFiles(File unzippedFile, PluginPlatform platform) {
        Path targetPath = this.getTargetPath(unzippedFile.getName(), platform);
        if (targetPath == null) {
            return;
        }
        if (!targetPath.toFile().exists()) {
            targetPath.toFile().mkdirs();
        }
        if (unzippedFile.isDirectory()) {
            if (unzippedFile.getName().equalsIgnoreCase("worldcontainer")) {
                this.moveWorlds(unzippedFile);
            } else {
                ConfigurationImporter.moveDirectory(unzippedFile, targetPath);
            }
        } else {
            ConfigurationImporter.moveFile(unzippedFile, targetPath);
        }
    }

    private Path getTargetPath(String folder, PluginPlatform platform) {
        return ConfigurationImportProfiles.resolve(this, folder, platform);
    }

    Path getEliteMobsPath() {
        return this.eliteMobsPath;
    }

    Path getExtractioncraftPath() {
        return this.extractioncraftPath;
    }

    Path getBetterStructuresPath() {
        return this.betterStructuresPath;
    }

    Path getFreeMinecraftModelsPath() {
        return this.freeMinecraftModelsPath;
    }

    Path getModelEnginePath() {
        return this.modelEnginePath;
    }

    Path getEternalTDPath() {
        return this.eternalTDPath;
    }

    Path getMegaBlockSurvivorsPath() {
        return this.megaBlockSurvivorsPath;
    }

    void markModelsInstalled() {
        this.modelsInstalled = true;
    }

    private String readPackMeta(File packMetaFile) {
        if (packMetaFile == null || !packMetaFile.exists()) {
            Logger.warn("File " + (packMetaFile != null ? packMetaFile.getPath() : "null") + " does not exist or is not valid.");
            return null;
        }
        try {
            Path filePath = packMetaFile.getCanonicalFile().toPath().normalize().toAbsolutePath();
            return Files.readString((Path)filePath, (Charset)StandardCharsets.UTF_8);
        } catch (IOException e) {
            Logger.warn("Failed to read pack.meta file " + packMetaFile.getPath() + ". Ensure the file is readable.");
            e.printStackTrace();
            return null;
        }
    }

    private void processBbmodel(File bbmodelFile) {
        Object pathName;
        File parentFile;
        BufferedReader reader;
        Gson readerGson = new Gson();
        try {
            reader = Files.newBufferedReader(bbmodelFile.getCanonicalFile().toPath());
        } catch (Exception ex) {
            Logger.warn("Failed to read file " + bbmodelFile.getAbsolutePath());
            return;
        }
        Map jsonMap = (Map)readerGson.fromJson((Reader)reader, Map.class);
        int blockBenchVersion = this.detectVersion(jsonMap);
        List mergedOutliner = this.mergeGroupsAndOutliner(jsonMap, blockBenchVersion);
        Gson writerGson = new Gson();
        HashMap minifiedMap = new HashMap();
        minifiedMap.put("meta", jsonMap.get("meta"));
        minifiedMap.put("resolution", jsonMap.get("resolution"));
        minifiedMap.put("elements", jsonMap.get("elements"));
        minifiedMap.put("outliner", mergedOutliner);
        ArrayList minifiedTextures = new ArrayList();
        ((ArrayList)jsonMap.get("textures")).forEach(innerMap -> minifiedTextures.add(Map.of((Object)"source", (Object)((LinkedTreeMap)innerMap).get((Object)"source"), (Object)"id", (Object)((LinkedTreeMap)innerMap).get((Object)"id"), (Object)"name", (Object)((LinkedTreeMap)innerMap).get((Object)"name"))));
        minifiedMap.put("textures", minifiedTextures);
        minifiedMap.put("animations", jsonMap.get("animations"));
        ArrayList<String> parentFiles = new ArrayList<String>();
        File currentFile = bbmodelFile;
        while (!(parentFile = currentFile.getParentFile()).getName().equals("imports")) {
            parentFiles.add(parentFile.getName());
            currentFile = parentFile;
        }
        String modelsDirectory = this.freeMinecraftModelsPath.toFile().getAbsolutePath() + File.separatorChar + "models";
        if (parentFiles.isEmpty()) {
            pathName = modelsDirectory + File.separatorChar + bbmodelFile.getName().replace(".bbmodel", ".fmmodel");
        } else {
            StringBuilder sb = new StringBuilder(modelsDirectory);
            for (int i = parentFiles.size() - 1; i >= 0; --i) {
                sb.append(File.separatorChar).append((String)parentFiles.get(i));
            }
            sb.append(File.separatorChar).append(bbmodelFile.getName().replace(".bbmodel", ".fmmodel"));
            pathName = sb.toString();
        }
        try {
            FileUtils.writeStringToFile((File)new File((String)pathName), (String)writerGson.toJson(minifiedMap), (Charset)StandardCharsets.UTF_8);
        } catch (IOException e) {
            Logger.warn("Failed to generate the minified file!");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private int detectVersion(Map<?, ?> bbmodelData) {
        try {
            Map meta = (Map)bbmodelData.get("meta");
            if (meta == null) {
                Logger.warn("Missing 'meta' field in model. Defaulting to version 4.");
                return 4;
            }
            Object versionObj = meta.get("format_version");
            if (versionObj == null) {
                Logger.warn("Missing 'format_version' in meta. Defaulting to version 4.");
                return 4;
            }
            String versionStr = versionObj.toString();
            String[] parts = versionStr.split("\\.");
            return Integer.parseInt(parts[0]);
        } catch (Exception e) {
            Logger.warn("Failed to parse format_version. Error: " + e.getMessage() + ". Defaulting to version 4.");
            return 4;
        }
    }

    private List mergeGroupsAndOutliner(Map<?, ?> bbmodelData, int blockBenchVersion) {
        ArrayList outlinerValues = (ArrayList)bbmodelData.get("outliner");
        if (blockBenchVersion < 5) {
            return outlinerValues;
        }
        ArrayList groupsList = (ArrayList)bbmodelData.get("groups");
        if (groupsList == null) {
            return outlinerValues;
        }
        HashMap<String, Map> groupsMap = new HashMap<String, Map>();
        for (Object groupObj : groupsList) {
            Map group;
            String uuid;
            if (!(groupObj instanceof Map) || (uuid = (String)(group = (Map)groupObj).get("uuid")) == null) continue;
            groupsMap.put(uuid, group);
        }
        return this.processOutlinerItems(outlinerValues, groupsMap);
    }

    private List processOutlinerItems(List items, HashMap<String, Map> groupsMap) {
        ArrayList result = new ArrayList();
        for (Object item : items) {
            List children;
            HashMap<String, List> mergedItem;
            if (item instanceof String) {
                result.add(item);
                continue;
            }
            if (!(item instanceof Map)) continue;
            Map outlinerItem = (Map)item;
            String uuid = (String)outlinerItem.get("uuid");
            if (uuid != null && groupsMap.containsKey(uuid)) {
                Map groupData = groupsMap.get(uuid);
                mergedItem = new HashMap(groupData);
            } else {
                mergedItem = new HashMap<String, List>(outlinerItem);
            }
            if (outlinerItem.containsKey("children") && (children = (List)outlinerItem.get("children")) != null && !children.isEmpty()) {
                List processedChildren = this.processOutlinerItems(children, groupsMap);
                mergedItem.put("children", processedChildren);
            }
            result.add(mergedItem);
        }
        return result;
    }

    static enum PluginPlatform {
        ELITEMOBS,
        EXTRACTIONCRAFT,
        BETTERSTRUCTURES,
        FREEMINECRAFTMODELS,
        ETERNALTD,
        MEGABLOCKSURVIVORS,
        RESURRECTIONCHEST,
        NONE;

    }
}

