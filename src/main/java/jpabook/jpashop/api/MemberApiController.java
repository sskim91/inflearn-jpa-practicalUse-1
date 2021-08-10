package jpabook.jpashop.api;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

/**
 * @author sskim
 */
@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    /**
     * 등록 V1: 요청 값으로 Member 엔티티를 직접 받는다.
     * 문제점 * - 엔티티에 프레젠테이션 계층을 위한 로직이 추가된다.
     * - 엔티티에 API 검증을 위한 로직이 들어간다. (@NotEmpty 등등) * - 실무에서는 회원 엔티티를 위한 API가 다양하게 만들어지는데, 한 엔티티에 각각의 API를 위한 모든 요청 요구사항을 담기는 어렵다.
     * - 엔티티가 변경되면 API 스펙이 변한다.
     * 결론 * - API 요청 스펙에 맞추어 별도의 DTO를 파라미터로 받는다.
     */
    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    /**
     * 등록 V2: 요청 값으로 Member 엔티티 대신에 별도의 DTO를 받는다.
     */
    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {
        Member member = new Member();
        member.setName(request.getName());
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    /**
     * 수정 API
     * 커멘드와 쿼리를 분리하는 것은 단순히 효율성을 넘어, 코드를 유지보수 하기 쉽게 만드는 방법중 하나
     * 변경을 하는 메서드와 단순히 조회를 하는 메서드를 아주 명확하게 분리해버리면, 변경 코드는 변경에만 집중하고, 조회 코드는 조회에만 집중할 수 있습니다.
     */
    @PostMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(@PathVariable Long id,
                                               @RequestBody @Valid UpdateMemberRequest request) {
        memberService.update(id, request.getName());
        Member findMember = memberService.findOne(id);
        return new UpdateMemberResponse(findMember.getId(), findMember.getName());
    }

    /**
     * 이너클래스 사용하는 이유?
     * 이너 클래스는 이너 클래스를 포함하는 클래스 안에서만 한정적으로 접근할 때만 사용합니다.
     * 만약 여러 클래스에서 접근해야 하면 외부 클래스로 사용하는 것이 맞습니다^^
     * 이너클래스의 이점
     *  - 해당 클래스 안에서만 한정적으로 사용한다는 의미 부여
     *  - 개발자 입장에서 신경써야하는 외부 클래들이 줄어드는 효과
     */
    @Data
    static class CreateMemberRequest {
        @NotEmpty
        private String name;
    }

    /**
     * 왜 클래스들에 static을 붙일까?
     * 내부 클래스에 static을 선언하지 않으면 MemberController 클래스 외부에서 이 객체를 직접 생성할 수 없습니다.
     * Response는 클래스 내부에서 생성해서 반환하기 때문에 static이 없어도 됩니다.
     * 반면에 Request는 클래스 외부에서 생성해서 들어오기 때문에 static이 없으면 객체를 생성할 수 없습니다.
     * 근데 Intellij 에서 make static으로 만들라고 한다.
     */

    @Data
    @AllArgsConstructor
    static class CreateMemberResponse {
        private Long id;
    }

    @Data
    static class UpdateMemberRequest {
        private String name;
    }

    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse {
        private Long id;
        private String name;
    }


}
