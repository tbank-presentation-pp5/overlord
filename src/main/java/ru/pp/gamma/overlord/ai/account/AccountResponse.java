package ru.pp.gamma.overlord.ai.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AccountResponse {
    private String status;

    @JsonProperty("account_id")
    private String accountId;

    @JsonProperty("ai_token")
    private String aiToken;

    @JsonProperty("neurons_count")
    private Integer neuronsCount;

    private String email;
    private String message;

    public boolean isSuccess() {
        return "success".equals(status);
    }

    public boolean hasNoAccounts() {
        return "no_accounts".equals(status);
    }
}