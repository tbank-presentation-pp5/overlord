package ru.pp.gamma.overlord.presentationedit.redis.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import ru.pp.gamma.overlord.presentationedit.redis.RedisNotifyMessage;

import static ru.pp.gamma.overlord.presentationedit.redis.EditPresentationRedisConsts.REDIS_NOTIFICATION_TOPIC;

@Configuration
public class PresentationEditMessageListenerConfig {
    @Bean
    public RedisMessageListenerContainer presentationEditMessageListenerContainer(
            RedisConnectionFactory redisConnectionFactory,
            MessageListenerAdapter messageListenerAdapter
    ) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        container.addMessageListener(messageListenerAdapter, Topic.channel(REDIS_NOTIFICATION_TOPIC));
        return container;
    }

    @Bean
    public MessageListenerAdapter presentationEditMessageListenerAdapter(PresentationEditMessageDelegate presentationEditMessageDelegate) {
        MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(presentationEditMessageDelegate, "handleMessage");
        messageListenerAdapter.setSerializer(new Jackson2JsonRedisSerializer<>(new ObjectMapper(), RedisNotifyMessage.class));
        return messageListenerAdapter;
    }
}
