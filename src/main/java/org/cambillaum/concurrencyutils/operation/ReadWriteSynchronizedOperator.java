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
package org.cambillaum.concurrencyutils.operation;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * A class that can be extended by or can be used as a component of any class
 * that needs a read/write concurrency mechanism. Contrary to the java provided
 * synchronized mechanism, using this class allows an unlimited number of
 * concurrent reading threads which can highly improve performance of heavily
 * concurrent applications. To ensure data consistency, only one thread can perform write operations at a
 * time. Here is an example of how you can make a
 * class thread safe in a more flexible and efficient way than using the java
 * synchronized mechanism:
 * 
 * <pre>
 * public class ConcurrentClass {
 *
 *   private ReadWriteSynchronizedOperator readWriteSynchronizedOperator = new ReadWriteSynchronizedOperator();
 *
 *    public String yourReadOperation() {
 *        String result = this.readWriteSynchronizedOperator.doRead(new SynchronizedOperationWithResult&lt;String&gt;() {
 *           &#64;Override
 *           public String doOperation() {
 *               &#47;&#47;Do your read operation instead
 *               String result = "readString";
 *               return result;
 *           }
 *        });
 *        return result;
 *    }
 *
 *    public String yourWriteOperation() {
 *        String result = this.readWriteSynchronizedOperator.doWrite(new SynchronizedOperationWithResult@lt;String&gt;() {
 *            &#64;Override
 *            public String doOperation() {
 *                &#47;&#47;Do you write operation instead
 *                String result = "writtenString";
 *                return result;
 *            }
 *        });
 *        return result;
 *    }
 * }
 * </pre>
 *
 * @author Mathieu Cambillau <cambillaum@gmail.com>
 */
public class ReadWriteSynchronizedOperator {

    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private Lock readLock = this.readWriteLock.readLock();
    private Lock writeLock = this.readWriteLock.writeLock();

    /**
     * Performs a read operation wrapped in a read lock transaction and return a
     * result.
     *
     * @param <T> the type of the result to be returned.
     * @param op the operation that has to be executed.
     * @return the result of op.doOperation().
     */
    public <T> T doRead(SynchronizedOperationWithResult<T> op) {
        this.readLock.lock();
        try {
            T result = op.doOperation();
            return result;
        } finally {
            this.readLock.unlock();
        }
    }

    /**
     * Performs a write operation wrapped in a write transaction and return a
     * result.
     *
     * @param <T> the type of the result to be returned.
     * @param op the operation that has to be executed.
     * @return the result of op.doOperation().
     */
    public <T> T doWrite(SynchronizedOperationWithResult<T> op) {
        this.writeLock.lock();
        try {
            T result = op.doOperation();
            return result;
        } finally {
            this.writeLock.unlock();
        }
    }

    /**
     * Performs a read operation wrapped in a read transaction without returning
     * any result.
     *
     * @param op the operation that has to be executed
     */
    public void doRead(SynchronizedOperationWithoutResult op) {
        this.readLock.lock();
        try {
            op.doOperation();
        } finally {
            this.readLock.unlock();
        }
    }

    /**
     * Performs a write operation wrapped in a write transaction without
     * returning any result.
     *
     * @param op the operation that has to be executed
     */
    public void doWrite(SynchronizedOperationWithoutResult op) {
        this.writeLock.lock();
        try {
            op.doOperation();
        } finally {
            this.writeLock.unlock();
        }
    }
}
