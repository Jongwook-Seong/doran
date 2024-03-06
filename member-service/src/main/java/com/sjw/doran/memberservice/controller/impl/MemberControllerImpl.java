package com.sjw.doran.memberservice.controller.impl;

import com.sjw.doran.memberservice.controller.MemberController;
import com.sjw.doran.memberservice.entity.MemberEntity;
import com.sjw.doran.memberservice.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberControllerImpl implements MemberController {

    private final MemberService memberService;

    @GetMapping("/members")
    public List<MemberEntity> members() {
        return memberService.findMembers();
    }

    @PostMapping("/member")
    public void saveMember(@RequestBody MemberEntity member) {
        memberService.saveMember(member);
    }
}
