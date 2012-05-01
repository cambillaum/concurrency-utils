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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * A class that allows you to reproduce concurrency issues in unit tests. When
 * you spot a concurrency problem in your application, you can use this class to
 * reproduce it in a unit test. Once you introduce synchronization mechanisms in
 * your application to fix your program your unit test may jam in a deadlock
 * situation. You can solve this problem by using the version of the
 * waitForOtherThreads() method that accepts a timeout as an argument. Here is a
 * usage example which reproduces a racing condition:
 * <pre>
 * Account account = new Account(1000L);
 * ConcurrentTester concurrentTester = new ConcurrentTester();
 * WaitingCondition waitingCondition = new WaitingCondition(2);
 * Runnable runnable = new Runnable() {
 *            public void run() {
 *                try {
 *                    long oldBalance = account.getBalance();
 *                    concurrentTester.waitForOtherThreads(waitingCondition);
 *                    long newBalance = oldBalance + 100;
 *                    account.setBalance(newBalance);
 *                } catch (InterruptedException ex) {
 *                    throw new RuntimeException(ex);
 *                }
 *            }
 * };
 * concurrentTester.addRunnables(this.runnable, this.runnable).execute();
 * assertEquals(1100L, account.getBalance());
 * </pre>
 *
 * @author Mathieu Cambillau <cambillaum@gmail.com>
 */
public class ConcurrentTester {

    private List<Runnable> runnables = new ArrayList<Runnable>();
    private boolean executed = false;

    /**
     * Adds a {@link Runnable } to be executed by this ConcurrentTester.
     *
     * @param runnable a {@link Runnable } to be executed by this
     * ConcurrentTester.
     * @return this ConcurrentTester.
     */
    public ConcurrentTester addRunnable(Runnable runnable) {
        validateRunnable(runnable);
        validateNotExecuted();
        this.runnables.add(runnable);
        return this;
    }

    /**
     * Adds a collection of {@link Runnable } to be executed by this
     * ConcurrentTester.
     *
     * @param runnables the collection of {@link Runnable } to be executed by
     * this ConcurrentTester.
     * @return this concurrentTester.
     */
    public ConcurrentTester addRunnables(Collection<Runnable> runnables) {
        validateRunnablesCollection(runnables);
        validateNotExecuted();
        this.runnables = new ArrayList<Runnable>(runnables);
        return this;
    }

    /**
     * Adds several {@link Runnable } to be executed by this ConcurrentTester
     * using varargs.
     *
     * @param runnable the first {@link Runnable } to add.
     * @param runnables the others {@link Runnable } to add.
     * @return this ConcurrentTester.
     */
    public ConcurrentTester addRunnables(Runnable runnable, Runnable... runnables) {
        validateRunnable(runnable);
        validateRunnablesVarArgs(runnables);
        validateNotExecuted();
        this.runnables.add(runnable);
        addVarArgsRunnables(runnables);
        return this;
    }

    /**
     * Sets a {@link WaitingCondition } point where runnables invoking
     * waitForOtherThreads on the same {@link WaitingCondition }
     * have to wait for each other for at least the amount of time defined by
     * the given timeout before continuing processing.
     *
     * @param waitingCondition the waitingCondition on which the runnables have
     * to wait.
     * @param timeout the timeout.
     * @param timeUnit the timeout unit.
     */
    public void waitForOtherThreads(WaitingCondition waitingCondition, long timeout, TimeUnit timeUnit) {
        waitingCondition.waitOn(timeout, timeUnit);
    }

    /**
     * Sets a {@link WaitingCondition } point where runnables invoking
     * waitForOtherThreads on the same {@link WaitingCondition }
     * have to wait for each other before continuing processing.
     *
     * @param wc1 the waitingCondition on which the runnables have to wait.
     */
    public void waitForOtherThreads(WaitingCondition wc1) {
        wc1.waitOn();
    }

    /**
     * Executes the runnables, taking care of the waiting conditions and
     * timeouts.
     */
    public void execute() {
        validateNotExecuted();
        this.executed = true;
        List<Thread> threads = createAndStartThreads();
        joinOnThreads(threads);
    }

    private List<Thread> createAndStartThreads() {
        List<Thread> threads = new ArrayList<Thread>();
        for (Runnable runnable : runnables) {
            Thread thread = new Thread(runnable);
            threads.add(thread);
            thread.start();
        }
        return threads;
    }

    private void joinOnThreads(List<Thread> threads) {
        try {
            for (Thread thread : threads) {
                thread.join();
            }
        } catch (InterruptedException ex) {
            //Swallow InterruptedException, the user does not have acces to the
            //threads to interrupt them so the execption should not be thrown
        }
    }

    private void validateRunnable(Runnable runnable) {
        if (runnable == null) {
            throw new IllegalArgumentException("Cannot add a null Runnable");
        }
    }

    private void validateRunnablesCollection(Collection<Runnable> runnables) {
        if (runnables == null) {
            throw new IllegalArgumentException("Cannot add a null runnable collection");
        }
        for (Runnable runnable : runnables) {
            validateRunnable(runnable);
        }
    }

    private void validateRunnablesVarArgs(Runnable[] runnables) {
        if (runnables == null) {
            throw new IllegalArgumentException("Cannot add null runnables");
        }
        for (Runnable runnable : runnables) {
            validateRunnable(runnable);
        }
    }

    private void addVarArgsRunnables(Runnable[] runnables) {
        this.runnables.addAll(Arrays.asList(runnables));
    }

    private void validateNotExecuted() {
        if (this.executed) {
            throw new IllegalStateException("ConcurrentTester already executed");
        }
    }
}
