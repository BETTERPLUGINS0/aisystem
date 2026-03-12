package fr.xephi.authme.libs.com.maxmind.db.model;

import fr.xephi.authme.libs.com.google.gson.JsonElement;
import fr.xephi.authme.libs.com.google.gson.JsonObject;

public class Country extends AbstractRecord {
   private final String isoCode;
   private final Integer geoNameId;

   public Country(String isoCode, Integer geoNameId, String name) {
      super(name, geoNameId);
      this.isoCode = isoCode;
      this.geoNameId = geoNameId;
   }

   public static Country of(JsonElement jsonElement) {
      JsonObject countryJson = jsonElement.getAsJsonObject();
      int countryGeoName = countryJson.getAsJsonPrimitive("geoname_id").getAsInt();
      String isoCode = countryJson.getAsJsonPrimitive("iso_code").getAsString();
      String countryName = countryJson.getAsJsonObject("names").get("en").getAsString();
      return new Country(isoCode, countryGeoName, countryName);
   }

   public String getIsoCode() {
      return this.isoCode;
   }

   public String toString() {
      return "Country{isoCode='" + this.isoCode + '\'' + ", geoNameId=" + this.geoNameId + "} " + super.toString();
   }
}
