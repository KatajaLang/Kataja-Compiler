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

package katajaLang.compiler.lexer;

import java.io.PrintStream;

public class LexingException extends RuntimeException {

    private final String file;
    private final int line;
    private final int position;

    LexingException(String message, String file, int line, int position) {
        super(message);
        this.file = file;
        this.line = line;
        this.position = position;
    }

    @Override
    public void printStackTrace(PrintStream s) {
        s.println("LexingException: "+getMessage());
        s.println("\tat " + file+":"+line+":"+position);
    }
}
