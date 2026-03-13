/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 */
package com.magmaguy.magmacore.nightbreak;

import com.magmaguy.magmacore.nightbreak.NightbreakAccount;
import com.magmaguy.magmacore.nightbreak.NightbreakContentManager;
import com.magmaguy.magmacore.nightbreak.NightbreakManagedContent;
import com.magmaguy.magmacore.util.Logger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class NightbreakContentRefresher {
    private NightbreakContentRefresher() {
    }

    public static <T extends NightbreakManagedContent> void refreshAsync(JavaPlugin plugin, Collection<T> packages, Predicate<T> shouldCheckVersion, Consumer<List<T>> onComplete) {
        Bukkit.getScheduler().runTaskAsynchronously((Plugin)plugin, () -> {
            LinkedHashMap<String, NightbreakAccount.VersionInfo> versionInfoBySlug = new LinkedHashMap<String, NightbreakAccount.VersionInfo>();
            NightbreakAccount account = NightbreakAccount.getInstance();
            if (account != null) {
                versionInfoBySlug.putAll(account.getAllVersions());
            }
            if (!versionInfoBySlug.isEmpty()) {
                NightbreakContentManager.getVersionCache().clear();
                NightbreakContentManager.getVersionCache().putAll(versionInfoBySlug);
            }
            LinkedHashMap<String, NightbreakAccount.AccessInfo> accessInfoBySlug = new LinkedHashMap<String, NightbreakAccount.AccessInfo>();
            ArrayList<NightbreakManagedContent> newlyOutdated = new ArrayList<NightbreakManagedContent>();
            for (NightbreakManagedContent contentPackage : packages) {
                boolean outOfDate;
                NightbreakAccount.VersionInfo versionInfo;
                String slug = contentPackage.getNightbreakSlug();
                if (slug == null || slug.isEmpty()) continue;
                if (NightbreakAccount.hasToken()) {
                    NightbreakAccount.AccessInfo accessInfo = accessInfoBySlug.computeIfAbsent(slug, key -> {
                        NightbreakAccount.AccessInfo fetched = NightbreakAccount.getInstance().checkAccess((String)key);
                        if (fetched != null) {
                            NightbreakContentManager.getAccessCache().put((String)key, fetched);
                        }
                        return fetched;
                    });
                    contentPackage.setCachedAccessInfo(accessInfo);
                } else {
                    contentPackage.setCachedAccessInfo(null);
                }
                if (!shouldCheckVersion.test(contentPackage) || !contentPackage.isInstalled() || (versionInfo = (NightbreakAccount.VersionInfo)versionInfoBySlug.get(slug)) == null) continue;
                boolean bl = outOfDate = versionInfo.versionInt > contentPackage.getLocalVersion();
                if (outOfDate && !contentPackage.isOutOfDate()) {
                    newlyOutdated.add(contentPackage);
                    Logger.warn("Content " + contentPackage.getDisplayName() + " is outdated! Your version: " + contentPackage.getLocalVersion() + " / remote version: " + versionInfo.versionInt + " / link: " + contentPackage.getDownloadLink());
                }
                contentPackage.setOutOfDate(outOfDate);
            }
            Bukkit.getScheduler().runTask((Plugin)plugin, () -> onComplete.accept(newlyOutdated));
        });
    }
}

