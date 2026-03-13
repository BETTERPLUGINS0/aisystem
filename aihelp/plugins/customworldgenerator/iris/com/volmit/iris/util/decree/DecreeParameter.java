package com.volmit.iris.util.decree;

import com.volmit.iris.engine.data.cache.AtomicCache;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.decree.annotations.Param;
import com.volmit.iris.util.decree.specialhandlers.DummyHandler;
import java.lang.reflect.Parameter;
import lombok.Generated;

public class DecreeParameter {
   private final Parameter parameter;
   private final Param param;
   private final transient AtomicCache<DecreeParameterHandler<?>> handlerCache = new AtomicCache();

   public DecreeParameter(Parameter parameter) {
      this.parameter = var1;
      this.param = (Param)var1.getDeclaredAnnotation(Param.class);
      if (this.param == null) {
         String var10002 = var1.getName();
         throw new RuntimeException("Cannot instantiate DecreeParameter on " + var10002 + " in method " + var1.getDeclaringExecutable().getName() + "(...) in class " + var1.getDeclaringExecutable().getDeclaringClass().getCanonicalName() + " not annotated by @Param");
      }
   }

   public DecreeParameterHandler<?> getHandler() {
      return (DecreeParameterHandler)this.handlerCache.aquire(() -> {
         try {
            return this.param.customHandler().equals(DummyHandler.class) ? DecreeSystem.getHandler(this.getType()) : (DecreeParameterHandler)this.param.customHandler().getConstructor().newInstance();
         } catch (Throwable var2) {
            var2.printStackTrace();
            return null;
         }
      });
   }

   public Class<?> getType() {
      return this.parameter.getType();
   }

   public String getName() {
      return this.param.name().isEmpty() ? this.parameter.getName() : this.param.name();
   }

   public String getDescription() {
      return this.param.description().isEmpty() ? "No Description Provided" : this.param.description();
   }

   public boolean isRequired() {
      return !this.hasDefault();
   }

   public KList<String> getNames() {
      KList var1 = new KList();
      String[] var2 = this.param.aliases();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String var5 = var2[var4];
         if (!var5.isEmpty()) {
            var1.add((Object)var5);
         }
      }

      var1.add((Object)this.getName());
      var1.removeDuplicates();
      return var1;
   }

   public Object getDefaultValue() {
      return this.param.defaultValue().trim().isEmpty() ? null : this.getHandler().parse(this.param.defaultValue().trim(), true);
   }

   public boolean hasDefault() {
      return !this.param.defaultValue().trim().isEmpty();
   }

   public String example() {
      KList var1 = this.getHandler().getPossibilities();
      var1 = var1 != null ? var1 : new KList();
      KList var2 = var1.convert((var1x) -> {
         return this.getHandler().toStringForce(var1x);
      });
      if (var2.isEmpty()) {
         var2 = new KList();
         var2.add((Object)this.getHandler().getRandomDefault());
      }

      return (String)var2.getRandom();
   }

   public boolean isContextual() {
      return this.param.contextual();
   }

   @Generated
   public Parameter getParameter() {
      return this.parameter;
   }

   @Generated
   public Param getParam() {
      return this.param;
   }

   @Generated
   public AtomicCache<DecreeParameterHandler<?>> getHandlerCache() {
      return this.handlerCache;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof DecreeParameter)) {
         return false;
      } else {
         DecreeParameter var2 = (DecreeParameter)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else {
            Parameter var3 = this.getParameter();
            Parameter var4 = var2.getParameter();
            if (var3 == null) {
               if (var4 != null) {
                  return false;
               }
            } else if (!var3.equals(var4)) {
               return false;
            }

            Param var5 = this.getParam();
            Param var6 = var2.getParam();
            if (var5 == null) {
               if (var6 != null) {
                  return false;
               }
            } else if (!var5.equals(var6)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof DecreeParameter;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      Parameter var3 = this.getParameter();
      int var5 = var2 * 59 + (var3 == null ? 43 : var3.hashCode());
      Param var4 = this.getParam();
      var5 = var5 * 59 + (var4 == null ? 43 : var4.hashCode());
      return var5;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getParameter());
      return "DecreeParameter(parameter=" + var10000 + ", param=" + String.valueOf(this.getParam()) + ", handlerCache=" + String.valueOf(this.getHandlerCache()) + ")";
   }
}
