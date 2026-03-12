package fr.xephi.authme.libs.org.apache.http.cookie.params;

import fr.xephi.authme.libs.org.apache.http.params.HttpAbstractParamBean;
import fr.xephi.authme.libs.org.apache.http.params.HttpParams;
import java.util.Collection;

/** @deprecated */
@Deprecated
public class CookieSpecParamBean extends HttpAbstractParamBean {
   public CookieSpecParamBean(HttpParams params) {
      super(params);
   }

   public void setDatePatterns(Collection<String> patterns) {
      this.params.setParameter("http.protocol.cookie-datepatterns", patterns);
   }

   public void setSingleHeader(boolean singleHeader) {
      this.params.setBooleanParameter("http.protocol.single-cookie-header", singleHeader);
   }
}
