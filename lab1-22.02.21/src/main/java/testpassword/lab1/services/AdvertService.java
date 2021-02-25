package testpassword.lab1.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import testpassword.lab1.models.Advert;
import testpassword.lab1.repos.AdvertRepo;
import java.util.*;

@Service public class AdvertService {

    private final AdvertRepo repo;

    @Autowired public AdvertService(AdvertRepo repo) {
        this.repo = repo;
    }

    //просто заглушка, здесь может быть любая логика
    private boolean moderate(Advert a) { return new Random().nextInt(10 - 1) + 1 < 8; }

    public boolean add(Advert a) {
        if (a.getUser() == null || !this.moderate(a)) return false;
        else {
            this.save(a);
            return true;
        }
    }

    public Set<Advert> getAll() { return new HashSet<>((Collection<? extends Advert>) repo.findAll()); }

    public Advert get(int id) { return repo.getByAdvertId(id); }

    @Transactional public void save(Advert a) { this.repo.save(a); }

    @Transactional public void delete(Advert a) { repo.delete(a); }
}
