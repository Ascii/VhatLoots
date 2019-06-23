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

package de.articdive.vhatloots.configuration;

import de.articdive.enum_to_configuration.ConfigurationNode;
import de.articdive.enum_to_configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Lukas Mansour
 */
@SuppressWarnings("unused")
public enum CoreConfiguration implements ConfigurationNode {
    VERSION_SECTION("version", new ConfigurationSection()),
    VERSION("version.version", "",
            "# This is the current version of VhatLoots",
            "# Please do not edit this value!"
    ),
    LATEST_RAN_VERSION("version.latest_ran_version", "",
            "# This is the latest ran version of VhatLoots",
            "# Please do not edit this value!"),
    DATABASE_SECTION("database", new ConfigurationSection(),
            " ",
            "##############################",
            "# +------------------------+ #",
            "# | Database Configuration | #",
            "# +------------------------+ #",
            "##############################",
            "# Valid DB objects: h2, mysql, oracle, postgresql, sqlite, sqlserver.",
            "# For h2 and sqlite: ",
            "# Database hostname becomes file location (./) is relativ to your server's spigot.jar",
            "# Database schema becomes the name of the h2 or sqlite file."
    ),
    DATABASE_TABLE_PREFIX("database.table_prefix", "VHATLOOTS_",
            "# Database table prefix."),
    DATABASE_TYPE("database.type", "h2",
            "# Database type."
    ),
    DATABASE_HOSTNAME("database.hostname", "./plugins/VhatLoots/database",
            "# Database hostname."
    ),
    DATABASE_PORT("database.port", "3306",
            "# Database port."
    ),
    DATABASE_SCHEMA_NAME("database.schema", "VhatLoots",
            "# Database schema."
    ),
    DATABASE_USERNAME("database.username", "",
            "# Database username."
    ),
    DATABASE_PASSWORD("database.password", "",
            "# Database password."
    ),
    LANGUAGE("language", new ConfigurationSection(),
            " ",
            "##############################",
            "# +------------------------+ #",
            "# | Language Configuration | #",
            "# +------------------------+ #",
            "##############################",
            " ",
            "# Valid Languages: english, german, french."
    ),
    LANGUAGE_PLAYER__LANGUAGES("language.player_languages", new ArrayList<>(Arrays.asList("english", "german")),
            "# The languages that players can set for themselves.",
            "# The first language is the default player language."
    ),
    LANGUAGE_CONSOLE__LANGUAGE("language.console_language", "english",
            "# The language in which the console's commands are in."
    );
    
    private final String path;
    private final Object defaultValue;
    private final String[] comments;
    
    CoreConfiguration(String path, Object defaultValue, String... comments) {
        this.path = path;
        this.defaultValue = defaultValue;
        this.comments = comments;
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
        return comments;
    }
}
