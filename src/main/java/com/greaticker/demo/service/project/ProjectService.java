package com.greaticker.demo.service.project;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.greaticker.demo.constants.enums.history.HistoryKind;
import com.greaticker.demo.constants.enums.project.ProjectState;
import com.greaticker.demo.dto.request.project.ProjectRequest;
import com.greaticker.demo.dto.response.project.ProjectResponse;
import com.greaticker.demo.exception.customException.NoSupportedProjectStateChangeException;
import com.greaticker.demo.exception.customException.TodayStickerAlreadyGotException;
import com.greaticker.demo.exception.customException.TooLongProjectNameException;
import com.greaticker.demo.exception.customException.TooShortProjectNameException;
import com.greaticker.demo.model.history.History;
import com.greaticker.demo.model.project.Project;
import com.greaticker.demo.model.user.User;
import com.greaticker.demo.repository.history.HistoryRepository;
import com.greaticker.demo.repository.project.ProjectRepository;
import com.greaticker.demo.repository.user.UserRepository;
import com.greaticker.demo.utils.NamingRule;
import com.greaticker.demo.utils.StringConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import static com.greaticker.demo.constants.StickerCnt.TOTAL_STICKER_CNT;
import static com.greaticker.demo.constants.StringLimit.PROJECT_NAME_LENGTH_LIMIT;
import static com.greaticker.demo.constants.StringLimit.PROJECT_NAME_LENGTH_UNDER_LIMIT;
import static com.greaticker.demo.exception.errorCode.ErrorCode.*;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final HistoryRepository historyRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    public String getNewSticker() throws JsonProcessingException {
        User user = userRepository.findById(1L).get();

        LocalDate lastStickerGotDay = user.getLastGet() == null ? LocalDate.EPOCH: user.getLastGet().toLocalDate();
        LocalDate today = LocalDateTime.now().toLocalDate();
        if (lastStickerGotDay.isBefore(today)) {
            String stickerInveotoryStr = user.getStickerInventory();
            List<String> stickerInventoryList = objectMapper.readValue(stickerInveotoryStr, new TypeReference<List<String>>() {
            });
            String gotStickerId = getRandomSticker(TOTAL_STICKER_CNT, stickerInventoryList);

            stickerInventoryList.add(gotStickerId);
            String updatedStickerInventoryStr = objectMapper.writeValueAsString(stickerInventoryList);
            user.setStickerInventory(updatedStickerInventoryStr);
            user.setLastGet(LocalDateTime.now());

            Optional<Project> fetchedData = projectRepository.findByUserId(user.getId());
            if (fetchedData.isEmpty()) {
                throw new RuntimeException("Project cannot Be null Since Someone pushed Get Sticker Button Which is Shown when project is in Progress");
            }
            Project fetchedProject = fetchedData.get();
            fetchedProject.plusDayInARow();
            historyRepository.save(new History(null, HistoryKind.GET_STICKER, fetchedProject.getName(), fetchedProject.getDay_in_a_row(), Long.valueOf(gotStickerId), user));
            return gotStickerId;
        } else {
            throw new TodayStickerAlreadyGotException("TODAY_STICKER_ALREADY_GOT");
        }
    }

    public String updateProject(ProjectRequest projectRequest) {
        User user = userRepository.findById(1L).get();//추후 여기서 Redis에서 유저정보를 가져오게 수정할 거임

        ProjectState prevState = projectRequest.getPrevProjectState();
        ProjectState nextState = projectRequest.getNextProjectState();

        if (prevState == ProjectState.NO_EXIST && nextState == ProjectState.IN_PROGRESS) {
            String requestedProjectName = projectRequest.getProjectName();
            checkNamingRule(requestedProjectName);
            Project newProject = projectRepository.save(new Project(null, ProjectState.IN_PROGRESS, requestedProjectName, LocalDateTime.now(), 0, user));
            user.setNowProjectId(newProject.getId());
            historyRepository.save(new History(null, HistoryKind.START_GOAL, newProject.getName(), newProject.getDay_in_a_row(), null, user));
            return StringConverter.longToStringConvert(newProject.getId());
        } else if (prevState == ProjectState.IN_PROGRESS && nextState == ProjectState.COMPLETED) {
            Optional<Project> fetchedData = projectRepository.findById(user.getNowProjectId());
            if (fetchedData.isEmpty()) throw new RuntimeException("Fetched Project Cannot Be Empty Since prevState is In Progess");
            Project fetchedProject = fetchedData.get();
            fetchedProject.setState(projectRequest.getNextProjectState());
            historyRepository.save(new History(null, HistoryKind.ACCOMPLISH_GOAL, fetchedProject.getName(), fetchedProject.getDay_in_a_row(), null, user));
            return StringConverter.longToStringConvert(fetchedProject.getId());
        } else if (prevState == ProjectState.IN_PROGRESS && nextState == ProjectState.NO_EXIST) {
            Optional<Project> fetchedData = projectRepository.findById(user.getNowProjectId());
            if (fetchedData.isEmpty()) throw new RuntimeException("Fetched Project Cannot Be Empty Since prevState is In Progess");
            Project fetchedProject = fetchedData.get();
            projectRepository.delete(fetchedProject);
            user.setNowProjectId(null);
            user.setLastGet(null);
            user.setStickerInventory("[]");
            historyRepository.save(new History(null, HistoryKind.DELETE_GOAL, fetchedProject.getName(), null, null, user));
            return StringConverter.longToStringConvert(fetchedProject.getId());
        } else if (prevState == ProjectState.COMPLETED && nextState == ProjectState.IN_PROGRESS){
            Optional<Project> fetchedData = projectRepository.findById(user.getNowProjectId());
            if (fetchedData.isEmpty()) throw new RuntimeException("Fetched Project Cannot Be Empty Since prevState is Completed");
            Project fetchedProject = fetchedData.get();
            projectRepository.delete(fetchedProject);
            String requestedProjectName = projectRequest.getProjectName();
            checkNamingRule(requestedProjectName);
            Project newProject = projectRepository.save(new Project(null, ProjectState.IN_PROGRESS, requestedProjectName, LocalDateTime.now(), 0, user));
            user.setNowProjectId(newProject.getId());
            user.setLastGet(null);
            user.setStickerInventory("[]");
            historyRepository.save(new History(null, HistoryKind.START_GOAL, newProject.getName(), newProject.getDay_in_a_row(), null, user));
            return StringConverter.longToStringConvert(newProject.getId());
        } else {
            throw new NoSupportedProjectStateChangeException(NO_SUPPORTED_PROJECT_STATE_CHANGE);
        }
    }

    @Transactional(readOnly = true)
    public ProjectResponse getProject() {
        Long user_id = 1L; //추후 여기서 Redis에서 유저정보를 가져오게 수정할 거임
        Optional<Project> fetchedData = projectRepository.findByUserId(user_id);
        return fetchedData.map(ProjectResponse::fromEntity)
                .orElseGet(() -> new ProjectResponse(ProjectState.NO_EXIST, null, null, null));
    }


    private void checkNamingRule(String requestedProjectName) {
        NamingRule.validateNoSpecialCharacters(requestedProjectName);
        if (NamingRule.calculateLength(requestedProjectName) > PROJECT_NAME_LENGTH_LIMIT) {
            throw new TooLongProjectNameException(TOO_LONG_PROJECT_NAME);
        } else if (NamingRule.calculateLength(requestedProjectName) < PROJECT_NAME_LENGTH_UNDER_LIMIT) {
            throw new TooShortProjectNameException(TOO_SHORT_PROJECT_NAME);
        }
    }

    private String getRandomSticker(int total_cnt, List<String> stickerInventoryList) {
        // stickerInventoryList의 문자열을 정수로 변환하여 리스트 생성
        List<Integer> excludedNumbers = stickerInventoryList.stream()
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        // 전체 스티커 번호 목록 생성
        List<Integer> availableStickers = new ArrayList<>();
        for (int i = 1; i <= total_cnt; i++) {
            if (!excludedNumbers.contains(i)) {
                availableStickers.add(i);
            }
        }
        // availableStickers에서 랜덤하게 하나 선택
        if (availableStickers.isEmpty()) {
            throw new IllegalArgumentException("No available stickers to choose from.");
        }

        Random random = new Random();
        int randomIndex = random.nextInt(availableStickers.size());
        int selectedSticker = availableStickers.get(randomIndex);

        // 선택된 숫자를 문자열로 변환하여 반환
        return String.valueOf(selectedSticker);
    }
}
