package service.ai;

import java.util.List;

public class ModelFallbackManager {

    private final List<String> models = List.of(
            "gemini-2.5-flash",
            "gemini-2.5-flash-lite",
            "gemini-2.0-flash"
    );

    public List<String> getModels() {
        return models;
    }

}