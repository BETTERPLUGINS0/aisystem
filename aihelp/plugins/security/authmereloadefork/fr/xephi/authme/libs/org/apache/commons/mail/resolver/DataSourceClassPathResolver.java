package fr.xephi.authme.libs.org.apache.commons.mail.resolver;

import java.io.IOException;
import java.io.InputStream;
import javax.activation.DataSource;
import javax.activation.FileTypeMap;
import javax.mail.util.ByteArrayDataSource;

public class DataSourceClassPathResolver extends DataSourceBaseResolver {
   private final String classPathBase;

   public DataSourceClassPathResolver() {
      this.classPathBase = "/";
   }

   public DataSourceClassPathResolver(String classPathBase) {
      this.classPathBase = classPathBase.endsWith("/") ? classPathBase : classPathBase + "/";
   }

   public DataSourceClassPathResolver(String classPathBase, boolean lenient) {
      super(lenient);
      this.classPathBase = classPathBase.endsWith("/") ? classPathBase : classPathBase + "/";
   }

   public String getClassPathBase() {
      return this.classPathBase;
   }

   public DataSource resolve(String resourceLocation) throws IOException {
      return this.resolve(resourceLocation, this.isLenient());
   }

   public DataSource resolve(String resourceLocation, boolean isLenient) throws IOException {
      ByteArrayDataSource result = null;

      try {
         if (!this.isCid(resourceLocation) && !this.isHttpUrl(resourceLocation)) {
            String mimeType = FileTypeMap.getDefaultFileTypeMap().getContentType(resourceLocation);
            String resourceName = this.getResourceName(resourceLocation);
            InputStream is = DataSourceClassPathResolver.class.getResourceAsStream(resourceName);
            if (is == null) {
               if (isLenient) {
                  return null;
               }

               throw new IOException("The following class path resource was not found : " + resourceLocation);
            }

            try {
               ByteArrayDataSource ds = new ByteArrayDataSource(is, mimeType);
               ds.setName(DataSourceClassPathResolver.class.getResource(resourceName).toString());
               result = ds;
            } finally {
               is.close();
            }
         }

         return result;
      } catch (IOException var12) {
         if (isLenient) {
            return null;
         } else {
            throw var12;
         }
      }
   }

   private String getResourceName(String resourceLocation) {
      return (this.getClassPathBase() + resourceLocation).replaceAll("//", "/");
   }
}
