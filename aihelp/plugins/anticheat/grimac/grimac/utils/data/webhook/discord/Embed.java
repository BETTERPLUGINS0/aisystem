package ac.grim.grimac.utils.data.webhook.discord;

import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.utils.data.json.JsonSerializable;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.time.Instant;
import java.util.Objects;
import lombok.Generated;

public class Embed implements JsonSerializable {
   public static final int MAX_TITLE_LENGTH = 256;
   public static final int MAX_DESCRIPTION_LENGTH = 4096;
   public static final int MAX_FIELDS = 25;
   @Nullable
   private String title;
   @NotNull
   private String description;
   @Nullable
   private String titleURL;
   @Nullable
   private Instant timestamp;
   @Nullable
   private Integer color;
   @Nullable
   private EmbedFooter footer;
   @Nullable
   private String imageURL;
   @Nullable
   private String thumbnailURL;
   @Nullable
   private EmbedAuthor author;
   @NotNull
   @Nullable
   private EmbedField[] fields;

   public Embed(@NotNull String description) {
      this.description(description);
   }

   public Embed(@NotNull JsonElement jsonElement) {
      JsonObject json = jsonElement.getAsJsonObject();
      this.description(json.get("description").getAsString());
      JsonElement element;
      if ((element = json.get("title")) != null) {
         this.title(element.getAsString());
      }

      if ((element = json.get("url")) != null) {
         this.titleURL(element.getAsString());
      }

      if ((element = json.get("timestamp")) != null) {
         this.timestamp(Instant.parse(element.getAsString()));
      }

      if ((element = json.get("color")) != null) {
         this.color(element.getAsInt());
      }

      if ((element = json.get("footer")) != null) {
         this.footer(new EmbedFooter(element));
      }

      if ((element = json.get("image")) != null) {
         this.imageURL(element.getAsJsonObject().get("url").getAsString());
      }

      if ((element = json.get("thumbnail")) != null) {
         this.imageURL(element.getAsJsonObject().get("url").getAsString());
      }

      if ((element = json.get("author")) != null) {
         this.author(new EmbedAuthor(element));
      }

      if ((element = json.get("fields")) != null) {
         this.fields((EmbedField[])JsonSerializable.deserializeArray(element.getAsJsonArray(), (x$0) -> {
            return new EmbedField[x$0];
         }, EmbedField::new));
      }

   }

   @Contract(
      value = "_ -> this",
      mutates = "this"
   )
   @NotNull
   public Embed description(@NotNull String description) {
      Objects.requireNonNull(description, "Embed description cannot be null!");
      if (description.length() > 4096) {
         throw new IllegalArgumentException("Embed description too long, " + description.length() + " > 4096");
      } else {
         this.description = description;
         return this;
      }
   }

   @Contract(
      value = "_ -> this",
      mutates = "this"
   )
   @NotNull
   public Embed title(@Nullable String title) {
      if (title != null && title.length() > 256) {
         throw new IllegalArgumentException("Embed title too long, " + title.length() + " > 256");
      } else {
         this.title = title;
         return this;
      }
   }

   @Contract(
      value = "_ -> this",
      mutates = "this"
   )
   @NotNull
   public Embed fields(@NotNull @Nullable EmbedField[] fields) {
      if (fields != null) {
         if (fields.length > 25) {
            throw new IllegalArgumentException("Too many fields, " + fields.length + " > 25");
         }

         EmbedField[] var2 = fields;
         int var3 = fields.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            EmbedField field = var2[var4];
            Objects.requireNonNull(field);
         }
      }

      this.fields = fields;
      return this;
   }

   @Contract(
      value = "_ -> this",
      mutates = "this"
   )
   @NotNull
   public Embed addFields(@NotNull EmbedField... fields) {
      if (fields.length == 0) {
         return this;
      } else if (this.fields() == null) {
         return this.fields(fields);
      } else {
         EmbedField[] newFields = new EmbedField[this.fields().length + fields.length];
         System.arraycopy(this.fields(), 0, newFields, 0, this.fields().length);
         System.arraycopy(fields, this.fields().length, newFields, this.fields().length, fields.length);
         return this.fields(newFields);
      }
   }

   @NotNull
   public Embed footer(@Nullable EmbedFooter footer) {
      if (footer != null && (footer.icon() != null || !footer.text().isBlank())) {
         this.footer = footer;
      } else {
         this.footer = null;
      }

      return this;
   }

   @NotNull
   public JsonObject toJson() {
      JsonObject json = new JsonObject();
      json.addProperty("description", this.description());
      if (this.title() != null) {
         json.addProperty("title", this.title());
      }

      if (this.color() != null) {
         json.addProperty("color", this.color() & 16777215);
      }

      if (this.titleURL() != null) {
         json.addProperty("url", this.titleURL());
      }

      if (this.timestamp() != null) {
         json.addProperty("timestamp", this.timestamp().toString());
      }

      if (this.footer() != null) {
         json.add("footer", this.footer().toJson());
      }

      JsonObject thumbnail;
      if (this.imageURL() != null) {
         thumbnail = new JsonObject();
         thumbnail.addProperty("url", this.imageURL());
         json.add("image", thumbnail);
      }

      if (this.thumbnailURL() != null) {
         thumbnail = new JsonObject();
         thumbnail.addProperty("url", this.thumbnailURL());
         json.add("thumbnail", thumbnail);
      }

      if (this.author() != null) {
         json.add("author", this.author().toJson());
      }

      if (this.fields() != null) {
         json.add("fields", JsonSerializable.serializeArray(this.fields()));
      }

      return json;
   }

   @Nullable
   @Generated
   public String title() {
      return this.title;
   }

   @NotNull
   @Generated
   public String description() {
      return this.description;
   }

   @Nullable
   @Generated
   public String titleURL() {
      return this.titleURL;
   }

   @Nullable
   @Generated
   public Instant timestamp() {
      return this.timestamp;
   }

   @Nullable
   @Generated
   public Integer color() {
      return this.color;
   }

   @Nullable
   @Generated
   public EmbedFooter footer() {
      return this.footer;
   }

   @Nullable
   @Generated
   public String imageURL() {
      return this.imageURL;
   }

   @Nullable
   @Generated
   public String thumbnailURL() {
      return this.thumbnailURL;
   }

   @Nullable
   @Generated
   public EmbedAuthor author() {
      return this.author;
   }

   @NotNull
   @Generated
   @Nullable
   public EmbedField[] fields() {
      return this.fields;
   }

   @Generated
   public Embed titleURL(@Nullable String titleURL) {
      this.titleURL = titleURL;
      return this;
   }

   @Generated
   public Embed timestamp(@Nullable Instant timestamp) {
      this.timestamp = timestamp;
      return this;
   }

   @Generated
   public Embed color(@Nullable Integer color) {
      this.color = color;
      return this;
   }

   @Generated
   public Embed imageURL(@Nullable String imageURL) {
      this.imageURL = imageURL;
      return this;
   }

   @Generated
   public Embed thumbnailURL(@Nullable String thumbnailURL) {
      this.thumbnailURL = thumbnailURL;
      return this;
   }

   @Generated
   public Embed author(@Nullable EmbedAuthor author) {
      this.author = author;
      return this;
   }
}
