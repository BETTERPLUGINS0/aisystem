package me.casperge.realisticseasons.dump;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.HashMap;

public class DumpProcessor {
   private static final HttpClient httpClient;

   public static DumpResponse processDump(DumpInformation var0) {
      HashMap var1 = var0.info;
      Gson var2 = new Gson();
      Type var3 = (new TypeToken<HashMap>() {
      }).getType();
      String var4 = var2.toJson(var1, var3);
      HttpRequest var5 = HttpRequest.newBuilder().POST(BodyPublishers.ofString(var4)).uri(URI.create("http://paste.realisticseasons.com/submit.php")).header("Content-Type", "application/json").build();
      HttpResponse var6 = null;

      try {
         var6 = httpClient.send(var5, BodyHandlers.ofString());
      } catch (InterruptedException | IOException var8) {
         var8.printStackTrace();
         return new DumpResponse(false, "Error occured sending http request");
      }

      return ((String)var6.body()).equalsIgnoreCase("succes") ? new DumpResponse(true, "success") : new DumpResponse(false, "(" + String.valueOf(var6.statusCode()) + ") " + (String)var6.body());
   }

   static {
      httpClient = HttpClient.newBuilder().version(Version.HTTP_2).build();
   }
}
