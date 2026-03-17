package me.PM2.infinitevehicles.xseries.reflection.jvm.objects;

import java.lang.reflect.Field;
import java.lang.reflect.Member;

final class ReflectedObjectField extends AbstractMemberReflectedObject {
   private final Field delegate;

   ReflectedObjectField(Field var1) {
      this.delegate = var1;
   }

   public ReflectedObject.Type type() {
      return ReflectedObject.Type.FIELD;
   }

   public Field unreflect() {
      return this.delegate;
   }

   protected Member member() {
      return this.delegate;
   }
}
