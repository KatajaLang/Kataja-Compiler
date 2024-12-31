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

import katajaLang.compiler.lexer.Lexer;
import katajaLang.compiler.lexer.TokenHandler;
import katajaLang.model.Compilable;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;

public final class KtjParser {

    private HashMap<String, Compilable> classes;
    private TokenHandler th;
    private Compilable current;
    private String path;
    private String name;

    public HashMap<String, Compilable> parse(File file, String relativePath) throws FileNotFoundException {
        th = new Lexer().lexFile(file);
        classes = new HashMap<>();
        current = null;

        if(0 < file.getPath().length() - file.getName().length() - 1) path = file.getPath().substring(0, file.getPath().length() - file.getName().length() - 1);
        else path = "";
        if(path.startsWith(relativePath.replace("/", "\\"))) path = path.substring(relativePath.length()+1);
        else if(relativePath.endsWith(".ktj")) path = "";
        name = file.getName().substring(0, file.getName().length() - 4);

        return classes;
    }
}
