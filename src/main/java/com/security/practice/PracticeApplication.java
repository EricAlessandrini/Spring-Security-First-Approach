package com.security.practice;

import com.security.practice.domain.entity.PermissionEntity;
import com.security.practice.domain.entity.RoleEntity;
import com.security.practice.domain.entity.UserEntity;
import com.security.practice.persistence.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Set;

@SpringBootApplication
public class PracticeApplication {

	public static void main(String[] args) {
		SpringApplication.run(PracticeApplication.class, args);
	}

	@Bean
	CommandLineRunner initializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			PermissionEntity read = PermissionEntity.builder()
					.name("READ")
					.build();
			PermissionEntity create = PermissionEntity.builder()
					.name("CREATE")
					.build();
			PermissionEntity update = PermissionEntity.builder()
					.name("UPDATE")
					.build();
			PermissionEntity delete = PermissionEntity.builder()
					.name("DELETE")
					.build();

			RoleEntity ownerRole = RoleEntity.builder()
					.role("OWNER")
					.permissionsList(Set.of(read, create, update, delete))
					.build();
			RoleEntity employeeRole = RoleEntity.builder()
					.role("EMPLOYEE")
					.permissionsList(Set.of(read, create, update))
					.build();
			RoleEntity clientRole = RoleEntity.builder()
					.role("EMPLOYEE")
					.permissionsList(Set.of(read, create))
					.build();

			UserEntity owner = UserEntity.builder()
					.username("DoomBringer")
					.password(passwordEncoder.encode("DoomBringerDota"))
					.isEnabled(true)
					.accountNoExpired(true)
					.accountNoLocked(true)
					.credentialNoExpired(true)
					.roles(Set.of(ownerRole))
					.build();

			UserEntity employee = UserEntity.builder()
					.username("DrowRanger")
					.password(passwordEncoder.encode("DrowRangerDota"))
					.isEnabled(true)
					.accountNoExpired(true)
					.accountNoLocked(true)
					.credentialNoExpired(true)
					.roles(Set.of(employeeRole))
					.build();

			UserEntity client = UserEntity.builder()
					.username("Butcher")
					.password(passwordEncoder.encode("ButcherDota"))
					.isEnabled(true)
					.accountNoExpired(true)
					.accountNoLocked(true)
					.credentialNoExpired(true)
					.roles(Set.of(clientRole))
					.build();

			userRepository.saveAll(List.of(owner, employee, client));
		};
	}
}
