package me.PM2.infinitevehicles.commands;

import org.bukkit.Bukkit;

/** @deprecated */
@Deprecated
public class ACFFoliaScheduler extends ACFPaperScheduler {
   public ACFFoliaScheduler() {
      super(Bukkit.getAsyncScheduler());
   }
}
