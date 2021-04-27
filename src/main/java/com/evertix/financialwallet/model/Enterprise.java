package com.evertix.financialwallet.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "enterprises")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Enterprise implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotNull(message = "RUC cannot be null")
    @NotBlank(message = "RUC cannot be blank")
    @Size(min = 8, max = 11)
    private String ruc;

    @Column(unique = true)
    @NotNull(message = "Name cannot be null")
    @NotBlank(message = "Name cannot be blank")
    @Size(max = 150)
    private String name;

    @Column(unique = true)
    @NotNull(message = "Email cannot be null")
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid Email")
    @Size(max = 100)
    private String email;

    @Column(unique = true)
    @NotNull(message = "Address cannot be null")
    @NotBlank(message = "Address cannot be blank")
    @Size(max = 200)
    private String address;

    @Column(unique = true)
    @NotNull(message = "Phone cannot be null")
    @NotBlank(message = "Phone cannot be null")
    @Size(max = 12, min = 9)
    private String phone;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "economicActivity_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private EconomicActivity economicActivity;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "manager_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User manager;

    public Enterprise(String ruc, String name, String email, String address, String phone) {
        this.ruc = ruc;
        this.name = name;
        this.email = email;
        this.address = address;
        this.phone = phone;
    }
}
