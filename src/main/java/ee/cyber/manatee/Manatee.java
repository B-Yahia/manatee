package ee.cyber.manatee;


import ee.cyber.manatee.dto.ApplicationDto;
import ee.cyber.manatee.dto.CandidateDto;
import ee.cyber.manatee.dto.InterviewDto;
import ee.cyber.manatee.dto.InterviewTypeDto;
import ee.cyber.manatee.mapper.ApplicationMapper;
import ee.cyber.manatee.mapper.InterviewMapper;
import ee.cyber.manatee.model.Application;
import ee.cyber.manatee.service.ApplicationService;
import ee.cyber.manatee.service.InterviewService;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;


@SpringBootApplication
public class Manatee
        implements CommandLineRunner
{
    @Autowired
    private ApplicationService applicationService;
    @Autowired
    private ApplicationMapper applicationMapper;
    @Autowired
    private InterviewService interviewService;
    @Autowired
    private InterviewMapper interviewMapper;

    public static void main(String[] args) {
        SpringApplication.run(Manatee.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        List<Application> applications = new ArrayList<>();
        val draftInterview1 = InterviewDto.builder().interviewType(InterviewTypeDto.INFORMAL)
                .time(OffsetDateTime.now()).interviewerName("interviewer1").build();
        val draftInterview2 = InterviewDto.builder().interviewType(InterviewTypeDto.TECHNICAL)
                .time(OffsetDateTime.now()).interviewerName("interviewer2").build();
        for (int i=1 ;i<=6;i++){
            val draftCandidate= CandidateDto.builder().firstName("Firstname"+i).lastName("Lastname"+i).build();
            val draftApplicationDto = ApplicationDto.builder().candidate(draftCandidate).build();
            if (i<3){
                interviewService.addInterviewToApplication(applicationService.insertApplication(applicationMapper.dtoToEntity(draftApplicationDto)),interviewMapper.dtoToEntity(draftInterview1));
            }else if (i<5){
                interviewService.addInterviewToApplication(applicationService.insertApplication(applicationMapper.dtoToEntity(draftApplicationDto)),interviewMapper.dtoToEntity(draftInterview2));
            }else {
                applicationService.insertApplication(applicationMapper.dtoToEntity(draftApplicationDto));
            }
        }



    }
}

