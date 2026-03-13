package com.volmit.iris.core.scripting.environment;

import com.volmit.iris.core.scripting.kotlin.environment.IrisSimpleExecutionEnvironment;
import java.io.File;
import java.util.Map;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

public interface SimpleEnvironment {
   static SimpleEnvironment create() {
      return new IrisSimpleExecutionEnvironment();
   }

   static SimpleEnvironment create(@NonNull File projectDir) {
      if (projectDir == null) {
         throw new NullPointerException("projectDir is marked non-null but is null");
      } else {
         return new IrisSimpleExecutionEnvironment(projectDir);
      }
   }

   void configureProject();

   void execute(@NonNull String script);

   void execute(@NonNull String script, @NonNull Class<?> type, @Nullable Map<String, Object> vars);

   @Nullable
   Object evaluate(@NonNull String script);

   @Nullable
   Object evaluate(@NonNull String script, @NonNull Class<?> type, @Nullable Map<String, Object> vars);
}
