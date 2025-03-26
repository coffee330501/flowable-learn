package io.github.coffee330501.entity;

import lombok.Data;
import org.joda.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@Entity(name = "holiday_process")
public class HolidayProcessEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private LocalDate startTime;
    private LocalDate endTime;
    private String reason;
    private String processInstanceId;
    private String leaderApproved;
}
