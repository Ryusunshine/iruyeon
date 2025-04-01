package com.iruyeon.v1.config.common;

import com.iruyeon.v1.config.exception.BaseException;
import com.iruyeon.v1.config.response.ErrorCode;
import com.iruyeon.v1.domain.member.entity.Member;
import com.iruyeon.v1.domain.member.enums.Role;
import com.iruyeon.v1.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;


@Slf4j
@Service
@RequiredArgsConstructor
public class OAuth2UserCustomService extends DefaultOAuth2UserService {
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;

    /**
     * OAuth2 로그인 요청이 들어오면 loadUser() 실행.
     * OAuth2 제공자(Google, GitHub 등)로부터 사용자 정보를 가져옴.
     * 사용자 정보를 표준화된 OAuth2Attributes 객체로 변환.
     * DB에서 해당 사용자가 존재하는지 확인 후 저장 또는 업데이트.
     * 사용자 정보를 담은 CustomOAuth2User 객체를 생성하여 반환.
     */

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("OAuth2UserCustomService.loadUser() 실행 - OAuth2 로그인 요청 진입");

        //Spring Security의 기본 OAuth2UserService(DefaultOAuth2UserService)를 이용해 OAuth2 제공자로부터 사용자 정보를 가져옴.
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        //ClientRegistration = Spring Security의 OAuth2 클라이언트에서 사용되는 객체로,
        // OAuth2 제공자(Google, Facebook 등)에 대한 **클라이언트 정보(클라이언트 ID, 비밀 키, 리디렉션 URI 등)**를 저장하는 역할을 합니다.
        // registrationId = ClientRegistration 객체에서 OAuth2 제공자의 고유한 식별자
        // Google 로그인을 사용하면 "google", GitHub 로그인을 사용하면 "github" 이런 식으로 나옴.
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        //OAuth2 제공자의 API에서 사용자 정보(프로필, 이메일 등)를 가져올 때 사용할 키 값을 가져옴.
        //예: Google → "sub" 에 대한 키 값(socialId)
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        //OAuth2Attributes.of를 통해 OAuth2 제공자의 사용자 정보를 표준화된 객체(OAuth2Attributes)로 변환.
        OAuth2Attributes extractAttributes = OAuth2Attributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        //attributes는 API가 제공하는 사용자 정보 JSON 데이터를 저장.
        Map<String, Object> attributes = oAuth2User.getAttributes(); // 소셜 로그인에서 API가 제공하는 userInfo의 Json 값(유저 정보들)

        Member member = saveOrUpdate(extractAttributes);

        // DefaultOAuth2User를 구현한 CustomOAuth2User 객체를 생성해서 반환
        return new CustomOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(member.getRole().name())),
                attributes,
                extractAttributes.getNameAttributeKey(),
                extractAttributes.getEmail(),
                member.getRole(),
                member.getId()
        );
    }

    private Member saveOrUpdate(OAuth2Attributes attributes) {
        Member member = memberRepository.findBySocialId(attributes.getNameAttributeKey());

        try {
            if (member != null) {
                member.updateName(attributes.getName());

            } else {
                member = memberRepository.save(Member.builder()
                        .socialType(attributes.getSocialType())
                        .email(attributes.getEmail())
                        .name(attributes.getName())
                        .role(Role.ANONYMOUS)
                        .socialId(attributes.getNameAttributeKey())
                        .build());
            }
            return member;
        } catch (Exception e) {
            throw new BaseException(ErrorCode.COMMON_INTERNAL_SERVER_ERROR);
        }
    }
}
