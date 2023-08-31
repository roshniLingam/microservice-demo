package com.microservicedemo.orderservice.service;

import com.microservicedemo.orderservice.dto.InventoryResponse;
import com.microservicedemo.orderservice.dto.OrderLineItemsDto;
import com.microservicedemo.orderservice.dto.OrderRequest;
import com.microservicedemo.orderservice.model.Order;
import com.microservicedemo.orderservice.model.OrderLineItems;
import com.microservicedemo.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;

    public String placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToDto)
                .toList();

        order.setOrderLineItemsList(orderLineItems);

        // Get all sku codes
        List<String> skuCodes = order.getOrderLineItemsList().stream()
            .map(OrderLineItems::getSkuCode)
            .toList();

        /*
         * Call inventory service
         * and place order when product is in stock
         * Make synchronous request
         */
        InventoryResponse[] inventoryResponseArray = webClientBuilder.build().get()
            .uri("http://inventory-service/api/inventory", uriBuilder -> 
                uriBuilder.queryParam("skuCode", skuCodes).build())
            .retrieve()
            .bodyToMono(InventoryResponse[].class)
            .block();

        // check if all products are in stock
        // if any one product is out of stock, return false
        boolean allProductsInStock = Arrays.stream(inventoryResponseArray)
            .allMatch(InventoryResponse::getIsInStock);
        
        if(allProductsInStock) {
            orderRepository.save(order);
            return "Order Placed Successfully";
        } else{
            throw new IllegalArgumentException("Product not in stock.");
        }
    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }
}
