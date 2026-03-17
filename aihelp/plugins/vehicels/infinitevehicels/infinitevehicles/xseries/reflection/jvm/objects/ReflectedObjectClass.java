package me.PM2.infinitevehicles.xseries.reflection.jvm.objects;

final class ReflectedObjectClass extends AbstractReflectedObject {
   private final Class<?> delegate;

   ReflectedObjectClass(Class<?> var1) {
      this.delegate = var1;
   }

   public ReflectedObject.Type type() {
      return ReflectedObject.Type.CLASS;
   }

   public Class<?> unreflect() {
      return this.delegate;
   }

   public String name() {
      return this.delegate.getSimpleName();
   }

   public Class<?> getDeclaringClass() {
      return this.delegate.getDeclaringClass();
   }

   public int getModifiers() {
      return this.delegate.getModifiers();
   }
}
