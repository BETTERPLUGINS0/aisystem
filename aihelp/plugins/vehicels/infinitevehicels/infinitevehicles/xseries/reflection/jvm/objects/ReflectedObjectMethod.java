package me.PM2.infinitevehicles.xseries.reflection.jvm.objects;

import java.lang.reflect.Member;
import java.lang.reflect.Method;

final class ReflectedObjectMethod extends AbstractMemberReflectedObject {
   private final Method delegate;

   ReflectedObjectMethod(Method var1) {
      this.delegate = var1;
   }

   public ReflectedObject.Type type() {
      return ReflectedObject.Type.METHOD;
   }

   public Method unreflect() {
      return this.delegate;
   }

   protected Member member() {
      return this.delegate;
   }
}
