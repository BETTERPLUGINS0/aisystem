package fr.xephi.authme.libs.org.apache.http.cookie;

import fr.xephi.authme.libs.org.apache.http.annotation.Obsolete;
import java.util.Date;

public interface Cookie {
   String getName();

   String getValue();

   @Obsolete
   String getComment();

   @Obsolete
   String getCommentURL();

   Date getExpiryDate();

   boolean isPersistent();

   String getDomain();

   String getPath();

   @Obsolete
   int[] getPorts();

   boolean isSecure();

   @Obsolete
   int getVersion();

   boolean isExpired(Date var1);
}
