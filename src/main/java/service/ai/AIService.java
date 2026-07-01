package service.ai;

import model.AIRequest;
import model.AIResponse;

public class AIService {

    private final GeminiClient geminiClient = new GeminiClient();

    private final ModelFallbackManager modelFallbackManager = new ModelFallbackManager();

    private final APIKeyManager apiKeyManager = new APIKeyManager();

    public AIResponse generate(AIRequest request) {

        System.out.println("\n========================================");
        System.out.println("🤖 DevTracker AI Engine");
        System.out.println("========================================");

        AIResponse response = new AIResponse();

        for (String model : modelFallbackManager.getModels()) {

            request.setPreferredModel(model);

            System.out.println("\n[AI] Trying Model : " + model);

            int keyNumber = 1;

            for (String apiKey : apiKeyManager.getApiKeys()) {

                System.out.println("[AI] Trying API Key #" + keyNumber);

                long start = System.currentTimeMillis();

                response = geminiClient.generate(request, apiKey);

                long end = System.currentTimeMillis();

                System.out.println("[AI] Response Time : " + (end - start) + " ms");

                if (response.isSuccess()) {

                    System.out.println("✅ Success");
                    System.out.println("[AI] Model Used : " + model);
                    System.out.println("[AI] API Key    : #" + keyNumber);
                    System.out.println("========================================\n");

                    return response;
                }

                if (!response.isRetryable()) {

                    System.out.println("🛑 Non-retryable error");
                    System.out.println("[AI] Reason : " + response.getErrorMessage());
                    System.out.println("========================================\n");

                    return response;
                }

                System.out.println("❌ Failed");
                System.out.println("[AI] Reason : " + response.getErrorMessage());
                System.out.println("[AI] Trying next API Key...");

                keyNumber++;
            }

            System.out.println("[AI] Switching to next model...");
        }

        response.setSuccess(false);
        response.setErrorMessage("All AI models and API keys failed.");

        System.out.println("\n❌ All AI models failed.");
        System.out.println("========================================\n");

        return response;
    }

}