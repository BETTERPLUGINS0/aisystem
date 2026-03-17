/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package co.aikar.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandIssuer;
import co.aikar.commands.RegisteredCommand;
import java.util.List;

@FunctionalInterface
public interface ExceptionHandler {
    public boolean execute(BaseCommand var1, RegisteredCommand var2, CommandIssuer var3, List<String> var4, Throwable var5);
}

