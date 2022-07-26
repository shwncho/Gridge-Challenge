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


    //User
    USER_NOT_EXIST("U001","존재하지 않는 유저입니다."),
    USER_EXIST_ACCOUNT("U002","이미 존재하정 계정입니다."),
    USER_INVALID_PASSWORD("U003","비밀번호가 일치하지 않습니다."),
    USER_INVALID_OAUTH("U004","지원하지않는 oAuth 로그인 형식입니다."),
    USER_NOT_INVALID("U005","권한이 없는 유저입니다."),

    //Post
    POST_NOT_EXIST("P001","존재하지 않는 게시물입니다."),

    //Media

    //Tag

    //Follow
    FOLLOW_EXIST_RELATIONSHIP("F001","이미 팔로우한 관계입니다."),
    FOLLOW_NOT_EXIST_RELATIONSHIP("F002","이미 팔로우관계가 아닙니다."),
    //Likes
    LIKE_EXIST_POST("L001","이미 좋아요한 게시물 입니다."),
    LIKE_NOT_EXIST_POST("L002","좋아요를 하지 않은 게시물 입니다."),

    //Comment
    COMMENT_NOT_EXIST("C001","존재하지 않는 댓글입니다.");








    private final String code;
    private final String message;


}
