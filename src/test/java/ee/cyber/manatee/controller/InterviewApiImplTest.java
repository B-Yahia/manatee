package ee.cyber.manatee.controller;

import ee.cyber.manatee.api.InterviewApi;
import ee.cyber.manatee.dto.ApplicationDto;
import ee.cyber.manatee.dto.CandidateDto;
import ee.cyber.manatee.dto.InterviewDto;
import ee.cyber.manatee.dto.InterviewTypeDto;
import ee.cyber.manatee.mapper.ApplicationMapper;
import ee.cyber.manatee.service.ApplicationService;
import ee.cyber.manatee.statemachine.ApplicationState;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class InterviewApiImplTest {

    @Autowired
    private InterviewApi interviewApi;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private ApplicationMapper applicationMapper;

    @Test
    void scheduleInterview() {
        //Create entities
        val draftCandidate = CandidateDto
                .builder().firstName("Mari").lastName("Maasikas").build();
        val draftApplicationDto = ApplicationDto
                .builder().candidate(draftCandidate).build();
        val draftInterview = InterviewDto.builder().interviewType(InterviewTypeDto.INFORMAL)
                .time(OffsetDateTime.now()).interviewerName("Yahya BAHHOUSS").build();
        //Save application
        val savedApplication =applicationService.insertApplication(applicationMapper.dtoToEntity(draftApplicationDto));

        //Test the endpoint to schedule interview
        val response = interviewApi.scheduleInterview(savedApplication.getId(),draftInterview);

        val updatedApplication = applicationService.getApplication(savedApplication.getId());

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        val savedInterviewDto = response.getBody();
        assertNotNull(savedInterviewDto.getInterviewerName());
        assertNotNull(savedInterviewDto.getInterviewType());
        assertNotNull(savedInterviewDto.getTime());

        //Test The status of ApplicationState
        assertEquals(draftInterview.getInterviewType(),savedInterviewDto.getInterviewType());
        assertEquals(draftInterview.getInterviewerName(),savedInterviewDto.getInterviewerName());

        //Test The status of ApplicationState
        assertEquals(ApplicationState.INTERVIEW,updatedApplication.getApplicationState());
    }
}