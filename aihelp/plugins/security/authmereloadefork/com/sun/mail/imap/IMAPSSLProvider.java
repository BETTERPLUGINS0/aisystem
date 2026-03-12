package com.sun.mail.imap;

import javax.mail.Provider;

public class IMAPSSLProvider extends Provider {
   public IMAPSSLProvider() {
      super(Provider.Type.STORE, "imaps", IMAPSSLStore.class.getName(), "Oracle", (String)null);
   }
}
