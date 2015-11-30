package de.sigma.sigmabase.repository;

import de.sigma.sigmabase.model.user.User;
import org.springframework.data.repository.CrudRepository;

/**
 * User Repo for accessing Users in the DB.
 * <p/>
 * Created by:  nilsraabe
 * Date:        07.06.15
 * Time:        22:50
 * E-Mail:      satriani.vay@gmail.com
 */
public interface UserRepository extends CrudRepository<User, Long> {

    User findByUsername(String username);

}
