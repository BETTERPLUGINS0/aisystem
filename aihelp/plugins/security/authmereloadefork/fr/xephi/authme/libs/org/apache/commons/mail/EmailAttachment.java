package fr.xephi.authme.libs.org.apache.commons.mail;

import java.net.URL;

public class EmailAttachment {
   public static final String ATTACHMENT = "attachment";
   public static final String INLINE = "inline";
   private String name = "";
   private String description = "";
   private String path = "";
   private URL url;
   private String disposition = "attachment";

   public String getDescription() {
      return this.description;
   }

   public String getName() {
      return this.name;
   }

   public String getPath() {
      return this.path;
   }

   public URL getURL() {
      return this.url;
   }

   public String getDisposition() {
      return this.disposition;
   }

   public void setDescription(String desc) {
      this.description = desc;
   }

   public void setName(String aName) {
      this.name = aName;
   }

   public void setPath(String aPath) {
      this.path = aPath;
   }

   public void setURL(URL aUrl) {
      this.url = aUrl;
   }

   public void setDisposition(String aDisposition) {
      this.disposition = aDisposition;
   }
}
