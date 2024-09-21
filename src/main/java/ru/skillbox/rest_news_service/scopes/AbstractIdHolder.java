package ru.skillbox.rest_news_service.scopes;

import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public abstract class AbstractIdHolder implements IdHolder {
    private final UUID requestId;

    public AbstractIdHolder() {
        this.requestId = UUID.randomUUID();
    }

    @Override
    public void logId() {
        log.info("{} is: {}", holderType(), requestId);
    }

    abstract String holderType();
}
