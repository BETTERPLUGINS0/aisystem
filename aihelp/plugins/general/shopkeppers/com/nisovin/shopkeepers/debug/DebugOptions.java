package com.nisovin.shopkeepers.debug;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public final class DebugOptions {
   private static final Set<String> allOptions = new LinkedHashSet();
   private static final Set<? extends String> allOptionsView;
   public static final String logAllEvents;
   public static final String printListeners;
   public static final String shopkeeperActivation;
   public static final String regularTickActivities;
   public static final String visualizeShopkeeperTicks;
   public static final String commands;
   public static final String ownerNameUpdates;
   public static final String itemMigrations;
   public static final String itemUpdates;
   public static final String emptyTrades;
   public static final String textComponents;
   public static final String unsafeTeleports;

   private static String add(String debugOption) {
      allOptions.add(debugOption);
      return debugOption;
   }

   public static Set<? extends String> getAll() {
      return allOptionsView;
   }

   private DebugOptions() {
   }

   static {
      allOptionsView = Collections.unmodifiableSet(allOptions);
      logAllEvents = add("log-all-events");
      printListeners = add("print-listeners");
      shopkeeperActivation = add("shopkeeper-activation");
      regularTickActivities = add("regular-tick-activities");
      visualizeShopkeeperTicks = add("visualize-shopkeeper-ticks");
      commands = add("commands");
      ownerNameUpdates = add("owner-name-updates");
      itemMigrations = add("item-migrations");
      itemUpdates = add("item-updates");
      emptyTrades = add("empty-trades");
      textComponents = add("text-components");
      unsafeTeleports = add("unsafe-teleports");
   }
}
