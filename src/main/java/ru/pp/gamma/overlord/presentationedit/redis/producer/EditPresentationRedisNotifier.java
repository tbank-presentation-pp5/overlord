package ru.pp.gamma.overlord.presentationedit.redis.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import ru.pp.gamma.overlord.presentationedit.redis.RedisNotifyMessage;
import ru.pp.gamma.overlord.presentationedit.ws.message.common.PresentationEditBaseMessage;

import static ru.pp.gamma.overlord.presentationedit.redis.EditPresentationRedisConsts.REDIS_NOTIFICATION_TOPIC;

/**
 * Механизм pub/sub в Redis. В классе реализована отправка сообщений
 */
@RequiredArgsConstructor
@Component
public class EditPresentationRedisNotifier {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final RedisTemplate<Object, Object> redisOperations;

    public void byPresentationId(long presentationId, PresentationEditBaseMessage message) {
        try {
            redisOperations.convertAndSend(REDIS_NOTIFICATION_TOPIC, new RedisNotifyMessage(OBJECT_MAPPER.writeValueAsString(message), presentationId));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
