package fr.xephi.authme.libs.org.apache.http.impl.cookie;

import fr.xephi.authme.libs.org.apache.http.cookie.CommonCookieAttributeHandler;

class RFC6265CookieSpecBase extends RFC6265CookieSpec {
   RFC6265CookieSpecBase(CommonCookieAttributeHandler... handlers) {
      super(handlers);
   }
}
