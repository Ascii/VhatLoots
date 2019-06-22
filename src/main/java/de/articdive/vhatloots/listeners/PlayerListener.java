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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import de.articdive.vhatloots.configuration.gson.LootHandler;
import de.articdive.vhatloots.configuration.gson.objects.Loot;
import de.articdive.vhatloots.events.PlayerLootEvent;
import de.articdive.vhatloots.events.PrePlayerLootEvent;
import de.articdive.vhatloots.events.abstractions.PreLootEvent;
import de.articdive.vhatloots.events.objects.LootBundle;
import de.articdive.vhatloots.language.LanguageConfigurationNode;
import de.articdive.vhatloots.objects.LootContainer;
import de.articdive.vhatloots.objects.Player;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Period;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.UUID;

/**
 * @author Lukas Mansour
 */
public class PlayerListener extends BaseListener {
    private final LootHandler lootHandler = LootHandler.getInstance();
    
    public PlayerListener() {
        super();
    }
    
    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        if (!Player.exists(event.getPlayer().getUniqueId())) {
            if (!Player.add(event.getPlayer().getUniqueId())) {
                event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Failed to create a database record for your UUID!");
            }
        }
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!event.hasBlock() || event.getClickedBlock() == null || event.getHand() == EquipmentSlot.OFF_HAND) {
            return;
        }
        Block clickedBlock = event.getClickedBlock();
        
        LootContainer lootContainer = LootContainer.getContainer(clickedBlock.getWorld().getUID(), clickedBlock.getX(), clickedBlock.getY(), clickedBlock.getZ());
        if (lootContainer == null) {
            return;
        }
        Type collectionType = new TypeToken<HashMap<UUID, String>>() {
        }.getType();
        HashMap<UUID, String> cooldowns = new Gson().fromJson(lootContainer.getString("cooldowns"), collectionType);
        if (cooldowns == null) {
            cooldowns = new HashMap<>();
        }
        
        org.bukkit.entity.Player p = event.getPlayer();
        
        HashMap<String, String> localPlayerHolders = new HashMap<>();
        String lootName = lootContainer.getString("loot");
        localPlayerHolders.put("lootName", lootName);
        if (!lootHandler.exists(lootName)) {
            p.sendMessage(formatMsg(localPlayerHolders, getMessage(p, LanguageConfigurationNode.LOOTBOX_MISCONFIGURED)));
            event.setCancelled(true);
            return;
        }
        Loot loot = lootHandler.get(lootName);
        
        boolean lootable = false;
        if (cooldowns.containsKey(p.getUniqueId())) {
            DateTime cooldownTime = new DateTime(cooldowns.get(p.getUniqueId()), DateTimeZone.UTC);
            DateTime now = new DateTime(DateTimeZone.UTC);
            Period delay = new Period(0, 0, 0, loot.getDelay());
            localPlayerHolders.put("years", "");
            localPlayerHolders.put("months", "");
            localPlayerHolders.put("weeks", "");
            localPlayerHolders.put("days", "");
            localPlayerHolders.put("hours", "");
            localPlayerHolders.put("minutes", "");
            localPlayerHolders.put("seconds", "");
            if (cooldownTime.plus(delay).isBefore(now)) {
                lootable = true;
            } else {
                Period period = new Period(cooldownTime.plus(delay), now);
                // Remove negative time.
                if (period.getMillis() < 0) {
                    period = period.minus(period).minus(period);
                }
                // Get cooldown.
                if (period.getYears() > 0) {
                    localPlayerHolders.put("years", period.getYears() + " " + getMessage(p, LanguageConfigurationNode.TIME_YEARS));
                    period = period.minusYears(period.getYears());
                }
                if (period.getMonths() > 0) {
                    localPlayerHolders.put("months", period.getMonths() + " " + getMessage(p, LanguageConfigurationNode.TIME_MONTHS));
                    period = period.minusMonths(period.getMonths());
                }
                if (period.getWeeks() > 0) {
                    localPlayerHolders.put("weeks", period.getWeeks() + " " + getMessage(p, LanguageConfigurationNode.TIME_WEEKS));
                    period = period.minusWeeks(period.getWeeks());
                }
                if (period.getDays() > 0) {
                    localPlayerHolders.put("days", period.getDays() + " " + getMessage(p, LanguageConfigurationNode.TIME_DAYS));
                    period = period.minusDays(period.getDays());
                }
                if (period.getHours() > 0) {
                    localPlayerHolders.put("hours", period.getHours() + " " + getMessage(p, LanguageConfigurationNode.TIME_HOURS));
                    period = period.minusHours(period.getHours());
                }
                if (period.getMinutes() > 0) {
                    localPlayerHolders.put("minutes", period.getMinutes() + " " + getMessage(p, LanguageConfigurationNode.TIME_MINUTES));
                    period = period.minusMinutes(period.getMinutes());
                }
                if (period.getSeconds() > 0) {
                    localPlayerHolders.put("seconds", period.getSeconds() + " " + getMessage(p, LanguageConfigurationNode.TIME_SECONDS));
                }
                p.sendMessage(formatMsg(localPlayerHolders, getMessage(p, LanguageConfigurationNode.LOOTBOX_COOLDOWN)));
            }
        } else {
            lootable = true;
        }
        event.setCancelled(true);
        if (lootable) {
            PreLootEvent preLootEvent = new PrePlayerLootEvent(p, loot, lootContainer, 0);
            Bukkit.getPluginManager().callEvent(preLootEvent);
            if (preLootEvent.isCancelled()) {
                return;
            }
            LootBundle lootBundle = loot.generateLoot(0);
            PlayerLootEvent lootEvent = new PlayerLootEvent(p, loot, lootContainer, lootBundle);
            Bukkit.getPluginManager().callEvent(event);
            if (lootEvent.isCancelled()) {
                return;
            }
            
            cooldowns.put(p.getUniqueId(), new DateTime(DateTimeZone.UTC).toString());
            lootContainer.setString("cooldowns", new Gson().toJson(cooldowns));
            lootContainer.saveIt();
        }
    }
}
