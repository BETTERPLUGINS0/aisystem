package com.volmit.iris.core.scripting.kotlin.runner;

import java.util.Map;
import kotlin.Metadata;
import kotlin.script.experimental.api.EvaluationResult;
import kotlin.script.experimental.api.ResultWithDiagnostics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(
   mv = {2, 2, 0},
   k = 1,
   xi = 48,
   d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010$\n\u0002\u0010\u000e\n\u0000\bf\u0018\u00002\u00020\u0001J&\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\u0016\u0010\u0005\u001a\u0012\u0012\u0004\u0012\u00020\u0007\u0012\u0006\u0012\u0004\u0018\u00010\u0001\u0018\u00010\u0006H&¨\u0006\bÀ\u0006\u0003"},
   d2 = {"Lcom/volmit/iris/core/scripting/kotlin/runner/Script;", "", "evaluate", "Lkotlin/script/experimental/api/ResultWithDiagnostics;", "Lkotlin/script/experimental/api/EvaluationResult;", "properties", "", "", "core"}
)
public interface Script {
   @NotNull
   ResultWithDiagnostics<EvaluationResult> evaluate(@Nullable Map<String, ? extends Object> var1);
}
