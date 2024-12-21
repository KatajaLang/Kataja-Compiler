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

public final class TokenHandler {

    private final Token[][] token;
    private final String file;
    private final int lineOffset;
    private int line;
    private int index;

    public TokenHandler(Token[][] token, String file, int lineOffset){
        this.token = token;
        this.file = file;
        this.lineOffset = lineOffset;
        line = 0;
        index = -1;
    }
}
