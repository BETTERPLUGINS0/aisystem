package fr.xephi.authme.service;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.initialization.DataFolder;
import fr.xephi.authme.libs.com.google.common.annotations.VisibleForTesting;
import fr.xephi.authme.libs.com.maxmind.db.GeoIp2Provider;
import fr.xephi.authme.libs.com.maxmind.db.Reader;
import fr.xephi.authme.libs.com.maxmind.db.cache.CHMCache;
import fr.xephi.authme.libs.com.maxmind.db.model.AbstractRecord;
import fr.xephi.authme.libs.com.maxmind.db.model.Country;
import fr.xephi.authme.libs.com.maxmind.db.model.CountryResponse;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.output.ConsoleLoggerFactory;
import fr.xephi.authme.util.InternetProtocolUtils;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Optional;

public class GeoIpService {
   private static final String DATABASE_NAME = "GeoLite2-Country";
   private static final String DATABASE_FILE = "GeoLite2-Country.mmdb";
   private final ConsoleLogger logger = ConsoleLoggerFactory.get(GeoIpService.class);
   private final Path dataFile;
   private GeoIp2Provider databaseReader;
   private volatile boolean downloading;

   @Inject
   GeoIpService(@DataFolder File dataFolder) {
      this.dataFile = dataFolder.toPath().resolve("GeoLite2-Country.mmdb");
      this.isDataAvailable();
   }

   @VisibleForTesting
   GeoIpService(@DataFolder File dataFolder, GeoIp2Provider reader) {
      this.dataFile = dataFolder.toPath().resolve("GeoLite2-Country.mmdb");
      this.databaseReader = reader;
   }

   private synchronized boolean isDataAvailable() {
      if (this.downloading) {
         return false;
      } else if (this.databaseReader != null) {
         return true;
      } else if (Files.exists(this.dataFile, new LinkOption[0])) {
         try {
            this.startReading();
            return true;
         } catch (IOException var2) {
            this.logger.logException("Failed to load GeoLiteAPI database", var2);
            return false;
         }
      } else {
         return false;
      }
   }

   private void startReading() throws IOException {
      this.databaseReader = new Reader(this.dataFile.toFile(), Reader.FileMode.MEMORY, new CHMCache());
      this.downloading = false;
   }

   public String getCountryCode(String ip) {
      return InternetProtocolUtils.isLocalAddress(ip) ? "LOCALHOST" : (String)this.getCountry(ip).map(Country::getIsoCode).orElse("--");
   }

   public String getCountryName(String ip) {
      return InternetProtocolUtils.isLocalAddress(ip) ? "LocalHost" : (String)this.getCountry(ip).map(AbstractRecord::getName).orElse("N/A");
   }

   private Optional<Country> getCountry(String ip) {
      if (ip != null && !ip.isEmpty() && this.isDataAvailable()) {
         try {
            InetAddress address = InetAddress.getByName(ip);
            return Optional.ofNullable(this.databaseReader.getCountry(address)).map(CountryResponse::getCountry);
         } catch (UnknownHostException var3) {
         } catch (IOException var4) {
            this.logger.logException("Cannot lookup country for " + ip + " at GEO IP database", var4);
         }

         return Optional.empty();
      } else {
         return Optional.empty();
      }
   }
}
