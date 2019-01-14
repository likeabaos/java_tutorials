package play.ground.HelloSpringBoot.auth.service;

import play.ground.HelloSpringBoot.auth.model.User;

public interface UserService {

	void save(User user);

	User findByUsername(String username);

}
