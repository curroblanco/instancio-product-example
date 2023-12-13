package org.example.mother;

import org.example.product.Product;
import org.instancio.Instancio;
import org.instancio.Model;

import java.util.UUID;

import static org.instancio.Select.field;

public class ProductMother implements Mother<Model<Product>> {
    @Override
    public Model<Product> buildDefaultInstance() {
        return Instancio.of(Product.class)
                .set(field(Product::getId), UUID.randomUUID().toString())
                .generate(field(Product::getFirstAvailabilityDate), gen -> gen.temporal().localDate().past())
                .generate(field(Product::getUnPublicationDate), gen -> gen.temporal().localDate().future())
                .generate(field(Product::getPartNumber), gen -> gen.text().pattern("C-#d#d#d#d-#d#d#d#d"))
                .toModel();
    }
}
