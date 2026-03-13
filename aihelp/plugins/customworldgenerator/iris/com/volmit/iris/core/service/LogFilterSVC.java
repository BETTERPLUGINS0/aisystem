package com.volmit.iris.core.service;

import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.plugin.IrisService;
import java.util.Objects;
import java.util.stream.Stream;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.Filter.Result;
import org.apache.logging.log4j.core.LifeCycle.State;
import org.apache.logging.log4j.message.Message;

public class LogFilterSVC implements IrisService, Filter {
   private static final String HEIGHTMAP_MISMATCH = "Ignoring heightmap data for chunk";
   private static final String RAID_PERSISTENCE = "Could not save data net.minecraft.world.entity.raid.PersistentRaid";
   private static final String DUPLICATE_ENTITY_UUID = "UUID of added entity already exists";
   private static final KList<String> FILTERS = new KList();

   public void onEnable() {
      FILTERS.add((Object[])("Ignoring heightmap data for chunk", "Could not save data net.minecraft.world.entity.raid.PersistentRaid", "UUID of added entity already exists"));
      ((Logger)LogManager.getRootLogger()).addFilter(this);
   }

   public void initialize() {
   }

   public void start() {
   }

   public void stop() {
   }

   public void onDisable() {
   }

   public boolean isStarted() {
      return true;
   }

   public boolean isStopped() {
      return false;
   }

   public State getState() {
      try {
         return State.STARTED;
      } catch (Exception var2) {
         return null;
      }
   }

   public Result getOnMatch() {
      return Result.NEUTRAL;
   }

   public Result getOnMismatch() {
      return Result.NEUTRAL;
   }

   public Result filter(LogEvent event) {
      return this.check(var1.getMessage().getFormattedMessage());
   }

   public Result filter(Logger logger, Level level, Marker marker, Object msg, Throwable t) {
      return this.check(var4.toString());
   }

   public Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) {
      return this.check(var4.getFormattedMessage());
   }

   public Result filter(Logger logger, Level level, Marker marker, String message, Object... params) {
      return this.check(var4);
   }

   public Result filter(Logger logger, Level level, Marker marker, String message, Object p0) {
      return this.check(var4);
   }

   public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1) {
      return this.check(var4);
   }

   public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1, Object p2) {
      return this.check(var4);
   }

   public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3) {
      return this.check(var4);
   }

   public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
      return this.check(var4);
   }

   public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
      return this.check(var4);
   }

   public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
      return this.check(var4);
   }

   public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
      return this.check(var4);
   }

   public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
      return this.check(var4);
   }

   public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
      return this.check(var4);
   }

   private Result check(String string) {
      Stream var10000 = FILTERS.stream();
      Objects.requireNonNull(var1);
      return var10000.anyMatch(var1::contains) ? Result.DENY : Result.NEUTRAL;
   }
}
