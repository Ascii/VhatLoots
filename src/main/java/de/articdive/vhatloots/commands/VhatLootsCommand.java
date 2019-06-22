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

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import de.articdive.vhatloots.VhatLoots;
import org.bukkit.command.CommandSender;

import java.util.HashMap;

/**
 * @author Lukas Mansour
 */
@CommandAlias("vhat|vhatloots")
@Description("Main command for VhatLoots.")
public class VhatLootsCommand extends VhatLootsBaseCommand {
    private final VhatLoots main = VhatLoots.getPlugin(VhatLoots.class);
    
    @Subcommand("information|info|version|ver")
    @CommandPermission("vhatloots.information")
    @Description("Shows information about VhatLoots.")
    public void onInformation(CommandSender sender) {
        sender.sendMessage(formatMsg(new HashMap<>(),
                "&a#&a#&a#&a#&a#&a#&a# &c----&7[ &2Vhat&aLoots &7]&c----",
                "&a#&c#&a#&a#&a#&c#&a# &eCreated by &9Articdive",
                "&a#&c#&a#&a#&a#&c#&a# &eVersion: &2" + main.getDescription().getVersion(),
                "&a#&c#&a#&a#&a#&c#&a# &ePlayers: &2 {players-amount}",
                "&a#&a#&c#&a#&c#&a#&a# &eContainers: &2{containers-amount}",
                "&a#&a#&a#&c#&a#&a#&a#",
                "&a#&a#&a#&a#&a#&a#&a#"
        ));
    }
}
