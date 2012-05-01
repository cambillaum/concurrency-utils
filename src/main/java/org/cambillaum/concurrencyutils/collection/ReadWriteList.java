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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * An implementation of the {@link List } interface that supports full read
 * concurrency and limits write operations to one thread at a time. <p>Whether
 * this implementation will improve performance over the use of a non
 * synchronized {@link List} implementation depends on the frequency that the
 * data is read compared to being modified, the duration of the read and write
 * operations, and the contention for the data.
 *
 * @author Mathieu Cambillau <cambillaum@gmail.com>
 */
public class ReadWriteList<E extends Object> implements List<E>, Serializable {

    private List<E> list;
    private ReadWriteLock readWriteLock;
    private Lock readLock;
    private Lock writeLock;

    /**
     * Constructs a new empty ReadWriteList backed by the specified {@link List
     * }.
     *
     * @param list the {@link List } that backs this ReadWriteList.
     */
    public ReadWriteList(List<E> list) {
        this(list, new ReentrantReadWriteLock());
    }

    /**
     * Constructs a new empty ReadWriteList backed by the specified {@link List
     * }
     * with the given fairness policy.
     *
     * @param list the {@link List} that backs this ReadWriteList.
     * @param fair true if the list's lock should use a fair ordering policy.
     */
    public ReadWriteList(List<E> list, boolean fair) {
        this(list, new ReentrantReadWriteLock(fair));
    }

    /**
     * Constructs a new empty ReadWriteList backed by the specified {@link List
     * }
     * and using the given {@link ReadWriteLock } implementation.
     *
     * @param list the {@link List } that backs this ReadWriteList.
     * @param readWriteLock the {@link ReadWriteLock } implementation used to
     * enforce the locking policy.
     */
    ReadWriteList(List<E> list, ReadWriteLock readWriteLock) {
        this.list = list;
        this.readWriteLock = readWriteLock;
        this.readLock = readWriteLock.readLock();
        this.writeLock = readWriteLock.writeLock();

        if (this.list == null) {
            throw new IllegalArgumentException("list cannot be null");
        }
    }

    @Override
    public int size() {
        this.readLock.lock();
        try {
            return this.list.size();
        } finally {
            this.readLock.unlock();
        }
    }

    @Override
    public boolean isEmpty() {
        this.readLock.lock();
        try {
            return this.list.isEmpty();
        } finally {
            this.readLock.unlock();
        }
    }

    @Override
    public boolean contains(Object o) {
        this.readLock.lock();
        try {
            return this.list.contains(o);
        } finally {
            this.readLock.unlock();
        }
    }

    @Override
    public Iterator<E> iterator() {
        this.readLock.lock();
        try {
            List<E> copyList = getCopyDependingOnType();
            return copyList.iterator();
        } finally {
            this.readLock.unlock();
        }
    }

    @Override
    public Object[] toArray() {
        this.readLock.lock();
        try {
            return this.list.toArray();
        } finally {
            this.readLock.unlock();
        }
    }

    @Override
    public <T> T[] toArray(T[] ts) {
        this.readLock.lock();
        try {
            return this.list.toArray(ts);
        } finally {
            this.readLock.unlock();
        }
    }

    @Override
    public boolean add(E e) {
        this.writeLock.lock();
        try {
            return this.list.add(e);
        } finally {
            this.writeLock.unlock();
        }
    }

    @Override
    public boolean remove(Object o) {
        this.writeLock.lock();
        try {
            return this.list.remove(o);
        } finally {
            this.writeLock.unlock();
        }
    }

    @Override
    public boolean containsAll(Collection<?> clctn) {
        this.readLock.lock();
        try {
            return this.list.containsAll(clctn);
        } finally {
            this.readLock.unlock();
        }
    }

    @Override
    public boolean addAll(Collection<? extends E> clctn) {
        this.writeLock.lock();
        try {
            return this.list.addAll(clctn);
        } finally {
            this.writeLock.unlock();
        }
    }

    @Override
    public boolean addAll(int i, Collection<? extends E> clctn) {
        this.writeLock.lock();
        try {
            return this.list.addAll(i, clctn);
        } finally {
            this.writeLock.unlock();
        }
    }

    @Override
    public boolean removeAll(Collection<?> clctn) {
        this.writeLock.lock();
        try {
            return this.list.removeAll(clctn);
        } finally {
            this.writeLock.unlock();
        }
    }

    @Override
    public boolean retainAll(Collection<?> clctn) {
        this.writeLock.lock();
        try {
            return this.list.retainAll(clctn);
        } finally {
            this.writeLock.unlock();
        }
    }

    @Override
    public void clear() {
        this.writeLock.lock();
        try {
            this.list.clear();
        } finally {
            this.writeLock.unlock();
        }
    }

    @Override
    public E get(int i) {
        this.readLock.lock();
        try {
            return this.list.get(i);
        } finally {
            this.readLock.unlock();
        }
    }

    @Override
    public E set(int i, E e) {
        this.writeLock.lock();
        try {
            return this.list.set(i, e);
        } finally {
            this.writeLock.unlock();
        }
    }

    @Override
    public void add(int i, E e) {
        this.writeLock.lock();
        try {
            this.list.add(i, e);
        } finally {
            this.writeLock.unlock();
        }
    }

    @Override
    public E remove(int i) {
        this.writeLock.lock();
        try {
            return this.list.remove(i);
        } finally {
            this.writeLock.unlock();
        }
    }

    @Override
    public int indexOf(Object o) {
        this.readLock.lock();
        try {
            return this.list.indexOf(o);
        } finally {
            this.readLock.unlock();
        }
    }

    @Override
    public int lastIndexOf(Object o) {
        this.readLock.lock();
        try {
            return this.list.lastIndexOf(o);
        } finally {
            this.readLock.unlock();
        }
    }

    @Override
    public ListIterator<E> listIterator() {
        this.readLock.lock();
        try {
            List<E> copy = getCopyDependingOnType();
            return copy.listIterator();
        } finally {
            this.readLock.unlock();
        }
    }

    @Override
    public ListIterator<E> listIterator(int i) {
        this.readLock.lock();
        try {
            List<E> copy = getCopyDependingOnType();
            return copy.listIterator(i);
        } finally {
            this.readLock.unlock();
        }
    }

    @Override
    public List<E> subList(int i, int i1) {
        this.readLock.lock();
        try {
            List<E> subList = this.list.subList(i, i1);
            return new ReadWriteList<E>(subList, this.readWriteLock);
        } finally {
            this.readLock.unlock();
        }
    }

    private List<E> getCopyDependingOnType() {
        List<E> copyList;
        if (this.list instanceof LinkedList) {
            copyList = new LinkedList<E>(this.list);
        } else {
            copyList = new ArrayList<E>(this.list);
        }
        return copyList;
    }
}
