package com.iruyeon.v1.domain.match.enums;

public enum MatchStatus {
    PENDING,        // 호감을 보낸 상태
    REJECTED,       // 상대가 거절
    MATCHED,        // 매칭 완료
    DEACTIVATED_USER, // 호감 받은 사람 탈퇴
    DELETED // 삭제된 매칭
}
