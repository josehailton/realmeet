package br.com.sw2you.realmeet.unit;

import static br.com.sw2you.realmeet.domain.entity.Allocation.SORTABLE_FIELDS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import br.com.sw2you.realmeet.core.BaseUnitTest;
import br.com.sw2you.realmeet.exception.InvalidOrderByFieldException;
import br.com.sw2you.realmeet.util.PageUtils;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

public class PageUtilsUnitTest extends BaseUnitTest {

    @Test
    void testNewPageableWhenPageIsNullAndLimitIsNullAndOrderByIsNull() {
        var pageable = PageUtils.newPageable(null, null, 10, null, SORTABLE_FIELDS);

        assertEquals(0, pageable.getPageNumber());
        assertEquals(10, pageable.getPageSize());
        assertEquals(Sort.unsorted(), pageable.getSort());
    }

    @Test
    void testNewPageableWhenPageIsNegative() {
        assertThrows(
            IllegalArgumentException.class,
            () -> {
                PageUtils.newPageable(-1, null, 10, null, SORTABLE_FIELDS);
            }
        );
    }

    @Test
    void testNewPageableWhenPageIsInvalid() {
        assertThrows(
            IllegalArgumentException.class,
            () -> {
                PageUtils.newPageable(null, 0, 10, null, SORTABLE_FIELDS);
            }
        );
    }

    @Test
    void testNewPageableWhenPageLimitExceedsMaximum() {
        var pageable = PageUtils.newPageable(null, 30, 10, null, SORTABLE_FIELDS);
        assertEquals(10, pageable.getPageSize());
    }

    @Test
    void testNewPageableWhenOrderByIsNull() {
        assertThrows(
            IllegalArgumentException.class,
            () -> {
                PageUtils.newPageable(null, 0, 10, null, null);
            }
        );
    }

    @Test
    void testNewPageableWhenOrderByIsEmpty() {
        assertThrows(
            IllegalArgumentException.class,
            () -> {
                PageUtils.newPageable(null, 0, 10, null, Collections.emptyList());
            }
        );
    }

    @Test
    void testNewPageableWhenOrderByAscIsValid() {
        var pageable = PageUtils.newPageable(null, null, 10, SORTABLE_FIELDS.get(0), SORTABLE_FIELDS);
        assertEquals(Sort.by(Sort.Order.asc(SORTABLE_FIELDS.get(0))), pageable.getSort());
    }

    @Test
    void testNewPageableWhenOrderByDescIsValid() {
        var pageable = PageUtils.newPageable(null, null, 10, "-" + SORTABLE_FIELDS.get(0), SORTABLE_FIELDS);
        assertEquals(Sort.by(Sort.Order.desc(SORTABLE_FIELDS.get(0))), pageable.getSort());
    }

    @Test
    void testNewPageableWhenOrderByFieldIsInvalid() {
        assertThrows(
            InvalidOrderByFieldException.class,
            () -> {
                PageUtils.newPageable(null, null, 10, "invalid", SORTABLE_FIELDS);
            }
        );
    }
}
