package com.mw.voicefilllists;

import android.util.Log;

import junit.framework.TestCase;

import java.util.ArrayList;
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
}