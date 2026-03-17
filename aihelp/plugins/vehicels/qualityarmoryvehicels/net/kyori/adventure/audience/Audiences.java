/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package net.kyori.adventure.audience;

import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Consumer;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import net.kyori.adventure.resource.ResourcePackCallback;
import net.kyori.adventure.text.ComponentLike;
import org.jetbrains.annotations.NotNull;

public final class Audiences {
    static final Collector<? super Audience, ?, ForwardingAudience> COLLECTOR = Collectors.collectingAndThen(Collectors.toCollection(ArrayList::new), arrayList -> Audience.audience(Collections.unmodifiableCollection(arrayList)));

    private Audiences() {
    }

    @NotNull
    public static Consumer<? super Audience> sendingMessage(@NotNull ComponentLike componentLike) {
        return audience -> audience.sendMessage(componentLike);
    }

    @NotNull
    static ResourcePackCallback unwrapCallback(Audience audience, Audience audience2, @NotNull ResourcePackCallback resourcePackCallback) {
        if (resourcePackCallback == ResourcePackCallback.noOp()) {
            return resourcePackCallback;
        }
        return (uUID, resourcePackStatus, audience3) -> resourcePackCallback.packEventReceived(uUID, resourcePackStatus, audience3 == audience2 ? audience : audience3);
    }
}

