package ac.grim.grimac.utils.data.webhook.discord;

import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.utils.data.json.JsonSerializable;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Objects;
import lombok.Generated;

public class EmbedAuthor implements JsonSerializable {
   public static final int MAX_NAME_LENGTH = 256;
   @NotNull
   private String name;
   @Nullable
   private String url;
   @Nullable
   private String icon;

   public EmbedAuthor(@NotNull String name) {
      this(name, (String)null, (String)null);
   }

   public EmbedAuthor(@NotNull String name, @Nullable String url, @Nullable String icon) {
      this.name(name);
      this.url(url);
      this.icon(icon);
   }

   public EmbedAuthor(@NotNull JsonElement jsonElement) {
      JsonObject json = jsonElement.getAsJsonObject();
      this.name(json.get("name").getAsString());
      JsonElement element;
      if ((element = json.get("url")) != null) {
         this.url(element.getAsString());
      }

      if ((element = json.get("icon_url")) != null) {
         this.icon(element.getAsString());
      }

   }

   @Contract(
      value = "_ -> this",
      mutates = "this"
   )
   @NotNull
   public EmbedAuthor name(@NotNull String name) {
      Objects.requireNonNull(name, "Embed author name cannot be null!");
      if (name.length() > 256) {
         throw new IllegalArgumentException("Embed author name too long, " + name.length() + " > 256");
      } else {
         this.name = name;
         return this;
      }
   }

   @NotNull
   public JsonElement toJson() {
      JsonObject json = new JsonObject();
      json.addProperty("name", this.name());
      if (this.url() != null) {
         json.addProperty("url", this.url());
      }

      if (this.icon() != null) {
         json.addProperty("icon_url", this.icon());
      }

      return json;
   }

   @NotNull
   @Generated
   public String name() {
      return this.name;
   }

   @Nullable
   @Generated
   public String url() {
      return this.url;
   }

   @Nullable
   @Generated
   public String icon() {
      return this.icon;
   }

   @Generated
   public EmbedAuthor url(@Nullable String url) {
      this.url = url;
      return this;
   }

   @Generated
   public EmbedAuthor icon(@Nullable String icon) {
      this.icon = icon;
      return this;
   }
}
