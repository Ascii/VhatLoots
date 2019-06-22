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
import de.articdive.vhatloots.configuration.gson.LootHandler;
import de.articdive.vhatloots.configuration.gson.objects.Loot;
import de.articdive.vhatloots.configuration.gson.objects.CollectionLoot;
import de.articdive.vhatloots.configuration.gson.objects.ItemLoot;
import de.articdive.vhatloots.configuration.gson.objects.MoneyLoot;
import de.articdive.vhatloots.configuration.gson.objects.XPLoot;
import de.articdive.vhatloots.language.Language;
import de.articdive.vhatloots.language.LanguageHandler;
import de.articdive.vhatloots.objects.LootContainer;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
        // Remove colours
        manager.setFormat(MessageType.ERROR, ChatColor.WHITE);
        manager.setFormat(MessageType.INFO, ChatColor.WHITE);
        manager.setFormat(MessageType.HELP, ChatColor.WHITE);
        manager.setFormat(MessageType.SYNTAX, ChatColor.WHITE);
        
        
        LootHandler lootHandler = LootHandler.getInstance();
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
        manager.enableUnstableAPI("help");
        manager.setDefaultHelpPerPage(8);
        // Add custom completions
        manager.getCommandCompletions().registerCompletion("loot",
                c -> lootHandler.getAll().stream().map(Loot::getName).collect(Collectors.toList()));
        manager.getCommandCompletions().registerCompletion("lootcontainers",
                c -> LootContainer.findAll().stream().map(model -> model.getString("name")).collect(Collectors.toList()));
        manager.getCommandCompletions().registerStaticCompletion("boolean", Arrays.asList("true", "false"));
        
        manager.getCommandCompletions().registerCompletion("lootxp",
                c -> {
                    CollectionLoot collection = c.getContextValue(CollectionLoot.class);
                    if (collection == null) {
                        return new ArrayList<>();
                    }
                    return collection.getXp().keySet();
                });
        manager.getCommandCompletions().registerCompletion("lootxproot",
                c -> {
                    Loot loot = c.getContextValue(Loot.class);
                    if (loot == null) {
                        return new ArrayList<>();
                    }
                    return loot.getXP().keySet();
                });
        
        manager.getCommandCompletions().registerCompletion("lootmoney",
                c -> {
                    CollectionLoot collection = c.getContextValue(CollectionLoot.class);
                    if (collection == null) {
                        return new ArrayList<>();
                    }
                    return collection.getMoney().keySet();
                });
        manager.getCommandCompletions().registerCompletion("lootmoneyroot",
                c -> {
                    Loot loot = c.getContextValue(Loot.class);
                    if (loot == null) {
                        return new ArrayList<>();
                    }
                    return loot.getMoney().keySet();
                });
        
        manager.getCommandCompletions().registerCompletion("lootitems",
                c -> {
                    CollectionLoot collection = c.getContextValue(CollectionLoot.class);
                    if (collection == null) {
                        return new ArrayList<>();
                    }
                    return collection.getItems().keySet();
                });
        manager.getCommandCompletions().registerCompletion("lootitemsroot",
                c -> {
                    Loot loot = c.getContextValue(Loot.class);
                    if (loot == null) {
                        return new ArrayList<>();
                    }
                    return loot.getItems().keySet();
                });
        
        manager.getCommandCompletions().registerCompletion("lootcollections",
                c -> {
                    Loot loot = c.getContextValue(Loot.class);
                    if (loot == null) {
                        return new ArrayList<>();
                    }
                    return loot.getLootPaths().keySet();
                });
        // Add custom contexts.
        manager.getCommandContexts().registerContext(Loot.class,
                c -> {
                    String arg = c.popFirstArg();
                    
                    if (!lootHandler.exists(arg)) {
                        throw new InvalidCommandArgument("Could not find a loot-configuration with that name!");
                    }
                    return lootHandler.get(arg);
                });
        manager.getCommandContexts().registerContext(XPLoot.class,
                c -> {
                    String arg = c.popFirstArg();
                    Loot loot = (Loot) c.getResolvedArg(Loot.class);
                    CollectionLoot collection = (CollectionLoot) c.getResolvedArg(CollectionLoot.class);
                    if (collection == null && loot == null) {
                        throw new InvalidCommandArgument("Could not find a collection with that name!");
                    } else if (collection == null) {
                        if (!loot.getXP().containsKey(arg)) {
                            throw new InvalidCommandArgument("Could not find a xp-layout with that name in loot " + loot.getName());
                        }
                        return loot.getXP().get(arg);
                    }
                    if (!collection.getXp().containsKey(arg)) {
                        throw new InvalidCommandArgument("Could not find a xp-layout with that name in collection " + collection.getName());
                    }
                    return collection.getXp().get(arg);
                });
        manager.getCommandContexts().registerContext(MoneyLoot.class,
                c -> {
                    String arg = c.popFirstArg();
                    Loot loot = (Loot) c.getResolvedArg(Loot.class);
                    CollectionLoot collection = (CollectionLoot) c.getResolvedArg(CollectionLoot.class);
                    if (collection == null && loot == null) {
                        throw new InvalidCommandArgument("Could not find a collection with that name!");
                    } else if (collection == null) {
                        if (!loot.getMoney().containsKey(arg)) {
                            throw new InvalidCommandArgument("Could not find a money-layout with that name in loot " + loot.getName());
                        }
                        return loot.getMoney().get(arg);
                    }
                    if (!collection.getMoney().containsKey(arg)) {
                        throw new InvalidCommandArgument("Could not find a money-layout with that name in collection " + collection.getName());
                    }
                    return collection.getMoney().get(arg);
                });
        manager.getCommandContexts().registerContext(ItemLoot.class,
                c -> {
                    String arg = c.popFirstArg();
                    Loot loot = (Loot) c.getResolvedArg(Loot.class);
                    CollectionLoot collection = (CollectionLoot) c.getResolvedArg(CollectionLoot.class);
                    if (collection == null && loot == null) {
                        throw new InvalidCommandArgument("Could not find a collection with that name!");
                    } else if (collection == null) {
                        if (!loot.getItems().containsKey(arg)) {
                            throw new InvalidCommandArgument("Could not find a money-layout with that name in loot " + loot.getName());
                        }
                        return loot.getItems().get(arg);
                    }
                    if (!collection.getItems().containsKey(arg)) {
                        throw new InvalidCommandArgument("Could not find a money-layout with that name in collection " + collection.getName());
                    }
                    return collection.getItems().get(arg);
                });
        manager.getCommandContexts().registerContext(CollectionLoot.class,
                c -> {
                    String arg = c.popFirstArg();
                    Loot loot = (Loot) c.getResolvedArg(Loot.class);
                    if (loot == null) {
                        throw new InvalidCommandArgument("Could not find a loot-configuration with that name!");
                    }
                    if (!loot.getLootPaths().containsKey(arg)) {
                        throw new InvalidCommandArgument("Could not find a collection-layout with that name in " + loot.getName());
                    }
                    return loot.getLootPaths().get(arg);
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
