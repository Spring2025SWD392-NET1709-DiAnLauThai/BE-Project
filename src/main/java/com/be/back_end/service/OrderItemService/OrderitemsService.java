package com.be.back_end.service.OrderItemService;

import com.be.back_end.dto.OrderitemsDTO;
import com.be.back_end.model.Orderitems;
import com.be.back_end.repository.OrderitemsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderitemsService implements IOrderitemsService {
    private final OrderitemsRepository orderitemsRepository;

    public OrderitemsService(OrderitemsRepository orderitemsRepository) {
        this.orderitemsRepository = orderitemsRepository;
    }

    @Override
    public boolean createOrderItem(OrderitemsDTO dto) {
        Orderitems orderItem = new Orderitems();
        orderItem.setQuantity(dto.getQuantity());
        orderItem.setUnit_price(dto.getUnit_price());

        return  orderitemsRepository.save(orderItem)!=null;
    }

    @Override
    public List<Orderitems> getAllOrderItems() {
        return orderitemsRepository.findAll();
    }

    @Override
    public Orderitems getOrderItemById(String id) {
        return orderitemsRepository.findById(id).orElse(null);
    }

    @Override
    public Orderitems updateOrderItem(String id, OrderitemsDTO dto) {
        Orderitems orderItem = orderitemsRepository.findById(id).orElse(null);
        if (orderItem != null) {
            orderItem.setQuantity(dto.getQuantity());
            orderItem.setUnit_price(dto.getUnit_price());
            return orderitemsRepository.save(orderItem);
        }
        return null;
    }

    @Override
    public boolean deleteOrderItem(String id) {
        if (orderitemsRepository.existsById(id)) {
            orderitemsRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
