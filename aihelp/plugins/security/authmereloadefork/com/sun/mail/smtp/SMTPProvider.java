package com.sun.mail.smtp;

import javax.mail.Provider;

public class SMTPProvider extends Provider {
   public SMTPProvider() {
      super(Provider.Type.TRANSPORT, "smtp", SMTPTransport.class.getName(), "Oracle", (String)null);
   }
}
