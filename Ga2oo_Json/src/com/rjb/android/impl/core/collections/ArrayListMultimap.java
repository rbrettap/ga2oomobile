
package com.rjb.android.impl.core.collections;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

// TODO: this is quick and dirty, make this a formal collection and add interfaces if we so desire, or replace with Guava Multimap
public class ArrayListMultimap<K, V> {
    private final Map<K, List<V>> fMap;
    private int fInitialValueCapacity;

    public ArrayListMultimap() {
        fMap = new HashMap<K, List<V>>();
    }

    public ArrayListMultimap(int initialKeyCapacity, int initialValueCapacity) {
        fMap = new HashMap<K, List<V>>(initialKeyCapacity);
        fInitialValueCapacity = initialValueCapacity;
    }

    public void clear() {
        fMap.clear();
    }

    public List<V> get(K key) {
        if (key == null) {
            return Collections.emptyList();
        }

        List<V> mapEntries = getListForKey(key, false);
        
        // Return empty list if no item exists in the map.
        if (null == mapEntries) {
        	return Collections.emptyList();
        }
        
        return mapEntries;
    }

    public void put(K key, V value) {
        if (key == null) {
            return;
        }

        List<V> list = getListForKey(key, true);
        list.add(value);
    }

    public void putAll(ArrayListMultimap<K, V> map) {
        if (map == null) {
            return;
        }

        Set<Map.Entry<K, List<V>>> entrySet = map.fMap.entrySet();
        for (Map.Entry<K, List<V>> entry : entrySet) {
            fMap.put(entry.getKey(), entry.getValue());
        }
    }

    public boolean remove(K key, V value) {
        if (key == null) {
            return false;
        }

        List<V> list = getListForKey(key, false);
        if (list != null) {
            boolean removed = list.remove(value);
            if (list.size() == 0) {
                fMap.remove(key);
            }

            return removed;
        }

        return false;
    }

    public boolean removeAll(K key) {
        if (key == null) {
            return false;
        }

        List<V> list = fMap.remove(key);
        return list != null;
    }

    public boolean containsKey(K key) {
        if (key == null) {
            return false;
        }

        return fMap.containsKey(key);
    }

    public boolean containsEntry(K key, V value) {
        if (key == null) {
            return false;
        }

        List<V> list = getListForKey(key, false);
        if (list != null) {
            return list.contains(value);
        }

        return false;
    }

    public Collection<Map.Entry<K, V>> entries() {
        ArrayList<Map.Entry<K, V>> entries = new ArrayList<Map.Entry<K, V>>();

        Set<Map.Entry<K, List<V>>> entrySet = fMap.entrySet();
        for (Map.Entry<K, List<V>> entry : entrySet) {
            for (V value : entry.getValue()) {
                entries.add(new AbstractMap.SimpleImmutableEntry<K, V>(entry.getKey(), value));
            }
        }

        return entries;
    }

    public Set<K> keySet() {
        return fMap.keySet();
    }

    public Collection<V> values() {
        ArrayList<V> values = new ArrayList<V>();

        Set<Map.Entry<K, List<V>>> entrySet = fMap.entrySet();
        for (Map.Entry<K, List<V>> entry : entrySet) {
            values.addAll(entry.getValue());
        }

        return values;
    }

    public int size() {
        int size = 0;

        Set<Map.Entry<K, List<V>>> entrySet = fMap.entrySet();
        for (Map.Entry<K, List<V>> entry : entrySet) {
            size += entry.getValue().size();
        }

        return size;
    }

    public boolean isEmpty() {
        return fMap.keySet().size() == 0;
    }

    private List<V> getListForKey(K key, boolean create) {
        List<V> list = fMap.get(key);
        if (create && list == null) {
            if (fInitialValueCapacity > 0) {
                list = new ArrayList<V>(fInitialValueCapacity);
            } else {
                list = new ArrayList<V>();
            }

            fMap.put(key, list);
        }

        return list;
    }
    
}
