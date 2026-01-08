/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

/**
 *
 * @author USER
 */


import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import Service.ElderlyService;
import Service.AuthService;

public class ClientConnector {

    private static Registry getRegistry() throws Exception {
        // Connect to registry on localhost:3000
        return LocateRegistry.getRegistry("localhost", 3000);
    }

    public static ElderlyService elderlyService() throws Exception {
        try {
            return (ElderlyService) getRegistry().lookup("elderlyService");
        } catch (Exception e) {
            throw new Exception("Unable to connect to elderlyService on RMI server: " + e.getMessage(), e);
        }
    }

    public static AuthService authService() throws Exception {
        try {
            return (AuthService) getRegistry().lookup("authService");
        } catch (Exception e) {
            throw new Exception("Unable to connect to authService on RMI server: " + e.getMessage(), e);
        }
    }
}
