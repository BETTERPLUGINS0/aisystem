package me.PM2.infinitevehicles.xseries.reflection.proxy.processors;

import java.lang.reflect.Method;
import me.PM2.infinitevehicles.xseries.reflection.ReflectiveHandle;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public class ProxyMethodInfo {
   public final ReflectiveHandle<?> handle;
   public final Method interfaceMethod;
   public final MappedType rType;
   public final MappedType[] pTypes;

   public ProxyMethodInfo(ReflectiveHandle<?> var1, Method var2, MappedType var3, MappedType[] var4) {
      this.handle = var1;
      this.interfaceMethod = var2;
      this.rType = var3;
      this.pTypes = var4;
   }

   public String toString() {
      return this.getClass().getSimpleName() + '(' + this.interfaceMethod + ')';
   }
}
