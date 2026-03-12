package ac.grim.grimac.utils.latency;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.utils.anticheat.LogUtil;
import ac.grim.grimac.utils.anticheat.MessageUtil;
import ac.grim.grimac.utils.common.arguments.CommonGrimArguments;
import ac.grim.grimac.utils.data.Pair;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

public class LatencyUtils {
   private final LinkedList<Pair<Integer, Runnable>> transactionMap = new LinkedList();
   private final GrimPlayer player;
   private final ArrayList<Runnable> tasksToRun = new ArrayList();

   public LatencyUtils(GrimPlayer player) {
      this.player = player;
   }

   public void addRealTimeTask(int transaction, Runnable runnable) {
      this.addRealTimeTask(transaction, false, runnable);
   }

   public void addRealTimeTaskAsync(int transaction, Runnable runnable) {
      this.addRealTimeTask(transaction, true, runnable);
   }

   public void addRealTimeTask(int transaction, boolean async, Runnable runnable) {
      if (this.player.lastTransactionReceived.get() >= transaction) {
         if (async) {
            this.player.runSafely(runnable);
         } else {
            runnable.run();
         }

      } else {
         synchronized(this) {
            this.transactionMap.add(new Pair(transaction, runnable));
         }
      }
   }

   public void handleNettySyncTransaction(int transaction) {
      synchronized(this) {
         this.tasksToRun.clear();
         ListIterator iterator = this.transactionMap.listIterator();

         while(iterator.hasNext()) {
            Pair<Integer, Runnable> pair = (Pair)iterator.next();
            if (transaction + 1 < (Integer)pair.first()) {
               break;
            }

            if (transaction != (Integer)pair.first() - 1) {
               this.tasksToRun.add((Runnable)pair.second());
               iterator.remove();
            }
         }

         Iterator var10 = this.tasksToRun.iterator();

         while(var10.hasNext()) {
            Runnable runnable = (Runnable)var10.next();

            try {
               runnable.run();
            } catch (Exception var8) {
               LogUtil.error("An error has occurred when running transactions for player: " + this.player.user.getName(), var8);
               if ((Boolean)CommonGrimArguments.KICK_ON_TRANSACTION_ERRORS.value()) {
                  this.player.disconnect(MessageUtil.miniMessage(MessageUtil.replacePlaceholders(this.player, GrimAPI.INSTANCE.getConfigManager().getDisconnectPacketError())));
               }
            }
         }

      }
   }
}
