package com.volmit.iris.engine;

import com.volmit.iris.core.nms.container.Pair;
import com.volmit.iris.engine.data.cache.AtomicCache;
import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.engine.mantle.EngineMantle;
import com.volmit.iris.engine.mantle.MantleComponent;
import com.volmit.iris.engine.mantle.components.MantleCarvingComponent;
import com.volmit.iris.engine.mantle.components.MantleFluidBodyComponent;
import com.volmit.iris.engine.mantle.components.MantleJigsawComponent;
import com.volmit.iris.engine.mantle.components.MantleObjectComponent;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.mantle.Mantle;
import com.volmit.iris.util.mantle.flag.MantleFlag;
import java.io.File;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;
import lombok.Generated;

public class IrisEngineMantle implements EngineMantle {
   private final Engine engine;
   private final Mantle mantle;
   private final KMap<Integer, KList<MantleComponent>> components;
   private final KMap<MantleFlag, MantleComponent> registeredComponents = new KMap();
   private final AtomicCache<List<Pair<List<MantleComponent>, Integer>>> componentsCache = new AtomicCache();
   private final AtomicCache<Set<MantleFlag>> disabledFlags = new AtomicCache();
   private final MantleObjectComponent object;
   private final MantleJigsawComponent jigsaw;

   public IrisEngineMantle(Engine engine) {
      this.engine = var1;
      this.mantle = new Mantle(new File(var1.getWorld().worldFolder(), "mantle"), var1.getTarget().getHeight());
      this.components = new KMap();
      this.registerComponent(new MantleCarvingComponent(this));
      this.registerComponent(new MantleFluidBodyComponent(this));
      this.jigsaw = new MantleJigsawComponent(this);
      this.registerComponent(this.jigsaw);
      this.object = new MantleObjectComponent(this);
      this.registerComponent(this.object);
   }

   public int getRadius() {
      return this.components.isEmpty() ? 0 : (Integer)((Pair)this.getComponents().getFirst()).getB();
   }

   public int getRealRadius() {
      return this.components.isEmpty() ? 0 : (Integer)((Pair)this.getComponents().getLast()).getB();
   }

   public List<Pair<List<MantleComponent>, Integer>> getComponents() {
      return (List)this.componentsCache.aquire(() -> {
         Stream var10000 = this.components.keySet().stream().sorted();
         KMap var10001 = this.components;
         Objects.requireNonNull(var10001);
         List var1 = var10000.map(var10001::get).map((var0) -> {
            int var1 = var0.stream().filter(MantleComponent::isEnabled).mapToInt(MantleComponent::getRadius).max().orElse(0);
            return new Pair(List.copyOf(var0), var1);
         }).filter((var0) -> {
            return !((List)var0.getA()).isEmpty();
         }).toList();
         int var2 = 0;
         Iterator var3 = var1.reversed().iterator();

         while(var3.hasNext()) {
            Pair var4 = (Pair)var3.next();
            var2 += (Integer)var4.getB();
            var4.setB(Math.ceilDiv(var2, 16));
         }

         return var1;
      });
   }

   public Map<MantleFlag, MantleComponent> getRegisteredComponents() {
      return Collections.unmodifiableMap(this.registeredComponents);
   }

   public boolean registerComponent(MantleComponent c) {
      if (this.registeredComponents.putIfAbsent(var1.getFlag(), var1) != null) {
         return false;
      } else {
         var1.setEnabled(!this.getDisabledFlags().contains(var1.getFlag()));
         ((KList)this.components.computeIfAbsent(var1.getPriority(), (var0) -> {
            return new KList();
         })).add((Object)var1);
         this.componentsCache.reset();
         return true;
      }
   }

   public KList<MantleFlag> getComponentFlags() {
      return new KList(this.registeredComponents.keySet());
   }

   public void hotload() {
      this.disabledFlags.reset();
      Iterator var1 = this.registeredComponents.values().iterator();

      while(var1.hasNext()) {
         MantleComponent var2 = (MantleComponent)var1.next();
         var2.hotload();
         var2.setEnabled(!this.getDisabledFlags().contains(var2.getFlag()));
      }

      this.componentsCache.reset();
   }

   private Set<MantleFlag> getDisabledFlags() {
      return (Set)this.disabledFlags.aquire(() -> {
         return Set.copyOf(this.getDimension().getDisabledComponents());
      });
   }

   public MantleJigsawComponent getJigsawComponent() {
      return this.jigsaw;
   }

   public MantleObjectComponent getObjectComponent() {
      return this.object;
   }

   @Generated
   public Engine getEngine() {
      return this.engine;
   }

   @Generated
   public Mantle getMantle() {
      return this.mantle;
   }

   @Generated
   public AtomicCache<List<Pair<List<MantleComponent>, Integer>>> getComponentsCache() {
      return this.componentsCache;
   }

   @Generated
   public MantleObjectComponent getObject() {
      return this.object;
   }

   @Generated
   public MantleJigsawComponent getJigsaw() {
      return this.jigsaw;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisEngineMantle)) {
         return false;
      } else {
         IrisEngineMantle var2 = (IrisEngineMantle)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else {
            label95: {
               Mantle var3 = this.getMantle();
               Mantle var4 = var2.getMantle();
               if (var3 == null) {
                  if (var4 == null) {
                     break label95;
                  }
               } else if (var3.equals(var4)) {
                  break label95;
               }

               return false;
            }

            List var5 = this.getComponents();
            List var6 = var2.getComponents();
            if (var5 == null) {
               if (var6 != null) {
                  return false;
               }
            } else if (!var5.equals(var6)) {
               return false;
            }

            Map var7 = this.getRegisteredComponents();
            Map var8 = var2.getRegisteredComponents();
            if (var7 == null) {
               if (var8 != null) {
                  return false;
               }
            } else if (!var7.equals(var8)) {
               return false;
            }

            label74: {
               AtomicCache var9 = this.getComponentsCache();
               AtomicCache var10 = var2.getComponentsCache();
               if (var9 == null) {
                  if (var10 == null) {
                     break label74;
                  }
               } else if (var9.equals(var10)) {
                  break label74;
               }

               return false;
            }

            label67: {
               Set var11 = this.getDisabledFlags();
               Set var12 = var2.getDisabledFlags();
               if (var11 == null) {
                  if (var12 == null) {
                     break label67;
                  }
               } else if (var11.equals(var12)) {
                  break label67;
               }

               return false;
            }

            MantleObjectComponent var13 = this.getObject();
            MantleObjectComponent var14 = var2.getObject();
            if (var13 == null) {
               if (var14 != null) {
                  return false;
               }
            } else if (!var13.equals(var14)) {
               return false;
            }

            MantleJigsawComponent var15 = this.getJigsaw();
            MantleJigsawComponent var16 = var2.getJigsaw();
            if (var15 == null) {
               if (var16 != null) {
                  return false;
               }
            } else if (!var15.equals(var16)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof IrisEngineMantle;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      Mantle var3 = this.getMantle();
      int var10 = var2 * 59 + (var3 == null ? 43 : var3.hashCode());
      List var4 = this.getComponents();
      var10 = var10 * 59 + (var4 == null ? 43 : var4.hashCode());
      Map var5 = this.getRegisteredComponents();
      var10 = var10 * 59 + (var5 == null ? 43 : var5.hashCode());
      AtomicCache var6 = this.getComponentsCache();
      var10 = var10 * 59 + (var6 == null ? 43 : var6.hashCode());
      Set var7 = this.getDisabledFlags();
      var10 = var10 * 59 + (var7 == null ? 43 : var7.hashCode());
      MantleObjectComponent var8 = this.getObject();
      var10 = var10 * 59 + (var8 == null ? 43 : var8.hashCode());
      MantleJigsawComponent var9 = this.getJigsaw();
      var10 = var10 * 59 + (var9 == null ? 43 : var9.hashCode());
      return var10;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getMantle());
      return "IrisEngineMantle(mantle=" + var10000 + ", components=" + String.valueOf(this.getComponents()) + ", registeredComponents=" + String.valueOf(this.getRegisteredComponents()) + ", componentsCache=" + String.valueOf(this.getComponentsCache()) + ", disabledFlags=" + String.valueOf(this.getDisabledFlags()) + ", object=" + String.valueOf(this.getObject()) + ", jigsaw=" + String.valueOf(this.getJigsaw()) + ")";
   }
}
