package ru.pp.gamma.overlord.ai.account;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.pp.gamma.overlord.ai.account.dto.CfAccount;
import ru.pp.gamma.overlord.ai.cf.text.CfProps;

@Slf4j
@Service
public class CfAccountService {
    private final AccountProviderClient accountProviderClient;
    private final CfProps cfProps;

    private AccountResponse cachedAccount;
    private long lastFetchTime;
    private static final long CACHE_TTL_MS = 60000;

    public CfAccountService(AccountProviderClient accountProviderClient, CfProps cfProps) {
        this.accountProviderClient = accountProviderClient;
        this.cfProps = cfProps;
        log.info("CfAccountService initialized. Fallback account: {}",
                cfProps.getIdAccount() != null ? cfProps.getIdAccount().substring(0, Math.min(8, cfProps.getIdAccount().length())) + "..." : "null");
    }

    public CfAccount getAccount() {
        log.debug("Getting CF account...");

        // Проверяем кэш
        if (cachedAccount != null &&
                System.currentTimeMillis() - lastFetchTime < CACHE_TTL_MS &&
                cachedAccount.isSuccess()) {
            log.debug("Using cached account from Accounter");
            return new CfAccount(cachedAccount.getAccountId(), cachedAccount.getAiToken());
        }

        // Пытаемся получить аккаунт от Accounter'a
        log.debug("Fetching account from Accounter...");
        AccountResponse account = accountProviderClient.getAccount();

        if (account != null && account.isSuccess()) {
            log.info("Using account from Accounter: accountId={}, email={}, neurons={}",
                    account.getAccountId(), account.getEmail(), account.getNeuronsCount());

            cachedAccount = account;
            lastFetchTime = System.currentTimeMillis();

            return new CfAccount(account.getAccountId(), account.getAiToken());
        }

        // Если Accounter не вернул аккаунт, используем из конфигурации
        log.warn("Accounter returned no account. Using fallback from configuration. accountId={}",
                cfProps.getIdAccount());
        return new CfAccount(cfProps.getIdAccount(), cfProps.getAuthToken());
    }
}