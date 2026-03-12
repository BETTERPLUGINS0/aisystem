package fr.xephi.authme.libs.org.postgresql.replication.fluent.logical;

import fr.xephi.authme.libs.org.postgresql.replication.LogSequenceNumber;
import fr.xephi.authme.libs.org.postgresql.replication.PGReplicationStream;
import fr.xephi.authme.libs.org.postgresql.replication.fluent.AbstractStreamBuilder;
import fr.xephi.authme.libs.org.postgresql.util.internal.Nullness;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Properties;
import org.checkerframework.checker.nullness.qual.Nullable;

public class LogicalStreamBuilder extends AbstractStreamBuilder<ChainedLogicalStreamBuilder> implements ChainedLogicalStreamBuilder, LogicalReplicationOptions {
   private final Properties slotOptions;
   private final StartLogicalReplicationCallback startCallback;

   public LogicalStreamBuilder(StartLogicalReplicationCallback startCallback) {
      this.startCallback = startCallback;
      this.slotOptions = new Properties();
   }

   protected ChainedLogicalStreamBuilder self() {
      return this;
   }

   public PGReplicationStream start() throws SQLException {
      return this.startCallback.start(this);
   }

   @Nullable
   public String getSlotName() {
      return this.slotName;
   }

   public ChainedLogicalStreamBuilder withStartPosition(LogSequenceNumber lsn) {
      this.startPosition = lsn;
      return this;
   }

   public ChainedLogicalStreamBuilder withSlotOption(String optionName, boolean optionValue) {
      this.slotOptions.setProperty(optionName, String.valueOf(optionValue));
      return this;
   }

   public ChainedLogicalStreamBuilder withSlotOption(String optionName, int optionValue) {
      this.slotOptions.setProperty(optionName, String.valueOf(optionValue));
      return this;
   }

   public ChainedLogicalStreamBuilder withSlotOption(String optionName, String optionValue) {
      this.slotOptions.setProperty(optionName, optionValue);
      return this;
   }

   public ChainedLogicalStreamBuilder withSlotOptions(Properties options) {
      Iterator var2 = options.stringPropertyNames().iterator();

      while(var2.hasNext()) {
         String propertyName = (String)var2.next();
         this.slotOptions.setProperty(propertyName, (String)Nullness.castNonNull(options.getProperty(propertyName)));
      }

      return this;
   }

   public LogSequenceNumber getStartLSNPosition() {
      return this.startPosition;
   }

   public Properties getSlotOptions() {
      return this.slotOptions;
   }

   public int getStatusInterval() {
      return this.statusIntervalMs;
   }
}
