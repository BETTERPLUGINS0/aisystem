package com.lenis0012.bukkit.loginsecurity.modules.language;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class Translation {
   private final Map<String, String> translations;
   private Translation fallback;
   private String name;

   public Translation(Translation fallback, Reader reader, String name) throws IOException {
      this(fallback, (new JsonParser()).parse(reader).getAsJsonObject(), name);
   }

   public Translation(Translation fallback, JsonObject data, String name) throws IOException {
      this.translations = Maps.newConcurrentMap();
      this.fallback = fallback;
      Iterator var4 = data.entrySet().iterator();

      while(var4.hasNext()) {
         Entry<String, JsonElement> entry = (Entry)var4.next();
         this.translations.put((String)entry.getKey(), ((JsonElement)entry.getValue()).getAsString());
      }

      this.name = name.split("\\.")[0];
   }

   public String getName() {
      return this.name;
   }

   public String getLocalizedName() {
      return (String)this.translations.get("localizedName");
   }

   public TranslatedMessage translate(String key) {
      if (this.translations.containsKey(key)) {
         return new TranslatedMessage((String)this.translations.get(key));
      } else if (this.fallback != null) {
         return this.fallback.translate(key);
      } else {
         throw new IllegalArgumentException("Unknown translation key \"" + key + "\" for language " + this.name);
      }
   }
}
