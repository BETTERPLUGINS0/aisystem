/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package co.aikar.commands;

import co.aikar.commands.CommandIssuer;
import java.util.Locale;

public interface IssuerLocaleChangedCallback<I extends CommandIssuer> {
    public void onIssuerLocaleChange(I var1, Locale var2, Locale var3);
}

