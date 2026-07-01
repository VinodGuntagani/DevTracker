package model;

public class AIRequest {

    private String prompt;
    private String preferredModel;

    public AIRequest() {
    }

    public AIRequest(String prompt, String preferredModel) {
        this.prompt = prompt;
        this.preferredModel = preferredModel;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getPreferredModel() {
        return preferredModel;
    }

    public void setPreferredModel(String preferredModel) {
        this.preferredModel = preferredModel;
    }

}