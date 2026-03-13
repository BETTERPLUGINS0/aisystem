package com.volmit.iris.util.decree;

import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.decree.annotations.Decree;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import lombok.Generated;

public class DecreeNode {
   private final Method method;
   private final Object instance;
   private final Decree decree;

   public DecreeNode(Object instance, Method method) {
      this.instance = var1;
      this.method = var2;
      this.decree = (Decree)var2.getDeclaredAnnotation(Decree.class);
      if (this.decree == null) {
         String var10002 = var2.getName();
         throw new RuntimeException("Cannot instantiate DecreeNode on method " + var10002 + " in " + var2.getDeclaringClass().getCanonicalName() + " not annotated by @Decree");
      }
   }

   public KList<DecreeParameter> getParameters() {
      KList var1 = new KList();
      KList var2 = new KList();
      Parameter[] var3 = this.method.getParameters();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Parameter var6 = var3[var5];
         DecreeParameter var7 = new DecreeParameter(var6);
         if (var7.isRequired()) {
            var1.add((Object)var7);
         } else {
            var2.add((Object)var7);
         }
      }

      var1.addAll(var2);
      return var1;
   }

   public String getName() {
      return this.decree.name().isEmpty() ? this.method.getName() : this.decree.name();
   }

   public DecreeOrigin getOrigin() {
      return this.decree.origin();
   }

   public String getDescription() {
      return this.decree.description().isEmpty() ? "No Description Provided" : this.decree.description();
   }

   public KList<String> getNames() {
      KList var1 = new KList();
      var1.add((Object)this.getName());
      String[] var2 = this.decree.aliases();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String var5 = var2[var4];
         if (!var5.isEmpty()) {
            var1.add((Object)var5);
         }
      }

      var1.removeDuplicates();
      return var1;
   }

   public boolean isSync() {
      return this.decree.sync();
   }

   @Generated
   public Method getMethod() {
      return this.method;
   }

   @Generated
   public Object getInstance() {
      return this.instance;
   }

   @Generated
   public Decree getDecree() {
      return this.decree;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof DecreeNode)) {
         return false;
      } else {
         DecreeNode var2 = (DecreeNode)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else {
            label47: {
               Method var3 = this.getMethod();
               Method var4 = var2.getMethod();
               if (var3 == null) {
                  if (var4 == null) {
                     break label47;
                  }
               } else if (var3.equals(var4)) {
                  break label47;
               }

               return false;
            }

            Object var5 = this.getInstance();
            Object var6 = var2.getInstance();
            if (var5 == null) {
               if (var6 != null) {
                  return false;
               }
            } else if (!var5.equals(var6)) {
               return false;
            }

            Decree var7 = this.getDecree();
            Decree var8 = var2.getDecree();
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
      return var1 instanceof DecreeNode;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      Method var3 = this.getMethod();
      int var6 = var2 * 59 + (var3 == null ? 43 : var3.hashCode());
      Object var4 = this.getInstance();
      var6 = var6 * 59 + (var4 == null ? 43 : var4.hashCode());
      Decree var5 = this.getDecree();
      var6 = var6 * 59 + (var5 == null ? 43 : var5.hashCode());
      return var6;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getMethod());
      return "DecreeNode(method=" + var10000 + ", instance=" + String.valueOf(this.getInstance()) + ", decree=" + String.valueOf(this.getDecree()) + ")";
   }
}
