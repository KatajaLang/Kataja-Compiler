/*
 * Copyright (C) ni271828mand
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License 3.0 as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package katajaLang.compiler.plugin;

import katajaLang.model.Compilable;
import katajaLang.model.Interface;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public final class PluginManager {

    private static final HashMap<String, String> plugins = new HashMap<>();
    private static Interface loadedPlugin = null;

    public static void setUp(){
        Path dir;
        try {
            dir = Paths.get(PluginManager.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent();
        } catch (Exception ignored) {
            throw new RuntimeException("Failed to load Plugins");
        }

        for(File file: Objects.requireNonNull(dir.toFile().listFiles())){
            if(file.isFile() && file.getPath().endsWith(".jar")){
                Interface plugin = loadPlugin(file);

                if(plugin != null) for(String target:plugin.getTargetTypes()) if(!plugins.containsKey(target)) plugins.put(target, file.getAbsolutePath());
            }
        }
    }

    public static String[] getTargets(){
        return plugins.keySet().toArray(new String[0]);
    }

    public static boolean loadPlugin(String target){
        if(plugins.containsKey(target)){
            loadedPlugin = loadPlugin(new File(plugins.get(target)));
            return true;
        }

        return false;
    }

    public static HashMap<String, Compilable> loadLibrary(String path){
        if(loadedPlugin == null) throw new RuntimeException("No plugin loaded");

        return loadedPlugin.loadLibrary(path);
    }

    public static void compileResult(HashMap<String, Compilable> classes){
        if(loadedPlugin == null) throw new RuntimeException("No plugin loaded");

        loadedPlugin.compile(classes, null, null); //TODO///////////////////////////////////////////////////////////////////////////////////////////////////
    }

    private static Interface[] loadPlugins(Path dir){
        ArrayList<Interface> result = new ArrayList<>();

        for(File file: Objects.requireNonNull(dir.toFile().listFiles())){
            if(file.isFile() && file.getPath().endsWith(".jar")){
                Interface plugin = loadPlugin(file);

                if(plugin != null) result.add(plugin);
            }
        }

        return result.toArray(new Interface[0]);
    }

    private static Interface loadPlugin(File file){
        try{
            JarFile jarFile = new JarFile(file);
            URLClassLoader classLoader = new URLClassLoader(new URL[]{file.toURI().toURL()});

            Enumeration<JarEntry> entries = jarFile.entries();
            while(entries.hasMoreElements()){
                JarEntry entry = entries.nextElement();

                if(entry.getName().endsWith(".class")){
                    try{
                        Class<?> clazz = classLoader.loadClass(entry.getName().replace(".class", "").replace("/", "."));

                        if(clazz.getInterfaces().length == 1 && clazz.getInterfaces()[0] == Interface.class)
                            return (Interface) clazz.newInstance();
                    }catch(ClassNotFoundException | InstantiationException | IllegalAccessException ignored){}
                }
            }

            classLoader.close();
            jarFile.close();
        }catch (Exception ignored){}

        return null;
    }
}
