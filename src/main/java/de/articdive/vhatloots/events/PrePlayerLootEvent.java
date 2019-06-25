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

package de.articdive.vhatloots.events;

import de.articdive.vhatloots.configuration.loot.LootConfiguration;
import de.articdive.vhatloots.events.abstractions.PreLootEvent;
import de.articdive.vhatloots.objects.LootContainer;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * @author Lukas Mansour
 */
public class PrePlayerLootEvent extends PreLootEvent {
    private static final HandlerList handlers = new HandlerList();
    private final Player looter;
    private final LootConfiguration lootConfiguration;
    private final LootContainer container;
    
    /**
     * Creates a new event with the given data
     *
     * @param looter       The Player who is looting
     * @param lootConfiguration         The lootConfiguration that the Player looted
     * @param container    The LootContainer being looted or null if no container was involved
     * @param lootingBonus The bonus amount of looting probability
     */
    public PrePlayerLootEvent(Player looter, LootConfiguration lootConfiguration, LootContainer container, double lootingBonus) {
        this.looter = looter;
        this.lootConfiguration = lootConfiguration;
        this.container = container;
        this.lootingBonus = lootingBonus;
    }
    
    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    
    /**
     * Returns the Player who is looting
     *
     * @return The Player who is looting
     */
    public Player getLooter() {
        return looter;
    }
    
    /**
     * Returns the PhatLoot that provided the lootConfiguration
     *
     * @return The PhatLoot that provided the lootConfiguration
     */
    public LootConfiguration getLootConfiguration() {
        return lootConfiguration;
    }
    
    /**
     * Returns the container that is being looted
     *
     * @return The LootContainer being looted or null if no chest was involved
     */
    public LootContainer getContainer() {
        return container;
    }
}
