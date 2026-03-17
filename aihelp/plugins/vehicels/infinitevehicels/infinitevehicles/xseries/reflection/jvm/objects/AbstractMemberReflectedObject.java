package me.PM2.infinitevehicles.xseries.reflection.jvm.objects;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Member;

abstract class AbstractMemberReflectedObject extends AbstractReflectedObject {
   public abstract AnnotatedElement unreflect();

   protected abstract Member member();

   public String name() {
      return this.member().getName();
   }

   public final Class<?> getDeclaringClass() {
      return this.member().getDeclaringClass();
   }

   public final int getModifiers() {
      return this.member().getModifiers();
   }
}
