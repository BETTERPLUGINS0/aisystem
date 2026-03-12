package fr.xephi.authme.libs.org.apache.commons.mail;

import java.io.IOException;
import javax.activation.DataSource;

public interface DataSourceResolver {
   DataSource resolve(String var1) throws IOException;

   DataSource resolve(String var1, boolean var2) throws IOException;
}
