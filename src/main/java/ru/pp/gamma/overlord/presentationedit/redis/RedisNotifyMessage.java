package ru.pp.gamma.overlord.presentationedit.redis;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RedisNotifyMessage {

    private String originalTextMessage;
    private Long presentationId;

}
