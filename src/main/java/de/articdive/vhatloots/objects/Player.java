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

import de.articdive.vhatloots.language.Language;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.CompositePK;
import org.javalite.activejdbc.annotations.DbName;
import org.javalite.activejdbc.annotations.Table;

import java.util.UUID;

/**
 * @author Lukas Mansour
 */
@Table("PLAYERS")
@DbName("VhatLoots")
@CompositePK("uuid")
public class Player extends Model {
    
    static {
        validatePresenceOf("uuid");
    }
    
    public static boolean add(UUID uuid) {
        Player player = new Player();
        player.set("uuid", uuid);
        player.set("language", Language.ENGLISH.name());
        return player.saveIt();
    }
    
    public static boolean exists(UUID uuid) {
        return Player.findFirst("uuid = ?", uuid.toString()) != null;
    }
    
    public static long getAmount() {
        return Player.count();
    }
    
    public static Language getLanguage(UUID uuid) {
        return Language.valueOf(Player.findByCompositeKeys(uuid).getString("language"));
    }
    
}
