package code.mogaktae.domain.common.util;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CursorBasedPaginationCollection<T> {

    private final List<T> contents;
    private final int size;

    public static <T> CursorBasedPaginationCollection<T> of (List<T> contents, int size){
        return new CursorBasedPaginationCollection<>(contents, size);
    }

    public Boolean isLastCursor(){
        return this.contents.size() <= size;
    }

    public List<T> getContents(){
        if(Boolean.TRUE.equals(isLastCursor()))
            return this.contents;
        return this.contents.subList(0, size);
    }

    public T getNextCursor(){
        return contents.get(size -1);
    }
}

