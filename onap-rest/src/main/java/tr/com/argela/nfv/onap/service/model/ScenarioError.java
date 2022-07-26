package tr.com.argela.nfv.onap.service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ScenarioError {

    String errorCode;
    String message;
    Exception e;
}
