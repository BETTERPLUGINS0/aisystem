package com.volmit.iris.engine.object;

import com.volmit.iris.engine.object.annotations.ArrayType;
import com.volmit.iris.engine.object.annotations.DependsOn;
import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.MaxNumber;
import com.volmit.iris.engine.object.annotations.MinNumber;
import com.volmit.iris.engine.object.annotations.Required;
import com.volmit.iris.engine.object.annotations.Snippet;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.math.RNG;
import java.util.Iterator;
import lombok.Generated;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@Snippet("command-registry")
@Desc("Represents a casting location for a command")
public class IrisCommandRegistry {
   @Required
   @ArrayType(
      min = 1,
      type = IrisCommand.class
   )
   @Desc("Run commands, at the exact location of the player")
   private KList<IrisCommand> rawCommands = new KList();
   @DependsOn({"rawCommands"})
   @MinNumber(-8.0D)
   @MaxNumber(8.0D)
   @Desc("The alt x, usually represents motion if the particle count is zero. Otherwise an offset.")
   private double commandOffsetX = 0.0D;
   @DependsOn({"rawCommands"})
   @MinNumber(-8.0D)
   @MaxNumber(8.0D)
   @Desc("The alt y, usually represents motion if the particle count is zero. Otherwise an offset.")
   private double commandOffsetY = 0.0D;
   @DependsOn({"rawCommands"})
   @MinNumber(-8.0D)
   @MaxNumber(8.0D)
   @Desc("The alt z, usually represents motion if the particle count is zero. Otherwise an offset.")
   private double commandOffsetZ = 0.0D;
   @DependsOn({"rawCommands"})
   @Desc("Randomize the altX from -altX to altX")
   private boolean commandRandomAltX = true;
   @DependsOn({"rawCommands"})
   @Desc("Randomize the altY from -altY to altY")
   private boolean commandRandomAltY = false;
   @DependsOn({"rawCommands"})
   @Desc("Randomize the altZ from -altZ to altZ")
   private boolean commandRandomAltZ = true;
   @DependsOn({"rawCommands"})
   @Desc("Randomize location for all separate commands (true), or run all on the same location (false)")
   private boolean commandAllRandomLocations = true;

   public void run(Player p) {
      if (this.rawCommands.isNotEmpty()) {
         Location var2 = var1.getLocation().clone().add(this.commandRandomAltX ? RNG.r.d(-this.commandOffsetX, this.commandOffsetX) : this.commandOffsetX, this.commandRandomAltY ? RNG.r.d(-this.commandOffsetY, this.commandOffsetY) : this.commandOffsetY, this.commandRandomAltZ ? RNG.r.d(-this.commandOffsetZ, this.commandOffsetZ) : this.commandOffsetZ);
         Iterator var3 = this.rawCommands.iterator();

         while(var3.hasNext()) {
            IrisCommand var4 = (IrisCommand)var3.next();
            var4.run(var2);
            if (this.commandAllRandomLocations) {
               var2 = var1.getLocation().clone().add(this.commandRandomAltX ? RNG.r.d(-this.commandOffsetX, this.commandOffsetX) : this.commandOffsetX, this.commandRandomAltY ? RNG.r.d(-this.commandOffsetY, this.commandOffsetY) : this.commandOffsetY, this.commandRandomAltZ ? RNG.r.d(-this.commandOffsetZ, this.commandOffsetZ) : this.commandOffsetZ);
            }
         }
      }

   }

   @Generated
   public KList<IrisCommand> getRawCommands() {
      return this.rawCommands;
   }

   @Generated
   public double getCommandOffsetX() {
      return this.commandOffsetX;
   }

   @Generated
   public double getCommandOffsetY() {
      return this.commandOffsetY;
   }

   @Generated
   public double getCommandOffsetZ() {
      return this.commandOffsetZ;
   }

   @Generated
   public boolean isCommandRandomAltX() {
      return this.commandRandomAltX;
   }

   @Generated
   public boolean isCommandRandomAltY() {
      return this.commandRandomAltY;
   }

   @Generated
   public boolean isCommandRandomAltZ() {
      return this.commandRandomAltZ;
   }

   @Generated
   public boolean isCommandAllRandomLocations() {
      return this.commandAllRandomLocations;
   }

   @Generated
   public IrisCommandRegistry setRawCommands(final KList<IrisCommand> rawCommands) {
      this.rawCommands = var1;
      return this;
   }

   @Generated
   public IrisCommandRegistry setCommandOffsetX(final double commandOffsetX) {
      this.commandOffsetX = var1;
      return this;
   }

   @Generated
   public IrisCommandRegistry setCommandOffsetY(final double commandOffsetY) {
      this.commandOffsetY = var1;
      return this;
   }

   @Generated
   public IrisCommandRegistry setCommandOffsetZ(final double commandOffsetZ) {
      this.commandOffsetZ = var1;
      return this;
   }

   @Generated
   public IrisCommandRegistry setCommandRandomAltX(final boolean commandRandomAltX) {
      this.commandRandomAltX = var1;
      return this;
   }

   @Generated
   public IrisCommandRegistry setCommandRandomAltY(final boolean commandRandomAltY) {
      this.commandRandomAltY = var1;
      return this;
   }

   @Generated
   public IrisCommandRegistry setCommandRandomAltZ(final boolean commandRandomAltZ) {
      this.commandRandomAltZ = var1;
      return this;
   }

   @Generated
   public IrisCommandRegistry setCommandAllRandomLocations(final boolean commandAllRandomLocations) {
      this.commandAllRandomLocations = var1;
      return this;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisCommandRegistry)) {
         return false;
      } else {
         IrisCommandRegistry var2 = (IrisCommandRegistry)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (Double.compare(this.getCommandOffsetX(), var2.getCommandOffsetX()) != 0) {
            return false;
         } else if (Double.compare(this.getCommandOffsetY(), var2.getCommandOffsetY()) != 0) {
            return false;
         } else if (Double.compare(this.getCommandOffsetZ(), var2.getCommandOffsetZ()) != 0) {
            return false;
         } else if (this.isCommandRandomAltX() != var2.isCommandRandomAltX()) {
            return false;
         } else if (this.isCommandRandomAltY() != var2.isCommandRandomAltY()) {
            return false;
         } else if (this.isCommandRandomAltZ() != var2.isCommandRandomAltZ()) {
            return false;
         } else if (this.isCommandAllRandomLocations() != var2.isCommandAllRandomLocations()) {
            return false;
         } else {
            KList var3 = this.getRawCommands();
            KList var4 = var2.getRawCommands();
            if (var3 == null) {
               if (var4 != null) {
                  return false;
               }
            } else if (!var3.equals(var4)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof IrisCommandRegistry;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      long var3 = Double.doubleToLongBits(this.getCommandOffsetX());
      int var10 = var2 * 59 + (int)(var3 >>> 32 ^ var3);
      long var5 = Double.doubleToLongBits(this.getCommandOffsetY());
      var10 = var10 * 59 + (int)(var5 >>> 32 ^ var5);
      long var7 = Double.doubleToLongBits(this.getCommandOffsetZ());
      var10 = var10 * 59 + (int)(var7 >>> 32 ^ var7);
      var10 = var10 * 59 + (this.isCommandRandomAltX() ? 79 : 97);
      var10 = var10 * 59 + (this.isCommandRandomAltY() ? 79 : 97);
      var10 = var10 * 59 + (this.isCommandRandomAltZ() ? 79 : 97);
      var10 = var10 * 59 + (this.isCommandAllRandomLocations() ? 79 : 97);
      KList var9 = this.getRawCommands();
      var10 = var10 * 59 + (var9 == null ? 43 : var9.hashCode());
      return var10;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getRawCommands());
      return "IrisCommandRegistry(rawCommands=" + var10000 + ", commandOffsetX=" + this.getCommandOffsetX() + ", commandOffsetY=" + this.getCommandOffsetY() + ", commandOffsetZ=" + this.getCommandOffsetZ() + ", commandRandomAltX=" + this.isCommandRandomAltX() + ", commandRandomAltY=" + this.isCommandRandomAltY() + ", commandRandomAltZ=" + this.isCommandRandomAltZ() + ", commandAllRandomLocations=" + this.isCommandAllRandomLocations() + ")";
   }
}
