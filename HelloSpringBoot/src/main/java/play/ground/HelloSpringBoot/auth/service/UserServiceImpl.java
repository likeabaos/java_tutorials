package play.ground.HelloSpringBoot.auth.service;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import play.ground.HelloSpringBoot.auth.model.Role;
import play.ground.HelloSpringBoot.auth.model.User;
import play.ground.HelloSpringBoot.auth.repository.RoleRepository;
import play.ground.HelloSpringBoot.auth.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	public void save(User user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setRoles(new HashSet<Role>(roleRepository.findAll()));
		userRepository.save(user);
	}

	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}
}