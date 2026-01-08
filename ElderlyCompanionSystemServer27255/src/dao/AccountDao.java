/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

/**
 *
 * @author USER
 */

import model.Account;

public class AccountDao extends BaseDao {

    public void save(Account a) { 
        try {
            inTransaction(s -> s.persist(a)); 
        } catch (Exception e) {
            System.out.println("AccountDao save failed, operation ignored: " + e.getMessage());
        }
    }
    public void update(Account a) { 
        try {
            inTransaction(s -> s.merge(a)); 
        } catch (Exception e) {
            System.out.println("AccountDao update failed, operation ignored: " + e.getMessage());
        }
    }
    public void delete(Integer id) {
        try {
            inTransaction(s -> {
                Account a = (Account) s.get(Account.class, id);
                if (a != null) s.delete(a);
            });
        } catch (Exception e) {
            System.out.println("AccountDao delete failed, operation ignored: " + e.getMessage());
        }
    }
}