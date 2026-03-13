/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.as.impl.effects.effects.utils;

import java.lang.invoke.LambdaMetafactory;
import java.util.HashMap;
import java.util.UUID;
import java.util.function.Function;
import net.advancedplugins.as.impl.utils.SchedulerUtils;

public final class Combo {
    private static final HashMap<UUID, ComboEntry> combos = new HashMap();

    public static void add(UUID uUID) {
        ++Combo.combos.computeIfAbsent((UUID)uUID, (Function<UUID, ComboEntry>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, lambda$add$2(java.util.UUID ), (Ljava/util/UUID;)Lnet/advancedplugins/as/impl/effects/effects/utils/Combo$ComboEntry;)()).combo;
    }

    public static int getCombos(UUID uUID) {
        if (!combos.containsKey(uUID)) {
            return 0;
        }
        return Combo.combos.get((Object)uUID).combo;
    }

    public static void endCombos(UUID uUID) {
        combos.remove(uUID);
    }

    private Combo() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    private static /* synthetic */ ComboEntry lambda$add$2(UUID uUID) {
        return new ComboEntry(0, System.currentTimeMillis());
    }

    static {
        SchedulerUtils.runTaskTimerAsync(() -> combos.entrySet().removeIf(entry -> System.currentTimeMillis() - ((ComboEntry)entry.getValue()).insertTime > 2000L), 20L, 20L);
    }

    private static class ComboEntry {
        private int combo;
        private final long insertTime;

        public ComboEntry(int n, long l) {
            this.combo = n;
            this.insertTime = l;
        }
    }
}

