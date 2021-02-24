package testpassword.lab1.models;

import lombok.Data;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import testpassword.lab1.services.UserService;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Data @Entity public class User implements Serializable, UserDetails {

    @Id @GeneratedValue
    private long userId;
    @Transient private static final long serialVersionUID = 4L;
    @Nullable private String email;
    @Nullable private String password;
    @Nullable private String name;
    @OneToMany private List<Advert> adverts;
    @Transient @Autowired private UserService service;
    @Transient private boolean autosave = false;

    public User() {}

    public User(String email, String password) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public User(String email, String password, String name, boolean autosave) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.autosave = autosave;
        if (autosave) this.service.save(this);
    }

    public void save() { this.service.save(this); }

    public void setEmail(String email) {
        this.email = email;
        if (autosave) this.service.save(this);
    }

    public void setPassword(String password) {
        this.password = password;
        if (autosave) this.service.save(this);
    }

    public void setName(String name) {
        this.name = name;
        if (autosave) this.service.save(this);
    }

    public void setAdverts(List<Advert> adverts) {
        this.adverts = adverts;
        if (autosave) this.service.save(this);
    }

    public void setAutosave(boolean autosave) { this.autosave = autosave; }

    @Override public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("User"));
    }

    public boolean isAutosave() { return this.autosave; }

    public String getName() { return this.name; }

    @Override public String getPassword() { return password; }

    @Override public String getUsername() { return email; }

    @Override public boolean isAccountNonExpired() { return true; }

    @Override public boolean isAccountNonLocked() { return true; }

    @Override public boolean isCredentialsNonExpired() { return true; }

    @Override public boolean isEnabled() { return true; }
}