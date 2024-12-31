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

import katajaLang.model.Class;
import katajaLang.model.Compilable;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public final class KppCompiler {

    //just for testing TODO remove
    public static void main(String[] args) {
        HashMap<String, Compilable> classes = new HashMap<>();

        classes.put("A", new Class("TestA"));
        classes.put("B", new Class("TestA"));
        classes.put("C", new Class("TestB"));

        new KppCompiler().compile(classes, "out");
    }

    private StringBuilder sb;

    public void compile(HashMap<String, Compilable> classes, String outFolder){
        sb = new StringBuilder();

        HashMap<String, HashMap<String, Compilable>> files = groupFiles(classes);

        for(String name: files.keySet()) writeFile(name, files.get(name));

        writeFile(outFolder+"/Program", sb.toString());
    }

    private HashMap<String, HashMap<String, Compilable>> groupFiles(HashMap<String, Compilable> classes){
        HashMap<String, HashMap<String, Compilable>> files = new HashMap<>();

        for(String name: classes.keySet()){
            Compilable clazz = classes.get(name);

            if(!files.containsKey(clazz.srcFile))
                files.put(clazz.srcFile, new HashMap<>());

            files.get(clazz.srcFile).put(name, clazz);
        }

        return files;
    }

    private void writeFile(String name, HashMap<String, Compilable> classes){
        if(sb.length() != 0) sb.append("\n\n");

        sb.append("file \"").append(name).append("\" {");

        for(String clazzName: classes.keySet()) writeClass(clazzName, classes.get(clazzName));

        sb.append("\n}");
    }

    private void writeClass(String name, Compilable clazz){
        if(sb.length() != 0) sb.append("\n\n");

        sb.append("\tclass \"").append(name).append("\" {");

        sb.append("\n\t}");
    }

    private void writeFile(String path, String content) {
        File file = new File(path+".kpp");

        if(file.exists()) file.delete();

        try {
            file.createNewFile();
        }catch (IOException ignored){
            throw new RuntimeException("Failed to create File");
        }

        try {
            FileWriter writer = new FileWriter(file);

            writer.write(String.valueOf(calculateHash(content)));

            if(!content.isEmpty()) {
                writer.write('\n');
                writer.write(content);
            }

            writer.flush();
            writer.close();
        }catch (IOException ignored){
            throw new RuntimeException("Failed to write file");
        }
    }

    public static long calculateHash(String value) {
        long hash = Long.MAX_VALUE;

        byte[] bytes = value.getBytes();

        for (byte b : bytes) {
            hash += b;
            hash ^= (hash << 21);
            hash ^= (hash >> 7);
            hash += (hash << 14);
        }

        return hash;
    }
}
