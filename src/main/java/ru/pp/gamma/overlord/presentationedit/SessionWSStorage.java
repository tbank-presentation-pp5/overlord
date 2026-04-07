package ru.pp.gamma.overlord.presentationedit;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionWSStorage {

    // presentationId --> list of sessions
    public Map<Long, List<WebSocketSession>> wsSessions = new ConcurrentHashMap<>();

}
