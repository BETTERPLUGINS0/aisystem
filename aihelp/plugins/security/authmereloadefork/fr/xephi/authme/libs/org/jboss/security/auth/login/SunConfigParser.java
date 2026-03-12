package fr.xephi.authme.libs.org.jboss.security.auth.login;

import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.AppConfigurationEntry.LoginModuleControlFlag;

public class SunConfigParser implements SunConfigParserConstants {
   private XMLLoginConfigImpl loginConfig;
   public SunConfigParserTokenManager token_source;
   SimpleCharStream jj_input_stream;
   public Token token;
   public Token jj_nt;
   private int jj_ntk;
   private int jj_gen;
   private final int[] jj_la1;
   private static int[] jj_la1_0;
   private List<int[]> jj_expentries;
   private int[] jj_expentry;
   private int jj_kind;
   private int trace_indent;
   private boolean trace_enabled;

   public SunConfigParser() {
      this((Reader)(new StringReader("")));
   }

   public void parse(Reader configFile, XMLLoginConfigImpl loginConfig) throws ParseException {
      this.parse(configFile, loginConfig, false);
   }

   public void parse(Reader configFile, XMLLoginConfigImpl loginConfig, boolean trace) throws ParseException {
      this.ReInit(configFile);
      if (trace) {
         this.enable_tracing();
      } else {
         this.disable_tracing();
      }

      this.loginConfig = loginConfig;
      this.config();
   }

   private String stripQuotes(String image) {
      return image.substring(1, image.length() - 1);
   }

   public static void doParse(Reader configFile, XMLLoginConfigImpl loginConfig) throws ParseException {
      doParse(configFile, loginConfig, false);
   }

   public static void doParse(Reader configFile, XMLLoginConfigImpl loginConfig, boolean trace) throws ParseException {
      SunConfigParser parser = new SunConfigParser();
      parser.parse(configFile, loginConfig, trace);
   }

   public final void config() throws ParseException {
      this.trace_call("config");

      try {
         while(true) {
            switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
            case 16:
               this.appConfig();
               break;
            default:
               this.jj_la1[0] = this.jj_gen;
               this.jj_consume_token(0);
               return;
            }
         }
      } finally {
         this.trace_return("config");
      }
   }

   public final void appConfig() throws ParseException {
      this.trace_call("appConfig");

      try {
         Token t = null;
         ArrayList<AppConfigurationEntry> entries = new ArrayList();
         t = this.jj_consume_token(16);
         String appName = t.image;
         this.jj_consume_token(7);

         while(true) {
            AppConfigurationEntry entry = this.loginModuleConfig();
            entries.add(entry);
            switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
            case 17:
               break;
            default:
               this.jj_la1[1] = this.jj_gen;
               this.jj_consume_token(8);
               this.jj_consume_token(9);
               AppConfigurationEntry[] appConfig = new AppConfigurationEntry[entries.size()];
               entries.toArray(appConfig);
               this.loginConfig.addAppConfig(appName, appConfig);
               return;
            }
         }
      } finally {
         this.trace_return("appConfig");
      }
   }

   public final AppConfigurationEntry loginModuleConfig() throws ParseException {
      this.trace_call("loginModuleConfig");

      try {
         Token t = null;
         HashMap<String, String> optionsMap = new HashMap();
         t = this.jj_consume_token(17);
         String loginModuleClassName = t.image;
         LoginModuleControlFlag controlFlag = this.controlFlag();

         while(true) {
            switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
            case 16:
            case 17:
               this.moduleOptions(optionsMap);
               break;
            default:
               this.jj_la1[2] = this.jj_gen;
               this.jj_consume_token(9);
               AppConfigurationEntry var5 = new AppConfigurationEntry(loginModuleClassName, controlFlag, optionsMap);
               AppConfigurationEntry var6 = var5;
               return var6;
            }
         }
      } finally {
         this.trace_return("loginModuleConfig");
      }
   }

   public final LoginModuleControlFlag controlFlag() throws ParseException {
      this.trace_call("controlFlag");

      LoginModuleControlFlag var3;
      try {
         LoginModuleControlFlag flag = null;
         Token t = this.jj_consume_token(15);
         if (LoginModuleControlFlag.REQUIRED.toString().indexOf(t.image) > 0) {
            flag = LoginModuleControlFlag.REQUIRED;
         } else if (LoginModuleControlFlag.REQUISITE.toString().indexOf(t.image) > 0) {
            flag = LoginModuleControlFlag.REQUISITE;
         } else if (LoginModuleControlFlag.SUFFICIENT.toString().indexOf(t.image) > 0) {
            flag = LoginModuleControlFlag.SUFFICIENT;
         } else if (LoginModuleControlFlag.OPTIONAL.toString().indexOf(t.image) > 0) {
            flag = LoginModuleControlFlag.OPTIONAL;
         }

         var3 = flag;
      } finally {
         this.trace_return("controlFlag");
      }

      return var3;
   }

   public final void moduleOptions(HashMap<String, String> optionsMap) throws ParseException {
      this.trace_call("moduleOptions");

      try {
         Token t;
         switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 16:
            t = this.jj_consume_token(16);
            break;
         case 17:
            t = this.jj_consume_token(17);
            break;
         default:
            this.jj_la1[3] = this.jj_gen;
            this.jj_consume_token(-1);
            throw new ParseException();
         }

         String name = t.image;
         this.jj_consume_token(10);
         String value;
         switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 11:
         case 12:
         case 16:
         case 17:
         case 20:
            switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
            case 11:
               t = this.jj_consume_token(11);
               break;
            case 12:
               t = this.jj_consume_token(12);
               break;
            case 13:
            case 14:
            case 15:
            case 18:
            case 19:
            default:
               this.jj_la1[4] = this.jj_gen;
               this.jj_consume_token(-1);
               throw new ParseException();
            case 16:
               t = this.jj_consume_token(16);
               break;
            case 17:
               t = this.jj_consume_token(17);
               break;
            case 20:
               t = this.jj_consume_token(20);
            }

            value = t.image;
            optionsMap.put(name, value);
            break;
         case 13:
         case 15:
         case 18:
         case 19:
         default:
            this.jj_la1[5] = this.jj_gen;
            this.jj_consume_token(-1);
            throw new ParseException();
         case 14:
            t = this.jj_consume_token(14);
            value = this.stripQuotes(t.image);
            optionsMap.put(name, value);
         }
      } finally {
         this.trace_return("moduleOptions");
      }

   }

   private static void jj_la1_init_0() {
      jj_la1_0 = new int[]{65536, 131072, 196608, 196608, 1251328, 1267712};
   }

   public SunConfigParser(InputStream stream) {
      this(stream, (String)null);
   }

   public SunConfigParser(InputStream stream, String encoding) {
      this.jj_la1 = new int[6];
      this.jj_expentries = new ArrayList();
      this.jj_kind = -1;
      this.trace_indent = 0;
      this.trace_enabled = true;

      try {
         this.jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1);
      } catch (UnsupportedEncodingException var4) {
         throw new RuntimeException(var4);
      }

      this.token_source = new SunConfigParserTokenManager(this.jj_input_stream);
      this.token = new Token();
      this.jj_ntk = -1;
      this.jj_gen = 0;

      for(int i = 0; i < 6; ++i) {
         this.jj_la1[i] = -1;
      }

   }

   public void ReInit(InputStream stream) {
      this.ReInit(stream, (String)null);
   }

   public void ReInit(InputStream stream, String encoding) {
      try {
         this.jj_input_stream.ReInit(stream, encoding, 1, 1);
      } catch (UnsupportedEncodingException var4) {
         throw new RuntimeException(var4);
      }

      this.token_source.ReInit(this.jj_input_stream);
      this.token = new Token();
      this.jj_ntk = -1;
      this.jj_gen = 0;

      for(int i = 0; i < 6; ++i) {
         this.jj_la1[i] = -1;
      }

   }

   public SunConfigParser(Reader stream) {
      this.jj_la1 = new int[6];
      this.jj_expentries = new ArrayList();
      this.jj_kind = -1;
      this.trace_indent = 0;
      this.trace_enabled = true;
      this.jj_input_stream = new SimpleCharStream(stream, 1, 1);
      this.token_source = new SunConfigParserTokenManager(this.jj_input_stream);
      this.token = new Token();
      this.jj_ntk = -1;
      this.jj_gen = 0;

      for(int i = 0; i < 6; ++i) {
         this.jj_la1[i] = -1;
      }

   }

   public void ReInit(Reader stream) {
      this.jj_input_stream.ReInit((Reader)stream, 1, 1);
      this.token_source.ReInit(this.jj_input_stream);
      this.token = new Token();
      this.jj_ntk = -1;
      this.jj_gen = 0;

      for(int i = 0; i < 6; ++i) {
         this.jj_la1[i] = -1;
      }

   }

   public SunConfigParser(SunConfigParserTokenManager tm) {
      this.jj_la1 = new int[6];
      this.jj_expentries = new ArrayList();
      this.jj_kind = -1;
      this.trace_indent = 0;
      this.trace_enabled = true;
      this.token_source = tm;
      this.token = new Token();
      this.jj_ntk = -1;
      this.jj_gen = 0;

      for(int i = 0; i < 6; ++i) {
         this.jj_la1[i] = -1;
      }

   }

   public void ReInit(SunConfigParserTokenManager tm) {
      this.token_source = tm;
      this.token = new Token();
      this.jj_ntk = -1;
      this.jj_gen = 0;

      for(int i = 0; i < 6; ++i) {
         this.jj_la1[i] = -1;
      }

   }

   private Token jj_consume_token(int kind) throws ParseException {
      Token oldToken;
      if ((oldToken = this.token).next != null) {
         this.token = this.token.next;
      } else {
         this.token = this.token.next = this.token_source.getNextToken();
      }

      this.jj_ntk = -1;
      if (this.token.kind == kind) {
         ++this.jj_gen;
         this.trace_token(this.token, "");
         return this.token;
      } else {
         this.token = oldToken;
         this.jj_kind = kind;
         throw this.generateParseException();
      }
   }

   public final Token getNextToken() {
      if (this.token.next != null) {
         this.token = this.token.next;
      } else {
         this.token = this.token.next = this.token_source.getNextToken();
      }

      this.jj_ntk = -1;
      ++this.jj_gen;
      this.trace_token(this.token, " (in getNextToken)");
      return this.token;
   }

   public final Token getToken(int index) {
      Token t = this.token;

      for(int i = 0; i < index; ++i) {
         if (t.next != null) {
            t = t.next;
         } else {
            t = t.next = this.token_source.getNextToken();
         }
      }

      return t;
   }

   private int jj_ntk() {
      return (this.jj_nt = this.token.next) == null ? (this.jj_ntk = (this.token.next = this.token_source.getNextToken()).kind) : (this.jj_ntk = this.jj_nt.kind);
   }

   public ParseException generateParseException() {
      this.jj_expentries.clear();
      boolean[] la1tokens = new boolean[22];
      if (this.jj_kind >= 0) {
         la1tokens[this.jj_kind] = true;
         this.jj_kind = -1;
      }

      int i;
      int j;
      for(i = 0; i < 6; ++i) {
         if (this.jj_la1[i] == this.jj_gen) {
            for(j = 0; j < 32; ++j) {
               if ((jj_la1_0[i] & 1 << j) != 0) {
                  la1tokens[j] = true;
               }
            }
         }
      }

      for(i = 0; i < 22; ++i) {
         if (la1tokens[i]) {
            this.jj_expentry = new int[1];
            this.jj_expentry[0] = i;
            this.jj_expentries.add(this.jj_expentry);
         }
      }

      int[][] exptokseq = new int[this.jj_expentries.size()][];

      for(j = 0; j < this.jj_expentries.size(); ++j) {
         exptokseq[j] = (int[])this.jj_expentries.get(j);
      }

      return new ParseException(this.token, exptokseq, tokenImage);
   }

   public final void enable_tracing() {
      this.trace_enabled = true;
   }

   public final void disable_tracing() {
      this.trace_enabled = false;
   }

   private void trace_call(String s) {
      if (this.trace_enabled) {
         for(int i = 0; i < this.trace_indent; ++i) {
            System.out.print(" ");
         }

         System.out.println("Call:   " + s);
      }

      this.trace_indent += 2;
   }

   private void trace_return(String s) {
      this.trace_indent -= 2;
      if (this.trace_enabled) {
         for(int i = 0; i < this.trace_indent; ++i) {
            System.out.print(" ");
         }

         System.out.println("Return: " + s);
      }

   }

   private void trace_token(Token t, String where) {
      if (this.trace_enabled) {
         for(int i = 0; i < this.trace_indent; ++i) {
            System.out.print(" ");
         }

         System.out.print("Consumed token: <" + tokenImage[t.kind]);
         if (t.kind != 0 && !tokenImage[t.kind].equals("\"" + t.image + "\"")) {
            System.out.print(": \"" + t.image + "\"");
         }

         System.out.println(" at line " + t.beginLine + " column " + t.beginColumn + ">" + where);
      }

   }

   private void trace_scan(Token t1, int t2) {
      if (this.trace_enabled) {
         for(int i = 0; i < this.trace_indent; ++i) {
            System.out.print(" ");
         }

         System.out.print("Visited token: <" + tokenImage[t1.kind]);
         if (t1.kind != 0 && !tokenImage[t1.kind].equals("\"" + t1.image + "\"")) {
            System.out.print(": \"" + t1.image + "\"");
         }

         System.out.println(" at line " + t1.beginLine + " column " + t1.beginColumn + ">; Expected token: <" + tokenImage[t2] + ">");
      }

   }

   static {
      jj_la1_init_0();
   }
}
