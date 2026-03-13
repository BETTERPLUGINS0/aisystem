/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  me.konsolas.aac.api.AACExemption
 *  org.bukkit.entity.Player
 *  org.jetbrains.annotations.NotNull
 */
package net.advancedplugins.as.impl.effects.effects.hook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import me.konsolas.aac.api.AACExemption;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface AntiCheatHook {
    public static final List<UUID> generalExemptions = new ArrayList<UUID>();
    public static final Map<UUID, AACExemption> aac5Exemptions = new HashMap<UUID, AACExemption>();

    public boolean isPresent();

    public boolean isRunning();

    public void register();

    public boolean exemptDebug(@NotNull Player var1);

    public boolean exempt(@NotNull Player var1);

    public void nonExempt(@NotNull Player var1);

    public AntiCheatHook getHook();
}

