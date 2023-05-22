package image.encrypt.decrypt;

import image.encrypt.decrypt.auth.model.User;
import image.encrypt.decrypt.auth.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class ImageEncryptDecryptApplication
{

	public static void main(String[] args) {
		SpringApplication.run(ImageEncryptDecryptApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(UserRepository users, PasswordEncoder encoder) {
		return args -> {
			users.save(new User("user",encoder.encode("password"),"ROLE_USER"));
			users.save(new User("admin",encoder.encode("password"),"ROLE_USER,ROLE_ADMIN"));
		};
	}
}
