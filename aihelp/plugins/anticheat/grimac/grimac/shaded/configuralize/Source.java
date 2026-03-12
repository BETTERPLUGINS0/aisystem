package ac.grim.grimac.shaded.configuralize;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

public class Source {
   private final DynamicConfig config;
   private final Class<?> clazz;
   private final String resource;
   private final File file;

   public Source(DynamicConfig config, Class<?> clazz, String resource, File file) {
      this.config = config;
      this.clazz = clazz;
      this.resource = resource;
      this.file = file.getAbsoluteFile();
   }

   public String getResourcePath() {
      return this.getResourcePath(this.config.getLanguage());
   }

   public String getResourcePath(Language language) {
      return "/" + this.resource + "/" + language.getCode().toLowerCase() + "." + this.file.getName().substring(this.file.getName().lastIndexOf(".") + 1);
   }

   public URL getResource() {
      return this.getResource(this.config.getLanguage());
   }

   public URL getResource(Language language) {
      return this.clazz.getResource(this.getResourcePath(language));
   }

   public String getResourceName() {
      return this.resource;
   }

   public boolean isLanguageAvailable() {
      return this.isLanguageAvailable(this.config.getLanguage());
   }

   public boolean isLanguageAvailable(Language language) {
      try {
         InputStream stream = this.getResource(language).openStream();
         stream.close();
         return true;
      } catch (Exception var3) {
         return false;
      }
   }

   public File getFile() {
      return this.file;
   }

   public Class<?> getClazz() {
      return this.clazz;
   }
}
