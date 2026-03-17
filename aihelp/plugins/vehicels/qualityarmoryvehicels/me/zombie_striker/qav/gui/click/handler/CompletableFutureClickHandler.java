/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package me.zombie_striker.qav.gui.click.handler;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import me.zombie_striker.qav.gui.click.ClickContext;
import me.zombie_striker.qav.gui.click.action.GuiClickAction;
import me.zombie_striker.qav.gui.click.action.RunnableGuiClickAction;
import me.zombie_striker.qav.gui.click.controller.ClickController;
import me.zombie_striker.qav.gui.click.handler.ClickHandler;
import me.zombie_striker.qav.gui.exception.TriumphGuiException;
import org.jetbrains.annotations.NotNull;

public final class CompletableFutureClickHandler<P>
implements ClickHandler<P> {
    private final long timeout;
    private final TimeUnit unit;

    public CompletableFutureClickHandler() {
        this(6L, TimeUnit.SECONDS);
    }

    public CompletableFutureClickHandler(long l, @NotNull TimeUnit timeUnit) {
        this.timeout = l;
        this.unit = timeUnit;
    }

    @Override
    public void handle(@NotNull P p, @NotNull ClickContext clickContext, @NotNull GuiClickAction<P> guiClickAction, @NotNull ClickController clickController) {
        if (!(guiClickAction instanceof RunnableGuiClickAction)) {
            throw new TriumphGuiException("The click action type '" + guiClickAction.getClass().getName() + "' is supported by the 'CompletableFutureClickHandler'.");
        }
        clickController.completingLater(true);
        CompletableFuture.runAsync(() -> ((RunnableGuiClickAction)guiClickAction).run(p, clickContext)).orTimeout(this.timeout, this.unit).whenComplete((void_, throwable) -> clickController.complete((Throwable)throwable));
    }
}

