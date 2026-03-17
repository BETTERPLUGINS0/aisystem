package com.bergerkiller.bukkit.tc.pathfinding;

import com.bergerkiller.bukkit.common.component.LibraryComponent;
import com.bergerkiller.bukkit.common.config.FileConfiguration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RouteManager implements LibraryComponent {
   private final FileConfiguration config;
   private boolean changed;

   public RouteManager(String configFileName) {
      this.config = new FileConfiguration(configFileName);
      this.changed = false;
   }

   public void enable() {
      this.load();
   }

   public void disable() {
   }

   public void load() {
      this.config.load();
      this.config.setHeader("This file stores lists of destinations that can be set as a route on trains or carts");
      if (!this.config.exists()) {
         this.config.save();
      }

      this.changed = false;
   }

   public void save(boolean autosave) {
      if (this.changed || !autosave) {
         this.changed = false;
         this.config.save();
      }

   }

   public List<String> getRouteNames() {
      return new ArrayList(this.config.getKeys());
   }

   public List<String> findRoute(String name) {
      return this.config.contains(name) ? Collections.unmodifiableList(this.config.getList(name, String.class)) : Collections.emptyList();
   }

   public void storeRoute(String name, List<String> route) {
      if (route != null && !route.isEmpty()) {
         this.config.set(name, route);
      } else {
         this.config.remove(name);
      }

      this.changed = true;
   }
}
