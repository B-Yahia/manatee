package ee.cyber.manatee.service;

import ee.cyber.manatee.model.Application;
import ee.cyber.manatee.model.Interview;
import ee.cyber.manatee.repository.InterviewRepository;
import ee.cyber.manatee.statemachine.ApplicationState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.OffsetDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class InterviewService {

    private final ApplicationService applicationService;
    private final InterviewRepository interviewRepository;

    @Transactional
    public Application addInterviewToApplication(Application application, Interview draftInterview) {
        val interview = interviewRepository.save(draftInterview);
        val interviewList = application.getInterviews();
        interviewList.add(interview);
        application.setInterviews(interviewList);
        application.setApplicationState(ApplicationState.INTERVIEW);
        application.setUpdatedOn(OffsetDateTime.now());
        return applicationService.saveApplication(application);
    }

}
