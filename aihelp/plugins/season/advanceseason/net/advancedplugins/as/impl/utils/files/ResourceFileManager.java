/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.as.impl.utils.files;

import java.io.File;
import java.security.CodeSource;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.bukkit.plugin.java.JavaPlugin;

public class ResourceFileManager {
    public static void saveAllResources(JavaPlugin javaPlugin, String string, String string2) {
        try {
            CodeSource codeSource;
            File file = new File(javaPlugin.getDataFolder(), string);
            if (!file.exists()) {
                file.mkdirs();
            }
            if ((codeSource = javaPlugin.getClass().getProtectionDomain().getCodeSource()) == null) {
                javaPlugin.getLogger().warning("Could not get CodeSource for plugin");
                return;
            }
            try (ZipInputStream zipInputStream = new ZipInputStream(codeSource.getLocation().openStream());){
                ZipEntry zipEntry;
                int n = 0;
                while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                    File file2;
                    String string3 = zipEntry.getName();
                    if (!string3.startsWith(string + "/")) continue;
                    String string4 = string3.substring(string.length() + 1);
                    if (string2 != null && !string3.endsWith(string2) || (file2 = new File(file, string4)).exists() || file2.isDirectory()) continue;
                    if (!string3.endsWith("/")) {
                        javaPlugin.saveResource(string3, false);
                    }
                    file2.getParentFile().mkdirs();
                    ++n;
                }
                if (n == 0) {
                    // empty if block
                }
            }
        } catch (Exception exception) {
            javaPlugin.getLogger().severe("Failed to save resources from " + string + ": " + exception.getMessage());
        }
    }

    public static void saveAllResourceFolders(JavaPlugin javaPlugin, String[] stringArray, String string) {
        for (String string2 : stringArray) {
            try {
                ResourceFileManager.saveAllResources(javaPlugin, string2, string);
            } catch (Exception exception) {
                javaPlugin.getLogger().warning("Skipping folder '" + string2 + "' due to error: " + exception.getMessage());
            }
        }
    }
}

