/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Service;

/**
 *
 * @author USER
 */
// Server & Client: service/AuthService.java


import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AuthService extends Remote {
    String sendOtp(String username, String destination) throws RemoteException;
    String loginWithOtp(String username, String otp) throws RemoteException;
    boolean verifySession(String token) throws RemoteException;
    boolean login(String username, String password) throws RemoteException;  // ✅ FIXED
}
