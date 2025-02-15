package com.be.back_end.service.OrderService;

import com.be.back_end.dto.OrderDTO;
import com.be.back_end.model.Orders;
import com.be.back_end.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService implements IOrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public boolean createOrder(OrderDTO orderDTO) {
        Orders order = new Orders();
        order.setTotal_price(orderDTO.getTotal_price());
        order.setTotal_quantity(orderDTO.getTotal_quantity());
        order.setStatus(orderDTO.getStatus());
        order.setOrder_notes(orderDTO.getOrder_notes());

        orderRepository.save(order);
        return true;
    }

    @Override
    public List<Orders> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Orders getOrderById(String id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Override
    public Orders updateOrder(String id, OrderDTO orderDTO) {
        Orders order = orderRepository.findById(id).orElse(null);
        if (order != null) {
            order.setTotal_price(orderDTO.getTotal_price());
            order.setTotal_quantity(orderDTO.getTotal_quantity());
            order.setStatus(orderDTO.getStatus());
            order.setOrder_notes(orderDTO.getOrder_notes());
            return orderRepository.save(order);
        }
        return null;
    }

    @Override
    public boolean deleteOrder(String id) {
        if (orderRepository.existsById(id)) {
            orderRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
