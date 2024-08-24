package com.greaticker.demo.service.history;

import com.greaticker.demo.dto.response.common.CursorPaginationDto;
import com.greaticker.demo.dto.response.common.CursorPaginationMeta;
import com.greaticker.demo.dto.response.history.HistoryResponseDto;
import com.greaticker.demo.model.history.History;
import com.greaticker.demo.repository.history.HistoryRepository;
import com.greaticker.demo.repository.project.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class HistoryService {

    private final HistoryRepository historyRepository;

    @Transactional(readOnly = true)
    public CursorPaginationDto<HistoryResponseDto> showHistoryOfUser() {
        Long user_id = 1L; //추후 여기서 Redis에서 유저정보를 가져오게 수정할 거임
        List<History> fetchedData = historyRepository.findByUserId(user_id);
        List<HistoryResponseDto> historyResponseDtoList = fetchedData.stream().map(HistoryResponseDto::fromEntity).toList();
        return new CursorPaginationDto<>(new CursorPaginationMeta(10L, false), historyResponseDtoList);
    }
}
