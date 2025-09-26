package io.reviewcoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@Slf4j
@SpringBootApplication
public class ReviewCoderApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReviewCoderApiApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void onReady() {
		log.info("서버가 실행되었습니다.");
	}
}
