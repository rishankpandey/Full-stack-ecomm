package com.rishank.ecommerce.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.rishank.ecommerce.dao.CustomerRepository;
import com.rishank.ecommerce.dto.PaymentInfo;
import com.rishank.ecommerce.dto.Purchase;
import com.rishank.ecommerce.dto.PurchaseResponse;
import com.rishank.ecommerce.entity.Customer;
import com.rishank.ecommerce.entity.Order;
import com.rishank.ecommerce.entity.OrderItem;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

import jakarta.transaction.Transactional;

@Service
public class CheckoutServiceImpl implements CheckoutService {

	
	private CustomerRepository customerRepository;
	
	public CheckoutServiceImpl(CustomerRepository customerRepository , @Value("${stripe.key.secret}") String secretKey) {
		this.customerRepository = customerRepository;
		
		Stripe.apiKey = secretKey;
	}
	
	@Override
	@Transactional
	public PurchaseResponse placeOrder(Purchase purchase) {
		
		//retrieve the order info from dto
		Order order = purchase.getOrder();
		
		//generate tracking number
		String orderTrackingNumber = generateOrdertrackingNumber();
		order.setOrdertrackingNumber(orderTrackingNumber);
		
		//populate order with orderitems
		Set<OrderItem> orderItems = purchase.getOrderItems();
		orderItems.forEach(item-> order.add(item));
		
		//populate order with billingAddress and shippingAddress
		order.setBillingAddress(purchase.getBillingAddress());
		order.setShipingAddres(purchase.getShippingAddress());
		
		//populate customer with order
		Customer customer = purchase.getCustomer();
		
		//check if this is an existing customer
		String theEmail =customer.getEmail();
		
		Customer customerFromDB =customerRepository.findByEmail(theEmail);
		
		if(customerFromDB!=null) {
			customer = customerFromDB;
		}
		customer.add(order);
		
		//save to the database
		customerRepository.save(customer);
		
		//return a response
		return new PurchaseResponse(orderTrackingNumber);
	}

	private String generateOrdertrackingNumber() {
		
		//generate a random UUID number (UUID version-4)
		
		return UUID.randomUUID().toString();
		
	}

	@Override
	public PaymentIntent createPaymentIntent(PaymentInfo paymentInfo) throws StripeException {
		List<String> paymentMethodTypes = new ArrayList<>();
		paymentMethodTypes.add("card");
		
		Map<String , Object> params= new HashMap<>();
		params.put("amount", paymentInfo.getAmount());
		params.put("currency", paymentInfo.getCurrency());
		params.put("payment_method_types", paymentMethodTypes);
		params.put("receipt_email", paymentInfo.getReceiptEmail());
		
		return PaymentIntent.create(params);
	}

}
