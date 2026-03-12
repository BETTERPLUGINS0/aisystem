package fr.xephi.authme.libs.com.maxmind.db;

import fr.xephi.authme.libs.com.google.gson.JsonElement;
import java.net.InetAddress;

public final class Record {
   private final JsonElement data;
   private final Network network;

   public Record(JsonElement data, InetAddress ipAddress, int prefixLength) {
      this.data = data;
      this.network = new Network(ipAddress, prefixLength);
   }

   public JsonElement getData() {
      return this.data;
   }

   public Network getNetwork() {
      return this.network;
   }
}
