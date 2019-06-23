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

package de.articdive.vhatloots.listeners;

import de.articdive.vhatloots.VhatLoots;
import de.articdive.vhatloots.language.LanguageConfigurationNode;
import de.articdive.vhatloots.helpers.MessageHelper;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;

import java.util.HashMap;

/**
 * @author Lukas Mansour
 */
public abstract class BaseListener implements Listener {
    private final VhatLoots main = VhatLoots.getPlugin(VhatLoots.class);
    
    BaseListener() {
    }
    
    public void register() {
        main.getServer().getPluginManager().registerEvents(this, main);
    }
    
    
    static String formatMsg(HashMap<String, Object> placeholders, String message) {
        return MessageHelper.formatMsg(placeholders, message);
    }
    
    static String getMessage(CommandSender sender, LanguageConfigurationNode node) {
        return MessageHelper.getMessage(sender, node);
    }
}
