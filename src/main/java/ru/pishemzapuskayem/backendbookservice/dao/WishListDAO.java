package ru.pishemzapuskayem.backendbookservice.dao;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.pishemzapuskayem.backendbookservice.model.entity.AccountAddress;
import ru.pishemzapuskayem.backendbookservice.model.entity.Category;
import ru.pishemzapuskayem.backendbookservice.model.entity.OfferList;
import ru.pishemzapuskayem.backendbookservice.model.entity.TypeList;
import ru.pishemzapuskayem.backendbookservice.model.entity.UserList;
import ru.pishemzapuskayem.backendbookservice.model.entity.UserValueCategory;
import ru.pishemzapuskayem.backendbookservice.model.entity.WishList;
import ru.pishemzapuskayem.backendbookservice.model.entity.message.Status;

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
        Map<Long, Category> categoryMap = new HashMap<>();

        jdbcTemplate.query(
            """
                    SELECT
                        wl.id AS wl_id,
                        wl.created_at AS wl_created_at,
                        wl.updated_at AS wl_updated_at,
                        wl.status AS wl_status,
                        wl.id_user_address AS wl_id_user_address,
                        ul.id AS ul_id,
                        ul.list_type AS ul_list_type,
                        ul.wish_list_id AS ul_wish_list_id,
                        ul.offer_list_id AS ul_offer_list_id,
                        uvc.id AS uvc_id,
                        uvc.user_list_id AS uvc_user_list_id,
                        uvc.category_id AS uvc_category_id,
                        c.id AS c_id,
                        c.name AS c_name,
                        c.parent_id AS c_parent_id,
                        c.multiselect AS c_multiselect,
                        aa.id AS aa_id,
                        aa.addr_index AS aa_addr_index,
                        aa.addr_city AS aa_addr_city,
                        aa.addr_street AS aa_addr_street,
                        aa.addr_house AS aa_addr_house,
                        aa.addr_structure AS aa_addr_structure,
                        aa.addr_apart AS aa_addr_apart,
                        aa.is_default AS aa_is_default
                    FROM wish_list wl
                    LEFT JOIN user_list ul ON wl.id = ul.wish_list_id
                    LEFT JOIN user_value_category uvc ON ul.id = uvc.user_list_id
                    LEFT JOIN category c ON uvc.category_id = c.id
                    LEFT JOIN account_address aa ON wl.id_user_address = aa.id
                    WHERE wl.status = ?
                """,
            rs -> {
                Long wishListId = rs.getLong("wl_id");
                WishList wishList = wishListMap.computeIfAbsent(wishListId, id -> {
                    try {
                        return new WishList()
                            .setId(id)
                            .setCreatedAt(rs.getTimestamp("wl_created_at").toLocalDateTime())
                            .setUpdatedAt(rs.getTimestamp("wl_updated_at").toLocalDateTime())
                            .setStatus(Status.byId(rs.getInt("wl_status")))
                            .setUserLists(new ArrayList<>())
                            .setAddress(new AccountAddress()
                                .setId(rs.getLong("aa_id"))
                                .setAddrIndex(rs.getString("aa_addr_index"))
                                .setAddrCity(rs.getString("aa_addr_city"))
                                .setAddrStreet(rs.getString("aa_addr_street"))
                                .setAddrHouse(rs.getString("aa_addr_house"))
                                .setAddrStructure(rs.getString("aa_addr_structure"))
                                .setAddrApart(rs.getString("aa_addr_apart"))
                                .setIsDefault(rs.getObject("aa_is_default", Boolean.class))
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
                                .setListType(TypeList.byId(rs.getInt("ul_list_type")))
                                .setWishList(wishList)
                                .setCategories(new ArrayList<>());
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    });

                    if (!wishList.getUserLists().contains(userList)) {
                        wishList.getUserLists().add(userList);
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

        return new ArrayList<>(wishListMap.values());
    }
}