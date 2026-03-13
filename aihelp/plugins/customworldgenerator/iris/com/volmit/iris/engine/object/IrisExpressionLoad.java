package com.volmit.iris.engine.object;

import com.volmit.iris.core.loader.IrisData;
import com.volmit.iris.engine.data.cache.AtomicCache;
import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.Required;
import com.volmit.iris.engine.object.annotations.Snippet;
import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.math.RNG;
import com.volmit.iris.util.noise.CNG;
import com.volmit.iris.util.stream.ProceduralStream;
import lombok.Generated;

@Snippet("expression-load")
@Desc("Represents a variable to use in your expression. Do not set the name to x, y, or z, also don't duplicate names.")
public class IrisExpressionLoad {
   @Required
   @Desc("The variable to assign this value to. Do not set the name to x, y, or z")
   private String name = "";
   @Desc("If the style value is not defined, this value will be used")
   private double staticValue = -1.0D;
   @Desc("If defined, this variable will use a generator style as it's value")
   private IrisGeneratorStyle styleValue = null;
   @Desc("If defined, iris will use an internal stream from the engine as it's value")
   private IrisEngineStreamType engineStreamValue = null;
   @Desc("If defined, iris will use an internal value from the engine as it's value")
   private IrisEngineValueType engineValue = null;
   private transient AtomicCache<ProceduralStream<Double>> streamCache = new AtomicCache();
   private transient AtomicCache<Double> valueCache = new AtomicCache();
   private final transient KMap<Long, CNG> styleCache = new KMap();

   public double getValue(RNG rng, IrisData data, double x, double z) {
      if (this.engineValue != null) {
         return (Double)this.valueCache.aquire(() -> {
            return this.engineValue.get(var2.getEngine());
         });
      } else if (this.engineStreamValue != null) {
         return (Double)((ProceduralStream)this.streamCache.aquire(() -> {
            return this.engineStreamValue.get(var2.getEngine());
         })).get(var3, var5);
      } else {
         return this.styleValue != null ? ((CNG)this.styleCache.computeIfAbsent(var1.getSeed(), (var2x) -> {
            return this.styleValue.createNoCache(new RNG(var2x), var2);
         })).noise(var3, var5) : this.staticValue;
      }
   }

   public double getValue(RNG rng, IrisData data, double x, double y, double z) {
      if (this.engineValue != null) {
         return (Double)this.valueCache.aquire(() -> {
            return this.engineValue.get(var2.getEngine());
         });
      } else if (this.engineStreamValue != null) {
         return (Double)((ProceduralStream)this.streamCache.aquire(() -> {
            return this.engineStreamValue.get(var2.getEngine());
         })).get(var3, var7);
      } else {
         return this.styleValue != null ? ((CNG)this.styleCache.computeIfAbsent(var1.getSeed(), (var2x) -> {
            return this.styleValue.createNoCache(new RNG(var2x), var2);
         })).noise(var3, var5, var7) : this.staticValue;
      }
   }

   @Generated
   public IrisExpressionLoad() {
   }

   @Generated
   public IrisExpressionLoad(final String name, final double staticValue, final IrisGeneratorStyle styleValue, final IrisEngineStreamType engineStreamValue, final IrisEngineValueType engineValue, final AtomicCache<ProceduralStream<Double>> streamCache, final AtomicCache<Double> valueCache) {
      this.name = var1;
      this.staticValue = var2;
      this.styleValue = var4;
      this.engineStreamValue = var5;
      this.engineValue = var6;
      this.streamCache = var7;
      this.valueCache = var8;
   }

   @Generated
   public String getName() {
      return this.name;
   }

   @Generated
   public double getStaticValue() {
      return this.staticValue;
   }

   @Generated
   public IrisGeneratorStyle getStyleValue() {
      return this.styleValue;
   }

   @Generated
   public IrisEngineStreamType getEngineStreamValue() {
      return this.engineStreamValue;
   }

   @Generated
   public IrisEngineValueType getEngineValue() {
      return this.engineValue;
   }

   @Generated
   public AtomicCache<ProceduralStream<Double>> getStreamCache() {
      return this.streamCache;
   }

   @Generated
   public AtomicCache<Double> getValueCache() {
      return this.valueCache;
   }

   @Generated
   public IrisExpressionLoad setName(final String name) {
      this.name = var1;
      return this;
   }

   @Generated
   public IrisExpressionLoad setStaticValue(final double staticValue) {
      this.staticValue = var1;
      return this;
   }

   @Generated
   public IrisExpressionLoad setStyleValue(final IrisGeneratorStyle styleValue) {
      this.styleValue = var1;
      return this;
   }

   @Generated
   public IrisExpressionLoad setEngineStreamValue(final IrisEngineStreamType engineStreamValue) {
      this.engineStreamValue = var1;
      return this;
   }

   @Generated
   public IrisExpressionLoad setEngineValue(final IrisEngineValueType engineValue) {
      this.engineValue = var1;
      return this;
   }

   @Generated
   public IrisExpressionLoad setStreamCache(final AtomicCache<ProceduralStream<Double>> streamCache) {
      this.streamCache = var1;
      return this;
   }

   @Generated
   public IrisExpressionLoad setValueCache(final AtomicCache<Double> valueCache) {
      this.valueCache = var1;
      return this;
   }

   @Generated
   public String toString() {
      String var10000 = this.getName();
      return "IrisExpressionLoad(name=" + var10000 + ", staticValue=" + this.getStaticValue() + ", styleValue=" + String.valueOf(this.getStyleValue()) + ", engineStreamValue=" + String.valueOf(this.getEngineStreamValue()) + ", engineValue=" + String.valueOf(this.getEngineValue()) + ", streamCache=" + String.valueOf(this.getStreamCache()) + ", valueCache=" + String.valueOf(this.getValueCache()) + ", styleCache=" + String.valueOf(this.styleCache) + ")";
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisExpressionLoad)) {
         return false;
      } else {
         IrisExpressionLoad var2 = (IrisExpressionLoad)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (Double.compare(this.getStaticValue(), var2.getStaticValue()) != 0) {
            return false;
         } else {
            label61: {
               String var3 = this.getName();
               String var4 = var2.getName();
               if (var3 == null) {
                  if (var4 == null) {
                     break label61;
                  }
               } else if (var3.equals(var4)) {
                  break label61;
               }

               return false;
            }

            label54: {
               IrisGeneratorStyle var5 = this.getStyleValue();
               IrisGeneratorStyle var6 = var2.getStyleValue();
               if (var5 == null) {
                  if (var6 == null) {
                     break label54;
                  }
               } else if (var5.equals(var6)) {
                  break label54;
               }

               return false;
            }

            IrisEngineStreamType var7 = this.getEngineStreamValue();
            IrisEngineStreamType var8 = var2.getEngineStreamValue();
            if (var7 == null) {
               if (var8 != null) {
                  return false;
               }
            } else if (!var7.equals(var8)) {
               return false;
            }

            IrisEngineValueType var9 = this.getEngineValue();
            IrisEngineValueType var10 = var2.getEngineValue();
            if (var9 == null) {
               if (var10 != null) {
                  return false;
               }
            } else if (!var9.equals(var10)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof IrisExpressionLoad;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      long var3 = Double.doubleToLongBits(this.getStaticValue());
      int var9 = var2 * 59 + (int)(var3 >>> 32 ^ var3);
      String var5 = this.getName();
      var9 = var9 * 59 + (var5 == null ? 43 : var5.hashCode());
      IrisGeneratorStyle var6 = this.getStyleValue();
      var9 = var9 * 59 + (var6 == null ? 43 : var6.hashCode());
      IrisEngineStreamType var7 = this.getEngineStreamValue();
      var9 = var9 * 59 + (var7 == null ? 43 : var7.hashCode());
      IrisEngineValueType var8 = this.getEngineValue();
      var9 = var9 * 59 + (var8 == null ? 43 : var8.hashCode());
      return var9;
   }
}
