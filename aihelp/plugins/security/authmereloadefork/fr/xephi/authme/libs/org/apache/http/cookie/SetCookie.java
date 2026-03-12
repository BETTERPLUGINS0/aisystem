package fr.xephi.authme.libs.org.apache.http.cookie;

import fr.xephi.authme.libs.org.apache.http.annotation.Obsolete;
import java.util.Date;

public interface SetCookie extends Cookie {
   void setValue(String var1);

   @Obsolete
   void setComment(String var1);

   void setExpiryDate(Date var1);

   void setDomain(String var1);

   void setPath(String var1);

   void setSecure(boolean var1);

   @Obsolete
   void setVersion(int var1);
}
