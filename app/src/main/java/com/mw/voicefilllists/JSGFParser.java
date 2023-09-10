package com.mw.voicefilllists;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JSGFParser {

    public static boolean check(String inputString, String jsgfGrammar) {
        String modifiedGrammar = preprocessGrammar(jsgfGrammar, false);
        return inputString.matches(modifiedGrammar);
    }

    private static String preprocessGrammar(String jsgfGrammar, boolean isSubcommandPattern) {
        Map<String, String> subcommandMap = getSubcommandMap(jsgfGrammar);

        // Remove lines starting with '#' and containing "grammar"
        jsgfGrammar = jsgfGrammar.replaceAll("(#.*\\n)|(.*grammar.*)\\n", "");

        // Allow for flexible spacing around operators and elements
        jsgfGrammar = jsgfGrammar.replaceAll("\\s+", " ");

        // remove spaces before and after or operators `|`
        jsgfGrammar = jsgfGrammar.replaceAll(" \\| ", "|");
        jsgfGrammar = jsgfGrammar.replaceAll("\\| ", "|");
        jsgfGrammar = jsgfGrammar.replaceAll(" \\|", "|");

        // Handle custom subcommands
        Pattern pattern = Pattern.compile("<(\\w+?)>");
        Matcher matcher = pattern.matcher(jsgfGrammar);

        jsgfGrammar = jsgfGrammar.replaceAll("public <command>", "public ___command___");

        while (matcher.find()) {
            String subcommand = matcher.group(1);
            assert subcommand != null;
            if (!subcommand.equals("command")) {
                String subcommandPattern = preprocessGrammar(subcommandMap.get(subcommand), true);
                subcommandPattern = subcommandPattern.substring(1, subcommandPattern.length() - 1);
                jsgfGrammar = removeSubcommand(jsgfGrammar, subcommand);
                jsgfGrammar = jsgfGrammar.replaceAll("<" + subcommand + ">", "(" + subcommandPattern + ")");
            }
        }

        // extract main command rule (public <command> = ...)
        if (!isSubcommandPattern) jsgfGrammar = extractCommandRule(jsgfGrammar);

        assert jsgfGrammar != null;
        return "^" + jsgfGrammar.trim() + "$";
    }

    public static String removeSubcommand(String input, String subcommand) {
        // Define the regular expression pattern
        List<String> patterns = Arrays.asList("public <" + subcommand + "> = .*?; ", "public <" + subcommand + "> = .*?;");

        String result = input;
        for (String pattern : patterns) {
            Pattern r = Pattern.compile(pattern, Pattern.DOTALL);

            // Create a matcher object
            Matcher matcher = r.matcher(result);

            // Replace all matches with an empty string
            result = matcher.replaceAll("");
        }

        return result;
    }

    public static String extractCommandRule(String input) {
        input = input.replaceAll("<command>", "___command___");

        // Define the regular expression pattern
        String pattern = "public ___command___ = (.*?)\\;";
        Pattern r = Pattern.compile(pattern);

        // Create a matcher object
        Matcher matcher = r.matcher(input);

        // If a match is found, extract the command rule
        if (matcher.find()) {
            return matcher.group(1);
        }

        return null; // Return null if no match is found
    }

    public static Map<String, String> getSubcommandMap(String jsgfGrammar) {
        Map<String, String> map = new HashMap<>();
        if (jsgfGrammar == null || jsgfGrammar.isEmpty()) return map;

        Pattern pattern = Pattern.compile("public <(\\w+?)>\\s*=\\s*(.+?)\\s*;");
        Matcher matcher = pattern.matcher(jsgfGrammar);

        while (matcher.find()) {
            String subcommand = matcher.group(1);
            String rule = matcher.group(2);
            map.put(subcommand, rule);
        }

        return map;
    }

    public static Map<String, List<String>> findSubcommandMatches(String inputString, String jsgfGrammar) {
        Map<String, List<String>> matches = new HashMap<>();

        Map<String, String> subcommandMap = getSubcommandMap(jsgfGrammar);

        Pattern pattern = Pattern.compile("<(\\w+?)>");
        Matcher matcher = pattern.matcher(jsgfGrammar);

        while (matcher.find()) {
            String subcommand = matcher.group(1);
            if (!subcommand.equals("command")) {
                String subcommandPattern = preprocessGrammar(subcommandMap.get(subcommand), true);
                subcommandPattern = subcommandPattern.substring(1, subcommandPattern.length() - 1);

                List<String> subcommandMatches = findMatches(inputString, subcommandPattern);
                matches.put(subcommand, subcommandMatches);
            }
        }

        return matches;
    }

    private static List<String> findMatches(String inputString, String pattern) {
        List<String> matches = new ArrayList<>();

        Pattern r = Pattern.compile(pattern);
        Matcher matcher = r.matcher(inputString);

        while (matcher.find()) {
            matches.add(matcher.group());
        }

        return matches;
    }

    public static List<String> parseData(String inputString, String jsgfGrammar) {
        if (check(inputString, jsgfGrammar)) {
            // matches --> extract data
            String commandRule = extractCommandRule(jsgfGrammar);
            List<String> subcommands = new ArrayList<>();
            Pattern pattern = Pattern.compile("<(\\w+?)>");
            Matcher matcher = pattern.matcher(commandRule);
            while (matcher.find()) {
                String subcommand = matcher.group(1);
                subcommands.add(subcommand);
            }

            List<String> result = new ArrayList<>();
            Map<String, List<String>> subcommandMatches = findSubcommandMatches(inputString, jsgfGrammar);
            Map<String, Integer> matchesNextIndex = new HashMap<>();
            for (String subcommand : subcommands) matchesNextIndex.put(subcommand, 0);
            for (String subcommand : subcommands) {
                List<String> matchesForSubcommand = subcommandMatches.get(subcommand);
                int matchIndex = matchesNextIndex.get(subcommand);
                assert matchesForSubcommand != null;
                result.add(matchesForSubcommand.get(matchIndex));
                matchesNextIndex.put(subcommand, matchIndex + 1);
            }

            return result;
        } else {
            return null;
        }
    }
}
