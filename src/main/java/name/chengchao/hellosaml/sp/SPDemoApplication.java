package name.chengchao.hellosaml.sp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan("name.chengchao.hellosaml.sp")

public class SPDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SPDemoApplication.class, args);
    }

}
