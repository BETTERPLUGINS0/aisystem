/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.java.JavaPlugin
 */
package com.magmaguy.betterstructures.content;

import com.magmaguy.betterstructures.MetadataHandler;
import com.magmaguy.betterstructures.commands.ReloadCommand;
import com.magmaguy.betterstructures.config.contentpackages.ContentPackageConfigFields;
import com.magmaguy.betterstructures.config.schematics.SchematicConfig;
import com.magmaguy.betterstructures.config.schematics.SchematicConfigField;
import com.magmaguy.magmacore.menus.ContentPackage;
import com.magmaguy.magmacore.nightbreak.NightbreakAccount;
import com.magmaguy.magmacore.nightbreak.NightbreakContentManager;
import com.magmaguy.magmacore.nightbreak.NightbreakManagedContent;
import com.magmaguy.magmacore.nightbreak.NightbreakSetupMenuHelper;
import com.magmaguy.magmacore.util.Logger;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class BSPackage
extends ContentPackage
implements NightbreakManagedContent {
    private static final Map<String, BSPackage> bsPackages = new HashMap<String, BSPackage>();
    private final ContentPackageConfigFields contentPackageConfigFields;
    private boolean outOfDate = false;
    private NightbreakAccount.AccessInfo cachedAccessInfo = null;

    public BSPackage(ContentPackageConfigFields contentPackageConfigFields) {
        this.contentPackageConfigFields = contentPackageConfigFields;
        bsPackages.put(contentPackageConfigFields.getFilename(), this);
    }

    public static void shutdown() {
        bsPackages.clear();
    }

    @Override
    protected void doInstall(Player player) {
        player.closeInventory();
        File folder = this.getSpecificContentFolder();
        if (!folder.exists()) {
            Logger.sendMessage((CommandSender)player, "Failed to find directory " + folder.getAbsolutePath());
            return;
        }
        this.toggleContentState(true);
        ReloadCommand.reload((CommandSender)player);
        Logger.sendMessage((CommandSender)player, "Installed " + this.contentPackageConfigFields.getName());
    }

    @Override
    public void doUninstall(Player player) {
        player.closeInventory();
        File folder = this.getSpecificContentFolder();
        if (!folder.exists()) {
            Logger.sendMessage((CommandSender)player, "Failed to find directory " + folder.getAbsolutePath());
            return;
        }
        this.toggleContentState(false);
        ReloadCommand.reload((CommandSender)player);
        Logger.sendMessage((CommandSender)player, "Uninstalled " + this.contentPackageConfigFields.getName());
    }

    @Override
    public void doDownload(Player player) {
        player.closeInventory();
        String slug = this.getNightbreakSlug();
        if (slug == null || slug.isEmpty()) {
            NightbreakSetupMenuHelper.sendManualDownloadMessage((CommandSender)player, this.contentPackageConfigFields.getDownloadLink(), "BetterStructures imports", "/bs reload");
            return;
        }
        if (!NightbreakAccount.hasToken()) {
            NightbreakSetupMenuHelper.sendNoTokenPrompt(player, "BetterStructures", "https://nightbreak.io/plugin/betterstructures/");
            return;
        }
        Logger.sendSimpleMessage((CommandSender)player, "&aChecking Nightbreak access for &2" + this.contentPackageConfigFields.getName() + "&a...");
        JavaPlugin plugin = (JavaPlugin)MetadataHandler.PLUGIN;
        NightbreakContentManager.checkAccessAsync(plugin, slug, accessInfo -> {
            this.cachedAccessInfo = accessInfo;
            if (!player.isOnline()) {
                return;
            }
            if (accessInfo == null) {
                Logger.sendSimpleMessage((CommandSender)player, "&cFailed to contact Nightbreak for access information.");
                return;
            }
            if (!accessInfo.hasAccess) {
                this.doShowAccessInfo(player);
                return;
            }
            File importsFolder = new File(plugin.getDataFolder(), "imports");
            if (!importsFolder.exists()) {
                importsFolder.mkdirs();
            }
            NightbreakContentManager.downloadAsync(plugin, slug, importsFolder, player, success -> {
                if (!player.isOnline()) {
                    return;
                }
                if (!success.booleanValue()) {
                    return;
                }
                Logger.sendSimpleMessage((CommandSender)player, "&aReloading BetterStructures so the new content is picked up...");
                ReloadCommand.reload((CommandSender)player);
            });
        });
    }

    @Override
    protected void doShowAccessInfo(Player player) {
        NightbreakSetupMenuHelper.sendAccessInfo(player, this.contentPackageConfigFields.getName(), this.cachedAccessInfo, "https://nightbreak.io/plugin/betterstructures/");
    }

    @Override
    protected ItemStack getInstalledItemStack() {
        return NightbreakSetupMenuHelper.createInstalledItem(this.contentPackageConfigFields.getName(), this.contentPackageConfigFields.getDescription());
    }

    @Override
    protected ItemStack getPartiallyInstalledItemStack() {
        return NightbreakSetupMenuHelper.createPartiallyInstalledItem(this.contentPackageConfigFields.getName(), this.contentPackageConfigFields.getDescription());
    }

    @Override
    protected ItemStack getNotInstalledItemStack() {
        return NightbreakSetupMenuHelper.createNotInstalledItem(this.contentPackageConfigFields.getName(), this.contentPackageConfigFields.getDescription());
    }

    @Override
    protected ItemStack getNotDownloadedItemStack() {
        return NightbreakSetupMenuHelper.createNotDownloadedItem(this.contentPackageConfigFields.getName(), this.contentPackageConfigFields.getDescription(), this.getNightbreakSlug(), this.cachedAccessInfo);
    }

    @Override
    protected ItemStack getNeedsAccessItemStack() {
        return NightbreakSetupMenuHelper.createNeedsAccessItem(this.contentPackageConfigFields.getName(), this.contentPackageConfigFields.getDescription(), this.cachedAccessInfo);
    }

    @Override
    protected ItemStack getOutOfDateUpdatableItemStack() {
        return NightbreakSetupMenuHelper.createOutOfDateUpdatableItem(this.contentPackageConfigFields.getName(), this.contentPackageConfigFields.getDescription(), this.getNightbreakSlug());
    }

    @Override
    protected ItemStack getOutOfDateNoAccessItemStack() {
        return NightbreakSetupMenuHelper.createOutOfDateNoAccessItem(this.contentPackageConfigFields.getName(), this.contentPackageConfigFields.getDescription());
    }

    @Override
    protected ContentPackage.ContentState getContentState() {
        boolean hasNightbreakSlug;
        boolean downloaded = this.isDownloaded();
        boolean installed = this.isInstalled();
        boolean bl = hasNightbreakSlug = this.getNightbreakSlug() != null && !this.getNightbreakSlug().isEmpty();
        if (installed && this.outOfDate) {
            if (hasNightbreakSlug && NightbreakAccount.hasToken() && this.cachedAccessInfo != null && !this.cachedAccessInfo.hasAccess) {
                return ContentPackage.ContentState.OUT_OF_DATE_NO_ACCESS;
            }
            return ContentPackage.ContentState.OUT_OF_DATE_UPDATABLE;
        }
        if (installed) {
            return ContentPackage.ContentState.INSTALLED;
        }
        if (downloaded) {
            return ContentPackage.ContentState.NOT_INSTALLED;
        }
        if (hasNightbreakSlug && NightbreakAccount.hasToken() && this.cachedAccessInfo != null && !this.cachedAccessInfo.hasAccess) {
            return ContentPackage.ContentState.NEEDS_ACCESS;
        }
        return ContentPackage.ContentState.NOT_DOWNLOADED;
    }

    private void toggleContentState(boolean enabled) {
        File folder;
        File[] files;
        if (this.contentPackageConfigFields.getContentPackageType() != ContentPackageConfigFields.ContentPackageType.MODULAR && (files = (folder = this.getSpecificContentFolder()).listFiles()) != null) {
            for (File file : files) {
                SchematicConfigField schematicConfigField;
                if (!file.getName().endsWith(".yml") || (schematicConfigField = SchematicConfig.getSchematicConfiguration(file.getName())) == null) continue;
                schematicConfigField.toggleEnabled(enabled);
            }
        }
        this.contentPackageConfigFields.setEnabledAndSave(enabled);
    }

    private File getSpecificContentFolder() {
        String baseFolder = this.contentPackageConfigFields.getContentPackageType() == ContentPackageConfigFields.ContentPackageType.MODULAR ? "modules" : "schematics";
        return new File(MetadataHandler.PLUGIN.getDataFolder(), baseFolder + File.separatorChar + this.contentPackageConfigFields.getFolderName());
    }

    @Override
    public String getNightbreakSlug() {
        return this.contentPackageConfigFields.getNightbreakSlug();
    }

    @Override
    public String getDisplayName() {
        return this.contentPackageConfigFields.getName();
    }

    @Override
    public String getDownloadLink() {
        return this.contentPackageConfigFields.getDownloadLink();
    }

    @Override
    public int getLocalVersion() {
        return this.contentPackageConfigFields.getVersion();
    }

    @Override
    public boolean isInstalled() {
        return this.isDownloaded() && this.contentPackageConfigFields.isEnabled();
    }

    @Override
    public boolean isDownloaded() {
        return this.getSpecificContentFolder().exists();
    }

    public static Map<String, BSPackage> getBsPackages() {
        return bsPackages;
    }

    public ContentPackageConfigFields getContentPackageConfigFields() {
        return this.contentPackageConfigFields;
    }

    @Override
    public boolean isOutOfDate() {
        return this.outOfDate;
    }

    @Override
    public void setOutOfDate(boolean outOfDate) {
        this.outOfDate = outOfDate;
    }

    @Override
    public NightbreakAccount.AccessInfo getCachedAccessInfo() {
        return this.cachedAccessInfo;
    }

    @Override
    public void setCachedAccessInfo(NightbreakAccount.AccessInfo cachedAccessInfo) {
        this.cachedAccessInfo = cachedAccessInfo;
    }
}

