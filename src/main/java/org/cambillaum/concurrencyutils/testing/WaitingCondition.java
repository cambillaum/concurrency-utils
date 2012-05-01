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

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * A condition on which several runnables can wait for each other.
 *
 * @author Mathieu Cambillau <cambillaum@gmail.com>
 */
public class WaitingCondition {

    private CountDownLatch countDownLatch;

    /**
     * Constructs a WaitingCondition that will make calling threads wait until
     * the given number of calls is reached.
     *
     * @param numberOfThreadsToWaitFor the number of threads that have to call
     * before they can keep going.
     */
    public WaitingCondition(int numberOfThreadsToWaitFor) {
        this.countDownLatch = new CountDownLatch(numberOfThreadsToWaitFor);
    }

    void waitOn(long timeout, TimeUnit timeUnit) {
        try {
            this.countDownLatch.countDown();
            this.countDownLatch.await(timeout, timeUnit);
        } catch (InterruptedException ex) {
            //Swallow InterruptedException, the user does not have acces to the
            //threads to interrupt them so the execption should not be thrown
        }
    }

    void waitOn() {
        try {
            this.countDownLatch.countDown();
            this.countDownLatch.await();
        } catch (InterruptedException ex) {
            //Swallow InterruptedException, the user does not have acces to the
            //threads to interrupt them so the execption should not be thrown
        }
    }
}
