package fr.xephi.authme.libs.org.apache.commons.mail.resolver;

import java.io.File;
import java.io.IOException;
import javax.activation.DataSource;
import javax.activation.FileDataSource;

public class DataSourceFileResolver extends DataSourceBaseResolver {
   private final File baseDir;

   public DataSourceFileResolver() {
      this.baseDir = new File(".");
   }

   public DataSourceFileResolver(File baseDir) {
      this.baseDir = baseDir;
   }

   public DataSourceFileResolver(File baseDir, boolean lenient) {
      super(lenient);
      this.baseDir = baseDir;
   }

   public File getBaseDir() {
      return this.baseDir;
   }

   public DataSource resolve(String resourceLocation) throws IOException {
      return this.resolve(resourceLocation, this.isLenient());
   }

   public DataSource resolve(String resourceLocation, boolean isLenient) throws IOException {
      DataSource result = null;
      if (!this.isCid(resourceLocation)) {
         File file = new File(resourceLocation);
         if (!file.isAbsolute()) {
            file = this.getBaseDir() != null ? new File(this.getBaseDir(), resourceLocation) : new File(resourceLocation);
         }

         if (file.exists()) {
            result = new FileDataSource(file);
         } else if (!isLenient) {
            throw new IOException("Cant resolve the following file resource :" + file.getAbsolutePath());
         }
      }

      return result;
   }
}
