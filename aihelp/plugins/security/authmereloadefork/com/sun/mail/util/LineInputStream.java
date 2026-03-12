package com.sun.mail.util;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.nio.charset.StandardCharsets;

public class LineInputStream extends FilterInputStream {
   private boolean allowutf8;
   private byte[] lineBuffer;
   private static int MAX_INCR = 1048576;

   public LineInputStream(InputStream in) {
      this(in, false);
   }

   public LineInputStream(InputStream in, boolean allowutf8) {
      super(in);
      this.lineBuffer = null;
      this.allowutf8 = allowutf8;
   }

   public String readLine() throws IOException {
      byte[] buf = this.lineBuffer;
      if (buf == null) {
         buf = this.lineBuffer = new byte[128];
      }

      int room = buf.length;

      int c1;
      int offset;
      for(offset = 0; (c1 = this.in.read()) != -1 && c1 != 10; buf[offset++] = (byte)c1) {
         if (c1 == 13) {
            boolean twoCRs = false;
            if (this.in.markSupported()) {
               this.in.mark(2);
            }

            int c2 = this.in.read();
            if (c2 == 13) {
               twoCRs = true;
               c2 = this.in.read();
            }

            if (c2 != 10) {
               if (this.in.markSupported()) {
                  this.in.reset();
               } else {
                  if (!(this.in instanceof PushbackInputStream)) {
                     this.in = new PushbackInputStream(this.in, 2);
                  }

                  if (c2 != -1) {
                     ((PushbackInputStream)this.in).unread(c2);
                  }

                  if (twoCRs) {
                     ((PushbackInputStream)this.in).unread(13);
                  }
               }
            }
            break;
         }

         --room;
         if (room < 0) {
            if (buf.length < MAX_INCR) {
               buf = new byte[buf.length * 2];
            } else {
               buf = new byte[buf.length + MAX_INCR];
            }

            room = buf.length - offset - 1;
            System.arraycopy(this.lineBuffer, 0, buf, 0, offset);
            this.lineBuffer = buf;
         }
      }

      if (c1 == -1 && offset == 0) {
         return null;
      } else {
         return this.allowutf8 ? new String(buf, 0, offset, StandardCharsets.UTF_8) : new String(buf, 0, 0, offset);
      }
   }
}
