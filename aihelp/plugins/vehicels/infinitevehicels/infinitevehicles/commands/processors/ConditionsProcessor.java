package me.PM2.infinitevehicles.commands.processors;

import me.PM2.infinitevehicles.commands.AnnotationProcessor;
import me.PM2.infinitevehicles.commands.CommandExecutionContext;
import me.PM2.infinitevehicles.commands.CommandOperationContext;
import me.PM2.infinitevehicles.commands.annotation.Conditions;

/** @deprecated */
@Deprecated
public class ConditionsProcessor implements AnnotationProcessor<Conditions> {
   public void onPreComand(CommandOperationContext context) {
   }

   public void onPostContextResolution(CommandExecutionContext context, Object resolvedValue) {
   }
}
