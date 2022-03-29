package org.jeff.rabbitmq.component;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ConfirmCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

/**
 * @author jeff
 * @since 1.0.0
 */
@Component
public class RabbitSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    final ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback() {

        @Override
        public void confirm(CorrelationData correlationData, boolean b, String s) {
            System.err.println("消息ACK结果:" + b + ", correlationData: " + correlationData.getId());
        }
    };


    /**
     * 对外发送消息的方法
     * @param message 具体消息内容
     * @param properties 额外附加属性
     * @throws Exception
     */
    public void send(Object message, Map<String, Object> properties) throws Exception {

        MessageHeaders messageHeaders = new MessageHeaders(properties);
        Message<?> msg = MessageBuilder.createMessage(message, messageHeaders);

        rabbitTemplate.setConfirmCallback(confirmCallback);

        // 指定业务唯一ID
        CorrelationData correlation = new CorrelationData(UUID.randomUUID().toString());

        MessagePostProcessor messagePostProcessor = new MessagePostProcessor() {
            @Override
            public org.springframework.amqp.core.Message postProcessMessage(org.springframework.amqp.core.Message message) throws AmqpException {
                System.err.println("---> post to do: " + message);
                return message;
            }
        };


        rabbitTemplate.convertAndSend("exchange-1", "springboot.rabbitmq",
                msg, messagePostProcessor, correlation);
    }


}



















