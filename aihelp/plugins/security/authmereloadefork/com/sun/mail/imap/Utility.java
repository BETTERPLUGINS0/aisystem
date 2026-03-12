package com.sun.mail.imap;

import com.sun.mail.imap.protocol.MessageSet;
import com.sun.mail.imap.protocol.UIDSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import javax.mail.Message;

public final class Utility {
   private Utility() {
   }

   public static MessageSet[] toMessageSet(Message[] msgs, Utility.Condition cond) {
      List<MessageSet> v = new ArrayList(1);

      for(int i = 0; i < msgs.length; ++i) {
         IMAPMessage msg = (IMAPMessage)msgs[i];
         if (!msg.isExpunged()) {
            int current = msg.getSequenceNumber();
            if (cond == null || cond.test(msg)) {
               MessageSet set = new MessageSet();
               set.start = current;
               ++i;

               for(; i < msgs.length; ++i) {
                  msg = (IMAPMessage)msgs[i];
                  if (!msg.isExpunged()) {
                     int next = msg.getSequenceNumber();
                     if (cond == null || cond.test(msg)) {
                        if (next != current + 1) {
                           --i;
                           break;
                        }

                        current = next;
                     }
                  }
               }

               set.end = current;
               v.add(set);
            }
         }
      }

      if (v.isEmpty()) {
         return null;
      } else {
         return (MessageSet[])v.toArray(new MessageSet[v.size()]);
      }
   }

   public static MessageSet[] toMessageSetSorted(Message[] msgs, Utility.Condition cond) {
      msgs = (Message[])msgs.clone();
      Arrays.sort(msgs, new Comparator<Message>() {
         public int compare(Message msg1, Message msg2) {
            return msg1.getMessageNumber() - msg2.getMessageNumber();
         }
      });
      return toMessageSet(msgs, cond);
   }

   public static UIDSet[] toUIDSet(Message[] msgs) {
      List<UIDSet> v = new ArrayList(1);

      for(int i = 0; i < msgs.length; ++i) {
         IMAPMessage msg = (IMAPMessage)msgs[i];
         if (!msg.isExpunged()) {
            long current = msg.getUID();
            UIDSet set = new UIDSet();
            set.start = current;
            ++i;

            for(; i < msgs.length; ++i) {
               msg = (IMAPMessage)msgs[i];
               if (!msg.isExpunged()) {
                  long next = msg.getUID();
                  if (next != current + 1L) {
                     --i;
                     break;
                  }

                  current = next;
               }
            }

            set.end = current;
            v.add(set);
         }
      }

      if (v.isEmpty()) {
         return null;
      } else {
         return (UIDSet[])v.toArray(new UIDSet[v.size()]);
      }
   }

   public static UIDSet[] getResyncUIDSet(ResyncData rd) {
      return rd.getUIDSet();
   }

   public interface Condition {
      boolean test(IMAPMessage var1);
   }
}
