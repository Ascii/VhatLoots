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

import de.articdive.vhatloots.events.objects.LootBundle;
import org.bukkit.entity.Player;

public class LootHelper {
    private static void giveLootBundle(Player player, LootBundle lootBundle) {
        if (lootBundle.getMoney() > 0) {
            // TODO: Give player money.
        }
        if (lootBundle.getExp() > 0) {
            player.giveExp(lootBundle.getExp());
            // TODO: Send message.
        }
        // TODO: Give items.
    }
}
