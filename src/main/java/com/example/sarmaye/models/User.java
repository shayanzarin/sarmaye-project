package com.example.sarmaye.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank (message = "Username is mandatory!!!!")
//    @Size(min = 6, max = 10)
    @Column(name = "username", unique = true)
    private String username;

    @NotBlank (message = "Password is mandatory!!!!")
    @Size(min = 6)
    private String password;

    @Transient
    private String retypePassword;

    private String lastName;
    private String firstName;
    private String phoneNum;
    private String nationalCode;
    private String email;
    private Integer status;


    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    private List<Role> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        roles.forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_"+role.name())));
        return authorities;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        if (this.status == 0)
            return false;
        else return true;
    }

    public void grantAuthority(Role role) {
        if (roles ==null ) roles = new ArrayList<>();
        roles.add(role);
    }

}
