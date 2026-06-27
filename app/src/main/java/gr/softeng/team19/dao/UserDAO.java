package gr.softeng.team19.dao;

import gr.softeng.team19.domain.ApplicationUser;
import java.util.List;

public interface UserDAO {
    void add(ApplicationUser user);
    void delete(ApplicationUser user);
    ApplicationUser find(String username);
    List<ApplicationUser> findAll();
}