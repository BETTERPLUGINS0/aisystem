/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.kyori.adventure.resource;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import net.kyori.adventure.internal.Internals;
import net.kyori.adventure.resource.ResourcePackCallback;
import net.kyori.adventure.resource.ResourcePackInfo;
import net.kyori.adventure.resource.ResourcePackInfoLike;
import net.kyori.adventure.resource.ResourcePackRequest;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.util.MonkeyBars;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class ResourcePackRequestImpl
implements ResourcePackRequest {
    private final List<ResourcePackInfo> packs;
    private final ResourcePackCallback cb;
    private final boolean replace;
    private final boolean required;
    @Nullable
    private final Component prompt;

    ResourcePackRequestImpl(List<ResourcePackInfo> list, ResourcePackCallback resourcePackCallback, boolean bl, boolean bl2, @Nullable Component component) {
        this.packs = list;
        this.cb = resourcePackCallback;
        this.replace = bl;
        this.required = bl2;
        this.prompt = component;
    }

    @Override
    @NotNull
    public List<ResourcePackInfo> packs() {
        return this.packs;
    }

    @Override
    @NotNull
    public ResourcePackRequest packs(@NotNull Iterable<? extends ResourcePackInfoLike> iterable) {
        if (this.packs.equals(iterable)) {
            return this;
        }
        return new ResourcePackRequestImpl(MonkeyBars.toUnmodifiableList(ResourcePackInfoLike::asResourcePackInfo, iterable), this.cb, this.replace, this.required, this.prompt);
    }

    @Override
    @NotNull
    public ResourcePackCallback callback() {
        return this.cb;
    }

    @Override
    @NotNull
    public ResourcePackRequest callback(@NotNull ResourcePackCallback resourcePackCallback) {
        if (resourcePackCallback == this.cb) {
            return this;
        }
        return new ResourcePackRequestImpl(this.packs, Objects.requireNonNull(resourcePackCallback, "cb"), this.replace, this.required, this.prompt);
    }

    @Override
    public boolean replace() {
        return this.replace;
    }

    @Override
    public boolean required() {
        return this.required;
    }

    @Override
    @Nullable
    public Component prompt() {
        return this.prompt;
    }

    @Override
    @NotNull
    public ResourcePackRequest replace(boolean bl) {
        if (bl == this.replace) {
            return this;
        }
        return new ResourcePackRequestImpl(this.packs, this.cb, bl, this.required, this.prompt);
    }

    public boolean equals(@Nullable Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        ResourcePackRequestImpl resourcePackRequestImpl = (ResourcePackRequestImpl)object;
        return this.replace == resourcePackRequestImpl.replace && Objects.equals(this.packs, resourcePackRequestImpl.packs) && Objects.equals(this.cb, resourcePackRequestImpl.cb) && this.required == resourcePackRequestImpl.required && Objects.equals(this.prompt, resourcePackRequestImpl.prompt);
    }

    public int hashCode() {
        return Objects.hash(this.packs, this.cb, this.replace, this.required, this.prompt);
    }

    @NotNull
    public String toString() {
        return Internals.toString(this);
    }

    @Override
    @NotNull
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(ExaminableProperty.of("packs", this.packs), ExaminableProperty.of("callback", this.cb), ExaminableProperty.of("replace", this.replace), ExaminableProperty.of("required", this.required), ExaminableProperty.of("prompt", this.prompt));
    }

    static final class BuilderImpl
    implements ResourcePackRequest.Builder {
        private List<ResourcePackInfo> packs;
        private ResourcePackCallback cb;
        private boolean replace;
        private boolean required;
        @Nullable
        private Component prompt;

        BuilderImpl() {
            this.packs = Collections.emptyList();
            this.cb = ResourcePackCallback.noOp();
            this.replace = false;
        }

        BuilderImpl(@NotNull ResourcePackRequest resourcePackRequest) {
            this.packs = resourcePackRequest.packs();
            this.cb = resourcePackRequest.callback();
            this.replace = resourcePackRequest.replace();
            this.required = resourcePackRequest.required();
            this.prompt = resourcePackRequest.prompt();
        }

        @Override
        @NotNull
        public ResourcePackRequest.Builder packs(@NotNull ResourcePackInfoLike resourcePackInfoLike, @NotNull @NotNull ResourcePackInfoLike @NotNull ... resourcePackInfoLikeArray) {
            this.packs = MonkeyBars.nonEmptyArrayToList(ResourcePackInfoLike::asResourcePackInfo, resourcePackInfoLike, resourcePackInfoLikeArray);
            return this;
        }

        @Override
        @NotNull
        public ResourcePackRequest.Builder packs(@NotNull Iterable<? extends ResourcePackInfoLike> iterable) {
            this.packs = MonkeyBars.toUnmodifiableList(ResourcePackInfoLike::asResourcePackInfo, iterable);
            return this;
        }

        @Override
        @NotNull
        public ResourcePackRequest.Builder callback(@NotNull ResourcePackCallback resourcePackCallback) {
            this.cb = Objects.requireNonNull(resourcePackCallback, "cb");
            return this;
        }

        @Override
        @NotNull
        public ResourcePackRequest.Builder replace(boolean bl) {
            this.replace = bl;
            return this;
        }

        @Override
        @NotNull
        public ResourcePackRequest.Builder required(boolean bl) {
            this.required = bl;
            return this;
        }

        @Override
        @NotNull
        public ResourcePackRequest.Builder prompt(@Nullable Component component) {
            this.prompt = component;
            return this;
        }

        @Override
        @NotNull
        public ResourcePackRequest build() {
            return new ResourcePackRequestImpl(this.packs, this.cb, this.replace, this.required, this.prompt);
        }
    }
}

