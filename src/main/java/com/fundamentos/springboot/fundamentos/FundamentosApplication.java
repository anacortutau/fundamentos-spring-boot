package com.fundamentos.springboot.fundamentos;

import com.fundamentos.springboot.fundamentos.bean.MyBean;
import com.fundamentos.springboot.fundamentos.bean.MyBeanWithDependency;
import com.fundamentos.springboot.fundamentos.bean.MyBeanWithProperties;
import com.fundamentos.springboot.fundamentos.component.ComponentDependency;
import com.fundamentos.springboot.fundamentos.entity.User;
import com.fundamentos.springboot.fundamentos.pojo.UserPojo;
import com.fundamentos.springboot.fundamentos.repository.UserRepository;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class FundamentosApplication implements CommandLineRunner {

	private Log LOGGER = LogFactory.getLog(FundamentosApplication.class);

	private ComponentDependency componentDependency;
	private MyBean myBean;
	private MyBeanWithDependency myBeanWithDependency;
	private MyBeanWithProperties myBeanWithProperties;
	private UserPojo userPojo;
	private UserRepository userRepository;

	public FundamentosApplication (@Qualifier("componentTwoImplement") ComponentDependency componentDependency, MyBean myBean, MyBeanWithDependency myBeanWithDependency, MyBeanWithProperties myBeanWithProperties, UserPojo userPojo, UserRepository userRepository){
		this.componentDependency = componentDependency;
		this.myBean = myBean;
		this.myBeanWithDependency = myBeanWithDependency;
		this.myBeanWithProperties = myBeanWithProperties;
		this.userPojo = userPojo;
		this.userRepository = userRepository;
	}


	public static void main(String[] args) {
		SpringApplication.run(FundamentosApplication.class, args);
	}

	@Override
	public void run(String... args){

		//ejemplosAnteriores();
		saveUsersInDataBase();
		getInformationJpqFromUser();

	}

	private void getInformationJpqFromUser(){
		LOGGER.info("Usuario con el metodo userRepository.findByUserEmail" +
				userRepository.findByUserEmail("julie@domain.com")
		.orElseThrow(()-> new RuntimeException("No se encontro el usuario")));

		userRepository.findAndSort("user", Sort.by("id").descending())
				.stream()
				.forEach(user-> LOGGER.info("Usuario con metodo sort" + user));

		userRepository.findByName("John")
				.stream()
				.forEach(user->LOGGER.info("Usuario con query method" + user));

		LOGGER.info("Usuario con query method findByEmailAndName" + userRepository.findByEmailAndName("daniela@domain.com", "Daniela")
				.orElseThrow(()-> new RuntimeException("usuario no encontrado")));
	}

	private void saveUsersInDataBase(){
		User user1 = new User("John", "john@domain.com",LocalDate.of(2021, 03,20));
		User user2 = new User("John", "julie@domain.com", LocalDate.of(2021, 03, 20));
		User user3 = new User("Daniela", "daniela@domain.com", LocalDate.of(2021, 07, 20));
		User user4 = new User("user4", "user4@domain.com", LocalDate.of(2021, 01, 20));
		User user5 = new User("user5", "user5@domain.com", LocalDate.of(2021, 11, 20));
		User user6 = new User("user6", "user6@domain.com", LocalDate.of(2021, 10, 20));
		User user7 = new User("user7", "user7@domain.com", LocalDate.of(2021, 06, 20));
		User user8 = new User("user8", "user8@domain.com", LocalDate.of(2021, 02, 20));
		User user9 = new User("user9", "user9@domain.com", LocalDate.of(2021, 04, 20));
		User user10 = new User("user10", "user10@domain.com", LocalDate.of(2021, 9, 20));
		User user11 = new User("user11", "user11@domain.com", LocalDate.of(2021, 10, 20));
		List<User> list = Arrays.asList(user1, user2, user3,user4, user5, user6, user7,user8, user9,  user10, user11);
		list.stream().forEach(userRepository::save);


	}

	private void ejemplosAnteriores(){
		componentDependency.saludar();
		myBean.print();
		myBeanWithDependency.printWithDependency();
		System.out.println(myBeanWithProperties.function());
		System.out.println(userPojo.getEmail() + "-" + userPojo.getPassword() + "-" + userPojo.getAge());
		try{

			int value = 10/0;
			LOGGER.debug("Mi valor: " + value);
		}catch(Exception e){
			LOGGER.error("Esto es un error al dividir por cero" + e.getMessage());
		}
	}
}
