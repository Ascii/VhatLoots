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
    
    @Subcommand("source")
    @Description("Sends you a link to the source-code of VhatLoots on GitHub")
    public void onSource(CommandSender sender) {
        sender.sendMessage(formatMsg(new HashMap<>(), "&aVhatLoots source-code:&b https://github.com/Articdive/VhatLoots"));
    }
    
    @Subcommand("libraries")
    @Description("Shows all open-source libraries used in VhatLoots.")
    public void onLibraries(CommandSender sender) {
        sender.sendMessage(formatMsg(new HashMap<>(),
                "&a(MIT License) &bDelegatingMap, Table, ACF, locales and MinecraftTimings &2(Aikar)",
                "&a(MIT License) &bSLF4J API Module &2(SLF4J project community)",
                "&a(MIT License) &bClassGraph &2(lukehutch)",
                "&a(MIT License) &bMicrosoft JDBC Driver for SQL Server &2(Microsoft)",
                "&a(LGPL v3.0) &bNightConfig &2(TheElectronWill)",
                "&a(Apache License, Version 2.0) &bJackson (with databind) &2(Jackson project community)",
                "&a(Apache License, Version 2.0) Typesafe-config &2(Lightbend)",
                "&a(Apache License, Version 2.0) &bJavaLite - ActiveJDBC ORM Framework &2(ipolevoy)",
                "&a(Apache License, Version 2.0) &bCommons Lang &2(Apache Software Foundation)",
                "&a(Apache License, Version 2.0) &bJoda-Time &2(Jodatime project community)",
                "&a(Apache License, Version 2.0) &bLiquibase &2(Datical)",
                "&a(Apache License, Version 2.0) &bExpiringMap &2(jhalterman)",
                "&a(Apache License, Version 2.0) &bApache Log4j API &2(Apache Software Foundation)",
                "&a(Apache License, Version 2.0) &bApache Log4j SLF4J Binding &2(Apache Software Foundation)",
                "&a(Apache License, Version 2.0) &bIntelliJ IDEA Annotations &2(Jetbrains)",
                "&a(Apache License, Version 2.0) &bGson and Guava &2(Google)",
                "&a(MPL 2.0 or EPL 1.0) &bH2 Database Engine &2(H2 project community)",
                "&a(BSD-2-Clause) &bPostgreSQL JDBC Driver &2(PostgreSQL project community)"
        ));
    }
}
