/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package org.apache.commons.io.function;

import java.io.IOException;
import org.apache.commons.io.function.Uncheck;

@FunctionalInterface
public interface IORunnable {
    default public Runnable asRunnable() {
        return () -> Uncheck.run(this);
    }

    public void run() throws IOException;
}

