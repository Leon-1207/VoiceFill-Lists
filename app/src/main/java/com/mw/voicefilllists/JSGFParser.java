package com.mw.voicefilllists;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JSGFParser {

    public static boolean check(String inputString, String jsgfGrammar) {
        String modifiedGrammar = preprocessGrammar(jsgfGrammar);
        return inputString.matches(modifiedGrammar);
    }

    private static String preprocessGrammar(String jsgfGrammar) {
        Map<String, String> subcommandMap = getSubcommandMap(jsgfGrammar);

        // Escape special characters
        jsgfGrammar = jsgfGrammar.replaceAll("([()|])", "\\\\$1");

        // Convert parentheses and OR operator to regex syntax
        jsgfGrammar = jsgfGrammar.replaceAll("\\(", "(");
        jsgfGrammar = jsgfGrammar.replaceAll("\\)", ")");
        jsgfGrammar = jsgfGrammar.replaceAll("\\|", "|");

        // Replace JSGF-specific syntax with regex equivalents
        jsgfGrammar = jsgfGrammar.replaceAll("<VAR>", "(\\\\w+)");
        jsgfGrammar = jsgfGrammar.replaceAll("<WILDCARD>", "(\\\\w+)");
        jsgfGrammar = jsgfGrammar.replaceAll("<NULL>", "");
        jsgfGrammar = jsgfGrammar.replaceAll("<VOID>", "");
        jsgfGrammar = jsgfGrammar.replaceAll("<SILENCE>", "");
        jsgfGrammar = jsgfGrammar.replaceAll("<DICTATION>", "(.*)");

        // Handle custom subcommands
        Pattern pattern = Pattern.compile("<(\\w+?)>");
        Matcher matcher = pattern.matcher(jsgfGrammar);

        while (matcher.find()) {
            String subcommand = matcher.group(1);
            String subcommandPattern = preprocessGrammar(subcommandMap.get(subcommand));
            jsgfGrammar = jsgfGrammar.replaceAll("<" + subcommand + ">", "(" + subcommandPattern + ")");
        }

        // Remove lines starting with '#' and containing "grammar"
        jsgfGrammar = jsgfGrammar.replaceAll("(#.*\\n)|(.*grammar.*)\\n", "");

        // Allow for flexible spacing around operators and elements
        jsgfGrammar = jsgfGrammar.replaceAll("\\s*\\|\\s*", "|");
        jsgfGrammar = jsgfGrammar.replaceAll("\\s*;\\s*", ";");

        return "^" + jsgfGrammar + "$";
    }

    public static Map<String, String> getSubcommandMap(String jsgfGrammar) {
        Map<String, String> map = new HashMap<>();
        Pattern pattern = Pattern.compile("public <(\\w+?)>\\s*=\\s*(.+?)\\s*;");
        Matcher matcher = pattern.matcher(jsgfGrammar);

        while (matcher.find()) {
            String subcommand = matcher.group(1);
            String rule = matcher.group(2);
            map.put(subcommand, rule);
        }

        return map;
    }
}
