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

package de.articdive.vhatloots.helpers;

import de.articdive.vhatloots.configuration.loot.LootConfiguration;
import de.articdive.vhatloots.events.objects.LootBundle;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class LootHelper {
    public static void giveLootBundle(Player player, LootConfiguration lootConfiguration, LootBundle lootBundle) {
        if (lootBundle.getMoney() > 0) {
            // TODO: Give player money.
            // TODO: Send message.
        }
        if (lootBundle.getExp() > 0) {
            player.giveExp(lootBundle.getExp());
            // TODO: Send message.
        }
        for (String command : lootBundle.getCommandList()) {
            if (command.startsWith("console:")) {
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
            } else {
                player.performCommand(command);
            }
            // TODO: Send message.
        }
        if (lootConfiguration.isAutoLoot()) {
            // TODO: Put directly into his inventory
        } else {
            // TODO: Open inventory
        }
    }
}
