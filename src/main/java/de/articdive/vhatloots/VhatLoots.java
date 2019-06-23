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

package de.articdive.vhatloots;

import de.articdive.enum_to_configuration.EnumConfiguration;
import de.articdive.enum_to_configuration.EnumConfigurationBuilder;
import de.articdive.vhatloots.commands.CommandHandler;
import de.articdive.vhatloots.configuration.CoreConfiguration;
import de.articdive.vhatloots.configuration.loot.LootHandler;
import de.articdive.vhatloots.database.DatabaseHandler;
import de.articdive.vhatloots.helpers.LoggingHelper;
import de.articdive.vhatloots.language.LanguageConfigurationNode;
import de.articdive.vhatloots.language.LanguageHandler;
import de.articdive.vhatloots.listeners.PlayerListener;
import de.articdive.vhatloots.tasks.UpdateGlobalPlaceholders;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * @author Lukas Mansour
 */
public final class VhatLoots extends JavaPlugin {
    private final EnumConfiguration config = new EnumConfigurationBuilder(
            new File(getDataFolder() + File.separator + "config.yml"), CoreConfiguration.class).build();
    
    {
        LoggingHelper.setupLogger(this);
        CommandHandler.getInstance();
    }
    
    @Override
    public void onLoad() {
        super.onLoad();
        // Update version
        config.set(CoreConfiguration.VERSION, this.getDescription().getVersion());
        LootHandler.getInstance();
        DatabaseHandler.getInstance();
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        // Open DatabaseHandler
        DatabaseHandler.getInstance().open();
        LanguageHandler.getInstance();
        CommandHandler.getInstance().register();
        registerListeners();
        // Start the UpdateGlobalPlaceHolders runnable, which will just update our global placeholders every minute.
        new UpdateGlobalPlaceholders().runTaskTimer(this, 0, 6000L);
        getLogger().info(LanguageHandler.getInstance().getConsoleMessage(LanguageConfigurationNode.PLUGIN_ENABLED));
    }
    
    private void registerListeners() {
        new PlayerListener().register();
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        // Close DatabaseHandler
        DatabaseHandler.getInstance().close();
        CommandHandler.getInstance().unregister();
        LootHandler.getInstance().update();
        getLogger().info(LanguageHandler.getInstance().getConsoleMessage(LanguageConfigurationNode.PLUGIN_DISABLED));
    }
    
    public EnumConfiguration getConfiguration() {
        return config;
    }
}
