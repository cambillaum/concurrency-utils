/*
 * Copyright (C) 2012 Mathieu Cambillau <cambillaum@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.cambillaum.concurrencyutils.collection;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * An implementation of the {@link Set} interface backed by a {@link ConcurrentHashMap}.
 * Like {@link  ConcurrentHashMap}, this class supports full concurrency of
 * retrievals and adjustable expected concurrency for updates.
 *
 * @author Mathieu Cambillau <cambillaum@gmail.com>
 */
public class ConcurrentSet<E extends Object> implements Set<E> {

    private Map<E, Boolean> map;

    /**
     * Creates a new empty set with a default initial capacity, load factor and
     * concurrency level.
     */
    public ConcurrentSet() {
        this.map = new ConcurrentHashMap<E, Boolean>();
    }

    /**
     * Creates a new empty set with the specified initial capacity and with
     * default load factor and concurrency level.
     *
     * @param initialCapacity the initial capacity. The implementation performs
     * internal sizing to accommodate this many elements
     */
    public ConcurrentSet(int initialCapacity) {
        this.map = new ConcurrentHashMap<E, Boolean>(initialCapacity);
    }

    /**
     * Creates a new empty set with the specified initial capacity and load
     * factor and with the default concurrency level.
     *
     * @param initialCapacity the initial capacity. The implementation performs
     * internal sizing to accommodate this many elements.
     * @param loadFactor the load factor threshold, used to control resizing.
     * Resizing may be performed when the average number of elements per bin
     * exceeds this threshold.
     */
    public ConcurrentSet(int initialCapacity, float loadFactor) {
        this.map = new ConcurrentHashMap<E, Boolean>(initialCapacity, loadFactor);
    }

    /**
     * Creates a new, empty set with the specified initial capacity, load
     * factor, and concurrency level.
     *
     * @param initialCapacity the initial capacity. The implementation performs
     * internal sizing to accommodate this many elements.
     * @param loadFactor the load factor threshold, used to control resizing.
     * Resizing may be performed when the average number of elements per bin
     * exceeds this threshold.
     * @param concurrencyLevel the estimated number of concurrently updating
     * threads. The implementation performs internal sizing to try to
     * accommodate this many threads.
     */
    public ConcurrentSet(int initialCapacity, float loadFactor, int concurrencyLevel) {
        this.map = new ConcurrentHashMap<E, Boolean>(initialCapacity, loadFactor, concurrencyLevel);
    }

    @Override
    public int size() {
        return this.map.size();
    }

    @Override
    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return this.map.containsKey(o);
    }

    @Override
    public Iterator<E> iterator() {
        return this.map.keySet().iterator();
    }

    @Override
    public Object[] toArray() {
        return this.map.keySet().toArray();
    }

    @Override
    public <T> T[] toArray(T[] ts) {
        return this.map.keySet().toArray(ts);
    }

    @Override
    public boolean add(E e) {
        Boolean returnedObject = this.map.put(e, Boolean.TRUE);
        if (returnedObject == null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean remove(Object o) {
        Boolean returnedObject = this.map.remove(o);
        if (returnedObject == null) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean containsAll(Collection<?> clctn) {
        return this.map.keySet().containsAll(clctn);
    }

    @Override
    public boolean addAll(Collection<? extends E> clctn) {
        boolean collectionChanged = false;
        for (E e : clctn) {
            boolean added = this.add(e);
            if (added) {
                collectionChanged = true;
            }
        }
        return collectionChanged;
    }

    @Override
    public boolean retainAll(Collection<?> clctn) {
        return this.map.keySet().retainAll(clctn);
    }

    @Override
    public boolean removeAll(Collection<?> clctn) {
        return this.map.keySet().removeAll(clctn);
    }

    @Override
    public void clear() {
        this.map.clear();
    }
}
