package com.volmit.iris.core.scripting.kotlin.runner;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(
   mv = {2, 2, 0},
   k = 1,
   xi = 48,
   d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0007\n\u0002\u0010%\n\u0000\n\u0002\u0010\u001f\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0003\b\u0000\u0018\u00002\u00020\u0001B\u0019\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005¢\u0006\u0004\b\u0006\u0010\u0007J\u000e\u0010\u0012\u001a\u00020\u00002\u0006\u0010\u0002\u001a\u00020\u0003J\b\u0010\u0013\u001a\u00020\u0014H\u0016J\u0013\u0010\u0015\u001a\u00020\u00052\b\u0010\u0016\u001a\u0004\u0018\u00010\u0001H\u0096\u0002R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u001a\u0010\f\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00000\rX\u0082\u0004¢\u0006\u0002\n\u0000R\u0017\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00000\u000f8F¢\u0006\u0006\u001a\u0004\b\u0010\u0010\u0011¨\u0006\u0017"},
   d2 = {"Lcom/volmit/iris/core/scripting/kotlin/runner/FileComponents;", "", "segment", "", "root", "", "<init>", "(Ljava/lang/String;Z)V", "getSegment", "()Ljava/lang/String;", "getRoot", "()Z", "children0", "", "children", "", "getChildren", "()Ljava/util/Collection;", "append", "hashCode", "", "equals", "other", "core"}
)
public final class FileComponents {
   @NotNull
   private final String segment;
   private final boolean root;
   @NotNull
   private final Map<String, FileComponents> children0;

   public FileComponents(@NotNull String var1, boolean var2) {
      Intrinsics.checkNotNullParameter(var1, "segment");
      super();
      this.segment = var1;
      this.root = var2;
      this.children0 = (Map)(new LinkedHashMap());
   }

   // $FF: synthetic method
   public FileComponents(String var1, boolean var2, int var3, DefaultConstructorMarker var4) {
      if ((var3 & 2) != 0) {
         var2 = false;
      }

      this(var1, var2);
   }

   @NotNull
   public final String getSegment() {
      return this.segment;
   }

   public final boolean getRoot() {
      return this.root;
   }

   @NotNull
   public final Collection<FileComponents> getChildren() {
      return this.children0.values();
   }

   @NotNull
   public final FileComponents append(@NotNull String var1) {
      Intrinsics.checkNotNullParameter(var1, "segment");
      Object var10000 = this.children0.computeIfAbsent(var1, FileComponents::append$lambda$1);
      Intrinsics.checkNotNullExpressionValue(var10000, "computeIfAbsent(...)");
      return (FileComponents)var10000;
   }

   public int hashCode() {
      int var1 = this.segment.hashCode();
      var1 = 31 * var1 + this.children0.hashCode();
      return var1;
   }

   public boolean equals(@Nullable Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof FileComponents)) {
         return false;
      } else if (!Intrinsics.areEqual(this.segment, ((FileComponents)var1).segment)) {
         return false;
      } else {
         return Intrinsics.areEqual(this.children0, ((FileComponents)var1).children0);
      }
   }

   private static final FileComponents append$lambda$0(String var0, String var1) {
      Intrinsics.checkNotNullParameter(var1, "it");
      return new FileComponents(var0, false, 2, (DefaultConstructorMarker)null);
   }

   private static final FileComponents append$lambda$1(Function1 var0, Object var1) {
      return (FileComponents)var0.invoke(var1);
   }
}
