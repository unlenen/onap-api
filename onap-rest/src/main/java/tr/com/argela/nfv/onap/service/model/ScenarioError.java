package tr.com.argela.nfv.onap.service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ScenarioError {

    String errorCode;
    String message;
    @JsonIgnore
    Exception e;
}
