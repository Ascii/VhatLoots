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

import de.articdive.enum_to_configuration.EnumConfiguration;
import de.articdive.enum_to_configuration.EnumConfigurationBuilder;
import de.articdive.vhatloots.VhatLoots;
import de.articdive.vhatloots.configuration.CoreConfiguration;
import de.articdive.vhatloots.exceptions.IllegalFileException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Lukas Mansour
 */
public class LanguageHandler {
    private static LanguageHandler instance = null;
    private final VhatLoots main = VhatLoots.getPlugin(VhatLoots.class);
    private final EnumConfiguration config = main.getConfiguration();
    private final HashMap<Language, EnumConfiguration> loadedLanguages = new HashMap<>();
    private final List<Language> playerLanguages;
    private final Language consoleLanguage;
    
    private LanguageHandler() {
        // Get and set the console language.
        consoleLanguage = findConsoleLanguage();
        // Get and set the player loadedLanguages.
        playerLanguages = findPlayerLanguages();
        File languageFolder = new File(main.getDataFolder()
                + File.separator + "languages");
        if (!languageFolder.getParentFile().mkdirs() && !languageFolder.getParentFile().isDirectory()) {
            throw new IllegalFileException("Language folder's parent directory was a file!");
        }
        if (!languageFolder.exists() && !languageFolder.mkdir()) {
            throw new IllegalFileException("Language folder was a file!");
        }
        // Console language loadDatabase:
        if (!loadedLanguages.containsKey(consoleLanguage)) {
            loadLanguage(consoleLanguage);
        }
        // Player language loadDatabase:
        for (Language language : playerLanguages) {
            if (!loadedLanguages.containsKey(language)) {
                loadLanguage(language);
            }
        }
    }
    
    private void loadLanguage(Language language) {
        File languageFile = language.getLanguageFile();
        if (!languageFile.getParentFile().mkdirs() && !languageFile.getParentFile().isDirectory()) {
            throw new IllegalFileException("Language folder's parent directory was a file!");
        }
        // Languages file doesn't exist so let's copyFile it from our resources.
        // So that copies the general language (unmodified) file.
        if (!languageFile.exists() && !unpackResourceToFile(language, languageFile)) {
            return;
        }
        EnumConfigurationBuilder enumConfigurationBuilder = new EnumConfigurationBuilder(languageFile, LanguageConfigurationNode.class);
        loadedLanguages.put(language, enumConfigurationBuilder.build());
    }
    
    private boolean unpackResourceToFile(Language language, File file) {
        try {
            if (!file.createNewFile()) {
                throw new IOException("The " + language.getName() + ".yml language file couldn't be created.");
            }
            // Core
            try (InputStream is = this.getClass().getResourceAsStream("/languages/" + language.getName().toLowerCase() + ".yml")) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                FileWriter out = new FileWriter(file);
                String line;
                while ((line = reader.readLine()) != null) {
                    out.write(line + System.getProperty("line.separator"));
                }
                reader.close();
                out.close();
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private List<Language> findPlayerLanguages() {
        ArrayList<Language> playerLanguages = new ArrayList<>();
        for (Object o : (ArrayList) config.get(CoreConfiguration.LANGUAGE_PLAYER__LANGUAGES)) {
            if (o instanceof String) {
                Language playerLanguage = getLanguageFromString((String) o);
                if (playerLanguage != null) {
                    playerLanguages.add(playerLanguage);
                }
            }
            if (playerLanguages.isEmpty()) {
                main.getLogger().info("No player languages were valid or found, adding English by default.");
                playerLanguages.add(Language.ENGLISH);
            }
        }
        return playerLanguages;
    }
    
    private Language findConsoleLanguage() {
        String consoleLanguageString = (String) main.getConfiguration().get(CoreConfiguration.LANGUAGE_CONSOLE__LANGUAGE);
        Language consoleLanguage = getLanguageFromString(consoleLanguageString);
        if (consoleLanguage != null) {
            return consoleLanguage;
        } else {
            main.getLogger().info("The console language was invalid and set to English.");
            return Language.ENGLISH;
        }
    }
    
    private Language getLanguageFromString(String langString) {
        for (Language language : Language.values()) {
            if (langString.trim().equalsIgnoreCase(language.getName())) {
                return language;
            }
        }
        main.getLogger().info(langString.trim() + " is not a valid language!");
        return null;
    }
    
    /**
     * Gets a message from the language files with the console's language.
     *
     * @param node {@link LanguageConfigurationNode} of the message.
     * @return String containing the message.
     */
    public String getConsoleMessage(LanguageConfigurationNode node) {
        return getMessage(consoleLanguage, node);
    }
    
    /**
     * Gets a message from the language files with the specified language.
     *
     * @param language {@link Language} of the message.
     * @param node     {@link LanguageConfigurationNode} of the message.
     * @return String containing the message.
     */
    public String getMessage(Language language, LanguageConfigurationNode node) {
        return (String) loadedLanguages.get(language).get(node);
    }
    
    /**
     * Gets the console's language.
     *
     * @return Console's {@link Language}
     */
    public Language getConsoleLanguage() {
        return consoleLanguage;
    }
    
    /**
     * Gets a list of valid player languages.
     *
     * @return {@link List} of {@link Language}
     */
    public List<Language> getPlayerLanguages() {
        return playerLanguages;
    }
    
    /**
     * Gets a list of all valid languages that are loaded and enabled.
     *
     * @return {@link List} of {@link Language}
     */
    public List<Language> getLanguages() {
        return new ArrayList<>(loadedLanguages.keySet());
    }
    
    public static LanguageHandler getInstance() {
        if (instance == null) {
            instance = new LanguageHandler();
        }
        return instance;
    }
}
