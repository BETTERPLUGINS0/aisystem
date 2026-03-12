package fr.xephi.authme.libs.org.apache.commons.mail;

public class SimpleEmail extends Email {
   public Email setMsg(String msg) throws EmailException {
      if (EmailUtils.isEmpty(msg)) {
         throw new EmailException("Invalid message supplied");
      } else {
         this.setContent(msg, "text/plain");
         return this;
      }
   }
}
