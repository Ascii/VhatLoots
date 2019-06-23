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

package de.articdive.vhatloots.configuration.loot;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import de.articdive.vhatloots.VhatLoots;
import de.articdive.vhatloots.configuration.loot.objects.LootCollection;
import de.articdive.vhatloots.configuration.loot.objects.LootConfiguration;
import de.articdive.vhatloots.configuration.loot.objects.LootObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Lukas Mansour
 */
public class LootHandler {
    private static LootHandler instance;
    private final VhatLoots main = VhatLoots.getPlugin(VhatLoots.class);
    private final Gson gson = new GsonBuilder()
            .serializeNulls()
            .setPrettyPrinting()
            .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
            .create();
    private final File lootFile = new File(main.getDataFolder() + File.separator + "loot.json");
    private Map<String, LootConfiguration> loot = new HashMap<>();
    
    private LootHandler() {
        if (!lootFile.getParentFile().mkdirs() && !lootFile.getParentFile().isDirectory()) {
            main.getLogger().severe("Parent folder for loot.json was a file, not a directory.");
            return;
        }
        if (!lootFile.exists() && !unpackLootJson()) {
            main.getLogger().severe("LootConfiguration.json could not be copied out of the jar.");
            return;
        }
        deSerialize();
    }
    
    public List<LootConfiguration> getAll() {
        return new ArrayList<>(loot.values());
    }
    
    public void addLootConfiguration(LootConfiguration lootConfiguration) {
        this.loot.put(lootConfiguration.getName(), lootConfiguration);
    }
    
    public void removeLootConfiguration(LootConfiguration lootConfiguration) {
        this.loot.remove(lootConfiguration.getName());
    }
    
    private void serialize() {
        try {
            FileWriter writer = new FileWriter(lootFile, false);
            gson.toJson(new ArrayList<>(loot.values()), writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void deSerialize() {
        try {
            FileReader reader = new FileReader(lootFile);
            Type collectionType = new TypeToken<List<LootConfiguration>>() {
            }.getType();
            List<LootConfiguration> lootConfigurations = gson.fromJson(reader, collectionType);
            if (lootConfigurations == null || lootConfigurations.isEmpty()) {
                lootConfigurations = new ArrayList<>();
            }
            reader.close();
            HashMap<String, LootConfiguration> newLoot = new HashMap<>();
            for (LootConfiguration lootConfiguration : lootConfigurations) {
                newLoot.put(lootConfiguration.getName(), lootConfiguration);
                LootCollection rootCollection = lootConfiguration.getLoot();
                String name = lootConfiguration.getName();
                lootConfiguration.addCollectionPath(name, rootCollection);
                rootCollection.setName(name);
                rootCollection.setPath(name);
                updateNestedObjects(rootCollection.getXp());
                updateNestedObjects(rootCollection.getMoney());
                updateNestedObjects(rootCollection.getItems());
                updateNestedObjects(rootCollection.getCommands());
                updateNestedCollections(rootCollection.getCollections(), name, lootConfiguration);
            }
            this.loot = newLoot;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void updateNestedObjects(LinkedHashMap<String, ? extends LootObject> lootObjectMap) {
        List<String> keys = new ArrayList<>(lootObjectMap.keySet());
        for (int i = 0; i < lootObjectMap.size(); i++) {
            LootObject lootObject = lootObjectMap.values().toArray(new LootObject[0])[i];
            lootObject.setName(keys.get(i));
        }
    }
    
    private void updateNestedCollections(LinkedHashMap<String, LootCollection> collectionMap, String root, LootConfiguration lootConfiguration) {
        List<String> keys = new ArrayList<>(collectionMap.keySet());
        for (int i = 0; i < collectionMap.size(); i++) {
            String name = keys.get(i);
            String fullPath = root + "." + name;
            // Remove the dot, if the root is empty.
            if (root.isEmpty()) {
                fullPath = fullPath.substring(1);
            }
            LootCollection collection = collectionMap.values().toArray(new LootCollection[0])[i];
            lootConfiguration.addCollectionPath(fullPath, collection);
            collection.setName(name);
            collection.setPath(fullPath);
            updateNestedObjects(collection.getXp());
            updateNestedObjects(collection.getMoney());
            updateNestedObjects(collection.getItems());
            updateNestedCollections(collection.getCollections(), fullPath, lootConfiguration);
        }
    }
    
    @SuppressWarnings("ConstantConditions")
    private boolean unpackLootJson() {
        try {
            if (!lootFile.createNewFile()) {
                return false;
            }
            // Core
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            this.getClass().getClassLoader().getResourceAsStream("loot.json")
                    )
            );
            OutputStreamWriter out = new OutputStreamWriter((new FileOutputStream(lootFile)));
            String line;
            while ((line = reader.readLine()) != null) {
                out.write(line + System.getProperty("line.separator"));
            }
            reader.close();
            out.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public LootConfiguration get(String name) {
        return loot.get(name);
    }
    
    public boolean exists(String name) {
        return loot.containsKey(name);
    }
    
    public void update() {
        update(false);
    }
    
    public void update(boolean updateNested) {
        serialize();
        if (updateNested) {
            deSerialize();
        }
    }
    
    public static LootHandler getInstance() {
        if (instance == null) {
            instance = new LootHandler();
        }
        return instance;
    }
}
