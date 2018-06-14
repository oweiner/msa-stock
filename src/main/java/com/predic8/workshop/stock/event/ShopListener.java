package com.predic8.workshop.stock.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.predic8.workshop.stock.dto.Stock;
import io.prometheus.client.Counter;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@Service
public class ShopListener {

	private final ObjectMapper mapper;
	private final NullAwareBeanUtilsBean beanUtils;
	private final Map<String, Stock> articles;

	private Counter counterQuantity;


	public ShopListener(ObjectMapper mapper, NullAwareBeanUtilsBean beanUtils, Map<String, Stock> articles) {
		this.mapper = mapper;
		this.beanUtils = beanUtils;
		this.articles = articles;
	}

	@KafkaListener(topics = "shop")
	public void listen(Operation op) throws InvocationTargetException, IllegalAccessException {

		if (!op.getBo().equals("article")) {
			return;
		}
		op.logReceive();

		System.out.println(op);

		Stock stock = mapper.convertValue(op.getObject(), Stock.class);

		switch (op.getAction()) {

			case "create":
				articles.put(stock.getUuid(), stock);
				break;

			case "update":
				Stock oldStock = articles.get(stock.getUuid());
				// oldStock.setQuantity(stock.getQuantity());

				if (oldStock != null) {
					beanUtils.copyProperties(oldStock, stock);
					articles.put(stock.getUuid(), oldStock);

				} else {
					articles.put(stock.getUuid(), stock);
				}
				counterQuantity.inc();
				break;

			case "delete":
				articles.remove(stock.getUuid());
				break;
		}
	}
}