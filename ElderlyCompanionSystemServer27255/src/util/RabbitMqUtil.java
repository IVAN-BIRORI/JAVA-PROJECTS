/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/*
 * RabbitMqUtil: will attempt to use RabbitMQ client if available at runtime.
 * If the RabbitMQ client library is not on the classpath, the method
 * falls back to logging the OTP to the console so the application can
 * continue running without a hard compile/runtime dependency.
 */
public class RabbitMqUtil {
    public static void sendOtp(String queue, String username, String otp) {
        try {
            // Check if RabbitMQ client is present
            Class.forName("com.rabbitmq.client.ConnectionFactory");
        } catch (ClassNotFoundException e) {
            // RabbitMQ client not available; fallback behaviour
            System.out.println("[RabbitMqUtil] RabbitMQ client not found; OTP for " + username + ": " + otp);
            return;
        }

        try {
            // Use reflection to avoid compile-time dependency
            Class<?> factoryClass = Class.forName("com.rabbitmq.client.ConnectionFactory");
            Object factory = factoryClass.getDeclaredConstructor().newInstance();
            factoryClass.getMethod("setHost", String.class).invoke(factory, "localhost");

            Object connection = factoryClass.getMethod("newConnection").invoke(factory);
            Class<?> connectionClass = Class.forName("com.rabbitmq.client.Connection");
            Object channel = connectionClass.getMethod("createChannel").invoke(connection);

            Class<?> channelClass = Class.forName("com.rabbitmq.client.Channel");
            channelClass.getMethod("queueDeclare", String.class, boolean.class, boolean.class, boolean.class, java.util.Map.class)
                    .invoke(channel, queue, false, false, false, null);

            String message = "OTP for " + username + " is: " + otp;
            channelClass.getMethod("basicPublish", String.class, String.class, Class.forName("com.rabbitmq.client.AMQP$BasicProperties"), byte[].class)
                    .invoke(channel, "", queue, null, message.getBytes());

            // close channel and connection
            channelClass.getMethod("close").invoke(channel);
            connectionClass.getMethod("close").invoke(connection);
        } catch (Exception ex) {
            // If anything goes wrong, fall back to logging
            System.out.println("[RabbitMqUtil] Failed to send via RabbitMQ: " + ex.getMessage());
            System.out.println("[RabbitMqUtil] OTP for " + username + ": " + otp);
        }
    }
}
