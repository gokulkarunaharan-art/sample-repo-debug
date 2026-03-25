package com.gokul.librarymanagement.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UploadSummaryDTO {
    Integer totalRows;
    Integer inserted;
    Integer skipped;
}
