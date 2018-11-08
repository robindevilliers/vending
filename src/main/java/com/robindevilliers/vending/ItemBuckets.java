package com.robindevilliers.vending;

import com.robindevilliers.vending.model.ItemBucket;

import java.util.List;

public class ItemBuckets {

    private final List<ItemBucket> buckets;

    public ItemBuckets(List<ItemBucket> buckets) {
        this.buckets = buckets;
    }

    public void add(ItemBucket itemBucket) {
        this.buckets.add(itemBucket);
    }

    public boolean isValidItemId(String id){
        return this.buckets.stream().anyMatch(item -> item.getId().equals(id));
    }

    //This is called by the service call.  It only resets the item quantities.  There is no way to specifying item definitions via the CLI.
    public void reset() {
        this.buckets.forEach(bucket-> bucket.setQuantity(20));
    }

    public int getPriceForItem(String itemId) {
        final ItemBucket itemBucket = this.buckets.stream()
                .filter(item -> item.getId().equals(itemId))
                .findAny()
                .orElseThrow(() -> new SystemException("ItemId not found."));

        return itemBucket.getPrice();
    }

    public void ejectItem(String itemId) {
        final ItemBucket itemBucket = this.buckets.stream()
                .filter(item -> item.getId().equals(itemId))
                .findAny()
                .orElseThrow(() -> new SystemException("ItemId not found."));

        itemBucket.setQuantity( itemBucket.getQuantity() - 1);

    }

    public boolean hasItem(String itemId) {
        final ItemBucket itemBucket = this.buckets.stream()
                .filter(item -> item.getId().equals(itemId))
                .findAny()
                .orElseThrow(() -> new SystemException("ItemId not found."));

        return itemBucket.getQuantity() > 0;
    }
}
