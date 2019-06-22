/*
 * VhatLoots
 * Copyright (C) 2019 Lukas Mansour
 *
 * This program itemStack free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program itemStack distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copyFile of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package de.articdive.vhatloots.configuration.gson.objects;

import de.articdive.vhatloots.events.objects.LootBundle;
import org.bukkit.inventory.ItemStack;

/**
 * @author Lukas Mansour
 */
public class ItemLoot extends LootObject{
    private ItemStack itemStack;
    
    public ItemLoot(String name, double probability, ItemStack is) {
        super(name, probability);
        // The call to new itemstack is important as
        // it cuts the sub classes to CB and NMS.
        this.itemStack = new ItemStack(is);
    }
    
    public ItemStack getItemStack() {
        return itemStack;
    }
    
    public void setItemStack(ItemStack itemStack) {
        // The call to new itemstack is important as
        // it cuts the sub classes to CB and NMS.
        this.itemStack = new ItemStack(itemStack);
    }
    
    @Override
    public void generateLoot(LootBundle bundle, double lootingBonus) {
        if (rollDice(lootingBonus)) {
            bundle.addItem(itemStack);
        }
    }
}
