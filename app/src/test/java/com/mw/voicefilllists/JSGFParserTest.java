package com.mw.voicefilllists;

import junit.framework.TestCase;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JSGFParserTest extends TestCase {
    String grammarVersion1 = "#JSGF V1.0;\n" +
            "grammar dynamic;\n" +
            "public <state> = open | close;\n" +
            "public <command> = <state> the (door|window);";
    String grammarVersion2 = "#JSGF V1.0;\n" +
            "grammar dynamic;\n" +
            "public <state> = open | close ;\n" +
            "public <command> = <state> the (door|window);";
    String grammarVersion3 = "#JSGF V1.0;\n" +
            "grammar dynamic;\n" +
            "public <state> = open | close ;\n" +
            "public <command> = <state> the (door | window);";
    String grammarVersion4 = "#JSGF V1.0;\n" +
            "grammar dynamic;\n" +
            "public <state> = open | close ;\n" +
            "public <command> = <state> the (door | window) ;";

    public void testCheck() {
        List<String> grammarList = Arrays.asList(grammarVersion1, grammarVersion2, grammarVersion3, grammarVersion4);
        for (String grammar : grammarList) {
            assertFalse(JSGFParser.check("", grammar));
            assertTrue(JSGFParser.check("open the door", grammar));
            assertTrue(JSGFParser.check("open the window", grammar));
            assertTrue(JSGFParser.check("close the door", grammar));
            assertTrue(JSGFParser.check("close the window", grammar));
            assertFalse(JSGFParser.check("close window", grammar));
            assertFalse(JSGFParser.check("close", grammar));
            assertFalse(JSGFParser.check("open", grammar));
            assertFalse(JSGFParser.check("clxse the window", grammar));
        }
    }


    public void testGetSubcommandMap() {
        List<String> grammarList = Arrays.asList(grammarVersion1, grammarVersion2, grammarVersion3, grammarVersion4);
        for (String grammar : grammarList) {
            Map<String, String> output = JSGFParser.getSubcommandMap(grammar);

            Map<String, String> expectedOutput = new HashMap<>();
            expectedOutput.put("state", "open | close");
            expectedOutput.put("command", "<state> the (door|window)");
        }
    }

    public void testRemoveSubcommand() {
        String result = JSGFParser.removeSubcommand("public <state> = open | close; public <command> = <state> the (door|window);", "state");
        assertEquals("public <command> = <state> the (door|window);", result);
    }

    public void testExtractCommandRule() {
        String result = JSGFParser.extractCommandRule("public <command> = (open | close) the (door|window);");
        assertEquals("(open | close) the (door|window)", result);
    }

    public void testFindSubcommandMatches() {
        String grammar = "#JSGF V1.0;\n" +
                "grammar dynamic;\n" +
                "public <name> = Tim | Max | Leon;\n" +
                "public <task> = code | debug | plan;\n" +
                "public <command> = <name> <task> <task>;";

        assertTrue(JSGFParser.check("Max code plan", grammar));
        assertTrue(JSGFParser.check("Leon plan debug", grammar));

        Map<String, List<String>> substringMatches = JSGFParser.findSubcommandMatches("Leon plan debug", grammar);
        System.out.println(substringMatches);
    }

    public void testCheck2() {
        String grammar = "#JSGF V1.0;\n" +
                "grammar dynamic;\n" +
                "public <name> = Tim | Max | Leon;\n" +
                "public <task> = code | debug | plan;\n" +
                "public <name2> = Aaron | Leon;\n" +
                "public <command> = <name> <task> <name2>;";

        for (String name1 : Arrays.asList("Tim", "Max", "Leon")) {
            for (String task : Arrays.asList("code", "debug", "plan")) {
                for (String name2 : Arrays.asList("Aaron", "Leon")) {
                    System.out.println(name1 + " " + task + " " + name2 + " ---> " + JSGFParser.findSubcommandMatches(name1 + " " + task + " " + name2, grammar));
                    assertTrue(JSGFParser.check(name1 + " " + task + " " + name2, grammar));
                }
            }
        }
    }

    public void testParseData() {
        String grammar = "#JSGF V1.0;\n" +
                "grammar dynamic;\n" +
                "public <name> = Tim | Max | Leon;\n" +
                "public <task> = code | debug | plan;\n" +
                "public <name2> = Aaron | Leon;\n" +
                "public <command> = <name> <task> <name2>;";

        for (String name1 : Arrays.asList("Tim", "Max", "Leon")) {
            for (String task : Arrays.asList("code", "debug", "plan")) {
                for (String name2 : Arrays.asList("Aaron", "Leon")) {
                    assertEquals(Arrays.asList(name1, task, name2), JSGFParser.parseData(name1 + " " + task + " " + name2, grammar));
                }
            }
        }
        assert JSGFParser.parseData("Leon plan", grammar) == null;
    }
}