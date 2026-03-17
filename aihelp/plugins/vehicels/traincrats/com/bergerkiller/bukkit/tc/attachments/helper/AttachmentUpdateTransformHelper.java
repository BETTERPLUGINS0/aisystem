package com.bergerkiller.bukkit.tc.attachments.helper;

import com.bergerkiller.bukkit.common.math.Matrix4x4;
import com.bergerkiller.bukkit.tc.attachments.api.Attachment;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

public abstract class AttachmentUpdateTransformHelper {
   protected final QueuedActiveChangeHandler activeChangeHandler = new QueuedActiveChangeHandler();

   public static AttachmentUpdateTransformHelper createSimple() {
      return new AttachmentUpdateTransformHelper.AttachmentUpdateHelperSingleThreaded();
   }

   public static AttachmentUpdateTransformHelper create(int parallelism) {
      if (parallelism <= 0) {
         parallelism = Runtime.getRuntime().availableProcessors();
      }

      return (AttachmentUpdateTransformHelper)(parallelism > 1 ? new AttachmentUpdateTransformHelper.AttachmentUpdateHelperMultiThreaded(parallelism) : new AttachmentUpdateTransformHelper.AttachmentUpdateHelperSingleThreaded());
   }

   protected AttachmentUpdateTransformHelper() {
   }

   public final void startAndFinish(Attachment attachment, Matrix4x4 initialTransform) {
      try {
         this.start(attachment, initialTransform);
      } finally {
         this.finish();
      }

   }

   public abstract void start(Attachment var1, Matrix4x4 var2);

   public abstract void finish();

   private static final class AttachmentUpdateHelperSingleThreaded extends AttachmentUpdateTransformHelper {
      private final ArrayList<Attachment> pendingUpdates;

      private AttachmentUpdateHelperSingleThreaded() {
         this.pendingUpdates = new ArrayList();
      }

      public void start(Attachment attachment, Matrix4x4 initialTransform) {
         attachment.getInternalState().updateTransform(attachment, initialTransform, this.activeChangeHandler);
         this.pendingUpdates.addAll(attachment.getChildren());
      }

      public void finish() {
         int endIndex;
         try {
            for(int startIndex = 0; startIndex < (endIndex = this.pendingUpdates.size()); startIndex = endIndex) {
               for(int index = startIndex; index < endIndex; ++index) {
                  Attachment attachment = (Attachment)this.pendingUpdates.get(index);
                  attachment.getInternalState().updateTransform(attachment, attachment.getParent().getTransform(), this.activeChangeHandler);
                  this.pendingUpdates.addAll(attachment.getChildren());
               }
            }
         } finally {
            this.pendingUpdates.clear();
            this.activeChangeHandler.sync();
         }

      }

      // $FF: synthetic method
      AttachmentUpdateHelperSingleThreaded(Object x0) {
         this();
      }
   }

   private static final class AttachmentUpdateHelperMultiThreaded extends AttachmentUpdateTransformHelper {
      private final List<ForkJoinTask<Void>> pendingTasks = new ArrayList();
      private final ForkJoinPool pool;

      public AttachmentUpdateHelperMultiThreaded(int parallelism) {
         this.pool = new ForkJoinPool(parallelism, ForkJoinPool.defaultForkJoinWorkerThreadFactory, (UncaughtExceptionHandler)null, false);
      }

      public void start(Attachment attachment, Matrix4x4 initialTransform) {
         ForkJoinTask<Void> task = attachment.getInternalState().updateTransformRecurseAsync(attachment, initialTransform, this.activeChangeHandler);
         this.pendingTasks.add(task);
         this.pool.execute(task);
      }

      public void finish() {
         try {
            for(int i = this.pendingTasks.size() - 1; i >= 0; --i) {
               ((ForkJoinTask)this.pendingTasks.get(i)).join();
            }
         } finally {
            this.pendingTasks.clear();
            this.activeChangeHandler.sync();
         }

      }
   }
}
