package com.greaticker.demo.intergration_test.repository.history;

import com.greaticker.demo.constants.enums.history.HistoryKind;
import com.greaticker.demo.dto.request.PaginationParam;
import com.greaticker.demo.model.history.History;
import com.greaticker.demo.model.user.User;
import com.greaticker.demo.repository.history.HistoryRepository;
import com.greaticker.demo.repository.user.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class HistoryRepositoryTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private HistoryRepository historyRepository;
    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    public void setUp() {
        // 사용자 및 히스토리 데이터 생성
        user = new User();
        user.setAuthId("auth001");
        user.setStickerInventory("[]");
        user.setHitFavoriteList("[]");
        user.setCreatedDateTime(LocalDateTime.of(2024, 8, 24, 10, 0, 0));
        user.setUpdatedDateTime(LocalDateTime.of(2024, 8, 24, 10, 0, 0));

        userRepository.save(user);

        for (int i = 1; i <= 15; i++) {
            History history = new History();
            history.setKind(HistoryKind.GET_STICKER);
            history.setUser(user);
            history.setProject_name("앱 만들기 " + i);
            history.setSticker_id(15L);
            history.setDay_in_a_row(15);
            historyRepository.save(history);
        }
    }

    @AfterEach
    public void clearDatabase() {
        historyRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void testFindHistoriesAfter() {
        PaginationParam paginationParam = new PaginationParam();
        paginationParam.setAfter(null); // 첫 페이지 조회
        paginationParam.setCount(10); // 10개 조회

        List<History> histories = historyRepository.findHistoriesAfter(user.getId(), paginationParam);

        //hasMore 트릭을 위해서 11개 가져오는 게 맞음
        assertThat(histories).hasSize(11);
        assertThat(histories.get(0).getProject_name()).isEqualTo("앱 만들기 1");
        assertThat(histories.get(9).getProject_name()).isEqualTo("앱 만들기 10");

        // 다음 페이지 조회
        paginationParam.setAfter(histories.get(9).getId());
        histories = historyRepository.findHistoriesAfter(user.getId(), paginationParam);

        assertThat(histories).hasSize(5); // 남은 5개만 조회
        assertThat(histories.get(0).getProject_name()).isEqualTo("앱 만들기 11");
        assertThat(histories.get(4).getProject_name()).isEqualTo("앱 만들기 15");
    }
}
