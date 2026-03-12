package fr.xephi.authme.libs.com.github.benmanes.caffeine.cache;

class WISMW<K, V> extends WIS<K, V> {
   long maximum;
   long weightedSize;
   long windowMaximum;
   long windowWeightedSize;
   long mainProtectedMaximum;
   long mainProtectedWeightedSize;
   double stepSize;
   long adjustment;
   int hitsInSample;
   int missesInSample;
   double previousSampleHitRate;
   final FrequencySketch<K> sketch = new FrequencySketch();
   final AccessOrderDeque<Node<K, V>> accessOrderWindowDeque;
   final AccessOrderDeque<Node<K, V>> accessOrderProbationDeque;
   final AccessOrderDeque<Node<K, V>> accessOrderProtectedDeque;

   WISMW(Caffeine<K, V> builder, CacheLoader<? super K, V> cacheLoader, boolean async) {
      super(builder, cacheLoader, async);
      if (builder.hasInitialCapacity()) {
         long capacity = Math.min(builder.getMaximum(), (long)builder.getInitialCapacity());
         this.sketch.ensureCapacity(capacity);
      }

      this.accessOrderWindowDeque = !builder.evicts() && !builder.expiresAfterAccess() ? null : new AccessOrderDeque();
      this.accessOrderProbationDeque = new AccessOrderDeque();
      this.accessOrderProtectedDeque = new AccessOrderDeque();
   }

   protected final boolean evicts() {
      return true;
   }

   protected final long maximum() {
      return this.maximum;
   }

   protected final void setMaximum(long maximum) {
      this.maximum = maximum;
   }

   protected final long weightedSize() {
      return this.weightedSize;
   }

   protected final void setWeightedSize(long weightedSize) {
      this.weightedSize = weightedSize;
   }

   protected final long windowMaximum() {
      return this.windowMaximum;
   }

   protected final void setWindowMaximum(long windowMaximum) {
      this.windowMaximum = windowMaximum;
   }

   protected final long windowWeightedSize() {
      return this.windowWeightedSize;
   }

   protected final void setWindowWeightedSize(long windowWeightedSize) {
      this.windowWeightedSize = windowWeightedSize;
   }

   protected final long mainProtectedMaximum() {
      return this.mainProtectedMaximum;
   }

   protected final void setMainProtectedMaximum(long mainProtectedMaximum) {
      this.mainProtectedMaximum = mainProtectedMaximum;
   }

   protected final long mainProtectedWeightedSize() {
      return this.mainProtectedWeightedSize;
   }

   protected final void setMainProtectedWeightedSize(long mainProtectedWeightedSize) {
      this.mainProtectedWeightedSize = mainProtectedWeightedSize;
   }

   protected final double stepSize() {
      return this.stepSize;
   }

   protected final void setStepSize(double stepSize) {
      this.stepSize = stepSize;
   }

   protected final long adjustment() {
      return this.adjustment;
   }

   protected final void setAdjustment(long adjustment) {
      this.adjustment = adjustment;
   }

   protected final int hitsInSample() {
      return this.hitsInSample;
   }

   protected final void setHitsInSample(int hitsInSample) {
      this.hitsInSample = hitsInSample;
   }

   protected final int missesInSample() {
      return this.missesInSample;
   }

   protected final void setMissesInSample(int missesInSample) {
      this.missesInSample = missesInSample;
   }

   protected final double previousSampleHitRate() {
      return this.previousSampleHitRate;
   }

   protected final void setPreviousSampleHitRate(double previousSampleHitRate) {
      this.previousSampleHitRate = previousSampleHitRate;
   }

   protected final FrequencySketch<K> frequencySketch() {
      return this.sketch;
   }

   protected final AccessOrderDeque<Node<K, V>> accessOrderWindowDeque() {
      return this.accessOrderWindowDeque;
   }

   protected final AccessOrderDeque<Node<K, V>> accessOrderProbationDeque() {
      return this.accessOrderProbationDeque;
   }

   protected final AccessOrderDeque<Node<K, V>> accessOrderProtectedDeque() {
      return this.accessOrderProtectedDeque;
   }
}
