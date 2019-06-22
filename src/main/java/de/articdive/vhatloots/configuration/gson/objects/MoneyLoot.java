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
public class MoneyLoot extends LootObject {
    private int lowerMoney;
    private int upperMoney;
    
    public MoneyLoot(String name, double probability, int lowerMoney, int upperMoney) {
        super(name, probability);
        this.lowerMoney = lowerMoney;
        this.upperMoney = upperMoney;
    }
    
    public int getLowerMoney() {
        return lowerMoney;
    }
    
    public void setLowerMoney(int lowerMoney) {
        this.lowerMoney = lowerMoney;
    }
    
    public int getUpperMoney() {
        return upperMoney;
    }
    
    public void setUpperMoney(int upperMoney) {
        this.upperMoney = upperMoney;
    }
    
    @Override
    public void generateLoot(LootBundle bundle, double lootingBonus) {
        if (rollDice(lootingBonus)) {
            bundle.addMoney(RandomHelper.rollForDouble(lowerMoney, upperMoney));
        }
        
    }
}
