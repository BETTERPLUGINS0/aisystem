package fr.xephi.authme.libs.org.apache.commons.mail.resolver;

import fr.xephi.authme.libs.org.apache.commons.mail.DataSourceResolver;

public abstract class DataSourceBaseResolver implements DataSourceResolver {
   private final boolean lenient;

   public DataSourceBaseResolver() {
      this.lenient = false;
   }

   public DataSourceBaseResolver(boolean lenient) {
      this.lenient = lenient;
   }

   public boolean isLenient() {
      return this.lenient;
   }

   protected boolean isCid(String resourceLocation) {
      return resourceLocation.startsWith("cid:");
   }

   protected boolean isFileUrl(String urlString) {
      return urlString.startsWith("file:/");
   }

   protected boolean isHttpUrl(String urlString) {
      return urlString.startsWith("http://") || urlString.startsWith("https://");
   }
}
