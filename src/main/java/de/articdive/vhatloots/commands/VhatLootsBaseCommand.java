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

package de.articdive.vhatloots.commands;

import co.aikar.commands.BaseCommand;
import de.articdive.vhatloots.language.LanguageConfigurationNode;
import de.articdive.vhatloots.helpers.MessageHelper;
import org.bukkit.command.CommandSender;

import java.util.HashMap;

/**
 * @author Lukas Mansour
 */
class VhatLootsBaseCommand extends BaseCommand {
    static String[] formatMsg(HashMap<String, String> placeHolders, String... messages) {
        return MessageHelper.formatMsg(placeHolders, messages);
    }
    
    static String formatMsg(HashMap<String, String> placeHolders, String message) {
        return MessageHelper.formatMsg(placeHolders, message);
    }
    
    static String getMessage(CommandSender sender, LanguageConfigurationNode node) {
        return MessageHelper.getMessage(sender, node);
    }
    
}
