package goorm_runner.backend.member.security.config.oauth2;

import goorm_runner.backend.member.application.MemberRepository;
import goorm_runner.backend.member.domain.Member;
import goorm_runner.backend.member.domain.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOauth2UserService extends DefaultOAuth2UserService {

    private  MemberRepository memberRepository;

    @Autowired
    public void setMemberRepository(MemberRepository memberRepository)
    {
        this.memberRepository=memberRepository;
    }


    @Override//loadUser 메서드를 override 해줘야함 !
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
       OAuth2User oauth2User=super.loadUser(userRequest);// loadUser메서드에 요청을 보내서 oauth2정보취득
        String email=oauth2User.getAttribute("email");//보통 카카오 , 구글 등은 이메일 형식 !
        String name=oauth2User.getAttribute("name");

        Member member=memberRepository.findByLoginId(email)
                .orElseGet(()->createMember(email,name));

        return new CustomOauth2User(member,oauth2User.getAttributes());
    }

    private Member createMember(String email,String name)
    {
        Member member=new Member();
        member.setLoginId(email);
        member.setNickname(name);
        member.setRole(Role.USER);
        return memberRepository.save(member);

    }


}
