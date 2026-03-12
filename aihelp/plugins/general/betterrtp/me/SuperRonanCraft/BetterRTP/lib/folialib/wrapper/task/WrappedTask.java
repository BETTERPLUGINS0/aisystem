package me.SuperRonanCraft.BetterRTP.lib.folialib.wrapper.task;

import org.bukkit.plugin.Plugin;

public interface WrappedTask {
   void cancel();

   boolean isCancelled();

   Plugin getOwningPlugin();
}
