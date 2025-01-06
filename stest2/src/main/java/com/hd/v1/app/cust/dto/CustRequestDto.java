package com.hd.v1.app.cust.dto;

import com.hd.v1.common.entity.CustEntity;
import com.hd.v1.common.entity.ItemEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CustRequestDto {
    String id;
    @NotNull
    @Schema(description = "Cust Password",
            example = "password",
            required = true,
            type = "String"
    )
    String pwd;
    @NotNull
    @Schema(description = "Cust Name",
            example = "Mike",
            required = true,
            type = "String"
    )
    String name;

    public CustEntity toEntity() {
        return CustEntity.builder()
                .id(this.id)
                .pwd(this.pwd)
                .name(this.name)
                .build();
    }

}
