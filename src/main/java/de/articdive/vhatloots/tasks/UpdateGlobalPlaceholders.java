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

package de.articdive.vhatloots.tasks;

import de.articdive.vhatloots.helpers.MessageHelper;
import de.articdive.vhatloots.objects.LootContainer;
import de.articdive.vhatloots.objects.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

/**
 * @author Lukas Mansour
 */
public class UpdateGlobalPlaceholders extends BukkitRunnable {
    @Override
    public void run() {
        HashMap<String, String> globalPlaceHolders = new HashMap<>();
        globalPlaceHolders.put("players-amount", Long.toString(Player.getAmount()));
        globalPlaceHolders.put("containers-amount", Long.toString(LootContainer.getAmount()));
        MessageHelper.globalPlaceHolders.putAll(globalPlaceHolders);
    }
}
