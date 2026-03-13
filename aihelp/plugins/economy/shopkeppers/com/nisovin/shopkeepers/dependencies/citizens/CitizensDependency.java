package com.nisovin.shopkeepers.dependencies.citizens;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class CitizensDependency {
   public static final String PLUGIN_NAME = "Citizens";

   @Nullable
   public static Plugin getPlugin() {
      return Bukkit.getPluginManager().getPlugin("Citizens");
   }

   public static boolean isPluginEnabled() {
      return Bukkit.getPluginManager().isPluginEnabled("Citizens");
   }

   private CitizensDependency() {
   }
}
