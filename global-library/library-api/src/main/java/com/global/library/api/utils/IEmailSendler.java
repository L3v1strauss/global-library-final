package com.global.library.api.utils;

import com.global.library.entity.User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface IEmailSendler {

    void sendSimpleMessage (String to);

    void sendMessageWithActivationInstruction (User user, String subject);

    void sendMessageWithNewPassword (User user, String subject, String newPassword);

}
