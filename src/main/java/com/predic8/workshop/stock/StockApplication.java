package com.predic8.workshop.stock;

import com.predic8.workshop.stock.dto.Stock;
import com.predic8.workshop.stock.event.NullAwareBeanUtilsBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@EnableDiscoveryClient
@SpringBootApplication
public class StockApplication {

	@Bean
	public Map<String, Stock> articles() {
		return new ConcurrentHashMap<>();
	}

	@Bean
	public NullAwareBeanUtilsBean beanUtils() {
		return new NullAwareBeanUtilsBean();
	}

	public static void main(String[] args) {
		SpringApplication.run(StockApplication.class, args);
	}
}