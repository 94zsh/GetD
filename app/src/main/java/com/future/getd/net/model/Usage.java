package com.future.getd.net.model;

public class Usage {
    /**
     * The number of prompt tokens used.
     */
    long prompt_tokens;

    /**
     * The number of completion tokens used.
     */
    long completion_tokens;

    /**
     * The number of total tokens used
     */
    long total_tokens;

    public long getPrompt_tokens() {
        return prompt_tokens;
    }

    public void setPrompt_tokens(long prompt_tokens) {
        this.prompt_tokens = prompt_tokens;
    }

    public long getCompletion_tokens() {
        return completion_tokens;
    }

    public void setCompletion_tokens(long completion_tokens) {
        this.completion_tokens = completion_tokens;
    }

    public long getTotal_tokens() {
        return total_tokens;
    }

    public void setTotal_tokens(long total_tokens) {
        this.total_tokens = total_tokens;
    }
}
