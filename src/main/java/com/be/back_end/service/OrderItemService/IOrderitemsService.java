package com.be.back_end.service.OrderItemService;

import com.be.back_end.dto.OrderitemsDTO;
import com.be.back_end.model.Orderitems;

import java.util.List;

public interface IOrderitemsService {
    boolean createOrderItem(OrderitemsDTO dto);
    List<Orderitems> getAllOrderItems();
    Orderitems getOrderItemById(String id);
    Orderitems updateOrderItem(String id, OrderitemsDTO dto);
    boolean deleteOrderItem(String id);
}
