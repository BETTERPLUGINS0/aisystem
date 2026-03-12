package fr.xephi.authme.libs.org.mariadb.jdbc.client.impl;

import fr.xephi.authme.libs.org.mariadb.jdbc.message.client.RedoableClientMessage;
import java.util.Arrays;

public class TransactionSaver {
   private final RedoableClientMessage[] buffers;
   private int idx = 0;
   private boolean dirty = false;

   public TransactionSaver(int transactionReplaySize) {
      this.buffers = new RedoableClientMessage[transactionReplaySize];
   }

   public void add(RedoableClientMessage clientMessage) {
      if (this.idx < this.buffers.length) {
         this.buffers[this.idx++] = clientMessage;
      } else {
         this.dirty = true;
      }

   }

   public void clear() {
      Arrays.fill(this.buffers, (Object)null);
      this.dirty = false;
      this.idx = 0;
   }

   public int getIdx() {
      return this.idx;
   }

   public boolean isDirty() {
      return this.dirty;
   }

   public RedoableClientMessage[] getBuffers() {
      return this.buffers;
   }
}
