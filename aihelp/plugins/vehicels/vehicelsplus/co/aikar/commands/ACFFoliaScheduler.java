/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 */
package co.aikar.commands;

import co.aikar.commands.ACFPaperScheduler;
import org.bukkit.Bukkit;

@Deprecated
public class ACFFoliaScheduler
extends ACFPaperScheduler {
    public ACFFoliaScheduler() {
        super(Bukkit.getAsyncScheduler());
    }
}

