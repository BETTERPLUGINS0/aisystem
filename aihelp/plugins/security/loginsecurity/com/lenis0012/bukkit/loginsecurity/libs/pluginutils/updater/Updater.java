package com.lenis0012.bukkit.loginsecurity.libs.pluginutils.updater;

import java.util.concurrent.CompletableFuture;
import org.bukkit.entity.Player;

public interface Updater {
   boolean isUpdateAvailable();

   Version getLatestVersion();

   CompletableFuture<InstalledVersion> downloadLatestVersion();

   void notifyIfUpdateAvailable(Player var1);
}
