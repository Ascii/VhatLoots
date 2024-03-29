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

package de.articdive.vhatloots.language;

import de.articdive.enum_to_configuration.ConfigurationNode;
import de.articdive.enum_to_configuration.ConfigurationSection;

/**
 * @author Lukas Mansour
 */
@SuppressWarnings("unused")
public enum LanguageConfigurationNode implements ConfigurationNode {
    // Aikar's command framework.
    ACF_CORE("acf-core", new ConfigurationSection()),
    ACF_A("acf-core.permission_denied", "&cI'm sorry, but you do not have permission to perform this command."),
    ACF_B("acf-core.permission_denied_paramete", "&cI'm sorry, but you do not have permission to perform this command."),
    ACF_C("acf-core.error_generic_logged", "&cAn error occurred. This problem has been logged. Sorry for the inconvenience."),
    ACF_D("acf-core.unknown_command", "&cUnknown Command, please type &e/help"),
    ACF_E("acf-core.invalid_syntax", "&eUsage: &a{command} &f{syntax}"),
    ACF_F("acf-core.error_prefix", "&4Error: &c{message}"),
    ACF_G("acf-core.error_performing_command", "&cI'm sorry, but there was an error performing this command."),
    ACF_H("acf-core.info_message", "{message}"),
    ACF_I("acf-core.please_specify_one_of", "&4Error: &cPlease specify one of (&e{valid})"),
    ACF_J("acf-core.must_be_a_number", "&4Error: &c{num} must be a number."),
    ACF_K("acf-core.must_be_min_length", "&4Error: &cMust be at least {min} characters long."),
    ACF_L("acf-core.must_be_max_length", "&4Error: &cMust be at most {max} characters long."),
    ACF_M("acf-core.please_specify_at_most", "&4Error: &cPlease specify a value at most {max}."),
    ACF_N("acf-core.please_specify_at_least", "&4Error: &cPlease specify a value at least {min}."),
    ACF_O("acf-core.not_allowed_on_console", "&4Error: &cConsole may not execute this command."),
    ACF_P("acf-core.could_not_find_playe", "&4Error: &cCould not find a player by the name: &e{search}"),
    ACF_Q("acf-core.no_command_matched_search", "&cNo command matched &e{search}&c."),
    ACF_R("acf-core.help_page_information", "&b- Showing page &a{page} &bof &a{totalpages} &e{results}&b results)."),
    ACF_S("acf-core.help_no_results", "&4Error: &cNo more results."),
    ACF_T("acf-core.help_header", "&e=== &bShowing help for &a{commandprefix}{command} &e==="),
    ACF_U("acf-core.help_format", "&b{command} &a{parameters}"),
    ACF_V("acf-core.help_detailed_header", "&e=== &bShowing detailed help for &a{commandprefix}{command} &e==="),
    ACF_W("acf-core.help_detailed_command_format", "&b{command} &a{parameters}"),
    ACF_X("acf-core.help_detailed_parameter_format", "&b{syntaxorname}"),
    ACF_Y("acf-core.help_search_header", "&e=== &bSearch results for &a{commandprefix}{command} {search} &e==="),
    
    ACF_MINERAFT("acf-minecraft", new ConfigurationSection()),
    ACF_Z("acf-minecraft.invalid_world", "&4Error: &cThat world does not exists."),
    ACF_AA("acf-minecraft.you_must_be_holding_item", "&4Error: &cYou must be holding an item in your main hand."),
    ACF_AB("acf-minecraft.player_is_vanished_confirm", "Warning: <c2>{vanished}</c2> is vanished. Do not blow their cover!\n\\" +
            "To confirm your action, add &a:confirm to the end of their name.\n\\" +
            "Ex: &a{vanished}:confirm"),
    ACF_AC("acf-minecraft.username_too_short", "&4Error: &cUsername too short, must be at least three characters."),
    ACF_AD("acf-minecraft.is_not_a_valid_name", "&4Error: &e{name} &cis not a valid username."),
    ACF_AE("acf-minecraft.multiple_players_match", "&4Error: &cMultiple players matched &e{search} &a({all})&c, please be more specific."),
    ACF_AF("acf-minecraft.no_player_found_server", "&cNo player matching &e{search} &cis connected to this server."),
    ACF_AG("acf-minecraft.no_player_found_offline", "&cNo player matching &e{search} &ccould be found."),
    ACF_AH("acf-minecraft.no_player_found", "&cNo player matching &e{search} &ccould be found."),
    ACF_AI("acf-minecraft.location_please_specify_world", "&4Error: &cPlease specify world. Example: &aworld:x,y,z&c."),
    ACF_AJ("acf-minecraft.location_please_specify_xyz", "&4Error: &cPlease specify the coordinates x, y and z. Example: &aworld:x,y,z&c."),
    ACF_AK("acf-minecraft.location_console_not_relative", "&4Error: &cConsole may not use relative coordinates for location."),
    
    PLUGIN_ENABLED("plugin_enabled", "VhatLoots has been enabled."),
    PLUGIN_DISABLED("plugin_disabled", "VhatLoots has been disabled."),
    
    TIME("time", new ConfigurationSection()),
    TIME_YEARS("time.years", "&3Years&c, "),
    TIME_MONTHS("time.months", "&3Months&c, "),
    TIME_WEEKS("time.weeks", "&3Weeks&c, "),
    TIME_DAYS("time.days", "&3Days&c, "),
    TIME_HOURS("time.hours", "&3Hours&c, "),
    TIME_MINUTES("time.minutes", "&3minutes&c, "),
    TIME_SECONDS("time.seconds", "&3seconds&c. "),
    
    LOOT("loot", new ConfigurationSection()),
    LOOT_CREATED("loot.created", "&aSuccessfully created a loot layout called &2{lootName}&a."),
    LOOT_EXISTS("loot.exists", "&cLoot layout called &4{lootName} &calready exists."),
    LOOT_INVALID_BLOCK("loot.invalid_block", "&cYou are not looking at a valid Container."),
    LOOT_RENAMED("loot.renamed", "&aLoot layout &2{oldLootName} &awas renamed to &2{newLootName}&a."),
    LOOT_FAILED_LINK("loot.failed_link", "&cLoot-configuration failed to link to target Block."),
    LOOT_DELETED("loot.deleted", "&aLoot layout &2{lootName} &awas successfully deleted."),
    LOOT_HEADER("loot.list_header", " &9--- &7[&aLoot-Configurations&7] &9--- "),
    LOOT_ELEMENT("loot.list_element", "&a{lootName} &e- &3AL: {autoLoot} &e- &3G: {global}"),
    LOOT_FOOTER("loot.list_footer", "&9Page: &2{page}&7/&2{maxPage}"),
    
    LOOT_SET("loot_set", new ConfigurationSection()),
    LOOT_SET_GLOBAL("loot_set.global", "&aThe global option &aof &2{lootName} &a,was set to &3{global}&a."),
    LOOT_SET_AUTOLOOT("loot_set.autoloot", "&aThe autoloot option of &2{lootName} &a,was set to &3{autoLoot}&a."),
    LOOT_SET_DELAY("loot_set.delay", "&aThe delay of &2{lootname} &a,was set to &3{delay}."),
    
    LOOT_XP("loot_xp", new ConfigurationSection()),
    LOOT_XP_CREATED("loot_xp.created", "&a&aSuccessfully created xp layout &2{xpName} &ain &2{collectionPath}&a."),
    LOOT_XP_EXISTS("loot_xp.exists", "&cXP layout called &4{xpName}&c already exists in &4{collectionPath}."),
    LOOT_XP_DELETED("loot_xp.deleted", "&aXP layout &2{xpName} &ain &2{collectionPath} &awas successfully deleted."),
    LOOT_XP_SET("loot_xp.set", "&aSet option &2{option} &ato be &2{value} &ain &2{xpName} &ain &2{collectionPath}&a."),
    LOOT_XP_INVALID_OPTION("loot_xp.invalid_option", "&cOption &4{option} &cis not a valid option for xp layout &4{xpName} &cin &4{collectionPath}&c."),
    LOOT_XP_INVALID_VALUE("loot_xp.invalid_value", "&cValue &4[value} &cwas not in the valid format for option &4{option}&c."),
    
    LOOT_XP_HEADER("loot_xp.list_header", " &9--- &7[&aXP-Configurations&7] &9--- "),
    LOOT_XP_ELEMENT("loot_xp.list_element", "&a{xpName} &e- &3{probability}% &e- &3{range}"),
    LOOT_XP_FOOTER("loot_xp.list_footer", "&9Page: &2{page}&7/&2{maxPage}"),
    
    LOOT_MONEY("loot_money", new ConfigurationSection()),
    LOOT_MONEY_CREATED("loot_money.created", "&aSuccessfully created money layout &2{moneyName} &ain &2{collectionPath}&a."),
    LOOT_MONEY_EXISTS("loot_money.exists", "&cMoney layout called &4{moneyName}&c already exists in &4{collectionPath}."),
    LOOT_MONEY_DELETED("loot_money.deleted", "&aMoney layout &2{moneyName} &ain &2{collectionPath} &awas successfully deleted."),
    LOOT_MONEY_SET("loot_money.set", "&aSet option &2{option} &ato be &2{value} &ain &2{moneyName} &ain &2{collectionPath}&a."),
    LOOT_MONEY_INVALID_OPTION("loot_money.invalid_option", "&cOption &4{option} &cis not a valid option for money layout &4{moneyName} &cin &4{collectionPath}&c."),
    LOOT_MONEY_INVALID_VALUE("loot_money.invalid_value", "&cValue &4[value} &cwas not in the valid format for option &4{option}&c."),
    
    LOOT_MONEY_HEADER("loot_money.list_header", " &9--- &7[&aMoney-Configurations&7] &9--- "),
    LOOT_MONEY_ELEMENT("loot_money.list_element", "&a{moneyName} &e - &3{probability}% &e - &3{range}"),
    LOOT_MONEY_FOOTER("loot_money.list_footer", "&9Page: &2{page}&7/&2{maxPage}"),
    
    LOOT_ITEM("loot_item", new ConfigurationSection()),
    LOOT_ITEM_CREATED("loot_item.created", "&aSuccessfully created item layout &2{itemName} &ain &2{collectionPath}&a."),
    LOOT_ITEM_EXISTS("loot_item.exists", "&cItem layout called &4{itemName}&c already exists in collection &4{collectionPath}."),
    LOOT_ITEM_DELETED("loot_item.deleted", "&aItem layout &2{itemName} &ain &2{collectionPath} &awas successfully deleted."),
    LOOT_ITEM_SET("loot_item.set", "&aSet option &2{option} &ato be &2{value} &ain &2{moneyName} &ain &2{collectionPath}&a."),
    LOOT_ITEM_INVALID_OPTION("loot_item.invalid_option", "&cOption &4{option} &cis not a valid option for item layout &4{itemName} &cin &4{collectionPath}&c."),
    LOOT_ITEM_INVALID_VALUE("loot_item.invalid_value", "&cValue &4[value} &cwas not in the valid format for option &4{option}&c."),
    
    LOOT_ITEM_HEADER("loot_item.list_header", " &9--- &7[&aItem-Configurations&7] &9--- "),
    
    LOOT_ITEM_ELEMENT("loot_item.list_element", "&a{itemName} &e - &f{probability}% &e - &9{item}."),
    LOOT_ITEM_ELEMENT_NAME("loot_item.list_element_name", "&f[Item]"),
    LOOT_ITEM_FOOTER("loot_item.list_footer", "&9Page: &2{page}&7/&2{maxPage}"),
    
    LOOT_COLLECTION("loot_collection", new ConfigurationSection()),
    LOOT_COLLECTION_CREATED("loot_collection_created", "&aSuccessfully created collection &2{collectionPath}&a."),
    LOOT_COLLECTION_EXISTS("loot_collection.exists", "&cCollection with path &4{collectionPath}&c already exists in loot &4{lootName}&c."),
    LOOT_COLLECTION_DELETED("loot_collection.deleted", "&aCollection &2{collectionPath} &ain &2{lootName} &awas successfully deleted."),
    LOOT_COLLECTION_SET("loot_collection.set", "&aSet option &2{option} &ato be &2{value} &ain &2{collectionPath}&a."),
    LOOT_COLLECTION_INVALID_OPTION("loot_collection.invalid_option", "&cOption &4{option} &cis not a valid option for collection with path &4{collectionPath} &cin &4{lootName}&c."),
    LOOT_COLLECTION_INVALID_VALUE("loot_collection.invalid_value", "&cValue &4[value} &cwas not in the valid format for option &4{option}&c."),
    LOOT_COLLECTION_PARENT_DOESNT_EXIST("loot_collection.parent_doesnt_exist", "&cParent-collection called &4{parentCollectionPath}&c does not exist in loot &4{lootName}&c."),
    
    LOOTBOX("loot_container", new ConfigurationSection()),
    LOOTBOX_MISCONFIGURED("loot_container.misconfigured", "&cLoot-layout &4{lootName} &cis misconfigured."),
    LOOTBOX_CREATED("loot_container.created", "&aSuccessfully created a loot-container called {containerName}."),
    LOOTBOX_EXISTS("loot_container.exists", "&cLoot-container called &4{containerName} &calready exists."),
    LOOTBOX_COOLDOWN("loot_container.cooldown", "&cYou can open this container in {years}{months}{weeks}{days}{hours}{minutes}{seconds}");
    
    
    private final String path;
    private final Object defaultValue;
    
    LanguageConfigurationNode(String path, Object defaultValue) {
        this.path = path;
        this.defaultValue = defaultValue;
    }
    
    @Override
    public String getPath() {
        return path;
    }
    
    @Override
    public Object getDefaultValue() {
        return defaultValue;
    }
    
    @Override
    public String[] getComments() {
        return new String[0];
    }
}