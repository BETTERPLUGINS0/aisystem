/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.sbdeveloper.vehiclesplus.libs.inventory.content;

import java.util.Arrays;
import nl.sbdeveloper.vehiclesplus.libs.inventory.ClickableItem;
import nl.sbdeveloper.vehiclesplus.libs.inventory.content.SlotIterator;

public interface Pagination {
    public ClickableItem[] getPageItems();

    public int getPage();

    public Pagination page(int var1);

    public boolean isFirst();

    public boolean isLast();

    public Pagination first();

    public Pagination previous();

    public Pagination next();

    public Pagination last();

    public Pagination addToIterator(SlotIterator var1);

    public Pagination setItems(ClickableItem ... var1);

    public Pagination setItemsPerPage(int var1);

    public static class Impl
    implements Pagination {
        private int currentPage;
        private ClickableItem[] items = new ClickableItem[0];
        private int itemsPerPage = 5;

        @Override
        public ClickableItem[] getPageItems() {
            return Arrays.copyOfRange(this.items, this.currentPage * this.itemsPerPage, (this.currentPage + 1) * this.itemsPerPage);
        }

        @Override
        public int getPage() {
            return this.currentPage;
        }

        @Override
        public Pagination page(int n) {
            this.currentPage = n;
            return this;
        }

        @Override
        public boolean isFirst() {
            return this.currentPage == 0;
        }

        @Override
        public boolean isLast() {
            int n = (int)Math.ceil((double)this.items.length / (double)this.itemsPerPage);
            return this.currentPage >= n - 1;
        }

        @Override
        public Pagination first() {
            this.currentPage = 0;
            return this;
        }

        @Override
        public Pagination previous() {
            if (!this.isFirst()) {
                --this.currentPage;
            }
            return this;
        }

        @Override
        public Pagination next() {
            if (!this.isLast()) {
                ++this.currentPage;
            }
            return this;
        }

        @Override
        public Pagination last() {
            this.currentPage = this.items.length / this.itemsPerPage;
            return this;
        }

        @Override
        public Pagination addToIterator(SlotIterator slotIterator) {
            for (ClickableItem clickableItem : this.getPageItems()) {
                slotIterator.next().set(clickableItem);
                if (slotIterator.ended()) break;
            }
            return this;
        }

        @Override
        public Pagination setItems(ClickableItem ... clickableItemArray) {
            this.items = clickableItemArray;
            return this;
        }

        @Override
        public Pagination setItemsPerPage(int n) {
            this.itemsPerPage = n;
            return this;
        }
    }
}

