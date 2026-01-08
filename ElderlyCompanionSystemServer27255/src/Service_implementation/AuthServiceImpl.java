/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Service_implementation;

/**
 *
 * @author USER
 */


import Service.AuthService;
import dao.SessionTokenDao;
import model.SessionToken;
import util.OtpUtil;
import util.NotificationUtil;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AuthServiceImpl extends UnicastRemoteObject implements AuthService {
    private String lastOtp = null;
    private SessionTokenDao sessionTokenDao;
    private List<SessionToken> inMemoryTokens = new ArrayList<>();

    public AuthServiceImpl() throws RemoteException {
        super();
        try {
            sessionTokenDao = new SessionTokenDao();
        } catch (Exception e) {
            System.out.println("SessionTokenDao initialization failed, using in-memory storage: " + e.getMessage());
            sessionTokenDao = null;
        }
    }

    @Override
    public String sendOtp(String username, String destination) throws RemoteException {
        lastOtp = OtpUtil.generateOtp(6);
        NotificationUtil.notifyUser(destination, lastOtp);
        return lastOtp; // For testing, return the OTP to client as well
    }

    @Override
    public String loginWithOtp(String username, String otp) throws RemoteException {
        if (otp == null || !otp.equals(lastOtp)) {
            throw new RemoteException("Invalid OTP");
        }
        String tokenValue = "SESSION-" + System.currentTimeMillis();

        SessionToken token = new SessionToken();
        token.setToken(tokenValue);
        token.setUsername(username);
        token.setExpiresAt(LocalDateTime.now().plusMinutes(30));

        if (sessionTokenDao != null) {
            sessionTokenDao.save(token);
        } else {
            inMemoryTokens.add(token);
        }

        return tokenValue;
    }

    @Override
    public boolean verifySession(String token) throws RemoteException {
        if (sessionTokenDao != null) {
            return sessionTokenDao.isValid(token);
        } else {
            // Check in-memory tokens
            return inMemoryTokens.stream()
                    .anyMatch(t -> t.getToken().equals(token) &&
                            t.getExpiresAt() != null &&
                            t.getExpiresAt().isAfter(LocalDateTime.now()));
        }
    }

    @Override
    public boolean login(String username, String password) throws RemoteException {
        // Very simple password rule for demo: password must be at least 4 chars and equal to "1234"
        if (password == null || password.length() < 4) {
            return false;
        }
        boolean ok = "1234".equals(password);
        if (ok) {
            // also create a session token for password-based login
            String tokenValue = "PASSWORD-SESSION-" + System.currentTimeMillis();
            SessionToken token = new SessionToken();
            token.setToken(tokenValue);
            token.setUsername(username);
            token.setExpiresAt(LocalDateTime.now().plusMinutes(30));

            if (sessionTokenDao != null) {
                sessionTokenDao.save(token);
            } else {
                inMemoryTokens.add(token);
            }
        }
        return ok;
    }
}
