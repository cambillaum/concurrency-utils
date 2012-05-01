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

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

/**
 *
 * @author Mathieu Cambillau <cambillaum@gmail.com>
 */
public class ReadWriteListTest {

    private ReadWriteList<String> readWriteList;
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
        this.readWriteList = new ReadWriteList<String>(new ArrayList<String>(), this.readWriteLock);
    }

    private void verifyWriteLockAndUnlockCalled() {
        verify(this.writeLock).lock();
        verify(this.writeLock).unlock();
    }

    private void verifyReadLockAndUnlockCalled() {
        verify(this.readLock).lock();
        verify(this.readLock).unlock();

    }

    @Test
    public void testAdd() {
        this.readWriteList.add(0, "a");
        verifyWriteLockAndUnlockCalled();
    }

    @Test
    public void testAdd2() {
        this.readWriteList.add("a");
        verifyWriteLockAndUnlockCalled();
    }

    @Test
    public void testAddAll() {
        this.readWriteList.addAll(0, new ArrayList<String>());
        verifyWriteLockAndUnlockCalled();
    }

    @Test
    public void testAddAll2() {
        this.readWriteList.addAll(new ArrayList<String>());
        verifyWriteLockAndUnlockCalled();
    }

    @Test
    public void testClear() {
        this.readWriteList.clear();
        verifyWriteLockAndUnlockCalled();
    }

    @Test
    public void testContains() {
        this.readWriteList.contains("a");
        verifyReadLockAndUnlockCalled();
    }

    @Test
    public void testContainsAll() {
        this.readWriteList.containsAll(new ArrayList<String>());
        verifyReadLockAndUnlockCalled();
    }

    @Test
    public void testGet() {
        this.readWriteList.add("a");
        this.readWriteList.get(0);
        verifyReadLockAndUnlockCalled();
    }

    @Test
    public void testIndexOf() {
        this.readWriteList.indexOf("a");
        verifyReadLockAndUnlockCalled();
    }

    @Test
    public void testIsEmpty() {
        this.readWriteList.isEmpty();
        verifyReadLockAndUnlockCalled();
    }

    @Test
    public void testIterator() {
        this.readWriteList.iterator();
        verifyReadLockAndUnlockCalled();
    }

    @Test
    public void testLastIndexOf() {
        this.readWriteList.lastIndexOf(0);
        verifyReadLockAndUnlockCalled();
    }

    @Test
    public void testListIterator() {
        this.readWriteList.listIterator(0);
        verifyReadLockAndUnlockCalled();
    }

    @Test
    public void testListIterator2() {
        this.readWriteList.listIterator();
        verifyReadLockAndUnlockCalled();
    }

    @Test
    public void testRemove() {
        this.readWriteList.add("a");
        reset(this.readWriteLock, this.readLock, this.writeLock);
        this.readWriteList.remove(0);
        verifyWriteLockAndUnlockCalled();
    }

    @Test
    public void testRemove2() {
        this.readWriteList.remove("a");
        verifyWriteLockAndUnlockCalled();
    }

    @Test
    public void testRemoveAll() {
        this.readWriteList.removeAll(new ArrayList<String>());
        verifyWriteLockAndUnlockCalled();
    }

    @Test
    public void testRetainAll() {
        this.readWriteList.retainAll(new ArrayList<String>());
        verifyWriteLockAndUnlockCalled();
    }

    @Test
    public void testSet() {
        this.readWriteList.add("a");
        reset(this.readWriteLock, this.readLock, this.writeLock);
        this.readWriteList.set(0, "a");
        verifyWriteLockAndUnlockCalled();
    }

    @Test
    public void testSize() {
        this.readWriteList.size();
        verifyReadLockAndUnlockCalled();
    }

    @Test
    public void testSubList() {
        this.readWriteList.subList(0, 0);
        verifyReadLockAndUnlockCalled();
    }

    @Test
    public void testToArray() {
        this.readWriteList.toArray();
        verifyReadLockAndUnlockCalled();
    }

    @Test
    public void testToArray2() {
        this.readWriteList.toArray(new String[1]);
        verifyReadLockAndUnlockCalled();
    }
}
