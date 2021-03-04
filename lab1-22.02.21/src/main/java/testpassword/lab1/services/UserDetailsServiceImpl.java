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
        return userRepo.getByEmail(username);
    }
}