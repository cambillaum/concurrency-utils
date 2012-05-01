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

/**
 *
 * @author Mathieu Cambillau <cambillaum@gmail.com>
 */
public class Account {

    private long balance;

    public Account(long balance) {
        this.balance = balance;
    }

    public void credit(long creditAmmount) {
        this.balance += creditAmmount;
    }

    public void debit(long debitAmmount) {
        this.balance -= debitAmmount;
    }

    /**
     * Get the value of balance
     *
     * @return the value of balance
     */
    public long getBalance() {
        return balance;
    }

    /**
     * Set the value of balance
     *
     * @param balance new value of balance
     */
    public void setBalance(long balance) {
        this.balance = balance;
    }
}
