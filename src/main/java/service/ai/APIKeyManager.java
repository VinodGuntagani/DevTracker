package service.ai;

import java.util.ArrayList;
import java.util.List;

public class APIKeyManager {

    private final List<String> apiKeys = new ArrayList<>();

    public APIKeyManager() {

        addKey(System.getenv("GEMINI_API_KEY_1"));
        addKey(System.getenv("GEMINI_API_KEY_2"));
        addKey(System.getenv("GEMINI_API_KEY_3"));
        addKey(System.getenv("GEMINI_API_KEY_4"));

    }

    private void addKey(String key) {

        if (key != null && !key.isBlank()) {
            apiKeys.add(key);
        }

    }

    public List<String> getApiKeys() {
        return apiKeys;
    }

}