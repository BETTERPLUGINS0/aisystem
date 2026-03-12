package fr.xephi.authme.libs.net.kyori.adventure.resource;

import fr.xephi.authme.libs.net.kyori.adventure.internal.Internals;
import fr.xephi.authme.libs.net.kyori.adventure.text.Component;
import fr.xephi.authme.libs.net.kyori.adventure.util.MonkeyBars;
import fr.xephi.authme.libs.net.kyori.examination.ExaminableProperty;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class ResourcePackRequestImpl implements ResourcePackRequest {
   private final List<ResourcePackInfo> packs;
   private final ResourcePackCallback cb;
   private final boolean replace;
   private final boolean required;
   @Nullable
   private final Component prompt;

   ResourcePackRequestImpl(final List<ResourcePackInfo> packs, final ResourcePackCallback cb, final boolean replace, final boolean required, @Nullable final Component prompt) {
      this.packs = packs;
      this.cb = cb;
      this.replace = replace;
      this.required = required;
      this.prompt = prompt;
   }

   @NotNull
   public List<ResourcePackInfo> packs() {
      return this.packs;
   }

   @NotNull
   public ResourcePackRequest packs(@NotNull final Iterable<? extends ResourcePackInfoLike> packs) {
      return this.packs.equals(packs) ? this : new ResourcePackRequestImpl(MonkeyBars.toUnmodifiableList(ResourcePackInfoLike::asResourcePackInfo, packs), this.cb, this.replace, this.required, this.prompt);
   }

   @NotNull
   public ResourcePackCallback callback() {
      return this.cb;
   }

   @NotNull
   public ResourcePackRequest callback(@NotNull final ResourcePackCallback cb) {
      return cb == this.cb ? this : new ResourcePackRequestImpl(this.packs, (ResourcePackCallback)Objects.requireNonNull(cb, "cb"), this.replace, this.required, this.prompt);
   }

   public boolean replace() {
      return this.replace;
   }

   public boolean required() {
      return this.required;
   }

   @Nullable
   public Component prompt() {
      return this.prompt;
   }

   @NotNull
   public ResourcePackRequest replace(final boolean replace) {
      return replace == this.replace ? this : new ResourcePackRequestImpl(this.packs, this.cb, replace, this.required, this.prompt);
   }

   public boolean equals(@Nullable final Object other) {
      if (this == other) {
         return true;
      } else if (other != null && this.getClass() == other.getClass()) {
         ResourcePackRequestImpl that = (ResourcePackRequestImpl)other;
         return this.replace == that.replace && Objects.equals(this.packs, that.packs) && Objects.equals(this.cb, that.cb) && this.required == that.required && Objects.equals(this.prompt, that.prompt);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.packs, this.cb, this.replace, this.required, this.prompt});
   }

   @NotNull
   public String toString() {
      return Internals.toString(this);
   }

   @NotNull
   public Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(ExaminableProperty.of("packs", (Object)this.packs), ExaminableProperty.of("callback", (Object)this.cb), ExaminableProperty.of("replace", this.replace), ExaminableProperty.of("required", this.required), ExaminableProperty.of("prompt", (Object)this.prompt));
   }

   static final class BuilderImpl implements ResourcePackRequest.Builder {
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

      BuilderImpl(@NotNull final ResourcePackRequest req) {
         this.packs = req.packs();
         this.cb = req.callback();
         this.replace = req.replace();
         this.required = req.required();
         this.prompt = req.prompt();
      }

      @NotNull
      public ResourcePackRequest.Builder packs(@NotNull final ResourcePackInfoLike first, @NotNull final ResourcePackInfoLike... others) {
         this.packs = MonkeyBars.nonEmptyArrayToList(ResourcePackInfoLike::asResourcePackInfo, first, others);
         return this;
      }

      @NotNull
      public ResourcePackRequest.Builder packs(@NotNull final Iterable<? extends ResourcePackInfoLike> packs) {
         this.packs = MonkeyBars.toUnmodifiableList(ResourcePackInfoLike::asResourcePackInfo, packs);
         return this;
      }

      @NotNull
      public ResourcePackRequest.Builder callback(@NotNull final ResourcePackCallback cb) {
         this.cb = (ResourcePackCallback)Objects.requireNonNull(cb, "cb");
         return this;
      }

      @NotNull
      public ResourcePackRequest.Builder replace(final boolean replace) {
         this.replace = replace;
         return this;
      }

      @NotNull
      public ResourcePackRequest.Builder required(final boolean required) {
         this.required = required;
         return this;
      }

      @NotNull
      public ResourcePackRequest.Builder prompt(@Nullable final Component prompt) {
         this.prompt = prompt;
         return this;
      }

      @NotNull
      public ResourcePackRequest build() {
         return new ResourcePackRequestImpl(this.packs, this.cb, this.replace, this.required, this.prompt);
      }
   }
}
