/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.kyori.adventure.resource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Stream;
import net.kyori.adventure.internal.Internals;
import net.kyori.adventure.resource.ResourcePackInfo;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class ResourcePackInfoImpl
implements ResourcePackInfo {
    private final UUID id;
    private final URI uri;
    private final String hash;

    ResourcePackInfoImpl(@NotNull UUID uUID, @NotNull URI uRI, @NotNull String string) {
        this.id = Objects.requireNonNull(uUID, "id");
        this.uri = Objects.requireNonNull(uRI, "uri");
        this.hash = Objects.requireNonNull(string, "hash");
    }

    @Override
    @NotNull
    public UUID id() {
        return this.id;
    }

    @Override
    @NotNull
    public URI uri() {
        return this.uri;
    }

    @Override
    @NotNull
    public String hash() {
        return this.hash;
    }

    @Override
    @NotNull
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(ExaminableProperty.of("id", this.id), ExaminableProperty.of("uri", this.uri), ExaminableProperty.of("hash", this.hash));
    }

    public String toString() {
        return Internals.toString(this);
    }

    public boolean equals(@Nullable Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof ResourcePackInfoImpl)) {
            return false;
        }
        ResourcePackInfoImpl resourcePackInfoImpl = (ResourcePackInfoImpl)object;
        return this.id.equals(resourcePackInfoImpl.id) && this.uri.equals(resourcePackInfoImpl.uri) && this.hash.equals(resourcePackInfoImpl.hash);
    }

    public int hashCode() {
        int n = this.id.hashCode();
        n = 31 * n + this.uri.hashCode();
        n = 31 * n + this.hash.hashCode();
        return n;
    }

    static CompletableFuture<String> computeHash(URI uRI, Executor executor) {
        CompletableFuture<String> completableFuture = new CompletableFuture<String>();
        executor.execute(() -> {
            try {
                URL uRL = uRI.toURL();
                URLConnection uRLConnection = uRL.openConnection();
                uRLConnection.addRequestProperty("User-Agent", "adventure/" + ResourcePackInfoImpl.class.getPackage().getSpecificationVersion() + " (pack-fetcher)");
                try (InputStream inputStream = uRLConnection.getInputStream();){
                    int n;
                    MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
                    byte[] byArray = new byte[8192];
                    while ((n = inputStream.read(byArray)) != -1) {
                        messageDigest.update(byArray, 0, n);
                    }
                    completableFuture.complete(ResourcePackInfoImpl.bytesToString(messageDigest.digest()));
                }
            } catch (IOException | NoSuchAlgorithmException exception) {
                completableFuture.completeExceptionally(exception);
            }
        });
        return completableFuture;
    }

    static String bytesToString(byte[] byArray) {
        StringBuilder stringBuilder = new StringBuilder(byArray.length * 2);
        Formatter formatter = new Formatter(stringBuilder, Locale.ROOT);
        for (int i = 0; i < byArray.length; ++i) {
            formatter.format("%02x", byArray[i] & 0xFF);
        }
        return stringBuilder.toString();
    }

    static final class BuilderImpl
    implements ResourcePackInfo.Builder {
        private UUID id;
        private URI uri;
        private String hash;

        BuilderImpl() {
        }

        @Override
        @NotNull
        public ResourcePackInfo.Builder id(@NotNull UUID uUID) {
            this.id = Objects.requireNonNull(uUID, "id");
            return this;
        }

        @Override
        @NotNull
        public ResourcePackInfo.Builder uri(@NotNull URI uRI) {
            this.uri = Objects.requireNonNull(uRI, "uri");
            if (this.id == null) {
                this.id = UUID.nameUUIDFromBytes(uRI.toString().getBytes(StandardCharsets.UTF_8));
            }
            return this;
        }

        @Override
        @NotNull
        public ResourcePackInfo.Builder hash(@NotNull String string) {
            this.hash = Objects.requireNonNull(string, "hash");
            return this;
        }

        @Override
        @NotNull
        public ResourcePackInfo build() {
            return new ResourcePackInfoImpl(this.id, this.uri, this.hash);
        }

        @Override
        @NotNull
        public CompletableFuture<ResourcePackInfo> computeHashAndBuild(@NotNull Executor executor) {
            return ResourcePackInfoImpl.computeHash(Objects.requireNonNull(this.uri, "uri"), executor).thenApply(string -> {
                this.hash((String)string);
                return this.build();
            });
        }
    }
}

