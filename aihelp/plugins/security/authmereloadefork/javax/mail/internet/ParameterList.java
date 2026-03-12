package javax.mail.internet;

import com.sun.mail.util.ASCIIUtility;
import com.sun.mail.util.PropUtil;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class ParameterList {
   private Map<String, Object> list;
   private Set<String> multisegmentNames;
   private Map<String, Object> slist;
   private String lastName;
   private static final boolean encodeParameters = PropUtil.getBooleanSystemProperty("mail.mime.encodeparameters", true);
   private static final boolean decodeParameters = PropUtil.getBooleanSystemProperty("mail.mime.decodeparameters", true);
   private static final boolean decodeParametersStrict = PropUtil.getBooleanSystemProperty("mail.mime.decodeparameters.strict", false);
   private static final boolean applehack = PropUtil.getBooleanSystemProperty("mail.mime.applefilenames", false);
   private static final boolean windowshack = PropUtil.getBooleanSystemProperty("mail.mime.windowsfilenames", false);
   private static final boolean parametersStrict = PropUtil.getBooleanSystemProperty("mail.mime.parameters.strict", true);
   private static final boolean splitLongParameters = PropUtil.getBooleanSystemProperty("mail.mime.splitlongparameters", true);
   private static final char[] hex = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

   public ParameterList() {
      this.list = new LinkedHashMap();
      this.lastName = null;
      if (decodeParameters) {
         this.multisegmentNames = new HashSet();
         this.slist = new HashMap();
      }

   }

   public ParameterList(String s) throws ParseException {
      this();
      HeaderTokenizer h = new HeaderTokenizer(s, "()<>@,;:\\\"\t []/?=");

      while(true) {
         HeaderTokenizer.Token tk = h.next();
         int type = tk.getType();
         if (type != -4) {
            String value;
            if ((char)type != ';') {
               if (type != -1 || this.lastName == null || (!applehack || !this.lastName.equals("name") && !this.lastName.equals("filename")) && parametersStrict) {
                  throw new ParseException("In parameter list <" + s + ">, expected ';', got \"" + tk.getValue() + "\"");
               }

               String lastValue = (String)this.list.get(this.lastName);
               value = lastValue + " " + tk.getValue();
               this.list.put(this.lastName, value);
               continue;
            }

            tk = h.next();
            if (tk.getType() != -4) {
               if (tk.getType() != -1) {
                  throw new ParseException("In parameter list <" + s + ">, expected parameter name, got \"" + tk.getValue() + "\"");
               }

               String name = tk.getValue().toLowerCase(Locale.ENGLISH);
               tk = h.next();
               if ((char)tk.getType() != '=') {
                  throw new ParseException("In parameter list <" + s + ">, expected '=', got \"" + tk.getValue() + "\"");
               }

               if (windowshack && (name.equals("name") || name.equals("filename"))) {
                  tk = h.next(';', true);
               } else if (parametersStrict) {
                  tk = h.next();
               } else {
                  tk = h.next(';');
               }

               type = tk.getType();
               if (type != -1 && type != -2) {
                  throw new ParseException("In parameter list <" + s + ">, expected parameter value, got \"" + tk.getValue() + "\"");
               }

               value = tk.getValue();
               this.lastName = name;
               if (decodeParameters) {
                  this.putEncodedName(name, value);
               } else {
                  this.list.put(name, value);
               }
               continue;
            }
         }

         if (decodeParameters) {
            this.combineMultisegmentNames(false);
         }

         return;
      }
   }

   public void combineSegments() {
      if (decodeParameters && this.multisegmentNames.size() > 0) {
         try {
            this.combineMultisegmentNames(true);
         } catch (ParseException var2) {
         }
      }

   }

   private void putEncodedName(String name, String value) throws ParseException {
      int star = name.indexOf(42);
      if (star < 0) {
         this.list.put(name, value);
      } else if (star == name.length() - 1) {
         name = name.substring(0, star);
         ParameterList.Value v = extractCharset(value);

         try {
            v.value = decodeBytes(v.value, v.charset);
         } catch (UnsupportedEncodingException var6) {
            if (decodeParametersStrict) {
               throw new ParseException(var6.toString());
            }
         }

         this.list.put(name, v);
      } else {
         String rname = name.substring(0, star);
         this.multisegmentNames.add(rname);
         this.list.put(rname, "");
         Object v;
         if (name.endsWith("*")) {
            if (name.endsWith("*0*")) {
               v = extractCharset(value);
            } else {
               v = new ParameterList.Value();
               ((ParameterList.Value)v).encodedValue = value;
               ((ParameterList.Value)v).value = value;
            }

            name = name.substring(0, name.length() - 1);
         } else {
            v = value;
         }

         this.slist.put(name, v);
      }

   }

   private void combineMultisegmentNames(boolean keepConsistentOnFailure) throws ParseException {
      boolean success = false;
      boolean var23 = false;

      Iterator sit;
      try {
         var23 = true;
         sit = this.multisegmentNames.iterator();

         while(true) {
            if (!sit.hasNext()) {
               success = true;
               var23 = false;
               break;
            }

            String name = (String)sit.next();
            ParameterList.MultiValue mv = new ParameterList.MultiValue();
            String charset = null;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int segment = 0;

            while(true) {
               String sname = name + "*" + segment;
               Object v = this.slist.get(sname);
               if (v == null) {
                  break;
               }

               mv.add(v);

               try {
                  if (v instanceof ParameterList.Value) {
                     ParameterList.Value vv = (ParameterList.Value)v;
                     if (segment == 0) {
                        charset = vv.charset;
                     } else if (charset == null) {
                        this.multisegmentNames.remove(name);
                        break;
                     }

                     decodeBytes(vv.value, (OutputStream)bos);
                  } else {
                     bos.write(ASCIIUtility.getBytes((String)v));
                  }
               } catch (IOException var28) {
               }

               this.slist.remove(sname);
               ++segment;
            }

            if (segment == 0) {
               this.list.remove(name);
            } else {
               try {
                  if (charset != null) {
                     charset = MimeUtility.javaCharset(charset);
                  }

                  if (charset == null || charset.length() == 0) {
                     charset = MimeUtility.getDefaultJavaCharset();
                  }

                  if (charset != null) {
                     mv.value = bos.toString(charset);
                  } else {
                     mv.value = bos.toString();
                  }
               } catch (UnsupportedEncodingException var27) {
                  if (decodeParametersStrict) {
                     throw new ParseException(var27.toString());
                  }

                  try {
                     mv.value = bos.toString("iso-8859-1");
                  } catch (UnsupportedEncodingException var24) {
                  }
               }

               this.list.put(name, mv);
            }
         }
      } finally {
         if (var23) {
            if (keepConsistentOnFailure || success) {
               if (this.slist.size() > 0) {
                  Iterator sit = this.slist.values().iterator();

                  label252:
                  while(true) {
                     Object v;
                     do {
                        if (!sit.hasNext()) {
                           this.list.putAll(this.slist);
                           break label252;
                        }

                        v = sit.next();
                     } while(!(v instanceof ParameterList.Value));

                     ParameterList.Value vv = (ParameterList.Value)v;

                     try {
                        vv.value = decodeBytes(vv.value, vv.charset);
                     } catch (UnsupportedEncodingException var25) {
                        if (decodeParametersStrict) {
                           throw new ParseException(var25.toString());
                        }
                     }
                  }
               }

               this.multisegmentNames.clear();
               this.slist.clear();
            }

         }
      }

      if (keepConsistentOnFailure || success) {
         if (this.slist.size() > 0) {
            sit = this.slist.values().iterator();

            label274:
            while(true) {
               Object v;
               do {
                  if (!sit.hasNext()) {
                     this.list.putAll(this.slist);
                     break label274;
                  }

                  v = sit.next();
               } while(!(v instanceof ParameterList.Value));

               ParameterList.Value vv = (ParameterList.Value)v;

               try {
                  vv.value = decodeBytes(vv.value, vv.charset);
               } catch (UnsupportedEncodingException var26) {
                  if (decodeParametersStrict) {
                     throw new ParseException(var26.toString());
                  }
               }
            }
         }

         this.multisegmentNames.clear();
         this.slist.clear();
      }

   }

   public int size() {
      return this.list.size();
   }

   public String get(String name) {
      Object v = this.list.get(name.trim().toLowerCase(Locale.ENGLISH));
      String value;
      if (v instanceof ParameterList.MultiValue) {
         value = ((ParameterList.MultiValue)v).value;
      } else if (v instanceof ParameterList.LiteralValue) {
         value = ((ParameterList.LiteralValue)v).value;
      } else if (v instanceof ParameterList.Value) {
         value = ((ParameterList.Value)v).value;
      } else {
         value = (String)v;
      }

      return value;
   }

   public void set(String name, String value) {
      name = name.trim().toLowerCase(Locale.ENGLISH);
      if (decodeParameters) {
         try {
            this.putEncodedName(name, value);
         } catch (ParseException var4) {
            this.list.put(name, value);
         }
      } else {
         this.list.put(name, value);
      }

   }

   public void set(String name, String value, String charset) {
      if (encodeParameters) {
         ParameterList.Value ev = encodeValue(value, charset);
         if (ev != null) {
            this.list.put(name.trim().toLowerCase(Locale.ENGLISH), ev);
         } else {
            this.set(name, value);
         }
      } else {
         this.set(name, value);
      }

   }

   void setLiteral(String name, String value) {
      ParameterList.LiteralValue lv = new ParameterList.LiteralValue();
      lv.value = value;
      this.list.put(name, lv);
   }

   public void remove(String name) {
      this.list.remove(name.trim().toLowerCase(Locale.ENGLISH));
   }

   public Enumeration<String> getNames() {
      return new ParameterList.ParamEnum(this.list.keySet().iterator());
   }

   public String toString() {
      return this.toString(0);
   }

   public String toString(int used) {
      ParameterList.ToStringBuffer sb = new ParameterList.ToStringBuffer(used);
      Iterator e = this.list.entrySet().iterator();

      while(true) {
         while(e.hasNext()) {
            Entry<String, Object> ent = (Entry)e.next();
            String name = (String)ent.getKey();
            Object v = ent.getValue();
            String value;
            if (v instanceof ParameterList.MultiValue) {
               ParameterList.MultiValue vv = (ParameterList.MultiValue)v;
               name = name + "*";

               for(int i = 0; i < vv.size(); ++i) {
                  Object va = vv.get(i);
                  String ns;
                  if (va instanceof ParameterList.Value) {
                     ns = name + i + "*";
                     value = ((ParameterList.Value)va).encodedValue;
                  } else {
                     ns = name + i;
                     value = (String)va;
                  }

                  sb.addNV(ns, quote(value));
               }
            } else if (v instanceof ParameterList.LiteralValue) {
               value = ((ParameterList.LiteralValue)v).value;
               sb.addNV(name, quote(value));
            } else if (v instanceof ParameterList.Value) {
               name = name + "*";
               value = ((ParameterList.Value)v).encodedValue;
               sb.addNV(name, quote(value));
            } else {
               value = (String)v;
               if (value.length() > 60 && splitLongParameters && encodeParameters) {
                  int seg = 0;

                  for(name = name + "*"; value.length() > 60; ++seg) {
                     sb.addNV(name + seg, quote(value.substring(0, 60)));
                     value = value.substring(60);
                  }

                  if (value.length() > 0) {
                     sb.addNV(name + seg, quote(value));
                  }
               } else {
                  sb.addNV(name, quote(value));
               }
            }
         }

         return sb.toString();
      }
   }

   private static String quote(String value) {
      return MimeUtility.quote(value, "()<>@,;:\\\"\t []/?=");
   }

   private static ParameterList.Value encodeValue(String value, String charset) {
      if (MimeUtility.checkAscii(value) == 1) {
         return null;
      } else {
         byte[] b;
         try {
            b = value.getBytes(MimeUtility.javaCharset(charset));
         } catch (UnsupportedEncodingException var6) {
            return null;
         }

         StringBuffer sb = new StringBuffer(b.length + charset.length() + 2);
         sb.append(charset).append("''");

         for(int i = 0; i < b.length; ++i) {
            char c = (char)(b[i] & 255);
            if (c > ' ' && c < 127 && c != '*' && c != '\'' && c != '%' && "()<>@,;:\\\"\t []/?=".indexOf(c) < 0) {
               sb.append(c);
            } else {
               sb.append('%').append(hex[c >> 4]).append(hex[c & 15]);
            }
         }

         ParameterList.Value v = new ParameterList.Value();
         v.charset = charset;
         v.value = value;
         v.encodedValue = sb.toString();
         return v;
      }
   }

   private static ParameterList.Value extractCharset(String value) throws ParseException {
      ParameterList.Value v = new ParameterList.Value();
      v.value = v.encodedValue = value;

      try {
         int i = value.indexOf(39);
         if (i < 0) {
            if (decodeParametersStrict) {
               throw new ParseException("Missing charset in encoded value: " + value);
            }

            return v;
         }

         String charset = value.substring(0, i);
         int li = value.indexOf(39, i + 1);
         if (li < 0) {
            if (decodeParametersStrict) {
               throw new ParseException("Missing language in encoded value: " + value);
            }

            return v;
         }

         v.value = value.substring(li + 1);
         v.charset = charset;
      } catch (NumberFormatException var5) {
         if (decodeParametersStrict) {
            throw new ParseException(var5.toString());
         }
      } catch (StringIndexOutOfBoundsException var6) {
         if (decodeParametersStrict) {
            throw new ParseException(var6.toString());
         }
      }

      return v;
   }

   private static String decodeBytes(String value, String charset) throws ParseException, UnsupportedEncodingException {
      byte[] b = new byte[value.length()];
      int i = 0;

      int bi;
      for(bi = 0; i < value.length(); ++i) {
         char c = value.charAt(i);
         if (c == '%') {
            try {
               String hex = value.substring(i + 1, i + 3);
               c = (char)Integer.parseInt(hex, 16);
               i += 2;
            } catch (NumberFormatException var7) {
               if (decodeParametersStrict) {
                  throw new ParseException(var7.toString());
               }
            } catch (StringIndexOutOfBoundsException var8) {
               if (decodeParametersStrict) {
                  throw new ParseException(var8.toString());
               }
            }
         }

         b[bi++] = (byte)c;
      }

      if (charset != null) {
         charset = MimeUtility.javaCharset(charset);
      }

      if (charset == null || charset.length() == 0) {
         charset = MimeUtility.getDefaultJavaCharset();
      }

      return new String(b, 0, bi, charset);
   }

   private static void decodeBytes(String value, OutputStream os) throws ParseException, IOException {
      for(int i = 0; i < value.length(); ++i) {
         char c = value.charAt(i);
         if (c == '%') {
            try {
               String hex = value.substring(i + 1, i + 3);
               c = (char)Integer.parseInt(hex, 16);
               i += 2;
            } catch (NumberFormatException var5) {
               if (decodeParametersStrict) {
                  throw new ParseException(var5.toString());
               }
            } catch (StringIndexOutOfBoundsException var6) {
               if (decodeParametersStrict) {
                  throw new ParseException(var6.toString());
               }
            }
         }

         os.write((byte)c);
      }

   }

   private static class ToStringBuffer {
      private int used;
      private StringBuilder sb = new StringBuilder();

      public ToStringBuffer(int used) {
         this.used = used;
      }

      public void addNV(String name, String value) {
         this.sb.append("; ");
         this.used += 2;
         int len = name.length() + value.length() + 1;
         if (this.used + len > 76) {
            this.sb.append("\r\n\t");
            this.used = 8;
         }

         this.sb.append(name).append('=');
         this.used += name.length() + 1;
         if (this.used + value.length() > 76) {
            String s = MimeUtility.fold(this.used, value);
            this.sb.append(s);
            int lastlf = s.lastIndexOf(10);
            if (lastlf >= 0) {
               this.used += s.length() - lastlf - 1;
            } else {
               this.used += s.length();
            }
         } else {
            this.sb.append(value);
            this.used += value.length();
         }

      }

      public String toString() {
         return this.sb.toString();
      }
   }

   private static class ParamEnum implements Enumeration<String> {
      private Iterator<String> it;

      ParamEnum(Iterator<String> it) {
         this.it = it;
      }

      public boolean hasMoreElements() {
         return this.it.hasNext();
      }

      public String nextElement() {
         return (String)this.it.next();
      }
   }

   private static class MultiValue extends ArrayList<Object> {
      private static final long serialVersionUID = 699561094618751023L;
      String value;

      private MultiValue() {
      }

      // $FF: synthetic method
      MultiValue(Object x0) {
         this();
      }
   }

   private static class LiteralValue {
      String value;

      private LiteralValue() {
      }

      // $FF: synthetic method
      LiteralValue(Object x0) {
         this();
      }
   }

   private static class Value {
      String value;
      String charset;
      String encodedValue;

      private Value() {
      }

      // $FF: synthetic method
      Value(Object x0) {
         this();
      }
   }
}
