package com.be.back_end.service.OrderService;

import com.be.back_end.dto.OrderDTO;
import com.be.back_end.model.Orders;

import java.util.List;

public interface IOrderService {
    boolean createOrder(OrderDTO orderDTO);
    List<Orders> getAllOrders();
    Orders getOrderById(String id);
    Orders updateOrder(String id, OrderDTO orderDTO);
    boolean deleteOrder(String id);
}