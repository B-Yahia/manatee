package ee.cyber.manatee.model;


import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import ee.cyber.manatee.statemachine.ApplicationState;


@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Application {

    @Id
    @GeneratedValue
    private Integer id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ApplicationState applicationState;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Interview> interviews;

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL)
    private Candidate candidate;

    @NotNull
    private OffsetDateTime updatedOn;

    public void addInterview(Interview interview) {
        if (interviews == null) {
            interviews = new ArrayList<>();
        }
        interviews.add(interview);
    }
    public List<Interview> getInterviews() {
        if (interviews == null) {
            interviews = new ArrayList<>();
        }
        return interviews;
    }
}
