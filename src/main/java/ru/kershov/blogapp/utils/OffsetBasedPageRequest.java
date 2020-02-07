package ru.kershov.blogapp.utils;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(of = {"offset", "limit", "sort"})
public class OffsetBasedPageRequest implements Pageable {
    private int offset;
    private int limit;
    private final Sort sort;

    public OffsetBasedPageRequest(int offset, int limit, Sort sort) {
        if (offset < 0) {
            throw new IllegalArgumentException("Offset index must not be less than zero!");
        }

        if (limit < 1) {
            throw new IllegalArgumentException("Limit must not be less than one!");
        }

        this.limit = limit;
        this.offset = offset;
        this.sort = sort;
    }

    public OffsetBasedPageRequest(int offset, int limit, Sort.Direction direction, String... properties) {
        this(offset, limit, Sort.by(direction, properties));
    }

    public OffsetBasedPageRequest(int offset, int limit) {
        this(offset, limit, Sort.by(Sort.Direction.ASC,"id"));
    }

    @Override
    public int getPageNumber() {
        return offset / limit;
    }

    @Override
    public int getPageSize() {
        return limit;
    }

    @Override
    public long getOffset() {
        return offset;
    }

    @Override
    public Sort getSort() {
        return sort;
    }

    @Override
    public Pageable next() {
        return new OffsetBasedPageRequest((int) (getOffset() + getPageSize()), getPageSize(), getSort());
    }

    public OffsetBasedPageRequest previous() {
        return hasPrevious() ? new OffsetBasedPageRequest((int) (getOffset() - getPageSize()), getPageSize(), getSort()) : this;
    }

    @Override
    public Pageable previousOrFirst() {
        return hasPrevious() ? previous() : first();
    }

    @Override
    public Pageable first() {
        return new OffsetBasedPageRequest(0, getPageSize(), getSort());
    }

    @Override
    public boolean hasPrevious() {
        return offset > limit;
    }
}
