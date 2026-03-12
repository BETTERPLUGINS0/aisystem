package fr.xephi.authme.libs.org.apache.commons.mail;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.activation.DataSource;

public class ImageHtmlEmail extends HtmlEmail {
   public static final String REGEX_IMG_SRC = "(<[Ii][Mm][Gg]\\s*[^>]*?\\s+[Ss][Rr][Cc]\\s*=\\s*[\"'])([^\"']+?)([\"'])";
   public static final String REGEX_SCRIPT_SRC = "(<[Ss][Cc][Rr][Ii][Pp][Tt]\\s*.*?\\s+[Ss][Rr][Cc]\\s*=\\s*[\"'])([^\"']+?)([\"'])";
   private static final Pattern IMG_PATTERN = Pattern.compile("(<[Ii][Mm][Gg]\\s*[^>]*?\\s+[Ss][Rr][Cc]\\s*=\\s*[\"'])([^\"']+?)([\"'])");
   private static final Pattern SCRIPT_PATTERN = Pattern.compile("(<[Ss][Cc][Rr][Ii][Pp][Tt]\\s*.*?\\s+[Ss][Rr][Cc]\\s*=\\s*[\"'])([^\"']+?)([\"'])");
   private DataSourceResolver dataSourceResolver;

   public DataSourceResolver getDataSourceResolver() {
      return this.dataSourceResolver;
   }

   public void setDataSourceResolver(DataSourceResolver dataSourceResolver) {
      this.dataSourceResolver = dataSourceResolver;
   }

   public void buildMimeMessage() throws EmailException {
      try {
         String temp = this.replacePattern(super.html, IMG_PATTERN);
         temp = this.replacePattern(temp, SCRIPT_PATTERN);
         this.setHtmlMsg(temp);
         super.buildMimeMessage();
      } catch (IOException var2) {
         throw new EmailException("Building the MimeMessage failed", var2);
      }
   }

   private String replacePattern(String htmlMessage, Pattern pattern) throws EmailException, IOException {
      StringBuffer stringBuffer = new StringBuffer();
      Map<String, String> cidCache = new HashMap();
      Map<String, DataSource> dataSourceCache = new HashMap();
      Matcher matcher = pattern.matcher(htmlMessage);

      while(matcher.find()) {
         String resourceLocation = matcher.group(2);
         DataSource dataSource;
         if (dataSourceCache.get(resourceLocation) == null) {
            dataSource = this.getDataSourceResolver().resolve(resourceLocation);
            if (dataSource != null) {
               dataSourceCache.put(resourceLocation, dataSource);
            }
         } else {
            dataSource = (DataSource)dataSourceCache.get(resourceLocation);
         }

         if (dataSource != null) {
            String name = dataSource.getName();
            if (EmailUtils.isEmpty(name)) {
               name = resourceLocation;
            }

            String cid = (String)cidCache.get(name);
            if (cid == null) {
               cid = this.embed(dataSource, name);
               cidCache.put(name, cid);
            }

            matcher.appendReplacement(stringBuffer, Matcher.quoteReplacement(matcher.group(1) + "cid:" + cid + matcher.group(3)));
         }
      }

      matcher.appendTail(stringBuffer);
      cidCache.clear();
      dataSourceCache.clear();
      return stringBuffer.toString();
   }
}
