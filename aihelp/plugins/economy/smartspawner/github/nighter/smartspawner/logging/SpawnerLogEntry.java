package github.nighter.smartspawner.logging;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import lombok.Generated;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.Nullable;

public class SpawnerLogEntry {
   private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());
   private final long timestamp;
   private final SpawnerEventType eventType;
   private final String playerName;
   private final UUID playerUuid;
   private final Location location;
   private final EntityType entityType;
   private final Map<String, Object> metadata;

   private SpawnerLogEntry(SpawnerLogEntry.Builder builder) {
      this.timestamp = builder.timestamp;
      this.eventType = builder.eventType;
      this.playerName = builder.playerName;
      this.playerUuid = builder.playerUuid;
      this.location = builder.location;
      this.entityType = builder.entityType;
      this.metadata = new HashMap(builder.metadata);
   }

   @Nullable
   public String getPlayerName() {
      return this.playerName;
   }

   @Nullable
   public UUID getPlayerUuid() {
      return this.playerUuid;
   }

   @Nullable
   public Location getLocation() {
      return this.location;
   }

   @Nullable
   public EntityType getEntityType() {
      return this.entityType;
   }

   public Map<String, Object> getMetadata() {
      return new HashMap(this.metadata);
   }

   public String toJson() {
      StringBuilder json = new StringBuilder("{");
      json.append("\"timestamp\":\"").append(FORMATTER.format(Instant.ofEpochMilli(this.timestamp))).append("\",");
      json.append("\"timestamp_ms\":").append(this.timestamp).append(",");
      json.append("\"event_type\":\"").append(this.eventType.name()).append("\",");
      json.append("\"description\":\"").append(this.eventType.getDescription()).append("\"");
      if (this.playerName != null) {
         json.append(",\"player\":\"").append(this.escapeJson(this.playerName)).append("\"");
      }

      if (this.playerUuid != null) {
         json.append(",\"player_uuid\":\"").append(this.playerUuid).append("\"");
      }

      if (this.location != null) {
         json.append(",\"location\":{");
         json.append("\"world\":\"").append(this.escapeJson(this.location.getWorld().getName())).append("\",");
         json.append("\"x\":").append(this.location.getBlockX()).append(",");
         json.append("\"y\":").append(this.location.getBlockY()).append(",");
         json.append("\"z\":").append(this.location.getBlockZ());
         json.append("}");
      }

      if (this.entityType != null) {
         json.append(",\"entity_type\":\"").append(this.entityType.name()).append("\"");
      }

      if (!this.metadata.isEmpty()) {
         json.append(",\"metadata\":{");
         boolean first = true;

         for(Iterator var3 = this.metadata.entrySet().iterator(); var3.hasNext(); first = false) {
            Entry<String, Object> entry = (Entry)var3.next();
            if (!first) {
               json.append(",");
            }

            json.append("\"").append(this.escapeJson((String)entry.getKey())).append("\":");
            Object value = entry.getValue();
            if (value instanceof String) {
               json.append("\"").append(this.escapeJson(value.toString())).append("\"");
            } else if (!(value instanceof Number) && !(value instanceof Boolean)) {
               json.append("\"").append(this.escapeJson(String.valueOf(value))).append("\"");
            } else {
               json.append(value);
            }
         }

         json.append("}");
      }

      json.append("}");
      return json.toString();
   }

   public String toReadableString() {
      StringBuilder sb = new StringBuilder();
      sb.append("[").append(FORMATTER.format(Instant.ofEpochMilli(this.timestamp))).append("] ");
      sb.append(this.eventType.getDescription());
      if (this.playerName != null) {
         sb.append(" | Player: ").append(this.playerName);
      }

      if (this.location != null) {
         sb.append(" | Location: ").append(this.location.getWorld().getName()).append(" (").append(this.location.getBlockX()).append(", ").append(this.location.getBlockY()).append(", ").append(this.location.getBlockZ()).append(")");
      }

      if (this.entityType != null) {
         sb.append(" | Entity: ").append(this.entityType.name());
      }

      if (!this.metadata.isEmpty()) {
         sb.append(" | ");
         this.metadata.forEach((key, value) -> {
            sb.append(key).append("=").append(value).append(" ");
         });
      }

      return sb.toString().trim();
   }

   private String escapeJson(String str) {
      return str == null ? "" : str.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t");
   }

   @Generated
   public SpawnerEventType getEventType() {
      return this.eventType;
   }

   public static class Builder {
      private long timestamp = System.currentTimeMillis();
      private SpawnerEventType eventType;
      private String playerName;
      private UUID playerUuid;
      private Location location;
      private EntityType entityType;
      private final Map<String, Object> metadata = new HashMap();

      public Builder(SpawnerEventType eventType) {
         this.eventType = eventType;
      }

      public SpawnerLogEntry.Builder timestamp(long timestamp) {
         this.timestamp = timestamp;
         return this;
      }

      public SpawnerLogEntry.Builder player(String name, UUID uuid) {
         this.playerName = name;
         this.playerUuid = uuid;
         return this;
      }

      public SpawnerLogEntry.Builder location(Location location) {
         this.location = location;
         return this;
      }

      public SpawnerLogEntry.Builder entityType(EntityType entityType) {
         this.entityType = entityType;
         return this;
      }

      public SpawnerLogEntry.Builder metadata(String key, Object value) {
         this.metadata.put(key, value);
         return this;
      }

      public SpawnerLogEntry.Builder metadata(Map<String, Object> metadata) {
         this.metadata.putAll(metadata);
         return this;
      }

      public SpawnerLogEntry build() {
         return new SpawnerLogEntry(this);
      }
   }
}
