package kq.practice.ssf15demo;

import java.time.Duration;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@SpringBootApplication
public class Ssf15demoApplication implements CommandLineRunner {
	
	@Autowired @Qualifier("myredis")
	private RedisTemplate<String, String> template;

	public static void main(String[] args) {
		SpringApplication.run(Ssf15demoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println(">>> redistemplate: %s".formatted(template));

		// ValueOperations<String, String> opsValue = template.opsForValue();
		// // opsValue.set("name", "barney");
		// opsValue.set("email", "barney@mail.com");
		// // opsValue.increment("count", 5);
		// String name = opsValue.get("name");
		// System.out.println(name);
		// opsValue.set("promo", "lala", Duration.ofSeconds(30));

		// List
		// ListOperations<String, String> opsList = template.opsForList();

		// template.delete("fred_cart");

		// opsList.leftPushAll("fred_cart", "apple", "banana", "pear", "orange");

		// List<String> list = new LinkedList<>();
		// list.add("durian");
		// list.add("mango");
		// opsList.leftPushAll("fred_cart", list);

		//Map
		// HashOperations<String, String, String> opsHash = template.opsForHash();
		// opsHash.put("1", "name", "fred");
		// opsHash.put("1", "email", "fred@mail.com");
		// opsHash.put("2", "name", "barney");
		// opsHash.put("2", "email", "barney@mail.com");
	}
	
}
