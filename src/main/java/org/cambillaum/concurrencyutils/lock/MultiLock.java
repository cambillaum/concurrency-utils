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

/**
 * An interface providing central locking capabilities that can be shared across
 * a whole application. This interface is especially useful when you need to
 * synchronize objects and cannot modify their code. By instantiating one of this
 * interface's implementation and making it accessible through the ServletContext or
 * Spring's ApplicationContext or even through an Application EJB, any component
 * can acquire and release locks attached to specific instances of a class. Here
 * is a usage example:
 * <pre>
 * MultiLock<Integer> multiLock = new ReentrantMultiLock<Integer>();
 * multiLock.lock(1);
 * multiLock.lock(2);
 * multiLock.lock(3);
 * //Here do operations on instances of some entity object
 * //that have 1,2 and 3
 * //as id and may be concurrent to other operations on these instances
 * multiLock.unLock(3);
 * multiLock.unLock(2);
 * multiLock.unLock(1);
 * </pre>
 *
 * @author Mathieu Cambillau <cambillaum@gmail.com>
 */
public interface MultiLock<E extends Object> {

    /**
     * Try to acquire lock on e. This call blocks until the lock can be acquired.
     *
     * @param e the instance on which the locks is to be acquired.
     */
    public void lock(E e);

    /**
     * Releases the lock on e.
     *
     * @param e the instance on which the lock is to be released.
     */
    public void unLock(E e);
}
