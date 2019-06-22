/*
 * VhatLoots
 * Copyright (C) 2019 Lukas Mansour
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
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package de.articdive.vhatloots.events.abstractions;

import de.articdive.vhatloots.events.objects.LootBundle;
import org.bukkit.event.Cancellable;
import org.bukkit.inventory.ItemStack;

import java.util.List;


/**
 * @author Lukas Mansour
 */
public abstract class LootEvent extends VhatLootsEvent implements Cancellable {
    protected LootBundle lootBundle;
    
    /**
     * Returns the loot that will be looted
     *
     * @return the bundle of loot that will be looted
     */
    public LootBundle getLootBundle() {
        return lootBundle;
    }
    
    /**
     * Returns the items that will be looted
     *
     * @return A list of items that will be looted
     */
    public List<ItemStack> getItemList() {
        return lootBundle.getItemList();
    }
    
    /**
     * Sets the items that will be looted
     *
     * @param items The items to be looted.
     */
    public void setItemList(List<ItemStack> items) {
        lootBundle.setItemList(items);
    }
    
    /**
     * Returns the amount of money that the Player will loot
     *
     * @return The amount of money that the Player will loot
     */
    public double getMoney() {
        return lootBundle.getMoney();
    }
    
    /**
     * Sets the amount of money to be looted by the player
     *
     * @param money The amount of money to be looted
     */
    public void setMoney(double money) {
        lootBundle.setMoney(money);
    }
    
    /**
     * Returns the amount of experience that will be looted
     *
     * @return The amount of experience looted
     */
    public int getExp() {
        return lootBundle.getExp();
    }
    
    /**
     * Sets the amount of experience to be looted
     *
     * @param exp The amount of experience to be looted
     */
    public void setExp(int exp) {
        lootBundle.setExp(exp);
    }
    
    
}
