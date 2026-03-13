package com.volmit.iris.core.safeguard.task;

import com.volmit.iris.Iris;
import com.volmit.iris.core.IrisWorlds;
import com.volmit.iris.core.nms.INMS;
import com.volmit.iris.core.nms.INMSBinding;
import com.volmit.iris.core.nms.v1X.NMSBinding1X;
import com.volmit.iris.core.safeguard.Mode;
import com.volmit.iris.engine.object.IrisDimension;
import com.volmit.iris.util.agent.Agent;
import com.volmit.iris.util.misc.getHardware;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.tools.ToolProvider;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Result.Companion;
import kotlin.collections.CollectionsKt;
import kotlin.collections.SetsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.PropertyReference0;
import kotlin.jvm.internal.PropertyReference0Impl;
import kotlin.jvm.internal.Reflection;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.properties.PropertyDelegateProvider;
import kotlin.properties.ReadOnlyProperty;
import kotlin.reflect.KProperty;
import kotlin.text.StringsKt;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.jetbrains.annotations.NotNull;

@Metadata(
   mv = {2, 2, 0},
   k = 2,
   xi = 48,
   d1 = {"\u0000J\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u001a\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010!\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\u001a1\u0010#\u001a\u00020$\"\u0004\b\u0000\u0010%*\b\u0012\u0004\u0012\u0002H%0&2\u0012\u0010'\u001a\n\u0012\u0006\b\u0001\u0012\u0002H%0(\"\u0002H%H\u0002¢\u0006\u0002\u0010)\u001a6\u0010*\u001a\u001e\u0012\u0006\u0012\u0004\u0018\u00010,\u0012\u0012\u0012\u0010\u0012\u0006\u0012\u0004\u0018\u00010,\u0012\u0004\u0012\u00020\u00010-0+2\u0012\u0010.\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u000201000/\"\u001b\u0010\u0000\u001a\u00020\u00018BX\u0082\u0084\u0002¢\u0006\f\n\u0004\b\u0004\u0010\u0005\u001a\u0004\b\u0002\u0010\u0003\"\u001b\u0010\u0006\u001a\u00020\u00018BX\u0082\u0084\u0002¢\u0006\f\n\u0004\b\b\u0010\u0005\u001a\u0004\b\u0007\u0010\u0003\"\u001b\u0010\t\u001a\u00020\u00018BX\u0082\u0084\u0002¢\u0006\f\n\u0004\b\u000b\u0010\u0005\u001a\u0004\b\n\u0010\u0003\"\u001b\u0010\f\u001a\u00020\u00018BX\u0082\u0084\u0002¢\u0006\f\n\u0004\b\u000e\u0010\u0005\u001a\u0004\b\r\u0010\u0003\"\u001b\u0010\u000f\u001a\u00020\u00018BX\u0082\u0084\u0002¢\u0006\f\n\u0004\b\u0011\u0010\u0005\u001a\u0004\b\u0010\u0010\u0003\"\u001b\u0010\u0012\u001a\u00020\u00018BX\u0082\u0084\u0002¢\u0006\f\n\u0004\b\u0014\u0010\u0005\u001a\u0004\b\u0013\u0010\u0003\"\u001b\u0010\u0015\u001a\u00020\u00018BX\u0082\u0084\u0002¢\u0006\f\n\u0004\b\u0017\u0010\u0005\u001a\u0004\b\u0016\u0010\u0003\"\u001b\u0010\u0018\u001a\u00020\u00018BX\u0082\u0084\u0002¢\u0006\f\n\u0004\b\u001a\u0010\u0005\u001a\u0004\b\u0019\u0010\u0003\"\u0017\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u00010\u001c¢\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u001e\"\u0014\u0010\u001f\u001a\u00020 8BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b!\u0010\"¨\u00062"},
   d2 = {"memory", "Lcom/volmit/iris/core/safeguard/task/Task;", "getMemory", "()Lcom/volmit/iris/core/safeguard/task/Task;", "memory$delegate", "Lkotlin/properties/ReadOnlyProperty;", "incompatibilities", "getIncompatibilities", "incompatibilities$delegate", "software", "getSoftware", "software$delegate", "version", "getVersion", "version$delegate", "injection", "getInjection", "injection$delegate", "dimensionTypes", "getDimensionTypes", "dimensionTypes$delegate", "diskSpace", "getDiskSpace", "diskSpace$delegate", "java", "getJava", "java$delegate", "tasks", "", "getTasks", "()Ljava/util/List;", "server", "Lorg/bukkit/Server;", "getServer", "()Lorg/bukkit/Server;", "addAll", "", "T", "", "values", "", "(Ljava/util/List;[Ljava/lang/Object;)V", "task", "Lkotlin/properties/PropertyDelegateProvider;", "", "Lkotlin/properties/ReadOnlyProperty;", "action", "Lkotlin/Function0;", "Lcom/volmit/iris/core/safeguard/task/ValueWithDiagnostics;", "Lcom/volmit/iris/core/safeguard/Mode;", "core"}
)
@SourceDebugExtension({"SMAP\nTasks.kt\nKotlin\n*S Kotlin\n*F\n+ 1 Tasks.kt\ncom/volmit/iris/core/safeguard/task/TasksKt\n+ 2 _Arrays.kt\nkotlin/collections/ArraysKt___ArraysKt\n+ 3 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n+ 4 ArraysJVM.kt\nkotlin/collections/ArraysKt__ArraysJVMKt\n+ 5 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,138:1\n13472#2,2:139\n1761#3,3:141\n37#4:144\n36#4,3:145\n1#5:148\n*S KotlinDebug\n*F\n+ 1 Tasks.kt\ncom/volmit/iris/core/safeguard/task/TasksKt\n*L\n135#1:139,2\n58#1:141,3\n96#1:144\n96#1:145,3\n*E\n"})
public final class TasksKt {
   // $FF: synthetic field
   static final KProperty<Object>[] $$delegatedProperties;
   @NotNull
   private static final ReadOnlyProperty memory$delegate;
   @NotNull
   private static final ReadOnlyProperty incompatibilities$delegate;
   @NotNull
   private static final ReadOnlyProperty software$delegate;
   @NotNull
   private static final ReadOnlyProperty version$delegate;
   @NotNull
   private static final ReadOnlyProperty injection$delegate;
   @NotNull
   private static final ReadOnlyProperty dimensionTypes$delegate;
   @NotNull
   private static final ReadOnlyProperty diskSpace$delegate;
   @NotNull
   private static final ReadOnlyProperty java$delegate;
   @NotNull
   private static final List<Task> tasks;

   private static final Task getMemory() {
      return (Task)memory$delegate.getValue((Object)null, $$delegatedProperties[0]);
   }

   private static final Task getIncompatibilities() {
      return (Task)incompatibilities$delegate.getValue((Object)null, $$delegatedProperties[1]);
   }

   private static final Task getSoftware() {
      return (Task)software$delegate.getValue((Object)null, $$delegatedProperties[2]);
   }

   private static final Task getVersion() {
      return (Task)version$delegate.getValue((Object)null, $$delegatedProperties[3]);
   }

   private static final Task getInjection() {
      return (Task)injection$delegate.getValue((Object)null, $$delegatedProperties[4]);
   }

   private static final Task getDimensionTypes() {
      return (Task)dimensionTypes$delegate.getValue((Object)null, $$delegatedProperties[5]);
   }

   private static final Task getDiskSpace() {
      return (Task)diskSpace$delegate.getValue((Object)null, $$delegatedProperties[6]);
   }

   private static final Task getJava() {
      return (Task)java$delegate.getValue((Object)null, $$delegatedProperties[7]);
   }

   @NotNull
   public static final List<Task> getTasks() {
      return tasks;
   }

   private static final Server getServer() {
      Server var10000 = Bukkit.getServer();
      Intrinsics.checkNotNullExpressionValue(var10000, "getServer(...)");
      return var10000;
   }

   private static final <T> void addAll(List<T> var0, T... var1) {
      Object[] var2 = var1;
      boolean var3 = false;
      int var4 = 0;

      for(int var5 = var1.length; var4 < var5; ++var4) {
         Object var6 = var2[var4];
         boolean var8 = false;
         var0.add(var6);
      }

   }

   @NotNull
   public static final PropertyDelegateProvider<Object, ReadOnlyProperty<Object, Task>> task(@NotNull Function0<? extends ValueWithDiagnostics<? extends Mode>> var0) {
      Intrinsics.checkNotNullParameter(var0, "action");
      return TasksKt::task$lambda$15;
   }

   private static final ValueWithDiagnostics memory_delegate$lambda$0() {
      long var0 = getHardware.getProcessMemory();
      ValueWithDiagnostics var10000;
      if (var0 >= 5999L) {
         var10000 = ValueWithDiagnosticsKt.withDiagnostics(Mode.STABLE, (Diagnostic[])());
      } else {
         Mode var3 = Mode.STABLE;
         Diagnostic[] var2 = new Diagnostic[]{Diagnostic.Logger.create$default(Diagnostic.Logger.WARN, "Low Memory", (Throwable)null, 2, (Object)null), Diagnostic.Logger.create$default(Diagnostic.Logger.WARN, "- 6GB+ Ram is recommended", (Throwable)null, 2, (Object)null), Diagnostic.Logger.create$default(Diagnostic.Logger.WARN, "- Process Memory: " + var0 + " MB", (Throwable)null, 2, (Object)null)};
         var10000 = ValueWithDiagnosticsKt.withDiagnostics(var3, (Diagnostic[])var2);
      }

      return var10000;
   }

   private static final boolean incompatibilities_delegate$lambda$3$lambda$1(String var0) {
      Intrinsics.checkNotNullParameter(var0, "it");
      return getServer().getPluginManager().getPlugin(var0) == null;
   }

   private static final boolean incompatibilities_delegate$lambda$3$lambda$2(Function1 var0, Object var1) {
      return (Boolean)var0.invoke(var1);
   }

   private static final ValueWithDiagnostics incompatibilities_delegate$lambda$3() {
      String[] var1 = new String[]{"dynmap", "Stratos"};
      Set var0 = SetsKt.mutableSetOf(var1);
      var0.removeIf(TasksKt::incompatibilities_delegate$lambda$3$lambda$2);
      ValueWithDiagnostics var10000;
      if (var0.isEmpty()) {
         var10000 = ValueWithDiagnosticsKt.withDiagnostics(Mode.STABLE, (Diagnostic[])());
      } else {
         List var3 = (List)(new ArrayList());
         Diagnostic[] var2;
         if (var0.contains("dynmap")) {
            var2 = new Diagnostic[]{Diagnostic.Logger.create$default(Diagnostic.Logger.ERROR, "Dynmap", (Throwable)null, 2, (Object)null), Diagnostic.Logger.create$default(Diagnostic.Logger.ERROR, "- The plugin Dynmap is not compatible with the server.", (Throwable)null, 2, (Object)null), Diagnostic.Logger.create$default(Diagnostic.Logger.ERROR, "- If you want to have a map plugin like Dynmap, consider Bluemap.", (Throwable)null, 2, (Object)null)};
            addAll(var3, var2);
         }

         if (var0.contains("Stratos")) {
            var2 = new Diagnostic[]{Diagnostic.Logger.create$default(Diagnostic.Logger.ERROR, "Stratos", (Throwable)null, 2, (Object)null), Diagnostic.Logger.create$default(Diagnostic.Logger.ERROR, "- Iris is not compatible with other worldgen plugins.", (Throwable)null, 2, (Object)null)};
            addAll(var3, var2);
         }

         var10000 = ValueWithDiagnosticsKt.withDiagnostics(Mode.WARNING, (List)var3);
      }

      return var10000;
   }

   private static final ValueWithDiagnostics software_delegate$lambda$5() {
      String[] var1 = new String[]{"purpur", "pufferfish", "paper", "spigot", "bukkit"};
      Set var0 = SetsKt.setOf(var1);
      Iterable var7 = (Iterable)var0;
      boolean var2 = false;
      boolean var9;
      if (var7 instanceof Collection && ((Collection)var7).isEmpty()) {
         var9 = false;
      } else {
         Iterator var3 = var7.iterator();

         while(true) {
            if (!var3.hasNext()) {
               var9 = false;
               break;
            }

            Object var4 = var3.next();
            String var5 = (String)var4;
            boolean var6 = false;
            String var10000 = getServer().getName();
            Intrinsics.checkNotNullExpressionValue(var10000, "getName(...)");
            if (StringsKt.contains((CharSequence)var10000, (CharSequence)var5, true)) {
               var9 = true;
               break;
            }
         }
      }

      ValueWithDiagnostics var10;
      if (var9) {
         var10 = ValueWithDiagnosticsKt.withDiagnostics(Mode.STABLE, (Diagnostic[])());
      } else {
         Mode var11 = Mode.WARNING;
         Diagnostic[] var8 = new Diagnostic[]{Diagnostic.Logger.create$default(Diagnostic.Logger.WARN, "Unsupported Server Software", (Throwable)null, 2, (Object)null), Diagnostic.Logger.create$default(Diagnostic.Logger.WARN, "- Please consider using Paper or Purpur instead.", (Throwable)null, 2, (Object)null)};
         var10 = ValueWithDiagnosticsKt.withDiagnostics(var11, (Diagnostic[])var8);
      }

      return var10;
   }

   private static final ValueWithDiagnostics version_delegate$lambda$6() {
      String var10000 = Iris.instance.getDescription().getVersion();
      Intrinsics.checkNotNullExpressionValue(var10000, "getVersion(...)");
      CharSequence var5 = (CharSequence)var10000;
      char[] var1 = new char[]{'-'};
      List var0 = StringsKt.split$default(var5, var1, false, 0, 6, (Object)null);
      String var4 = (String)var0.get(1);
      String var2 = (String)var0.get(2);
      ValueWithDiagnostics var6;
      if (!(INMS.get() instanceof NMSBinding1X)) {
         var6 = ValueWithDiagnosticsKt.withDiagnostics(Mode.STABLE, (Diagnostic[])());
      } else {
         Mode var7 = Mode.UNSTABLE;
         Diagnostic[] var3 = new Diagnostic[]{Diagnostic.Logger.create$default(Diagnostic.Logger.ERROR, "Server Version", (Throwable)null, 2, (Object)null), Diagnostic.Logger.create$default(Diagnostic.Logger.ERROR, "- Iris only supports " + var4 + " > " + var2, (Throwable)null, 2, (Object)null)};
         var6 = ValueWithDiagnosticsKt.withDiagnostics(var7, (Diagnostic[])var3);
      }

      return var6;
   }

   private static final ValueWithDiagnostics injection_delegate$lambda$7() {
      Mode var10000;
      Diagnostic[] var0;
      ValueWithDiagnostics var1;
      if (!Agent.install()) {
         var10000 = Mode.UNSTABLE;
         var0 = new Diagnostic[]{Diagnostic.Logger.create$default(Diagnostic.Logger.ERROR, "Java Agent", (Throwable)null, 2, (Object)null), Diagnostic.Logger.create$default(Diagnostic.Logger.ERROR, "- Please enable dynamic agent loading by adding -XX:+EnableDynamicAgentLoading to your jvm arguments.", (Throwable)null, 2, (Object)null), Diagnostic.Logger.create$default(Diagnostic.Logger.ERROR, "- or add the jvm argument -javaagent:" + Agent.AGENT_JAR.getPath(), (Throwable)null, 2, (Object)null)};
         var1 = ValueWithDiagnosticsKt.withDiagnostics(var10000, (Diagnostic[])var0);
      } else if (!INMS.get().injectBukkit()) {
         var10000 = Mode.UNSTABLE;
         var0 = new Diagnostic[]{Diagnostic.Logger.create$default(Diagnostic.Logger.ERROR, "Code Injection", (Throwable)null, 2, (Object)null), Diagnostic.Logger.create$default(Diagnostic.Logger.ERROR, "- Failed to inject code. Please contact support", (Throwable)null, 2, (Object)null)};
         var1 = ValueWithDiagnosticsKt.withDiagnostics(var10000, (Diagnostic[])var0);
      } else {
         var1 = ValueWithDiagnosticsKt.withDiagnostics(Mode.STABLE, (Diagnostic[])());
      }

      return var1;
   }

   private static final String dimensionTypes_delegate$lambda$10$lambda$8(IrisDimension var0) {
      return var0.getDimensionTypeKey();
   }

   private static final String dimensionTypes_delegate$lambda$10$lambda$9(Function1 var0, Object var1) {
      return (String)var0.invoke(var1);
   }

   private static final ValueWithDiagnostics dimensionTypes_delegate$lambda$10() {
      Set var0 = (Set)IrisWorlds.get().getDimensions().map(TasksKt::dimensionTypes_delegate$lambda$10$lambda$9).collect(Collectors.toSet());
      INMSBinding var10000 = INMS.get();
      Intrinsics.checkNotNull(var0);
      Collection var2 = (Collection)var0;
      boolean var3 = false;
      String[] var1 = (String[])var2.toArray(new String[0]);
      ValueWithDiagnostics var6;
      if (!var10000.missingDimensionTypes((String[])Arrays.copyOf(var1, var1.length))) {
         var6 = ValueWithDiagnosticsKt.withDiagnostics(Mode.STABLE, (Diagnostic[])());
      } else {
         Mode var7 = Mode.UNSTABLE;
         Diagnostic[] var5 = new Diagnostic[]{Diagnostic.Logger.create$default(Diagnostic.Logger.ERROR, "Dimension Types", (Throwable)null, 2, (Object)null), Diagnostic.Logger.create$default(Diagnostic.Logger.ERROR, "- Required Iris dimension types were not loaded.", (Throwable)null, 2, (Object)null), Diagnostic.Logger.create$default(Diagnostic.Logger.ERROR, "- If this still happens after a restart please contact support.", (Throwable)null, 2, (Object)null)};
         var6 = ValueWithDiagnosticsKt.withDiagnostics(var7, (Diagnostic[])var5);
      }

      return var6;
   }

   private static final ValueWithDiagnostics diskSpace_delegate$lambda$11() {
      ValueWithDiagnostics var10000;
      if ((double)getServer().getWorldContainer().getFreeSpace() / (double)1073741824 > 3.0D) {
         var10000 = ValueWithDiagnosticsKt.withDiagnostics(Mode.STABLE, (Diagnostic[])());
      } else {
         Mode var1 = Mode.WARNING;
         Diagnostic[] var0 = new Diagnostic[]{Diagnostic.Logger.create$default(Diagnostic.Logger.WARN, "Insufficient Disk Space", (Throwable)null, 2, (Object)null), Diagnostic.Logger.create$default(Diagnostic.Logger.WARN, "- 3GB of free space is required for Iris to function.", (Throwable)null, 2, (Object)null)};
         var10000 = ValueWithDiagnosticsKt.withDiagnostics(var1, (Diagnostic[])var0);
      }

      return var10000;
   }

   private static final ValueWithDiagnostics java_delegate$lambda$13() {
      int var0 = Iris.getJavaVersion();

      Companion var10000;
      Object var2;
      try {
         var10000 = Result.Companion;
         boolean var5 = false;
         var2 = Result.constructor-impl(ToolProvider.getSystemJavaCompiler());
      } catch (Throwable var4) {
         var10000 = Result.Companion;
         var2 = Result.constructor-impl(ResultKt.createFailure(var4));
      }

      boolean var1 = (Result.isFailure-impl(var2) ? null : var2) != null;
      ValueWithDiagnostics var8;
      if (SetsKt.setOf(21).contains(var0) && var1) {
         var8 = ValueWithDiagnosticsKt.withDiagnostics(Mode.STABLE, (Diagnostic[])());
      } else {
         Mode var7 = Mode.WARNING;
         Diagnostic[] var6 = new Diagnostic[]{Diagnostic.Logger.create$default(Diagnostic.Logger.WARN, "Unsupported Java version", (Throwable)null, 2, (Object)null), Diagnostic.Logger.create$default(Diagnostic.Logger.WARN, "- Please consider using JDK 21 Instead of " + (var1 ? "JDK" : "JRE") + " " + var0, (Throwable)null, 2, (Object)null)};
         var8 = ValueWithDiagnosticsKt.withDiagnostics(var7, (Diagnostic[])var6);
      }

      return var8;
   }

   private static final Task task$lambda$15$lambda$14(Function0 var0, Object var1, KProperty var2) {
      Intrinsics.checkNotNullParameter(var2, "property");
      return Task.Companion.of(var2.getName(), var0);
   }

   private static final ReadOnlyProperty task$lambda$15(Function0 var0, Object var1, KProperty var2) {
      Intrinsics.checkNotNullParameter(var2, "<unused var>");
      return TasksKt::task$lambda$15$lambda$14;
   }

   static {
      KProperty[] var0 = new KProperty[]{Reflection.property0((PropertyReference0)(new PropertyReference0Impl(TasksKt.class, "memory", "getMemory()Lcom/volmit/iris/core/safeguard/task/Task;", 1))), Reflection.property0((PropertyReference0)(new PropertyReference0Impl(TasksKt.class, "incompatibilities", "getIncompatibilities()Lcom/volmit/iris/core/safeguard/task/Task;", 1))), Reflection.property0((PropertyReference0)(new PropertyReference0Impl(TasksKt.class, "software", "getSoftware()Lcom/volmit/iris/core/safeguard/task/Task;", 1))), Reflection.property0((PropertyReference0)(new PropertyReference0Impl(TasksKt.class, "version", "getVersion()Lcom/volmit/iris/core/safeguard/task/Task;", 1))), Reflection.property0((PropertyReference0)(new PropertyReference0Impl(TasksKt.class, "injection", "getInjection()Lcom/volmit/iris/core/safeguard/task/Task;", 1))), Reflection.property0((PropertyReference0)(new PropertyReference0Impl(TasksKt.class, "dimensionTypes", "getDimensionTypes()Lcom/volmit/iris/core/safeguard/task/Task;", 1))), Reflection.property0((PropertyReference0)(new PropertyReference0Impl(TasksKt.class, "diskSpace", "getDiskSpace()Lcom/volmit/iris/core/safeguard/task/Task;", 1))), Reflection.property0((PropertyReference0)(new PropertyReference0Impl(TasksKt.class, "java", "getJava()Lcom/volmit/iris/core/safeguard/task/Task;", 1)))};
      $$delegatedProperties = var0;
      memory$delegate = (ReadOnlyProperty)task(TasksKt::memory_delegate$lambda$0).provideDelegate((Object)null, $$delegatedProperties[0]);
      incompatibilities$delegate = (ReadOnlyProperty)task(TasksKt::incompatibilities_delegate$lambda$3).provideDelegate((Object)null, $$delegatedProperties[1]);
      software$delegate = (ReadOnlyProperty)task(TasksKt::software_delegate$lambda$5).provideDelegate((Object)null, $$delegatedProperties[2]);
      version$delegate = (ReadOnlyProperty)task(TasksKt::version_delegate$lambda$6).provideDelegate((Object)null, $$delegatedProperties[3]);
      injection$delegate = (ReadOnlyProperty)task(TasksKt::injection_delegate$lambda$7).provideDelegate((Object)null, $$delegatedProperties[4]);
      dimensionTypes$delegate = (ReadOnlyProperty)task(TasksKt::dimensionTypes_delegate$lambda$10).provideDelegate((Object)null, $$delegatedProperties[5]);
      diskSpace$delegate = (ReadOnlyProperty)task(TasksKt::diskSpace_delegate$lambda$11).provideDelegate((Object)null, $$delegatedProperties[6]);
      java$delegate = (ReadOnlyProperty)task(TasksKt::java_delegate$lambda$13).provideDelegate((Object)null, $$delegatedProperties[7]);
      Task[] var1 = new Task[]{getMemory(), getIncompatibilities(), getSoftware(), getVersion(), getInjection(), getDimensionTypes(), getDiskSpace(), getJava()};
      tasks = CollectionsKt.listOf(var1);
   }
}
