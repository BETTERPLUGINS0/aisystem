/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package me.zombie_striker.qav.gui.click.processor;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.time.LocalTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import me.zombie_striker.qav.gui.AbstractGuiView;
import me.zombie_striker.qav.gui.click.ClickContext;
import me.zombie_striker.qav.gui.click.action.EmptyGuiClickAction;
import me.zombie_striker.qav.gui.click.action.GuiClickAction;
import me.zombie_striker.qav.gui.click.controller.DefaultClickController;
import me.zombie_striker.qav.gui.click.handler.ClickHandler;
import me.zombie_striker.qav.gui.item.RenderedGuiItem;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ClickProcessor<P, I> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClickProcessor.class);
    private final long spamPreventionDuration;
    private final Cache<UUID, LocalTime> spamPrevention;
    private boolean isProcessing = false;

    public ClickProcessor(long l) {
        this.spamPreventionDuration = l;
        this.spamPrevention = CacheBuilder.newBuilder().expireAfterWrite(l, TimeUnit.MILLISECONDS).build();
    }

    public void processClick(int n, @NotNull AbstractGuiView<P, I> abstractGuiView) {
        UUID uUID = abstractGuiView.viewerUuid();
        if (!this.canClick(uUID)) {
            return;
        }
        RenderedGuiItem<P, I> renderedGuiItem = abstractGuiView.getItem(n);
        if (renderedGuiItem == null) {
            return;
        }
        GuiClickAction<P> guiClickAction = renderedGuiItem.action();
        if (guiClickAction instanceof EmptyGuiClickAction) {
            return;
        }
        this.isProcessing = true;
        ClickContext clickContext = new ClickContext();
        ClickHandler<P> clickHandler = renderedGuiItem.clickHandler();
        DefaultClickController defaultClickController = new DefaultClickController((bl, throwable) -> {
            if (throwable != null) {
                LOGGER.error("An exception occurred while processing click for '{}' on slot '{}'.", abstractGuiView.viewerName(), n, throwable);
            }
            this.isProcessing = false;
        });
        Exception exception = null;
        try {
            clickHandler.handle(abstractGuiView.viewer(), clickContext, guiClickAction, defaultClickController);
        } catch (Exception exception2) {
            exception = exception2;
        }
        if (!defaultClickController.completingLater()) {
            defaultClickController.complete(exception);
        }
    }

    private boolean canClick(@NotNull UUID uUID) {
        if (this.spamPreventionDuration != 0L) {
            LocalTime localTime = this.spamPrevention.getIfPresent(uUID);
            if (localTime != null) {
                return false;
            }
            this.spamPrevention.put(uUID, LocalTime.now());
        }
        return !this.isProcessing;
    }
}

