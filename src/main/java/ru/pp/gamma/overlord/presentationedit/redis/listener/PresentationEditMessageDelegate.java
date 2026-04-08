package ru.pp.gamma.overlord.presentationedit.redis.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import ru.pp.gamma.overlord.presentationedit.redis.RedisNotifyMessage;
import ru.pp.gamma.overlord.presentationedit.service.SessionWSStorage;

import java.io.IOException;

/**
 * Listener обновлений презентации
 * <a href="https://docs.spring.io/spring-data/redis/reference/redis/pubsub.html#redis:pubsub:subscribe:containers">...</a>
 */
@RequiredArgsConstructor
@Component
public class PresentationEditMessageDelegate {


    private final SessionWSStorage sessionWSStorage;

    public void handleMessage(RedisNotifyMessage message) {
        sessionWSStorage.wsSessions.get(message.getPresentationId()).forEach(wsSession -> {
            try {
                wsSession.sendMessage(new TextMessage(message.getOriginalTextMessage()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }


}
