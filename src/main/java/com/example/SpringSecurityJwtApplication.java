package com.example;

import com.example.models.Erole;
import com.example.models.RoleEntity;
import com.example.models.UserEntity;
import com.example.repositories.UserRepository;
import com.example.security.filters.JwtAuthenticationFilters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@SpringBootApplication
public class SpringSecurityJwtApplication {

	private static final Logger logger = LoggerFactory.getLogger(SpringSecurityJwtApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityJwtApplication.class, args);
	}
	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	UserRepository userRepository;
	@Bean
	CommandLineRunner init(){
		return args -> {
			logger.info("init");
			  UserEntity userEntity = UserEntity.builder()
					  .email("juancamilo1478@gmail.com")
					  .username("juan")
					  .password(passwordEncoder.encode("1234"))
					  .roles(Set.of(RoleEntity.builder()
							  .name(Erole.valueOf(Erole.ADMIN.name())).build())
					  ).build();


			UserEntity userEntity2 = UserEntity.builder()
					.email("angi@gmail.com")
					.username("angg")
					.password(passwordEncoder.encode("1234"))
					.roles(Set.of(RoleEntity.builder()
							.name(Erole.valueOf(Erole.INVITE.name())).build())
					).build();


			UserEntity userEntity3 = UserEntity.builder()
					.email("carolina@gmail.com")
					.username("carol")
					.password(passwordEncoder.encode("1234"))
					.roles(Set.of(RoleEntity.builder()
							.name(Erole.valueOf(Erole.USER.name())).build())
					).build();

			logger.info("init2");
			userRepository.save(userEntity);
			userRepository.save(userEntity2);
			userRepository.save(userEntity3);

		};
	}


}
