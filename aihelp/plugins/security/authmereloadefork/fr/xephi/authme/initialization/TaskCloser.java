package fr.xephi.authme.initialization;

import fr.xephi.authme.AuthMe;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.libs.com.github.Anon8281.universalScheduler.scheduling.schedulers.TaskScheduler;

public class TaskCloser implements Runnable {
   private final TaskScheduler scheduler = AuthMe.getScheduler();
   private final DataSource dataSource;

   public TaskCloser(DataSource dataSource) {
      this.dataSource = dataSource;
   }

   public void run() {
      this.scheduler.cancelTasks();
      if (this.dataSource != null) {
         this.dataSource.closeConnection();
      }

   }
}
