package com.rishank.ecommerce.service;

import com.rishank.ecommerce.dto.PaymentInfo;
import com.rishank.ecommerce.dto.Purchase;
import com.rishank.ecommerce.dto.PurchaseResponse;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

public interface CheckoutService {

	PurchaseResponse placeOrder(Purchase purchase);
	
	PaymentIntent createPaymentIntent(PaymentInfo paymentInfo) throws StripeException;
}

