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

package de.articdive.vhatloots.configuration.gson.objects;

import de.articdive.vhatloots.events.objects.LootBundle;
import de.articdive.vhatloots.helpers.RandomHelper;

/**
 * @author Lukas Mansour
 */
public class XPLoot extends LootObject {
    private int lowerXP;
    private int upperXP;
    
    public XPLoot(String name, double probability, int lowerXP, int upperXP) {
        super(name, probability);
        this.lowerXP = lowerXP;
        this.upperXP = upperXP;
    }
    
    public int getLowerXP() {
        return lowerXP;
    }
    
    public void setLowerXP(int lowerXP) {
        this.lowerXP = lowerXP;
    }
    
    public int getUpperXP() {
        return upperXP;
    }
    
    public void setUpperXP(int upperXP) {
        this.upperXP = upperXP;
    }
    
    @Override
    public void generateLoot(LootBundle bundle, double lootingBonus) {
        if (rollDice(lootingBonus)) {
            bundle.addExp(RandomHelper.rollForInt(lowerXP, upperXP));
        }
    }
}
