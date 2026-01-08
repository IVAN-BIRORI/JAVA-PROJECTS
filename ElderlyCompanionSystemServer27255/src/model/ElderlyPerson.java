/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

@Entity
public class ElderlyPerson implements Serializable {
    private static final long serialVersionUID = 1L;  // ✅ fixed UID

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String nationalId;

    @Column(nullable = false)
    private String contact;

    @OneToOne(mappedBy = "elderlyPerson", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Account account;

    @ManyToMany
    @JoinTable(
        name = "elderly_caregiver",
        joinColumns = @JoinColumn(name = "elderly_id"),
        inverseJoinColumns = @JoinColumn(name = "caregiver_id")
    )
    private Set<Caregiver> caregivers = new HashSet<>();

    // Getters and setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getNationalId() { return nationalId; }
    public void setNationalId(String nationalId) { this.nationalId = nationalId; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    public Account getAccount() { return account; }
    public void setAccount(Account account) { this.account = account; }

    public Set<Caregiver> getCaregivers() { return caregivers; }
    public void setCaregivers(Set<Caregiver> caregivers) { this.caregivers = caregivers; }
}
