package ac.grim.grimac.utils.data.webhook.discord;

import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.utils.data.json.JsonSerializable;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Objects;
import lombok.Generated;

public class EmbedFooter implements JsonSerializable {
   public static final int MAX_TEXT_LENGTH = 2048;
   @NotNull
   private String text;
   @Nullable
   private String icon;

   public EmbedFooter(@NotNull String text) {
      this(text, (String)null);
   }

   public EmbedFooter(@NotNull String text, @Nullable String icon) {
      this.text(text);
      this.icon(icon);
   }

   public EmbedFooter(@NotNull JsonElement jsonElement) {
      JsonObject json = jsonElement.getAsJsonObject();
      this.text(json.get("text").getAsString());
      JsonElement icon_url = json.get("icon_url");
      if (icon_url != null) {
         this.icon(icon_url.getAsString());
      }

   }

   @Contract(
      value = "_ -> this",
      mutates = "this"
   )
   @NotNull
   public EmbedFooter text(@NotNull String text) {
      Objects.requireNonNull(text, "Embed footer text cannot be null!");
      if (text.length() > 2048) {
         throw new IllegalArgumentException("Embed footer text too long, " + text.length() + " > 2048");
      } else {
         this.text = text;
         return this;
      }
   }

   @NotNull
   public JsonElement toJson() {
      JsonObject json = new JsonObject();
      json.addProperty("text", this.text());
      if (this.icon() != null) {
         json.addProperty("icon_url", this.icon());
      }

      return json;
   }

   @NotNull
   @Generated
   public String text() {
      return this.text;
   }

   @Nullable
   @Generated
   public String icon() {
      return this.icon;
   }

   @Generated
   public EmbedFooter icon(@Nullable String icon) {
      this.icon = icon;
      return this;
   }
}
