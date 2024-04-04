package ru.pishemzapuskayem.backendbookservice.dao;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.pishemzapuskayem.backendbookservice.model.entity.Category;
import ru.pishemzapuskayem.backendbookservice.model.entity.OfferList;
import ru.pishemzapuskayem.backendbookservice.model.entity.UserList;
import ru.pishemzapuskayem.backendbookservice.model.entity.UserValueCategory;
import ru.pishemzapuskayem.backendbookservice.model.entity.WishList;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
@RequiredArgsConstructor
public class WishListDAO {
    public final RowMapper<WishList> wishListRowMapper;
    public final RowMapper<UserList> userListRowMapper;
    public final RowMapper<UserValueCategory> userValueCategoryRowMapper;
    public final RowMapper<Category> categoryRowMapper;

    private final JdbcTemplate jdbcTemplate;

    @SneakyThrows
    public List<WishList> findWishListsByStatus(int status) {
        Map<Long, WishList> wishListMap = new HashMap<>();
        Map<Long, UserList> userListMap = new HashMap<>();

        jdbcTemplate.query(
            "SELECT wl.*, ul.*, uvc.*, c.* FROM wish_list wl " +
                "LEFT JOIN user_list ul ON wl.id = ul.wish_list_id " +
                "LEFT JOIN user_value_category uvc ON ul.id = uvc.user_list_id " +
                "LEFT JOIN category c ON uvc.category_id = c.id " +
                "WHERE wl.status = ?",
            rs -> {
                Long wishListId = rs.getLong("wl.id");
                WishList wishList = wishListMap.computeIfAbsent(wishListId, id -> {
                    try {
                        return wishListRowMapper.mapRow(rs, 0);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });

                Long userListId = rs.getLong("ul.id");
                if (!rs.wasNull()) {
                    UserList userList = userListMap.computeIfAbsent(userListId, id -> {
                        try {
                            return userListRowMapper.mapRow(rs, 0);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    if (!wishList.getUserLists().contains(userList)) {
                        wishList.getUserLists().add(userList);
                    }

                    Long categoryId = rs.getLong("c.id");
                    if (!rs.wasNull()) {
                        Category category = categoryRowMapper.mapRow(rs, 0);
                        UserValueCategory userValueCategory = userValueCategoryRowMapper.mapRow(rs, 0);
                        userValueCategory.setCategory(category);
                        userList.getCategories().add(userValueCategory);
                    }
                }
            }, status
        );

        return new ArrayList<>(wishListMap.values());
    }
}
