package fr.xephi.authme.libs.net.kyori.adventure.resource;

import fr.xephi.authme.libs.net.kyori.adventure.internal.Internals;
import fr.xephi.authme.libs.net.kyori.examination.ExaminableProperty;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class ResourcePackInfoImpl implements ResourcePackInfo {
   private final UUID id;
   private final URI uri;
   private final String hash;

   ResourcePackInfoImpl(@NotNull final UUID id, @NotNull final URI uri, @NotNull final String hash) {
      this.id = (UUID)Objects.requireNonNull(id, "id");
      this.uri = (URI)Objects.requireNonNull(uri, "uri");
      this.hash = (String)Objects.requireNonNull(hash, "hash");
   }

   @NotNull
   public UUID id() {
      return this.id;
   }

   @NotNull
   public URI uri() {
      return this.uri;
   }

   @NotNull
   public String hash() {
      return this.hash;
   }

   @NotNull
   public Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(ExaminableProperty.of("id", (Object)this.id), ExaminableProperty.of("uri", (Object)this.uri), ExaminableProperty.of("hash", this.hash));
   }

   public String toString() {
      return Internals.toString(this);
   }

   public boolean equals(@Nullable final Object other) {
      if (this == other) {
         return true;
      } else if (!(other instanceof ResourcePackInfoImpl)) {
         return false;
      } else {
         ResourcePackInfoImpl that = (ResourcePackInfoImpl)other;
         return this.id.equals(that.id) && this.uri.equals(that.uri) && this.hash.equals(that.hash);
      }
   }

   public int hashCode() {
      int result = this.id.hashCode();
      result = 31 * result + this.uri.hashCode();
      result = 31 * result + this.hash.hashCode();
      return result;
   }

   static CompletableFuture<String> computeHash(final URI uri, final Executor exec) {
      CompletableFuture<String> result = new CompletableFuture();
      exec.execute(() -> {
         try {
            URL url = uri.toURL();
            URLConnection conn = url.openConnection();
            conn.addRequestProperty("User-Agent", "adventure/" + ResourcePackInfoImpl.class.getPackage().getSpecificationVersion() + " (pack-fetcher)");
            InputStream is = conn.getInputStream();

            try {
               MessageDigest digest = MessageDigest.getInstance("SHA-1");
               byte[] buf = new byte[8192];

               while(true) {
                  int read;
                  if ((read = is.read(buf)) == -1) {
                     result.complete(bytesToString(digest.digest()));
                     break;
                  }

                  digest.update(buf, 0, read);
               }
            } catch (Throwable var9) {
               if (is != null) {
                  try {
                     is.close();
                  } catch (Throwable var8) {
                     var9.addSuppressed(var8);
                  }
               }

               throw var9;
            }

            if (is != null) {
               is.close();
            }
         } catch (NoSuchAlgorithmException | IOException var10) {
            result.completeExceptionally(var10);
         }

      });
      return result;
   }

   static String bytesToString(final byte[] arr) {
      StringBuilder builder = new StringBuilder(arr.length * 2);
      Formatter fmt = new Formatter(builder, Locale.ROOT);

      for(int i = 0; i < arr.length; ++i) {
         fmt.format("%02x", arr[i] & 255);
      }

      return builder.toString();
   }

   static final class BuilderImpl implements ResourcePackInfo.Builder {
      private UUID id;
      private URI uri;
      private String hash;

      @NotNull
      public ResourcePackInfo.Builder id(@NotNull final UUID id) {
         this.id = (UUID)Objects.requireNonNull(id, "id");
         return this;
      }

      @NotNull
      public ResourcePackInfo.Builder uri(@NotNull final URI uri) {
         this.uri = (URI)Objects.requireNonNull(uri, "uri");
         if (this.id == null) {
            this.id = UUID.nameUUIDFromBytes(uri.toString().getBytes(StandardCharsets.UTF_8));
         }

         return this;
      }

      @NotNull
      public ResourcePackInfo.Builder hash(@NotNull final String hash) {
         this.hash = (String)Objects.requireNonNull(hash, "hash");
         return this;
      }

      @NotNull
      public ResourcePackInfo build() {
         return new ResourcePackInfoImpl(this.id, this.uri, this.hash);
      }

      @NotNull
      public CompletableFuture<ResourcePackInfo> computeHashAndBuild(@NotNull final Executor executor) {
         return ResourcePackInfoImpl.computeHash((URI)Objects.requireNonNull(this.uri, "uri"), executor).thenApply((hash) -> {
            this.hash(hash);
            return this.build();
         });
      }
   }
}
