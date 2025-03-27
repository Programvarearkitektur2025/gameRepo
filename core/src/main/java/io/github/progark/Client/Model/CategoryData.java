package io.github.progark.Client.Model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.util.*;

public class CategoryData {

    public static class Category {
        public String id;
        public String title;
        public Map<String, Integer> answers;

        public boolean isCorrect(String answer) {
            return answers.containsKey(answer.trim().toLowerCase());
        }

        public int getPoints(String answer) {
            return answers.getOrDefault(answer.trim().toLowerCase(), 0);
        }

        public Map<String, Integer> getAllAnswers(){
            return this.answers;
        }
    }

    private static final Map<String, Category> categories = new HashMap<>();

    public static void loadFromFile(String path) {
        FileHandle file = Gdx.files.internal(path);
        Json json = new Json();
        JsonValue root = new JsonReader().parse(file);

        for (JsonValue entry = root.child; entry != null; entry = entry.next) {
            String id = entry.name;
            String title = entry.getString("title");
            JsonValue answersJson = entry.get("answers");

            Map<String, Integer> answers = new HashMap<>();
            for (JsonValue ans = answersJson.child; ans != null; ans = ans.next) {
                answers.put(ans.name.toLowerCase(), ans.asInt());
            }

            Category category = new Category();
            category.id = id;
            category.title = title;
            category.answers = answers;

            categories.put(id.toLowerCase(), category);
        }
    }

    public static Category getCategory(String id) {
        return categories.get(id.toLowerCase());
    }

    public static Set<String> getAvailableCategoryIds() {
        return categories.keySet();
    }
}
