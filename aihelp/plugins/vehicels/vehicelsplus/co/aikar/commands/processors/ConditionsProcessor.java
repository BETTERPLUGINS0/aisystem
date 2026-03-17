/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package co.aikar.commands.processors;

import co.aikar.commands.AnnotationProcessor;
import co.aikar.commands.CommandExecutionContext;
import co.aikar.commands.CommandOperationContext;
import co.aikar.commands.annotation.Conditions;

@Deprecated
public class ConditionsProcessor
implements AnnotationProcessor<Conditions> {
    @Override
    public void onPreComand(CommandOperationContext commandOperationContext) {
    }

    @Override
    public void onPostContextResolution(CommandExecutionContext commandExecutionContext, Object object) {
    }
}

