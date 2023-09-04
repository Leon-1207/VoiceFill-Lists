package com.mw.voicefilllists.model;

public interface ColumnWithGrammar {
    String getGrammarJsgfRow();

    /**
     * @return name of the command like: "<value_from_group>" as String
     */
    String getGrammarCommandName();

    boolean isPossibleValue(String inputStr);
}
