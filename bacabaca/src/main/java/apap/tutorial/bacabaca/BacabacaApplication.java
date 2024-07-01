package apap.tutorial.bacabaca;

import apap.tutorial.bacabaca.controller.MyErrorController;
import apap.tutorial.bacabaca.dto.BukuMapper;
import apap.tutorial.bacabaca.dto.PenerbitMapper;
import apap.tutorial.bacabaca.dto.request.CreateBukuRequestDTO;
import apap.tutorial.bacabaca.dto.request.CreatePenerbitRequestDTO;
import apap.tutorial.bacabaca.model.Penerbit;
import apap.tutorial.bacabaca.service.BukuService;
import apap.tutorial.bacabaca.service.PenerbitService;
import com.github.javafaker.Faker;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class BacabacaApplication{
	
	public static void main(String[] args) { 
		SpringApplication.run(BacabacaApplication.class, args);
		System.setProperty("spring.profiles.active", "prod");		
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	//CommandLineRunner digunakan untuk execyte code saat spring pertama kali start up
	@Bean
	@Transactional
	CommandLineRunner run(BukuService bukuService, PenerbitService penerbitService, BukuMapper bukuMapper, PenerbitMapper penerbitMapper ){
		return args -> {
			var faker = new Faker(new Locale("in-ID"));
			var random = new Random();
			List<Penerbit> listPenerbit = new ArrayList<>();

			for (int i = 0; i < 1000; i ++){
				var penerbitDTO = new CreatePenerbitRequestDTO();
				var fakeBook1 = faker.book();
				penerbitDTO.setNamaPenerbit(fakeBook1.publisher());
				penerbitDTO.setEmail(faker.internet().emailAddress());
				penerbitDTO.setAlamat(faker.address().fullAddress());
				// Mapping penerbitDTO ke buku lalu save penerbit ke database
				var penerbit = penerbitMapper.createPenerbitRequestDTOToPenerbit(penerbitDTO);
				penerbit = penerbitService.createPenerbit(penerbit);
				listPenerbit.add(penerbit);
			}

			for (int i = 0; i < 1000; i ++){
			// Membuat fake data memanfaatkan Java Faker
			var bukuDTO = new CreateBukuRequestDTO();
			var fakeBook = faker.book();
			var fakeDate = faker.date();
			bukuDTO.setHarga(new BigDecimal(Math.random()*1000000));
			bukuDTO.setJudul(fakeBook.title());
			bukuDTO.setTahunTerbit(String.valueOf(fakeDate.past(36500, TimeUnit.DAYS).getYear()+1900));
			// Mapping bukuDTO ke buku lalu save buku ke database
			var buku = bukuMapper.createBukuRequestDTOToBuku(bukuDTO);
			buku.setPenerbit(listPenerbit.get(random.nextInt(1000)));
			bukuService.saveBuku(buku);
			}

			

		};
	}
	
}