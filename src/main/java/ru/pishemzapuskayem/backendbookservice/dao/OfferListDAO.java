package ru.pishemzapuskayem.backendbookservice.dao;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.pishemzapuskayem.backendbookservice.model.entity.BookLiterary;
import ru.pishemzapuskayem.backendbookservice.model.entity.Category;
import ru.pishemzapuskayem.backendbookservice.model.entity.OfferList;
import ru.pishemzapuskayem.backendbookservice.model.entity.ListType;
import ru.pishemzapuskayem.backendbookservice.model.entity.UserList;
import ru.pishemzapuskayem.backendbookservice.model.entity.UserValueCategory;
import ru.pishemzapuskayem.backendbookservice.model.entity.message.Status;

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
        Map<Long, Category> categoryMap = new HashMap<>();

        jdbcTemplate.query(
            """
                SELECT
                    ol.id AS ol_id,
                    ol.created_at AS ol_created_at,
                    ol.status AS ol_status,
                    ul.id AS ul_id,
                    ul.list_type AS ul_list_type,
                    ul.offer_list_id AS ul_offer_list_id,
                    ul.wish_list_id AS ul_wish_list_id,
                    uvc.id AS uvc_id,
                    uvc.user_list_id AS uvc_user_list_id,
                    uvc.category_id AS uvc_category_id,
                    c.id AS c_id,
                    c.name AS c_name,
                    c.parent_id AS c_parent_id,
                    c.multiselect AS c_multiselect,
                    bl.id AS bl_id,
                    bl.booke_name AS bl_book_name,
                    bl.isbn AS bl_isbn,
                    bl.publish_year AS bl_publish_year,
                    bl.note AS bl_note 
                FROM offer_list ol
                LEFT JOIN user_list ul ON ol.id = ul.offer_list_id
                LEFT JOIN user_value_category uvc ON ul.id = uvc.user_list_id
                LEFT JOIN category c ON uvc.category_id = c.id
                LEFT JOIN book_literary bl ON ol.id_book_literary = bl.id
                WHERE ol.status = ?
                """,
            rs -> {
                Long offerListId = rs.getLong("ol_id");
                OfferList offerList = offerListMap.computeIfAbsent(offerListId, id -> {
                    try {
                        return new OfferList()
                            .setId(id)
                            .setCreatedAt(rs.getTimestamp("ol_created_at").toLocalDateTime())
                            .setStatus(Status.byId(rs.getInt("ol_status")))
                            .setUserLists(new ArrayList<>())
                            .setBookLiterary(new BookLiterary()
                                .setId(rs.getLong("bl_id"))
                                .setBookName(rs.getString("bl_book_name"))
                                .setIsbn(rs.getString("bl_isbn"))
                                .setPublishYear(rs.getInt("bl_publish_year"))
                                .setNote(rs.getString("bl_note"))
                            );
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });

                Long userListId = rs.getLong("ul_id");
                if (!rs.wasNull()) {
                    UserList userList = userListMap.computeIfAbsent(userListId, id -> {
                        try {
                            return new UserList()
                                .setId(id)
                                .setListType(ListType.byId(rs.getInt("ul_list_type")))
                                .setOfferList(offerList)
                                .setCategories(new ArrayList<>());
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    });

                    if (!offerList.getUserLists().contains(userList)) {
                        offerList.getUserLists().add(userList);
                    }

                    Long categoryId = rs.getLong("c_id");
                    if (!rs.wasNull()) {
                        Category category = categoryMap.computeIfAbsent(categoryId, id -> {
                            try {
                                return new Category()
                                    .setId(id)
                                    .setName(rs.getString("c_name"))
                                    .setParentId(rs.getLong("c_parent_id"))
                                    .setMultiselect(rs.getObject("c_multiselect", Boolean.class));
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        });

                        UserValueCategory userValueCategory = new UserValueCategory()
                            .setUserList(userList)
                            .setCategory(category);

                        userList.getCategories().add(userValueCategory);
                    }
                }
            }, status
        );

        return new ArrayList<>(offerListMap.values());
    }
}
