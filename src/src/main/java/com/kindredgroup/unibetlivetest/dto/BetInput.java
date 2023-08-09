package com.kindredgroup.unibetlivetest.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;


@Jacksonized
@Builder
@Getter
public class BetInput {

    @NotNull
    @Positive
    private final Long selectionId;

    @NotNull
    @Min(1)
    private final BigDecimal odd;

    @NotNull
    @Positive
    private final BigDecimal amount;
}
