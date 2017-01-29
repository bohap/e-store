package com.finki.emt.bookstore.web.rest;

import com.finki.emt.bookstore.domain.Order;
import com.finki.emt.bookstore.service.OrderService;
import com.finki.emt.bookstore.util.exceptions.ModelNotFoundException;
import com.finki.emt.bookstore.web.rest.response.BookOrderResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Inject
    private OrderService service;

    @GetMapping
    public List<Order> index() {
        return service.findAll();
    }

    @PreAuthorize("@orderService.canSee(#id)")
    @GetMapping("/{id}")
    public Order show(@PathVariable long id) {
        return service.findById(id).orElseThrow(() ->
                new ModelNotFoundException("order with id " + id + " can't be find"));
    }

    @PreAuthorize("@orderService.canSee(#id)")
    @GetMapping("/{id}/books")
    public List<BookOrderResponse> books(@PathVariable long id) {
        return service.getBooks(id).stream()
                .map(BookOrderResponse::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/count")
    public Map<String, Long> count() {
        long count = service.count();
        return Collections.singletonMap("count", count);
    }

    @PutMapping("/{id}/finish")
    public Map<String, String> finish(@PathVariable long id) {
        service.finish(id);

        return Collections.singletonMap("message", "order status updated");
    }
}
