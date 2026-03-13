package github.nighter.smartspawner.logging.discord;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;

public class DiscordEmbed {
   private String title;
   private String description;
   private int color = 5793266;
   private DiscordEmbed.Footer footer;
   private DiscordEmbed.Thumbnail thumbnail;
   private String timestamp;
   private final List<DiscordEmbed.Field> fields = new ArrayList();

   public void setFooter(String text, String iconUrl) {
      this.footer = new DiscordEmbed.Footer(text, iconUrl);
   }

   public void setThumbnail(String url) {
      this.thumbnail = new DiscordEmbed.Thumbnail(url);
   }

   public void setTimestamp(Instant instant) {
      this.timestamp = DateTimeFormatter.ISO_INSTANT.format(instant);
   }

   public void addField(String name, String value, boolean inline) {
      this.fields.add(new DiscordEmbed.Field(name, value, inline));
   }

   public String toJson() {
      StringBuilder json = new StringBuilder("{\"embeds\":[{");
      if (this.title != null && !this.title.isEmpty()) {
         json.append("\"title\":\"").append(this.escapeJson(this.title)).append("\",");
      }

      if (this.description != null && !this.description.isEmpty()) {
         json.append("\"description\":\"").append(this.escapeJson(this.description)).append("\",");
      }

      json.append("\"color\":").append(this.color).append(",");
      if (this.footer != null) {
         json.append("\"footer\":{\"text\":\"").append(this.escapeJson(this.footer.text)).append("\"");
         if (this.footer.iconUrl != null) {
            json.append(",\"icon_url\":\"").append(this.escapeJson(this.footer.iconUrl)).append("\"");
         }

         json.append("},");
      }

      if (this.thumbnail != null) {
         json.append("\"thumbnail\":{\"url\":\"").append(this.escapeJson(this.thumbnail.url)).append("\"},");
      }

      if (this.timestamp != null) {
         json.append("\"timestamp\":\"").append(this.timestamp).append("\",");
      }

      if (!this.fields.isEmpty()) {
         json.append("\"fields\":[");

         for(int i = 0; i < this.fields.size(); ++i) {
            if (i > 0) {
               json.append(",");
            }

            DiscordEmbed.Field field = (DiscordEmbed.Field)this.fields.get(i);
            json.append("{\"name\":\"").append(this.escapeJson(field.name)).append("\",");
            json.append("\"value\":\"").append(this.escapeJson(field.value)).append("\",");
            json.append("\"inline\":").append(field.inline).append("}");
         }

         json.append("],");
      }

      if (json.charAt(json.length() - 1) == ',') {
         json.setLength(json.length() - 1);
      }

      json.append("}]}");
      return json.toString();
   }

   private String escapeJson(String str) {
      return str == null ? "" : str.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t");
   }

   @Generated
   public void setTitle(String title) {
      this.title = title;
   }

   @Generated
   public void setDescription(String description) {
      this.description = description;
   }

   @Generated
   public void setColor(int color) {
      this.color = color;
   }

   private static class Footer {
      String text;
      String iconUrl;

      Footer(String text, String iconUrl) {
         this.text = text;
         this.iconUrl = iconUrl;
      }
   }

   private static class Thumbnail {
      String url;

      Thumbnail(String url) {
         this.url = url;
      }
   }

   private static class Field {
      String name;
      String value;
      boolean inline;

      Field(String name, String value, boolean inline) {
         this.name = name;
         this.value = value;
         this.inline = inline;
      }
   }
}
