/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package co.aikar.commands;

import co.aikar.commands.InvalidCommandArgument;
import co.aikar.locales.MessageKey;
import co.aikar.locales.MessageKeyProvider;

public class ConditionFailedException
extends InvalidCommandArgument {
    public ConditionFailedException() {
        super(false);
    }

    public ConditionFailedException(MessageKeyProvider messageKeyProvider, String ... stringArray) {
        super(messageKeyProvider, false, stringArray);
    }

    public ConditionFailedException(MessageKey messageKey, String ... stringArray) {
        super(messageKey, false, stringArray);
    }

    public ConditionFailedException(String string) {
        super(string, false);
    }
}

