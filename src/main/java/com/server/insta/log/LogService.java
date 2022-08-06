package com.server.insta.log;

public interface LogService {

    void add(String text);

    void deleteLog(String adminId);
}
