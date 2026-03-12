package fr.xephi.authme.libs.org.apache.commons.mail.resolver;

import fr.xephi.authme.libs.org.apache.commons.mail.DataSourceResolver;
import java.io.IOException;
import javax.activation.DataSource;

public class DataSourceCompositeResolver extends DataSourceBaseResolver {
   private final DataSourceResolver[] dataSourceResolvers;

   public DataSourceCompositeResolver(DataSourceResolver[] dataSourceResolvers) {
      this.dataSourceResolvers = new DataSourceResolver[dataSourceResolvers.length];
      System.arraycopy(dataSourceResolvers, 0, this.dataSourceResolvers, 0, dataSourceResolvers.length);
   }

   public DataSourceCompositeResolver(DataSourceResolver[] dataSourceResolvers, boolean isLenient) {
      super(isLenient);
      this.dataSourceResolvers = new DataSourceResolver[dataSourceResolvers.length];
      System.arraycopy(dataSourceResolvers, 0, this.dataSourceResolvers, 0, dataSourceResolvers.length);
   }

   public DataSourceResolver[] getDataSourceResolvers() {
      DataSourceResolver[] resolvers = new DataSourceResolver[this.dataSourceResolvers.length];
      System.arraycopy(this.dataSourceResolvers, 0, resolvers, 0, this.dataSourceResolvers.length);
      return resolvers;
   }

   public DataSource resolve(String resourceLocation) throws IOException {
      DataSource result = this.resolve(resourceLocation, true);
      if (!this.isLenient() && result == null) {
         throw new IOException("The following resource was not found : " + resourceLocation);
      } else {
         return result;
      }
   }

   public DataSource resolve(String resourceLocation, boolean isLenient) throws IOException {
      for(int i = 0; i < this.getDataSourceResolvers().length; ++i) {
         DataSourceResolver dataSourceResolver = this.getDataSourceResolvers()[i];
         DataSource dataSource = dataSourceResolver.resolve(resourceLocation, isLenient);
         if (dataSource != null) {
            return dataSource;
         }
      }

      if (isLenient) {
         return null;
      } else {
         throw new IOException("The following resource was not found : " + resourceLocation);
      }
   }
}
