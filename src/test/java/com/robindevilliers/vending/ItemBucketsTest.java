package com.robindevilliers.vending;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import com.robindevilliers.vending.model.ItemBucket;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemBucketsTest {

    @Test
    public void testGetPriceForItem_givenCorrectItemId() {

        ItemBuckets bucket = new ItemBuckets(Collections.singletonList(new ItemBucket("A", 65, 1)));

        int response = bucket.getPriceForItem("A");

        assertThat(response, is(65));
    }

    @Test
    public void testGetPriceForItem_givenIncorrectItemId() {
        ItemBuckets bucket = new ItemBuckets(Collections.emptyList());

        try {
            bucket.getPriceForItem("A");

            fail("expected exception to be thrown");
        } catch (SystemException e) {
            assertThat(e.getMessage(), is("ItemId not found."));
        }
    }

    @Test
    public void testAdd() {
        List<ItemBucket> list = new ArrayList<>();
        ItemBuckets itemBuckets = new ItemBuckets(list);

        itemBuckets.add(new ItemBucket("A", 65, 1));

        ItemBucket item = list.get(0);
        assertThat(item.getId(), is("A"));
        assertThat(item.getPrice(), is(65));
        assertThat(item.getQuantity(), is(1));
    }

    @Test
    public void testIsValidItemId_givenCorrectItemId() {

        ItemBuckets bucket = new ItemBuckets(Collections.singletonList(new ItemBucket("A", 65, 1)));

        boolean response = bucket.isValidItemId("A");

        assertThat(response, is(true));
    }

    @Test
    public void testIsValidItemId_givenIncorrectItemId() {

        ItemBuckets bucket = new ItemBuckets(Collections.emptyList());

        boolean response = bucket.isValidItemId("A");

        assertThat(response, is(false));
    }

    @Test
    public void testReset() {
        List<ItemBucket> list = Collections.singletonList(new ItemBucket("A", 65, 1));
        ItemBuckets bucket = new ItemBuckets(list);

        bucket.reset();

        assertThat(list.get(0).getQuantity(), is(20));
    }

    @Test
    public void testEjectItem_givenSufficientStock() {
        List<ItemBucket> list = Collections.singletonList(new ItemBucket("A", 65, 2));
        ItemBuckets bucket = new ItemBuckets(list);

        bucket.ejectItem("A");

        assertThat(list.get(0).getQuantity(), is(1));
    }

    @Test
    public void testEjectItem_givenInvalidItemId() {
        ItemBuckets bucket = new ItemBuckets(Collections.emptyList());

        try {
            bucket.ejectItem("A");
            fail("expected exception to be thrown");
        } catch (SystemException e) {
            assertThat(e.getMessage(), is("ItemId not found."));
        }
    }

    @Test
    public void testHasItem_givenValidItemId_AndSufficientStock() {
        List<ItemBucket> list = Collections.singletonList(new ItemBucket("A", 65, 2));
        ItemBuckets bucket = new ItemBuckets(list);

        boolean response = bucket.hasItem("A");

        assertThat(response, is(true));
    }

    @Test
    public void testHasItem_givenValidItemId_AndInsufficientStock() {
        List<ItemBucket> list = Collections.singletonList(new ItemBucket("A", 65, 0));
        ItemBuckets bucket = new ItemBuckets(list);

        boolean response = bucket.hasItem("A");

        assertThat(response, is(false));
    }

    @Test
    public void testHasItem_givenInvalidItemId() {
        ItemBuckets bucket = new ItemBuckets(Collections.emptyList());

        try {
            bucket.hasItem("A");
            fail("expected exception to be thrown");
        } catch (SystemException e) {
            assertThat(e.getMessage(), is("ItemId not found."));
        }
    }

}
