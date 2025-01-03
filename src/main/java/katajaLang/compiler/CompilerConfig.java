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

import katajaLang.compiler.plugin.PluginManager;

public final class CompilerConfig {

    private static String targetType = null;

    public static boolean setTargetType(String type){
        if(type.equals("kpp") || PluginManager.loadPlugin(type)){
            targetType = type;
            return true;
        }

        return false;
    }

    public static String getTargetType(){
        return targetType;
    }
}
