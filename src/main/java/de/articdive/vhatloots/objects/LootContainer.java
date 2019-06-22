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

package de.articdive.vhatloots.objects;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.CompositePK;
import org.javalite.activejdbc.annotations.DbName;
import org.javalite.activejdbc.annotations.Table;

import java.util.UUID;

/**
 * @author Lukas Mansour
 */
@Table("CONTAINERS")
@DbName("VhatLoots")
@CompositePK({"uuid", "name"})
public class LootContainer extends Model {
    
    static {
        validatePresenceOf("uuid", "name", "world", "x", "y", "z");
    }
    
    public static boolean add(UUID uuid, String name, UUID world, int x, int y, int z, String loot) {
        LootContainer lootContainer = new LootContainer();
        lootContainer.set("uuid", uuid, "name", name, "world", world, "x", x, "y", y, "z", z, "loot", loot);
        return lootContainer.saveIt();
    }
    
    public static boolean exists(String name) {
        return LootContainer.findFirst("name = ?", name) != null;
    }
    
    public static long getAmount() {
        return LootContainer.count();
    }
    
    public static LootContainer getContainer(UUID world, int x, int y, int z) {
        return LootContainer.findFirst("world = '" + world + "' and  x = '" + x + "' and y = '" + y + "' and z = '" + z + "'");
    }
}
