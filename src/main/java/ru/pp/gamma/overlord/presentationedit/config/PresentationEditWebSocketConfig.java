package ru.pp.gamma.overlord.presentationedit.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import ru.pp.gamma.overlord.presentationedit.ws.handler.PresentationEditWSHandler;
import ru.pp.gamma.overlord.presentationedit.ws.interceptor.ResolvePresentationWsInterceptor;

@RequiredArgsConstructor
@Configuration
@EnableWebSocket
public class PresentationEditWebSocketConfig implements WebSocketConfigurer {

    private final PresentationEditWSHandler presentationEditWSHandler;
    private final ResolvePresentationWsInterceptor resolvePresentationWsInterceptor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(presentationEditWSHandler, "/ws/presentation/changes")
                .addInterceptors(resolvePresentationWsInterceptor)
                .setAllowedOrigins("*");
    }

}
