/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package co.aikar.commands.lib.timings;

public abstract class MCTiming
implements AutoCloseable {
    public abstract MCTiming startTiming();

    public abstract void stopTiming();

    @Override
    public void close() {
        this.stopTiming();
    }
}

