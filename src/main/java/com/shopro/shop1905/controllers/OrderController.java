package com.shopro.shop1905.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shopro.shop1905.dtos.dtosReq.OrderDTO;
import com.shopro.shop1905.dtos.dtosRes.ApiMetaRes;
import com.shopro.shop1905.dtos.dtosRes.ApiRes;
import com.shopro.shop1905.dtos.dtosRes.DetailOrderDTO;
import com.shopro.shop1905.dtos.dtosRes.MetadataDTO;
import com.shopro.shop1905.dtos.dtosRes.OrderResDTO;
import com.shopro.shop1905.services.OrderService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/order")
public class OrderController {
        private final OrderService orderService;

        @PostMapping("/save")
        public ApiRes<DetailOrderDTO> save(@RequestBody OrderDTO order, HttpServletRequest request) throws IOException {
                return ApiRes.<DetailOrderDTO>builder().code(1000).message("Create order success")
                                .result(orderService.save(order, request))
                                .build();
        }

        @GetMapping("/number-pending")
        public ApiRes<Long> getNumberPaymentingOrder() {
                return ApiRes.<Long>builder().code(1000).message("Create order success")
                                .result(orderService.getNumberPaymentingOrder())
                                .build();
        }

        @GetMapping("/{id}")
        public ApiRes<DetailOrderDTO> getDetailOrder(@PathVariable Long id) {
                return ApiRes.<DetailOrderDTO>builder().code(1000).message("Create order success")
                                .result(orderService.getDetailOrder(id))
                                .build();
        }

        @GetMapping("/all")
        public ApiMetaRes<List<OrderResDTO>> getOrders(@RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "5") int size) {
                Page<OrderResDTO> ordersPage = orderService.getOrders(page, size);
                MetadataDTO metadata = new MetadataDTO(
                                ordersPage.getTotalElements(),
                                ordersPage.getTotalPages(),
                                ordersPage.getNumber(),
                                ordersPage.getSize());
                return ApiMetaRes.<List<OrderResDTO>>builder().code(1000).message("get orders success")
                                .result(ordersPage.getContent()).metadata(metadata)
                                .build();
        }
}