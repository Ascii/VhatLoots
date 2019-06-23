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

import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.HelpCommand;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import de.articdive.vhatloots.configuration.loot.LootHandler;
import de.articdive.vhatloots.configuration.loot.objects.ItemLoot;
import de.articdive.vhatloots.configuration.loot.objects.LootCollection;
import de.articdive.vhatloots.configuration.loot.objects.LootConfiguration;
import de.articdive.vhatloots.configuration.loot.objects.MoneyLoot;
import de.articdive.vhatloots.configuration.loot.objects.XPLoot;
import de.articdive.vhatloots.helpers.MessageHelper;
import de.articdive.vhatloots.language.LanguageConfigurationNode;
import de.articdive.vhatloots.objects.LootContainer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Container;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

/**
 * @author Lukas Mansour
 */
@CommandAlias("loot|vhatloot")
@Description("Command for managing loot containers.")
public class LootCommand extends VhatLootsBaseCommand {
    private final LootHandler lootHandler = LootHandler.getInstance();
    
    @Subcommand("create|make")
    @Syntax("<lootName>")
    @CommandPermission("vhatloots.create")
    public void onCreate(CommandSender sender, String lootName) {
        HashMap<String, Object> localPlaceHolders = new HashMap<>();
        localPlaceHolders.put("lootName", lootName);
        if (lootHandler.exists(lootName)) {
            sender.sendMessage(formatMsg(localPlaceHolders, getMessage(sender, LanguageConfigurationNode.LOOT_EXISTS)));
            return;
        }
        lootHandler.addLootConfiguration(new LootConfiguration(lootName));
        lootHandler.update(true);
        sender.sendMessage(formatMsg(localPlaceHolders, getMessage(sender, LanguageConfigurationNode.LOOT_CREATED)));
    }
    
    @Subcommand("delete|remove")
    @Syntax("<lootName>")
    @CommandPermission("vhatloots.delete")
    @CommandCompletion("@loot-configurations ")
    public void onDelete(CommandSender sender, LootConfiguration lootConfiguration) {
        HashMap<String, Object> localPlaceHolders = new HashMap<>();
        localPlaceHolders.put("lootName", lootConfiguration.getName());
        lootHandler.removeLootConfiguration(lootConfiguration);
        lootHandler.update(true);
        sender.sendMessage(formatMsg(localPlaceHolders, getMessage(sender, LanguageConfigurationNode.LOOT_DELETED)));
    }
    
    @Subcommand("link")
    @Syntax("<lootName> <containerName>")
    @CommandPermission("vhatloots.link")
    @CommandCompletion("@loot-configurations *")
    public void onLink(Player player, LootConfiguration lootConfiguration, String containerName) {
        HashMap<String, Object> localPlaceHolders = new HashMap<>();
        localPlaceHolders.put("lootName", lootConfiguration.getName());
        localPlaceHolders.put("containerName", containerName);
        
        if (LootContainer.exists(containerName)) {
            player.sendMessage(formatMsg(localPlaceHolders, getMessage(player, LanguageConfigurationNode.LOOTBOX_EXISTS)));
            return;
        }
        
        RayTraceResult result = player.rayTraceBlocks(20.0);
        if (result == null || result.getHitBlock() == null) {
            player.sendMessage(formatMsg(localPlaceHolders, getMessage(player, LanguageConfigurationNode.LOOT_INVALID_BLOCK)));
        } else {
            Block b = result.getHitBlock();
            BlockState bs = b.getState();
            if (bs instanceof Container) {
                if (LootContainer.add(UUID.randomUUID(), containerName, b.getWorld().getUID(), b.getX(), b.getY(), b.getZ(), lootConfiguration.getName())) {
                    player.sendMessage(formatMsg(localPlaceHolders, getMessage(player, LanguageConfigurationNode.LOOTBOX_CREATED)));
                } else {
                    player.sendMessage(formatMsg(localPlaceHolders, getMessage(player, LanguageConfigurationNode.LOOT_FAILED_LINK)));
                }
            } else {
                player.sendMessage(formatMsg(localPlaceHolders, getMessage(player, LanguageConfigurationNode.LOOT_INVALID_BLOCK)));
            }
        }
    }
    
    @Subcommand("list")
    @Syntax("<page>")
    @CommandPermission("vhatloots.list")
    public void onList(CommandSender sender, @Default("1") int page) {
        if (page == 0) {
            page = 1;
        }
        List<LootConfiguration> lootConfiguration = lootHandler.getAll();
        int maxPage = (int) Math.ceil(lootConfiguration.size() / 8.0);
        if (maxPage == 0) {
            maxPage = 1;
        }
        HashMap<String, Object> localPlaceHolders = new HashMap<>();
        localPlaceHolders.put("page", page);
        localPlaceHolders.put("maxPage", maxPage);
        List<String> msg = new ArrayList<>();
        msg.add(formatMsg(localPlaceHolders, getMessage(sender, LanguageConfigurationNode.LOOT_HEADER)));
        for (int i = (page - 1) * 8; i < lootConfiguration.size(); i++) {
            if (i > page * 8 - 1) {
                break;
            }
            LootConfiguration lut = lootConfiguration.get(i);
            localPlaceHolders.put("lootName", lut.getName());
            localPlaceHolders.put("autoLoot", lut.isAutoLoot());
            localPlaceHolders.put("global", lut.isGlobal());
            localPlaceHolders.put("delay", lut.getDelay());
            msg.add(formatMsg(localPlaceHolders, getMessage(sender, LanguageConfigurationNode.LOOT_ELEMENT)));
        }
        msg.add(formatMsg(localPlaceHolders, getMessage(sender, LanguageConfigurationNode.LOOT_FOOTER)));
        sender.sendMessage(msg.toArray(new String[0]));
    }
    
    @HelpCommand
    @Subcommand("help|?")
    @Syntax("<page>")
    public void onHelp(CommandSender sender, CommandHelp help) {
        help.showHelp();
    }
    
    @Subcommand("set")
    @CommandPermission("vhatloots.create")
    public class Set extends VhatLootsBaseCommand {
        
        @Subcommand("global")
        @Syntax("<lootName> <global>")
        @CommandCompletion("@loot-configurations @boolean")
        public void onSetGlobal(CommandSender sender, LootConfiguration lootConfiguration, boolean value) {
            HashMap<String, Object> localPlaceHolders = new HashMap<>();
            localPlaceHolders.put("lootName", lootConfiguration.getName());
            localPlaceHolders.put("global", value);
            
            lootConfiguration.setGlobal(value);
            lootHandler.update();
            sender.sendMessage(formatMsg(localPlaceHolders, getMessage(sender, LanguageConfigurationNode.LOOT_SET_GLOBAL)));
        }
        
        @Subcommand("autoloot")
        @Syntax("<lootName> <autoLoot>")
        @CommandCompletion("@loot-configurations @boolean")
        public void onSetAutoLoot(CommandSender sender, LootConfiguration lootConfiguration, boolean value) {
            HashMap<String, Object> localPlaceHolders = new HashMap<>();
            localPlaceHolders.put("lootName", lootConfiguration.getName());
            localPlaceHolders.put("autoLoot", value);
            
            lootConfiguration.setAutoLoot(value);
            lootHandler.update();
            sender.sendMessage(formatMsg(localPlaceHolders, getMessage(sender, LanguageConfigurationNode.LOOT_SET_AUTOLOOT)));
        }
        
        @Subcommand("delay")
        @Syntax("<lootName> <delay>")
        @CommandCompletion("@loot-configurations *")
        public void onSetDelay(CommandSender sender, LootConfiguration lootConfiguration, int delay) {
            HashMap<String, Object> localPlaceHolders = new HashMap<>();
            localPlaceHolders.put("lootName", lootConfiguration.getName());
            localPlaceHolders.put("delay", delay);
            
            lootConfiguration.setDelay(delay);
            lootHandler.update();
            sender.sendMessage(formatMsg(localPlaceHolders, getMessage(sender, LanguageConfigurationNode.LOOT_SET_DELAY)));
        }
    }
    
    @Subcommand("xp")
    @CommandPermission("vhatloots.create")
    public class Xp extends VhatLootsBaseCommand {
        @Subcommand("add")
        @Syntax("<collectionName> <xpName> <low> <high> <probability>")
        @CommandCompletion("@loot-collections * * * *")
        public void onXPAdd(CommandSender sender, LootCollection lootCollection, String xpName, int low, int high, double probability) {
            if (low > high) {
                int j = low;
                low = high;
                high = j;
            }
            HashMap<String, Object> localPlaceHolders = new HashMap<>();
            localPlaceHolders.put("collectionName", lootCollection.getName());
            localPlaceHolders.put("xpName", xpName);
            localPlaceHolders.put("probability", probability);
            localPlaceHolders.put("range", low + " - " + high);
            
            LinkedHashMap<String, XPLoot> xp = lootCollection.getXp();
            if (xp.containsKey(xpName)) {
                sender.sendMessage(formatMsg(localPlaceHolders, getMessage(sender, LanguageConfigurationNode.LOOT_XP_EXISTS)));
                return;
            }
            xp.put(xpName, new XPLoot(xpName, probability, low, high));
            lootCollection.setXp(xp);
            lootHandler.update();
            sender.sendMessage(formatMsg(localPlaceHolders, getMessage(sender, LanguageConfigurationNode.LOOT_XP_ADDED)));
        }
        
        @Subcommand("edit")
        @Syntax("<collectionName> <xpName> <low> <high> <probability>")
        @CommandCompletion("@loot-collections @loot-xp * * *")
        public void onXPEdit(CommandSender sender, LootCollection lootCollection, XPLoot xp, int low, int high, @Optional double probability) {
            if (low > high) {
                int j = low;
                low = high;
                high = j;
            }
            HashMap<String, Object> localPlaceHolders = new HashMap<>();
            localPlaceHolders.put("collectionName", lootCollection.getName());
            localPlaceHolders.put("xpName", xp.getName());
            localPlaceHolders.put("probability", probability);
            localPlaceHolders.put("range", low + " - " + high);
            
            xp.setProbability(probability);
            xp.setLowerXP(low);
            xp.setUpperXP(high);
            lootHandler.update();
            sender.sendMessage(formatMsg(localPlaceHolders, getMessage(sender, LanguageConfigurationNode.LOOT_XP_EDITED)));
        }
        
        @Subcommand("remove")
        @Syntax("<lootName> <xpName> ")
        @CommandCompletion("@loot-collections @loot-xp")
        public void onXPRemove(CommandSender sender, LootCollection lootCollection, XPLoot xp) {
            HashMap<String, Object> localPlaceHolders = new HashMap<>();
            localPlaceHolders.put("xpName", xp.getName());
            localPlaceHolders.put("collectionName", lootCollection.getName());
            
            lootCollection.getXp().remove(xp.getName());
            lootHandler.update();
            sender.sendMessage(formatMsg(localPlaceHolders, getMessage(sender, LanguageConfigurationNode.LOOT_XP_REMOVED)));
        }
        
        @Subcommand("list")
        @Syntax("<lootName> <page>")
        @CommandCompletion("@loot-collections *")
        public void onXPList(CommandSender sender, LootCollection lootCollection, @Default("1") int page) {
            if (page == 0) {
                page = 1;
            }
            LinkedHashMap<String, XPLoot> xpmap = lootCollection.getXp();
            int maxPage = (int) Math.ceil(xpmap.size() / 8.0);
            if (maxPage == 0) {
                maxPage = 1;
            }
            HashMap<String, Object> localPlaceHolders = new HashMap<>();
            localPlaceHolders.put("page", page);
            localPlaceHolders.put("collectionName", lootCollection.getName());
            localPlaceHolders.put("maxPage", maxPage);
            
            List<XPLoot> vals = new ArrayList<>(xpmap.values());
            
            List<String> msg = new ArrayList<>();
            msg.add(formatMsg(localPlaceHolders, getMessage(sender, LanguageConfigurationNode.LOOT_XP_HEADER)));
            for (int i = (page - 1) * 8; i < vals.size(); i++) {
                if (i > page * 8 - 1) {
                    break;
                }
                XPLoot val = vals.get(i);
                localPlaceHolders.put("xpName", val.getName());
                localPlaceHolders.put("probability", val.getProbability());
                localPlaceHolders.put("range", val.getLowerXP() + " - " + val.getUpperXP());
                msg.add(formatMsg(localPlaceHolders, getMessage(sender, LanguageConfigurationNode.LOOT_XP_ELEMENT)));
            }
            msg.add(formatMsg(localPlaceHolders, getMessage(sender, LanguageConfigurationNode.LOOT_XP_FOOTER)));
            sender.sendMessage(msg.toArray(new String[0]));
        }
    }
    
    @Subcommand("money")
    @CommandPermission("vhatloots.create")
    public class Money extends VhatLootsBaseCommand {
        
        @Subcommand("add")
        @Syntax("<lootName> <moneyName> <low> <high> <probability>")
        @CommandCompletion("@loot-collections * * * *")
        public void onMoneyAdd(CommandSender sender, LootCollection lootCollection, String moneyName, int low, int high, double probability) {
            if (low > high) {
                int j = low;
                low = high;
                high = j;
            }
            HashMap<String, Object> localPlaceHolders = new HashMap<>();
            localPlaceHolders.put("lootName", lootCollection.getName());
            localPlaceHolders.put("moneyName", moneyName);
            localPlaceHolders.put("collectionName", lootCollection.getName());
            localPlaceHolders.put("probability", probability);
            localPlaceHolders.put("range", low + " - " + high);
            
            LinkedHashMap<String, MoneyLoot> money = lootCollection.getMoney();
            if (money.containsKey(moneyName)) {
                sender.sendMessage(formatMsg(localPlaceHolders, getMessage(sender, LanguageConfigurationNode.LOOT_MONEY_EXISTS)));
                return;
            }
            money.put(moneyName, new MoneyLoot(moneyName, probability, low, high));
            lootCollection.setMoney(money);
            lootHandler.update();
            sender.sendMessage(formatMsg(localPlaceHolders, getMessage(sender, LanguageConfigurationNode.LOOT_MONEY_ADDED)));
        }
        
        @Subcommand("edit")
        @Syntax("<lootName> <moneyName> <low> <high> <probability>")
        @CommandCompletion("@loot-collections @loot-money * * *")
        public void onMoneyEdit(CommandSender sender, LootCollection lootCollection, MoneyLoot money, int low, int high, @Optional double probability) {
            HashMap<String, Object> localPlaceHolders = new HashMap<>();
            if (low > high) {
                int j = low;
                low = high;
                high = j;
            }
            localPlaceHolders.put("collectionName", lootCollection.getName());
            localPlaceHolders.put("moneyName", money.getName());
            localPlaceHolders.put("probability", probability);
            localPlaceHolders.put("range", low + " - " + high);
            money.setProbability(probability);
            money.setLowerMoney(low);
            money.setUpperMoney(high);
            lootHandler.update();
            sender.sendMessage(formatMsg(localPlaceHolders, getMessage(sender, LanguageConfigurationNode.LOOT_MONEY_EDITED)));
        }
        
        @Subcommand("remove")
        @Syntax("<lootName> <moneyName>")
        @CommandCompletion("@loot-collections @loot-money")
        public void onMoneyRemove(CommandSender sender, LootCollection lootCollection, MoneyLoot money) {
            HashMap<String, Object> localPlaceHolders = new HashMap<>();
            localPlaceHolders.put("collectionName", lootCollection.getName());
            localPlaceHolders.put("moneyName", money.getName());
            lootCollection.getMoney().remove(money.getName());
            lootHandler.update();
            sender.sendMessage(formatMsg(localPlaceHolders, getMessage(sender, LanguageConfigurationNode.LOOT_MONEY_REMOVED)));
        }
        
        @Subcommand("list")
        @Syntax("<lootName> <page>")
        @CommandCompletion("@loot-collections *")
        public void onMoneyList(CommandSender sender, LootCollection lootCollection, @Default("1") int page) {
            if (page == 0) {
                page = 1;
            }
            LinkedHashMap<String, MoneyLoot> moneymap = lootCollection.getMoney();
            int maxPage = (int) Math.ceil(moneymap.size() / 8.0);
            if (maxPage == 0) {
                maxPage = 1;
            }
            HashMap<String, Object> localPlaceHolders = new HashMap<>();
            localPlaceHolders.put("page", page);
            localPlaceHolders.put("lootName", lootCollection.getName());
            localPlaceHolders.put("collectionName", lootCollection.getName());
            localPlaceHolders.put("maxPage", maxPage);
            
            List<MoneyLoot> vals = new ArrayList<>(moneymap.values());
            
            List<String> msg = new ArrayList<>();
            msg.add(formatMsg(localPlaceHolders, getMessage(sender, LanguageConfigurationNode.LOOT_MONEY_HEADER)));
            for (int i = (page - 1) * 8; i < vals.size(); i++) {
                if (i > page * 8 - 1) {
                    break;
                }
                MoneyLoot val = vals.get(i);
                localPlaceHolders.put("moneyName", val.getName());
                localPlaceHolders.put("probability", val.getProbability());
                localPlaceHolders.put("range", val.getLowerMoney() + " - " + val.getUpperMoney());
                msg.add(formatMsg(localPlaceHolders, getMessage(sender, LanguageConfigurationNode.LOOT_MONEY_ELEMENT)));
            }
            msg.add(formatMsg(localPlaceHolders, getMessage(sender, LanguageConfigurationNode.LOOT_MONEY_FOOTER)));
            sender.sendMessage(msg.toArray(new String[0]));
        }
    }
    
    @Subcommand("item")
    @CommandPermission("vhatloots.create")
    public class Item extends VhatLootsBaseCommand {
        
        @Subcommand("add")
        @Syntax("<lootName> <itemName> <probability>")
        @CommandCompletion("@loot-collections * * ")
        public void onItemAdd(Player player, LootCollection lootCollection, String itemName, double probability) {
            HashMap<String, Object> localPlaceHolders = new HashMap<>();
            localPlaceHolders.put("collectionName", lootCollection.getName());
            localPlaceHolders.put("itemName", itemName);
            localPlaceHolders.put("probability", probability);
            
            LinkedHashMap<String, ItemLoot> item = lootCollection.getItems();
            if (item.containsKey(itemName)) {
                player.sendMessage(formatMsg(localPlaceHolders, getMessage(player, LanguageConfigurationNode.LOOT_ITEM_EXISTS)));
                return;
            }
            item.put(itemName, new ItemLoot(itemName, probability, player.getInventory().getItemInMainHand()));
            lootCollection.setItems(item);
            lootHandler.update();
            player.sendMessage(formatMsg(localPlaceHolders, getMessage(player, LanguageConfigurationNode.LOOT_ITEM_ADDED)));
        }
        
        @Subcommand("remove")
        @Syntax("<lootName> <itemName>")
        @CommandCompletion("@loot-collections @loot-items")
        public void onItemRemove(CommandSender sender, LootCollection lootCollection, ItemLoot item) {
            HashMap<String, Object> localPlaceHolders = new HashMap<>();
            localPlaceHolders.put("collectionName", lootCollection.getName());
            localPlaceHolders.put("itemName", item.getName());
            
            lootCollection.getItems().remove(item.getName());
            lootHandler.update();
            sender.sendMessage(formatMsg(localPlaceHolders, getMessage(sender, LanguageConfigurationNode.LOOT_ITEM_REMOVED)));
        }
        
        @Subcommand("list")
        @Syntax("<lootName> <page>")
        @CommandCompletion("@loot-collections *")
        public void onItemList(CommandSender sender, LootCollection lootCollection, @Default("1") int page) {
            if (page == 0) {
                page = 1;
            }
            LinkedHashMap<String, ItemLoot> xpmap = lootCollection.getItems();
            int maxPage = (int) Math.ceil(xpmap.size() / 8.0);
            if (maxPage == 0) {
                maxPage = 1;
            }
            HashMap<String, Object> localPlaceHolders = new HashMap<>();
            localPlaceHolders.put("page", page);
            localPlaceHolders.put("lootName", lootCollection.getName());
            localPlaceHolders.put("collectionName", lootCollection.getName());
            localPlaceHolders.put("maxPage", maxPage);
            
            List<ItemLoot> vals = new ArrayList<>(xpmap.values());
            
            sender.sendMessage(formatMsg(localPlaceHolders, getMessage(sender, LanguageConfigurationNode.LOOT_ITEM_HEADER)));
            for (int i = (page - 1) * 8; i < vals.size(); i++) {
                if (i > page * 8 - 1) {
                    break;
                }
                ItemLoot val = vals.get(i);
                localPlaceHolders.put("itemName", val.getName());
                localPlaceHolders.put("probability", val.getProbability());
                // So to allow the custom stuff so {item} is replaced by a hoverable item
                // We need to section up the message into before, item, after to do that we split
                // Also note that it can have multiple {item} why do I do this to myself?
                String msg = formatMsg(localPlaceHolders, getMessage(sender, LanguageConfigurationNode.LOOT_ITEM_ELEMENT));
                List<BaseComponent> components = new ArrayList<>();
                while (msg.contains("{item}")) {
                    String before = msg.substring(0, msg.indexOf("{item}"));
                    components.addAll(Arrays.asList(TextComponent.fromLegacyText(before)));
                    components.add(MessageHelper.getClickableItem(formatMsg(localPlaceHolders,
                            getMessage(sender, LanguageConfigurationNode.LOOT_ITEM_ELEMENT_NAME)), val.getItemStack()));
                    msg = msg.substring(msg.indexOf("{item}")).substring(6);
                }
                components.addAll(Arrays.asList(TextComponent.fromLegacyText(msg)));
                sender.spigot().sendMessage(components.toArray(new BaseComponent[0]));
            }
            sender.sendMessage(formatMsg(localPlaceHolders, getMessage(sender, LanguageConfigurationNode.LOOT_ITEM_FOOTER)));
        }
    }
    
    @Subcommand("collection")
    @CommandPermission("vhatloots.create")
    public class Collection extends VhatLootsBaseCommand {
        @Subcommand("add")
        @Syntax("<lootName> <collectionPath> <probability>")
        @CommandCompletion("@loot-configurations * *")
        public void onCollectionAdd(CommandSender sender, LootConfiguration lootConfiguration, String collectionName, double probability) {
            HashMap<String, Object> localPlaceHolders = new HashMap<>();
            localPlaceHolders.put("lootName", lootConfiguration.getName());
            while (collectionName.startsWith(".")) {
                collectionName = collectionName.substring(1);
            }
            while (collectionName.endsWith(".")) {
                collectionName = collectionName.substring(0, collectionName.length() - 1);
            }
            localPlaceHolders.put("collectionName", collectionName);
            localPlaceHolders.put("probability", probability);
            
            HashMap<String, LootCollection> lootCollectionPaths = lootConfiguration.getLootPaths();
            if (lootCollectionPaths.containsKey(collectionName)) {
                sender.sendMessage(formatMsg(localPlaceHolders, getMessage(sender, LanguageConfigurationNode.LOOT_COLLECTION_EXISTS)));
                return;
            }
            LinkedHashMap<String, LootCollection> collections = lootConfiguration.getLoot().getCollections();
            String[] parts = collectionName.split("\\.");
            LootCollection newCollection = new LootCollection(collectionName, probability);
            if (parts.length == 1) {
                collections.put(collectionName, newCollection);
                lootCollectionPaths.put(lootConfiguration.getName() + "." + collectionName, newCollection);
                
                lootConfiguration.getLoot().setCollections(collections);
                lootConfiguration.setLootPaths(lootCollectionPaths);
            } else {
                StringBuilder parentPath = new StringBuilder(parts[0]);
                for (int i = 1; i <= parts.length - 2; i++) {
                    parentPath.append(".").append(parts[i]);
                }
                LootCollection parentCollection = lootCollectionPaths.get(lootConfiguration.getName() + "." + parentPath.toString());
                if (parentCollection == null) {
                    localPlaceHolders.put("parentCollectionName", parentPath.toString());
                    sender.sendMessage(formatMsg(localPlaceHolders, getMessage(sender, LanguageConfigurationNode.LOOT_COLLECTION_PARENT_DOESNT_EXIST)));
                    return;
                } else {
                    LinkedHashMap<String, LootCollection> parentCollections = parentCollection.getCollections();
                    
                    parentCollections.put(parts[parts.length - 1], newCollection);
                    lootCollectionPaths.put(lootConfiguration.getName() + "." + collectionName, newCollection);
                    
                    parentCollection.setCollections(parentCollections);
                    lootConfiguration.setLootPaths(lootCollectionPaths);
                }
            }
            lootHandler.update();
            sender.sendMessage(formatMsg(localPlaceHolders, LootCommand.getMessage(sender, LanguageConfigurationNode.LOOT_COLLECTION_ADDED)));
            
        }
        
        @Subcommand("set")
        @Syntax("<lootName> <collectionPath> <attribute> <value>")
        @CommandCompletion("@loot-configurations @loot-collections * *")
        public void onCollectionSet(CommandSender sender, LootConfiguration lootConfiguration, LootCollection collection, String attribute, String value) {
            
        }
        
        public void onCollectionRemove(CommandSender sender, LootConfiguration lootConfiguration, LootCollection collection) {
            
        }
    }
}
