package com.volmit.iris.core.scripting.kotlin.environment;

import java.io.File;
import java.util.Comparator;
import kotlin.Metadata;
import kotlin.comparisons.ComparisonsKt;
import kotlin.jvm.internal.SourceDebugExtension;

@Metadata(
   mv = {2, 2, 0},
   k = 3,
   xi = 48
)
@SourceDebugExtension({"SMAP\nComparisons.kt\nKotlin\n*S Kotlin\n*F\n+ 1 Comparisons.kt\nkotlin/comparisons/ComparisonsKt__ComparisonsKt$compareBy$2\n+ 2 IrisSimpleExecutionEnvironment.kt\ncom/volmit/iris/core/scripting/kotlin/environment/IrisSimpleExecutionEnvironment\n*L\n1#1,328:1\n83#2:329\n*E\n"})
public final class IrisSimpleExecutionEnvironment$configureProject$$inlined$sortedBy$1<T> implements Comparator {
   public final int compare(T var1, T var2) {
      File var3 = (File)var1;
      boolean var4 = false;
      Comparable var10000 = (Comparable)var3.getAbsolutePath();
      var3 = (File)var2;
      Comparable var5 = var10000;
      var4 = false;
      return ComparisonsKt.compareValues(var5, (Comparable)var3.getAbsolutePath());
   }
}
