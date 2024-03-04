package id.segari.ortools.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ResponseDTO<T> {
    T body;
    String errors;
}
