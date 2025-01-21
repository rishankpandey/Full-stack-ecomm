package com.rishank.ecommerce.dto;

import java.util.Set;

import com.rishank.ecommerce.entity.Address;
import com.rishank.ecommerce.entity.Customer;
import com.rishank.ecommerce.entity.Order;
import com.rishank.ecommerce.entity.OrderItem;

import lombok.Data;

@Data
public class Purchase {

	private Customer customer;
	
	private Address shippingAddress;
	
	private Address billingAddress;
	
	private Order order;
	
	private Set<OrderItem> orderItems;
}
