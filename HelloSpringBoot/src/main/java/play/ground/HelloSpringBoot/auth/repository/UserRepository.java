package play.ground.HelloSpringBoot.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import play.ground.HelloSpringBoot.auth.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByUsername(String username);

}
