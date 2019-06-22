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

import de.articdive.vhatloots.VhatLoots;

import java.io.File;

/**
 * @author Lukas Mansour
 */
public enum Language {
    ENGLISH("English"),
    GERMAN("German");
    
    private final String name;
    private final File languageFile;
    
    Language(String name) {
        this.name = name;
        languageFile = new File(VhatLoots.getPlugin(VhatLoots.class).getDataFolder()
                + File.separator + "languages" + File.separator + name.toLowerCase() + ".yml");
    }
    
    public String getName() {
        return name;
    }
    
    public File getLanguageFile() {
        return languageFile;
    }
}
