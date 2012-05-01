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
package org.cambillaum.concurrencyutils.lock;

import java.util.Map.Entry;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * An implementation of {@link MultiLock } based on {@link ReentrantLock }.
 *
 * @author Mathieu Cambillau <cambillaum@gmail.com>
 */
public class ReentrantMultiLock<E extends Object> implements MultiLock<E> {

    private Map<E, MarkedLock> locks = new HashMap<E, MarkedLock>();
    private boolean fair;

    /**
     * Construct a new ReentrantMultiLock using a non fair lock ordering policy
     * as default.
     */
    public ReentrantMultiLock() {
        this(false);
    }

    /**
     * Constructs a new ReentrantMultiLock with the given fairness policy.
     *
     * @param fair true if the locks should use a fair ordering policy.
     */
    public ReentrantMultiLock(boolean fair) {
        this.fair = fair;
    }

    @Override
    public void lock(E e) {
        if (e == null) {
            throw new IllegalArgumentException("Cannot lock on null object");
        }

        MarkedLock lock;
        synchronized (this) {
            if (!this.locks.containsKey(e)) {
                this.locks.put(e, new MarkedLock(new ReentrantLock(this.fair)));
            }
            lock = this.locks.get(e);
            lock.incrementOwnedOrWantedCount();
        }
        lock.getReentrantLock().lock();
    }

    @Override
    public void unLock(E e) {
        if (e == null) {
            throw new IllegalArgumentException("Cannot unlock on null object");
        }

        synchronized (this) {
            MarkedLock lock = this.locks.get(e);
            if (lock == null) {
                throw new IllegalArgumentException("Cannot unlock on object " + e);
            }
            lock.decrementOwnedOrWantedCount();
            lock.getReentrantLock().unlock();
        }
    }

    /**
     * Clears the internal map of locks that are not used anymore to
     * save some memory.
     */
    public void clean() {
        synchronized (this) {
            Set<E> keysToRemove = new HashSet<E>();
            for (Entry<E, MarkedLock> entry : this.locks.entrySet()) {
                MarkedLock lock = entry.getValue();
                if (lock.getOwnedOrWantedCount() == 0) {
                    keysToRemove.add(entry.getKey());
                }
            }
            this.removeAll(keysToRemove);
        }
    }

    private void removeAll(Collection<E> keysToRemove) {
        for (E e : keysToRemove) {
            this.locks.remove(e);
        }
    }

    private static class MarkedLock {

        private ReentrantLock reentrantLock;
        private volatile int ownedOrWantedCount = 0;

        public MarkedLock(ReentrantLock reentrantLock) {
            this.reentrantLock = reentrantLock;
        }

        public ReentrantLock getReentrantLock() {
            return reentrantLock;
        }

        public synchronized void incrementOwnedOrWantedCount() {
            this.ownedOrWantedCount++;
        }

        public synchronized void decrementOwnedOrWantedCount() {
            this.ownedOrWantedCount--;
        }

        public synchronized int getOwnedOrWantedCount() {
            return this.ownedOrWantedCount;
        }
    }
}
