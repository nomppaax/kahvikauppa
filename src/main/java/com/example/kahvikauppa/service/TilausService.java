package com.example.kahvikauppa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.kahvikauppa.model.Tilaus;
import com.example.kahvikauppa.repository.TilausRepository;

@Service
public class TilausService {
    @Autowired
    private TilausRepository tilausRepository;

    public Tilaus newOrder(String order) {
        Tilaus newOrder = new Tilaus();
        newOrder.setOrderDetails(order);

        return this.tilausRepository.save(newOrder);
    }

}
