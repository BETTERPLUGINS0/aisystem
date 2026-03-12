package fr.xephi.authme.libs.org.apache.commons.mail.resolver;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.activation.DataSource;
import javax.activation.URLDataSource;

public class DataSourceUrlResolver extends DataSourceBaseResolver {
   private final URL baseUrl;

   public DataSourceUrlResolver(URL baseUrl) {
      this.baseUrl = baseUrl;
   }

   public DataSourceUrlResolver(URL baseUrl, boolean lenient) {
      super(lenient);
      this.baseUrl = baseUrl;
   }

   public URL getBaseUrl() {
      return this.baseUrl;
   }

   public DataSource resolve(String resourceLocation) throws IOException {
      return this.resolve(resourceLocation, this.isLenient());
   }

   public DataSource resolve(String resourceLocation, boolean isLenient) throws IOException {
      URLDataSource result = null;

      try {
         if (!this.isCid(resourceLocation)) {
            URL url = this.createUrl(resourceLocation);
            result = new URLDataSource(url);
            result.getInputStream();
         }

         return result;
      } catch (IOException var5) {
         if (isLenient) {
            return null;
         } else {
            throw var5;
         }
      }
   }

   protected URL createUrl(String resourceLocation) throws MalformedURLException {
      if (this.baseUrl == null) {
         return new URL(resourceLocation);
      } else if (resourceLocation != null && resourceLocation.length() != 0) {
         return !this.isFileUrl(resourceLocation) && !this.isHttpUrl(resourceLocation) ? new URL(this.getBaseUrl(), resourceLocation.replaceAll("&amp;", "&")) : new URL(resourceLocation);
      } else {
         throw new IllegalArgumentException("No resource defined");
      }
   }
}
