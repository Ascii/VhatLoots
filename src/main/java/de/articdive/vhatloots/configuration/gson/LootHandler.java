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

package de.articdive.vhatloots.configuration.gson;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import de.articdive.vhatloots.VhatLoots;
import de.articdive.vhatloots.configuration.gson.objects.Loot;
import de.articdive.vhatloots.configuration.gson.objects.CollectionLoot;
import de.articdive.vhatloots.configuration.gson.objects.ItemLoot;
import de.articdive.vhatloots.configuration.gson.objects.MoneyLoot;
import de.articdive.vhatloots.configuration.gson.objects.XPLoot;

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
    private Map<String, Loot> loot = new HashMap<>();
    
    private LootHandler() {
        if (!lootFile.getParentFile().mkdirs() && !lootFile.getParentFile().isDirectory()) {
            main.getLogger().severe("Parent folder for loot.json was a file, not a directory.");
            return;
        }
        if (!lootFile.exists() && !unpackLootJson()) {
            main.getLogger().severe("Loot.json could not be copied out of the jar.");
            return;
        }
        deSerialize();
    }
    
    public List<Loot> getAll() {
        return new ArrayList<>(loot.values());
    }
    
    public void addLootConfiguration(Loot loot) {
        this.loot.put(loot.getName(), loot);
    }
    
    public void removeLootConfiguration(Loot loot) {
        this.loot.remove(loot.getName());
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
            Type collectionType = new TypeToken<List<Loot>>() {
            }.getType();
            List<Loot> loot = gson.fromJson(reader, collectionType);
            if (loot == null || loot.isEmpty()) {
                loot = new ArrayList<>();
            }
            reader.close();
            HashMap<String, Loot> newLoot = new HashMap<>();
            for (Loot lut : loot) {
                newLoot.put(lut.getName(), lut);
                setCollectionNames(lut.getCollections(), "", lut);
            }
            this.loot = newLoot;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void setXPNames(LinkedHashMap<String, XPLoot> xpMap) {
        List<String> keys = new ArrayList<>(xpMap.keySet());
        for (int i = 0; i < xpMap.size(); i++) {
            XPLoot xp = xpMap.values().toArray(new XPLoot[0])[i];
            xp.setName(keys.get(i));
        }
    }
    
    private void setMoneyNames(LinkedHashMap<String, MoneyLoot> moneyMap) {
        List<String> keys = new ArrayList<>(moneyMap.keySet());
        for (int i = 0; i < moneyMap.size(); i++) {
            MoneyLoot money = moneyMap.values().toArray(new MoneyLoot[0])[i];
            money.setName(keys.get(i));
        }
    }
    
    private void setItemNames(LinkedHashMap<String, ItemLoot> itemMap) {
        List<String> keys = new ArrayList<>(itemMap.keySet());
        for (int i = 0; i < itemMap.size(); i++) {
            ItemLoot item = itemMap.values().toArray(new ItemLoot[0])[i];
            item.setName(keys.get(i));
        }
    }
    
    private void setCollectionNames(LinkedHashMap<String, CollectionLoot> collectionMap, String root, Loot loot) {
        List<String> keys = new ArrayList<>(collectionMap.keySet());
        for (int i = 0; i < collectionMap.size(); i++) {
            String name = keys.get(i);
            String fullPath = root + "." + name;
            // Remove the dot, if the root is empty.
            if (root.isEmpty()) {
                fullPath = fullPath.substring(1);
            }
            CollectionLoot collection = collectionMap.values().toArray(new CollectionLoot[0])[i];
            loot.addCollectionPath(fullPath, collection);
            collection.setName(name);
            collection.setPath(fullPath);
            setXPNames(collection.getXp());
            setMoneyNames(collection.getMoney());
            setItemNames(collection.getItems());
            setCollectionNames(collection.getCollections(), fullPath, loot);
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
    
    public Loot get(String name) {
        return loot.get(name);
    }
    
    public boolean exists(String name) {
        return loot.containsKey(name);
    }
    
    public void update() {
        serialize();
    }
    
    public static LootHandler getInstance() {
        if (instance == null) {
            instance = new LootHandler();
        }
        return instance;
    }
}
