/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 *
 * @author USER
 */

import java.io.FileWriter;
import java.io.IOException;

public class NotificationUtil {
    /**
     * Send OTP notification via RabbitMQ if possible, otherwise log to console/file.
     * @param destination email or phone number
     * @param otp generated one-time password
     */
    public static void notifyUser(String destination, String otp) {
        // Try to send via RabbitMQ (queue name logical for exam requirements)
        try {
            RabbitMqUtil.sendOtp("elderly_otp_queue", destination, otp);
        } catch (Throwable t) {
            // Any failure falls back to local logging
            System.out.println("[NotificationUtil] Fallback, RabbitMQ failed: " + t.getMessage());
        }

        // Always log to console
        System.out.println("OTP for " + destination + " is: " + otp);

        // Save to file (simple audit trail)
        try (FileWriter fw = new FileWriter("otp_log.txt", true)) {
            fw.write("Destination: " + destination + " | OTP: " + otp + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
