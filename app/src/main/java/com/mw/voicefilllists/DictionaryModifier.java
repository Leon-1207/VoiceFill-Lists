package com.mw.voicefilllists;

import com.mw.voicefilllists.model.DataListPage;
import com.mw.voicefilllists.model.PhonemeValue;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class DictionaryModifier {
    private File dictionaryFile;

    public DictionaryModifier(File inputDictionaryFile) {
        dictionaryFile = inputDictionaryFile;
    }

    public boolean isWordInDictionary(String word) {
        try (BufferedReader reader = new BufferedReader(new FileReader(dictionaryFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(word + " ")) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void addWordToDictionary(String word, String phoneticTranscription) {
        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(dictionaryFile))) {
            String line;
            boolean added = false;
            while ((line = reader.readLine()) != null) {
                if (!added && line.compareTo(word) > 0) {
                    lines.add(word + " " + phoneticTranscription);
                    added = true;
                }
                lines.add(line);
            }
            if (!added) {
                lines.add(word + " " + phoneticTranscription);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(dictionaryFile))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeWordFromDictionary(String word) {
        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(dictionaryFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.startsWith(word + " ")) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(dictionaryFile))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void copyFile(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        try {
            OutputStream out = new FileOutputStream(dst);
            try {
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            } finally {
                out.close();
            }
        } finally {
            in.close();
        }
    }

    public void addWordToDictionary(PhonemeValue phonemeValue) {
        addWordToDictionary(phonemeValue.getLabel(), String.join(" ", phonemeValue.getPhoneticTranscription()));
    }
}
