package com.volmit.iris.engine.object;

import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.Snippet;
import com.volmit.iris.util.math.RNG;
import lombok.Generated;

@Snippet("color")
@Desc("Represents a color")
public class IrisSeed {
   @Desc("The seed to use")
   private long seed = 1337L;
   @Desc("To calculate a seed Iris passes in it's natural seed for the current feature, then mixes it with your seed. Setting this to true ignores the parent seed and always uses your exact seed ignoring the input of Iris feature seeds. You can use this to match seeds on other generators.")
   private boolean ignoreNaturalSeedInput = false;

   public long getSeed(long seed) {
      return var1 * 47L + this.getSeed() + 29334667L;
   }

   public RNG rng(long inseed) {
      return new RNG(this.getSeed(var1));
   }

   @Generated
   public long getSeed() {
      return this.seed;
   }

   @Generated
   public boolean isIgnoreNaturalSeedInput() {
      return this.ignoreNaturalSeedInput;
   }

   @Generated
   public IrisSeed setSeed(final long seed) {
      this.seed = var1;
      return this;
   }

   @Generated
   public IrisSeed setIgnoreNaturalSeedInput(final boolean ignoreNaturalSeedInput) {
      this.ignoreNaturalSeedInput = var1;
      return this;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisSeed)) {
         return false;
      } else {
         IrisSeed var2 = (IrisSeed)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.getSeed() != var2.getSeed()) {
            return false;
         } else {
            return this.isIgnoreNaturalSeedInput() == var2.isIgnoreNaturalSeedInput();
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof IrisSeed;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      long var3 = this.getSeed();
      int var5 = var2 * 59 + (int)(var3 >>> 32 ^ var3);
      var5 = var5 * 59 + (this.isIgnoreNaturalSeedInput() ? 79 : 97);
      return var5;
   }

   @Generated
   public String toString() {
      long var10000 = this.getSeed();
      return "IrisSeed(seed=" + var10000 + ", ignoreNaturalSeedInput=" + this.isIgnoreNaturalSeedInput() + ")";
   }
}
