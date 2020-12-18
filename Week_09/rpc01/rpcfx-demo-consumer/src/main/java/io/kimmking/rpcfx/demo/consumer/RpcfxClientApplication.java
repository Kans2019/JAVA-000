package io.kimmking.rpcfx.demo.consumer;

import io.kimmking.rpcfx.aop.ByteBuddyFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(ByteBuddyFactory.class)
public class RpcfxClientApplication {

	// 二方库
	// 三方库 lib
	// nexus, userserivce -> userdao -> user
	//

	public static void main(String[] args) {
		SpringApplication.run(RpcfxClientApplication.class, args);
	}

}
