package id.segari.ortools.dto;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationInfoDTO {
    private String commitId;
    private String buildAt;
}
