/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  ch.njol.skript.Skript
 *  ch.njol.skript.SkriptAddon
 *  org.bukkit.Bukkit
 *  org.bukkit.plugin.java.JavaPlugin
 */
package nl.mtvehicles.core.infrastructure.dependencies;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import java.io.IOException;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.enums.SoftDependency;
import nl.mtvehicles.core.infrastructure.modules.DependencyModule;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class SkriptUtils {
    private static SkriptAddon addonInstance;

    public static void load() {
        try {
            SkriptUtils.getAddonInstance().loadClasses("nl.mtvehicles.core.infrastructure.dependencies.skript", new String[]{"types", "effects", "expressions", "events", "conditions"});
        } catch (IOException e) {
            Bukkit.getLogger().severe("[MTVehicles] An error occurred whilst loading Skript as a soft-dependency. (Make sure you're using the latest version, or try restarting the server.)");
            DependencyModule.loadedDependencies.remove((Object)SoftDependency.SKRIPT);
        }
    }

    public static SkriptAddon getAddonInstance() {
        if (addonInstance == null) {
            addonInstance = Skript.registerAddon((JavaPlugin)Main.instance);
        }
        return addonInstance;
    }
}

