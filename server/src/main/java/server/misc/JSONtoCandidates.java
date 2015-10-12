package server.misc;

import com.google.gson.Gson;
import server.model.entities.Candidate;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by luisburgos on 2/09/15.
 */
public class JSONtoCandidates {

    private static String FILE_PATH = "src/main/java/candidates.json";

    public static ArrayList<Candidate> loadCandidates() {

        Gson gson = new Gson();
        Candidate[] result = null;
        ArrayList<Candidate> candidates = null;

        try {
            BufferedReader br = new BufferedReader(new FileReader(FILE_PATH));
            result = gson.fromJson(br, Candidate[].class);
            candidates = new ArrayList<Candidate>();
            candidates.addAll(Arrays.asList(result));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return candidates;
    }
}
