package com.greaticker.demo.dto.response.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CursorPaginationMeta {
    private long count;
    private boolean hasMore;
}
