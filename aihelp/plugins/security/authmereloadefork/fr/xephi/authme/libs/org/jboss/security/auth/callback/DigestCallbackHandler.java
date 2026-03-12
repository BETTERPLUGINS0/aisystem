package fr.xephi.authme.libs.org.jboss.security.auth.callback;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import java.io.IOException;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

public class DigestCallbackHandler implements CallbackHandler {
   private String username;
   private String nonce;
   private String nc;
   private String cnonce;
   private String qop;
   private String realm;
   private String md5a2;

   public DigestCallbackHandler(String username, String nonce, String nc, String cnonce, String qop, String realm, String md5a2) {
      this.username = username;
      this.nonce = nonce;
      this.nc = nc;
      this.cnonce = cnonce;
      this.qop = qop;
      this.realm = realm;
      this.md5a2 = md5a2;
   }

   public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
      boolean foundCallback = false;
      Callback firstUnknown = null;
      int count = callbacks != null ? callbacks.length : 0;

      for(int n = 0; n < count; ++n) {
         Callback c = callbacks[n];
         if (c instanceof MapCallback) {
            MapCallback mc = (MapCallback)c;
            mc.setInfo("username", this.username);
            mc.setInfo("cnonce", this.cnonce);
            mc.setInfo("nonce", this.nonce);
            mc.setInfo("nc", this.nc);
            mc.setInfo("qop", this.qop);
            mc.setInfo("realm", this.realm);
            mc.setInfo("a2hash", this.md5a2);
            foundCallback = true;
         } else if (firstUnknown == null) {
            firstUnknown = c;
         }
      }

      if (!foundCallback) {
         throw PicketBoxMessages.MESSAGES.unableToHandleCallback(firstUnknown, this.getClass().getName(), firstUnknown.getClass().getCanonicalName());
      }
   }
}
