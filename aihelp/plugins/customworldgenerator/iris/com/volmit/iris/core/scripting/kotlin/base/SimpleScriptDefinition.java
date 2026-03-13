package com.volmit.iris.core.scripting.kotlin.base;

import com.volmit.iris.core.scripting.kotlin.runner.UtilsKt;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;
import kotlin.script.experimental.annotations.KotlinScript;
import kotlin.script.experimental.api.ScriptCompilationConfiguration;
import kotlin.script.experimental.api.ScriptCompilationConfigurationKeys;
import kotlin.script.experimental.api.ScriptCompilationKt;
import kotlin.script.experimental.api.ScriptCompilationConfiguration.Builder;
import kotlin.script.experimental.dependencies.DependsOn;
import kotlin.script.experimental.dependencies.Repository;
import kotlin.script.experimental.jvm.JvmScriptCompilationConfigurationBuilder;
import kotlin.script.experimental.jvm.JvmScriptCompilationKt;
import kotlin.script.experimental.util.PropertiesCollection.Key;
import org.jetbrains.annotations.NotNull;

@Metadata(
   mv = {2, 2, 0},
   k = 1,
   xi = 48,
   d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0000\n\u0000\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\b\u0010\u0004\u001a\u00020\u0005H\u0002¨\u0006\u0006"},
   d2 = {"Lcom/volmit/iris/core/scripting/kotlin/base/SimpleScriptDefinition;", "Lkotlin/script/experimental/api/ScriptCompilationConfiguration;", "<init>", "()V", "readResolve", "", "core"}
)
public final class SimpleScriptDefinition extends ScriptCompilationConfiguration {
   @NotNull
   public static final SimpleScriptDefinition INSTANCE = new SimpleScriptDefinition();

   private SimpleScriptDefinition() {
      super(SimpleScriptDefinition::_init_$lambda$1);
   }

   private final Object readResolve() {
      return INSTANCE;
   }

   private static final Unit _init_$lambda$1$lambda$0(JvmScriptCompilationConfigurationBuilder var0) {
      Intrinsics.checkNotNullParameter(var0, "$this$jvm");
      JvmScriptCompilationKt.dependenciesFromClassContext(var0, Reflection.getOrCreateKotlinClass(KotlinScript.class), new String[0], true);
      JvmScriptCompilationKt.dependenciesFromClassContext(var0, Reflection.getOrCreateKotlinClass(SimpleScript.class), new String[0], true);
      return Unit.INSTANCE;
   }

   private static final Unit _init_$lambda$1(Builder var0) {
      Intrinsics.checkNotNullParameter(var0, "<this>");
      Key var10001 = ScriptCompilationKt.getDefaultImports((ScriptCompilationConfigurationKeys)var0);
      String[] var1 = new String[6];
      String var10004 = Reflection.getOrCreateKotlinClass(DependsOn.class).getQualifiedName();
      Intrinsics.checkNotNull(var10004);
      var1[0] = var10004;
      var10004 = Reflection.getOrCreateKotlinClass(Repository.class).getQualifiedName();
      Intrinsics.checkNotNull(var10004);
      var1[1] = var10004;
      var1[2] = "com.volmit.iris.Iris.info";
      var1[3] = "com.volmit.iris.Iris.debug";
      var1[4] = "com.volmit.iris.Iris.warn";
      var1[5] = "com.volmit.iris.Iris.error";
      var0.invoke(var10001, var1);
      var0.invoke((kotlin.script.experimental.util.PropertiesCollection.Builder)JvmScriptCompilationKt.getJvm((ScriptCompilationConfigurationKeys)var0), SimpleScriptDefinition::_init_$lambda$1$lambda$0);
      UtilsKt.configure(var0);
      return Unit.INSTANCE;
   }
}
