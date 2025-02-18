package com.be.back_end.service.OrderService;

import com.be.back_end.dto.OrderDTO;
import com.be.back_end.model.Bookings;

import java.util.List;

public interface IOrderService {
    boolean createOrder(OrderDTO orderDTO);
    List<Bookings> getAllOrders();
    Bookings getOrderById(String id);
    Bookings updateOrder(String id, OrderDTO orderDTO);
    boolean deleteOrder(String id);
}