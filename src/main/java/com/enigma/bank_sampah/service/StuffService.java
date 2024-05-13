package com.enigma.bank_sampah.service;

import com.enigma.bank_sampah.dto.request.SearchStuffRequest;
import com.enigma.bank_sampah.dto.request.StuffRequest;
import com.enigma.bank_sampah.dto.request.UpdateStuffRequest;
import com.enigma.bank_sampah.dto.response.StuffResponse;
import com.enigma.bank_sampah.entity.Stuff;
import org.springframework.data.domain.Page;

public interface StuffService {
    StuffResponse create(StuffRequest request);

    Page<Stuff> getAll(SearchStuffRequest request);

    Stuff getByIdEntity(String id);

    StuffResponse getByIdDTO(String id);

    StuffResponse update(UpdateStuffRequest request);

    void updateStatusById(String id, Boolean status);

    void deleteById(String id);
}
