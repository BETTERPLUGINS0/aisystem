package ac.grim.grimac.utils.data.webhook.discord;

import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.utils.data.json.JsonSerializable;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Objects;
import lombok.Generated;

public class EmbedField implements JsonSerializable {
   public static final int MAX_NAME_LENGTH = 256;
   public static final int MAX_VALUE_LENGTH = 1024;
   @NotNull
   private String name;
   @NotNull
   private String value;
   private boolean inline;

   public EmbedField(@NotNull String name, @NotNull String value) {
      this(name, value, false);
   }

   public EmbedField(@NotNull String name, @NotNull String value, boolean inline) {
      this.name(name);
      this.value(value);
      this.inline(inline);
   }

   public EmbedField(@NotNull JsonElement jsonElement) {
      JsonObject json = jsonElement.getAsJsonObject();
      this.name(json.get("name").getAsString());
      this.value(json.get("value").getAsString());
      JsonElement inline = json.get("inline");
      if (inline != null) {
         this.inline(inline.getAsBoolean());
      }

   }

   @Contract(
      value = "_ -> this",
      mutates = "this"
   )
   @NotNull
   public EmbedField name(@NotNull String name) {
      Objects.requireNonNull(name, "Embed field name cannot be null!");
      if (name.length() > 256) {
         throw new IllegalArgumentException("Embed field name too long, " + name.length() + " > 256");
      } else {
         this.name = name;
         return this;
      }
   }

   @Contract(
      value = "_ -> this",
      mutates = "this"
   )
   @NotNull
   public EmbedField value(@NotNull String value) {
      Objects.requireNonNull(value, "Embed field value cannot be null!");
      if (value.length() > 1024) {
         throw new IllegalArgumentException("Embed field value too long, " + value.length() + " > 1024");
      } else {
         this.value = value;
         return this;
      }
   }

   @NotNull
   public JsonElement toJson() {
      JsonObject json = new JsonObject();
      json.addProperty("name", this.name());
      json.addProperty("value", this.value());
      json.addProperty("inline", this.inline());
      return json;
   }

   @NotNull
   @Generated
   public String name() {
      return this.name;
   }

   @NotNull
   @Generated
   public String value() {
      return this.value;
   }

   @Generated
   public boolean inline() {
      return this.inline;
   }

   @Generated
   public EmbedField inline(boolean inline) {
      this.inline = inline;
      return this;
   }
}
