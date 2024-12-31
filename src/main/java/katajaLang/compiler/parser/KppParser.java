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

package katajaLang.compiler.parser;

import katajaLang.compiler.KppCompiler;
import katajaLang.compiler.lexer.Lexer;
import katajaLang.compiler.lexer.TokenHandler;
import katajaLang.model.Compilable;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public final class KppParser {

    private TokenHandler th;
    private HashMap<String, Compilable> classes;

    public HashMap<String, Compilable> parse(File file){
        th = new Lexer().lexCode(getFileContent(file), file.getPath(), 0);
        classes = new HashMap<>();

        return classes;
    }

    private String getFileContent(File file){
        Scanner sc;

        try {
            sc = new Scanner(file);
        }catch (IOException ignored){
            throw new RuntimeException("Unable to read file "+file.getAbsolutePath());
        }

        long hash = sc.nextLong();

        sc.nextLine();

        StringBuilder sb = new StringBuilder();

        if(sc.hasNext())
            sb.append(sc.nextLine());

        while(sc.hasNext())
            sb.append("\n").append(sc.nextLine());

        if(hash != KppCompiler.calculateHash(sb.toString()))
            throw new RuntimeException("File "+file.getAbsolutePath()+" is damaged");

        return sb.toString();
    }
}
