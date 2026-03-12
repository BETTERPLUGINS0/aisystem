package javax.mail.internet;

import java.util.Enumeration;
import javax.mail.MessagingException;
import javax.mail.Part;

public interface MimePart extends Part {
   String getHeader(String var1, String var2) throws MessagingException;

   void addHeaderLine(String var1) throws MessagingException;

   Enumeration<String> getAllHeaderLines() throws MessagingException;

   Enumeration<String> getMatchingHeaderLines(String[] var1) throws MessagingException;

   Enumeration<String> getNonMatchingHeaderLines(String[] var1) throws MessagingException;

   String getEncoding() throws MessagingException;

   String getContentID() throws MessagingException;

   String getContentMD5() throws MessagingException;

   void setContentMD5(String var1) throws MessagingException;

   String[] getContentLanguage() throws MessagingException;

   void setContentLanguage(String[] var1) throws MessagingException;

   void setText(String var1) throws MessagingException;

   void setText(String var1, String var2) throws MessagingException;

   void setText(String var1, String var2, String var3) throws MessagingException;
}
