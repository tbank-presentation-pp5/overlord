package ru.pp.gamma.overlord.presentationedit.ws.interceptor;

import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import ru.pp.gamma.overlord.presentation.service.PresentationService;
import ru.pp.gamma.overlord.presentationedit.service.SessionWSStorage;

import java.util.Map;

import static ru.pp.gamma.overlord.common.util.CommonUtil.isLongNumber;
import static ru.pp.gamma.overlord.presentationedit.util.PresentationEditConsts.PRESENTATION_ID_ATTRIBUTE;

@RequiredArgsConstructor
@Component
public class ResolvePresentationWsInterceptor implements HandshakeInterceptor {

    private static final String PRESENTATION_ID_QUERY = "presentationId";

    private final PresentationService presentationService;
    private final SessionWSStorage sessionWSStorage;

    @Override
    public boolean beforeHandshake(
            @NonNull ServerHttpRequest request,
            @NonNull ServerHttpResponse response,
            @NonNull WebSocketHandler wsHandler,
            @NonNull Map<String, Object> attributes
    ) throws Exception {
        Long presentationId = resolvePresentationId(request);

        if (presentationId == null) {
            response.setStatusCode(HttpStatus.BAD_REQUEST);
            return false;
        }

        if (!presentationService.existsById(presentationId)) {
            response.setStatusCode(HttpStatus.NOT_FOUND);
            return false;
        }

        attributes.put(PRESENTATION_ID_ATTRIBUTE, presentationId);

        return true;
    }

    @Override
    public void afterHandshake(
            @NonNull ServerHttpRequest request,
            @NonNull ServerHttpResponse response,
            @NonNull WebSocketHandler wsHandler,
            @Nullable Exception exception
    ){
    }

    private @Nullable Long resolvePresentationId(ServerHttpRequest request) {
        String query = request.getURI().getQuery();
        if (query == null) {
            return null;
        }

        String[] queryPairs = query.split("&");
        for (String queryPair : queryPairs) {
            String[] pair = queryPair.split("=");

            if (pair.length == 2 && PRESENTATION_ID_QUERY.equals(pair[0]) && isLongNumber(pair[1])) {
                return Long.parseLong(pair[1]);
            }
        }

        return null;
    }
}
