package fr.xephi.authme.libs.com.maxmind.db;

import fr.xephi.authme.libs.com.google.gson.JsonElement;
import fr.xephi.authme.libs.com.maxmind.db.model.CountryResponse;
import java.io.IOException;
import java.net.InetAddress;

public interface GeoIp2Provider {
   JsonElement get(InetAddress var1) throws IOException;

   CountryResponse getCountry(InetAddress var1) throws IOException;

   Metadata getMetadata();
}
