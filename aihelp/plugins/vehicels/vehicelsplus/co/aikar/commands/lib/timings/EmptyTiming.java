/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package co.aikar.commands.lib.timings;

import co.aikar.commands.lib.timings.MCTiming;

class EmptyTiming
extends MCTiming {
    EmptyTiming() {
    }

    @Override
    public final MCTiming startTiming() {
        return this;
    }

    @Override
    public final void stopTiming() {
    }
}

