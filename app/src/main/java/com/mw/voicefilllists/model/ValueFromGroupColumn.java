package com.mw.voicefilllists.model;

import com.mw.voicefilllists.localdb.entities.ValueGroupDatabaseEntry;

import java.util.ArrayList;
import java.util.List;

public class ValueFromGroupColumn extends DataListColumn implements ColumnWithGrammar {
    public int valueGroupId;

    public List<PhonemeValue> phonemeValues;

    @Override
    public String getGrammarJsgfRow() {
        StringBuilder result = new StringBuilder("public " + getGrammarCommandName() + " = ");

        // get list of possible values
        List<String> possibleValues = new ArrayList<>();
        for (PhonemeValue phonemeValue : phonemeValues) {
            String label = phonemeValue.getLabel();
            if (label != null && phonemeValue.getPhoneticTranscription().length > 0) {
                possibleValues.add(label);
            }
        }

        result.append(String.join(" | ", possibleValues));  // add possible values to grammar string
        result.append(";");
        return result.toString();
    }

    @Override
    public String getGrammarCommandName() {
        return "<value_from_group_" + valueGroupId + ">";
    }

    @Override
    public boolean isPossibleValue(String inputStr) {
        for (PhonemeValue phonemeValue : phonemeValues) {
            String label = phonemeValue.getLabel();
            if (label != null && phonemeValue.getPhoneticTranscription().length > 0) {
                if (label.equalsIgnoreCase(inputStr)) return true;
            }
        }
        return false;
    }
}
