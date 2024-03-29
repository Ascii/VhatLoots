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
public class XPLoot extends LootObject {
    private int lowerBound;
    private int upperBound;
    
    public XPLoot(String name, double probability, int lowerXP, int upperXP) {
        super(name, probability);
        this.lowerBound = lowerXP;
        this.upperBound = upperXP;
    }
    
    public int getLowerBound() {
        return lowerBound;
    }
    
    public void setLowerBound(int lowerXP) {
        this.lowerBound = lowerXP;
    }
    
    public int getUpperBound() {
        return upperBound;
    }
    
    public void setUpperBound(int upperXP) {
        this.upperBound = upperXP;
    }
    
    @Override
    public void generateLoot(LootBundle bundle, double lootingBonus) {
        if (rollDice(lootingBonus)) {
            bundle.addExp(RandomHelper.rollForInt(lowerBound, upperBound));
        }
    }
}
