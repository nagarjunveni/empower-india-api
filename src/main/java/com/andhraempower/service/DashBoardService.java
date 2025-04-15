package com.andhraempower.service;

import com.andhraempower.dto.DashBoardStatisticsDTO;
import com.andhraempower.repository.DonarsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DashBoardService {


    @Autowired
    private DonarsRepository donarsRepository;


    public DashBoardStatisticsDTO getDashBoardCounts() {
        return donarsRepository.getDashBoardCounts();
    }
}
