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

package de.articdive.vhatloots.configuration.loot.objects;

import de.articdive.vhatloots.events.objects.LootBundle;
import de.articdive.vhatloots.helpers.RandomHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Lukas Mansour
 */
public class LootConfiguration {
    private String name;
    private boolean autoLoot;
    private boolean global;
    private int delay;
    private LootCollection loot;
    
    private transient HashMap<String, LootCollection> lootPaths;
    
    public LootConfiguration(String name) {
        this.name = name;
        this.autoLoot = false;
        this.global = false;
        this.delay = 4096;
        this.loot = new LootCollection(name, 100);
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public boolean isAutoLoot() {
        return autoLoot;
    }
    
    public void setAutoLoot(boolean autoLoot) {
        this.autoLoot = autoLoot;
    }
    
    public boolean isGlobal() {
        return global;
    }
    
    public void setGlobal(boolean global) {
        this.global = global;
    }
    
    public int getDelay() {
        return delay;
    }
    
    public void setDelay(int delay) {
        this.delay = delay;
    }
    
    public void addCollectionPath(String path, LootCollection collection) {
        if (lootPaths == null) {
            lootPaths = new HashMap<>();
        }
        lootPaths.put(path, collection);
    }
    
    public HashMap<String, LootCollection> getLootPaths() {
        if (lootPaths == null) {
            return new HashMap<>();
        }
        return lootPaths;
    }
    
    public void setLootPaths(HashMap<String, LootCollection> lootPaths) {
        this.lootPaths = lootPaths;
    }
    
    public LootBundle generateLoot(double lootingBonus) {
        LootBundle lootBundle = new LootBundle();
        LootCollection loot = this.loot;
        boolean collectiveXP = false,
                collectiveMoney = false,
                collectiveItems = false,
                collectiveCommands = false,
                collectiveCollections = false;
        if (loot.getMaxRollAmountXP() <= 0) {
            for (XPLoot xp : loot.getXp().values()) {
                xp.generateLoot(lootBundle, lootingBonus);
            }
        } else {
            collectiveXP = true;
        }
        if (loot.getMaxRollAmountMoney() <= 0) {
            for (MoneyLoot money : loot.getMoney().values()) {
                money.generateLoot(lootBundle, lootingBonus);
            }
        } else {
            collectiveMoney = true;
        }
        if (loot.getMaxRollAmountItems() <= 0) {
            for (ItemLoot item : loot.getItems().values()) {
                item.generateLoot(lootBundle, lootingBonus);
            }
        } else {
            collectiveItems = true;
        }
        if (loot.getMaxRollAmountCommands() <= 0) {
            for (CommandLoot command : loot.getCommands().values()) {
                command.generateLoot(lootBundle, lootingBonus);
            }
        } else {
            collectiveCommands = true;
        }
        if (loot.getMaxRollAmountCollections() <= 0) {
            for (LootCollection collection : loot.getCollections().values()) {
                collection.generateLoot(lootBundle, lootingBonus);
            }
        } else {
            collectiveCollections = true;
        }
        if (collectiveXP) {
            generateLootObject(new ArrayList<>(loot.getXp().values()), loot.getMinRollAmountXP(), loot.getMaxRollAmountXP(), lootBundle, lootingBonus);
        }
        if (collectiveMoney) {
            generateLootObject(new ArrayList<>(loot.getMoney().values()), loot.getMinRollAmountMoney(), loot.getMaxRollAmountItems(), lootBundle, lootingBonus);
        }
        if (collectiveItems) {
            generateLootObject(new ArrayList<>(loot.getItems().values()), loot.getMinRollAmountItems(), loot.getMaxRollAmountItems(), lootBundle, lootingBonus);
        }
        if (collectiveCommands) {
            generateLootObject(new ArrayList<>(loot.getCommands().values()), loot.getMinRollAmountCommands(), loot.getMaxRollAmountCommands(), lootBundle, lootingBonus);
        }
        if (collectiveCollections) {
            generateLootObject(new ArrayList<>(loot.getCollections().values()), loot.getMinRollAmountCollections(), loot.getMaxRollAmountCollections(), lootBundle, lootingBonus);
        }
        return lootBundle;
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
    
    public LootCollection getLoot() {
        return loot;
    }
    
    public void setLoot(LootCollection loot) {
        this.loot = loot;
    }
}
