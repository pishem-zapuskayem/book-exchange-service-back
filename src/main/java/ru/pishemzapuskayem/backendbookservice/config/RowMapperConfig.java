package ru.pishemzapuskayem.backendbookservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import ru.pishemzapuskayem.backendbookservice.model.entity.Category;
import ru.pishemzapuskayem.backendbookservice.model.entity.OfferList;
import ru.pishemzapuskayem.backendbookservice.model.entity.UserList;
import ru.pishemzapuskayem.backendbookservice.model.entity.UserValueCategory;
import ru.pishemzapuskayem.backendbookservice.model.entity.WishList;

import java.sql.ResultSet;
import java.sql.SQLException;

@Configuration
public class RowMapperConfig {
    @Bean
    public RowMapper<OfferList> offerListRowMapper() {
        return new SafeBeanPropertyRowMapper<>(OfferList.class);
    }

    @Bean
    public RowMapper<WishList> wishListRowMapper() {
        return new SafeBeanPropertyRowMapper<>(WishList.class);
    }

    @Bean
    public RowMapper<UserList> userListRowMapper() {
        return new SafeBeanPropertyRowMapper<>(UserList.class);
    }

    @Bean
    public RowMapper<UserValueCategory> userValueCategoryRowMapper() {
        return new SafeBeanPropertyRowMapper<>(UserValueCategory.class);
    }

    @Bean
    public RowMapper<Category> categoryRowMapper() {
        return new SafeBeanPropertyRowMapper<>(Category.class);
    }

    public static class SafeBeanPropertyRowMapper<T> implements RowMapper<T> {
        private final BeanPropertyRowMapper<T> delegate;

        public SafeBeanPropertyRowMapper(Class<T> mappedClass) {
            this.delegate = new BeanPropertyRowMapper<>(mappedClass);
        }

        @Override
        public T mapRow(ResultSet rs, int rowNum) {
            try {
                return delegate.mapRow(rs, rowNum);
            } catch (SQLException e) {
                return null;
            }
        }
    }
}
