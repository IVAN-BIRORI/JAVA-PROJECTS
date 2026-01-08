/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
// Server: controller/ServerMain.java
package Controller;


import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import Service_implementation.AuthServiceImpl;
import Service_implementation.ElderlyServiceImpl;
import Service.ElderlyService;

public class ServerMain {
    public static void main(String[] args) {
        try {
            // Start or reuse registry on port 3000
            Registry registry;
            try {
                registry = LocateRegistry.getRegistry(3000);
                registry.list(); // test call
                System.out.println("Connected to existing RMI registry on port 3000");
            } catch (Exception ex) {
                registry = LocateRegistry.createRegistry(3000);
                System.out.println("Started new RMI registry on port 3000");
            }

            // Bind services with clear names
            registry.rebind("authService", new AuthServiceImpl());
            registry.rebind("elderlyService", new ElderlyServiceImpl());

            System.out.println("✅ ElderlyCompanionSystem RMI server running on port 3000");
        } catch (Exception e) {
            System.err.println("❌ Server failed to start: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
