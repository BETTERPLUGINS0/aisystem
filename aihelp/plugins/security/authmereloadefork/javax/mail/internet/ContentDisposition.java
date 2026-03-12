package javax.mail.internet;

import com.sun.mail.util.PropUtil;

public class ContentDisposition {
   private static final boolean contentDispositionStrict = PropUtil.getBooleanSystemProperty("mail.mime.contentdisposition.strict", true);
   private String disposition;
   private ParameterList list;

   public ContentDisposition() {
   }

   public ContentDisposition(String disposition, ParameterList list) {
      this.disposition = disposition;
      this.list = list;
   }

   public ContentDisposition(String s) throws ParseException {
      HeaderTokenizer h = new HeaderTokenizer(s, "()<>@,;:\\\"\t []/?=");
      HeaderTokenizer.Token tk = h.next();
      if (tk.getType() != -1) {
         if (contentDispositionStrict) {
            throw new ParseException("Expected disposition, got " + tk.getValue());
         }
      } else {
         this.disposition = tk.getValue();
      }

      String rem = h.getRemainder();
      if (rem != null) {
         try {
            this.list = new ParameterList(rem);
         } catch (ParseException var6) {
            if (contentDispositionStrict) {
               throw var6;
            }
         }
      }

   }

   public String getDisposition() {
      return this.disposition;
   }

   public String getParameter(String name) {
      return this.list == null ? null : this.list.get(name);
   }

   public ParameterList getParameterList() {
      return this.list;
   }

   public void setDisposition(String disposition) {
      this.disposition = disposition;
   }

   public void setParameter(String name, String value) {
      if (this.list == null) {
         this.list = new ParameterList();
      }

      this.list.set(name, value);
   }

   public void setParameterList(ParameterList list) {
      this.list = list;
   }

   public String toString() {
      if (this.disposition == null) {
         return "";
      } else if (this.list == null) {
         return this.disposition;
      } else {
         StringBuilder sb = new StringBuilder(this.disposition);
         sb.append(this.list.toString(sb.length() + 21));
         return sb.toString();
      }
   }
}
