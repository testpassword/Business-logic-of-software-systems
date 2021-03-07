package testpassword.lab1.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import testpassword.lab1.models.Advert;
import testpassword.lab1.repos.AdvertRepo;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service public class AdvertService {

    private final AdvertRepo repo;

    @Autowired public AdvertService(AdvertRepo repo) {
        this.repo = repo;
    }

    public boolean add(Advert a) {
        if (a.getUser() == null || !ModeratorService.moderate(a)) return false;
        else {
            this.save(a);
            return true;
        }
    }

    public List<Advert> getAll() {
        return StreamSupport.stream(repo.findAll().spliterator(), false).collect(Collectors.toList());
    }

    public Advert get(long id) { return repo.getByAdvertId(id); }

    @Transactional public void save(Advert a) { this.repo.save(a); }

    @Transactional public void delete(Advert a) { repo.delete(a); }
}
