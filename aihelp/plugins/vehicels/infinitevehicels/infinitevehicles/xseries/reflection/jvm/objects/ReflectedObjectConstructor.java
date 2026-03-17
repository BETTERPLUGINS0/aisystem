package me.PM2.infinitevehicles.xseries.reflection.jvm.objects;

import java.lang.reflect.Constructor;
import java.lang.reflect.Member;

final class ReflectedObjectConstructor extends AbstractMemberReflectedObject {
   private final Constructor<?> delegate;

   ReflectedObjectConstructor(Constructor<?> var1) {
      this.delegate = var1;
   }

   public ReflectedObject.Type type() {
      return ReflectedObject.Type.CONSTRUCTOR;
   }

   public String name() {
      return "<init>";
   }

   public Constructor<?> unreflect() {
      return this.delegate;
   }

   protected Member member() {
      return this.delegate;
   }
}
