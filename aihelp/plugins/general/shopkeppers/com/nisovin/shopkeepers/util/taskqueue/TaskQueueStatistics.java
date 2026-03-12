package com.nisovin.shopkeepers.util.taskqueue;

public interface TaskQueueStatistics {
   int getPendingCount();

   int getMaxPendingCount();
}
