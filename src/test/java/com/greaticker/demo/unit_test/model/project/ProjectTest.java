package com.greaticker.demo.unit_test.model.project;

import com.greaticker.demo.constants.enums.project.ProjectState;
import com.greaticker.demo.model.project.Project;
import com.greaticker.demo.model.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ProjectTest {

    private Project project;

    @BeforeEach
    void setUp() {
        // Arrange: Project 객체 초기화
        User user = new User(); // 적절히 초기화된 User 객체
        project = new Project(
                1L,                    // id
                ProjectState.IN_PROGRESS,  // state
                "Test Project",          // name
                LocalDateTime.now(),     // start_date
                5,                       // day_in_a_row 초기 값
                user                     // user
        );
    }

    @Test
    void testPlusDayInARow() {
        // Act: plusDayInARow 메서드 호출
        project.plusDayInARow();

        // Assert: day_in_a_row 값이 1 증가했는지 확인
        assertThat(project.getDay_in_a_row()).isEqualTo(6);
    }

    @Test
    void testPlusDayInARowMultipleTimes() {
        // Act: plusDayInARow 메서드 여러 번 호출
        project.plusDayInARow();
        project.plusDayInARow();

        // Assert: day_in_a_row 값이 2 증가했는지 확인
        assertThat(project.getDay_in_a_row()).isEqualTo(7);
    }
}
