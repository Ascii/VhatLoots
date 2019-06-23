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

package de.articdive.vhatloots.commands;

import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.MessageType;
import co.aikar.commands.PaperCommandManager;
import de.articdive.vhatloots.VhatLoots;
import de.articdive.vhatloots.configuration.loot.LootHandler;
import de.articdive.vhatloots.configuration.loot.objects.CommandLoot;
import de.articdive.vhatloots.configuration.loot.objects.ItemLoot;
import de.articdive.vhatloots.configuration.loot.objects.LootCollection;
import de.articdive.vhatloots.configuration.loot.objects.LootConfiguration;
import de.articdive.vhatloots.configuration.loot.objects.MoneyLoot;
import de.articdive.vhatloots.configuration.loot.objects.XPLoot;
import de.articdive.vhatloots.language.Language;
import de.articdive.vhatloots.language.LanguageHandler;
import de.articdive.vhatloots.objects.LootContainer;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * @author Lukas Mansour
 */
public class CommandHandler {
    private static CommandHandler instance;
    private PaperCommandManager manager;
    
    private CommandHandler() {
    }
    
    @SuppressWarnings("deprecation")
    public void register() {
        if (manager == null) {
            manager = new PaperCommandManager(VhatLoots.getPlugin(VhatLoots.class));
        }
        for (Language language : LanguageHandler.getInstance().getPlayerLanguages()) {
            try {
                switch (language) {
                    case ENGLISH: {
                        manager.getLocales().loadYamlLanguageFile(language.getLanguageFile(), Locale.ENGLISH);
                        break;
                    }
                    case GERMAN: {
                        manager.getLocales().loadYamlLanguageFile(language.getLanguageFile(), Locale.GERMAN);
                        break;
                    }
                }
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }
        }
        // Remove colours
        manager.setFormat(MessageType.ERROR, ChatColor.WHITE);
        manager.setFormat(MessageType.INFO, ChatColor.WHITE);
        manager.setFormat(MessageType.HELP, ChatColor.WHITE);
        manager.setFormat(MessageType.SYNTAX, ChatColor.WHITE);
        
        manager.enableUnstableAPI("help");
        manager.setDefaultHelpPerPage(8);
        manager.getCommandCompletions().registerCompletion("lootcontainers",
                c -> LootContainer.findAll().stream().map(model -> model.getString("name")).collect(Collectors.toList()));
        manager.getCommandCompletions().registerStaticCompletion("boolean", Arrays.asList("true", "false"));
        
        LootHandler lootHandler = LootHandler.getInstance();
        manager.getCommandCompletions().registerCompletion("loot-configurations",
                c -> lootHandler.getAll().stream().map(LootConfiguration::getName).collect(Collectors.toList()));
        manager.getCommandContexts().registerContext(LootConfiguration.class,
                c -> {
                    String arg = c.popFirstArg();
                    
                    if (!lootHandler.exists(arg)) {
                        throw new InvalidCommandArgument("Could not find a loot-configuration with that name!");
                    }
                    return lootHandler.get(arg);
                });
        manager.getCommandCompletions().registerCompletion("loot-collections",
                c -> {
                    List<String> output = new ArrayList<>();
                    lootHandler.getAll().stream().map(configuration -> configuration.getLootPaths().keySet()).forEach(output::addAll);
                    return output;
                });
        manager.getCommandContexts().registerContext(LootCollection.class,
                c -> {
                    String arg = c.popFirstArg();
                    String[] splitPath = arg.split("\\.");
                    if (splitPath.length == 1) {
                        return lootHandler.get(arg).getLoot();
                    } else {
                        return lootHandler.get(splitPath[0]).getLootPaths().get(arg);
                    }
                });
        manager.getCommandCompletions().registerCompletion("loot-xp",
                c -> {
                    LootCollection collection = c.getContextValue(LootCollection.class);
                    if (collection == null) {
                        return new ArrayList<>();
                    }
                    return collection.getXp().keySet();
                });
        manager.getCommandContexts().registerContext(XPLoot.class,
                c -> {
                    String arg = c.popFirstArg();
                    LootCollection collection = (LootCollection) c.getResolvedArg(LootCollection.class);
                    // TODO: Internationalization
                    if (collection == null) {
                        throw new InvalidCommandArgument("Could not find a collection with that name!");
                    }
                    if (!collection.getXp().containsKey(arg)) {
                        throw new InvalidCommandArgument("Could not find an xp-layout with that name in collection " + collection.getName());
                    }
                    return collection.getXp().get(arg);
                });
        manager.getCommandCompletions().registerCompletion("loot-money",
                c -> {
                    LootCollection collection = c.getContextValue(LootCollection.class);
                    if (collection == null) {
                        return new ArrayList<>();
                    }
                    return collection.getMoney().keySet();
                });
        manager.getCommandContexts().registerContext(MoneyLoot.class,
                c -> {
                    String arg = c.popFirstArg();
                    LootCollection collection = (LootCollection) c.getResolvedArg(LootCollection.class);
                    // TODO: Internationalization
                    if (collection == null) {
                        throw new InvalidCommandArgument("Could not find a collection with that name!");
                    }
                    if (!collection.getMoney().containsKey(arg)) {
                        throw new InvalidCommandArgument("Could not find a money-layout with that name in collection " + collection.getName());
                    }
                    return collection.getMoney().get(arg);
                });
        manager.getCommandCompletions().registerCompletion("loot-items",
                c -> {
                    LootCollection collection = c.getContextValue(LootCollection.class);
                    if (collection == null) {
                        return new ArrayList<>();
                    }
                    return collection.getItems().keySet();
                });
        manager.getCommandContexts().registerContext(ItemLoot.class,
                c -> {
                    String arg = c.popFirstArg();
                    LootCollection collection = (LootCollection) c.getResolvedArg(LootCollection.class);
                    // TODO: Internationalization
                    if (collection == null) {
                        throw new InvalidCommandArgument("Could not find a collection with that name!");
                    }
                    if (!collection.getItems().containsKey(arg)) {
                        throw new InvalidCommandArgument("Could not find an item-layout with that name in collection " + collection.getName());
                    }
                    return collection.getItems().get(arg);
                });
        manager.getCommandCompletions().registerCompletion("loot-commands",
                c -> {
                    LootCollection collection = c.getContextValue(LootCollection.class);
                    if (collection == null) {
                        return new ArrayList<>();
                    }
                    return collection.getCommands().keySet();
                });
        manager.getCommandContexts().registerContext(CommandLoot.class,
                c -> {
                    String arg = c.popFirstArg();
                    LootCollection collection = (LootCollection) c.getResolvedArg(LootCollection.class);
                    // TODO: Internationalization
                    if (collection == null) {
                        throw new InvalidCommandArgument("Could not find a collection with that name!");
                    }
                    if (!collection.getCommands().containsKey(arg)) {
                        throw new InvalidCommandArgument("Could not find a xp-layout with that name in collection " + collection.getName());
                    }
                    return collection.getCommands().get(arg);
                });
        manager.registerCommand(new VhatLootsCommand());
        manager.registerCommand(new LootCommand());
    }
    
    public void unregister() {
        if (manager == null) {
            return;
        }
        // Disable commands.
        manager.unregisterCommands();
    }
    
    public PaperCommandManager getManager() {
        return manager;
    }
    
    public static CommandHandler getInstance() {
        if (instance == null) {
            instance = new CommandHandler();
        }
        return instance;
    }
}
