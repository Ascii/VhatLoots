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
 * You should have received a copyFile of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package de.articdive.vhatloots.configuration.gson.objects;

import de.articdive.vhatloots.events.objects.LootBundle;
import de.articdive.vhatloots.helpers.RandomHelper;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Lukas Mansour
 */
public class CollectionLoot extends LootObject {
    private transient String path;
    private int minRollAmountXP;
    private int maxRollAmountXP;
    private int minRollAmountMoney;
    private int maxRollAmountMoney;
    private int minRollAmountItems;
    private int maxRollAmountItems;
    private int minRollAmountCommands;
    private int maxRollAmountCommands;
    private int minRollAmountCollections;
    private int maxRollAmountCollections;
    private LinkedHashMap<String, XPLoot> xp;
    private LinkedHashMap<String, MoneyLoot> money;
    private LinkedHashMap<String, ItemLoot> items;
    private LinkedHashMap<String, CommandLoot> commands;
    private LinkedHashMap<String, CollectionLoot> collections;
    
    public CollectionLoot(String name, double probability) {
        super(name, probability);
        minRollAmountXP = 0;
        minRollAmountMoney = 0;
        minRollAmountItems = 0;
        minRollAmountCollections = 0;
        maxRollAmountXP = 0;
        maxRollAmountMoney = 0;
        maxRollAmountItems = 0;
        maxRollAmountCollections = 0;
        minRollAmountCommands = 0;
        maxRollAmountCommands = 0;
        this.xp = new LinkedHashMap<>();
        this.money = new LinkedHashMap<>();
        this.items = new LinkedHashMap<>();
        this.commands = new LinkedHashMap<>();
        this.collections = new LinkedHashMap<>();
    }
    
    @Override
    public void generateLoot(LootBundle lootBundle, double lootingBonus) {
        boolean collectiveXP = false,
                collectiveMoney = false,
                collectiveItems = false,
                collectiveCollections = false;
        if (maxRollAmountXP <= 0) {
            for (XPLoot xp : this.xp.values()) {
                xp.generateLoot(lootBundle, lootingBonus);
            }
        } else {
            collectiveXP = true;
        }
        if (maxRollAmountMoney <= 0) {
            for (MoneyLoot money : this.money.values()) {
                money.generateLoot(lootBundle, lootingBonus);
            }
        } else {
            collectiveMoney = true;
        }
        if (maxRollAmountItems <= 0) {
            for (ItemLoot item : this.items.values()) {
                item.generateLoot(lootBundle, lootingBonus);
            }
        } else {
            collectiveItems = true;
        }
        if (maxRollAmountCollections <= 0) {
            for (CollectionLoot collection : this.collections.values()) {
                collection.generateLoot(lootBundle, lootingBonus);
            }
        } else {
            collectiveCollections = true;
        }
        if (collectiveXP) {
            generateLootObject(new ArrayList<>(this.xp.values()), minRollAmountXP, maxRollAmountXP, lootBundle, lootingBonus);
        }
        if (collectiveMoney) {
            generateLootObject(new ArrayList<>(this.money.values()), minRollAmountMoney, maxRollAmountMoney, lootBundle, lootingBonus);
        }
        if (collectiveItems) {
            generateLootObject(new ArrayList<>(this.items.values()), minRollAmountItems, maxRollAmountItems, lootBundle, lootingBonus);
        }
        if (collectiveCollections) {
            generateLootObject(new ArrayList<>(this.collections.values()), minRollAmountCollections, maxRollAmountCollections, lootBundle, lootingBonus);
        }
    }
    
    private void generateLootObject(List<LootObject> objects, int min, int max, LootBundle lootBundle, double lootingBonus) {
        if (!objects.isEmpty()) {
            int numberOfRolls = min;
            if (min != max) {
                numberOfRolls = RandomHelper.rollForInt(min, max);
            }
            int numberLooted = 0;
            while (numberLooted < numberOfRolls) {
                double total = 0;
                for (LootObject lootObject : objects) {
                    total += lootObject.getProbability();
                }
                double roll = RandomHelper.rollForDouble(total) - lootingBonus;
                for (LootObject lootObject : objects) {
                    
                    roll -= lootObject.getProbability();
                    if (roll <= 0) {
                        lootObject.generateLoot(lootBundle, lootingBonus);
                        // TODO: Allow duplicates?
                        break;
                    }
                }
                numberLooted++;
            }
            
        }
    }
    
    public String getPath() {
        return path;
    }
    
    public void setPath(String path) {
        this.path = path;
    }
    
    public int getMinRollAmountXP() {
        return minRollAmountXP;
    }
    
    public void setMinRollAmountXP(int minRollAmountXP) {
        this.minRollAmountXP = minRollAmountXP;
    }
    
    public int getMinRollAmountMoney() {
        return minRollAmountMoney;
    }
    
    public void setMinRollAmountMoney(int minRollAmountMoney) {
        this.minRollAmountMoney = minRollAmountMoney;
    }
    
    public int getMinRollAmountItems() {
        return minRollAmountItems;
    }
    
    public void setMinRollAmountItems(int minRollAmountItems) {
        this.minRollAmountItems = minRollAmountItems;
    }
    
    public int getMinRollAmountCollections() {
        return minRollAmountCollections;
    }
    
    public void setMinRollAmountCollections(int minRollAmountCollections) {
        this.minRollAmountCollections = minRollAmountCollections;
    }
    
    public int getMaxRollAmountItems() {
        return maxRollAmountItems;
    }
    
    public void setMaxRollAmountItems(int maxRollAmountItems) {
        this.maxRollAmountItems = maxRollAmountItems;
    }
    
    public int getMaxRollAmountMoney() {
        return maxRollAmountMoney;
    }
    
    public void setMaxRollAmountMoney(int maxRollAmountMoney) {
        this.maxRollAmountMoney = maxRollAmountMoney;
    }
    
    public int getMaxRollAmountCollections() {
        return maxRollAmountCollections;
    }
    
    public void setMaxRollAmountCollections(int maxRollAmountCollections) {
        this.maxRollAmountCollections = maxRollAmountCollections;
    }
    
    public int getMaxRollAmountCommands() {
        return maxRollAmountCommands;
    }
    
    public void setMaxRollAmountCommands(int maxRollAmountCommands) {
        this.maxRollAmountCommands = maxRollAmountCommands;
    }
    
    public int getMinRollAmountCommands() {
        return minRollAmountCommands;
    }
    
    public void setMinRollAmountCommands(int minRollAmountCommands) {
        this.minRollAmountCommands = minRollAmountCommands;
    }
    
    public int getMaxRollAmountXP() {
        return maxRollAmountXP;
    }
    
    public void setMaxRollAmountXP(int maxRollAmountXP) {
        this.maxRollAmountXP = maxRollAmountXP;
    }
    
    public LinkedHashMap<String, XPLoot> getXp() {
        if (xp == null) {
            return new LinkedHashMap<>();
        }
        return xp;
    }
    
    public void setXp(LinkedHashMap<String, XPLoot> xp) {
        this.xp = xp;
    }
    
    public LinkedHashMap<String, MoneyLoot> getMoney() {
        if (money == null) {
            return new LinkedHashMap<>();
        }
        return money;
    }
    
    public void setMoney(LinkedHashMap<String, MoneyLoot> money) {
        this.money = money;
    }
    
    public LinkedHashMap<String, ItemLoot> getItems() {
        if (items == null) {
            return new LinkedHashMap<>();
        }
        return items;
    }
    
    public void setItems(LinkedHashMap<String, ItemLoot> items) {
        this.items = items;
    }
    
    public LinkedHashMap<String, CollectionLoot> getCollections() {
        if (collections == null) {
            return new LinkedHashMap<>();
        }
        return collections;
    }
    
    public void setCollections(LinkedHashMap<String, CollectionLoot> collections) {
        this.collections = collections;
    }
    
    public LinkedHashMap<String, CommandLoot> getCommands() {
        if (commands == null) {
            return new LinkedHashMap<>();
        }
        return commands;
    }
    
    public void setCommands(LinkedHashMap<String, CommandLoot> commands) {
        this.commands = commands;
    }
}
