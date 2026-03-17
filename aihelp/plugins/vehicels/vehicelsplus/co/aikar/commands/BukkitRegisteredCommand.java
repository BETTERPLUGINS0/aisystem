/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package co.aikar.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.BukkitCommandExecutionContext;
import co.aikar.commands.RegisteredCommand;
import java.lang.reflect.Method;

public class BukkitRegisteredCommand
extends RegisteredCommand<BukkitCommandExecutionContext> {
    BukkitRegisteredCommand(BaseCommand baseCommand, String string, Method method, String string2) {
        super(baseCommand, string, method, string2);
    }
}

