package com.enigma.bank_sampah.service.impl;

import com.enigma.bank_sampah.constant.APIUrl;
import com.enigma.bank_sampah.constant.ResponseMessage;
import com.enigma.bank_sampah.dto.request.SearchStuffRequest;
import com.enigma.bank_sampah.dto.request.StuffRequest;
import com.enigma.bank_sampah.dto.request.UpdateStuffRequest;
import com.enigma.bank_sampah.dto.response.ImageResponse;
import com.enigma.bank_sampah.dto.response.StuffResponse;
import com.enigma.bank_sampah.entity.Image;
import com.enigma.bank_sampah.entity.Stuff;
import com.enigma.bank_sampah.repository.StuffRepository;
import com.enigma.bank_sampah.service.ImageService;
import com.enigma.bank_sampah.service.StuffService;
import com.enigma.bank_sampah.specification.StuffSpecification;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class StuffServiceImpl implements StuffService {
    private final StuffRepository stuffRepository;
    private final ImageService imageService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public StuffResponse create(StuffRequest request) {
        if (request.getImage().isEmpty()) throw new ConstraintViolationException("image is required", null);
        Image image = imageService.create(request.getImage());

        Stuff stuff = Stuff.builder()
                .stuffName(request.getStuffName())
                .buyingPrice(request.getBuyingPrice())
                .sellingPrice(request.getSellingPrice())
                .weight(0F)
                .status(true)
                .image(image)
                .build();
        stuffRepository.saveAndFlush(stuff);

        return StuffResponse.builder()
                .id(stuff.getId())
                .stuffName(stuff.getStuffName())
                .buyingPrice(stuff.getBuyingPrice())
                .sellingPrice(stuff.getSellingPrice())
                .weight(stuff.getWeight())
                .status(stuff.getStatus())
                .image(ImageResponse.builder()
                        .url(APIUrl.STUFF_IMAGE_API + "/" + stuff.getImage().getId())
                        .name(stuff.getImage().getName())
                        .build())
                .build();
    }

    @Transactional(readOnly = true)
    @Override
    public Page<StuffResponse> getAll(SearchStuffRequest request) {
        if (request.getPage() <= 0) request.setPage(1);

        Sort sort = Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy());

        Pageable pageable = PageRequest.of((request.getPage() - 1), request.getSize(), sort);
        Specification<Stuff> specification = StuffSpecification.getSpecification(request);

        Page<Stuff> stuffPage = stuffRepository.findAll(specification, pageable);

        return stuffPage.map(stuff -> StuffResponse.builder()
                .id(stuff.getId())
                .stuffName(stuff.getStuffName())
                .buyingPrice(stuff.getBuyingPrice())
                .sellingPrice(stuff.getSellingPrice())
                .weight(stuff.getWeight())
                .image(ImageResponse.builder()
                        .url(APIUrl.STUFF_IMAGE_API + "/" + stuff.getImage().getId())
                        .name(stuff.getImage().getName())
                        .build())
                .status(stuff.getStatus())
                .build());
    }

    @Transactional(readOnly = true)
    @Override
    public Stuff getByIdEntity(String id) {
        return findByIdOrThrowNotFound(id);
    }

    @Transactional(readOnly = true)
    @Override
    public StuffResponse getByIdDTO(String id) {
        Stuff stuff = findByIdOrThrowNotFound(id);

        return StuffResponse.builder()
                .id(stuff.getId())
                .stuffName(stuff.getStuffName())
                .buyingPrice(stuff.getBuyingPrice())
                .sellingPrice(stuff.getSellingPrice())
                .weight(stuff.getWeight())
                .status(stuff.getStatus())
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public StuffResponse update(UpdateStuffRequest request) {
        Stuff stuff = findByIdOrThrowNotFound(request.getId());

        stuff.setBuyingPrice(request.getBuyingPrice());
        stuff.setSellingPrice(request.getSellingPrice());

        stuffRepository.saveAndFlush(stuff);

        return StuffResponse.builder()
                .id(stuff.getId())
                .stuffName(stuff.getStuffName())
                .buyingPrice(stuff.getBuyingPrice())
                .sellingPrice(stuff.getSellingPrice())
                .weight(stuff.getWeight())
                .status(stuff.getStatus())
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateStatusById(String id, Boolean status) {
        findByIdOrThrowNotFound(id);

        stuffRepository.updateStatus(id, status);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(String id) {
        findByIdOrThrowNotFound(id);

        stuffRepository.updateStatus(id, false);
    }

    private Stuff findByIdOrThrowNotFound(String id) {
        return stuffRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.ERROR_NOT_FOUND));
    }
}
