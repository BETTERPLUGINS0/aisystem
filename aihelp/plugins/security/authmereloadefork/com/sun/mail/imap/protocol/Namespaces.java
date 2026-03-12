package com.sun.mail.imap.protocol;

import com.sun.mail.iap.ProtocolException;
import com.sun.mail.iap.Response;
import java.util.ArrayList;

public class Namespaces {
   public Namespaces.Namespace[] personal;
   public Namespaces.Namespace[] otherUsers;
   public Namespaces.Namespace[] shared;

   public Namespaces(Response r) throws ProtocolException {
      this.personal = this.getNamespaces(r);
      this.otherUsers = this.getNamespaces(r);
      this.shared = this.getNamespaces(r);
   }

   private Namespaces.Namespace[] getNamespaces(Response r) throws ProtocolException {
      if (!r.isNextNonSpace('(')) {
         String s = r.readAtom();
         if (s == null) {
            throw new ProtocolException("Expected NIL, got null");
         } else if (!s.equalsIgnoreCase("NIL")) {
            throw new ProtocolException("Expected NIL, got " + s);
         } else {
            return null;
         }
      } else {
         ArrayList v = new ArrayList();

         do {
            Namespaces.Namespace ns = new Namespaces.Namespace(r);
            v.add(ns);
         } while(!r.isNextNonSpace(')'));

         return (Namespaces.Namespace[])v.toArray(new Namespaces.Namespace[v.size()]);
      }
   }

   public static class Namespace {
      public String prefix;
      public char delimiter;

      public Namespace(Response r) throws ProtocolException {
         if (!r.isNextNonSpace('(')) {
            throw new ProtocolException("Missing '(' at start of Namespace");
         } else {
            this.prefix = r.readString();
            if (!r.supportsUtf8()) {
               this.prefix = BASE64MailboxDecoder.decode(this.prefix);
            }

            r.skipSpaces();
            if (r.peekByte() == 34) {
               r.readByte();
               this.delimiter = (char)r.readByte();
               if (this.delimiter == '\\') {
                  this.delimiter = (char)r.readByte();
               }

               if (r.readByte() != 34) {
                  throw new ProtocolException("Missing '\"' at end of QUOTED_CHAR");
               }
            } else {
               String s = r.readAtom();
               if (s == null) {
                  throw new ProtocolException("Expected NIL, got null");
               }

               if (!s.equalsIgnoreCase("NIL")) {
                  throw new ProtocolException("Expected NIL, got " + s);
               }

               this.delimiter = 0;
            }

            if (!r.isNextNonSpace(')')) {
               r.readString();
               r.skipSpaces();
               r.readStringList();
               if (!r.isNextNonSpace(')')) {
                  throw new ProtocolException("Missing ')' at end of Namespace");
               }
            }
         }
      }
   }
}
