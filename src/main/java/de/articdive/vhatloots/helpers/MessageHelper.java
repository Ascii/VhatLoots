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

package de.articdive.vhatloots.helpers;

import de.articdive.vhatloots.language.Language;
import de.articdive.vhatloots.language.LanguageConfigurationNode;
import de.articdive.vhatloots.language.LanguageHandler;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Lukas Mansour
 */
public class MessageHelper {
    public static final HashMap<String, String> globalPlaceHolders = new HashMap<>();
    private static final Pattern placeHolderPattern = Pattern.compile("(?<=\\{)(.*?)(?=})");
    /*
     * Cache of NMS classes that we've searched for
     */
    private final static Map<String, Class<?>> loadedNMSClasses = new HashMap<>();
    /*
     * Cache of OBS classes that we've searched for
     */
    private final static Map<String, Class<?>> loadedOBCClasses = new HashMap<>();
    /*
     * Cache of methods that we've found in particular classes
     */
    private final static Map<Class<?>, Map<String, Method>> loadedMethods = new HashMap<>();
    /*
     * The server version string to location NMS & OBC classes
     */
    private static String versionString;
    
    public static String[] formatMsg(HashMap<String, String> placeholders, String... messages) {
        placeholders.putAll(globalPlaceHolders);
        List<String> formattedMsgs = new ArrayList<>();
        for (String msg : messages) {
            Matcher m = placeHolderPattern.matcher(msg);
            while (m.find()) {
                for (int i = 1; i <= m.groupCount(); i++) {
                    if (placeholders.get(m.group(i)) == null) {
                        msg = msg.replace("{" + m.group(i) + "}", "");
                    } else {
                        msg = msg.replace("{" + m.group(i) + "}", placeholders.get(m.group(i)));
                    }
                }
            }
            formattedMsgs.add(ChatColor.translateAlternateColorCodes('&', msg));
        }
        return formattedMsgs.toArray(new String[0]);
    }
    
    public static String formatMsg(HashMap<String, String> placeholders, String message) {
        placeholders.putAll(globalPlaceHolders);
        Matcher m = placeHolderPattern.matcher(message);
        while (m.find()) {
            for (int i = 1; i <= m.groupCount(); i++) {
                if (placeholders.get(m.group(i)) != null) {
                    message = message.replace("{" + m.group(i) + "}", placeholders.get(m.group(i)));
                }
            }
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }
    
    public static String getMessage(CommandSender sender, LanguageConfigurationNode node) {
        LanguageHandler languageHandler = LanguageHandler.getInstance();
        Language language;
        if (sender instanceof Player) {
            language = de.articdive.vhatloots.objects.Player
                    .getLanguage(((Player) sender).getUniqueId());
        } else {
            language = languageHandler.getConsoleLanguage();
        }
        return languageHandler.getMessage(language, node);
    }
    
    public static TextComponent getClickableItem(String msg, ItemStack is) {
        BaseComponent[] hoverEventComponents = new BaseComponent[]{
                new TextComponent(convertItemStackToJson(is))
        };
        
        HoverEvent event = new HoverEvent(HoverEvent.Action.SHOW_ITEM, hoverEventComponents);
        
        TextComponent component = new TextComponent(TextComponent.fromLegacyText(msg));
        component.setHoverEvent(event);
        return component;
    }
    
    private static String convertItemStackToJson(ItemStack itemStack) {
        // ItemStack methods to get a net.minecraft.server.ItemStack object for serialization
        Class<?> craftItemStackClazz = getOBCClass();
        Method asNMSCopyMethod = getMethod(craftItemStackClazz, "asNMSCopy", ItemStack.class);
        
        // NMS Method to serialize a net.minecraft.server.ItemStack to a valid Json string
        Class<?> nmsItemStackClazz = getNMSClass("ItemStack");
        Class<?> nbtTagCompoundClazz = getNMSClass("NBTTagCompound");
        Method saveNmsItemStackMethod = getMethod(nmsItemStackClazz, "save", nbtTagCompoundClazz);
        
        Object nmsNbtTagCompoundObj; // This will just be an empty NBTTagCompound instance to invoke the saveNms method
        Object nmsItemStackObj; // This is the net.minecraft.server.ItemStack object received from the asNMSCopy method
        Object itemAsJsonObject; // This is the net.minecraft.server.ItemStack after being put through saveNmsItem method
        
        try {
            nmsNbtTagCompoundObj = nbtTagCompoundClazz.newInstance();
            nmsItemStackObj = asNMSCopyMethod.invoke(null, itemStack);
            itemAsJsonObject = saveNmsItemStackMethod.invoke(nmsItemStackObj, nmsNbtTagCompoundObj);
        } catch (Throwable t) {
            Bukkit.getLogger().log(Level.SEVERE, "Failed to serialize itemstack to nms item", t);
            return null;
        }
        
        // Return a string representation of the serialized object
        return itemAsJsonObject.toString();
    }
    
    /**
     * Gets the version string for NMS & OBC class paths
     *
     * @return The version string of OBC and NMS packages
     */
    private static String getVersion() {
        if (versionString == null) {
            String name = Bukkit.getServer().getClass().getPackage().getName();
            versionString = name.substring(name.lastIndexOf('.') + 1) + ".";
        }
        
        return versionString;
    }
    
    /**
     * Get an NMS Class
     *
     * @param nmsClassName The name of the class
     * @return The class
     */
    private static Class<?> getNMSClass(String nmsClassName) {
        if (loadedNMSClasses.containsKey(nmsClassName)) {
            return loadedNMSClasses.get(nmsClassName);
        }
        
        String clazzName = "net.minecraft.server." + getVersion() + nmsClassName;
        Class<?> clazz;
        
        try {
            clazz = Class.forName(clazzName);
        } catch (Throwable t) {
            t.printStackTrace();
            return loadedNMSClasses.put(nmsClassName, null);
        }
        
        loadedNMSClasses.put(nmsClassName, clazz);
        return clazz;
    }
    
    /**
     * Get a class from the org.bukkit.craftbukkit package
     *
     * @return the found class at the specified path
     */
    private synchronized static Class<?> getOBCClass() {
        if (loadedOBCClasses.containsKey("inventory.CraftItemStack")) {
            return loadedOBCClasses.get("inventory.CraftItemStack");
        }
        
        String clazzName = "org.bukkit.craftbukkit." + getVersion() + "inventory.CraftItemStack";
        Class<?> clazz;
        
        try {
            clazz = Class.forName(clazzName);
        } catch (Throwable t) {
            t.printStackTrace();
            loadedOBCClasses.put("inventory.CraftItemStack", null);
            return null;
        }
        
        loadedOBCClasses.put("inventory.CraftItemStack", clazz);
        return clazz;
    }
    
    /**
     * Get a method from a class that has the specific paramaters
     *
     * @param clazz      The class we are searching
     * @param methodName The name of the method
     * @param params     Any parameters that the method has
     * @return The method with appropriate paramaters
     */
    private static Method getMethod(Class<?> clazz, String methodName, Class<?>... params) {
        if (!loadedMethods.containsKey(clazz)) {
            loadedMethods.put(clazz, new HashMap<>());
        }
        
        Map<String, Method> methods = loadedMethods.get(clazz);
        
        if (methods.containsKey(methodName)) {
            return methods.get(methodName);
        }
        
        try {
            Method method = clazz.getMethod(methodName, params);
            methods.put(methodName, method);
            loadedMethods.put(clazz, methods);
            return method;
        } catch (Exception e) {
            e.printStackTrace();
            methods.put(methodName, null);
            loadedMethods.put(clazz, methods);
            return null;
        }
    }
    
}
