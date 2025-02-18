package com.be.back_end.service.OrderService;

import com.be.back_end.dto.OrderDTO;
import com.be.back_end.model.Bookings;
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
        Bookings order = new Bookings();
        order.setTotal_price(orderDTO.getTotal_price());
        order.setTotal_quantity(orderDTO.getTotal_quantity());
        order.setStatus(orderDTO.getStatus());


        orderRepository.save(order);
        return true;
    }

    @Override
    public List<Bookings> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Bookings getOrderById(String id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Override
    public Bookings updateOrder(String id, OrderDTO orderDTO) {
        Bookings order = orderRepository.findById(id).orElse(null);
        if (order != null) {
            order.setTotal_price(orderDTO.getTotal_price());
            order.setTotal_quantity(orderDTO.getTotal_quantity());
            order.setStatus(orderDTO.getStatus());

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
