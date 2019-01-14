package play.ground.HelloSpringBoot.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import play.ground.HelloSpringBoot.auth.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long>{

}
