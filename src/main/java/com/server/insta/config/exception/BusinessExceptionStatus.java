package com.server.insta.config.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BusinessExceptionStatus {

    //Common
    SERVER_ERROR("SERVER","서버와의 연결에 실패했습니다."),
    METHOD_NOT_ALLOWED("METHOD","허용되지 않은 메서드입니다."),
    KAKAO_LOGIN("KAKAO","카카오 로그인에 실패했습니다."),
    DATABASE_ERROR("DB","데이터베이스 오류입니다."),
    NOT_FOUND("NF","존재하지 않는 URL 입니다."),


    //User
    USER_NOT_EXIST("U001","존재하지 않는 유저입니다."),
    USER_EXIST_USERNAME("U002","이미 존재하는 사용자 이름입니다."),
    USER_INVALID_PASSWORD("U003","비밀번호가 일치하지 않습니다."),
    USER_INVALID_OAUTH("U004","지원하지않는 oAuth 로그인 형식입니다."),
    USER_NOT_INVALID("U005","권한이 없는 유저입니다."),
    USER_NOT_SEND_SELF("U006","본인에게는 메세지를 보낼 수 없습니다."),
    USER_NOT_CHANGE_PASSWORD("U007","소셜 로그인 유저는 비밀번호를 변경할 수 없습니다."),
    USER_NOT_ADMIN("U008","관리자 계정이 아닙니다."),
    USER_AGREE_PRIVACY("U009","개인정보처리동의를 다시 받아야 합니다."),

    //Post
    POST_NOT_EXIST("P001","존재하지 않는 게시물입니다."),

    //Media

    //Tag

    //Follow
    FOLLOW_NOT_SELF("F001","본인에게 팔로우 할 수 없습니다."),
    //Likes
    LIKE_EXIST_POST("L001","이미 좋아요한 게시물 입니다."),
    LIKE_NOT_EXIST_POST("L002","좋아요를 하지 않은 게시물 입니다."),

    //Comment
    COMMENT_NOT_EXIST("C001","존재하지 않는 댓글입니다."),


    //Message
    MESSAGE_NOT_EXIST("DM001","해당 유저와 채팅한 이력이 없습니다."),

    //Report
    REPORT_NOT_POST_SELF("R001","본인 게시글은 신고할 수 없습니다."),
    REPORT_NOT_COMMENT_SELF("R002","본인 댓글은 신고할 수 없습니다."),
    REPORT_NOT_EXIST("R003","존재하지 않는 신고입니다."),

    //Admin
    ADMIN_INVALID_DATE("ADM001","날짜 형식이 올바르지 않습니다.");





    private final String code;
    private final String message;


}
