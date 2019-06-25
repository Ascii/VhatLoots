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

package de.articdive.vhatloots.configuration.loot;

import de.articdive.vhatloots.events.objects.LootBundle;
import de.articdive.vhatloots.helpers.RandomHelper;

/**
 * @author Lukas Mansour
 */
public abstract class LootObject {
    private transient LootConfiguration root;
    private transient LootCollection parent;
    private transient String name;
    private double probability;
    
    LootObject(String name, double probability) {
        this.name = name;
        this.probability = probability;
    }
    
    boolean rollDice(double lootingBonus) {
        return RandomHelper.rollForDouble(100) <= probability + lootingBonus;
    }
    
    public abstract void generateLoot(LootBundle bundle, double lootingBonus);
    
    public String getName() {
        return name;
    }
    
    void setName(String name) {
        this.name = name;
    }
    
    public double getProbability() {
        return probability;
    }
    
    public void setProbability(double probability) {
        this.probability = probability;
    }
    
    public LootConfiguration getRoot() {
        return root;
    }
    
    void setRoot(LootConfiguration root) {
        this.root = root;
    }
    
    public LootCollection getParent() {
        return parent;
    }
    
    void setParent(LootCollection parent) {
        this.parent = parent;
    }
}
