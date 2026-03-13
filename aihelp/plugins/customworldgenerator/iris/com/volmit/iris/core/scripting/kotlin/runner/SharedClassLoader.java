package com.volmit.iris.core.scripting.kotlin.runner;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.script.experimental.api.ScriptCompilationConfiguration;
import kotlin.script.experimental.jvm.JvmDependencyFromClassLoader;
import org.jetbrains.annotations.NotNull;

@Metadata(
   mv = {2, 2, 0},
   k = 1,
   xi = 48,
   d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\b\u0000\u0018\u00002\u00020\u0001B\u0011\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003¢\u0006\u0004\b\u0004\u0010\u0005J\u0014\u0010\n\u001a\u00020\u000b2\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000e0\rR\u0011\u0010\u0006\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\t¨\u0006\u000f"},
   d2 = {"Lcom/volmit/iris/core/scripting/kotlin/runner/SharedClassLoader;", "Ljava/net/URLClassLoader;", "parent", "Ljava/lang/ClassLoader;", "<init>", "(Ljava/lang/ClassLoader;)V", "dependency", "Lkotlin/script/experimental/jvm/JvmDependencyFromClassLoader;", "getDependency", "()Lkotlin/script/experimental/jvm/JvmDependencyFromClassLoader;", "addFiles", "", "files", "", "Ljava/io/File;", "core"}
)
@SourceDebugExtension({"SMAP\nUtils.kt\nKotlin\n*S Kotlin\n*F\n+ 1 Utils.kt\ncom/volmit/iris/core/scripting/kotlin/runner/SharedClassLoader\n+ 2 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n*L\n1#1,217:1\n1869#2,2:218\n*S KotlinDebug\n*F\n+ 1 Utils.kt\ncom/volmit/iris/core/scripting/kotlin/runner/SharedClassLoader\n*L\n199#1:218,2\n*E\n"})
public final class SharedClassLoader extends URLClassLoader {
   @NotNull
   private final JvmDependencyFromClassLoader dependency;

   public SharedClassLoader(@NotNull ClassLoader var1) {
      Intrinsics.checkNotNullParameter(var1, "parent");
      super(new URL[0], var1);
      this.dependency = new JvmDependencyFromClassLoader(SharedClassLoader::dependency$lambda$0);
   }

   // $FF: synthetic method
   public SharedClassLoader(ClassLoader var1, int var2, DefaultConstructorMarker var3) {
      if ((var2 & 1) != 0) {
         ClassLoader var10000 = SharedClassLoader.class.getClassLoader();
         Intrinsics.checkNotNullExpressionValue(var10000, "getClassLoader(...)");
         var1 = var10000;
      }

      this(var1);
   }

   @NotNull
   public final JvmDependencyFromClassLoader getDependency() {
      return this.dependency;
   }

   public final void addFiles(@NotNull List<? extends File> var1) {
      Intrinsics.checkNotNullParameter(var1, "files");
      Iterable var2 = (Iterable)var1;
      boolean var3 = false;
      Iterator var4 = var2.iterator();

      while(var4.hasNext()) {
         Object var5 = var4.next();
         File var6 = (File)var5;
         boolean var7 = false;
         this.addURL(var6.toURI().toURL());
      }

   }

   private static final ClassLoader dependency$lambda$0(SharedClassLoader var0, ScriptCompilationConfiguration var1) {
      Intrinsics.checkNotNullParameter(var1, "it");
      return (ClassLoader)var0;
   }

   public SharedClassLoader() {
      this((ClassLoader)null, 1, (DefaultConstructorMarker)null);
   }
}
