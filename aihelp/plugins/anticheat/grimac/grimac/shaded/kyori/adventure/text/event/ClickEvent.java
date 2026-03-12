package ac.grim.grimac.shaded.kyori.adventure.text.event;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.audience.Audience;
import ac.grim.grimac.shaded.kyori.adventure.builder.AbstractBuilder;
import ac.grim.grimac.shaded.kyori.adventure.dialog.DialogLike;
import ac.grim.grimac.shaded.kyori.adventure.internal.Internals;
import ac.grim.grimac.shaded.kyori.adventure.key.Key;
import ac.grim.grimac.shaded.kyori.adventure.key.Keyed;
import ac.grim.grimac.shaded.kyori.adventure.nbt.api.BinaryTagHolder;
import ac.grim.grimac.shaded.kyori.adventure.text.format.Style;
import ac.grim.grimac.shaded.kyori.adventure.text.format.StyleBuilderApplicable;
import ac.grim.grimac.shaded.kyori.adventure.util.Index;
import ac.grim.grimac.shaded.kyori.examination.Examinable;
import ac.grim.grimac.shaded.kyori.examination.ExaminableProperty;
import java.net.URL;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;

public final class ClickEvent implements Examinable, StyleBuilderApplicable {
   private final ClickEvent.Action action;
   private final ClickEvent.Payload payload;

   @NotNull
   public static ClickEvent openUrl(@NotNull final String url) {
      return new ClickEvent(ClickEvent.Action.OPEN_URL, ClickEvent.Payload.string(url));
   }

   @NotNull
   public static ClickEvent openUrl(@NotNull final URL url) {
      return openUrl(url.toExternalForm());
   }

   @NotNull
   public static ClickEvent openFile(@NotNull final String file) {
      return new ClickEvent(ClickEvent.Action.OPEN_FILE, ClickEvent.Payload.string(file));
   }

   @NotNull
   public static ClickEvent runCommand(@NotNull final String command) {
      return new ClickEvent(ClickEvent.Action.RUN_COMMAND, ClickEvent.Payload.string(command));
   }

   @NotNull
   public static ClickEvent suggestCommand(@NotNull final String command) {
      return new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, ClickEvent.Payload.string(command));
   }

   /** @deprecated */
   @Deprecated
   @NotNull
   public static ClickEvent changePage(@NotNull final String page) {
      Objects.requireNonNull(page, "page");
      return new ClickEvent(ClickEvent.Action.CHANGE_PAGE, ClickEvent.Payload.integer(Integer.parseInt(page)));
   }

   @NotNull
   public static ClickEvent changePage(final int page) {
      return new ClickEvent(ClickEvent.Action.CHANGE_PAGE, ClickEvent.Payload.integer(page));
   }

   @NotNull
   public static ClickEvent copyToClipboard(@NotNull final String text) {
      return new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, ClickEvent.Payload.string(text));
   }

   @NotNull
   public static ClickEvent callback(@NotNull final ClickCallback<Audience> function) {
      return ClickCallbackInternals.PROVIDER.create((ClickCallback)Objects.requireNonNull(function, "function"), ClickCallbackOptionsImpl.DEFAULT);
   }

   @NotNull
   public static ClickEvent callback(@NotNull final ClickCallback<Audience> function, @NotNull final ClickCallback.Options options) {
      return ClickCallbackInternals.PROVIDER.create((ClickCallback)Objects.requireNonNull(function, "function"), (ClickCallback.Options)Objects.requireNonNull(options, "options"));
   }

   @NotNull
   public static ClickEvent callback(@NotNull final ClickCallback<Audience> function, @NotNull final Consumer<ClickCallback.Options.Builder> optionsBuilder) {
      return ClickCallbackInternals.PROVIDER.create((ClickCallback)Objects.requireNonNull(function, "function"), (ClickCallback.Options)AbstractBuilder.configureAndBuild(ClickCallback.Options.builder(), (Consumer)Objects.requireNonNull(optionsBuilder, "optionsBuilder")));
   }

   @NotNull
   public static ClickEvent showDialog(@NotNull final DialogLike dialog) {
      Objects.requireNonNull(dialog, "dialog");
      return new ClickEvent(ClickEvent.Action.SHOW_DIALOG, ClickEvent.Payload.dialog(dialog));
   }

   /** @deprecated */
   @Deprecated
   @NotNull
   public static ClickEvent custom(@NotNull final Key key, @NotNull final String data) {
      return custom(key, BinaryTagHolder.binaryTagHolder(data));
   }

   @NotNull
   public static ClickEvent custom(@NotNull final Key key, @NotNull final BinaryTagHolder nbt) {
      Objects.requireNonNull(key, "key");
      Objects.requireNonNull(nbt, "nbt");
      return new ClickEvent(ClickEvent.Action.CUSTOM, ClickEvent.Payload.custom(key, nbt));
   }

   /** @deprecated */
   @Deprecated
   @NotNull
   public static ClickEvent clickEvent(@NotNull final ClickEvent.Action action, @NotNull final String value) {
      if (action == ClickEvent.Action.CHANGE_PAGE) {
         return changePage(value);
      } else if (!action.payloadType().equals(ClickEvent.Payload.Text.class)) {
         throw new IllegalArgumentException("Action " + action + " does not support string payloads");
      } else {
         return new ClickEvent(action, ClickEvent.Payload.string(value));
      }
   }

   @NotNull
   public static ClickEvent clickEvent(@NotNull final ClickEvent.Action action, @NotNull final ClickEvent.Payload payload) {
      return new ClickEvent(action, payload);
   }

   private ClickEvent(@NotNull final ClickEvent.Action action, @NotNull final ClickEvent.Payload payload) {
      if (!action.supports(payload)) {
         throw new IllegalArgumentException("Action " + action + " does not support payload " + payload);
      } else {
         this.action = (ClickEvent.Action)Objects.requireNonNull(action, "action");
         this.payload = (ClickEvent.Payload)Objects.requireNonNull(payload, "payload");
      }
   }

   @NotNull
   public ClickEvent.Action action() {
      return this.action;
   }

   /** @deprecated */
   @Deprecated
   @NotNull
   public String value() {
      if (this.payload instanceof ClickEvent.Payload.Text) {
         return ((ClickEvent.Payload.Text)this.payload).value();
      } else if (this.action == ClickEvent.Action.CHANGE_PAGE) {
         return String.valueOf(((ClickEvent.Payload.Int)this.payload).integer());
      } else {
         throw new IllegalStateException("Payload is not a string payload, is " + this.payload);
      }
   }

   @NotNull
   public ClickEvent.Payload payload() {
      return this.payload;
   }

   public void styleApply(@NotNull final Style.Builder style) {
      style.clickEvent(this);
   }

   public boolean equals(@Nullable final Object other) {
      if (this == other) {
         return true;
      } else if (other != null && this.getClass() == other.getClass()) {
         ClickEvent that = (ClickEvent)other;
         return this.action == that.action && Objects.equals(this.payload, that.payload);
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = this.action.hashCode();
      result = 31 * result + this.payload.hashCode();
      return result;
   }

   @NotNull
   public Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(ExaminableProperty.of("action", (Object)this.action), ExaminableProperty.of("payload", (Object)this.payload));
   }

   public String toString() {
      return Internals.toString(this);
   }

   public static enum Action {
      OPEN_URL("open_url", true, ClickEvent.Payload.Text.class),
      OPEN_FILE("open_file", false, ClickEvent.Payload.Text.class),
      RUN_COMMAND("run_command", true, ClickEvent.Payload.Text.class),
      SUGGEST_COMMAND("suggest_command", true, ClickEvent.Payload.Text.class),
      CHANGE_PAGE("change_page", true, ClickEvent.Payload.Int.class),
      COPY_TO_CLIPBOARD("copy_to_clipboard", true, ClickEvent.Payload.Text.class),
      SHOW_DIALOG("show_dialog", false, ClickEvent.Payload.Dialog.class),
      CUSTOM("custom", true, ClickEvent.Payload.Custom.class);

      public static final Index<String, ClickEvent.Action> NAMES = Index.create(ClickEvent.Action.class, (constant) -> {
         return constant.name;
      });
      private final String name;
      private final boolean readable;
      private final Class<? extends ClickEvent.Payload> payloadType;

      private Action(@NotNull final String name, final boolean readable, @NotNull final Class<? extends ClickEvent.Payload> payloadType) {
         this.name = name;
         this.readable = readable;
         this.payloadType = payloadType;
      }

      public boolean readable() {
         return this.readable;
      }

      public boolean supports(@NotNull final ClickEvent.Payload payload) {
         Objects.requireNonNull(payload, "payload");
         return this.payloadType.isAssignableFrom(payload.getClass());
      }

      @NotNull
      public Class<? extends ClickEvent.Payload> payloadType() {
         return this.payloadType;
      }

      @NotNull
      public String toString() {
         return this.name;
      }

      // $FF: synthetic method
      private static ClickEvent.Action[] $values() {
         return new ClickEvent.Action[]{OPEN_URL, OPEN_FILE, RUN_COMMAND, SUGGEST_COMMAND, CHANGE_PAGE, COPY_TO_CLIPBOARD, SHOW_DIALOG, CUSTOM};
      }
   }

   public interface Payload extends Examinable {
      @NotNull
      static ClickEvent.Payload.Text string(@NotNull final String value) {
         Objects.requireNonNull(value, "value");
         return new PayloadImpl.TextImpl(value);
      }

      @NotNull
      static ClickEvent.Payload.Int integer(final int integer) {
         return new PayloadImpl.IntImpl(integer);
      }

      @NotNull
      static ClickEvent.Payload.Dialog dialog(@NotNull final DialogLike dialog) {
         Objects.requireNonNull(dialog, "dialog");
         return new PayloadImpl.DialogImpl(dialog);
      }

      /** @deprecated */
      @Deprecated
      @NotNull
      static ClickEvent.Payload.Custom custom(@NotNull final Key key, @NotNull final String data) {
         return custom(key, BinaryTagHolder.binaryTagHolder(data));
      }

      @NotNull
      static ClickEvent.Payload.Custom custom(@NotNull final Key key, @NotNull final BinaryTagHolder nbt) {
         Objects.requireNonNull(key, "key");
         Objects.requireNonNull(nbt, "nbt");
         return new PayloadImpl.CustomImpl(key, nbt);
      }

      public interface Custom extends ClickEvent.Payload, Keyed {
         /** @deprecated */
         @Deprecated
         @NotNull
         String data();

         @NotNull
         BinaryTagHolder nbt();
      }

      public interface Dialog extends ClickEvent.Payload {
         @NotNull
         DialogLike dialog();
      }

      public interface Int extends ClickEvent.Payload {
         int integer();
      }

      public interface Text extends ClickEvent.Payload {
         @NotNull
         String value();
      }
   }
}
