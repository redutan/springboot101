package xyz.groundx.helloworld;

import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;

class HitControllerTest {
    HitController controller;

    @BeforeEach
    void setUp() {
        controller = new HitController();
    }

    @Disabled("hit2Times 으로 대치")
    @Test
    void hit() {
        var result = controller.hit();

        assertThat(result).isEqualTo(1L);
    }

    @Disabled("오류가 있어서 제외")
    @DisplayName("hit 여러 번 테스트")
    @RepeatedTest(value = 10, name = "hit {currentRepetition}/{totalRepetitions}")
    void hitManyTimes(RepetitionInfo ri) {
        var result = controller.hit();

        var i = ri.getCurrentRepetition();
        assertThat(result).isEqualTo(i);
    }

    @DisplayName("hit 2번 테스트")
    @Test
    void hit2Times() {
        assertThat(controller.hit()).isEqualTo(1L);
        assertThat(controller.hit()).isEqualTo(2L);
    }
}