package by.smertex.dto.read;

import lombok.Value;
import org.springframework.data.domain.Page;

import java.util.List;

@Value
public class PageResponse <T>{

    List<T> content;

    Metadata<T> metadata;

    public static <T> PageResponse<T> of(Page<T> page){
        return new PageResponse<T>(
                page.getContent(),
                new Metadata<>(page.getNumber(), page.getSize(), page.getTotalElements())
        );
    }

    @Value
    private static class Metadata <T>{
        int page;

        int size;

        long totalElements;
    }
}
