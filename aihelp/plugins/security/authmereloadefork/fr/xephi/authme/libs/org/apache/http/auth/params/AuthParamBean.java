package fr.xephi.authme.libs.org.apache.http.auth.params;

import fr.xephi.authme.libs.org.apache.http.params.HttpAbstractParamBean;
import fr.xephi.authme.libs.org.apache.http.params.HttpParams;

/** @deprecated */
@Deprecated
public class AuthParamBean extends HttpAbstractParamBean {
   public AuthParamBean(HttpParams params) {
      super(params);
   }

   public void setCredentialCharset(String charset) {
      AuthParams.setCredentialCharset(this.params, charset);
   }
}
