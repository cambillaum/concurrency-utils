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

import java.util.HashSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

/**
 *
 * @author Mathieu Cambillau <cambillaum@gmail.com>
 */
public class ReadWriteSetTest {

    private ReadWriteSet<String> readWriteSet;
    private ReadWriteLock readWriteLock;
    private Lock readLock;
    private Lock writeLock;

    @Before
    public void setUp() {
        this.readWriteLock = mock(ReadWriteLock.class);
        this.readLock = mock(Lock.class);
        this.writeLock = mock(Lock.class);
        when(this.readWriteLock.readLock()).thenReturn(this.readLock);
        when(this.readWriteLock.writeLock()).thenReturn(this.writeLock);
        this.readWriteSet = new ReadWriteSet<String>(new HashSet<String>(), this.readWriteLock);
    }

    public void verifyReadLockAndUnlockCalled() {
        verify(this.readLock).lock();
        verify(this.readLock).unlock();
    }

    public void verifyWriteLockAndUnlockCalled() {
        verify(this.writeLock).lock();
        verify(this.writeLock).unlock();
    }

    @Test
    public void testAdd() {
        this.readWriteSet.add("a");
        verifyWriteLockAndUnlockCalled();
    }

    @Test
    public void testAddAll() {
        this.readWriteSet.addAll(new HashSet<String>());
        verifyWriteLockAndUnlockCalled();
    }

    @Test
    public void testClear() {
        this.readWriteSet.clear();
        verifyWriteLockAndUnlockCalled();
    }

    @Test
    public void testContains() {
        this.readWriteSet.contains("a");
        verifyReadLockAndUnlockCalled();
    }

    @Test
    public void testContainsAll() {
        this.readWriteSet.containsAll(new HashSet<String>());
        verifyReadLockAndUnlockCalled();
    }

    @Test
    public void testIsEmpty() {
        this.readWriteSet.isEmpty();
        verifyReadLockAndUnlockCalled();
    }

    @Test
    public void testIterator() {
        this.readWriteSet.iterator();
        verifyReadLockAndUnlockCalled();
    }

    @Test
    public void testRemove() {
        this.readWriteSet.remove("a");
        verifyWriteLockAndUnlockCalled();
    }

    @Test
    public void testRemoveAll() {
        this.readWriteSet.removeAll(new HashSet<String>());
        verifyWriteLockAndUnlockCalled();
    }

    @Test
    public void testRetainAll() {
        this.readWriteSet.retainAll(new HashSet<String>());
        verifyWriteLockAndUnlockCalled();
    }

    @Test
    public void testSize() {
        this.readWriteSet.size();
        verifyReadLockAndUnlockCalled();
    }

    @Test
    public void testToArray() {
        this.readWriteSet.toArray();
        verifyReadLockAndUnlockCalled();
    }

    @Test
    public void testToArray2() {
        this.readWriteSet.toArray(new String[2]);
        verifyReadLockAndUnlockCalled();
    }
}
