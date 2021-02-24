package testpassword.lab1.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import testpassword.lab1.repos.UserRepo;

@Service public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired private UserRepo userRepo;

    @Override public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var u = userRepo.getByEmail(username);
        if (u == null) throw new UsernameNotFoundException("User with this mail does not exist");
        else {
            u.setAutosave(true);
            return u;
        }
    }
}