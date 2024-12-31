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

package katajaLang.compiler;

import katajaLang.compiler.parser.KppParser;
import katajaLang.compiler.parser.KtjParser;
import katajaLang.model.Compilable;

import java.io.File;
import java.util.HashMap;

public final class Compiler {

    private static HashMap<String, Compilable> libClasses;
    private static HashMap<String, Compilable> classes;

    public static void compile(String...paths){
        if(CompilerConfig.getTargetType() == null) throw new RuntimeException("No target type specified");

        libClasses = new HashMap<>();
        classes = new HashMap<>();

        for(String path:paths){
            File file = new File(path);

            if(!file.exists())
                throw new RuntimeException("Unable to find "+path);

            if(path.endsWith(".ktj") || path.endsWith(".kpp"))
                compileFile(file, file.getPath());
            else if(file.isDirectory())
                compileFolder(file, file.getPath());
            else
                throw new RuntimeException("Expected .ktj or .kpp file");
        }
    }

    private static void compileFile(File file, String relative){
        HashMap<String, Compilable> parsedClasses;

        if(file.getAbsolutePath().endsWith(".ktj")) {
            try {
                parsedClasses = new KtjParser().parse(file, relative);
            }catch (Exception e){
                throw new RuntimeException(e);
            }
        }else
            parsedClasses = new KppParser().parse(file);

        for(String name:parsedClasses.keySet()){
            if(classes.containsKey(name) || libClasses.containsKey(name))
                throw new RuntimeException("Class "+name+" is already defined");

            classes.put(name, parsedClasses.get(name));
        }
    }

    private static void compileFolder(File folder, String relative){
        for(File file:folder.listFiles()){
            if(file.isDirectory()) compileFolder(file, relative);
            else if(file.getName().endsWith(".ktj") || file.getName().endsWith(".kpp")) compileFile(file, relative);
        }
    }

    private void deleteFolder(File folder, boolean delete){
        for(File file:folder.listFiles()){
            if(file.isDirectory()) deleteFolder(file, true);
            else if(!file.delete()) throw new RuntimeException("Failed to delete "+file.getPath());
        }
        if(delete && !folder.delete()) throw new RuntimeException("Failed to delete "+folder.getPath());
    }
}
