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

/**
 * @author Lukas Mansour
 */
public class CommandLoot extends LootObject {
    private String command;
    private boolean console;
    
    public CommandLoot(String name, double probability, String command, boolean console) {
        super(name, probability);
        this.command = command;
        this.console = console;
    }
    
    @Override
    public void generateLoot(LootBundle bundle, double lootingBonus) {
        if (rollDice(lootingBonus)) {
            if (console) {
                bundle.addCommand("console:" + command);
            } else {
                bundle.addCommand(command);
            }
        }
    }
    
    public String getCommand() {
        return command;
    }
    
    public void setCommand(String command) {
        this.command = command;
    }
    
    public boolean isConsole() {
        return console;
    }
    
    public void setConsole(boolean console) {
        this.console = console;
    }
}
