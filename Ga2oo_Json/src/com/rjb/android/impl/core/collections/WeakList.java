package com.rjb.android.impl.core.collections;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by rbrett on 11/4/13.
 */
public class WeakList<T> {
    private final List<WeakReference<T>> fItems = new LinkedList<WeakReference<T>>();

    public WeakList() {
    }

    public void clear() {
        fItems.clear();
    }

    public void add(T aItem) {
        if (aItem == null) {
            return;
        }

        fItems.add(new WeakReference<T>(aItem));
    }

    public boolean remove(T aItem) {
        if (aItem == null) {
            return false;
        }

        Iterator<WeakReference<T>> iterator = fItems
                .iterator();

        while (iterator.hasNext()) {
            WeakReference<T> ItemWeakReference = iterator.next();

            T item = ItemWeakReference.get();
            if (item == null) {
                iterator.remove();
            } else if (aItem == item || aItem.equals(item)) {
                iterator.remove();
                return true;
            }
        }

        return false;
    }

    public List<T> get() {
        ArrayList<T> output = new ArrayList<T>(fItems.size());

        Iterator<WeakReference<T>> iterator = fItems
                .iterator();

        while (iterator.hasNext()) {
            WeakReference<T> ItemWeakReference = iterator.next();

            T item = ItemWeakReference.get();
            if (item == null) {
                iterator.remove();
            } else {
                output.add(item);
            }
        }

        return output;
    }

    public int size() {
        return fItems.size();
    }

    public boolean contains(T aItem) {
        if (aItem == null) {
            return false;
        }

        for (WeakReference<T> ItemWeakReference : fItems) {
            T item = ItemWeakReference.get();
            if (aItem == item || aItem.equals(item)) {
                return true;
            }
        }

        return false;
    }
}
