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

package de.articdive.vhatloots.events.objects;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lukas Mansour
 */
public class LootBundle {
    private List<ItemStack> itemList = new ArrayList<>();
    private List<String> commandList = new ArrayList<>();
    private double money = 0.0;
    private int exp = 0;
    
    public LootBundle() {
    }
    
    public double getMoney() {
        return money;
    }
    
    public void setMoney(double money) {
        this.money = money;
    }
    
    public void addMoney(double money) {
        this.money += money;
    }
    
    public int getExp() {
        return exp;
    }
    
    public void setExp(int exp) {
        this.exp = exp;
    }
    
    public void addExp(int exp) {
        this.exp += exp;
    }
    
    public List<ItemStack> getItemList() {
        return itemList;
    }
    
    public void setItemList(List<ItemStack> itemList) {
        this.itemList = itemList;
    }
    
    public void addItem(ItemStack itemStack) {
        itemList.add(itemStack);
    }
    
    public void removeItem(ItemStack itemStack) {
        itemList.remove(itemStack);
    }
    
    public void addCommand(String command) {
        if (command == null || command.isEmpty()) {
            return;
        }
        if (command.startsWith("/")) {
            command = command.substring(1);
        }
        commandList.add(command);
    }
    
    public List<String> getCommandList() {
        return commandList;
    }
    
    public void setCommandList(List<String> commandList) {
        this.commandList = commandList;
    }
}
