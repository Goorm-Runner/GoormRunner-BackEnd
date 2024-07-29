package goorm_runner.backend.picture.application;

import goorm_runner.backend.member.application.MemberRepository;
import goorm_runner.backend.member.domain.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class UpdateMemberServiceTest {
    @MockBean
    private MemberRepository memberRepository;

    @Autowired
    private UpdateMemberService updateMemberService;

    @Test
    void testGetMemberIdByLoginId() {
        Member member = Member.builder()
                .loginId("testLogin")
                .username("testUser")
                .build();

        when(memberRepository.findByLoginId("testLogin")).thenReturn(Optional.of(member));

        Long memberId = updateMemberService.getMemberIdByLoginId("testLogin");
        assertNotNull(memberId);
        assertEquals(member.getId(), memberId);
    }

    @Test
    void testUpdateMemberProfileImage() {
        Member member = Member.builder()
                .loginId("testLogin")
                .username("testUser")
                .build();

        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        updateMemberService.updateMemberProfileImage(1L, "newImageUrl");
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    void testRemoveMemberProfileImage() {
        Member member = Member.builder()
                .loginId("testLogin")
                .username("testUser")
                .build();

        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        updateMemberService.removeMemberProfileImage(1L);
        verify(memberRepository, times(1)).save(any(Member.class));
    }
}