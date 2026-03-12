package fr.xephi.authme.libs.com.maxmind.db.model;

import fr.xephi.authme.libs.com.google.gson.JsonElement;
import fr.xephi.authme.libs.com.google.gson.JsonObject;

public class CountryResponse {
   private final Country country;
   private final Continent continent;

   public CountryResponse(Country country, Continent continent) {
      this.country = country;
      this.continent = continent;
   }

   public static CountryResponse of(JsonElement jsonElement) {
      JsonObject response = jsonElement.getAsJsonObject();
      Country country = Country.of(response.get("country"));
      Continent continent = Continent.of(response.get("continent"));
      return new CountryResponse(country, continent);
   }

   public Country getCountry() {
      return this.country;
   }

   public Continent getContinent() {
      return this.continent;
   }
}
