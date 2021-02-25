package com.meeting.appo.dao;

import com.meeting.appo.entities.Status;

import java.util.List;

public interface EventStatusDao {
    List<Status> getStatusList();
}
