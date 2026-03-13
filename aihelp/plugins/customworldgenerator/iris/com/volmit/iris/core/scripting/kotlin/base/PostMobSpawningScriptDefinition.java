package com.volmit.iris.core.scripting.kotlin.base;

import kotlin.Metadata;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;
import kotlin.script.experimental.api.ScriptCompilationConfiguration;
import kotlin.script.experimental.api.ScriptCompilationConfigurationKeys;
import kotlin.script.experimental.api.ScriptCompilationKt;
import kotlin.script.experimental.api.ScriptCompilationConfiguration.Builder;
import kotlin.script.experimental.util.PropertiesCollection.Key;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

@Metadata(
   mv = {2, 2, 0},
   k = 1,
   xi = 48,
   d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0000\n\u0000\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\b\u0010\u0004\u001a\u00020\u0005H\u0002¨\u0006\u0006"},
   d2 = {"Lcom/volmit/iris/core/scripting/kotlin/base/PostMobSpawningScriptDefinition;", "Lkotlin/script/experimental/api/ScriptCompilationConfiguration;", "<init>", "()V", "readResolve", "", "core"}
)
public final class PostMobSpawningScriptDefinition extends ScriptCompilationConfiguration {
   @NotNull
   public static final PostMobSpawningScriptDefinition INSTANCE = new PostMobSpawningScriptDefinition();

   private PostMobSpawningScriptDefinition() {
      super((Iterable)CollectionsKt.listOf(MobSpawningScriptDefinition.INSTANCE), PostMobSpawningScriptDefinition::_init_$lambda$0);
   }

   private final Object readResolve() {
      return INSTANCE;
   }

   private static final Unit _init_$lambda$0(Builder var0) {
      Intrinsics.checkNotNullParameter(var0, "<this>");
      Key var10001 = ScriptCompilationKt.getProvidedProperties((ScriptCompilationConfigurationKeys)var0);
      Pair[] var1 = new Pair[]{TuplesKt.to("entity", Reflection.getOrCreateKotlinClass(Entity.class))};
      var0.invoke_kotlintype_map_from_kclass(var10001, var1);
      return Unit.INSTANCE;
   }
}
