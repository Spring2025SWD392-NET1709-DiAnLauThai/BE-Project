package com.be.back_end.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.w3c.dom.stylesheets.LinkStyle;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class TshirtsUpdateRequest {
    private String tshirtId;
    private String name;
    private String description;
    private String imageFile;
    private String imageUrl;
    private List<TshirtColorUpdateRequest> colors;
    @Getter
    @Setter
    public static class TshirtColorUpdateRequest{
        private String colorId;

    }
}
