package org.jeff.rabbitmq.component;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author jeff
 * @since 1.0.0
 */
@Component
public class RabbitService {


    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${spring.rabbitmq.listener.demo.queue.name}", durable = "${spring.rabbitmq.listener.demo.queue.durable}"),
            exchange = @Exchange(name = "${spring.rabbitmq.listener.demo.exchange.name}",
                    durable = "${spring.rabbitmq.listener.demo.exchange.durable}",
                    type = "${spring.rabbitmq.listener.demo.exchange.type}",
                    ignoreDeclarationExceptions = "true"),
            key = "${spring.rabbitmq.listener.demo.exchange.key}"
    )
    )
    @RabbitHandler
    public void onMessage(Message message, Channel channel) throws IOException {
        // step 1. 收到消息进行业务处理
        System.err.println("-----------------------");
        System.err.println("消费消息:" + message.getPayload());

        // step2. 配置手动ack模式,处理完成后返回ack签收
        Long deliveryTag = (Long) message.getHeaders().get(AmqpHeaders.DELIVERY_TAG);
        channel.basicAck(deliveryTag, false);
    }


}



















