package javax.mail.internet;

import com.sun.mail.util.LineInputStream;
import com.sun.mail.util.PropUtil;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import javax.mail.Header;
import javax.mail.MessagingException;

public class InternetHeaders {
   private static final boolean ignoreWhitespaceLines = PropUtil.getBooleanSystemProperty("mail.mime.ignorewhitespacelines", false);
   protected List<InternetHeaders.InternetHeader> headers;

   public InternetHeaders() {
      this.headers = new ArrayList(40);
      this.headers.add(new InternetHeaders.InternetHeader("Return-Path", (String)null));
      this.headers.add(new InternetHeaders.InternetHeader("Received", (String)null));
      this.headers.add(new InternetHeaders.InternetHeader("Resent-Date", (String)null));
      this.headers.add(new InternetHeaders.InternetHeader("Resent-From", (String)null));
      this.headers.add(new InternetHeaders.InternetHeader("Resent-Sender", (String)null));
      this.headers.add(new InternetHeaders.InternetHeader("Resent-To", (String)null));
      this.headers.add(new InternetHeaders.InternetHeader("Resent-Cc", (String)null));
      this.headers.add(new InternetHeaders.InternetHeader("Resent-Bcc", (String)null));
      this.headers.add(new InternetHeaders.InternetHeader("Resent-Message-Id", (String)null));
      this.headers.add(new InternetHeaders.InternetHeader("Date", (String)null));
      this.headers.add(new InternetHeaders.InternetHeader("From", (String)null));
      this.headers.add(new InternetHeaders.InternetHeader("Sender", (String)null));
      this.headers.add(new InternetHeaders.InternetHeader("Reply-To", (String)null));
      this.headers.add(new InternetHeaders.InternetHeader("To", (String)null));
      this.headers.add(new InternetHeaders.InternetHeader("Cc", (String)null));
      this.headers.add(new InternetHeaders.InternetHeader("Bcc", (String)null));
      this.headers.add(new InternetHeaders.InternetHeader("Message-Id", (String)null));
      this.headers.add(new InternetHeaders.InternetHeader("In-Reply-To", (String)null));
      this.headers.add(new InternetHeaders.InternetHeader("References", (String)null));
      this.headers.add(new InternetHeaders.InternetHeader("Subject", (String)null));
      this.headers.add(new InternetHeaders.InternetHeader("Comments", (String)null));
      this.headers.add(new InternetHeaders.InternetHeader("Keywords", (String)null));
      this.headers.add(new InternetHeaders.InternetHeader("Errors-To", (String)null));
      this.headers.add(new InternetHeaders.InternetHeader("MIME-Version", (String)null));
      this.headers.add(new InternetHeaders.InternetHeader("Content-Type", (String)null));
      this.headers.add(new InternetHeaders.InternetHeader("Content-Transfer-Encoding", (String)null));
      this.headers.add(new InternetHeaders.InternetHeader("Content-MD5", (String)null));
      this.headers.add(new InternetHeaders.InternetHeader(":", (String)null));
      this.headers.add(new InternetHeaders.InternetHeader("Content-Length", (String)null));
      this.headers.add(new InternetHeaders.InternetHeader("Status", (String)null));
   }

   public InternetHeaders(InputStream is) throws MessagingException {
      this(is, false);
   }

   public InternetHeaders(InputStream is, boolean allowutf8) throws MessagingException {
      this.headers = new ArrayList(40);
      this.load(is, allowutf8);
   }

   public void load(InputStream is) throws MessagingException {
      this.load(is, false);
   }

   public void load(InputStream is, boolean allowutf8) throws MessagingException {
      LineInputStream lis = new LineInputStream(is, allowutf8);
      String prevline = null;
      StringBuilder lineBuffer = new StringBuilder();

      try {
         boolean first = true;

         String line;
         do {
            line = lis.readLine();
            if (line != null && (line.startsWith(" ") || line.startsWith("\t"))) {
               if (prevline != null) {
                  lineBuffer.append(prevline);
                  prevline = null;
               }

               if (first) {
                  String lt = line.trim();
                  if (lt.length() > 0) {
                     lineBuffer.append(lt);
                  }
               } else {
                  if (lineBuffer.length() > 0) {
                     lineBuffer.append("\r\n");
                  }

                  lineBuffer.append(line);
               }
            } else {
               if (prevline != null) {
                  this.addHeaderLine(prevline);
               } else if (lineBuffer.length() > 0) {
                  this.addHeaderLine(lineBuffer.toString());
                  lineBuffer.setLength(0);
               }

               prevline = line;
            }

            first = false;
         } while(line != null && !isEmpty(line));

      } catch (IOException var9) {
         throw new MessagingException("Error in input stream", var9);
      }
   }

   private static final boolean isEmpty(String line) {
      return line.length() == 0 || ignoreWhitespaceLines && line.trim().length() == 0;
   }

   public String[] getHeader(String name) {
      Iterator<InternetHeaders.InternetHeader> e = this.headers.iterator();
      ArrayList v = new ArrayList();

      while(e.hasNext()) {
         InternetHeaders.InternetHeader h = (InternetHeaders.InternetHeader)e.next();
         if (name.equalsIgnoreCase(h.getName()) && h.line != null) {
            v.add(h.getValue());
         }
      }

      if (v.size() == 0) {
         return null;
      } else {
         String[] r = new String[v.size()];
         r = (String[])v.toArray(r);
         return r;
      }
   }

   public String getHeader(String name, String delimiter) {
      String[] s = this.getHeader(name);
      if (s == null) {
         return null;
      } else if (s.length != 1 && delimiter != null) {
         StringBuilder r = new StringBuilder(s[0]);

         for(int i = 1; i < s.length; ++i) {
            r.append(delimiter);
            r.append(s[i]);
         }

         return r.toString();
      } else {
         return s[0];
      }
   }

   public void setHeader(String name, String value) {
      boolean found = false;

      for(int i = 0; i < this.headers.size(); ++i) {
         InternetHeaders.InternetHeader h = (InternetHeaders.InternetHeader)this.headers.get(i);
         if (name.equalsIgnoreCase(h.getName())) {
            if (found) {
               this.headers.remove(i);
               --i;
            } else {
               int j;
               if (h.line != null && (j = h.line.indexOf(58)) >= 0) {
                  h.line = h.line.substring(0, j + 1) + " " + value;
               } else {
                  h.line = name + ": " + value;
               }

               found = true;
            }
         }
      }

      if (!found) {
         this.addHeader(name, value);
      }

   }

   public void addHeader(String name, String value) {
      int pos = this.headers.size();
      boolean addReverse = name.equalsIgnoreCase("Received") || name.equalsIgnoreCase("Return-Path");
      if (addReverse) {
         pos = 0;
      }

      for(int i = this.headers.size() - 1; i >= 0; --i) {
         InternetHeaders.InternetHeader h = (InternetHeaders.InternetHeader)this.headers.get(i);
         if (name.equalsIgnoreCase(h.getName())) {
            if (!addReverse) {
               this.headers.add(i + 1, new InternetHeaders.InternetHeader(name, value));
               return;
            }

            pos = i;
         }

         if (!addReverse && h.getName().equals(":")) {
            pos = i;
         }
      }

      this.headers.add(pos, new InternetHeaders.InternetHeader(name, value));
   }

   public void removeHeader(String name) {
      for(int i = 0; i < this.headers.size(); ++i) {
         InternetHeaders.InternetHeader h = (InternetHeaders.InternetHeader)this.headers.get(i);
         if (name.equalsIgnoreCase(h.getName())) {
            h.line = null;
         }
      }

   }

   public Enumeration<Header> getAllHeaders() {
      return new InternetHeaders.MatchHeaderEnum(this.headers, (String[])null, false);
   }

   public Enumeration<Header> getMatchingHeaders(String[] names) {
      return new InternetHeaders.MatchHeaderEnum(this.headers, names, true);
   }

   public Enumeration<Header> getNonMatchingHeaders(String[] names) {
      return new InternetHeaders.MatchHeaderEnum(this.headers, names, false);
   }

   public void addHeaderLine(String line) {
      try {
         char c = line.charAt(0);
         if (c != ' ' && c != '\t') {
            this.headers.add(new InternetHeaders.InternetHeader(line));
         } else {
            InternetHeaders.InternetHeader h = (InternetHeaders.InternetHeader)this.headers.get(this.headers.size() - 1);
            h.line = h.line + "\r\n" + line;
         }
      } catch (StringIndexOutOfBoundsException var4) {
         return;
      } catch (NoSuchElementException var5) {
      }

   }

   public Enumeration<String> getAllHeaderLines() {
      return this.getNonMatchingHeaderLines((String[])null);
   }

   public Enumeration<String> getMatchingHeaderLines(String[] names) {
      return new InternetHeaders.MatchStringEnum(this.headers, names, true);
   }

   public Enumeration<String> getNonMatchingHeaderLines(String[] names) {
      return new InternetHeaders.MatchStringEnum(this.headers, names, false);
   }

   static class MatchHeaderEnum extends InternetHeaders.MatchEnum implements Enumeration<Header> {
      MatchHeaderEnum(List<InternetHeaders.InternetHeader> v, String[] n, boolean m) {
         super(v, n, m, false);
      }

      public Header nextElement() {
         return (Header)super.nextElement();
      }
   }

   static class MatchStringEnum extends InternetHeaders.MatchEnum implements Enumeration<String> {
      MatchStringEnum(List<InternetHeaders.InternetHeader> v, String[] n, boolean m) {
         super(v, n, m, true);
      }

      public String nextElement() {
         return (String)super.nextElement();
      }
   }

   static class MatchEnum {
      private Iterator<InternetHeaders.InternetHeader> e;
      private String[] names;
      private boolean match;
      private boolean want_line;
      private InternetHeaders.InternetHeader next_header;

      MatchEnum(List<InternetHeaders.InternetHeader> v, String[] n, boolean m, boolean l) {
         this.e = v.iterator();
         this.names = n;
         this.match = m;
         this.want_line = l;
         this.next_header = null;
      }

      public boolean hasMoreElements() {
         if (this.next_header == null) {
            this.next_header = this.nextMatch();
         }

         return this.next_header != null;
      }

      public Object nextElement() {
         if (this.next_header == null) {
            this.next_header = this.nextMatch();
         }

         if (this.next_header == null) {
            throw new NoSuchElementException("No more headers");
         } else {
            InternetHeaders.InternetHeader h = this.next_header;
            this.next_header = null;
            return this.want_line ? h.line : new Header(h.getName(), h.getValue());
         }
      }

      private InternetHeaders.InternetHeader nextMatch() {
         label40:
         while(true) {
            if (this.e.hasNext()) {
               InternetHeaders.InternetHeader h = (InternetHeaders.InternetHeader)this.e.next();
               if (h.line == null) {
                  continue;
               }

               if (this.names == null) {
                  return this.match ? null : h;
               }

               for(int i = 0; i < this.names.length; ++i) {
                  if (this.names[i].equalsIgnoreCase(h.getName())) {
                     if (!this.match) {
                        continue label40;
                     }

                     return h;
                  }
               }

               if (this.match) {
                  continue;
               }

               return h;
            }

            return null;
         }
      }
   }

   protected static final class InternetHeader extends Header {
      String line;

      public InternetHeader(String l) {
         super("", "");
         int i = l.indexOf(58);
         if (i < 0) {
            this.name = l.trim();
         } else {
            this.name = l.substring(0, i).trim();
         }

         this.line = l;
      }

      public InternetHeader(String n, String v) {
         super(n, "");
         if (v != null) {
            this.line = n + ": " + v;
         } else {
            this.line = null;
         }

      }

      public String getValue() {
         int i = this.line.indexOf(58);
         if (i < 0) {
            return this.line;
         } else {
            int j;
            for(j = i + 1; j < this.line.length(); ++j) {
               char c = this.line.charAt(j);
               if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                  break;
               }
            }

            return this.line.substring(j);
         }
      }
   }
}
