package goorm_runner.backend.member.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SexTest {
    @Test
    void fromCode() {
        Sex male = Sex.fromCode('1');
        assertThat(male == Sex.MALE).isTrue();
    }

    @Test
    void getCode() {
        Sex male = Sex.MALE;
        assertThat(male.getCode()).isEqualTo('1');
    }
}