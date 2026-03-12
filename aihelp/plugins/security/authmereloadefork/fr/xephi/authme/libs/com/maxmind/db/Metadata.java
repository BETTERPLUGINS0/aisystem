package fr.xephi.authme.libs.com.maxmind.db;

import fr.xephi.authme.libs.com.google.gson.Gson;
import fr.xephi.authme.libs.com.google.gson.JsonElement;
import fr.xephi.authme.libs.com.google.gson.JsonObject;
import fr.xephi.authme.libs.com.google.gson.reflect.TypeToken;
import java.util.Date;
import java.util.List;
import java.util.Map;

public final class Metadata {
   private final int binaryFormatMajorVersion;
   private final int binaryFormatMinorVersion;
   private final long buildEpoch;
   private final String databaseType;
   private final JsonElement description;
   private final int ipVersion;
   private final JsonElement languages;
   private final int nodeByteSize;
   private final int nodeCount;
   private final int recordSize;
   private final int searchTreeSize;

   Metadata(JsonObject metadata) {
      this.binaryFormatMajorVersion = metadata.get("binary_format_major_version").getAsInt();
      this.binaryFormatMinorVersion = metadata.get("binary_format_minor_version").getAsInt();
      this.buildEpoch = metadata.get("build_epoch").getAsLong();
      this.databaseType = metadata.get("database_type").getAsString();
      this.languages = metadata.get("languages");
      this.description = metadata.get("description");
      this.ipVersion = metadata.get("ip_version").getAsInt();
      this.nodeCount = metadata.get("node_count").getAsInt();
      this.recordSize = metadata.get("record_size").getAsInt();
      this.nodeByteSize = this.recordSize / 4;
      this.searchTreeSize = this.nodeCount * this.nodeByteSize;
   }

   public int getBinaryFormatMajorVersion() {
      return this.binaryFormatMajorVersion;
   }

   public int getBinaryFormatMinorVersion() {
      return this.binaryFormatMinorVersion;
   }

   public Date getBuildDate() {
      return new Date(this.buildEpoch * 1000L);
   }

   public String getDatabaseType() {
      return this.databaseType;
   }

   public Map<String, String> getDescription() {
      return (Map)(new Gson()).fromJson(this.description, (new TypeToken<Map<String, String>>() {
      }).getType());
   }

   public int getIpVersion() {
      return this.ipVersion;
   }

   public List<String> getLanguages() {
      return (List)(new Gson()).fromJson(this.languages, (new TypeToken<List<String>>() {
      }).getType());
   }

   int getNodeByteSize() {
      return this.nodeByteSize;
   }

   int getNodeCount() {
      return this.nodeCount;
   }

   int getRecordSize() {
      return this.recordSize;
   }

   int getSearchTreeSize() {
      return this.searchTreeSize;
   }

   public String toString() {
      return "Metadata [binaryFormatMajorVersion=" + this.binaryFormatMajorVersion + ", binaryFormatMinorVersion=" + this.binaryFormatMinorVersion + ", buildEpoch=" + this.buildEpoch + ", databaseType=" + this.databaseType + ", description=" + this.description + ", ipVersion=" + this.ipVersion + ", nodeCount=" + this.nodeCount + ", recordSize=" + this.recordSize + "]";
   }
}
