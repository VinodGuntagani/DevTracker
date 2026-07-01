package model;

public class AIResponse {

	private boolean success;
	private String response;
	private String modelUsed;
	private String errorMessage;
	private boolean retryable;

	public AIResponse() {
	}

	public AIResponse(boolean success, boolean retryable, String response, String modelUsed, String errorMessage) {

		this.success = success;
		this.retryable = retryable;
		this.response = response;
		this.modelUsed = modelUsed;
		this.errorMessage = errorMessage;
	}

	public boolean isRetryable() {
		return retryable;
	}

	public void setRetryable(boolean retryable) {
		this.retryable = retryable;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public String getModelUsed() {
		return modelUsed;
	}

	public void setModelUsed(String modelUsed) {
		this.modelUsed = modelUsed;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}