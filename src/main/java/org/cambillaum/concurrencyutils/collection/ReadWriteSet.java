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

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * An implementation of the {@link Set } interface that supports full read
 * concurrency and limits write operations to one thread at a time. <p>Whether
 * this implementation will improve performance over the use of a non
 * synchronized {@link Set} implementation depends on the frequency that the
 * data is read compared to being modified, the duration of the read and write
 * operations, and the contention for the data.
 *
 * @author Mathieu Cambillau <cambillaum@gmail.com>
 */
public class ReadWriteSet<E extends Object> implements Set<E>, Serializable {

    private Set<E> set;
    private ReadWriteLock readWriteLock;
    private Lock readLock;
    private Lock writeLock;

    /**
     * Constructs a new empty ReadWriteSet backed by the specified {@link Set}.
     *
     * @param set the {@link Set } that backs this ReadWriteSet.
     */
    public ReadWriteSet(Set<E> set) {
        this(set, new ReentrantReadWriteLock());
    }

    /**
     * Constructs a new empty ReadWriteSet backed by the specified {@link Set
     * }
     * with the given fairness policy.
     *
     * @param set the {@link Set} that backs this ReadWriteSet.
     * @param fair true if the set's lock should use a fair ordering policy.
     */
    public ReadWriteSet(Set<E> set, boolean fair) {
        this(set, new ReentrantReadWriteLock(fair));
    }

    /**
     * Constructs a new empty ReadWriteSet backed by the specified {@link Set
     * }
     * and using the given {@link ReadWriteLock } implementation.
     *
     * @param set the {@link Set } that backs this ReadWriteSet.
     * @param readWriteLock the {@link ReadWriteLock } implementation used to
     * enforce the locking policy.
     */
    ReadWriteSet(Set<E> set, ReadWriteLock readWriteLock) {
        this.set = set;
        this.readWriteLock = readWriteLock;
        this.readLock = this.readWriteLock.readLock();
        this.writeLock = this.readWriteLock.writeLock();

        if (this.set == null) {
            throw new IllegalArgumentException("set cannot be null");
        }
    }

    @Override
    public int size() {
        this.readLock.lock();
        try {
            return this.set.size();
        } finally {
            this.readLock.unlock();
        }
    }

    @Override
    public boolean isEmpty() {
        this.readLock.lock();
        try {
            return this.set.isEmpty();
        } finally {
            this.readLock.unlock();
        }
    }

    @Override
    public boolean contains(Object o) {
        this.readLock.lock();
        try {
            return this.set.contains(o);
        } finally {
            this.readLock.unlock();
        }
    }

    @Override
    public Iterator<E> iterator() {
        this.readLock.lock();
        try {
            Set<E> copy = getCopyDependingOnType();
            return copy.iterator();
        } finally {
            this.readLock.unlock();
        }
    }

    @Override
    public Object[] toArray() {
        this.readLock.lock();
        try {
            return this.set.toArray();
        } finally {
            this.readLock.unlock();
        }
    }

    @Override
    public <T> T[] toArray(T[] ts) {
        this.readLock.lock();
        try {
            return this.set.toArray(ts);
        } finally {
            this.readLock.unlock();
        }
    }

    @Override
    public boolean add(E e) {
        this.writeLock.lock();
        try {
            return this.set.add(e);
        } finally {
            this.writeLock.unlock();
        }
    }

    @Override
    public boolean remove(Object o) {
        this.writeLock.lock();
        try {
            return this.set.remove(o);
        } finally {
            this.writeLock.unlock();
        }
    }

    @Override
    public boolean containsAll(Collection<?> clctn) {
        this.readLock.lock();
        try {
            return this.set.containsAll(clctn);
        } finally {
            this.readLock.unlock();
        }
    }

    @Override
    public boolean addAll(Collection<? extends E> clctn) {
        this.writeLock.lock();
        try {
            return this.set.addAll(clctn);
        } finally {
            this.writeLock.unlock();
        }
    }

    @Override
    public boolean retainAll(Collection<?> clctn) {
        this.writeLock.lock();
        try {
            return this.set.retainAll(clctn);
        } finally {
            this.writeLock.unlock();
        }
    }

    @Override
    public boolean removeAll(Collection<?> clctn) {
        this.writeLock.lock();
        try {
            return this.set.removeAll(clctn);
        } finally {
            this.writeLock.unlock();
        }
    }

    @Override
    public void clear() {
        this.writeLock.lock();
        try {
            this.set.clear();
        } finally {
            this.writeLock.unlock();
        }
    }

    private Set<E> getCopyDependingOnType() {
        if (this.set instanceof HashSet) {
            return new HashSet<E>(this.set);
        } else if (this.set instanceof LinkedHashSet) {
            return new LinkedHashSet<E>(this.set);
        } else if (this.set instanceof TreeSet) {
            return new TreeSet<E>(this.set);
        } else if (this.set instanceof CopyOnWriteArraySet) {
            return new CopyOnWriteArraySet<E>(set);
        } else if (this.set instanceof ConcurrentSkipListSet) {
            return new ConcurrentSkipListSet<E>(this.set);
        } else {
            return new HashSet<E>(this.set);
        }
    }
}
