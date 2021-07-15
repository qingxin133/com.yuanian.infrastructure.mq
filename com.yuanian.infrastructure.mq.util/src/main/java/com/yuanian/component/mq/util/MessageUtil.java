package com.yuanian.component.mq.util;


import java.util.Map;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.converter.MessageConversionException;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.converter.SmartMessageConverter;
import org.springframework.messaging.core.MessagePostProcessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.MimeTypeUtils;

public class MessageUtil {
    public MessageUtil() {
    }

    public static Message<?> doConvert(MessageConverter converter, Object payload, Map<String, Object> headers, MessagePostProcessor postProcessor) {
        MessageHeaders messageHeaders = null;
        Object conversionHint = headers != null ? headers.get("conversionHint") : null;
        if (headers != null) {
            if (headers instanceof MessageHeaders) {
                messageHeaders = (MessageHeaders)headers;
            } else {
                messageHeaders = new MessageHeaders(headers);
            }
        }

        Message<?> message = converter instanceof SmartMessageConverter ? ((SmartMessageConverter)converter).toMessage(payload, messageHeaders, conversionHint) : converter.toMessage(payload, messageHeaders);
        if (message == null) {
            String payloadType = payload.getClass().getName();
            Object contentType = messageHeaders != null ? messageHeaders.get("contentType") : null;
            throw new MessageConversionException("Unable to convert payload with type='" + payloadType + "', contentType='" + contentType + "', converter=[" + converter + "]");
        } else {
            if (postProcessor != null) {
                message = postProcessor.postProcessMessage(message);
            }

            MessageBuilder<?> builder = MessageBuilder.fromMessage(message);
            builder.setHeaderIfAbsent("contentType", MimeTypeUtils.TEXT_PLAIN);
            return builder.build();
        }
    }
}
