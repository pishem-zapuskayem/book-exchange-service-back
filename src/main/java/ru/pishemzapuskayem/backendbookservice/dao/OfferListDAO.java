package ru.pishemzapuskayem.backendbookservice.dao;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.pishemzapuskayem.backendbookservice.model.entity.Category;
import ru.pishemzapuskayem.backendbookservice.model.entity.OfferList;
import ru.pishemzapuskayem.backendbookservice.model.entity.UserList;
import ru.pishemzapuskayem.backendbookservice.model.entity.UserValueCategory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Repository
public class OfferListDAO {
    public final RowMapper<OfferList> offerListRowMapper;
    public final RowMapper<UserList> userListRowMapper;
    public final RowMapper<UserValueCategory> userValueCategoryRowMapper;
    public final RowMapper<Category> categoryRowMapper;

    private final JdbcTemplate jdbcTemplate;

    @SneakyThrows
    public List<OfferList> findOfferListsByStatus(int status) {
        Map<Long, OfferList> offerListMap = new HashMap<>();
        Map<Long, UserList> userListMap = new HashMap<>();

        jdbcTemplate.query(
            "SELECT ol.*, ul.*, uvc.*, c.* FROM offer_list ol " +
                "LEFT JOIN user_list ul ON ol.id = ul.offer_list_id " +
                "LEFT JOIN user_value_category uvc ON ul.id = uvc.user_list_id " +
                "LEFT JOIN category c ON uvc.category_id = c.id " +
                "WHERE ol.status = ?",
            rs -> {
                Long offerListId = rs.getLong("ol.id");
                OfferList offerList = offerListMap.computeIfAbsent(offerListId, id -> {
                    try {
                        return offerListRowMapper.mapRow(rs, 0);
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
                    if (!offerList.getUserLists().contains(userList)) {
                        offerList.getUserLists().add(userList);
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

        return new ArrayList<>(offerListMap.values());
    }
}
