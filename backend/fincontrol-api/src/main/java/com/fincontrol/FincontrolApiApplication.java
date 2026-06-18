package com.fincontrol;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.hibernate.autoconfigure.HibernateJpaAutoConfiguration;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;



@SpringBootApplication(
	exclude = {
        DataSourceAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class
    }
)

public class FincontrolApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(FincontrolApiApplication.class, args);
	}

}
