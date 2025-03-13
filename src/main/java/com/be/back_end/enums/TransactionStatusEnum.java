package com.be.back_end.enums;

public enum TransactionStatusEnum {
    UNPAID,             // No payment has been made
    DEPOSITED,          // Partial payment (deposit) has been made
    FULLY_PAID,         // The full amount has been paid
    FAILED,             // Payment attempt failed
    REFUND_REQUESTED,   // Refund has been requested by the customer
    REFUND_PROCESSING,  // Refund is being reviewed (e.g., pending approval)
    REFUND_DENIED,      // Refund request was reviewed and rejected
    REFUNDED
}
