package ac.grim.grimac.utils.data.webhook.discord;

import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.utils.data.json.JsonSerializable;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Objects;
import lombok.Generated;

public class WebhookMessage implements JsonSerializable {
   public static final int MAX_CONTENT_LENGTH = 2000;
   public static final int MAX_EMBEDS = 10;
   @Nullable
   private String content;
   @Nullable
   private String username;
   @Nullable
   private String avatar;
   @Nullable
   private Boolean tts;
   @NotNull
   @Nullable
   private Embed[] embeds;

   public WebhookMessage() {
   }

   public WebhookMessage(@NotNull JsonObject json) {
      JsonElement element;
      if ((element = json.get("content")) != null) {
         this.content(element.getAsString());
      }

      if ((element = json.get("username")) != null) {
         this.username(element.getAsString());
      }

      if ((element = json.get("avatar_url")) != null) {
         this.avatar(element.getAsString());
      }

      if ((element = json.get("tts")) != null) {
         this.tts(element.getAsBoolean());
      }

      if ((element = json.get("embeds")) != null) {
         this.embeds((Embed[])JsonSerializable.deserializeArray(element.getAsJsonArray(), (x$0) -> {
            return new Embed[x$0];
         }, Embed::new));
      }

   }

   @Contract(
      value = "_ -> this",
      mutates = "this"
   )
   @NotNull
   public WebhookMessage content(@Nullable String content) {
      if (content != null && content.length() > 2000) {
         throw new IllegalArgumentException("Webhook content too long, " + content.length() + " > 2000");
      } else {
         this.content = content;
         return this;
      }
   }

   @Contract(
      value = "_ -> this",
      mutates = "this"
   )
   @NotNull
   public WebhookMessage embeds(@NotNull @Nullable Embed[] embeds) {
      if (embeds != null) {
         if (embeds.length > 10) {
            throw new IllegalArgumentException("Too many embeds, " + embeds.length + " > 10");
         }

         Embed[] var2 = embeds;
         int var3 = embeds.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            Embed embed = var2[var4];
            Objects.requireNonNull(embed);
         }
      }

      this.embeds = embeds;
      return this;
   }

   @Contract(
      value = "_ -> this",
      mutates = "this"
   )
   @NotNull
   public WebhookMessage addEmbeds(@NotNull Embed... embeds) {
      if (embeds.length == 0) {
         return this;
      } else if (this.embeds() == null) {
         return this.embeds(embeds);
      } else {
         Embed[] newEmbeds = new Embed[this.embeds().length + embeds.length];
         System.arraycopy(this.embeds(), 0, newEmbeds, 0, this.embeds().length);
         System.arraycopy(embeds, this.embeds().length, newEmbeds, this.embeds().length, embeds.length);
         return this.embeds(newEmbeds);
      }
   }

   @NotNull
   public JsonObject toJson() {
      JsonObject json = new JsonObject();
      if (this.content() != null) {
         json.addProperty("content", this.content());
      }

      if (this.username() != null) {
         json.addProperty("username", this.username());
      }

      if (this.avatar() != null) {
         json.addProperty("avatar_url", this.avatar());
      }

      if (this.tts() != null) {
         json.addProperty("tts", this.tts());
      }

      if (this.embeds() != null) {
         json.add("embeds", JsonSerializable.serializeArray(this.embeds()));
      }

      return json;
   }

   @Nullable
   @Generated
   public String content() {
      return this.content;
   }

   @Nullable
   @Generated
   public String username() {
      return this.username;
   }

   @Nullable
   @Generated
   public String avatar() {
      return this.avatar;
   }

   @Nullable
   @Generated
   public Boolean tts() {
      return this.tts;
   }

   @NotNull
   @Generated
   @Nullable
   public Embed[] embeds() {
      return this.embeds;
   }

   @Generated
   public WebhookMessage username(@Nullable String username) {
      this.username = username;
      return this;
   }

   @Generated
   public WebhookMessage avatar(@Nullable String avatar) {
      this.avatar = avatar;
      return this;
   }

   @Generated
   public WebhookMessage tts(@Nullable Boolean tts) {
      this.tts = tts;
      return this;
   }
}
