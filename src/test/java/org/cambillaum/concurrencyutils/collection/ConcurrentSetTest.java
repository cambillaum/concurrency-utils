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

import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Mathieu Cambillau <cambillaum@gmail.com>
 */
public class ConcurrentSetTest {

    private Set<String> set = new ConcurrentSet<String>();

    @Test
    public void testAdd() {
        boolean returnedBoolean = this.set.add("a");
        String retrievedValue = this.set.iterator().next();
        assertEquals("a", retrievedValue);
        assertEquals(true, returnedBoolean);
    }

    @Test
    public void testAddAll() {
        boolean retunedBoolean = this.set.addAll(Arrays.asList("a", "b"));
        assertEquals(2, this.set.size());
        assertEquals(true, retunedBoolean);
    }

    @Test
    public void testClear() {
        this.set.add("a");
        assertEquals(1, this.set.size());
        this.set.clear();
        assertEquals(0, this.set.size());
    }

    @Test
    public void testContains() {
        this.set.add("a");
        boolean shouldBeTrue = this.set.contains("a");
        boolean shouldBeFalse = this.set.contains("b");
        assertTrue(shouldBeTrue);
        assertFalse(shouldBeFalse);
    }

    @Test
    public void testContainsAll() {
        this.set.add("a");
        this.set.add("b");
        boolean shouldBeTrue = this.set.containsAll(Arrays.asList("a", "b"));
        boolean shouldBeFalse = this.set.contains(Arrays.asList("b", "c"));
        assertTrue(shouldBeTrue);
        assertFalse(shouldBeFalse);
    }

    @Test
    public void testIsEmpty() {
        boolean shouldBeTrue = this.set.isEmpty();
        this.set.add("a");
        boolean shouldBeFalse = this.set.isEmpty();
        assertTrue(shouldBeTrue);
        assertFalse(shouldBeFalse);
    }

    @Test
    public void testIterator() {
        this.set.add("a");
        this.set.add("b");
        Iterator<String> iterator = this.set.iterator();
        assertTrue(iterator.hasNext());
        String shouldBeA = iterator.next();
        assertEquals("a", shouldBeA);
        assertTrue(iterator.hasNext());
        String shouldBeB = iterator.next();
        assertEquals("b", shouldBeB);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testRemove() {
        this.set.add("a");
        assertEquals(1, this.set.size());
        this.set.remove("a");
        assertEquals(0, this.set.size());
    }

    @Test
    public void testRemoveAll() {
        this.set.add("a");
        this.set.add("b");
        this.set.add("c");
        this.set.removeAll(Arrays.asList("a", "b"));
        assertEquals(1, this.set.size());
        String shouldBeC = this.set.iterator().next();
        assertEquals("c", shouldBeC);
    }

    @Test
    public void testRetainAll() {
        this.set.add("a");
        this.set.add("b");
        this.set.add("c");
        assertEquals(3, this.set.size());
        this.set.retainAll(Arrays.asList("a"));
        assertEquals(1, this.set.size());
        String shouldBeA = this.set.iterator().next();
        assertEquals("a", shouldBeA);
    }

    @Test
    public void testSize() {
        assertEquals(0, this.set.size());
        this.set.add("a");
        assertEquals(1, this.set.size());
    }

    @Test
    public void testToArray() {
        this.set.add("a");
        Object[] array = this.set.toArray();
        assertEquals(1, array.length);
        assertEquals("a", array[0]);
    }

    @Test
    public void testToArray2() {
        this.set.add("a");
        String[] stringArray = new String[1];
        String[] returnedArray = this.set.toArray(stringArray);
        assertEquals("a", stringArray[0]);
        assertArrayEquals(stringArray, returnedArray);
    }

    @Test
    public void testToArray3() {
        this.set.add("a");
        this.set.add("b");
        String[] stringArray = new String[1];
        String[] returnedArray = this.set.toArray(stringArray);
        assertNull(stringArray[0]);
        assertEquals(2, returnedArray.length);
        assertEquals("a", returnedArray[0]);
        assertEquals("b", returnedArray[1]);
    }
}
