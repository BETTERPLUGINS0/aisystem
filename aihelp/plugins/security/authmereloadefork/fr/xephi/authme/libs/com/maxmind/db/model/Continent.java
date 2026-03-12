package fr.xephi.authme.libs.com.maxmind.db.model;

import fr.xephi.authme.libs.com.google.gson.JsonElement;
import fr.xephi.authme.libs.com.google.gson.JsonObject;

public class Continent extends AbstractRecord {
   private final String code;

   public Continent(String name, Integer geoNameId, String code) {
      super(name, geoNameId);
      this.code = code;
   }

   public static Continent of(JsonElement jsonElement) {
      JsonObject continentJson = jsonElement.getAsJsonObject();
      int geoNameId = continentJson.getAsJsonPrimitive("geoname_id").getAsInt();
      String code = continentJson.getAsJsonPrimitive("code").getAsString();
      String continentName = continentJson.getAsJsonObject("names").get("en").getAsString();
      return new Continent(continentName, geoNameId, code);
   }

   public String getCode() {
      return this.code;
   }

   public String toString() {
      return "Continent{code='" + this.code + '\'' + "} " + super.toString();
   }
}
