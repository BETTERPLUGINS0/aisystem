/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package co.aikar.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandExecutionContext;
import co.aikar.commands.CommandOperationContext;
import co.aikar.commands.RegisteredCommand;
import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.Set;
import org.jetbrains.annotations.Nullable;

@Deprecated
public interface AnnotationProcessor<T extends Annotation> {
    @Nullable
    default public Set<Class<?>> getApplicableParameters() {
        return null;
    }

    default public void onBaseCommandRegister(BaseCommand command, T annotation) {
    }

    default public void onCommandRegistered(RegisteredCommand command, T annotation) {
    }

    default public void onParameterRegistered(RegisteredCommand command, int parameterIndex, Parameter p, T annotation) {
    }

    default public void onPreComand(CommandOperationContext context) {
    }

    default public void onPostComand(CommandOperationContext context) {
    }

    default public void onPreContextResolution(CommandExecutionContext context) {
    }

    default public void onPostContextResolution(CommandExecutionContext context, Object resolvedValue) {
    }
}

