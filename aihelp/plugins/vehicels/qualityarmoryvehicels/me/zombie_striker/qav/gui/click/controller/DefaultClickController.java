/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package me.zombie_striker.qav.gui.click.controller;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import me.zombie_striker.qav.gui.click.controller.ClickController;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class DefaultClickController
implements ClickController {
    private final CompletableFuture<Boolean> deferred = new CompletableFuture();
    private boolean completingLater = false;

    public DefaultClickController(@NotNull BiConsumer<Boolean, ? super Throwable> biConsumer) {
        this.deferred.whenComplete(biConsumer);
    }

    @Override
    public boolean isDone() {
        return this.deferred.isDone() || this.deferred.isCancelled();
    }

    @Override
    public void complete(@Nullable Throwable throwable) {
        if (this.isDone()) {
            return;
        }
        if (throwable != null) {
            this.deferred.completeExceptionally(throwable);
            return;
        }
        this.deferred.complete(true);
    }

    @Override
    public boolean completingLater() {
        return this.completingLater;
    }

    @Override
    public void completingLater(boolean bl) {
        this.completingLater = bl;
    }
}

