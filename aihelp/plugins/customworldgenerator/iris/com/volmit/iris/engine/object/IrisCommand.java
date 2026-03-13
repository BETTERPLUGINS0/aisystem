package com.volmit.iris.engine.object;

import com.volmit.iris.Iris;
import com.volmit.iris.engine.object.annotations.ArrayType;
import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.Required;
import com.volmit.iris.engine.object.annotations.Snippet;
import com.volmit.iris.util.collection.KList;
import java.util.Iterator;
import lombok.Generated;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

@Snippet("command")
@Desc("Represents a set of Iris commands")
public class IrisCommand {
   @Required
   @ArrayType(
      min = 1,
      type = String.class
   )
   @Desc("List of commands. Iris replaces {x} {y} and {z} with the location of the entity spawn")
   private KList<String> commands = new KList();
   @Desc("The delay for running the command. Instant by default")
   private long delay = 0L;
   @Desc("If this should be repeated (indefinitely, cannot be cancelled). This does not persist with server-restarts, so it only repeats when the chunk is generated.")
   private boolean repeat = false;
   @Desc("The delay between repeats, in server ticks (by default 100, so 5 seconds)")
   private long repeatDelay = 100L;
   @Desc("The block of 24 hour time in which the command should execute.")
   private IrisTimeBlock timeBlock = new IrisTimeBlock();
   @Desc("The weather that is required for the command to execute.")
   private IrisWeather weather;

   public boolean isValid(World world) {
      return this.timeBlock.isWithin(var1) && this.weather.is(var1);
   }

   public void run(Location at) {
      if (this.isValid(var1.getWorld())) {
         Iterator var2 = this.commands.iterator();

         while(var2.hasNext()) {
            String var3 = (String)var2.next();
            var3 = (var3.startsWith("/") ? var3.replaceFirst("/", "") : var3).replaceAll("\\Q{x}\\E", String.valueOf(var1.getBlockX())).replaceAll("\\Q{y}\\E", String.valueOf(var1.getBlockY())).replaceAll("\\Q{z}\\E", String.valueOf(var1.getBlockZ()));
            if (this.repeat) {
               Bukkit.getScheduler().scheduleSyncRepeatingTask(Iris.instance, () -> {
                  Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), var3);
               }, this.delay, this.repeatDelay);
            } else {
               Bukkit.getScheduler().scheduleSyncDelayedTask(Iris.instance, () -> {
                  Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), var3);
               }, this.delay);
            }
         }

      }
   }

   @Generated
   public IrisCommand() {
      this.weather = IrisWeather.ANY;
   }

   @Generated
   public KList<String> getCommands() {
      return this.commands;
   }

   @Generated
   public long getDelay() {
      return this.delay;
   }

   @Generated
   public boolean isRepeat() {
      return this.repeat;
   }

   @Generated
   public long getRepeatDelay() {
      return this.repeatDelay;
   }

   @Generated
   public IrisTimeBlock getTimeBlock() {
      return this.timeBlock;
   }

   @Generated
   public IrisWeather getWeather() {
      return this.weather;
   }

   @Generated
   public IrisCommand setCommands(final KList<String> commands) {
      this.commands = var1;
      return this;
   }

   @Generated
   public IrisCommand setDelay(final long delay) {
      this.delay = var1;
      return this;
   }

   @Generated
   public IrisCommand setRepeat(final boolean repeat) {
      this.repeat = var1;
      return this;
   }

   @Generated
   public IrisCommand setRepeatDelay(final long repeatDelay) {
      this.repeatDelay = var1;
      return this;
   }

   @Generated
   public IrisCommand setTimeBlock(final IrisTimeBlock timeBlock) {
      this.timeBlock = var1;
      return this;
   }

   @Generated
   public IrisCommand setWeather(final IrisWeather weather) {
      this.weather = var1;
      return this;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisCommand)) {
         return false;
      } else {
         IrisCommand var2 = (IrisCommand)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.getDelay() != var2.getDelay()) {
            return false;
         } else if (this.isRepeat() != var2.isRepeat()) {
            return false;
         } else if (this.getRepeatDelay() != var2.getRepeatDelay()) {
            return false;
         } else {
            label54: {
               KList var3 = this.getCommands();
               KList var4 = var2.getCommands();
               if (var3 == null) {
                  if (var4 == null) {
                     break label54;
                  }
               } else if (var3.equals(var4)) {
                  break label54;
               }

               return false;
            }

            IrisTimeBlock var5 = this.getTimeBlock();
            IrisTimeBlock var6 = var2.getTimeBlock();
            if (var5 == null) {
               if (var6 != null) {
                  return false;
               }
            } else if (!var5.equals(var6)) {
               return false;
            }

            IrisWeather var7 = this.getWeather();
            IrisWeather var8 = var2.getWeather();
            if (var7 == null) {
               if (var8 != null) {
                  return false;
               }
            } else if (!var7.equals(var8)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof IrisCommand;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      long var3 = this.getDelay();
      int var10 = var2 * 59 + (int)(var3 >>> 32 ^ var3);
      var10 = var10 * 59 + (this.isRepeat() ? 79 : 97);
      long var5 = this.getRepeatDelay();
      var10 = var10 * 59 + (int)(var5 >>> 32 ^ var5);
      KList var7 = this.getCommands();
      var10 = var10 * 59 + (var7 == null ? 43 : var7.hashCode());
      IrisTimeBlock var8 = this.getTimeBlock();
      var10 = var10 * 59 + (var8 == null ? 43 : var8.hashCode());
      IrisWeather var9 = this.getWeather();
      var10 = var10 * 59 + (var9 == null ? 43 : var9.hashCode());
      return var10;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getCommands());
      return "IrisCommand(commands=" + var10000 + ", delay=" + this.getDelay() + ", repeat=" + this.isRepeat() + ", repeatDelay=" + this.getRepeatDelay() + ", timeBlock=" + String.valueOf(this.getTimeBlock()) + ", weather=" + String.valueOf(this.getWeather()) + ")";
   }
}
