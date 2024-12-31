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

package katajaLang.compiler.input;

import katajaLang.compiler.CompilerConfig;
import katajaLang.compiler.plugin.PluginManager;

import java.util.Scanner;

public final class InputHandler {

    public static void main(String[] args) {
        new InputHandler(args);
    }

    private final Scanner scanner = new Scanner(System.in);
    private ArgumentHandler argHandler;

    public InputHandler(String[] args) {
        if(args == null || args.length == 0) {
            System.out.println("run '-help' for help");
            nextLine();
        }else{
            argHandler = new ArgumentHandler(args);
            executeNext();
        }
    }

    private void nextLine(){
        System.out.print("> ");

        argHandler = new ArgumentHandler(scanner.nextLine().split(" "));

        executeNext();
    }

    private void executeNext(){
        if(!argHandler.hasNext()){
            nextLine();
            return;
        }

        if(argHandler.hasNextParameter()){
            System.err.println("Expected command, got value '"+argHandler.advance().argument+"'");
        }else{
            String arg = argHandler.advance().argument;
            switch (arg){
                case "-quit":
                case "-q":
                    System.exit(0);
                    break;
                case "-help":
                case "-h":
                    printHelp();
                    break;
                case "-target":
                case "-t":
                    setTarget();
                    break;
                default:
                    System.err.println("Unknown Command '"+arg+"', run '-help' for a list of all valid commands");
                    break;
            }
        }

        nextLine();
    }

    private void printHelp(){
        System.out.println("<---------------Help--------------->");
        System.out.println();
        System.out.println("Available target types:");
        System.out.println("kpp");
        for(String targetType: PluginManager.getTargets()) System.out.println(targetType);
        System.out.println();
        System.out.println("Available commands:");
        System.out.println("-q                  : quit Application");
        System.out.println("-h                  : print help");
        System.out.println("-t <string>         : set target type");
        System.out.println();
        System.out.println("<---------------------------------->");
    }

    private void setTarget(){
        if(argHandler.hasNextParameter()){
            String value = argHandler.advance().argument;

            if(!CompilerConfig.setTargetType(value))  System.err.println("Type '"+value+"' is not supported");
        }else System.err.println("Expected String Value for target option");
    }
}
