package service.ai;

import model.AIRequest;
import model.AIResponse;

public interface AIProvider {

    AIResponse generate(AIRequest request, String apiKey);

}