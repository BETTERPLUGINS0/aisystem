/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package com.magmaguy.magmacore.initialization;

import com.magmaguy.magmacore.initialization.PluginInitializationConfig;
import com.magmaguy.magmacore.initialization.PluginInitializationContext;
import com.magmaguy.magmacore.initialization.PluginInitializationProgressBar;
import com.magmaguy.magmacore.initialization.PluginInitializationState;
import com.magmaguy.magmacore.util.Logger;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class PluginInitializationManager {
    private static final Map<String, PluginInitializationState> pluginStates = new ConcurrentHashMap<String, PluginInitializationState>();
    private static final Map<String, Boolean> shutdownRequests = new ConcurrentHashMap<String, Boolean>();

    private PluginInitializationManager() {
    }

    public static void run(JavaPlugin plugin, PluginInitializationConfig config, Consumer<PluginInitializationContext> asyncInitialization, Consumer<PluginInitializationContext> syncInitialization, Runnable onSuccess, Consumer<Throwable> onFailure) {
        shutdownRequests.put(plugin.getName(), false);
        PluginInitializationManager.setState(plugin, PluginInitializationState.INITIALIZING);
        PluginInitializationProgressBar.start(plugin, config.displayName(), config.adminPermission(), config.totalSteps());
        PluginInitializationContext context = new PluginInitializationContext(plugin, config);
        List presentDependencies = config.resolveDependencies(plugin).stream().filter(PluginInitializationManager::isDependencyPresent).toList();
        if (presentDependencies.isEmpty()) {
            PluginInitializationManager.runAsyncPhase(plugin, context, asyncInitialization, syncInitialization, onSuccess, onFailure);
            return;
        }
        context.status("Waiting for " + String.join((CharSequence)", ", presentDependencies));
        PluginInitializationManager.waitForDependencies(plugin, context, presentDependencies, () -> PluginInitializationManager.runAsyncPhase(plugin, context, asyncInitialization, syncInitialization, onSuccess, onFailure));
    }

    public static PluginInitializationState getState(String pluginName) {
        return pluginStates.getOrDefault(pluginName, PluginInitializationState.UNINITIALIZED);
    }

    public static boolean isPluginReady(String pluginName) {
        Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
        if (plugin == null || !plugin.isEnabled()) {
            return true;
        }
        PluginInitializationState state = pluginStates.get(pluginName);
        if (state == null) {
            return true;
        }
        return state != PluginInitializationState.INITIALIZING;
    }

    public static boolean isShutdownRequested(JavaPlugin plugin) {
        return shutdownRequests.getOrDefault(plugin.getName(), false);
    }

    public static void requestShutdown(JavaPlugin plugin) {
        shutdownRequests.put(plugin.getName(), true);
    }

    public static void shutdown(JavaPlugin plugin) {
        shutdownRequests.put(plugin.getName(), true);
        PluginInitializationProgressBar.complete(plugin);
        PluginInitializationManager.setState(plugin, PluginInitializationState.UNINITIALIZED);
    }

    public static void setState(JavaPlugin plugin, PluginInitializationState state) {
        pluginStates.put(plugin.getName(), state);
    }

    private static boolean isDependencyPresent(String dependencyName) {
        Plugin plugin = Bukkit.getPluginManager().getPlugin(dependencyName);
        return plugin != null && plugin.isEnabled();
    }

    private static void waitForDependencies(final JavaPlugin plugin, final PluginInitializationContext context, final List<String> dependencies, final Runnable onReady) {
        new BukkitRunnable(){
            private String lastDescription = "";

            public void run() {
                if (PluginInitializationManager.isShutdownRequested(plugin) || !plugin.isEnabled()) {
                    this.cancel();
                    return;
                }
                List pendingDependencies = dependencies.stream().filter(dependency -> !PluginInitializationManager.isPluginReady(dependency)).collect(Collectors.toList());
                if (pendingDependencies.isEmpty()) {
                    this.cancel();
                    onReady.run();
                    return;
                }
                String description = "Waiting for " + String.join((CharSequence)", ", pendingDependencies);
                if (!description.equals(this.lastDescription)) {
                    this.lastDescription = description;
                    context.status(description);
                }
            }
        }.runTaskTimerAsynchronously((Plugin)plugin, 0L, 10L);
    }

    private static void runAsyncPhase(JavaPlugin plugin, PluginInitializationContext context, Consumer<PluginInitializationContext> asyncInitialization, Consumer<PluginInitializationContext> syncInitialization, Runnable onSuccess, Consumer<Throwable> onFailure) {
        Bukkit.getScheduler().runTaskAsynchronously((Plugin)plugin, () -> {
            try {
                asyncInitialization.accept(context);
                if (PluginInitializationManager.isShutdownRequested(plugin)) {
                    return;
                }
                Bukkit.getScheduler().runTask((Plugin)plugin, () -> PluginInitializationManager.runSyncPhase(plugin, context, syncInitialization, onSuccess, onFailure));
            } catch (Throwable throwable) {
                Bukkit.getScheduler().runTask((Plugin)plugin, () -> PluginInitializationManager.fail(plugin, throwable, onFailure));
            }
        });
    }

    private static void runSyncPhase(JavaPlugin plugin, PluginInitializationContext context, Consumer<PluginInitializationContext> syncInitialization, Runnable onSuccess, Consumer<Throwable> onFailure) {
        try {
            if (PluginInitializationManager.isShutdownRequested(plugin)) {
                return;
            }
            syncInitialization.accept(context);
            if (PluginInitializationManager.isShutdownRequested(plugin)) {
                return;
            }
            PluginInitializationProgressBar.complete(plugin);
            PluginInitializationManager.setState(plugin, PluginInitializationState.INITIALIZED);
            onSuccess.run();
        } catch (Throwable throwable) {
            PluginInitializationManager.fail(plugin, throwable, onFailure);
        }
    }

    private static void fail(JavaPlugin plugin, Throwable throwable, Consumer<Throwable> onFailure) {
        PluginInitializationProgressBar.complete(plugin);
        PluginInitializationManager.setState(plugin, PluginInitializationState.FAILED);
        Logger.warn(plugin.getName() + " initialization failed!");
        onFailure.accept(throwable);
    }
}

