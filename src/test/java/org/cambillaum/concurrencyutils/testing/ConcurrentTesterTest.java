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
package org.cambillaum.concurrencyutils.testing;

import java.util.Arrays;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Mathieu Cambillau <cambillaum@gmail.com>
 */
public class ConcurrentTesterTest {

    private Account account;
    private ConcurrentTester concurrentTester;
    private WaitingCondition waitingCondition;
    private Runnable runnable;

    @Before
    public void setUp() {
        this.account = new Account(1000L);
        this.concurrentTester = new ConcurrentTester();
        this.waitingCondition = new WaitingCondition(2);
        this.runnable = new Runnable() {

            @Override
            public void run() {
                long oldBalance = account.getBalance();
                concurrentTester.waitForOtherThreads(waitingCondition);
                long newBalance = oldBalance + 100;
                account.setBalance(newBalance);
            }
        };
    }

    @Test
    public void testBehavior() throws InterruptedException {
        this.concurrentTester.addRunnables(this.runnable, this.runnable).execute();
        assertEquals(1100L, account.getBalance());
    }

    @Test(expected = IllegalStateException.class)
    public void testCannotExecuteTwice() throws InterruptedException {
        this.concurrentTester.addRunnables(this.runnable, this.runnable).execute();
        this.concurrentTester.execute();
    }

    @Test(expected = IllegalStateException.class)
    public void testCannotAddIfAlreadyExecuted1() throws InterruptedException {
        this.concurrentTester.addRunnables(this.runnable, this.runnable).execute();
        this.concurrentTester.addRunnable(this.runnable);
    }

    @Test(expected = IllegalStateException.class)
    public void testCannotAddIfAlreadyExecuted2() throws InterruptedException {
        this.concurrentTester.addRunnables(this.runnable, this.runnable).execute();
        this.concurrentTester.addRunnables(this.runnable, this.runnable);
    }

    @Test(expected = IllegalStateException.class)
    public void testCannotAddIfAlreadyExecuted3() throws InterruptedException {
        this.concurrentTester.addRunnables(this.runnable, this.runnable).execute();
        this.concurrentTester.addRunnables(Arrays.asList(runnable, runnable));
    }
}
