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

import java.util.concurrent.TimeUnit;
import org.cambillaum.concurrencyutils.testing.Account;
import org.cambillaum.concurrencyutils.testing.ConcurrentTester;
import org.cambillaum.concurrencyutils.testing.WaitingCondition;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Mathieu Cambillau <cambillaum@gmail.com>
 */
public class ReentrantMultiLockTest {

    private MultiLock<Integer> multiLock = new ReentrantMultiLock<Integer>();
    private Account account = new Account(0);

    @Test
    public void testLockAndUnlock() {
        this.multiLock.lock(1);
        this.multiLock.unLock(1);
    }

    @Test
    public void testLockMultipleAndUnlock() {
        this.multiLock.lock(1);
        this.multiLock.lock(2);
        this.multiLock.lock(3);
        this.multiLock.unLock(3);
        this.multiLock.unLock(2);
        this.multiLock.unLock(1);
    }

    @Test
    public void testLocking() throws InterruptedException {
        final ConcurrentTester concurrentTester = new ConcurrentTester();
        final WaitingCondition waitingCondition = new WaitingCondition(2);
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                try {
                    multiLock.lock(1);
                    long balance = account.getBalance();
                    concurrentTester.waitForOtherThreads(waitingCondition, 1, TimeUnit.SECONDS);
                    account.setBalance(balance + 1);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                } finally {
                    multiLock.unLock(1);
                }
            }
        };
        concurrentTester.addRunnables(runnable, runnable);
        concurrentTester.execute();
        assertEquals(2, account.getBalance());
    }

    @Test
    public void testLocking2() throws InterruptedException {
        final ConcurrentTester concurrentTester = new ConcurrentTester();
        final WaitingCondition waitingCondition = new WaitingCondition(2);
        Runnable runnable1 = new Runnable() {

            @Override
            public void run() {
                try {
                    multiLock.lock(1);
                    long balance = account.getBalance();
                    concurrentTester.waitForOtherThreads(waitingCondition);
                    account.setBalance(balance + 1);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                } finally {
                    multiLock.unLock(1);
                }
            }
        };
        Runnable runnable2 = new Runnable() {

            @Override
            public void run() {
                try {
                    multiLock.lock(2);
                    long balance = account.getBalance();
                    concurrentTester.waitForOtherThreads(waitingCondition);
                    account.setBalance(balance + 1);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                } finally {
                    multiLock.unLock(2);
                }
            }
        };
        concurrentTester.addRunnables(runnable1, runnable2);
        concurrentTester.execute();
        assertEquals(1, account.getBalance());
    }

    @Test
    public void testClean() throws InterruptedException {
        final ConcurrentTester concurrentTester = new ConcurrentTester();
        final WaitingCondition waitingCondition = new WaitingCondition(2);
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                try {
                    ((ReentrantMultiLock<Integer>) multiLock).clean();
                    multiLock.lock(1);
                    ((ReentrantMultiLock<Integer>) multiLock).clean();
                    long balance = account.getBalance();
                    ((ReentrantMultiLock<Integer>) multiLock).clean();
                    concurrentTester.waitForOtherThreads(waitingCondition, 1, TimeUnit.SECONDS);
                    ((ReentrantMultiLock<Integer>) multiLock).clean();
                    account.setBalance(balance + 1);
                    ((ReentrantMultiLock<Integer>) multiLock).clean();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                } finally {
                    multiLock.unLock(1);
                }
            }
        };
        concurrentTester.addRunnables(runnable, runnable);
        concurrentTester.execute();
        assertEquals(2, account.getBalance());
    }

    @Test
    public void testClean2() throws InterruptedException {
        final ConcurrentTester concurrentTester = new ConcurrentTester();
        final WaitingCondition waitingCondition = new WaitingCondition(2);
        Runnable runnable1 = new Runnable() {

            @Override
            public void run() {
                try {
                    ((ReentrantMultiLock<Integer>) multiLock).clean();
                    multiLock.lock(1);
                    ((ReentrantMultiLock<Integer>) multiLock).clean();
                    long balance = account.getBalance();
                    ((ReentrantMultiLock<Integer>) multiLock).clean();
                    concurrentTester.waitForOtherThreads(waitingCondition);
                    ((ReentrantMultiLock<Integer>) multiLock).clean();
                    account.setBalance(balance + 1);
                    ((ReentrantMultiLock<Integer>) multiLock).clean();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                } finally {
                    multiLock.unLock(1);
                }
            }
        };
        Runnable runnable2 = new Runnable() {

            @Override
            public void run() {
                try {
                    ((ReentrantMultiLock<Integer>) multiLock).clean();
                    multiLock.lock(2);
                    ((ReentrantMultiLock<Integer>) multiLock).clean();
                    long balance = account.getBalance();
                    ((ReentrantMultiLock<Integer>) multiLock).clean();
                    concurrentTester.waitForOtherThreads(waitingCondition);
                    ((ReentrantMultiLock<Integer>) multiLock).clean();
                    account.setBalance(balance + 1);
                    ((ReentrantMultiLock<Integer>) multiLock).clean();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                } finally {
                    multiLock.unLock(2);
                }
            }
        };
        concurrentTester.addRunnables(runnable1, runnable2);
        concurrentTester.execute();
        assertEquals(1, account.getBalance());
    }
}
