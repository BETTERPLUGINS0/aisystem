/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package net.kyori.adventure.identity;

import net.kyori.adventure.identity.Identity;
import org.jetbrains.annotations.NotNull;

public interface Identified {
    @NotNull
    public Identity identity();
}

