package ru.pishemzapuskayem.backendbookservice.init;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.pishemzapuskayem.backendbookservice.model.entity.Category;
import ru.pishemzapuskayem.backendbookservice.service.CategoryService;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CategoriesInit implements CommandLineRunner {

    private final CategoryService service;

    @Override
    public void run(String... args) {
        if (service.checkFullCategory()) {
            List<Category> parentList = initCategoryParent();
            for (var item : parentList) {
                item.setId(service.findOrCreate(item).getId());
            }
            List<Category> childList = initCategoryChild(parentList);
            for (var childItem : childList) {
                service.findOrCreate(childItem);
            }
        }
        log.info("Категории добавлены");
    }

    private List<Category> initCategoryParent() {
        return List.of(
                new Category().setName("жанр").setMultiselect(true),
                new Category().setName("область науки").setMultiselect(false),
                new Category().setName("состояние").setMultiselect(false),
                new Category().setName("обложка").setMultiselect(false),
                new Category().setName("лауреат").setMultiselect(false),
                new Category().setName("экранизация").setMultiselect(false),
                new Category().setName("язык издания").setMultiselect(false)
        );
    }

    private List<Category> initCategoryChild(List<Category> parentList) {
        return List.of(
                new Category().setName("ужасы").setParent(parentList.get(0)).setMultiselect(false),
                new Category().setName("детектив").setParent(parentList.get(0)).setMultiselect(false),
                new Category().setName("история").setParent(parentList.get(0)).setMultiselect(false),
                new Category().setName("мемуары").setParent(parentList.get(0)).setMultiselect(false),
                new Category().setName("приключения").setParent(parentList.get(0)).setMultiselect(false),
                new Category().setName("психология").setParent(parentList.get(0)).setMultiselect(false),
                new Category().setName("фантастика").setParent(parentList.get(0)).setMultiselect(false),
                new Category().setName("эзотерика").setParent(parentList.get(0)).setMultiselect(false),

                new Category().setName("религия и богословие").setParent(parentList.get(0)).setMultiselect(false),
                new Category().setName("философия и этика").setParent(parentList.get(0)).setMultiselect(false),
                new Category().setName("биология и этология").setParent(parentList.get(0)).setMultiselect(false),
                new Category().setName("социология").setParent(parentList.get(0)).setMultiselect(false),
                new Category().setName("психология").setParent(parentList.get(0)).setMultiselect(false),
                new Category().setName("наука").setParent(parentList.get(0)).setMultiselect(false),
                new Category().setName("искусство").setParent(parentList.get(0)).setMultiselect(false),
                new Category().setName("экономика").setParent(parentList.get(0)).setMultiselect(false),
                new Category().setName("юриспруденция").setParent(parentList.get(0)).setMultiselect(false),

                new Category().setName("отличное").setParent(parentList.get(0)).setMultiselect(false),
                new Category().setName("хорошее").setParent(parentList.get(0)).setMultiselect(false),
                new Category().setName("потертое").setParent(parentList.get(0)).setMultiselect(false),
                new Category().setName("плохое").setParent(parentList.get(0)).setMultiselect(false),

                new Category().setName("отличное").setParent(parentList.get(0)).setMultiselect(false),
                new Category().setName("хорошее").setParent(parentList.get(0)).setMultiselect(false),
                new Category().setName("потертое").setParent(parentList.get(0)).setMultiselect(false),
                new Category().setName("отсутствует").setParent(parentList.get(0)).setMultiselect(false),

                new Category().setName("присутствует").setParent(parentList.get(0)).setMultiselect(false),
                new Category().setName("отсутствует").setParent(parentList.get(0)).setMultiselect(false),

                new Category().setName("присутствует").setParent(parentList.get(0)).setMultiselect(false),
                new Category().setName("отсутствует").setParent(parentList.get(0)).setMultiselect(false),

                new Category().setName("русский").setParent(parentList.get(0)).setMultiselect(false),
                new Category().setName("английский").setParent(parentList.get(0)).setMultiselect(false),
                new Category().setName("испанский").setParent(parentList.get(0)).setMultiselect(false),
                new Category().setName("японский").setParent(parentList.get(0)).setMultiselect(false),
                new Category().setName("китайский").setParent(parentList.get(0)).setMultiselect(false)
        );
    }
}
