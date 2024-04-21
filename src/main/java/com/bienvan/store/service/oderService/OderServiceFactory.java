package com.bienvan.store.service.oderService;

public class OderServiceFactory {
    public OrderService createOrderRepository(String databaseType) {
        if ("SQL".equals(databaseType)) {
            return new OrderServiceSQL();
        } else if ("HQL".equals(databaseType)) {
            return new OrderServiceHQL();
        }
        throw new IllegalArgumentException("Unsupported database type: " + databaseType);
    }
}
